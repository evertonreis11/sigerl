package br.com.ricardoeletro.coletor.geral.as;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculo;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculostatus;
import br.com.linkcom.wms.geral.bean.Conferenciaordemrecebimento;
import br.com.linkcom.wms.geral.bean.Deposito;
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
import br.com.linkcom.wms.geral.predicate.ConferenciaOrdemRecebimentoPredicate;
import br.com.linkcom.wms.geral.service.AcompanhamentoveiculoService;
import br.com.linkcom.wms.geral.service.AcompanhamentoveiculohistoricoService;
import br.com.linkcom.wms.geral.service.ConferenciaordemrecebimentoService;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
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
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.ricardoeletro.coletor.modulo.recebimento.process.filtro.ConferenciaRecebimentoFiltro;
import br.com.ricardoeletro.coletor.modulo.recebimento.process.filtro.ConferenciaRecebimentoFiltro.TipoColeta;

public class ConferenciaRecebimentoAS extends ColetorAS {
	
	protected final Deposito deposito = WmsUtil.getDeposito();
	protected final RecebimentoService recebimentoService = ((RecebimentoService)getService(RecebimentoService.class));
	protected final AcompanhamentoveiculoService acompanhamentoveiculoService = ((AcompanhamentoveiculoService)getService(AcompanhamentoveiculoService.class));
	protected final OrdemservicoService ordemservicoService = ((OrdemservicoService)getService(OrdemservicoService.class));
	protected final OrdemservicousuarioService ordemservicousuarioService = ((OrdemservicousuarioService)getService(OrdemservicousuarioService.class));
	protected final OrdemprodutohistoricoService ordemprodutohistoricoService = ((OrdemprodutohistoricoService)getService(OrdemprodutohistoricoService.class));
	protected final ProdutoembalagemService produtoembalagemService = ((ProdutoembalagemService)getService(ProdutoembalagemService.class));
	protected final ConfiguracaoService configuracaoService = ((ConfiguracaoService)getService(ConfiguracaoService.class));
	protected final ProdutotipopaleteService produtotipopaleteService = ((ProdutotipopaleteService)getService(ProdutotipopaleteService.class));
	protected final ProdutoService produtoService = ((ProdutoService)getService(ProdutoService.class));
	protected final ConferenciaordemrecebimentoService conferenciaOrdemRecebimentoService = ((ConferenciaordemrecebimentoService)getService(ConferenciaordemrecebimentoService.class));
	protected final OrdemservicoprodutoService ordemservicoprodutoService = ((OrdemservicoprodutoService)getService(OrdemservicoprodutoService.class));
	protected final AcompanhamentoveiculohistoricoService acompanhamentoveiculohistoricoService = ((AcompanhamentoveiculohistoricoService)getService(AcompanhamentoveiculohistoricoService.class));
	
	
	
	public Map<String, String> recuperaRecebimento(ConferenciaRecebimentoFiltro filtro){
		StringBuilder mensagem = null;
		Map<String, String> retorno = new HashMap<String, String>();
		
		if(findColetor(filtro, mensagem)){
			String dadosRecebimento = showInformationsRecebimento(filtro);
			filtro.setIniciarColeta(Boolean.TRUE);
			retorno.put("dados", dadosRecebimento);
		} else {
			mensagem = new StringBuilder("Recebimento indisponível.");
			retorno.put("mensagem", mensagem.toString());
		}
		
		return retorno;
		
	}
	
