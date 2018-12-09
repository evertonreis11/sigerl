package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecoproduto;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoendereco;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtocodigobarras;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Transferencia;
import br.com.linkcom.wms.geral.bean.Transferenciastatus;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.EnderecoService;
import br.com.linkcom.wms.geral.service.ExpedicaoService;
import br.com.linkcom.wms.geral.service.LinhaseparacaoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoService;
import br.com.linkcom.wms.geral.service.OrdemservicousuarioService;
import br.com.linkcom.wms.geral.service.ProdutocodigobarrasService;
import br.com.linkcom.wms.geral.service.ProdutoembalagemService;
import br.com.linkcom.wms.geral.service.ReabastecimentoService;
import br.com.linkcom.wms.geral.service.TransferenciaService;
import br.com.linkcom.wms.geral.service.TransferenciastatusService;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wmsconsole.system.ExecucaoOSWindow;

/**
 * 
 * @author Leonardo Guimar�es
 *
 */
public class TransferenciaWindow extends ExecucaoOSWindow{
	
	private static final String TITULO = "Transfer�ncia de endere�o";
	protected Ordemservico ordemservico;
	protected Linhaseparacao linhaseparacao;
	protected int pagesOSlista = 5;//N�mero de itens por p�gina da lista de OS
	protected int pagesLSlista = 5;//N�mero de itens por p�gina da lista de Linha de separa��o
	protected List<Ordemservicoproduto> listaOSP = new ArrayList<Ordemservicoproduto>();
	protected Long qtdeEsperada = 0L;
	protected Endereco enderecoOrigem = null;
	protected Endereco enderecoDestino = null;
	protected Boolean isUMA;
	private boolean deveColetarQuantidade = false;//criei este flag para desabilitar a coleta de quantidade sem ter de apagar ou comentar c�digo.
	
	@Override
	public void draw() throws IOException {
		try{
			List<Linhaseparacao> listaLinhaSeparacao = LinhaseparacaoService.getInstance().findByUsuario(usuario, deposito);
			
			boolean firstTime = true;
			if(listaLinhaSeparacao != null && !listaLinhaSeparacao.isEmpty()){
				do{
					if(listaLinhaSeparacao.size() == 1 && firstTime){//Se o operador so possu� uma linha de separa��o cadastrada
						this.linhaseparacao = listaLinhaSeparacao.get(0);
					}else{
						this.linhaseparacao = makeMenuLinhaSeparacao(listaLinhaSeparacao);
						if(this.linhaseparacao == null)
							return;
					}
					
					Ordemservico ordemSelecionada = getOrdemservico();
					if (ordemSelecionada != null)
						executarOrdem(ordemSelecionada);
					
					firstTime = false;
					
				}while(true);
				
			}else{
				alertNenhumaLinhaCadastrada();
			}
		}catch (Exception e) {
			System.out.println("Ocorreu um erro inesperado. O coletor ser� fechado. Motivo:");
			e.printStackTrace();
			alertError("Ocorreu um erro inesperado. O coletor ser� fechado.", true);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void executarOrdem(Ordemservico ordem) throws IOException {
		this.ordemservico = ordem;
		
		//Exibindo tela de informa��es
		drawEsqueleto("");
		writeOnCenter(getTitulo(), null, false, false);
		writeSeparator();
		if (ordem.getCarregamento() != null){
			writeLine("Carregamento: " + ordem.getCarregamento().getCdcarregamento());
			writeLine("");
		}
		if (ordem.getReabastecimentolote() != null && ordem.getReabastecimentolote().getReabastecimento() != null){
			writeLine("Reabastecimento: " + ordem.getReabastecimentolote().getReabastecimento().getCdreabastecimento());
			writeLine("");
		}
		writeLine("O.S.: " + this.ordemservico.getCdordemservico());
		writeLine("");
		read();
		//FIM da tela de informa��es

		
		try{						
			this.listaOSP = OrdemservicoprodutoService.getInstance().findDadosOSPForTransferencia(this.ordemservico);
			updateOrdemServico(Ordemstatus.EM_EXECUCAO);
			updateTransferencia(Transferenciastatus.EM_EXECUCAO);
			if(iniciarTransferencia()){
				if (ordem.getOrdemstatus().equals(Ordemstatus.EM_EXECUCAO)){
					Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){	
						public Object doInTransaction(org.springframework.transaction.TransactionStatus status) {
							OrdemservicoService.getInstance().finalizar(TransferenciaWindow.this.ordemservico, null, null, null, false);
							if (ordemservico.getTransferencia() != null)
								TransferenciaService.getInstance().finalizarSeCompleta(ordemservico.getTransferencia());
							ReabastecimentoService.getIntance().finalizarSeAcabou(ordemservico);
							return null;
						};
					});
				}
					
				alertConclusao();
				return;
			}else{ 
				this.ordemservico = null;
				if (isConvocacaoAtiva())
					logout();
			}
		}catch (Exception e) {
			e.printStackTrace();
			getTermIO().eraseScreen();
			alertError( e.getMessage());
			getTermIO().eraseScreen();
			return;
		}

		if(this.ordemservico != null){
			updateOrdemServico(Ordemstatus.FINALIZADO_SUCESSO);
			updateTransferencia(Transferenciastatus.FINALIZADO);
		}
	}
	
