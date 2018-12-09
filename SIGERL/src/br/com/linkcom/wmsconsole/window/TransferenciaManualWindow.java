package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Dadologistico;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecoproduto;
import br.com.linkcom.wms.geral.bean.Enderecostatus;
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
import br.com.linkcom.wms.geral.bean.Transferencia;
import br.com.linkcom.wms.geral.bean.Transferenciaitem;
import br.com.linkcom.wms.geral.bean.Transferenciastatus;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.DadologisticoService;
import br.com.linkcom.wms.geral.service.EnderecoService;
import br.com.linkcom.wms.geral.service.EnderecoprodutoService;
import br.com.linkcom.wms.geral.service.OrdemprodutoligacaoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoenderecoService;
import br.com.linkcom.wms.geral.service.OrdemservicousuarioService;
import br.com.linkcom.wms.geral.service.ProdutoService;
import br.com.linkcom.wms.geral.service.ProdutoembalagemService;
import br.com.linkcom.wms.geral.service.TransferenciaService;
import br.com.linkcom.wms.geral.service.TransferenciaitemService;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wmsconsole.system.ExecucaoOSWindow;

import com.ibm.icu.util.Calendar;

public class TransferenciaManualWindow extends ExecucaoOSWindow {

	private static final String PROMPT_QTDE = "Qtde. a transferir:";
	private static final String QTDE_INVALIDA = "A quantidade informada � maior que a quantidade dispon�vel.";
	private static final String PRODUTO_INVALIDO = "Produto inv�lido.";
	private static final String ENDERECO_INVALIDO = "Endere�o inv�lido.";
	private static final String ENDERECO_DE_ORIGEM = "Endere�o de origem:";
	private static final String INFORME_A_UMA = "Informe a UMA:";
	private static final String UMA_INVALIDA = "UMA inv�lida.";
	private static final String ENDERECO_PRODUTO_INVALIDO = "O endere�o n�o possui estoque do produto especificado.";
	private static final String ENDERECO_DE_DESTINO = "Endere�o de destino:";
	private static final String DIGITE_0_PARA_OPCOES = "Digite 0 para op��es.";
	private static final String TITULO = "Transfer�ncia manual";

	private Ordemservico ordemservico;
	private OrdemservicoUsuario ordemservicoUsuario;
	private final EnderecoprodutoService enderecoprodutoService;
	private final EnderecoService enderecoService;
	private final ProdutoembalagemService produtoembalagemService;
	private final ProdutoService produtoService;
	private final DadologisticoService dadologisticoService;
	private boolean lerUMA;
	private boolean exigirCodigoBarras = true;
	protected Timestamp dtinicio;

