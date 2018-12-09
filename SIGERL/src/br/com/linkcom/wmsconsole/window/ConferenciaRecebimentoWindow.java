package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculo;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculostatus;
import br.com.linkcom.wms.geral.bean.Conferenciaordemrecebimento;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtocodigobarras;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Produtotipopalete;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Recebimentonotafiscal;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;
import br.com.linkcom.wms.geral.service.AcompanhamentoveiculoService;
import br.com.linkcom.wms.geral.service.AcompanhamentoveiculohistoricoService;
import br.com.linkcom.wms.geral.service.ConferenciaordemrecebimentoService;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.OperacaoNegadaException;
import br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoService;
import br.com.linkcom.wms.geral.service.OrdemservicousuarioService;
import br.com.linkcom.wms.geral.service.ProdutoService;
import br.com.linkcom.wms.geral.service.ProdutoembalagemService;
import br.com.linkcom.wms.geral.service.ProdutotipopaleteService;
import br.com.linkcom.wms.geral.service.RecebimentoService;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.ConferenciaCegaPapelFiltro;
import br.com.linkcom.wms.util.InsercaoInvalidaException;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wmsconsole.system.ExecucaoOSWindow;
import net.wimpi.telnetd.io.terminal.ColorHelper;
import net.wimpi.telnetd.io.toolkit.Editfield;
import net.wimpi.telnetd.io.toolkit.InputValidator;

/**
 * Monta a de conferência cega no recebimento
 * 
 * @author Pedro Gonçalves
 */
public class ConferenciaRecebimentoWindow extends ExecucaoOSWindow {
	
	private static final String TITULO = "Conferência de recebimento";
	
	protected Recebimento recebimento;
	protected List<Ordemprodutohistorico> listaOPH;
	protected String veiculo;
	protected Boolean coletapadrao = true;
	protected Boolean coletaquantidade = true;
	protected boolean abort = false;
	protected Ordemservico ordemServico;
	protected OrdemservicoUsuario ordemservicoUsuario;
	protected boolean cancelProduct = false;
	protected boolean next = false;
	protected boolean isconcluido = false;
	protected TipoColeta tipoColeta = TipoColeta.FRACIONADA;
	protected AcompanhamentoveiculohistoricoService acompanhamentoveiculohistoricoService = AcompanhamentoveiculohistoricoService.getInstance();
	protected AcompanhamentoveiculoService acompanhamentoveiculoService = AcompanhamentoveiculoService.getInstance();
	protected ConferenciaordemrecebimentoService conferenciaOrdemRecebimentoService = ConferenciaordemrecebimentoService.getInstance();
	protected List<Acompanhamentoveiculo> listaAcompanhamentoVeiculo;
	protected List<Conferenciaordemrecebimento> listaconferencias;
	
	/**
	 * Fluxo inicial. Primeiro método a ser chamado.
	 * 
	 * Exibe na tela a mensagem para o usuário digitar o box
	 * @see #findColetor(String numero)
	 * 
	 * Caso possua um recebimento para este box, é mostrado as informações do recebimento.
	 * @see #showInformationsRecebimento()
	 * 
	 * Depois de apresentada as informações o menu de coleta é colocado.
	 * @see #makeMenuColeta()
	 * 
	 * Em seguida dá início a coleta dos produtos do recebimento
	 * @see #startCollectProduct()
	 * 
	 * Este método que carrega a lista que será coletada.
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#findByForRF(Recebimento recebimento)
	 * 
	 */
	public void draw() throws IOException{
		int total = 0;
		do{
			drawEsqueleto("Digite 0 para sair.");
			Integer valor = readInteger("Recebimento: ");
			if(valor != null && valor.equals(0))
				break;
			
			if(findColetor(valor)){
				executarOrdem(this.ordemServico);
				total = -1;
			} else {
				writeOnCenter("Recebimento indisponível.", ColorHelper.RED, false, false);
				writeLine("");
				writeOnCenter("[ "+(total + 1)+"/3 ]", null, false, true);
			}
			
			total ++;
			
			if(total == 3)
				break;
			
		} while (true);
		
	}
	
	@Override
	public String getTitulo() {
		return TITULO;
	}

	@Override
	public void executarOrdem(Ordemservico ordemservico) throws IOException {
		if (this.ordemServico != ordemservico){
			this.recebimento = RecebimentoService.getInstance().findRecebimentoByBoxRF(ordemservico.getRecebimento(),deposito);;		
			this.ordemServico = OrdemservicoService.getInstance().loadLastConferencia(recebimento);
			this.ordemservicoUsuario = OrdemservicousuarioService.getInstance().findByOrdemservico(this.ordemServico);
		}
		
		showInformationsRecebimento();
		
		read();
		this.listaOPH = OrdemprodutohistoricoService.getInstance().findByForRF(recebimento);

		//Inicia no modo de coleta fracionada
		this.tipoColeta = TipoColeta.FRACIONADA;
		
		if(abort) {
			if(isconcluido){
				atualizaRecebimentoStatus();
				alertConclusaoRecebimento();
				isconcluido = false;
			}
			abort = false;
			return;
		}
		
		try{
			startCollectProduct();
		}catch (WmsException e) {
			
			getTermIO().eraseScreen();
			alertError( e.getMessage());	
			
			recebimento = null;
			listaOPH = null;
			veiculo = null;
			this.tipoColeta = TipoColeta.FRACIONADA;
			abort = false;
			ordemServico = null;
			ordemservicoUsuario = null;
			cancelProduct = false;
			next = false;
			isconcluido = false;
		}
		//necessário pois o menu apresenta duas vezes.
		if(abort) {
			if(isconcluido){
				atualizaRecebimentoStatus();
				alertConclusaoRecebimento();
				isconcluido = false;
			}
			abort = false;
			return;
		}
	}
	