	/**
	 * Alerta que nenhuma ordem de servi�o foi encontrada
	 * @throws IOException 
	 */
	protected void alertFaltaOS() throws IOException {
		writeOnCenter("Nenhuma ordem de servi�o foi encontrada a partir desta linha de separa��o", null, true, true);
	}

	/**
	 * Cria a sele��o de ordens de servi�o
	 * 
	 * @param linhaseparacao
	 * @throws IOException 
	 */
	private Ordemservico getOrdemservico() throws IOException {
			List<Ordemservico> listaOS = carregarListaOS();
			
			if(listaOS != null && !listaOS.isEmpty()){
				if(listaOS.size() == 1)
					return listaOS.get(0);
				else{
					return makeMenuOS(listaOS);
				}
			}else 
				alertFaltaOS();

			return null;
	}
	
	/**
	 * Exibe a mensagem de conclus�o da opera��o
	 * @throws IOException 
	 */
	public void alertConclusao() throws IOException {
		writeOnCenter("Transfer�ncia conclu�da com sucesso.", null, true, true);
	}

	/**
	 * Carrega a lista de ordens de servi�o dispon�veis
	 * 
	 * @return
	 */
	public List<Ordemservico> carregarListaOS() {
		return OrdemservicoService.getInstance().findForTransferencia(this.linhaseparacao,this.deposito, this.usuario);
	}

	/**
	 * Atualiza o status da ordem de servi�o e associa/desassocia o usu�rio � ordem de servi�o.
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoService#updateStatusordemservico(Ordemservico ordemservico)
	 * 
	 * @param ordemstatus
	 */
	private void updateOrdemServico(Ordemstatus ordemstatus){
		if(this.ordemservico != null){
			this.ordemservico.setOrdemstatus(ordemstatus);
			OrdemservicoService.getInstance().atualizarStatusordemservico(this.ordemservico);
			
			//se a ordem voltou para 'Em aberto' eu desassocio do usu�rio
			if (Ordemstatus.EM_ABERTO.equals(ordemstatus)){
				OrdemservicousuarioService.getInstance().desassociarUsuario(this.usuario, this.ordemservico);								
			}
			//se a ordem est� em 'Em execu��o' eu associo do usu�rio
			else if (Ordemstatus.EM_EXECUCAO.equals(ordemstatus)){
				OrdemservicousuarioService.getInstance().associarUsuario(this.usuario, this.ordemservico);				
			}
			//se a ordem est� em 'Finalizado' eu atualizo a hora de t�rmino usu�rio
			else if (Ordemstatus.FINALIZADO_SUCESSO.equals(ordemstatus) || Ordemstatus.FINALIZADO_DIVERGENCIA.equals(ordemstatus)){
				OrdemservicousuarioService.getInstance().
					atualizarHoraFim(this.usuario, this.ordemservico, new Timestamp(System.currentTimeMillis()));								
			}

		}
	}
	