	private boolean findColetor(ConferenciaRecebimentoFiltro filtro, StringBuilder mensagem){
		Integer numero = Integer.parseInt(filtro.getValorInicial());
		mensagem = new StringBuilder();
		
		if(numero == null || numero.equals(0))
			return false;
		
		Recebimento findRecebimentoByBoxRF = new Recebimento(numero); 
		findRecebimentoByBoxRF = recebimentoService.findRecebimentoByBoxRF(findRecebimentoByBoxRF, deposito);
		
		if(findRecebimentoByBoxRF == null)
			return false;
		
		filtro.setRecebimento(findRecebimentoByBoxRF);
		
		List<Acompanhamentoveiculo> listaAcompanhamentoVeiculo = acompanhamentoveiculoService.findByRecebimento(findRecebimentoByBoxRF);
		
		if(listaAcompanhamentoVeiculo != null && !listaAcompanhamentoVeiculo.isEmpty()) {
			for (Acompanhamentoveiculo acompanhamentoveiculo : listaAcompanhamentoVeiculo) {
				if(acompanhamentoveiculo.getDataentrada() == null){
					StringBuilder erroAcompanhamento = new StringBuilder();
					erroAcompanhamento.append("A RAV: ")
						.append(acompanhamentoveiculo.getCdacompanhamentoveiculo())
						.append(" não teve a sua entrada confirmada.")
						.append(System.lineSeparator())
						.append("Por favor, confirme a entrada do veículo")
						.append(System.lineSeparator())
						.append(" antes de iniciar a conferência.");
					
					writeLine(mensagem, erroAcompanhamento.toString());
					
					return false;
				}
			}
		}
		
		filtro.setOrdemServico(ordemservicoService.loadLastConferencia(filtro.getRecebimento()));
		
		if(!validateOS(filtro.getOrdemServico()))
			return false;
			
		if(!verificaOsAberta(filtro))
			return false;
		
		
		associarUsuarioOS(filtro);
		
		return true;
	}
	
	/**
	 * Valida se a ordem de serviço pode ser acessada por esta tela.
	 * @param ordemservico 
	 * 
	 * @return true - caso possa ser acessada.
	 * 		   false - não possa ser acessada.
	 */
	protected boolean validateOS(Ordemservico ordemServico){
		if(ordemServico == null || ordemServico.getOrdem() > 1 
				|| (!Ordemstatus.EM_ABERTO.equals(ordemServico.getOrdemstatus()) && 
						!Ordemstatus.EM_EXECUCAO.equals(ordemServico.getOrdemstatus())))
			return false;
		
		return true;
	}
	
	/**
	 * Verifica se o usuário pode coletar esta OS. Caso o usuário tenha perdido a conexão, ele pode
	 * reconectar-se a mesma os.
	 * @param filtro 
	 * 
	 * @return true - Caso este usuário possa coletar esta OS.
	 *         false - O usuário não pode coletar essa OS.
	 */
	private Boolean verificaOsAberta(ConferenciaRecebimentoFiltro filtro){
		filtro.setOrdemservicoUsuario(null);
		
		OrdemservicoUsuario loadByRecebimento = ordemservicousuarioService.findByOrdemservico(filtro.getOrdemServico());
		if(loadByRecebimento == null)
			return true;
		else{
			if(loadByRecebimento.getUsuario().getCdpessoa().equals(WmsUtil.getUsuarioLogado().getCdpessoa())){
				filtro.setOrdemservicoUsuario(loadByRecebimento);
				return true;
			} else { 
				filtro.setRecebimento(null);
				return false;
			}
		}
	}
	
	/**
	 * Associa o usuário que está logado no box do recebimento, e amarra ele a última ordem de serviço.
	 * @param filtro 
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicousuarioService#salvarOrdemServicoUsuarioForConferencia(ConferenciaCegaPapelFiltro filtro)
	 */
	private void associarUsuarioOS(ConferenciaRecebimentoFiltro filtro){
		Recebimento recebimento = filtro.getRecebimento();
		
		if (recebimento.getRecebimentostatus() == null || recebimento.getRecebimentostatus().equals(Recebimentostatus.DISPONIVEL)){
			recebimento.setRecebimentostatus(Recebimentostatus.EM_ANDAMENTO);
			recebimentoService.gravaStatusRecebimento(recebimento);
		}
		
		if(filtro.getOrdemservicoUsuario() == null) {
			
			OrdemservicoUsuario ordemservicoUsuario = new OrdemservicoUsuario();
			
			Ordemservico loadLastButOne = ordemservicoService.loadLastConferencia(recebimento);
			ordemservicoUsuario.setOrdemservico(loadLastButOne);
			ordemservicoUsuario.setUsuario(WmsUtil.getUsuarioLogado());
			ordemservicoUsuario.setDtinicio(new Timestamp(System.currentTimeMillis()));
			ordemservicoUsuario.setDtfim(null);
			
			ordemservicousuarioService.saveOrUpdate(ordemservicoUsuario);
			
			Ordemservico ordemServico = filtro.getOrdemServico();
			
			ordemServico.setOrdemstatus(Ordemstatus.EM_EXECUCAO);
			ordemservicoService.atualizarStatusordemservico(ordemServico);
			
			filtro.setOrdemservicoUsuario(ordemservicoUsuario);
		}
	}
	
