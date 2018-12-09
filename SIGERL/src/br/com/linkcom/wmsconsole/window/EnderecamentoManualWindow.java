package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.terminal.ColorHelper;
import net.wimpi.telnetd.io.toolkit.Editfield;
import net.wimpi.telnetd.io.toolkit.InputValidator;
import net.wimpi.telnetd.io.toolkit.Label;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Dadologistico;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecostatus;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemprodutoligacao;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoendereco;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Produtotipoestrutura;
import br.com.linkcom.wms.geral.bean.Produtotipopalete;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Recebimentonotafiscal;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;
import br.com.linkcom.wms.geral.bean.Tipoenderecamento;
import br.com.linkcom.wms.geral.bean.Umareserva;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.DadologisticoService;
import br.com.linkcom.wms.geral.service.EnderecoService;
import br.com.linkcom.wms.geral.service.EnderecoprodutoService;
import br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoenderecoService;
import br.com.linkcom.wms.geral.service.OrdemservicousuarioService;
import br.com.linkcom.wms.geral.service.ProdutocodigobarrasService;
import br.com.linkcom.wms.geral.service.ProdutoembalagemService;
import br.com.linkcom.wms.geral.service.ProdutotipoestruturaService;
import br.com.linkcom.wms.geral.service.ProdutotipopaleteService;
import br.com.linkcom.wms.geral.service.RecebimentoService;
import br.com.linkcom.wms.util.InsercaoInvalidaException;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wmsconsole.system.ExecucaoOSWindow;

/**
 * 
 * @author Leonardo Guimar�es
 * 
 */
public class EnderecamentoManualWindow extends ExecucaoOSWindow {

	private static final String RECEBIMENTO_INDISPONIVEL = "Recebimento indispon�vel.";
	private static final String INFORME_O_END_DE_DESTINO = "Informe o end. de destino:";
	private static final String INFORME_O_ENDERECO = "Informe o endere�o:";
	protected static final String UMA_EM_EXECUCAO_POR_OUTRO_OPERADOR = "UMA em execu��o por outro operador.";

	public enum Acao { Voltar, TrocarRecebimento, CancelarOperacao, FinalizarEnderecamento, Nenhuma};
	
	private static final String SEM_ENDERECO_DISPONIVEL = "N�o h� endere�o dispon�vel neste pr�dio.";
	protected static final String ENDERECO_INVALIDO = "Endere�o inv�lido.";
	private static final String TITULO = "Endere�amento manual";
	private static final String ENDERECO_OCUPADO = "Este endere�o j� est� ocupado.";
	private static final String LARGURA_EXCEDENTE_INCOMPATIVEL = "Largura excedente incompat�vel.";
	protected Recebimento recebimento;
	protected List<Ordemservicoproduto> listaAvariada = new ArrayList<Ordemservicoproduto>(); 
	protected List<Ordemservicoproduto> listaFracionada = new ArrayList<Ordemservicoproduto>(); 
	protected List<Ordemservicoproduto> listaUma = new ArrayList<Ordemservicoproduto>();
	protected String cabecalho = "";
	protected TipoColeta tipoColeta;
	protected boolean isUMA = false;
	private boolean isManualComUma = false;

	/**
	 * M�todo inicial da classe
	 * 
	 * @see #isRecebimentoOk(String)
	 * @see #showInformationsRecebimento()
	 * @see #loadFracionadaAvariaUma()
	 * @see #alertErroRecebimento(int)
	 * @see #haveItens()
	 */
	@Override
	public void draw() throws IOException {
		do {
			drawEsqueleto("Digite 0 para sair.");

			Integer valor = readInteger("Recebimento: ");
			if (valor == null || valor.equals(0))
				break;
			if (isRecebimentoOk(valor)) {
				executarOrdem(null);
			} 
		} while (true);

	}
	
	@Override
	public void executarOrdem(Ordemservico ordemservico) throws IOException {
		if (ordemservico != null)
			this.recebimento = RecebimentoService.getInstance().findForEnderecamento(ordemservico.getRecebimento(),deposito, ordemservico.getRecebimento().getTipoenderecamento());
		
		this.isManualComUma = OrdemservicoService.getInstance().possuiUmaManual(this.recebimento);
		
		loadFracionadaAvariaUma(); // Carrega as listas
		tipoColeta = TipoColeta.PADRAO;
		
		if(!this.recebimento.getRecebimentostatus().equals(Recebimentostatus.EM_ENDERECAMENTO)){
			writeOnCenter(RECEBIMENTO_INDISPONIVEL, ColorHelper.RED, false, true);
			return;
		};
		
		showInformationsRecebimento(); // Mostra as informa��es do recebimento

		try {
			iniciaEnderecamento();
		} catch (TrocarRecebimentoException e) {
			if (e.getMessage() != null)
				alertError(e.getMessage(), true);
				
			return;
		}
	}
	
	/**
	 * Verifica se existem itens para endere�ar
	 * 
	 * @return
	 */
	private boolean haveItens() {
		if((this.listaAvariada == null || this.listaAvariada.isEmpty()) && 
		   (this.listaFracionada == null || this.listaFracionada.isEmpty()) && 
		   (this.listaUma == null || this.listaUma.isEmpty()))
				return false;
		
		return true;
	}

	/**
	 * Verifica se o {@link Ordemservicoproduto} ainda est� com status igual a
	 * {@link Ordemprodutostatus#NAO_CONCLUIDO}, fazendo uma consulta novamente
	 * ao banco de dados
	 * 
	 * @param osp
	 * @return
	 */
	protected boolean isOrdemservicoprodutoEmAberto(Ordemservicoproduto osp) {
		Ordemservicoproduto ospeAux = OrdemservicoprodutoService.getInstance().load(osp);
		return ospeAux.getOrdemprodutostatus().equals(Ordemprodutostatus.NAO_CONCLUIDO);
	}
	
	/**
	 * Faz o fluxo principal do endere�amento
	 * 
	 * @see #makeMenuEnderecamentoManual()
	 * @see #carregarInformacoesProdutos()
	 * @see #coletarFracionadosAvariados()
	 * 
	 * @throws IOException
	 * @throws TrocarRecebimentoException 
	 */
	private void iniciaEnderecamento() throws IOException, TrocarRecebimentoException {
		while (true) {

			tipoColeta = makeMenuEnderecamentoManual(); // Verifica o que o usu�rio deseja fazer
			if (tipoColeta == null) {
				if (isConvocacaoAtiva())
					logout();
				return;
			}

			carregarInformacoesProdutos();

			if (TipoColeta.PADRAO.equals(tipoColeta)) {
				isUMA = true;
				if (isManualComUma)
					coletarPorUMA();
				else
					coletarPorProduto();
			} else if (TipoColeta.FRACIONADA.equals(tipoColeta)) {
				isUMA = false;
				coletarPorProduto();
			} else if (TipoColeta.AVARIA.equals(tipoColeta)) {
				isUMA = false;
				coletarProdutosAvariados();
			}
			if (!haveItens())
				break;
		}
	}

	/**
	 * Carrega as informa��es dos produtos dependendo do tipo de coleta
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#loadLastUMA(Recebimento
	 *      recebimento, Produto produto)
	 */
	private void carregarInformacoesProdutos() {
		List<Ordemservicoproduto> lista = new ArrayList<Ordemservicoproduto>();
		
		if (TipoColeta.PADRAO.equals(tipoColeta)) {
			lista = this.listaUma;
		} else if (TipoColeta.FRACIONADA.equals(tipoColeta)) {
			lista = this.listaFracionada;
		} else if (TipoColeta.AVARIA.equals(tipoColeta))
			lista = this.listaAvariada;
		
		for (Ordemservicoproduto ordemservicoproduto : lista) {
			Produto produto = ordemservicoproduto.getProduto();
			produto.getListaProdutoTipoPalete().add(ProdutotipopaleteService.getInstance().findPaletePadrao(produto,deposito));
			
			Produtoembalagem embcompra = ProdutoembalagemService.getInstance().findCompraByProduto(produto);
			produto.getListaProdutoEmbalagem().add(embcompra);
			
			Produtoembalagem menorembalagem = ProdutoembalagemService.getInstance().findMenorEmbalagem(produto);
			if (!embcompra.equals(menorembalagem))
				produto.getListaProdutoEmbalagem().add(menorembalagem);
			
			produto.setListaProdutoCodigoDeBarras(ProdutocodigobarrasService.getInstance().findByProduto(produto));
		}
	}