	public TransferenciaManualWindow(){
		enderecoprodutoService = EnderecoprodutoService.getInstance();
		enderecoService = EnderecoService.getInstance();
		produtoembalagemService = ProdutoembalagemService.getInstance();
		produtoService = ProdutoService.getInstance();
		exigirCodigoBarras = ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS, deposito);
		dadologisticoService = DadologisticoService.getInstance();
	}
	
	@Override
	public void draw() throws IOException {
		executarOrdem(null);
	}

	/**
	 * Executa uma nova transfer�ncia manual.
	 * 
	 * @author Giovane Freitas
	 * @see br.com.linkcom.wmsconsole.system.ExecucaoOSWindow#executarOrdem(br.com.linkcom.wms.geral.bean.Ordemservico)
	 */
	@Override
	public void executarOrdem(Ordemservico ordemservico) throws IOException {
		this.ordemservico = ordemservico;
		
		dtinicio = new Timestamp(Calendar.getInstance().getTimeInMillis());
		
		lerUMA = true;
		do {
			showCabecalho(DIGITE_0_PARA_OPCOES);
			
			String uma = readBarcode(lerUMA ? INFORME_A_UMA : ENDERECO_DE_ORIGEM);
			
			//Menu de a��es
			if ("0".equals(uma)){
				if (continuarExecucao(true))
					continue;
				else
					break;
			}

			Endereco origem;
			Produto produto;
			Enderecoproduto enderecoproduto;
			
			if (lerUMA){
				try{
					enderecoproduto = enderecoprodutoService.loadForEntrada(new Enderecoproduto(Integer.parseInt(uma)));
					if (enderecoproduto == null){
						alertError(UMA_INVALIDA);
						continue;
					}
					
					origem = enderecoproduto.getEndereco();
					produto = enderecoproduto.getProduto();
					
				}catch (Exception e) {
					e.printStackTrace();
					alertError(UMA_INVALIDA);
					continue;
				}
			} else{
				try{
					origem = enderecoService.loadEnderecoByCodigoEtiqueta(uma, this.deposito);
					if (origem == null){
						alertError(ENDERECO_INVALIDO);
						continue;
					}
					
					if (origem.getArea().getBox()){
						alertError(ENDERECO_INVALIDO + " �rea de Box.");
						continue;						
					}
				}catch(Exception e){
					e.printStackTrace();
					alertError(ENDERECO_INVALIDO);
					continue;					
				}
				
				enderecoproduto = lerProduto(origem);
				
				//se o enderecoproduto retornou null � porque o usu�rio desistiu da opera��o.
				if (enderecoproduto == null)
					break;
				
				produto = enderecoproduto.getProduto();
			}
			
			Produtoembalagem embcompra = produtoembalagemService.findCompraByProduto(produto);
			int qtdeFracionada = (int)((enderecoproduto.getQtde().intValue() - enderecoproduto.getQtdereservadasaida().intValue()) % embcompra.getQtde());
			int qtdePadrao = (int)((enderecoproduto.getQtde().intValue() - enderecoproduto.getQtdereservadasaida().intValue()) / embcompra.getQtde());
			
			Produtoembalagem produtoembalagem;
			
			if (qtdeFracionada > 0 && qtdePadrao == 0){//Se h� quantidade fracionada mas n�o h� em embalagem padr�o
				produtoembalagem = produtoembalagemService.findMenorEmbalagem(produto);
			}else if (qtdeFracionada > 0 && qtdePadrao > 0){//Se h� qtde fracionada e h� embalagem padr�o
				Produtoembalagem menorEmbalagem = produtoembalagemService.findMenorEmbalagem(produto);
				
				HashMap<String, Object> opcoes = new LinkedHashMap<String, Object>();
				opcoes.put(embcompra.getDescricaoEmbalagem(), embcompra);
				opcoes.put(menorEmbalagem.getDescricaoEmbalagem(), menorEmbalagem);
				produtoembalagem = (Produtoembalagem) makeMenuByHash(opcoes , "Selecione a embalagem:", 10);
				
				//Se o usu�rio mandou sair
				if (produtoembalagem == null){
					continue;
				}
			}else//H� somente qtde padr�o
				produtoembalagem = embcompra;

			int qtdeMaxima = (int)((enderecoproduto.getQtde().intValue() - enderecoproduto.getQtdereservadasaida().intValue()) / produtoembalagem.getQtde());
			
			//N�o � permtido movimentar endere�os bloqueados ou que n�o tenha quantidade dispon�vel
			if (origem.getEnderecostatus().equals(Enderecostatus.BLOQUEADO) || qtdeMaxima <= 0){
				alertError(ENDERECO_INVALIDO);
				continue;
			}
			
			//O sistema solicita que o usu�rio informe o endere�o de destino;
			Endereco destino = lerDestino(origem, produto, enderecoproduto, produtoembalagem);
			
			//se o endere�o retornou null � porque o usu�rio desistiu da opera��o.
			if (destino == null)
				break;
			
			Integer qtdeTransferir = lerQtde(origem, destino, produto, enderecoproduto, produtoembalagem, qtdeMaxima);
			
			//se a qtde retornoda � null � porque o usu�rio desistiu da opera��o.
			if (qtdeTransferir == null)
				break;
			
			Long qtde = qtdeTransferir * produtoembalagem.getQtde();
			criarTransferenciaItem(origem, destino, produto, qtde);
			
		} while (true);
		
	}

	/**
	 * Aguarda a leitura de um produto v�lido.
	 * 
	 * @param origem
	 * @return
	 * @throws IOException
	 */
	private Enderecoproduto lerProduto(Endereco origem) throws IOException {
		do {
			showCabecalho(DIGITE_0_PARA_OPCOES);
			writeLine("Origem: " + origem.getEnderecoArea());
			writeSeparator();
			
			String barcode = readCodigoProduto();
			
			//Menu de a��es
			if ("0".equals(barcode)){
				if (continuarExecucao(true))
					continue;
				else
					return null;
			}
	
			Produto produto = produtoService.findProdutoByBarcode(barcode, deposito, !exigirCodigoBarras);
			if (produto == null){
				alertError(PRODUTO_INVALIDO);
				continue;
			}

			Enderecoproduto enderecoproduto = enderecoprodutoService.loadByEnderecoEProdutoUma(origem, produto);
			if (enderecoproduto == null){
				alertError(ENDERECO_PRODUTO_INVALIDO);
				continue;
			}else{
				enderecoproduto.setProduto(produto);
				return enderecoproduto;
			}
			
		}while (true);
	}

	/**
	 * Aguarda at� que o usu�rio informe uma quantidade v�lida.
	 * 
	 * @param qtdeMaxima
	 * @return
	 * @throws IOException
	 */
	private Integer lerQtde(Endereco origem, Endereco destino, Produto produto, Enderecoproduto enderecoproduto, Produtoembalagem produtoembalagem, int qtdeMaxima) throws IOException {
		do {
			showOrigem(origem, produto, enderecoproduto, produtoembalagem);
			writeLine("Destino: " + destino.getEnderecoArea());
			writeSeparator();
			
			Integer qtdeTransferir = readInteger(PROMPT_QTDE, Integer.MAX_VALUE);
			
			//Menu de a��es
			if (qtdeTransferir == null || qtdeTransferir.equals(0)){
				if (continuarExecucao(false))
					continue;
				else
					return null;
			}
			
			if (qtdeTransferir.intValue() > qtdeMaxima){
				alertError(QTDE_INVALIDA);
				continue;
			}else{
				return qtdeTransferir;
			}
		} while(true);
	}

	/**
	 * Aguarda a leitura de um endere�o de destino v�lido.
	 * 
	 * @param origem
	 * @param produto
	 * @param enderecoproduto
	 * @param produtoembalagem
	 * @return
	 * @throws IOException
	 */
	private Endereco lerDestino(Endereco origem, Produto produto, Enderecoproduto enderecoproduto, Produtoembalagem produtoembalagem) throws IOException {
		Endereco destino;
		
		do {
			showOrigem(origem, produto, enderecoproduto, produtoembalagem);
			try{
				String enderecoStr = readBarcode(ENDERECO_DE_DESTINO);
				
				//Menu de a��es
				if ("0".equals(enderecoStr)){
					if (continuarExecucao(false))
						continue;
					else
						break;
				}
				
				destino = enderecoService.loadEnderecoByCodigoEtiqueta(enderecoStr, this.deposito);
				if (destino == null || destino.equals(origem)){
					alertError(ENDERECO_INVALIDO);
					continue;
				}
				
				if (destino.getArea().getBox()){
					alertError(ENDERECO_INVALIDO + " �rea de Box.");
					continue;		
				}
				
				Dadologistico dadologistico = dadologisticoService.findByProduto(produto, deposito);

				//Se � um endere�o de picking, mas n�o � o picking do produto lido
				if (destino.getEnderecofuncao().equals(Enderecofuncao.PICKING) &&
						!dadologistico.getEndereco().equals(destino)){
					
					alertError(ENDERECO_INVALIDO);
					continue;
				}
				
				// Se o produto tem a caracter�stica de largura excedente, o
				// endere�o de destino deve ter essa caracter�stica tamb�m, e o apto
				// deve ser �mpar.
				if (dadologistico.getLarguraexcedente()){
					//Se o destino n�o tem largura excedente ou se � PAR
					if (!destino.getLarguraexcedente() || (destino.getApto() % 2 == 0)){
						destino = null;
						alertError(ENDERECO_INVALIDO);
						continue;
					}else
						return destino;
				}else
					return destino;
				
			}catch(Exception e){
				e.printStackTrace();
				alertError(ENDERECO_INVALIDO);
				continue;					
			}
			
		} while (true);
		
		return null;
	}

	private void showOrigem(Endereco origem, Produto produto, Enderecoproduto enderecoproduto, Produtoembalagem produtoembalagem)
			throws IOException {
		
		showCabecalho(DIGITE_0_PARA_OPCOES);
		
		writeLine("UMA: " + enderecoproduto.getCdenderecoproduto());
		writeLine("Origem: " + origem.getEnderecoArea());
		writeLine("Produto: " + produto.getDescriptionProperty());
		writeLine("Embalagem: " + produtoembalagem.getDescricaoEmbalagem());
		writeLine("Qtde.: " + (enderecoproduto.getQtde() - enderecoproduto.getQtdereservadasaida()) / produtoembalagem.getQtde());
		writeSeparator();
	}

	/**
	 * Verifica se o usu�rio deseja continuar a execu��o ou se deseja
	 * interromper. Tamb�m permite alterar configura��es como, se a leitura
	 * dever� ser por UMA ou por endere�o.
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean continuarExecucao(boolean permitirTrocarFormaLeitura) throws IOException {
		showCabecalho("");
		writeLine("Selecione uma op��o:");
		writeLine("");
		
		final int VOLTAR = 0;
		final int FINALIZAR = 1;
		final int CANCELAR = 2;
		final int LER_UMA = 3;
		final int LER_ENDERECO = 4;

		LinkedHashMap<Integer, String> menu = new LinkedHashMap<Integer, String>();
		menu.put(VOLTAR, "Voltar");
		menu.put(FINALIZAR, "Finalizar transfer�ncia");
		menu.put(CANCELAR, "Cancelar transfer�ncia");

		if (permitirTrocarFormaLeitura){
			menu.put(-1, "");
			menu.put(LER_UMA, "Ler UMA");
			menu.put(LER_ENDERECO, "Ler Endere�o");
		}
		
		drawMenu(menu );
		Integer opcao = readInteger("", menu.size() - 1);
		
		if (opcao == VOLTAR){
			return true;
		} else if (opcao == LER_UMA){
			lerUMA = true;
			return true;
		} else if (opcao == LER_ENDERECO){
			lerUMA = false;
			return true;
		} else if (opcao == FINALIZAR){
			if (this.ordemservico != null){
				finalizarTransferencia();
				writeOnCenter("Transfer�ncia finalizada com sucesso.", null, true, true);
				return false;
			}else{
				alertError("N�o h� movimenta��o para finalizar.");
				return continuarExecucao(permitirTrocarFormaLeitura);
			}
		} else if (opcao == CANCELAR){
			cancelarExecucao();
			writeOnCenter("Transfer�ncia cancelada.", null, true, true);
			return false;
		} else
			return true;
	}

	/**
	 * Cancela a ordem de servi�o e desfaz as reservas de estoque.
	 * 
	 * @author Giovane Freitas
	 */
	private void cancelarExecucao() {
		if (ordemservico != null){
			//Cancela a ordem e desfaz as reservas de estoque
			Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){
				public Object doInTransaction(TransactionStatus status) {
					OrdemservicoService.getInstance().cancelar(ordemservico);
					
					ordemservico.getTransferencia().setTransferenciastatus(Transferenciastatus.CANCELADO);
					TransferenciaService.getInstance().saveOrUpdate(ordemservico.getTransferencia());
					
					return null;
				}
			});
		}
	}

	/**
	 * Cria um item de transfer�ncia e reserva o estoque de sa�da e de entrada
	 * dos respectivos endere�os.
	 * 
	 * @author Giovane Freitas
	 */
	private void criarTransferenciaItem(final Endereco origem, final Endereco destino, final Produto produto, final Long qtde) {
		Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				if (ordemservico == null){
					Transferencia transferencia = new Transferencia();
					transferencia.setDeposito(deposito);
					transferencia.setDttransferencia(dtinicio);
					transferencia.setTransferenciastatus(Transferenciastatus.EM_EXECUCAO);
					TransferenciaService.getInstance().saveOrUpdate(transferencia);

					ordemservico = new Ordemservico();
					ordemservico.setDeposito(deposito);
					ordemservico.setOrdem(1);
					ordemservico.setOrdemstatus(Ordemstatus.EM_EXECUCAO);
					ordemservico.setOrdemtipo(Ordemtipo.TRANSFERENCIA);
					ordemservico.setTransferencia(transferencia);
					OrdemservicoService.getInstance().saveOrUpdate(ordemservico);
					
					ordemservicoUsuario = new OrdemservicoUsuario();
					ordemservicoUsuario.setDtinicio(dtinicio);
					ordemservicoUsuario.setOrdemservico(ordemservico);
					ordemservicoUsuario.setPaletes(0);
					ordemservicoUsuario.setUsuario(usuario);
					
					ordemservico.getListaOrdemServicoUsuario().add(ordemservicoUsuario);
				}
		
				//Criando o Transferenciaitem
				Transferenciaitem transferenciaitem = new Transferenciaitem();
				transferenciaitem.setEnderecoorigem(origem);
				transferenciaitem.setEnderecodestino(destino);
				transferenciaitem.setProduto(produto);
				transferenciaitem.setQtde(qtde);
				transferenciaitem.setTransferencia(ordemservico.getTransferencia());
				transferenciaitem.setOrdemservico(ordemservico);
				ordemservico.getTransferencia().getListaTransferenciaitem().add(transferenciaitem);
				TransferenciaitemService.getInstance().saveOrUpdate(transferenciaitem);
				
				//Criando a liga��o com a ordem de servi�o
				Ordemservicoproduto osp = new Ordemservicoproduto();
				osp.setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_OK);
				osp.setProduto(produto);
				osp.setQtdeesperada(qtde);
				OrdemservicoprodutoService.getInstance().saveOrUpdate(osp);
				
				Ordemprodutoligacao opl = new Ordemprodutoligacao();
				opl.setOrdemservico(ordemservico);
				opl.setOrdemservicoproduto(osp);
				OrdemprodutoligacaoService.getInstance().saveOrUpdate(opl);
				
				//O sistema faz a reserva de entrada e sa�da do produto nos respectivos endere�os;
				Ordemservicoprodutoendereco ospe = new Ordemservicoprodutoendereco();
				ospe.setOrdemservicoproduto(osp);
				ospe.setEnderecodestino(destino);
				ospe.setEnderecoorigem(origem);
				ospe.setQtde(qtde);
				OrdemservicoprodutoenderecoService.getInstance().saveOrUpdate(ospe);
				
				try{
					enderecoprodutoService.reservarEntrada(ospe, false);
					enderecoprodutoService.reservarSaida(ospe, false);
				}catch (Exception e) {
					throw new WmsException(e.getMessage(), e);
				}
				
				return null;
			}
		});
	}

	/**
	 * Finaliza a ordem de servi�o e realiza os ajustes no estoque.
	 *  
	 * @author Giovane Freitas
	 */
	private void finalizarTransferencia() {
		if (ordemservico != null){
			//Cancela a ordem e desfaz as reservas de estoque
			Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){
				public Object doInTransaction(TransactionStatus status) {
					Timestamp dttermino = new Timestamp(Calendar.getInstance().getTimeInMillis());
					OrdemservicoService.getInstance().finalizar(ordemservico, usuario, dtinicio, dttermino , false);
					
					ordemservicoUsuario.setDtfim(dttermino);
					OrdemservicousuarioService.getInstance().saveOrUpdate(ordemservicoUsuario);
					
					ordemservico.getTransferencia().setTransferenciastatus(Transferenciastatus.FINALIZADO);
					TransferenciaService.getInstance().saveOrUpdate(ordemservico.getTransferencia());
					
					return null;
				}
			});
		}
	}

	@Override
	public String getTitulo() {
		return TITULO;
	}
	
}