	/**
	 * Atualiza o status da transfer�ncia
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param transferenciastatus
	 */
	public void updateTransferencia(Transferenciastatus transferenciastatus){
		if(this.ordemservico != null && this.ordemservico.getTransferencia() != null){
			Transferencia t = this.ordemservico.getTransferencia();
			t.setTransferenciastatus(transferenciastatus);
			if(transferenciastatus.equals(Transferenciastatus.FINALIZADO)){
				List<Ordemservico> lista = OrdemservicoService.getInstance().findByTransferencia(t,Ordemstatus.EM_ABERTO);
				if(lista != null && lista.size() > 0)
					t.setTransferenciastatus(Transferenciastatus.EM_ABERTO);
			}
			TransferenciastatusService.getInstance().updateStatusTransferencia(t);
		}
	}
	
	/**
	 * Inicia a transferencia de endere�os
	 * 
	 * @throws IOException 
	 */
	private boolean iniciarTransferencia() throws IOException {
		if (this.listaOSP != null){
			for(int i = 0; i < listaOSP.size();){
				Ordemservicoproduto osp = listaOSP.get(0);
				Ordemservicoprodutoendereco ordemservicoprodutoendereco = osp.getListaOrdemservicoprodutoendereco().iterator().next();
				this.enderecoOrigem = ordemservicoprodutoendereco.getEnderecoorigem();
				Enderecoproduto enderecoproduto = this.enderecoOrigem.getListaEnderecoproduto().iterator().next();
				if(!this.enderecoOrigem.getEnderecofuncao().equals(Enderecofuncao.BLOCADO) 
						&& enderecoproduto.getUma() != null && enderecoproduto.getUma()){
					isUMA = true;
					if(!transferirUMA(osp)){
						return false;
					}
				}else{
					isUMA = false;
					if(! transferirNaoUMA(osp)){
						return false;
					}
				}
			}		
		}
		
		return true;
	}
	/**
	 * Faz as transferencias que n�o sao de UMA
	 * 
	 * @param osp
	 * @return
	 * @throws IOException 
	 */
	public boolean transferirNaoUMA(Ordemservicoproduto osp) throws IOException {
		Boolean enderecoValido = verificaEnderco(osp, this.enderecoOrigem, "Informe o endere�o de origem:");
		if(enderecoValido){
			 if(produtoValido(osp)){
				return collectQtde(osp,"111");
			 }
		}
		return false;
	}
	