	/**
	 * Exibe as informações do recebimento que foi encontrado na tela.
	 * 
	 * @throws IOException
	 * @author Pedro Gonçalves
	 * @param filtro 
	 * @return 
	 */
	private String showInformationsRecebimento(ConferenciaRecebimentoFiltro filtro) {
		StringBuilder dados = new StringBuilder();
		
		Recebimentonotafiscal next = filtro.getRecebimento().getListaRecebimentoNF().iterator().next();
		
		filtro.setVeiculo(next.getNotafiscalentrada().getVeiculo());

		writeLine(dados,"Conferência de recebimento");
		writeSeparator(dados);
		writeLine(dados, "");
		writeLine(dados, "Recebimento: " + filtro.getRecebimento().getCdrecebimento());
		writeLine(dados, "");
		writeLine(dados,"O.S.: " + filtro.getOrdemServico().getCdordemservico());
		writeLine(dados, "");
		writeLine(dados,"Box: " + filtro.getRecebimento().getBox().getNome());
		writeLine(dados, "");
		writeLine(dados,"Veículo: " + filtro.getVeiculo());
		
		return dados.toString();
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
	public String startCollectProduct(ConferenciaRecebimentoFiltro filtro) {

		String msg = null;

		List<Ordemprodutohistorico> listaOPH = recuperaObjetos(filtro);

		//verifica se o produto pertence a osp.
		filtro.setOrdemprodutohistorico(biparProduto(filtro,listaOPH));

		if(filtro.getOrdemprodutohistorico() != null){
			if(filtro.getOrdemprodutohistorico().getOrdemservicoproduto().getProduto().getModificado() == null || !filtro.getOrdemprodutohistorico().getOrdemservicoproduto().getProduto().getModificado()){
				//carrega o produto caso não tenha sido carregado ainda.
				filtro.getOrdemprodutohistorico().getOrdemservicoproduto().setProduto(produtoService.findDadosLogisticosProdutoRF(filtro.getOrdemprodutohistorico().getOrdemservicoproduto().getProduto(), deposito));
				filtro.getOrdemprodutohistorico().getOrdemservicoproduto().getProduto().setModificado(true);
			}
			
			if (ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETA_QUANT_AUTO_RECEB, this.deposito)){
				return collectQte(filtro);
			}else{
				//mostra as informações do produto
				produtoInformations(filtro.getOrdemprodutohistorico(), filtro);
				
				msg = "OK";
			}
			
			
			
			
			/*Boolean alterado = collectQte(ordemProdutoHistorico); //coleta das quantidades
				if(alterado) {
					produtoInformations(ordemProdutoHistorico);
					if(tipoColeta.equals(TipoColeta.PADRAO)){
						writeLine("Qtde. Emb.: " + ordemProdutoHistorico.getQtdeColetada());
					} else if(tipoColeta.equals(TipoColeta.FRACIONADA)){
						writeLine("Qtde. Frac.: " + ordemProdutoHistorico.getQtdefracionada());
					} else 
						writeLine("Avariado: " + ordemProdutoHistorico.getQtdeavaria());

					read();
				}*/
			
		} else {
			msg = "Produto não pertence ao lote." ;
		}
		
		return msg;

	}