	/**
	 * Bipa o endere�o
	 * @param ordemservicoprodutoendereco 
	 * 
	 * @return
	 * @throws IOException 
	 * @throws TrocarRecebimentoException 
	 */
	private Endereco biparEnderecoUma(Ordemservicoprodutoendereco ospe) throws IOException, TrocarRecebimentoException {
		while(true){
			getTermIO().eraseScreen();
			drawEsqueleto(this.cabecalho,"Digite 0 para a��es.");
			makeCabecalhoColeta();
			showInformationProduto(ospe.getOrdemservicoproduto(), true);
	
			String codigoEtiqueta = readLine(INFORME_O_ENDERECO);
			
			if (exibirMenu){
				Acao acao = menuMaisAcoes();
				if (Acao.TrocarRecebimento.equals(acao))
					throw new TrocarRecebimentoException();
				else
					return biparEnderecoUma(ospe);
			}
			
			Endereco endereco = null;
			try{
				Dadologistico dadologistico = DadologisticoService.getInstance().findByProduto(ospe.getOrdemservicoproduto().getProduto(),deposito);
				
				endereco = EnderecoService.getInstance().loadEnderecoByCodigoEtiqueta(codigoEtiqueta, this.deposito);
				if(endereco != null){
					if (endereco.getEnderecostatus().equals(Enderecostatus.BLOQUEADO)){
						alertError("Este endere�o est� bloqueado.");
						continue;
					}
						
					if(endereco.getEnderecofuncao().equals(Enderecofuncao.BLOCADO)){
						endereco = calcularEnderecoBlocado(endereco,ospe.getOrdemservicoproduto().getProduto());
						if (endereco == null){
							alertError(SEM_ENDERECO_DISPONIVEL);
							continue;
						}
					}else if(endereco.getEnderecofuncao().equals(Enderecofuncao.PICKING)){//se o endere�o � de picking
						if (dadologistico == null || !dadologistico.getEndereco().equals(endereco)){
							alertError(ENDERECO_INVALIDO);
							continue;
						}
					} else if (EnderecoprodutoService.getInstance().possuiOutroProduto(endereco, new Produto(-1))){
						// Se o endere�o j� possuir outro produto
						alertError(ENDERECO_OCUPADO);
						continue;
					}

					if(dadologistico != null && (dadologistico.getLarguraexcedente() && !endereco.getLarguraexcedente())){
						alertError(LARGURA_EXCEDENTE_INCOMPATIVEL);
						continue;//se o produto e o endere�o n�o possuem largura excedente compat�vel
					}
						
					if (confirmaEnderecamento(ospe, endereco))
						return endereco;
					else
						continue;
				} else {
					alertError(ENDERECO_INVALIDO);
					continue;
				}
			}catch (IOException e) {
				throw e;
			}catch (EnderecoInvalidoException e) {
				alertError(ENDERECO_INVALIDO);
			}catch (Exception e) {
				e.printStackTrace();
				alertError(ENDERECO_INVALIDO);
			}

		}
	}

	/**
	 * Bipa o endere�o
	 * 
	 * @return
	 * @throws IOException 
	 * @throws TrocarRecebimentoException 
	 */
	private String biparEndereco() throws IOException, TrocarRecebimentoException {
		
		getTermIO().eraseScreen();
		drawEsqueleto(this.cabecalho,"Digite 0 para a��es.");
		makeCabecalhoColeta();

		//Todos os produtos de avaria v�o para o mesmo endere�o, por isso posso exibir
		//o endere�o de qualquer um dos itens.
		showEndereco(listaAvariada.get(0).getListaOrdemservicoprodutoendereco().get(0));

		String codigo = readLine(INFORME_O_ENDERECO);
		
		if (exibirMenu){
			Acao acao = menuMaisAcoes();
			if (Acao.TrocarRecebimento.equals(acao))
				throw new TrocarRecebimentoException();
			else
				return biparEndereco();
		}else
			return codigo;
			
	}

	private Ordemservicoprodutoendereco validarProdutoUma(String codigo) {

		for (Ordemservicoproduto osp : listaUma){
			Iterator<Ordemservicoprodutoendereco> iterator = osp.getListaOrdemservicoprodutoendereco().iterator();
			while ( iterator.hasNext() ){
				Ordemservicoprodutoendereco ospe = iterator.next();
				
				Umareserva umareserva = ospe.getListaUmareserva().iterator().next();
			
				try{
					if (umareserva.getEnderecoproduto()!=null && umareserva.getEnderecoproduto().getCdenderecoproduto().equals(Integer.valueOf(codigo))){
						if (isOrdemservicoprodutoEmAberto(osp)){
							ospe.setEtiquetaUMA(codigo);
							osp.setOrdemprodutostatus(Ordemprodutostatus.EM_EXECUCAO);
							OrdemservicoprodutoService.getInstance().atualizarStatus(osp);
							
							Ordemservico ordemservico = ospe.getOrdemservicoproduto().getListaOrdemprodutoLigacao().iterator().next().getOrdemservico();
							if(ordemservico.getListaOrdemServicoUsuario().isEmpty()){
								OrdemservicoUsuario osu = OrdemservicousuarioService.getInstance().associarUsuario(usuario, ordemservico);
								ordemservico.getListaOrdemServicoUsuario().add(osu);
							}

							return ospe;
						} else {
							iterator.remove();
							alertError(UMA_EM_EXECUCAO_POR_OUTRO_OPERADOR);
						}
					}
				}catch (Exception e) {
					//Erro ao converter o c�digo para Long.
					return null;
				}
			}
		}
		
		//Se n�o achou a etiqueta retorna false
		return null;
		
	}
	
	private Ordemservicoprodutoendereco biparUmaManual(List<Ordemservicoproduto> lista) throws IOException, TrocarRecebimentoException {
		while(true){
			drawEsqueleto(this.cabecalho,"Digite 0 para a��es.");
			
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			Recebimentonotafiscal next = recebimento.getListaRecebimentoNF().iterator().next();
			writeLine("Recebimento: " + this.recebimento.getCdrecebimento());			
			writeLine("Veiculo: " + next.getNotafiscalentrada().getVeiculo());
			writeSeparator();

			String codigo = readBarcode("Informe a UMA:");
			if(exibirMenu){
				Acao acao = menuMaisAcoes();
				if (Acao.TrocarRecebimento.equals(acao) || Acao.FinalizarEnderecamento.equals(acao))
					throw new TrocarRecebimentoException();
				else
					return biparUmaManual(lista);
			}
			
			Ordemservicoprodutoendereco etiquetaLida = validarProdutoUma(codigo);
			if(etiquetaLida != null)
				return etiquetaLida;
			 
			alertError("Etiqueta inv�lida.");
		}
	}
	