	/**
	 * Inicializa a coleta dos produtos.
	 * 
	 * Caso o usuário tenha digitado 0 na área destinada a coleta de produto, é mostrado o menu de coleta novamente.
	 * @see #makeMenuColeta()
	 * 
	 * Caso o produto bipado pertença a osp, é mostrado na tela as informações deste produto e inicializa a coleta dos dados.
	 * @see #produtoPertence(String)
	 * @see #showInfoProduto(Ordemservicoproduto)
	 * @see #collectQte(Ordemservicoproduto)
	 * 
	 * Caso o produto tenha sido alterado o seu tipo de palete, reexibir o valor modificado.
	 * @see #produtoInformations(Ordemservicoproduto)
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#findBy(Recebimento recebimento)
	 * 
	 * @see #verificaStatusRecebimento()
	 */
	protected void startCollectProduct() throws IOException {
		
		boolean menu = false;
		do {
			if(menu) {
				makeMenuColeta();
				menu = false;
				if(abort)
					break;
				
			}
			
			prepararTela();
			
			//verifica se o produto pertence a osp.
			Ordemprodutohistorico ordemProdutoHistorico = biparProduto(this.listaOPH);
			
			if(exibirMenu) {
				menu = true;
				continue;
			}
			
			if (showAlertFinalizado())
				break;
			
			writeLine("");
			writeLine("");
			
			if(ordemProdutoHistorico != null){
				try {
					
					if(ordemProdutoHistorico.getOrdemservicoproduto().getProduto().getModificado() == null || !ordemProdutoHistorico.getOrdemservicoproduto().getProduto().getModificado()){
						//carrega o produto caso não tenha sido carregado ainda.
						ordemProdutoHistorico.getOrdemservicoproduto().setProduto(ProdutoService.getInstance().findDadosLogisticosProdutoRF(ordemProdutoHistorico.getOrdemservicoproduto().getProduto(), deposito));
						ordemProdutoHistorico.getOrdemservicoproduto().getProduto().setModificado(true);
					}
					
					//mostra as informações do produto
					produtoInformations(ordemProdutoHistorico);
					Boolean alterado = collectQte(ordemProdutoHistorico); //coleta das quantidades
					if(alterado) {
						produtoInformations(ordemProdutoHistorico);
						if(tipoColeta.equals(TipoColeta.PADRAO)){
							writeLine("Qtde. Emb.: " + ordemProdutoHistorico.getQtdeColetada());
						} else if(tipoColeta.equals(TipoColeta.FRACIONADA)){
							writeLine("Qtde. Frac.: " + ordemProdutoHistorico.getQtdefracionada());
						} else 
							writeLine("Avariado: " + ordemProdutoHistorico.getQtdeavaria());
						
						read();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				alertError("Produto não pertence ao lote.");
			}
			
		} while (true);
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	protected void prepararTela() throws IOException {
		drawEsqueleto("WMS CONSOLE - " + recebimento.getCdrecebimento(), "Digite 0 para ações.");
		writeOnCenter(getTitulo(), null, false, false);
		writeOnCenter(tipoColeta + " - O.S.: " + ordemServico.getCdordemservico(), null, false, false);
		writeSeparator();
		writeLine("");		
	}

	/**
	 * Valida se o recebimento já foi finalizado. Caso tenha sido finalizado exibe uma mensagem para o usuário.
	 * 
	 * @return
	 * @throws IOException
	 */
	protected boolean showAlertFinalizado() throws IOException {
		atualizaRecebimentoStatus();	
		if((!recebimento.getRecebimentostatus().equals(Recebimentostatus.EM_ANDAMENTO) && 
				!recebimento.getRecebimentostatus().equals(Recebimentostatus.EM_ENDERECAMENTO) && 
				!recebimento.getRecebimentostatus().equals(Recebimentostatus.ENDERECADO)
			) || Ordemstatus.FINALIZADO_DIVERGENCIA.equals(this.ordemServico.getOrdemstatus()) || 
				Ordemstatus.FINALIZADO_SUCESSO.equals(this.ordemServico.getOrdemstatus()) || 
				Ordemstatus.AGUARDANDO_CONFIRMACAO.equals(this.ordemServico.getOrdemstatus())){
			
			alertError("Recebimento encerrado pela mesa de operações.", true);
			return true;
		}else
			return false;
	}

	/**
	 * Atualiza o status do recebimento e da ordem de serviço.
	 * 
	 * Caso o recebimento seja finalizado pela mesa de operações, o coletor é impedido
	 * de trabalhar com o mesmo.
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @return
	 * @throws IOException 
	 */
	public void atualizaRecebimentoStatus() throws IOException {
		Recebimento aux = RecebimentoService.getInstance().load(this.recebimento);
		if(aux != null && aux.getRecebimentostatus() != null && aux.getRecebimentostatus().getCdrecebimentostatus() != null)
			this.recebimento.setRecebimentostatus(aux.getRecebimentostatus());
		
		Ordemservico osAux = OrdemservicoService.getInstance().load(this.ordemServico);
		if(osAux != null && osAux.getOrdemstatus() != null && 
				(Ordemstatus.FINALIZADO_DIVERGENCIA.equals(osAux.getOrdemstatus()) || 
						Ordemstatus.FINALIZADO_SUCESSO.equals(osAux.getOrdemstatus()) || 
						Ordemstatus.AGUARDANDO_CONFIRMACAO.equals(osAux.getOrdemstatus()))){
			
			this.ordemServico.setOrdemstatus(osAux.getOrdemstatus());
		}
		
	}

	/**
	 * Avisa ao usuário como o recebimento foi concluído
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @throws IOException 
	 * 
	 */
	protected void alertConclusaoRecebimento() throws IOException {
		if(ordemServico != null && ordemServico.getOrdemstatus() != null && ordemServico.getOrdemstatus().getCdordemstatus() != null){
			if(ordemServico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_SUCESSO)){
				writeOnCenter("Recebimento OK.", null, true, true);
			}
			else if(ordemServico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_DIVERGENCIA) || ordemServico.getOrdemstatus().equals(Ordemstatus.AGUARDANDO_CONFIRMACAO)){
				writeOnCenter("Recebimento com divergências.", null, true, true);
			}
			
			try{
				List<Acompanhamentoveiculo> lista = acompanhamentoveiculoService.findByRecebimento(recebimento);
				if(lista!=null && !lista.isEmpty()){
					for (Acompanhamentoveiculo av : lista){
						av.setAcompanhamentoveiculostatus(Acompanhamentoveiculostatus.RECEBIMENTO_FINALIZADO);
						acompanhamentoveiculoService.atualizaStatus(av,Acompanhamentoveiculostatus.RECEBIMENTO_FINALIZADO);
						acompanhamentoveiculohistoricoService.criarHistorico(av, null, usuario);	
					}
				}
			}catch (Exception e) {
				alertError("Erro ao adicionar o histórico ao RAV. Tente novamente.", true);
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * Mostra as informações do produto.
	 * 
	 * @param produtoPertence
	 * @throws IOException
	 */
	protected void produtoInformations(Ordemprodutohistorico produtoPertence) throws IOException{
		boolean showInfoProduto = showInfoProduto(produtoPertence);
		if(showInfoProduto){
			writeOnCenter("Dados logísticos incompletos", ColorHelper.RED, false, false);
			writeSeparator();
		} 
	}

	/**
	 * Exibe as informações do produto da osp bipada.
	 * 
	 * @see #getEmbalagemCompra(Produto)
	 * 
	 * @param produtoPertence
	 * @return false - Caso os dados logísticos estejam ok.
	 *         true - Caso falte algum dado logístico.
	 */
	private boolean showInfoProduto(Ordemprodutohistorico produtoPertence) throws IOException {
		boolean error = false;
		
		Produto produto = produtoPertence.getOrdemservicoproduto().getProduto();
		prepararTela();
		writeLine("Recebimento: " + recebimento.getCdrecebimento());
		writeLine("Veículo: " + veiculo);
		writeSeparator();

		String codigoP = "";
		try {
			codigoP = produto.getCodigo();
		} catch (Exception e) {error=true;}
		
		writeLine("Código: " + codigoP);
		if (produto.getProdutoprincipal() != null){
			writeLine("Descrição: " + produto.getProdutoprincipal().getDescricao());
			writeLine("Volume: " + produto.getComplementocodigobarras().substring(0, 2) + "/" + 
					produto.getComplementocodigobarras().substring(2));
		}else
			writeLine("Descrição: " + produto.getDescricao());
		
		
		if (tipoColeta.equals(TipoColeta.PADRAO)){
			String embalagem = "";
			try {
				Produtoembalagem embalagemCompra = ProdutoembalagemService.getInstance().getEmbalagemCompra(produto);
				embalagem = embalagemCompra.getDescricao() + " " + embalagemCompra.getQtde();
			} catch (Exception e) {error=true;}
			
			writeLine("Embalagem: " + embalagem);
		}
		
		if(produtoPertence.getCodigoBarrasIndex() != null){
			String codigobarras = "";
			try {
				codigobarras = produto.getListaProdutoCodigoDeBarras().get(produtoPertence.getCodigoBarrasIndex()).getCodigo();
			} catch (Exception e) {error=true;}
			
			writeLine("Código barras: " + codigobarras);
		}
		String norma = "";
		String tipopalete = "";
		try {
			List<Produtotipopalete> lista = ProdutotipopaleteService.getInstance().findByProduto(produto,deposito);
			norma = produto.getListaProdutoTipoPalete().get(0).getLastro() + " x " + produto.getListaProdutoTipoPalete().get(0).getCamada();
			//verifica se possui algum alterado
			for (Produtotipopalete produtotipopalete : lista) {
				if(produtotipopalete.getAlterado() != null && produtotipopalete.getAlterado()){
					tipopalete = produtotipopalete.getTipopalete().getNome() + " *";
					norma = produtotipopalete.getLastro() + " x "+produtotipopalete.getCamada() + " *";
					//ao processar a OSP, salvar tbm o tipo de palete.
					produtoPertence.getOrdemservicoproduto().setTipopalete(produtotipopalete.getTipopalete());
				}
			}
			//se não foi alterado ainda, carregar o default
			if("".equals(tipopalete)){
				for (Produtotipopalete produtotipopalete : lista) {
					if(produtotipopalete.getPadrao() != null && produtotipopalete.getPadrao()){
						tipopalete = produtotipopalete.getTipopalete().getNome();
						norma = produtotipopalete.getLastro() + " x "+produtotipopalete.getCamada();
						produtoPertence.getOrdemservicoproduto().setTipopalete(produtotipopalete.getTipopalete());
					}
				}
			}
		} catch (Exception e) {error=true;}
		
		writeLine("Tipo palete: "+ tipopalete);
		writeLine("Norma: "+norma);
		writeSeparator();
		
		return error;
	}
	
	/**
	 * Pega a embalagem de compra do produto. Caso o produto não possua uma embalagem para recebimento 
	 * é retornado null.
	 * 
	 * @param produto
	 * @return
	 */
	private Produtoembalagem getEmbalagemCompra(Produto produto){
		List<Produtoembalagem> listaProdutoEmbalagem = produto.getListaProdutoEmbalagem();
		for (Produtoembalagem produtoembalagem : listaProdutoEmbalagem) {
			if(produtoembalagem.getCompra() != null && produtoembalagem.getCompra())
				return produtoembalagem;
		}
		return null;
	}
		
	/**
	 * Faz a coleta da quantidade de acordo com o padrão.
	 * 
	 * Modo: 
	 * & Quantidade
	 * 	 Coleta padrão: Pega a quantidade de produtos por embalagem e multiplica.
	 *   Coleta fracionada: Quantidade é incrementada diretamente.
	 *  
	 * & Avaria
	 *   Coleta fracionada: (*Somente possui este modo para o modo de coleta de avarias.)
	 *
	 * Se o produto possui mais de um tipo de palete cadastrado, é dado a opção de alterar o tipo de palete.
	 * @see #makeMenuTipoPalete(Produto)
	 * 
	 * @param ordemProdutoHistorico
	 * @throws IOException
	 */
	protected boolean collectQte(Ordemprodutohistorico ordemProdutoHistorico) throws IOException{
		Produto produto = ordemProdutoHistorico.getOrdemservicoproduto().getProduto();
		Produtoembalagem embalagem = null;

		if (ordemProdutoHistorico.getCodigoBarrasIndex() != null){
			Produtocodigobarras produtocodigobarras = (Produtocodigobarras) CollectionUtils.find(produto.getListaProdutoCodigoDeBarras(), 
					new BeanPropertyValueEqualsPredicate("codigo", ordemProdutoHistorico.getCodigoBarrasConferencia()));
			embalagem = produtocodigobarras.getProdutoembalagem();
		}else{
			embalagem = ProdutoembalagemService.getInstance().getEmbalagemCompra(produto);
		}

		//entrada de dados para a quantidade
		if(embalagem == null){
			//produto não possui embalagem de recebimento. e será coletado neste modo de coleta. Pressione enter para continuar
			writeLine("Este produto não possui embalagem de recebimento. ");
			writeLine("");
			writeLine("Pressione ENTER para continuar");
			read();

			return false;
		}

		Long qtderecebida = recuperaQuantidadePorTipoColeta(this.tipoColeta, ordemProdutoHistorico, embalagem.getCompra());

		if (this.listaconferencias == null || this.listaconferencias.isEmpty()){
			List<Conferenciaordemrecebimento> conferenciasAux = conferenciaOrdemRecebimentoService.recuperaConferenciasPorRecebimento(this.recebimento); 

			if (conferenciasAux != null && !conferenciasAux.isEmpty())
				this.listaconferencias = conferenciasAux;
			else
				this.listaconferencias = new ArrayList<Conferenciaordemrecebimento>();
		}

		@SuppressWarnings("unchecked")
		List<Conferenciaordemrecebimento> conferenciasOrdem = (List<Conferenciaordemrecebimento>) CollectionUtils.select(this.listaconferencias
				,new BeanPropertyValueEqualsPredicate("ordemprodutohistorico.cdordemprodutohistorico"
						,ordemProdutoHistorico.getCdordemprodutohistorico()));

		Long qtdeEmbalagem = null;

		long coletado = NumberUtils.LONG_ONE.longValue();

		if (!ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETA_QUANT_AUTO_RECEB, this.deposito)){
			coletado = readInteger("Quantidade: ");
			ordemProdutoHistorico.setQtdeColetada(coletado);
		}

		if (embalagem.getQtde() != null)
			qtdeEmbalagem = embalagem.getQtde();
		else
			qtdeEmbalagem = NumberUtils.LONG_ONE;

		Conferenciaordemrecebimento conferencia = null; 

		if (conferenciasOrdem != null && !conferenciasOrdem.isEmpty())	
			conferencia = (Conferenciaordemrecebimento) CollectionUtils.find(conferenciasOrdem
					,new BeanPropertyValueEqualsPredicate("produtoembalagem.cdprodutoembalagem"
							,embalagem.getCdprodutoembalagem()));

		Boolean usaMesmaConferencia = conferencia != null 
				&& conferencia.getIscoletaavaria().equals(new Boolean(TipoColeta.AVARIA.equals(this.tipoColeta)));

		if (!usaMesmaConferencia){
			this.listaconferencias.add(conferenciaOrdemRecebimentoService.createConferenciaOrdem(ordemProdutoHistorico, 
					this.recebimento, 
					calculaQtdeProduto(coletado, qtderecebida, qtdeEmbalagem), 
					TipoColeta.AVARIA.equals(this.tipoColeta), 
					embalagem));
		}else{
			conferencia.setQtde((conferencia.getQtde() - qtderecebida) + calculaQtdeProduto(coletado, qtderecebida, qtdeEmbalagem));
			conferenciaOrdemRecebimentoService.saveOrUpdateNoUseTransaction(conferencia);
		}

		writeLine("");
		writeLine("");

		if (produto.getProdutoprincipal() != null) 
			produto = produto.getProdutoprincipal();

		if(produto.getListaProdutoTipoPalete() != null && 
				produto.getListaProdutoTipoPalete().size() > 1 ){
			if (confirmAction("Trocar o palete? ")){
				makeMenuTipoPalete(produto);
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Recupera quantidade de acordo com o tipo coleta que está sendo realizado.
	 *
	 * @param tipoColeta the tipo coleta
	 * @param ordemProdutoHistorico the ordemprodutohistorico
	 * @param isEmbalagemRecebimento the is embalagem recebimento
	 * @return the long
	 */
	public Long recuperaQuantidadePorTipoColeta(TipoColeta tipoColeta, Ordemprodutohistorico ordemProdutoHistorico, 
			Boolean isEmbalagemRecebimento){
		Long qtde = null;
		
		if (TipoColeta.AVARIA.equals(tipoColeta)){
			qtde = ordemProdutoHistorico.getQtdeavaria();
		}else if (isEmbalagemRecebimento){
			qtde = ordemProdutoHistorico.getQtde();
		}else{
			qtde = ordemProdutoHistorico.getQtdefracionada();
		}
		
		if (qtde == null)
			qtde = NumberUtils.LONG_ZERO;
		
		return qtde;
	}
	
	
	/**
	 * Calcula a quantidade coletada para o produto.
	 *
	 * @param coletado the coletado
	 * @param qtdeRecebida the qtde recebida
	 * @param qtdeEmbalagem the qtde embalagem
	 * @return the long
	 */
	public Long calculaQtdeProduto(long coletado, Long qtdeRecebida, Long qtdeEmbalagem){
		return (qtdeEmbalagem * coletado) + qtdeRecebida;
	}

	/**
	 * Procura o recebimento em aberto a partir do número do Box.
	 * 
	 * @see br.com.linkcom.wms.geral.service.RecebimentoService#findRecebimentoByBoxRF(String box)
	 * @see #recebimento
	 * 
	 * Por medidas de segurança, caso o usuário tenha perdido conexao com o coletor, 
	 * somente ele poderá voltar ao recebimento que estava coletando, portanto verifica se tem já
	 * alguma ordem servico anexada a ele.
	 * @see #verificaOsAberta()
	 * 
	 * Valida se o usuário pode acessar esta ordem de serviço.
	 * @see #validateOS()
	 * 
	 * Caso esse recebimento não tenha sido anexado a ninguém, é amarrado ao usuário esta ordem de
	 * serviço.
	 * @see #associarUsuarioOS()
	 * 
	 * @param numero
	 * @return true - Caso o recebimento foi encontrado na base de dados. Sera a propriedade recebimento.
	 * 		   false - O recebimento não foi encontrado.
	 * @throws IOException 
	 */
	private boolean findColetor(Integer numero) throws IOException{
		if(numero == null || numero.equals(0))
			return false;
		
		Recebimento findRecebimentoByBoxRF = new Recebimento(numero); 
		findRecebimentoByBoxRF = RecebimentoService.getInstance().findRecebimentoByBoxRF(findRecebimentoByBoxRF,deposito);
		if(findRecebimentoByBoxRF == null)
			return false;
		
		this.recebimento = findRecebimentoByBoxRF;
		listaAcompanhamentoVeiculo = acompanhamentoveiculoService.findByRecebimento(findRecebimentoByBoxRF);	
		
		if(listaAcompanhamentoVeiculo != null && !listaAcompanhamentoVeiculo.isEmpty()) {
			for (Acompanhamentoveiculo acompanhamentoveiculo : listaAcompanhamentoVeiculo) {
				if(acompanhamentoveiculo.getDataentrada() == null){
					writeOnCenter("A RAV: "+acompanhamentoveiculo.getCdacompanhamentoveiculo()+" não teve a sua entrada confirmada."
							+"\nPor favor, confirme a entrada do veículo"
							+"\nantes de iniciar a conferência.", 
							ColorHelper.RED, false, false);
					return false;
				}
			}
		}
		
		this.ordemServico = OrdemservicoService.getInstance().loadLastConferencia(recebimento);
		if(!validateOS())
			return false;
			
		if(!verificaOsAberta())
			return false;
		
		
		associarUsuarioOS();
		
		return true;
	}
	
	/**
	 * Valida se a ordem de serviço pode ser acessada por esta tela.
	 * 
	 * @return true - caso possa ser acessada.
	 * 		   false - não possa ser acessada.
	 */
	protected boolean validateOS(){
		if(ordemServico == null || ordemServico.getOrdem() > 1 
				|| (!Ordemstatus.EM_ABERTO.equals(ordemServico.getOrdemstatus()) && 
						!Ordemstatus.EM_EXECUCAO.equals(ordemServico.getOrdemstatus())))
			return false;
		
		return true;
	}
	
	/**
	 * Verifica se o usuário pode coletar esta OS. Caso o usuário tenha perdido a conexão, ele pode
	 * reconectar-se a mesma os.
	 * 
	 * @return true - Caso este usuário possa coletar esta OS.
	 *         false - O usuário não pode coletar essa OS.
	 */
	private Boolean verificaOsAberta(){
		this.ordemservicoUsuario = null;
		OrdemservicoUsuario loadByRecebimento = OrdemservicousuarioService.getInstance().findByOrdemservico(this.ordemServico);
		if(loadByRecebimento == null)
			return true;
		else{
			if(loadByRecebimento.getUsuario().getCdpessoa().equals(usuario.getCdpessoa())){
				this.ordemservicoUsuario = loadByRecebimento;
				return true;
			} else { 
				this.recebimento = null;
				return false;
			}
		}
	}
	
	/**
	 * Associa o usuário que está logado no box do recebimento, e amarra ele a última ordem de serviço.
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicousuarioService#salvarOrdemServicoUsuarioForConferencia(ConferenciaCegaPapelFiltro filtro)
	 */
	private void associarUsuarioOS(){
		if (recebimento.getRecebimentostatus() == null || recebimento.getRecebimentostatus().equals(Recebimentostatus.DISPONIVEL)){
			recebimento.setRecebimentostatus(Recebimentostatus.EM_ANDAMENTO);
			RecebimentoService.getInstance().gravaStatusRecebimento(recebimento);
		}
		
		if(ordemservicoUsuario == null) {
			
			OrdemservicoUsuario ordemservicoUsuario = new OrdemservicoUsuario();
			
			Ordemservico loadLastButOne = OrdemservicoService.getInstance().loadLastConferencia(recebimento);
			ordemservicoUsuario.setOrdemservico(loadLastButOne);
			ordemservicoUsuario.setUsuario(usuario);
			ordemservicoUsuario.setDtinicio(new Timestamp(System.currentTimeMillis()));
			ordemservicoUsuario.setDtfim(null);
			
			OrdemservicousuarioService.getInstance().saveOrUpdate(ordemservicoUsuario);
			
			ordemServico.setOrdemstatus(Ordemstatus.EM_EXECUCAO);
			OrdemservicoService.getInstance().atualizarStatusordemservico(ordemServico);
			
			this.ordemservicoUsuario = ordemservicoUsuario;
		}
	}
	 
	/**
	 * Exibe as informações do recebimento que foi encontrado na tela.
	 * 
	 * @throws IOException
	 * @author Pedro Gonçalves
	 */
	private void showInformationsRecebimento() throws IOException {
		Recebimentonotafiscal next = recebimento.getListaRecebimentoNF().iterator().next();
		this.veiculo = next.getNotafiscalentrada().getVeiculo();

		drawEsqueleto("");
		writeOnCenter(getTitulo(), null, false, false);
		writeSeparator();
		writeLine("");
		writeLine("Recebimento: " + recebimento.getCdrecebimento());
		writeLine("");
		writeLine("O.S.: " + ordemServico.getCdordemservico());
		writeLine("");
		writeLine("Box: " + recebimento.getBox().getNome());
		writeLine("");
		writeLine("Veículo: " + veiculo);
		writeLine("");
	}
	
	/**
	 * Monta o menu de coleta.
	 * <ul>
	 * 		<li><b>0</b> - Cancelar - O usuário cancela a operação e sai da coleta. O sistema
	 * desassocia ele da os e limpa todas as coletas já efetuadas.
	 * @see #desassociarOSU()</li>
	 * 		<li><b>1</b> - Encerrar coleta - O usuário finaliza a coleta. O sistema processa os dados coletados
	 * @see #processarOSP()
	 * </li>
	 * 		<li><b>2</b> - Coleta fracionada - Muda o modo para coleta fracionada. É o tipo de coleta que não leva em 
	 * consideração a embalagem, apenas a quantidade unitária do produto. (Incrementa o campo qtdrecebida)</li>
	 * 		<li><b>3</b> - Coleta padrão - Modo de coleta que pega a quantidade digitada, e multiplica pela qtde que possui
	 * em cada embalagem.</li>
	 * 		<li><b>4</b> - Coleta de avaria - Modo de coleta que incrementa a qtde avaria. é o mesmo que coleta fracionada, porém incrementa
	 * somente o campo qtdeavaria.</li>
	 * 
	 * @author Pedro Gonçalves
	 */
	protected void makeMenuColeta() throws IOException {
		drawEsqueleto("");
		writeLine("Selecione uma ação: ");
		writeLine("");
		
		final HashMap<Integer, String> mapaMenu = new LinkedHashMap<Integer, String>();

		mapaMenu.put(0, "Voltar");
		
		if (!isConvocacaoAtiva())
			mapaMenu.put(1, "Trocar de carregamento");
		else
			mapaMenu.put(1, "Sair");

		mapaMenu.put(2, "Cancelar coleta");
		mapaMenu.put(3, "Finalizar coleta");

		mapaMenu.put(-1, " ");
		
		//mapaMenu.put(1, "Col. padrão");
		mapaMenu.put(4, "Col. fracionada");
		mapaMenu.put(5, "Col. de avaria");
		
		if(cancelProduct){
			mapaMenu.put(6, "Não coletar produto");
		}
		
		Set<Entry<Integer, String>> entrySet = mapaMenu.entrySet();
		for (Entry<Integer, String> entry : entrySet) {
			if (entry.getKey() >= 0)
				writeLine(entry.getKey() + " - " + entry.getValue());
			else
				writeLine(entry.getValue());
		}
		
		writeLine("");
		writeLine("");
		
		Editfield optDep=new Editfield(getTermIO(),"editfield 3",2);
		optDep.registerInputValidator(new InputValidator(){
			public boolean validate(String str) {
				if(str == null || str.equals(""))
					return true;
				
				try{
					int option = Integer.parseInt(str);
					if(option <= 9 && option >= 0){
						return true;
					}
				} catch (Exception e){
					return false;
				}
				return false;
			}
		});
		
		optDep.run();
		
		String value = optDep.getValue();
		
		if(value == null || value.equals("")){
			makeMenuColeta();
			return;
		}
		
		abort = false;
//		if(value.equals("1")){
//			//seta a coleta padrão
//			this.coletapadrao = true;
//			this.coletaquantidade = true;
//			
//		} else 
		
		if(cancelProduct && "6".equals(value)){
			next = true;
		} else if("5".equals(value)){
			this.tipoColeta = TipoColeta.AVARIA;
		} else if ("4".equals(value)){
			//quando a coleta é fracionada
			this.tipoColeta = TipoColeta.FRACIONADA;
		} else if("3".equals(value)){
			System.out.println("Processa as OSP");			
			try{
				processarOSP();
				isconcluido = true;
				abort = true;
			}catch (OperacaoNegadaException e) {
				alertError( e.getMessage(), true);

				makeMenuColeta();
			}
		} else if("2".equals(value)){
			desassociarOSU();
			abort = true;
		} else if("1".equals(value)){
			if (confirmAction("Tem certeza deseja sair?")){
				configurarOSUDataFim();
				//associar conferente as ordens
				abort = true;
				if (isConvocacaoAtiva())
					logout();
			}
		} else if("0".equals(value)){
			return;
		} else {
			alertError("Opção inválida.", false);
			makeMenuColeta();
		}
		
	}
	
	/**
	 * Desassocia a ordem de serviço do usuário logado no coletor.
	 * @throws IOException 
	 */
	private void desassociarOSU() throws IOException {
		if (!isOrdemAindaAberta(ordemServico)){
			return;
		}
		
		OrdemservicoprodutoService.getInstance().resetarQuantidades(ordemServico);
		OrdemservicousuarioService.getInstance().delete(ordemservicoUsuario);
		
		ordemServico.setOrdemstatus(Ordemstatus.EM_ABERTO);
		OrdemservicoService.getInstance().atualizarStatusordemservico(ordemServico);
	}

	/**
	 * Processa a lista de ordem servico produto.
	 * @throws IOException 
	 */
	protected void processarOSP() throws IOException {
		configurarOSUDataFim();
		
		// Registra os valores coletados, na ordemprodutoHistorico
		atualizaQtdeOrdemProdutoHistorico();
		
		//Calculando o número de embalagens padrão recebidas
		/*
		 * Comentado devido ao calculo agora ser realizado na coleta da quantidade do produto, pela embalagem.
		 * Everton Reis - 19/04/2017
		 * 
		 * for (Ordemprodutohistorico oph : listaOPH){
			Produtoembalagem produtoembalagem = ProdutoembalagemService.getInstance().getEmbalagemCompra(oph.getOrdemservicoproduto().getProduto());
			if (produtoembalagem != null && oph.getQtdefracionada() != null){
				long embalagens = oph.getQtdefracionada() / produtoembalagem.getQtde();
				long qtdePadrao = embalagens * produtoembalagem.getQtde();
				if (qtdePadrao > 0){
					oph.setQtdefracionada(oph.getQtdefracionada() - qtdePadrao);
					oph.setQtde(qtdePadrao);
				}
			}
		}*/
		
		final ConferenciaCegaPapelFiltro filtro = new ConferenciaCegaPapelFiltro();
		filtro.setSkipSaveOSU(true);
		//filtro.setOrdemservico(ordemservico);
		recebimento.setDeposito(deposito);
		filtro.setRecebimento(recebimento);
		filtro.setListaOrdemProdutoHistorico(new ListSet<Ordemprodutohistorico>(Ordemprodutohistorico.class,listaOPH));
		filtro.setGeraReconferencia(true);
		filtro.setUsuariofinalizacao(usuario);
		
		Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				try {
					OrdemservicoprodutoService.getInstance().salvarListaOrdemServicoProdutoForConferencia(filtro,true);
				} catch (InsercaoInvalidaException e) {
					throw new WmsException(e.getMessage(), e);
				}
				return null;
			}
		});
	}
	
	/**
	 * Atualiza as quantidades coletadas anteriormente no registro de ordemProdutoHistorico.
	 */
	private void atualizaQtdeOrdemProdutoHistorico() {
		if (this.listaconferencias == null || this.listaconferencias.isEmpty()){
			this.listaconferencias = conferenciaOrdemRecebimentoService.recuperaConferenciasPorRecebimento(this.recebimento); 
		}   
		
		if (this.listaconferencias != null && !this.listaconferencias.isEmpty()){
			for (Ordemprodutohistorico oph : this.listaOPH) {
				
				@SuppressWarnings("unchecked")
				List<Conferenciaordemrecebimento> conferenciasPorOrdem = (List<Conferenciaordemrecebimento>) CollectionUtils.select(this.listaconferencias
						,new BeanPropertyValueEqualsPredicate("ordemprodutohistorico.cdordemprodutohistorico"
								,oph.getCdordemprodutohistorico()));
				
				if (conferenciasPorOrdem != null){
					for (Conferenciaordemrecebimento conferenciaordemrecebimento : conferenciasPorOrdem) {
						if (conferenciaordemrecebimento.getIscoletaavaria()){
							oph.setQtdeavaria(conferenciaordemrecebimento.getQtde());
						}else if (conferenciaordemrecebimento.getProdutoembalagem().getCompra()){
							oph.setQtde(conferenciaordemrecebimento.getQtde());
						}else{
							oph.setQtdefracionada(conferenciaordemrecebimento.getQtde());
						}
						
						conferenciaordemrecebimento.setConferenciafinalizada(Boolean.TRUE);
						conferenciaOrdemRecebimentoService.saveOrUpdateNoUseTransaction(conferenciaordemrecebimento);
					}
					
					OrdemprodutohistoricoService.getInstance().atualizarQuantidades(oph);
				}
			}
		}
	}

	/**
	 * Finaliza a data fim da ordemservicousuario
	 * 
	 */
	private void configurarOSUDataFim() {
		ordemservicoUsuario.setDtfim(new Timestamp(System.currentTimeMillis()));
		OrdemservicousuarioService.getInstance().saveOrUpdate(ordemservicoUsuario);
	}

	/**
	 * Dá a opção ao usuário de alterar o tipo de palete.
	 * 
	 * @throws IOException
	 */
	private void makeMenuTipoPalete(Produto produto) throws IOException{
		prepararTela();
		writeLine("Selecione um tipo de palete: ");
		writeLine("");
		
		List<Produtotipopalete> listaProdutoTipoPalete = produto.getListaProdutoTipoPalete();
		final HashMap<Integer, Produtotipopalete> mapa = new HashMap<Integer, Produtotipopalete>();
		
		writeLine("0 - Voltar");
				
		int i = 1;
		for (Produtotipopalete produtotipopalete : listaProdutoTipoPalete) {
			mapa.put(i, produtotipopalete);
			writeLine((i++)+" - "+produtotipopalete.getTipopalete().getNome());
		}
		
		writeLine("");
		Editfield optDep=new Editfield(getTermIO(),"editfield 3",2);
		optDep.registerInputValidator(new InputValidator(){
			public boolean validate(String str) {
				try{
					int option = Integer.parseInt(str);
					if(option <= mapa.size() && option >= 0){
						return true;
					}
				} catch (Exception e){
					return false;
				}
				return false;
			}
		});
		optDep.run();
		writeLine("");
		
		String valorSelecionado = optDep.getValue();
		
		if("0".equals(valorSelecionado))
			return;
		
		for (Produtotipopalete produtotipopalete : listaProdutoTipoPalete) {
			produtotipopalete.setAlterado(false);
		}
		
		Produtotipopalete produtotipopalete = mapa.get(Integer.parseInt(valorSelecionado));
		produtotipopalete.setAlterado(true);
		
		writeLine("");
		writeLine("Palete selecionado:");
		writeLine(produtotipopalete.getTipopalete().getNome());
		read();
	}

}