	protected List<Ordemprodutohistorico> recuperaObjetos(ConferenciaRecebimentoFiltro filtro) {
		filtro.setRecebimento(recebimentoService.findRecebimentoByBoxRF(filtro.getRecebimento(), deposito));		
		filtro.setOrdemServico(ordemservicoService.loadLastConferencia(filtro.getRecebimento()));
		filtro.setOrdemservicoUsuario(ordemservicousuarioService.findByOrdemservico(filtro.getOrdemServico())); 

		List<Ordemprodutohistorico> listaOPH = ordemprodutohistoricoService.findByForRF(filtro.getRecebimento());
		
		return listaOPH;
	}
	
	/**
	 * Solicita que usuário informe um código de produto e procura o objeto {@link Ordemprodutohistorico} associado.
	 * 
	 * @param codigo
	 * @return null caso não tenha sido encontrado nenhuma ocorrência.
	 * @throws IOException 
	 */
	protected Ordemprodutohistorico biparProduto(ConferenciaRecebimentoFiltro filtro, List<Ordemprodutohistorico> listaOPH){
		String codigoLido = filtro.getValorInicial();
		
		Ordemservicoproduto osp = null;
		for (Ordemprodutohistorico oph : listaOPH) {
			osp = oph.getOrdemservicoproduto();
			
			List<Produtocodigobarras> listaProdutoCodigoDeBarras = osp.getProduto().getListaProdutoCodigoDeBarras();
			
			String cod = getCodigoProduto(osp.getProduto().getCodigo().trim());
			
			//O sistema pode ser configurado para não aceitar leitura de código interno
			if((!configuracaoService.isTrue(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS, this.deposito))
					&& cod.toUpperCase().equals(codigoLido.trim().toUpperCase())){
				oph.setCodigoBarrasIndex(null);//Não encontrou através do código de barras
				return oph;
			}
			
			for (int i = 0; i < listaProdutoCodigoDeBarras.size(); i++) {
				Produtocodigobarras produtocodigobarras = listaProdutoCodigoDeBarras.get(i);

				String codigo2 = produtocodigobarras.getCodigo();
				
				if(codigoLido.trim().equals(codigo2.trim())){
					oph.setCodigoBarrasIndex(i);//Indice que encontrou o código de barras
					filtro.setCodigoBarrasConferencia(codigo2); // codigo de barras utilizado para identificar o produto.
					return oph;
				}
			}
		}
		return null;
	}
	
	/**
	 * Mostra as informações do produto.
	 * 
	 * @param produtoPertence
	 * @throws IOException
	 */
	protected void produtoInformations(Ordemprodutohistorico produtoPertence , ConferenciaRecebimentoFiltro filtro){
		StringBuilder sb = new StringBuilder();
		
		boolean showInfoProduto = showInfoProduto(sb, produtoPertence, filtro);
		
		if(showInfoProduto){
			writeLine(sb, "Dados logísticos incompletos");
			writeSeparator(sb);
		} 
		
		filtro.setResultado(sb.toString());
	}

