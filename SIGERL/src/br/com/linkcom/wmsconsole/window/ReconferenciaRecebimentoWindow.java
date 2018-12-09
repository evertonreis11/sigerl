package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import net.wimpi.telnetd.io.toolkit.Editfield;
import net.wimpi.telnetd.io.toolkit.InputValidator;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtocodigobarras;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.ProdutoService;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;

public class ReconferenciaRecebimentoWindow extends ConferenciaRecebimentoWindow{
	
	private static final String TITULO = "Reconfer�ncia de recebimento";
	protected boolean finalizado = false;
	
	/**
	 * Caso a ordem seja maior que 1 caracteriza uma reconfer�ncia.
	 * Assim a partir desta tela o usu�rio poder� acess�-lo.
	 */
	@Override
	protected boolean validateOS() {
		if(ordemServico != null && ordemServico.getOrdem() > 1)
			return true;
		
		return false;
	}
	
	@Override
	public String getTitulo() {
		return TITULO;
	}
	
	/**
	 * Inicializa a coleta dos produtos.
	 * 
	 * Caso o usu�rio tenha digitado 0 na �rea destinada a coleta de produto, � mostrado o menu de coleta novamente.
	 * @see #makeMenuColeta()
	 * 
	 * Caso o produto bipado perten�a a osp, � mostrado na tela as informa��es deste produto e inicializa a coleta dos dados.
	 * @see #produtoPertence(String)
	 * @see #showInfoProduto(Ordemservicoproduto)
	 * @see #collectQte(Ordemservicoproduto)
	 * 
	 * Caso o produto tenha sido alterado o seu tipo de palete, reexibir o valor modificado.
	 * @see #produtoInformations(Ordemservicoproduto)
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#findBy(Recebimento recebimento)
	 */
	protected void startCollectProduct() throws IOException {
		int i = 0;
		boolean menu = false;
		boolean menuReconferencia = false;
		boolean repetiuProduto = false;
		finalizado = false;
		for (i=0;i<listaOPH.size();) {
			cancelProduct = true;
			if(menu || menuReconferencia) {
				if(menu){
					makeMenuColeta();
					menu = false;
				}
				else
					if(menuReconferencia)
						makeMenuColetaProduto();
				//se for para pular o produto
				if(next)
					i++;
				
				if(next && i >= listaOPH.size()){
					finalizado = true;
					abort = true;
				}
				
				menuReconferencia = false;
				next = false;
				if(abort)
					break;
			}
			Ordemprodutohistorico oph = listaOPH.get(i);
			
			prepararTela();
			
			if(oph.getOrdemservicoproduto().getProduto().getModificado() == null || !oph.getOrdemservicoproduto().getProduto().getModificado()){
				//carrega o produto caso n�o tenha sido carregado ainda.
				oph.getOrdemservicoproduto().setProduto(ProdutoService.getInstance().findDadosLogisticosProdutoRF(oph.getOrdemservicoproduto().getProduto(),deposito));
				oph.getOrdemservicoproduto().getProduto().setModificado(true);
			}
			
			//mostra as informa��es resumidas do produto.
			showInfoResumidoProduto(oph.getOrdemservicoproduto().getProduto());
			String codigoBarras = readBarcode(null);
			if(exibirMenu || codigoBarras.equals("0")) {
				menu = true;
				continue;
			}
			
			if (showAlertFinalizado())
				break;
			
			writeLine("");
			writeLine("");
			
			//verifica se o produto pertence a osp.			
			Boolean produtoPertence = produtoPertence(codigoBarras,oph);
			
			if(produtoPertence){
				try{
					
					//mostra as informa��es do produto
					produtoInformations(oph);
						
					Boolean alterado = collectQte(oph); //coleta das quantidades
					if(alterado) {
						produtoInformations(oph);
						if(coletaquantidade)
							writeLine("Quantidade: " + oph.getQtdeColetada());
						else 
							writeLine("Avariado: "+oph.getQtdeavaria());
						
						read();
					}
					
					if(confirmAction("Continuar neste produto? ")){
						menuReconferencia = true;
						repetiuProduto = true;
					}
					else{
						i++;
						if(repetiuProduto && i < listaOPH.size() && alertTipoColeta()){
							menuReconferencia = true;
						}
						repetiuProduto = false;
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				alertError("O c�digo de barras n�o pertence ao produto.");
			}
			
			//encerra a coleta aqui.
			if(i >= listaOPH.size()){
				coletaQtdeProdutosNaoColetados();
				finalizado = true;
				break;
			}
		}
		if(finalizado){
			processarOSP();
			writeOnCenter("Reconfer�ncia finalizada.", null, true, true);
		}
		
		cancelProduct = false;
	}

	/**
	 * M�todo que possibilita o usu�rio bipar produto pelo c�digo ap�s bipar todos os produtos em sequencia
	 * 
	 * @throws IOException
	 * @author Tom�s Rabelo
	 */
	private void coletaQtdeProdutosNaoColetados() throws IOException {
		while (true) {
			if(confirmAction("Deseja finalizar a \nreconfer�ncia? "))
				break;
			
			prepararTela();
			
			//verifica se o produto pertence a osp.
			Ordemprodutohistorico produtoPertence = biparProduto(listaOPH);
			
			if(produtoPertence != null){
				//mostra as informa��es do produto
				produtoInformations(produtoPertence);
				collectQte(produtoPertence);
			}
		}
	}

	/**
	 * Verifica se o usu�rio deseja modificar o tipo de coleta
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @return
	 * @throws IOException 
	 */
	private boolean alertTipoColeta() throws IOException {
		drawEsqueleto("0 - N�o ou 1 - Sim");
		writeLine("Modo de coleta atual:");
		writeLine("");
		writeLine(tipoColeta.toString());
		writeLine("");
		writeLine("Mudar modo de coleta? ");
		Editfield optDep=new Editfield(getTermIO(),"editfield 3",1);
		optDep.run();
		String value = optDep.getValue();
		if(value != null && value.equals("1"))
			return true;
		return false;
	}

	/**
	 * Cria um menu com apenas as op��es de modo de coleta
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @throws IOException
	 */
	private void makeMenuColetaProduto() throws IOException {
		drawEsqueleto("");
		writeLine("Selecione uma a��o: ");
		writeLine("");
		
		final LinkedHashMap<Integer, String> mapaMenu = new LinkedHashMap<Integer, String>();
		
		mapaMenu.put(1, "Coleta padr�o");
		mapaMenu.put(2, "Coleta fracionada");
		mapaMenu.put(3, "Coleta de avaria");
		
		drawMenu(mapaMenu);//Imprime o menu
		
		Editfield optDep=new Editfield(getTermIO(),"editfield 3",2);
		optDep.registerInputValidator(new InputValidator(){
			public boolean validate(String str) {
				if(str == null || str.equals(""))
					return true;
				
				try{
					int option = Integer.parseInt(str);
					if(option <= mapaMenu.size() && option >= 0){
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
		
		if(value == null || value.equals(""))
			value = "1";
		
		abort = false;
		if(value.equals("1")){
			//seta a coleta padr�o
			this.coletapadrao = true;
			this.coletaquantidade = true;
			
		} else if ("3".equals(value)){
			//quando � avaria ele coleta por unidade
			this.coletapadrao = false;
			this.coletaquantidade = false;
		} else if("2".equals(value)){
			//quando a coleta � fracionada
			this.coletapadrao = false;
			this.coletaquantidade = true;
		} 
		
		if(Integer.parseInt(value) == 1){
			tipoColeta = TipoColeta.PADRAO;
		}else if(Integer.parseInt(value) == 2 ){
			tipoColeta = TipoColeta.FRACIONADA;
		}else if(Integer.parseInt(value) == 3){
			tipoColeta = TipoColeta.AVARIA;
		}
		
	}
	
	/**
	 * Mostra as informa��es simplificadas do produto
	 * 
	 * @param produto
	 * @throws IOException
	 */
	private void showInfoResumidoProduto(Produto produto) throws IOException{
		prepararTela();
		
		writeLine("Recebimento: " + recebimento.getCdrecebimento());
		writeLine("Ve�culo: " + veiculo);
		writeSeparator();
		
		String codigoP = "";
		try {
			codigoP = produto.getCodigo();
		} catch (Exception e) {}
		
		writeLine("C�digo: " + codigoP);
		if (produto.getProdutoprincipal() != null){
			writeLine("Descri��o: " + produto.getProdutoprincipal().getDescricao());
			writeLine("Volume: " + produto.getComplementocodigobarras().substring(0, 2) + "/" + 
					produto.getComplementocodigobarras().substring(2));
		}else
			writeLine("Descri��o: " + produto.getDescricao());

		Produtoembalagem produtoembalagem = null;
		if(produto.getListaProdutoEmbalagem() != null && !produto.getListaProdutoEmbalagem().isEmpty()){
			produtoembalagem = produto.getListaProdutoEmbalagem().get(0);
		}
		
		if (coletapadrao)
			writeLine("Embalagem: " + (produtoembalagem == null ? "" : produtoembalagem.getDescricao()));

		writeSeparator();

	}
	
	/**
	 * Verifica se o c�digo de barras bipado pertence ao produto.
	 * 
	 * @param codigo
	 * @param produto
	 * @return
	 */
	private Boolean produtoPertence(String codigo, Ordemprodutohistorico oph) {
		Produto produto = oph.getOrdemservicoproduto().getProduto();
		List<Produtocodigobarras> listaProdutoCodigoDeBarras = produto.getListaProdutoCodigoDeBarras();
		
		String codigo2 = getCodigoProduto(produto.getCodigo().trim()).toUpperCase();
		if((!ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS, this.deposito))
				&& codigo2.equalsIgnoreCase(codigo.toUpperCase())){
			oph.setCodigoBarrasIndex(null);//N�o encontrou atrav�s do c�digo de barras
			return true;
		}
		for (int i = 0;i < listaProdutoCodigoDeBarras.size(); i++) {
			Produtocodigobarras produtocodigobarras = listaProdutoCodigoDeBarras.get(i);
			codigo2 = produtocodigobarras.getCodigo();
			if(codigo.trim().equalsIgnoreCase(codigo2.trim())){
				oph.setCodigoBarrasIndex(i);//Indice que encontrou o c�digo de barras
				oph.setCodigoBarrasConferencia(codigo2); // codigo de barras utilizado para identificar o produto.
				return true;
			}
		}
		return false;
	}
}