	private void coletarPorUMA() throws IOException, TrocarRecebimentoException {
		
		if (!TipoColeta.PADRAO.equals(tipoColeta)) {
			return;
		}

		while (listaUma.size() > 0) {
			Ordemservicoprodutoendereco etiquetaLida = biparUmaManual(listaUma);
			
			if (etiquetaLida != null) {// Se a etiqueta UMA est� correta
				Ordemservico ordemservico = etiquetaLida.getOrdemservicoproduto().getListaOrdemprodutoLigacao().iterator().next().getOrdemservico();
				iniciarExecucao(ordemservico);
				
				Endereco endereco = biparEnderecoUma(etiquetaLida);
				
				if (endereco != null){
					try {
						etiquetaLida.setEnderecodestino(endereco);
						enderecarProduto(etiquetaLida);
					} catch (InsercaoInvalidaException e) {
						e.printStackTrace();
						etiquetaLida = null;
						alertError(e.getMessage());
					}
				}
				
				etiquetaLida = null;
				
				//Se a lista ficou vazia e n�o h� itens em execu��o por outro operador � porque acabou
				if (((ordemservico != null && ordemservico.getListaOrdemProdutoLigacao().isEmpty()) || listaUma.isEmpty()) 
						&& OrdemservicoprodutoService.getInstance().isTodosItensFinalizados(ordemservico)){
					
					Neo.getObject(TransactionTemplate.class).execute(new FinalizaExecucaoOS(ordemservico));
					alertConclusaoEnderecamento();
				}
				
			} else
				break;
		}

	}

	private boolean confirmaEnderecamento(Ordemservicoprodutoendereco ospe, Endereco endereco) throws IOException {
		getTermIO().eraseScreen();
		drawEsqueleto(this.cabecalho,"");
		makeCabecalhoColeta();
		showInformationProduto(ospe.getOrdemservicoproduto(), true);

		writeLine("Endere�o destino: ");
		writeLine(endereco.getEnderecoArea());
		writeSeparator();

		Label lb1 = new Label(getTermIO(), "lb1", "Executar o endere�amento do produto?");
		lb1.draw();
		getTermIO().write(BasicTerminalIO.CRLF);
		getTermIO().write(BasicTerminalIO.CRLF);
		getTermIO().write("1 - Sim" + BasicTerminalIO.CRLF);
		getTermIO().write("0 - N�o" + BasicTerminalIO.CRLF);
		getTermIO().write(BasicTerminalIO.CRLF);
		

		final Editfield confirmEdf = new Editfield(getTermIO(), "edf", 1);		
		confirmEdf.registerInputValidator(new InputValidator(){

			public boolean validate(String str) {
				return "1".equals(str) || "0".equals(str);
			}
			
		});
		confirmEdf.run();
		String valueProduto = confirmEdf.getValue();
		
		if(valueProduto.equals("1"))
			return true;
		
		return false;
	}
		
	/**
	 * Faz a coletada de endere�amento a partir do produto, este m�todo �
	 * utilizado no endere�amento manual de UMA e fracionados e no endere�amento
	 * autom�tico para os fracionados.
	 * 
	 * @see #verificarProdutoUMA()
	 * @see #saveOrdensUMA()
	 * 
	 * @throws IOException
	 * @throws TrocarRecebimentoException 
	 */
	protected void coletarPorProduto() throws IOException, TrocarRecebimentoException {
		List<Ordemservicoproduto> lista = new ArrayList<Ordemservicoproduto>();
		
		if (TipoColeta.PADRAO.equals(tipoColeta)) {
			lista = this.listaUma;
		} else if (TipoColeta.FRACIONADA.equals(tipoColeta)) {
			lista = this.listaFracionada;
		} else if (TipoColeta.AVARIA.equals(tipoColeta))
			lista = this.listaAvariada;
		
		do {
			Ordemservicoproduto ordemservicoproduto = biparProduto();
			if (ordemservicoproduto != null) {// Se o produto est� correto
				Ordemservico ordemservico = ordemservicoproduto.getListaOrdemprodutoLigacao().iterator().next().getOrdemservico();
				iniciarExecucao(ordemservico);
				
				Ordemservicoprodutoendereco ospe = coletarEndereco(ordemservicoproduto);
				
				if (ospe != null) {// Se o endere�o est� correto
					if (colectQtde(ospe)) {// Se a quantidade est� correta
						
						try{
							Neo.getObject(TransactionTemplate.class).execute(new EnderecarProduto(ospe));
						}catch (Exception e) {
							if (e.getCause() instanceof TrocarRecebimentoException)
								throw new TrocarRecebimentoException();
							else if (e.getCause() instanceof InsercaoInvalidaException){
								e.printStackTrace();
								alertError(e.getMessage());
								continue;
							} else
								throw new WmsException(e.getMessage(), e);
						}						
					} else
						break;
				} else
					break;
				
				//Se a lista ficou vazia � porque acabou
				if (((ordemservico != null && ordemservico.getListaOrdemProdutoLigacao().isEmpty()) || lista.isEmpty())
						&& OrdemservicoprodutoService.getInstance().isTodosItensFinalizados(ordemservico)){
					
					Neo.getObject(TransactionTemplate.class).execute(new FinalizaExecucaoOS(ordemservico));
					alertConclusaoEnderecamento();
				}
				
			} else
				break;
		} while (lista.size() > 0);
	}

	/**
	 * Como a ordem de servico � uma vari�vel local que muda o valor, 
	 * n�o posso passar ela diretamente para o TransactionCallback.
	 * Ent�o criei esta classe para poder passar a O.S. como par�metro para o construtor.
	 * 
	 * @author Giovane
	 *
	 */
	class FinalizaExecucaoOS implements TransactionCallback {
		private final Ordemservico ordemservico;
		
		public FinalizaExecucaoOS(Ordemservico ordemservico){
			this.ordemservico = ordemservico;
		}
		