	/**
	 * Exibe as informações do produto da osp bipada.
	 * @param sb 
	 * 
	 * @see #getEmbalagemCompra(Produto)
	 * 
	 * @param produtoPertence
	 * @return false - Caso os dados logísticos estejam ok.
	 *         true - Caso falte algum dado logístico.
	 */
	private boolean showInfoProduto(StringBuilder sb, Ordemprodutohistorico produtoPertence, ConferenciaRecebimentoFiltro filtro){
		boolean error = false;
		
		Produto produto = produtoService.loadForEntrada(produtoPertence.getOrdemservicoproduto().getProduto());
		
		writeLine(sb,"Recebimento: " + filtro.getRecebimento().getCdrecebimento());
		writeLine(sb, "Veículo: " + filtro.getVeiculo());
		writeSeparator(sb);

		String codigoP = "";
		try {
			codigoP = produto.getCodigo();
		} catch (Exception e) {error=true;}
		
		writeLine(sb, "Código: " + codigoP);
		if (produto.getProdutoprincipal() != null){
			writeLine(sb, "Descrição: " + produto.getProdutoprincipal().getDescricao());
			writeLine(sb, "Volume: " + produto.getComplementocodigobarras().substring(0, 2) + "/" + 
					produto.getComplementocodigobarras().substring(2));
		}else
			writeLine(sb, "Descrição: " + produto.getDescricao());
		
		
		if (filtro.getTipoColeta().equals(TipoColeta.PADRAO)){
			String embalagem = "";
			try {
				produto.setListaProdutoEmbalagem(null);
				Produtoembalagem embalagemCompra = produtoembalagemService.getEmbalagemCompra(produto);
				embalagem = embalagemCompra.getDescricao() + " " + embalagemCompra.getQtde();
			} catch (Exception e) {error=true;}
			
			writeLine(sb, "Embalagem: " + embalagem);
		}
		
		if(filtro.getCodigoBarrasConferencia() != null){
			writeLine(sb, "Código barras: " + filtro.getCodigoBarrasConferencia());
		}
		
		String norma = "";
		String tipopalete = "";
		try {
			List<Produtotipopalete> lista = produtotipopaleteService.findByProduto(produto,deposito);
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
		
		writeLine(sb, "Tipo palete: "+ tipopalete);
		writeLine(sb, "Norma: "+norma);
		writeSeparator(sb);
		
		return error;
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
	@SuppressWarnings("deprecation")
	public String collectQte(ConferenciaRecebimentoFiltro filtro) {

		List<Ordemprodutohistorico> listaOPH = ordemprodutohistoricoService.findByForRF(filtro.getRecebimento());

		Ordemprodutohistorico ordemProdutoHistorico = (Ordemprodutohistorico) CollectionUtils.find(listaOPH, 
				new BeanPropertyValueEqualsPredicate("cdordemprodutohistorico", filtro.getOrdemprodutohistorico().getCdordemprodutohistorico()));

		Produto produto = ordemProdutoHistorico.getOrdemservicoproduto().getProduto();
		Produtoembalagem embalagem = null;

		if (StringUtils.isNotBlank(filtro.getCodigoBarrasConferencia())){
			Produtocodigobarras produtocodigobarras = (Produtocodigobarras) CollectionUtils.find(produto.getListaProdutoCodigoDeBarras(), 
					new BeanPropertyValueEqualsPredicate("codigo", filtro.getCodigoBarrasConferencia()));
			embalagem = produtocodigobarras.getProdutoembalagem();
		}else{
			embalagem = produtoembalagemService.getEmbalagemCompra(produto);
		}

		//entrada de dados para a quantidade
		if(embalagem == null){
			//produto não possui embalagem de recebimento. e será coletado neste modo de coleta. Pressione enter para continuar
			return "Este produto não possui embalagem de recebimento. ";
		}

		Long qtderecebida = recuperaQuantidadePorTipoColeta(filtro.getTipoColeta(), ordemProdutoHistorico, embalagem.getCompra());

		List<Conferenciaordemrecebimento> listaconferencias = conferenciaOrdemRecebimentoService.recuperaConferenciasPorRecebimento(filtro.getRecebimento()); 

		@SuppressWarnings("unchecked")
		List<Conferenciaordemrecebimento> conferenciasOrdem = (List<Conferenciaordemrecebimento>) CollectionUtils.select(listaconferencias
				,new BeanPropertyValueEqualsPredicate("ordemprodutohistorico.cdordemprodutohistorico"
						,ordemProdutoHistorico.getCdordemprodutohistorico()));

		Long qtdeEmbalagem = null;

		long coletado = NumberUtils.LONG_ONE.longValue();

		if (!ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETA_QUANT_AUTO_RECEB, this.deposito)){
			coletado = Long.parseLong(filtro.getValorInicial());
			
			ordemProdutoHistorico.setQtdeColetada(coletado);
		}

		if (embalagem.getQtde() != null)
			qtdeEmbalagem = embalagem.getQtde();
		else
			qtdeEmbalagem = NumberUtils.LONG_ONE;

		Conferenciaordemrecebimento conferencia = null; 

		if (conferenciasOrdem != null && !conferenciasOrdem.isEmpty())	
			conferencia = (Conferenciaordemrecebimento) CollectionUtils.find(conferenciasOrdem
					,new ConferenciaOrdemRecebimentoPredicate(embalagem.getCdprodutoembalagem(),TipoColeta.AVARIA.equals(filtro.getTipoColeta())));

		if (conferencia == null){
			conferenciaOrdemRecebimentoService.createConferenciaOrdem(ordemProdutoHistorico, 
					filtro.getRecebimento(), 
					calculaQtdeProduto(coletado, qtderecebida, qtdeEmbalagem), 
					TipoColeta.AVARIA.equals(filtro.getTipoColeta()), 
					embalagem);
		}else{
			conferencia.setQtde((conferencia.getQtde() - qtderecebida) + calculaQtdeProduto(coletado, qtderecebida, qtdeEmbalagem));
			conferenciaOrdemRecebimentoService.saveOrUpdateNoUseTransaction(conferencia);
		}

		/*if (produto.getProdutoprincipal() != null) 
			produto = produto.getProdutoprincipal();

		if(produto.getListaProdutoTipoPalete() != null && 
				produto.getListaProdutoTipoPalete().size() > 1 ){
			if (confirmAction("Trocar o palete? ")){
				makeMenuTipoPalete(produto);
				return true;
			}
		}*/
		
		return "OK";
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

	public void configurarOSUDataFim(OrdemservicoUsuario ordemservicoUsuario) {
		ordemservicoUsuario = ordemservicousuarioService.loadForEntrada(ordemservicoUsuario);
		ordemservicoUsuario.setDtfim(new Timestamp(System.currentTimeMillis()));
		ordemservicousuarioService.saveOrUpdate(ordemservicoUsuario);
		
	}
	
	/**
	 * Desassocia a ordem de serviço do usuário logado no coletor.
	 * @throws IOException 
	 */
	public String desassociarOSU(ConferenciaRecebimentoFiltro filtro) {
		Ordemservico ordemServico = ordemservicoService.loadForEntrada(filtro.getOrdemServico());
		String msgErro = null;
		
		msgErro = isOrdemAindaAberta(ordemServico);
		
		if (StringUtils.isNotBlank(msgErro))
			return msgErro;
		
		ordemservicoprodutoService.resetarQuantidades(ordemServico);
		
		conferenciaOrdemRecebimentoService.resetarQuantidades(filtro.getRecebimento());
		
		ordemservicousuarioService.delete(filtro.getOrdemservicoUsuario());
		ordemServico.setOrdemstatus(Ordemstatus.EM_ABERTO);
		ordemservicoService.atualizarStatusordemservico(ordemServico);
		
		return msgErro;
	}
	
	protected String isOrdemAindaAberta(Ordemservico ordemservico){

		Ordemservico ordemservicoAux = ordemservicoService.load(ordemservico);
		
		if (!ordemservicoAux.getOrdemstatus().equals(Ordemstatus.EM_ABERTO) 
				&& !ordemservicoAux.getOrdemstatus().equals(Ordemstatus.EM_EXECUCAO)){
			return "Esta ordem de serviço foi finalizada por outro operador.";
			
		} else
			return null;
	}
	
	/**
	 * Processa a lista de ordem servico produto.
	 * @throws IOException 
	 */
	public String processarOSP(ConferenciaRecebimentoFiltro filtro){
		Recebimento recebimento = recebimentoService.findRecebimentoByBoxRF(filtro.getRecebimento(), deposito);
		Ordemservico ordemServico = ordemservicoService.loadLastConferencia(filtro.getRecebimento());
		List<Ordemprodutohistorico> listaOPH = ordemprodutohistoricoService.findByForRF(filtro.getRecebimento());
		
		configurarOSUDataFim(filtro.getOrdemservicoUsuario());
		
		// Registra os valores coletados, na ordemprodutoHistorico
		atualizaQtdeOrdemProdutoHistorico(listaOPH, recebimento);
		
		final ConferenciaCegaPapelFiltro conferenciaCegaPapelFiltro = new ConferenciaCegaPapelFiltro();
		conferenciaCegaPapelFiltro.setSkipSaveOSU(true);
		recebimento.setDeposito(deposito);
		conferenciaCegaPapelFiltro.setRecebimento(recebimento);
		conferenciaCegaPapelFiltro.setListaOrdemProdutoHistorico(new ListSet<Ordemprodutohistorico>(Ordemprodutohistorico.class,listaOPH));
		conferenciaCegaPapelFiltro.setGeraReconferencia(true);
		conferenciaCegaPapelFiltro.setUsuariofinalizacao(WmsUtil.getUsuarioLogado());
		
		Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				try {
					ordemservicoprodutoService.salvarListaOrdemServicoProdutoForConferencia(conferenciaCegaPapelFiltro,true);
				} catch (InsercaoInvalidaException e) {
					throw new WmsException(e.getMessage(), e);
				}
				return null;
			}
		});
		