	/**
	 * Verifica se o produto bipado e v�lido
	 * @param osp
	 * @return
	 * @throws IOException
	 */
	protected boolean produtoValido(Ordemservicoproduto osp) throws IOException {
		String codigo = "";
		do{
			showCabecalhoUma(osp, "111");
			codigo = readBarcode("Informe o produto:");
			Produto produto = osp.getProduto();
			
			if(codigo != null && codigo.equals("0"))
				return false;
			
			if((!ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS, this.deposito)) 
					&& produto.getCodigo().toUpperCase().equals(codigo.toUpperCase())){
				return true;
			}else{
				List<Produtocodigobarras> listaCodigoBarras = ProdutocodigobarrasService.getInstance().findByProduto(produto);
				for (Produtocodigobarras produtocodigobarras : listaCodigoBarras) {
					if(produtocodigobarras.getCodigo().equals(codigo)){
						return true;
					}
				}
			}
			
			alertError("Produto inv�lido.", 17);
		}while(true);
	}

	/**
	 * Faz a transferencia das UMAS
	 *
	 * @see #showCabecalhoUma(Ordemservicoproduto)
	 * 
	 * @param osp
	 * 
	 * @throws IOException 
	 */
	private boolean transferirUMA(Ordemservicoproduto osp) throws IOException {
		do{
			showCabecalhoUma(osp,"100");
			
			Boolean UMAValida = verificaUma();
			if(UMAValida == null){
				return false;
			}
			else if(UMAValida){
				return collectQtde(osp,"110");
			}else{
				alertError("UMA inv�lida.",15);
			}
		}while(true);
	}
	
	/**
	 * Faz a coleta das quantdidades
	 * 
	 * @see #showCabecalhoUma(Ordemservicoproduto, String)
	 * 
	 * @param osp
	 * @param booleanRepresentation <p>Representa 3 variaveis booleanas.[MostrarProduto,MostrarQuantidade,MostrarEnderecoDestino]</p> <p>Formato:000,111,101,010,100...</p>
	 * 
	 * @throws IOException 
	 */
	private boolean collectQtde(Ordemservicoproduto osp,String booleanRepresentation) throws IOException {
		if (this.deveColetarQuantidade){
			do{
				showCabecalhoUma(osp,booleanRepresentation);
				Integer qtde = readInteger("Informe a quantidade:");
				if(qtde == null || qtde.equals(0))
					return false;
				else{
					if(validateQtde(osp,qtde)){
						if(verificaEnderco(osp,this.enderecoDestino,"Informe o endere�o de destino:")){
							saveTransferencia(osp,usuario);
							return true;
						}
						else
							return false;
					}else{
						alertError("Quantidade inv�lida.", 17);
					}
				}
			}while(true);		
			
		}else/*N�o deve coletar quantidade*/{
			if(verificaEnderco(osp,this.enderecoDestino,"Informe o endere�o de destino:")){
				saveTransferencia(osp,usuario);
				return true;
			}
			else
				return false;			
		}
	}
	
	/**
	 * Salva a os dados da transfer�ncia
	 * 
	 * @throws IOException 
	 * 
	 */
	public void saveTransferencia(Ordemservicoproduto osp, final Usuario user) throws IOException {
		osp.getListaOrdemservicoprodutoendereco().remove(0);
		
		if (osp.getListaOrdemservicoprodutoendereco().isEmpty()){
			osp.setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_OK);
			OrdemservicoprodutoService.getInstance().updateStatusordemservicoproduto(osp);
			this.listaOSP.remove(osp);
		}

		if (listaOSP.isEmpty()){
			Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){	
				public Object doInTransaction(org.springframework.transaction.TransactionStatus status) {
					OrdemservicoService.getInstance().finalizar(TransferenciaWindow.this.ordemservico, null, null, null, false);
					if (ordemservico.getTransferencia() != null)
						TransferenciaService.getInstance().finalizarSeCompleta(ordemservico.getTransferencia());
					ReabastecimentoService.getIntance().finalizarSeAcabou(ordemservico);
					
					//==================================================================
					//Finalizando o carregamento se esta era a �ltima O.S. aberta
					if(ordemservico.getExpedicao() != null && ordemservico.getExpedicao().getCdexpedicao() != null){
						ExpedicaoService.getInstance().processaSituacaoExpedicao(ordemservico.getExpedicao(), user);
					}
					//==================================================================
					
					
					return null;
				};
			});
		}
		
		EnderecoService.getInstance().pAtualizarEndereco(this.enderecoOrigem);
		EnderecoService.getInstance().pAtualizarEndereco(this.enderecoDestino);
		
	}

	/**
	 * Verifica se a quantidade informada � a mesma que a esperada
	 * 
	 * @param ospe
	 * @param qtde
	 * @return
	 */
	private boolean validateQtde(Ordemservicoproduto osp, Integer qtde) {
		if (this.deveColetarQuantidade )
			return this.qtdeEsperada.equals(Long.valueOf(qtde));
		else
			return true;
	}
	
	/**
	 * Verifica se o endere�o � v�lido
	 * 
	 * @param osp
	 * @param ospe
	 * @param label
	 * @return
	 * @throws IOException
	 */
	public boolean verificaEnderco(Ordemservicoproduto osp,Endereco endereco,String label) throws IOException {
		do{
			showCabecalhoUma(osp,"111");
			
			String end = readBarcode(label);
			if(end == null || end.equals("0"))
				if(confirmAction("Tem certeza que deseja sair?")){
					return false;
				}else {
					continue;
				}
			try{
				Endereco aux = EnderecoService.getInstance().loadEnderecoByCodigoEtiqueta(end, this.deposito);
				
				if(!endereco.getEnderecofuncao().equals(Enderecofuncao.BLOCADO)){
					if(endereco.equals(aux))
						return true;
				}else{
					if(EnderecoService.getInstance().predioEquals(endereco,aux))
						return true;
				}
				alertError("Endere�o inv�lido.", 17);
			}catch (Exception e) {
				alertError("Endere�o inv�lido.", 17);
			}
		}while(true);
		
	}

	/**
	 * Mostra uma mensagem de erro na tela
	 * 
	 * @param string
	 * @param i
	 * @throws IOException 
	 */
	private void alertError(String message, int row) throws IOException {
		alertError(message);
	}

	/**
	 * Verifica se a UMA digitada � v�lida
	 * 
	 * @param 
	 * @throws IOException 
	 */
	private Boolean verificaUma() throws IOException {
		Enderecoproduto enderecoproduto = this.enderecoOrigem.getListaEnderecoproduto().iterator().next();
		
		writeSeparator();
		String codUma = readBarcode("Informe a UMA:");
		
		if(codUma == null || codUma.equals("0"))
			return null;
		else {
			try{
				return Integer.valueOf(codUma).equals(enderecoproduto.getCdenderecoproduto());
			}catch (Exception e) {
				// N�o digitou um inteiro v�lido
				return false;
			}
		}
		
		
	}

	/**
	 * Mostra ao usu�rio as informa��es necess�rias para a transferencia de uma UMA
	 * 
	 * @see #showInformationsTransferencia(Ordemservicoprodutoendereco, Ordemservicoproduto, String)
	 * 
	 * @param booleanRepresentation <p>Representa 3 variaveis booleanas.[MostrarProduto,MostrarQuantidade,MostrarEnderecoDestino]</p> <p>Formato:000,111,101,010,100...</p>
	 * @param osp
	 * 	  
	 * @throws IOException
	 */
	private void showCabecalhoUma(Ordemservicoproduto osp,String booleanRepresentation) throws IOException {
		
		Ordemservicoprodutoendereco ordemservicoprodutoendereco = osp.getListaOrdemservicoprodutoendereco().iterator().next();
		drawEsqueleto(getTitulo(), "Digite 0 para sair.");
		if(isUMA){
			Enderecoproduto enderecoproduto = this.enderecoOrigem.getListaEnderecoproduto().iterator().next();
			writeLine("UMA: " + String.format("%010d", enderecoproduto.getCdenderecoproduto()));
		}
		
		
		showInformationsTransferencia(ordemservicoprodutoendereco,osp,booleanRepresentation);
		
	}
	
	/**
	 * Mostra as informa��es para transfer�ncia
	 * 
	 * @param ordemservicoprodutoendereco
	 * @param osp
	 * @param booleanRepresentation <p>Representa 3 variaveis booleanas.[MostrarProduto,MostrarQuantidade,MostrarEnderecoDestino]</p> <p>Formato:000,111,101,010,100...</p>
	 */
	private void showInformationsTransferencia(Ordemservicoprodutoendereco ordemservicoprodutoendereco,Ordemservicoproduto osp,String booleanRepresentation) throws IOException {
		if(!booleanRepresentation.matches("[0,1][0,1][0,1]"))
			throw new WmsException("Valores incorretos em booleanRepresentation. Verifique a documenta��o do m�todo.");
		
		writeLine("End. origem: ");
		writeLine(this.enderecoOrigem.getEnderecoArea());
		writeSeparator();
		long qtdeEmbalagem = 1;
		this.enderecoDestino = ordemservicoprodutoendereco.getEnderecodestino();
		if(booleanRepresentation.startsWith("1")){
			Produto produto = osp.getProduto();
			writeLine("Prod.: " + produto.getCodigo() + " - " + produto.getDescricao());
			if(produto.getListaProdutoEmbalagem() != null){
				
				Produtoembalagem produtoembalagem = osp.getProduto().getListaProdutoEmbalagem().get(0);
				//Se a quantidade n�o � m�ltiplo da embalagem de recebimento ent�o usa a menor embalagem
				if (produtoembalagem == null || (osp.getQtdeesperada() % produtoembalagem.getQtde() != 0))
					produtoembalagem = ProdutoembalagemService.getInstance().findMenorEmbalagem(produto);
				
				writeLine("Embalagem: " + produtoembalagem.getDescricao() + " - " + produtoembalagem.getQtde());
				qtdeEmbalagem = produtoembalagem.getQtde();
				
			}
		}
		this.qtdeEsperada = osp.getQtdeesperada() / qtdeEmbalagem;
		if(booleanRepresentation.matches(".1.")){
			writeLine("Qtde.: " + this.qtdeEsperada);
			writeSeparator();
		}
		if(booleanRepresentation.endsWith("1")){
			writeLine("End. destino: ");
			writeLine(this.enderecoDestino.getEnderecoArea());
			writeSeparator();
		}
		
	}

	/**
	 * Cria um menu de sele��o de OS's
	 * 
	 * @param listaOS
	 * @return
	 * @throws IOException
	 */
	protected Ordemservico makeMenuOS(List<Ordemservico> listaOS) throws IOException {
		HashMap<String,Object> mapa = new HashMap<String, Object>();
		int size = listaOS.size();

		for(int i = 0;i < size; i++){
			String descricao = listaOS.get(i).getCdordemservico().toString();
			if (listaOS.get(i).getCarregamento() != null && listaOS.get(i).getCarregamento().getCdcarregamento() != null)
				descricao += " / " + listaOS.get(i).getCarregamento().getCdcarregamento();
			
			mapa.put(descricao , listaOS.get(i));
		}
		
		return (Ordemservico)makeMenuByHash(mapa, "Selecione uma O.S.: ", pagesOSlista);
	}

	/**
	 * Cria o menu das linhas de separa��o
	 * 
	 * @param listaLinhaSeparacao
	 * @throws IOException 
	 */
	protected Linhaseparacao makeMenuLinhaSeparacao(List<Linhaseparacao> listaLinhaSeparacao) throws IOException {
		HashMap<String,Object> mapa = new HashMap<String, Object>();
		int size = listaLinhaSeparacao.size();

		for(int i = 0;i < size; i++){
			mapa.put(listaLinhaSeparacao.get(i).getNome(), listaLinhaSeparacao.get(i));
		}
		
		return (Linhaseparacao)makeMenuByHash(mapa, "Escolha a linha de separa��o:", pagesLSlista);
		
	}

	/**
	 * Exibe a mensagem de que nenhum linha de separa��o foi cadastrada
	 * 
	 * @throws IOException
	 */
	protected void alertNenhumaLinhaCadastrada() throws IOException{
		alertError("Nenhuma linha de separa��o cadastrada.", true);
		alertError("Entre em contato com a mesa de opera��es.", true);
		
	}
	
	@Override
	public String getTitulo() {
		return TITULO;
	}
	

}