		public Object doInTransaction(TransactionStatus status) {
			try {
				validarOrdemServico(ordemservico);
				
				if (!OrdemservicoprodutoService.getInstance().isTodosItensFinalizados(ordemservico)){
					throw new TrocarRecebimentoException("Ainda existem itens em execu��o por outro operador.");
				}
				
				ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_SUCESSO);
				OrdemservicoService.getInstance().atualizarStatusordemservico(ordemservico);		
				
				if (ordemservico.getListaOrdemServicoUsuario().isEmpty()){
					OrdemservicoUsuario osu = OrdemservicousuarioService.getInstance().associarUsuario(usuario, ordemservico);
					ordemservico.getListaOrdemServicoUsuario().add(osu);
				}else{
					OrdemservicousuarioService.getInstance().atualizarHoraFim(usuario, ordemservico, new Timestamp(System.currentTimeMillis()));
				}
				
				RecebimentoService.getInstance().atualizarEstoqueRecebimento(recebimento);
				
			} catch (TrocarRecebimentoException e) {
				throw new WmsException(e.getMessage(), e);
			}
			return null;
		}
	}
	
	/**
	 * Como o OrdemServicoProdutoEndereco � uma vari�vel local que muda o valor, 
	 * n�o posso passar ela diretamente para o TransactionCallback.
	 * Ent�o criei esta classe para poder passar o objeto como par�metro para o construtor.
	 * 
	 * @author Giovane
	 *
	 */
	private class EnderecarProduto implements TransactionCallback {
		private final Ordemservicoprodutoendereco ordemservicoprodutoendereco;
		
		public EnderecarProduto(Ordemservicoprodutoendereco ordemservicoprodutoendereco){
			this.ordemservicoprodutoendereco = ordemservicoprodutoendereco;
		}
		
		public Object doInTransaction(TransactionStatus status) {
			try {
				enderecarProduto(ordemservicoprodutoendereco);
			} catch (InsercaoInvalidaException e) {
				throw new WmsException(e.getMessage(), e);
			} catch (TrocarRecebimentoException e) {
				throw new WmsException(e.getMessage(), e);
			}
			return null;
		}
	}
	
	/**
	 * Coleta a quantidade que ser� armazenada neste endere�o
	 * @param ordemservicoproduto 
	 * 
	 * @return
	 * @throws IOException 
	 * @throws TrocarRecebimentoException 
	 */
	private Boolean colectQtde(Ordemservicoprodutoendereco ospe) throws IOException, TrocarRecebimentoException {
		do{
			drawEsqueleto(this.cabecalho,"Digite 0 para a��es.");
			makeCabecalhoColeta();
			showInformationProduto(ospe.getOrdemservicoproduto(), true);
			showEndereco(ospe);
			
			//Calculando a quantidade a partir da embalagem de recebimento
			List<Produtoembalagem> listaProdutoEmbalagem = ospe.getOrdemservicoproduto().getProduto().getListaProdutoEmbalagem();
			long qtdeEmbalagem = 1L;
			if (TipoColeta.PADRAO.equals(tipoColeta)){
				Produtoembalagem produtoembalagem = getEmbalagem(listaProdutoEmbalagem, true);
				if (produtoembalagem != null && produtoembalagem.getQtde() != null && produtoembalagem.getQtde() > 0)
					qtdeEmbalagem  = produtoembalagem.getQtde();
				else
					qtdeEmbalagem = 1L;
			}
			
			
			Integer value = readInteger("Informe a quantidade:", Integer.MAX_VALUE);
			
			if (exibirMenu){
				Acao acao = menuMaisAcoes();
				if (Acao.TrocarRecebimento.equals(acao))
					throw new TrocarRecebimentoException();
				else
					return colectQtde(ospe);
			}

			if(value == null || value.equals(0))
				return false;
			
			Long qtde = Long.valueOf(value);
			qtde = qtde * qtdeEmbalagem;

			if(qtde > ospe.getOrdemservicoproduto().getQtdeFaltante()){
				alertError("A quantidade informada �  maior que a esperada.");
				continue;
			}
			
			ospe.setQtde(qtde);
			
			return true;
		}while(true);
	}

	/**
	 * Verifica se o endere�o bipado � v�lido
	 * @param osp 
	 * 
	 * @return
	 * @throws IOException 
	 * @throws TrocarRecebimentoException 
	 */
	private Ordemservicoprodutoendereco coletarEndereco(Ordemservicoproduto osp) throws IOException, TrocarRecebimentoException {
		while(true){
			drawEsqueleto(this.cabecalho,"Digite 0 para a��es.");
			
			makeCabecalhoColeta();
			showInformationProduto(osp, true);
			String endereco = readLine(INFORME_O_END_DE_DESTINO);
			
			if(endereco == null || endereco.equals(""))
				continue;
			
			if (endereco.equals("0")){
				Acao acao = menuMaisAcoes();
				if (Acao.TrocarRecebimento.equals(acao))
					throw new TrocarRecebimentoException();
				else
					continue;
			}
			
			Ordemservicoprodutoendereco ospe = findEndereco(osp, endereco);
			if(ospe != null)
				return ospe;
			else
				alertError(ENDERECO_INVALIDO);

		}
	}
	
	/**
	 * Verifica se o endere�o UMA bipado � v�lido
	 * 
	 * @return
	 * @throws IOException 
	 */
	private Ordemservicoprodutoendereco findEndereco(Ordemservicoproduto osp, String enderecoEtiqueta) throws IOException {
		Endereco endereco = null;
		try{
			Dadologistico dadologistico = DadologisticoService.getInstance().findByProduto(osp.getProduto(),deposito);
			endereco = EnderecoService.getInstance().loadEnderecoByCodigoEtiqueta(enderecoEtiqueta, this.deposito);
			if(endereco != null){
				if (endereco.getEnderecostatus().equals(Enderecostatus.BLOQUEADO)){
					alertError("Este endere�o est� bloqueado.");
					return null;
				}
					
				//Se � coleta fracionada e produto possui picking cadastrado e o endere�o bipado n�o � este picking, retorna false.
				if (TipoColeta.FRACIONADA.equals(tipoColeta) && dadologistico != null && dadologistico.getEndereco() != null && !endereco.equals(dadologistico.getEndereco()))
					return null;
					
				if(endereco.getEnderecofuncao().equals(Enderecofuncao.BLOCADO)){
					endereco = calcularEnderecoBlocado(endereco, osp.getProduto());
					if (endereco == null){
						alertError(SEM_ENDERECO_DISPONIVEL);
						return null;
					}
					
				}else if(endereco.getEnderecofuncao().equals(Enderecofuncao.PICKING)){//se o endere�o � de picking
					if (dadologistico == null || !endereco.equals(dadologistico.getEndereco()))
						return null;//s� ser� v�lido se for o endere�o de picking do produto informado
					
					//quando o tipo de coleta � Fracionada o OrdemServicoProdutoEndereco j� existe para o endere�o de picking
					if (TipoColeta.FRACIONADA.equals(tipoColeta)){
						for (Ordemservicoprodutoendereco ospe : osp.getListaOrdemservicoprodutoendereco())
							if (endereco.equals(ospe.getEnderecodestino()))
								return ospe;
					}

				}else if(dadologistico != null && (dadologistico.getLarguraexcedente() && !endereco.getLarguraexcedente())){
					return null;//se o produto e o endere�o n�o possuem largura excedente compat�vel
				}
				
				// Se o endere�o j� possuir outro produto devo questionar se deseja inserir
				if (EnderecoprodutoService.getInstance().possuiOutroProduto(endereco, osp.getProduto())){
					if(!confirmAction("Inserir novos produtos no endere�o?")){
						return null;
					}
				}

				return createOrdemEndereco(endereco, osp);
			}
		}catch (IOException e) {
			throw e;
		}catch (EnderecoInvalidoException e) {
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	/**
	 * Faz o calculo de qual parte do blocado o produto deve ser armazenado
	 * 
	 */
	private Endereco calcularEnderecoBlocado(final Endereco enderecoBase, Produto produto) {
		Produtotipoestrutura produtotipoestrutura = ProdutotipoestruturaService.getInstance().findBlocado(produto);
		int restricao = 99;
		if(produtotipoestrutura != null)
			restricao = produtotipoestrutura.getRestricaonivel().intValue();
		
		Endereco enderecoCalculado = EnderecoService.getInstance().findBlocadoForUMA(enderecoBase,restricao);
		return enderecoCalculado;
	}

	/**
	 * Cria uma ordemServicoProdutoEndereco e associa ao endereco bipado
	 * 
	 * @param endereco
	 * @param osp 
	 */
	private Ordemservicoprodutoendereco createOrdemEndereco(Endereco endereco, Ordemservicoproduto osp) {
		Ordemservicoprodutoendereco ospe = null;
		
	    if(osp.getListaOrdemservicoprodutoendereco() != null && !osp.getListaOrdemservicoprodutoendereco().isEmpty()){
			ospe = osp.getListaOrdemservicoprodutoendereco().get(0);
		}else{
			ospe = new Ordemservicoprodutoendereco();
		}

		ospe.setEnderecodestino(endereco);
		ospe.setOrdemservicoproduto(osp);
		osp.getListaOrdemservicoprodutoendereco().add(ospe);
		
		return ospe;
	}

	/**
	 * Aguarda a leitura de um produto v�lido.
	 * 
	 * @see #alertProdutoInvalido(int)
	 * @see #biparCodigo()
	 * 
	 * @return
	 * @throws IOException 
	 * @throws TrocarRecebimentoException 
	 */
	public Ordemservicoproduto biparProduto() throws IOException, TrocarRecebimentoException {
		while(true){
			drawEsqueleto(this.cabecalho,"Digite 0 para a��es.");
			makeCabecalhoColeta(); // Faz o cabe�alho da coleta
			
			Ordemservicoproduto ordemservicoproduto;
			
			if (TipoColeta.PADRAO.equals(tipoColeta))
				ordemservicoproduto = biparProduto(this.listaUma);
			else if (TipoColeta.FRACIONADA.equals(tipoColeta))
				ordemservicoproduto = biparProduto(this.listaFracionada);
			else //� avaria
				ordemservicoproduto = biparProduto(this.listaAvariada);
			
			if (exibirMenu){
				Acao acao = menuMaisAcoes();
				if (Acao.TrocarRecebimento.equals(acao))
					throw new TrocarRecebimentoException();
				else if (Acao.FinalizarEnderecamento.equals(acao))
					throw new TrocarRecebimentoException();
				else
					continue;
			}
			
			if(ordemservicoproduto == null)
				alertProdutoInvalido(15);
			else
				return ordemservicoproduto;
			
		}
	}
	
	/**
	 * Faz o endere�amento dos itens avariados
	 * 
	 * @return
	 * @throws IOException 
	 * @throws TrocarRecebimentoException 
	 */
	private void coletarProdutosAvariados() throws IOException, TrocarRecebimentoException {
		
		if (listaAvariada.size() <= 0){
			return;
		}
			
		final Ordemservico ordemservico = listaAvariada.get(0).getListaOrdemprodutoLigacao().iterator().next().getOrdemservico();
		iniciarExecucao(ordemservico);
		
		while(listaAvariada.size() > 0){

			try {
				String endereco = biparEndereco();
				if(endereco == null || endereco.equals("0")){
					Acao acao = menuMaisAcoes();
					if (Acao.TrocarRecebimento.equals(acao))
						throw new TrocarRecebimentoException();
					else
						continue;
				}
					
				Endereco aux = EnderecoService.getInstance().loadEnderecoByCodigoEtiqueta(endereco, this.deposito);
				if (aux.getEnderecostatus().equals(Enderecostatus.BLOQUEADO)){
					alertError("Este endere�o est� bloqueado.");
				} else if(isEnderecoAvariaValido(aux)){
					Ordemservicoproduto ordemservicoproduto = biparProduto();
					enderecarProdutoAvariado(ordemservicoproduto);
				}else
					alertError(ENDERECO_INVALIDO);
				
			} catch (IOException e) {
				//repassando exce��es de IO, sen�o haver� risco de ficar uma exce��o em loop.
				throw e;
			} catch (TrocarRecebimentoException e) {
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
				alertError(ENDERECO_INVALIDO);
			}
		}
	
		if (OrdemservicoprodutoService.getInstance().isTodosItensFinalizados(ordemservico)){
			Neo.getObject(TransactionTemplate.class).execute(new FinalizaExecucaoOS(ordemservico));
			alertConclusaoEnderecamento();
		}
	}

	/**
	 * Altera o status da ordem de servi�o para "Em execu��o" e associa o usu�rio � ordem.
	 * @param ordemservico
	 * @throws TrocarRecebimentoException 
	 */
	protected void iniciarExecucao(Ordemservico ordemservico) throws TrocarRecebimentoException {
		validarOrdemServico(ordemservico);
		
		ordemservico.setOrdemstatus(Ordemstatus.EM_EXECUCAO);
		OrdemservicoService.getInstance().atualizarStatusordemservico(ordemservico);		

		if (ordemservico.getListaOrdemServicoUsuario().isEmpty()){
			OrdemservicoUsuario osu = OrdemservicousuarioService.getInstance().associarUsuario(usuario, ordemservico);
			ordemservico.getListaOrdemServicoUsuario().add(osu);
		}else{
			OrdemservicoUsuario osu = ordemservico.getListaOrdemServicoUsuario().get(0);
			if (osu.getCdordemservicousuario() == null){
				ordemservico.getListaOrdemServicoUsuario().clear();
				
				if (ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.CONVOCACAO_ATIVA_COLETOR, null)){
					osu = OrdemservicousuarioService.getInstance().findByOrdemservico(ordemservico);
				}else
					osu = OrdemservicousuarioService.getInstance().associarUsuario(usuario, ordemservico);

				ordemservico.getListaOrdemServicoUsuario().add(osu);
			}
		}
	}
	
	/**
	 * Exibe a mensagem de conclus�o do endere�amento
	 * @throws IOException 
	 */
	protected void alertConclusaoEnderecamento() throws IOException {
		bell();
		writeOnCenter("Endere�amento conclu�do com sucesso.", null, true, true);
	}

	/**
	 * Conclui a ordem de servi�o dos endere�amentos avariados
	 * 
	 * @param lista 
	 * @throws IOException 
	 * @throws TrocarRecebimentoException 
	 */
	private void enderecarProdutoAvariado(Ordemservicoproduto osp) throws IOException, TrocarRecebimentoException {
		Ordemservico ordemservico = osp.getListaOrdemprodutoLigacao().iterator().next().getOrdemservico();
		
		validarOrdemServico(ordemservico);

		osp.setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_OK);
		OrdemservicoprodutoService.getInstance().atualizarStatus(osp);
		
		if(ordemservico.getListaOrdemServicoUsuario().isEmpty()){
			OrdemservicoUsuario osu = OrdemservicousuarioService.getInstance().associarUsuario(usuario, ordemservico);
			ordemservico.getListaOrdemServicoUsuario().add(osu);
		}
		

		//Coleta de avaria n�o precisa reservar entrada porque ela j� foi reservada.
		
		//Atualizando o hist�rico de coleta
		Ordemprodutohistorico oph = osp.getListaOrdemprodutohistorico().iterator().next();
		oph.setQtde(osp.getQtdeesperada());
		if (oph.getQtdeavaria() == null)
			oph.setQtdeavaria(0L);
		if (oph.getQtdefracionada() == null)
			oph.setQtdefracionada(0L);
		OrdemprodutohistoricoService.getInstance().atualizarQuantidades(oph );
		
		atualizarPaletesExecutados(ordemservico, osp.getQtdeesperada(), osp.getProduto());

		this.listaAvariada.remove(osp);
	}
	
	/**
	 * Atualiza o campo "pateles" na tabela OrdemServicoUsuario para a quantidades de paletes
	 * que o operador j� movimentou.
	 * 
	 * @param ordemservico
	 * @param qtde 
	 * @param produto 
	 * @throws IOException 
	 */
	protected void atualizarPaletesExecutados(Ordemservico ordemservico, Long qtde, Produto produto) {
		int numPaletes = 1;

		if (produto.getListaProdutoTipoPalete().size() > 0){
			// paletiza��o � calculada baseada em embalagens de recebimento
			Produtoembalagem embalagem = getEmbalagem(produto.getListaProdutoEmbalagem(), true);
			if (embalagem != null && embalagem.getQtde() != null)
				qtde = qtde / embalagem.getQtde();
			
			Long normaPaletizacao = produto.getListaProdutoTipoPalete().get(0).getLastro() *
										produto.getListaProdutoTipoPalete().get(0).getCamada();
			numPaletes = (int)Math.ceil(qtde.doubleValue() / normaPaletizacao.doubleValue());
		}
	
		OrdemservicoUsuario osu = ordemservico.getListaOrdemServicoUsuario().get(0);
		int paletesExecutados = osu.getPaletes() != null ? osu.getPaletes() : 0;
		osu.setPaletes(paletesExecutados + numPaletes);
		OrdemservicousuarioService.getInstance().atualizaPaletes(osu);
	}


	/**
	 * Executa o endere�amento do produto fracionado
	 * @param lista 
	 * 
	 * @param lista 
	 * @throws InsercaoInvalidaException 
	 * @throws TrocarRecebimentoException 
	 * @throws IOException 
	 */
	private void enderecarProduto(Ordemservicoprodutoendereco ospe) throws InsercaoInvalidaException, TrocarRecebimentoException {
		Ordemservico ordemservico = ospe.getOrdemservicoproduto().getListaOrdemprodutoLigacao().iterator().next().getOrdemservico();
		
		validarOrdemServico(ordemservico);
		
		Long qtdeFaltante = ospe.getOrdemservicoproduto().getQtdeFaltante() - ospe.getQtde();
		
		//Se o OSPE j� existe no BD e se a quantidade � diferente da esperada
		//Devo validar se o usu�rio reduziu a quantidade
		if (ospe.getCdordemservicoprodutoendereco() != null && !qtdeFaltante.equals(ospe.getOrdemservicoproduto().getQtdeesperada())){
			Ordemservicoprodutoendereco ospeAux = OrdemservicoprodutoenderecoService.getInstance().loadForEntrada(ospe);
			//Se a quantidade anterior n�o � igual � atual devo remover a reserva anterior e criar uma nova
			if (!ospe.getQtde().equals(ospeAux.getQtde())){
				ospeAux.setEnderecodestino(ospe.getEnderecodestino());
				ospeAux.setOrdemservicoproduto(ospe.getOrdemservicoproduto());
				ospeAux.setProdutoEmbalagem(ospe.getProdutoEmbalagem());
				EnderecoprodutoService.getInstance().removerQtdeReservada(ospeAux);
				EnderecoprodutoService.getInstance().reservarEntrada(ospe, false);

				ospe.setEnderecoorigem(EnderecoService.getInstance().findEnderecoBox(deposito));
				if (ospe.getEnderecoorigem() != null){
					EnderecoprodutoService.getInstance().validarSaidaBox(ospe, this.recebimento);
					EnderecoprodutoService.getInstance().reservarSaida(ospe, false);
				}
			}
		}
		//Se o item ainda n�o possui endere�o de origem vou associar o Box
		else if (ospe.getEnderecoorigem() == null){
			ospe.setEnderecoorigem(EnderecoService.getInstance().findEnderecoBox(deposito));
			if (ospe.getEnderecoorigem() != null){
				EnderecoprodutoService.getInstance().validarSaidaBox(ospe, this.recebimento);
				EnderecoprodutoService.getInstance().reservarSaida(ospe, false);
			}
		}
		
		//A reserva de estoque deve ser feita apenas para endere�amento padr�o,
		//pois endere�amento de fracionados j� reservou pelo procedure
		if (TipoColeta.PADRAO.equals(tipoColeta)){
			EnderecoprodutoService.getInstance().reservarEntrada(ospe, true);
			
			//Quando a coleta � do tipo padr�o sempre deve remover o OSPE da lista
			//mas quando � diferente de padr�o s� devo remover quando tive endere�ado tudo
			ospe.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().remove(ospe);
		}
		
		if(ordemservico.getListaOrdemServicoUsuario().isEmpty()){
			OrdemservicoUsuario osu = OrdemservicousuarioService.getInstance().associarUsuario(usuario, ordemservico);
			ordemservico.getListaOrdemServicoUsuario().add(osu);
		}
		
		ospe.getOrdemservicoproduto().setQtdeFaltante(qtdeFaltante);
		
		OrdemservicoprodutoenderecoService.getInstance().saveOrUpdateNoUseTransaction(ospe);
		
		//Atualizando o hist�rico de coleta
		Ordemprodutohistorico oph = ospe.getOrdemservicoproduto().getListaOrdemprodutohistorico().iterator().next();
		oph.setQtde(ospe.getOrdemservicoproduto().getQtdeesperada() - ospe.getOrdemservicoproduto().getQtdeFaltante());
		if (oph.getQtdeavaria() == null)
			oph.setQtdeavaria(0L);
		if (oph.getQtdefracionada() == null)
			oph.setQtdefracionada(0L);
		OrdemprodutohistoricoService.getInstance().atualizarQuantidades(oph );
		
		atualizarPaletesExecutados(ordemservico, ospe.getQtde(), ospe.getOrdemservicoproduto().getProduto());
		
		if(ospe.getOrdemservicoproduto().getQtdeFaltante() <= 0){
			if (!TipoColeta.PADRAO.equals(tipoColeta))
				ospe.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().remove(ospe);

			ospe.getOrdemservicoproduto().setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_OK);
			OrdemservicoprodutoService.getInstance().atualizarStatus(ospe.getOrdemservicoproduto());
			listaAvariada.remove(ospe.getOrdemservicoproduto());
			listaFracionada.remove(ospe.getOrdemservicoproduto());
			listaUma.remove(ospe.getOrdemservicoproduto());

			//esvaziando a lista para saber quando terminou
			for (Ordemprodutoligacao opl : ospe.getOrdemservicoproduto().getListaOrdemprodutoLigacao())
				ordemservico.getListaOrdemProdutoLigacao().remove(opl);
		}
		
	}
	
	/**
	 * Exibe a mensagem informando que o produto n�o � v�lido
	 * 
	 * @throws IOException 
	 */
	protected void alertProdutoInvalido(int row) throws IOException{
		alertError("Produto inv�lido.");
	}

	/**
	 * Mostra as informa��es do produto na tela
	 * 
	 * @throws IOException 
	 */
	protected void showInformationProduto(Ordemservicoproduto osp, boolean exibirQtde) throws IOException {
		Produto produto = osp.getProduto();

		if (produto.getProdutoprincipal() != null){
			writeLine(produto.getCodigo() 
					+ " - " 
					+ produto.getProdutoprincipal().getDescricao()
					+ " - "
					+ produto.getDescricao());
		}else
			writeLine(produto.getCodigo() + " - " + produto.getDescricao());
		
		if(osp.getCodigoBarrasLido() != null){//Se foi bipado o c�digo de barras
			writeLine("Cod. Barra: " + osp.getCodigoBarrasLido().getCodigo());
		}
		
		//Endere�o de Picking do Produto...
		Dadologistico dadologistico  = DadologisticoService.getInstance().findByProduto(produto, deposito);
		if (dadologistico!=null && dadologistico.getEndereco()!=null){
			writeLine("End. Picking: "+dadologistico.getEndereco().getEnderecoArea());
		}			
		
		List<Produtotipopalete> listaProdutoTipoPalete = produto.getListaProdutoTipoPalete();
		if(listaProdutoTipoPalete == null || listaProdutoTipoPalete.isEmpty()){
			writeLine("Nenhum palete cadastrado.", ColorHelper.RED, false);
		}else{
			String norma = "";
			Produtotipopalete produtotipopalete = listaProdutoTipoPalete.get(0);
			norma = produtotipopalete.getLastro() + " X " + produtotipopalete.getCamada();
			writeLine("Norma: " + norma);
		}

		Produtoembalagem embalagem = getEmbalagem(produto.getListaProdutoEmbalagem(), TipoColeta.PADRAO.equals(tipoColeta));
		
		if(embalagem == null){
			writeLine("Nenhuma embalagem cadastrada.", ColorHelper.RED, false);
		}else{
			writeLine("Embalagem: " + embalagem.getDescricao());
		}
		
		if (TipoColeta.PADRAO.equals(tipoColeta) && exibirQtde){
			if(embalagem != null){
				writeLine("Qtde esperada: " + osp.getQtdeFaltante() / embalagem.getQtde());
			}else{
				writeLine("Qtde esperada: " + osp.getQtdeFaltante());
			}
		}else if(exibirQtde){
			writeLine("Qtde. esperada: " + osp.getQtdeFaltante());
		}
		
		writeSeparator();
	}
	

	private Produtoembalagem getEmbalagem(List<Produtoembalagem> listaProdutoEmbalagem, boolean embalagemCompra) {
		if(listaProdutoEmbalagem != null && listaProdutoEmbalagem.size() > 0){
			if (listaProdutoEmbalagem.size() == 1)
				return listaProdutoEmbalagem.get(0);
			
			for (Produtoembalagem emb : listaProdutoEmbalagem)
				if (emb.getCompra().equals(embalagemCompra))
					return emb;
		}
		return null;
	}

	
	/**
	 * Verifica se o endere�o � v�lido
	 * 
	 * @param aux
	 * @param ordemservicoproduto
	 * @return
	 */
	private boolean isEnderecoAvariaValido(Endereco aux) {
		for (Ordemservicoproduto ordemservicoproduto : this.listaAvariada){
			for(Ordemservicoprodutoendereco ospe : ordemservicoproduto.getListaOrdemservicoprodutoendereco()){
				Endereco enderecodestino = ospe.getEnderecodestino();
				if(EnderecoService.getInstance().predioEquals(enderecodestino, aux)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Cria o cabecalho da coleta
	 * 
	 * @throws IOException 
	 * 
	 */
	protected void makeCabecalhoColeta() throws IOException {
		
		Recebimentonotafiscal next = recebimento.getListaRecebimentoNF().iterator().next();
		writeOnCenter(getTitulo(), null, false, false);
		writeSeparator();
		writeLine("Recebimento: " + this.recebimento.getCdrecebimento());
		writeLine("Box: " + this.recebimento.getBox().getNome());
		writeLine("Ve�culo: " + next.getNotafiscalentrada().getVeiculo());
		writeSeparator();
	}
	
	/**
	 * Mostra o endereco na tela
	 * @throws IOException 
	 * 
	 */
	protected void showEndereco(Ordemservicoprodutoendereco ospe) throws IOException{
		writeLine("Endere�o destino: ");
		writeLine(ospe.getEnderecodestino().getEnderecoArea());
		writeSeparator();
	}

	/**
	 * Cria o menu e retorna um n�mero dependendo da escolha: 
	 * 	 1 - Armazenar UMA
	 * 	 2 - Armazenar Fracionados
	 * 	 3 - Armazenar Avariados
	 * @return
	 * @throws IOException 
	 */
	private TipoColeta makeMenuEnderecamentoManual() throws IOException {
		TipoColeta opcao = null;
		int uma,fracionados,avariados;
		int maxValueMenu = 0;
		
		drawEsqueleto("Escolha a armazenagem", "Digite 0 para sair.");
		writeOnCenter(getTitulo(), null, false, false);
		writeSeparator();
		writeLine("");

		LinkedHashMap<Integer, String> mapaMenu = new LinkedHashMap<Integer, String>();
		
		if(this.listaUma != null && !this.listaUma.isEmpty()){ // Se h� produtos UMA
			mapaMenu.put(1, "Armazenagem padr�o");
			uma = 1;
			maxValueMenu = uma;
		}else uma = -1;
		
		if(this.listaFracionada != null && !this.listaFracionada.isEmpty()){ // Se h� produtos fracionados
			fracionados = uma == 1 ? 2 : 1; // Se h� fracionados e UMA = 2 sen�o 1;
			maxValueMenu = fracionados;
			mapaMenu.put(fracionados, "Armazenar fracionados");
		}else fracionados = -1;			
			
		if(this.listaAvariada != null && !this.listaAvariada.isEmpty()){ // Se h� produtos avariados
			avariados = fracionados > 0 ? fracionados + 1 : uma > 0 ? 2 : 1;
			maxValueMenu = avariados;
			mapaMenu.put(avariados, "Armazenar avariados");
		}else avariados = -1;
		
		drawMenu(mapaMenu); // Desenha o menu
		
		int opcaoEscolhida = readInteger("", maxValueMenu);
		
		if (opcaoEscolhida == 0){
			Acao acao = menuMaisAcoes();
			if (Acao.Voltar.equals(acao))
				return makeMenuEnderecamentoManual();
		}

		if (opcaoEscolhida == uma) {
			opcao = TipoColeta.PADRAO;
			this.cabecalho = mapaMenu.get(uma);
		} else if (opcaoEscolhida == fracionados) {
			opcao = TipoColeta.FRACIONADA;
			this.cabecalho = mapaMenu.get(fracionados);
		} else if (opcaoEscolhida == avariados) {
			opcao = TipoColeta.AVARIA;
			this.cabecalho = mapaMenu.get(avariados);
		}
		
		return opcao;
	}

	/**
	 * Carrega as listas dos produtos de tipo de enderecamento
	 * Fracionado, Avariado e de UMA
	 * 
	 */
	private void loadFracionadaAvariaUma() {
		this.listaAvariada = OrdemservicoprodutoService.getInstance().loadByOrdemTipo(this.recebimento,Ordemtipo.ENDERECAMENTO_AVARIADO, this.usuario);
		this.listaFracionada = OrdemservicoprodutoService.getInstance().loadByOrdemTipo(this.recebimento,Ordemtipo.ENDERECAMENTO_FRACIONADO, this.usuario);
		this.listaUma = OrdemservicoprodutoService.getInstance().loadByOrdemTipo(this.recebimento,Ordemtipo.ENDERECAMENTO_PADRAO, null);

		/* Estava implementando este recurso, mas surgiram muitas implica��es e tive de parar. Caso deseje continuar o 
		 * trabalho ser� necess�rio testar e tratar o endere�amento avariado de forma especial, pois a ordem aqui criada ter� 
		 * como quantidade a endere�ara a quantidade total do recebimento, como o coletor n�o solicita quantidade isso far� o 
		 * sistema registrar um endere�amento de avaria com a quantidade esperada.
		 * 
		 * Giovane Freitas
		 * 
		
		this.recebimento.setDeposito(deposito);
		if (this.listaAvariada == null || this.listaAvariada.isEmpty())
			this.listaAvariada = OrdemservicoprodutoService.getInstance().criarOrdemParaColetor(this.recebimento,Ordemtipo.ENDERECO_AVARIADO);
		if (this.listaFracionada == null || this.listaFracionada.isEmpty())
			this.listaFracionada = OrdemservicoprodutoService.getInstance().criarOrdemParaColetor(this.recebimento,Ordemtipo.ENDERECO_FRACIONADO);
		*/
		
		popularQtdeFaltante(this.listaAvariada, false);
		popularQtdeFaltante(this.listaFracionada, false);
		popularQtdeFaltante(this.listaUma, true);
	}

	private void popularQtdeFaltante(List<Ordemservicoproduto> listaOSP, boolean uma) {
		//O Neo n�o ajudou neste ponto, cada OrdemProdutoLigacao est� relacionado a um objeto OrdemServico diferente, mas todos com o mesmo ID
		Map<Ordemservico, ListSet<Ordemprodutoligacao>> oplMap = new HashMap<Ordemservico, ListSet<Ordemprodutoligacao>>();
		Map<Integer, Ordemservico> osMap = new HashMap<Integer, Ordemservico>();
		
		for (Ordemservicoproduto osp : listaOSP){
			osp.setQtdeFaltante(osp.getQtdeesperada());
			
			Ordemservico os = osp.getListaOrdemprodutoLigacao().iterator().next().getOrdemservico();
			if (!oplMap.containsKey(os))
				oplMap.put(os, new ListSet<Ordemprodutoligacao>(Ordemprodutoligacao.class));
			if (!osMap.containsKey(os.getCdordemservico()))
				osMap.put(os.getCdordemservico(), os);
				
			oplMap.get(os).addAll(osp.getListaOrdemprodutoLigacao());
			
			boolean isEnderecamentoPadrao = Ordemtipo.ENDERECAMENTO_PADRAO.equals((osp.getListaOrdemprodutoLigacao().iterator().next().getOrdemservico().getOrdemtipo()));
			
			for (Ordemservicoprodutoendereco ospe : osp.getListaOrdemservicoprodutoendereco()){
				ospe.setOrdemservicoproduto(osp);
			
				if (Tipoenderecamento.MANUAL.equals(recebimento.getTipoenderecamento()) && isEnderecamentoPadrao && !uma)
					osp.setQtdeFaltante(osp.getQtdeFaltante() - ospe.getQtde());
			}
			
			Ordemprodutohistorico oph = OrdemprodutohistoricoService.getInstance().findByOSP(osp);
			osp.getListaOrdemprodutohistorico().add(oph);
			
			if (Tipoenderecamento.MANUAL.equals(recebimento.getTipoenderecamento()) && !isEnderecamentoPadrao && !uma){
				long qtde = oph.getQtde() != null ? oph.getQtde() : 0L;
				long qtdeAvaria = oph.getQtdeavaria() != null ? oph.getQtdeavaria() : 0L;
				long qtdeFracionada = oph.getQtdefracionada() != null ? oph.getQtdefracionada() : 0L;
				osp.setQtdeFaltante(osp.getQtdeesperada() - qtde - qtdeAvaria - qtdeFracionada);
			}
		}
		
		//Removendo os outros usu�rios da lista, para deixar apenas o usu�rio logado
		for (Ordemservico os : osMap.values()){
			if (os.getListaOrdemServicoUsuario() != null){
				Iterator<OrdemservicoUsuario> iterator = os.getListaOrdemServicoUsuario().iterator();
				while (iterator.hasNext()){
					if (!iterator.next().getUsuario().equals(usuario)){
						iterator.remove();
					}
				}
			}
		}
		
		for (Ordemservicoproduto osp : listaOSP){
			//Como o Neo fica louco quando tem excesso de listas na query, tive de fazer o relacionamento ap�s consultar o BD
			Ordemservico os = osp.getListaOrdemprodutoLigacao().iterator().next().getOrdemservico();

			osp.getListaOrdemprodutoLigacao().iterator().next().setOrdemservico(osMap.get(os.getCdordemservico()));
			
			//Trocando o objeto lista da Ordemservico para que todos apontem para a mesma inst�ncia
			os.setListaOrdemProdutoLigacao(oplMap.get(os));
		}
	}

	/**
	 * Verifica se o recebimento possui todos os pr�-requisitos para a execu��o
	 * do enderecamento manual.
	 * 
	 * @param valor
	 * @return
	 * @throws IOException 
	 */
	private boolean isRecebimentoOk(Integer recebimento) throws IOException {
		try {
			this.recebimento = RecebimentoService.getInstance().findForEnderecamento(new Recebimento(recebimento),deposito, getTipoEnderecamento());
			if (this.recebimento != null)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			
			writeLine("");
			writeOnCenter(RECEBIMENTO_INDISPONIVEL, ColorHelper.RED, false, true);
			return false;
		}
		
		writeLine("");
		writeOnCenter(RECEBIMENTO_INDISPONIVEL, ColorHelper.RED, false, true);
		return false;
	}

	protected Tipoenderecamento getTipoEnderecamento() {
		return Tipoenderecamento.MANUAL;
	}

	/**
	 * Exibe as informa��es do recebimento que foi encontrado na tela.
	 * 
	 * @throws IOException
	 * @author Pedro Gon�alves
	 * @author Leonardo Guimar�es
	 */
	private void showInformationsRecebimento() throws IOException {
		Recebimentonotafiscal next = recebimento.getListaRecebimentoNF().iterator().next();
		drawEsqueleto("");
		
		writeOnCenter(getTitulo(), null, false, false);
		writeSeparator();
		writeLine("");
		writeLine("Recebimento: " + recebimento.getCdrecebimento());
		writeLine("");
		writeLine("Box: " + recebimento.getBox().getNome());
		writeLine("");
		writeLine("Ve�culo: " + next.getNotafiscalentrada().getVeiculo());
		read();
	}
	
	/**
	 * 1) Ao clicar em "0" sistema deve mostrar tela de mais a��es com op��es de
	 * "Finalizar endere�amento", "Cancelar", "Trocar recebimento" e "Voltar".<br/>
	 * 1.1) Finalizar endere�amento: Status da ordem de endere�amento deve ser
	 * alterado para "Finalizado com Sucesso" ou "Finalizado com diverg�ncia".<br/>
	 * 1.2) Cancelar: As altera��es feitas via coletor n�o devem ser
	 * registradas. Ordem de endere�amento fica com situa��o "Em aberto". <br/>
	 * 1.3) Trocar recebimento: Sistema registra as altera��es feitas via coletor,
	 * sai do recebimento (no RF) e a ordem fica com situa��o "Em execu��o"
	 * permitindo que o endere�amento possa ser continuado. <br/>
	 * 1.4) Voltar: Volta para o endere�amento.
	 * 
	 * @author Giovane Freitas
	 * @throws IOException 
	 */
	public Acao menuMaisAcoes() throws IOException {
		drawEsqueleto("");
		writeLine("Selecione uma a��o: ");
		writeLine("");
		
		final HashMap<Integer, String> mapaMenu = new HashMap<Integer, String>();
		mapaMenu.put(0, "Voltar");
		
		if (isConvocacaoAtiva())			
			mapaMenu.put(1, "Sair");
		else
			mapaMenu.put(1, "Trocar de recebimento");

		mapaMenu.put(2, "Finalizar endere�amento");
		
		/*
		mapaMenu.put(3, "Cancelar opera��o");
		
		 */	
		
		Set<Entry<Integer, String>> entrySet = mapaMenu.entrySet();
		
		for (Entry<Integer, String> entry : entrySet) {
			writeLine(entry.getKey()+" - "+entry.getValue());
		}
		
		getTermIO().write(BasicTerminalIO.CRLF);
		Editfield optDep=new Editfield(getTermIO(),"editfield 3",2);
		optDep.registerInputValidator(new InputValidator(){
			public boolean validate(String str) {
				if(str == null || str.equals(""))
					return true;
				
				try{
					int option = Integer.parseInt(str);
					if(option <= (mapaMenu.size() - 1) && option >= 0){
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
		
		if (value == null || value.trim().isEmpty()){
			return menuMaisAcoes();
		}
		
		
		if ("0".equals(value)) {//Voltar
			return Acao.Voltar;

		} else if ("1".equals(value)) {//Trocar de recebimento
			if (isConvocacaoAtiva())
				logout();
			
			return Acao.TrocarRecebimento;
		} else if ("2".equals(value)) {//Finalizar endere�amento
			finalizarEnderecamento();
			return Acao.FinalizarEnderecamento;
		} else if ("3".equals(value)) {//Cancelar coleta
			cancelarOperacao();
			return Acao.CancelarOperacao;
		} else 
			return Acao.Nenhuma;
		
	}

	
	/**
	 * Finaliza a execu��o da ordem de servi�o atual.
	 * @author Giovane Freitas
	 * @throws IOException 
	 */
	private void finalizarEnderecamento() throws IOException {
		List<Ordemservico> list = OrdemservicoService.getInstance().findOSByUsuario(usuario, recebimento);
		
		boolean existeItemOutroOperador = false;
		
		for (Ordemservico ordem : list){
			if (OrdemservicoprodutoService.getInstance().isTodosItensFinalizados(ordem)){
				Neo.getObject(TransactionTemplate.class).execute(new FinalizaExecucaoOS(ordem));
			} else {
				existeItemOutroOperador = true;
			}
		}

		if (!existeItemOutroOperador)
			alertConclusaoEnderecamento();
		else
			alertError("Ainda existem opera��es em execu��o por outro operador.");

		loadFracionadaAvariaUma(); // Carrega as listas
	}

	/**
	 * Cancela a execu��o da ordem de servi�o atual.
	 * @author Giovane Freitas
	 */
	private void cancelarOperacao() {
//		List<Ordemservico> list = OrdemservicoService.getInstance().findOSByUsuario(this.usuario, Ordemstatus.EM_EXECUCAO, 
//				Ordemtipo.ENDERECO_AVARIADO, Ordemtipo.ENDERECO_FRACIONADO, Ordemtipo.ENDERECO_PADRAO);
		
		//implementar esta fun��o
		
		//Validar se a ordem de servi�o foi exclu�da
		
//		loadFracionadaAvariaUma(); // Carrega as listas
	}
	
	@Override
	public String getTitulo() {
		return TITULO;
	}

	protected void validarOrdemServico(Ordemservico ordemservico) throws TrocarRecebimentoException{
		Ordemservico ordemAux = OrdemservicoService.getInstance().load(ordemservico);
		if (ordemAux == null)
			throw new TrocarRecebimentoException("O endere�amento foi cancelado.");
		if (ordemAux.getOrdemstatus().equals(Ordemstatus.FINALIZADO_DIVERGENCIA) || ordemAux.getOrdemstatus().equals(Ordemstatus.FINALIZADO_SUCESSO))
			throw new TrocarRecebimentoException("Esta ordem de endere�amento j� foi finalizada.");
		if (ordemAux.getOrdemstatus().equals(Ordemstatus.CANCELADO) )
			throw new TrocarRecebimentoException("Esta ordem de endere�amento foi cancelada.");
			
	}

}