		atualizaRecebimentoStatus(recebimento, ordemServico);
		
		return alertConclusaoRecebimento(recebimento, ordemServico);
	}
	
	/**
	 * Atualiza as quantidades coletadas anteriormente no registro de ordemProdutoHistorico.
	 */
	private void atualizaQtdeOrdemProdutoHistorico(List<Ordemprodutohistorico> listaOPH, Recebimento recebimento) {
		List<Conferenciaordemrecebimento> listaconferencias = conferenciaOrdemRecebimentoService.recuperaConferenciasPorRecebimento(recebimento); 
		
		if (listaconferencias != null && !listaconferencias.isEmpty()){
			for (Ordemprodutohistorico oph : listaOPH) {
				
				@SuppressWarnings("unchecked")
				List<Conferenciaordemrecebimento> conferenciasPorOrdem = (List<Conferenciaordemrecebimento>) CollectionUtils.select(listaconferencias
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
					
					ordemprodutohistoricoService.atualizarQuantidades(oph);
				}
			}
		}
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
	public void atualizaRecebimentoStatus(Recebimento recebimento, Ordemservico ordemServico) {
		Recebimento aux = recebimentoService.load(recebimento);
		
		if(aux != null && aux.getRecebimentostatus() != null && aux.getRecebimentostatus().getCdrecebimentostatus() != null)
			recebimento.setRecebimentostatus(aux.getRecebimentostatus());
		
		Ordemservico osAux = ordemservicoService.load(ordemServico);
		if(osAux != null && osAux.getOrdemstatus() != null && 
				(Ordemstatus.FINALIZADO_DIVERGENCIA.equals(osAux.getOrdemstatus()) || 
						Ordemstatus.FINALIZADO_SUCESSO.equals(osAux.getOrdemstatus()) || 
						Ordemstatus.AGUARDANDO_CONFIRMACAO.equals(osAux.getOrdemstatus()))){
			
			ordemServico.setOrdemstatus(osAux.getOrdemstatus());
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
	protected String alertConclusaoRecebimento(Recebimento recebimento, Ordemservico ordemServico) {
		String msg = null;
		
		if(ordemServico != null && ordemServico.getOrdemstatus() != null && ordemServico.getOrdemstatus().getCdordemstatus() != null){
			if(ordemServico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_SUCESSO)){
				msg = "Recebimento OK.";
			}
			else if(ordemServico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_DIVERGENCIA) || ordemServico.getOrdemstatus().equals(Ordemstatus.AGUARDANDO_CONFIRMACAO)){
				msg = "Recebimento com divergências.";
			}
			
			try{
				List<Acompanhamentoveiculo> lista = acompanhamentoveiculoService.findByRecebimento(recebimento);
				if(lista!=null && !lista.isEmpty()){
					for (Acompanhamentoveiculo av : lista){
						av.setAcompanhamentoveiculostatus(Acompanhamentoveiculostatus.RECEBIMENTO_FINALIZADO);
						acompanhamentoveiculoService.atualizaStatus(av,Acompanhamentoveiculostatus.RECEBIMENTO_FINALIZADO);
						acompanhamentoveiculohistoricoService.criarHistorico(av, null, WmsUtil.getUsuarioLogado());	
					}
				}
			}catch (Exception e) {
				msg = "Erro ao adicionar o histórico ao RAV. Tente novamente." ;
				e.printStackTrace();
			}
			
		}
		
		return msg;
	}

}
