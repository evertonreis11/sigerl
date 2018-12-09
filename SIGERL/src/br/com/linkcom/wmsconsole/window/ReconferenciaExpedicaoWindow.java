package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.util.List;

import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoerrado;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.vo.CodigobarrasVO;
import br.com.linkcom.wms.geral.service.OrdemservicoService;

public class ReconferenciaExpedicaoWindow extends ConferenciaExpedicaoWindow {
	
	private static final String TITULO = "Reconfer�ncia de expedi��o";

	@Override
	protected boolean isReconferencia() {
		return true;
	}

	/**
	 * Carrega a lista das ordens de servi�o que s�o do tipo confer�ncia.
	 * @return 
	 */
	protected List<Ordemservico> loadOsList(Carregamento carregamento) {
		return OrdemservicoService.getInstance().findByCarregamentoToReconferencia(carregamento, usuario);
	}
	
	/**
	 * Verifica se a ordem de servi�o � uma ordem de reconfer�ncia de expedi��o v�lida.
	 * 
	 * @param ordemservico2
	 * @return
	 */
	@Override
	protected boolean isOrdemValida(Ordemservico ordemservico2) {
		if (ordemservico2 == null)
			return false;
		
		return ordemservico.getOrdemtipo().equals(Ordemtipo.RECONFERENCIA_EXPEDICAO_1)
			|| ordemservico.getOrdemtipo().equals(Ordemtipo.RECONFERENCIA_EXPEDICAO_2);
	}
	
	/**
	 * Coleta dos produtos.
	 * 
	 * Caso o usu�rio digite 0 na etiqueta, � apresentado na tela o menu de sele��o.
	 * @see #makeMenu()
	 * 
	 * � solicitado a etiqueta e o produto, e � verificado se os mesmos percencem �s OSP.
	 * @see #etiquetaPertence(String)
	 * @see #produtoPertence(String, Etiquetaexpedicao)
	 * 
	 * Caso esteja ok, � apresentado informa��es do produto. e in�cio da coleta das quantidades.
	 * @see #showInfoProduto(CodigobarrasVO, Etiquetaexpedicao)
	 * 
	 * @throws IOException
	 */
	@Override
	protected void startCollectProduct() throws IOException {
		validarBloqueioExistente();
		
		for (Ordemprodutohistorico oph : listaOPH) {
			fixReadedEtiqueta(oph);
		}
		
		int i;
		for (i=0;i<listaOPH.size();) {
			next = false;
			stoploop = false;
			
			drawEsqueleto("Digite 0 para a��es.");
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			writeLine("O.S.: " + ordemservico.getCdordemservico().toString());
			writeSeparator();
			
			showInfoProdutoResumido(listaOPH.get(i));
			
			Etiquetaexpedicao etiquetaPertence = null;
			String valueEtiqueta = null;
			if(leituraPorEtiqueta) {
				if(canGoNextProduct(listaOPH.get(i))){
					i++;
					next = false;
					continue;
				}
				
				valueEtiqueta = readBarcode("Etiqueta: ");
				if(valueEtiqueta == null || "0".equals(valueEtiqueta)) {
					makeMenu();
					if(trocarCarregamento != null)
						if(trocarCarregamento)
							break;
						else
							continue;
					if(next){
						i++;
						next = false;
						continue;
					}
					if(stoploop)
						break;
					
					//se o usu�rio pressionou 0 para voltar devo continuar.
					continue;
				}
					
				
				valueEtiqueta = getCodigoProduto(valueEtiqueta);
				writeLine("");
				writeLine("");
				
				etiquetaPertence = etiquetaPertence(valueEtiqueta,listaOPH.get(i));
				
				if (etiquetaPertence == null && lerEmbalagemExpedicao(valueEtiqueta)){
					continue;
				} else if(etiquetaPertence == null){
					alertError( "Etiqueta n�o encontrada.");
					Ordemservicoprodutoerrado leituraErrada = gravarLeituraErrada(valueEtiqueta, null);
					bloquearColetor(leituraErrada);
					continue;
				}else
					if(etiquetaPertence.getReaded()){
						alertError( "Etiqueta j� coletada.");
						continue;
					}
			}
			
			String valueProduto = readBarcode("Produto: ");
			if(!leituraPorEtiqueta){
				if("0".equals(valueProduto)) {
					makeMenu();
					if(next){
						i++;
						next = false;
						continue;
					}
					if(stoploop)
						break;
				}
			}
			
			writeLine("");
			writeLine("");
			
			CodigobarrasVO produtoPertence = produtoPertence(valueProduto,etiquetaPertence,listaOPH.get(i));
			if(produtoPertence == null){
				alertError( "Produto n�o encontrado.");
				Ordemservicoprodutoerrado leituraErrada = gravarLeituraErrada(valueEtiqueta, valueProduto);
				bloquearColetor(leituraErrada);
				continue;
			}
			
			//carrega a etiqueta caso seja um carregamento por carga.
			if(!leituraPorEtiqueta){
				etiquetaPertence = produtoPertence.getEtiquetaexpedicao();

				//mostra as informa��es do produto
				produtoInformations(produtoPertence.getOrdemprodutohistorico());
			}
			
			//coleta das quantidades
			collectQte(produtoPertence.getOrdemprodutohistorico(), produtoPertence, etiquetaPertence);
			
			if(leituraPorEtiqueta)
				etiquetaPertence.setReaded(true);
			
			
			if(next)
				i++;
		}
		
		if(!stoploop){
			writeOnCenter("N�o existem mais itens para serem coletados." +
					"\nA reconfer�ncia ser� finalizada.", null, true, true);
			processarOSP();
		}
	}
	
	/**
	 * Exibe as informa��es resumidas do produto.
	 * 
	 * @param produtoPertence
	 */
	private void showInfoProdutoResumido(Ordemprodutohistorico ordemprodutohistorico) throws IOException {

		Produto produto = ordemprodutohistorico.getOrdemservicoproduto().getProduto();
		
		drawEsqueleto("Digite 0 para a��es.");
		writeOnCenter(getTitulo(), null, false, false);
		writeSeparator();
		writeLine("O.S.: " + ordemservico.getCdordemservico().toString());
		writeSeparator();
		writeLine("C�digo: " + produto.getCodigo());
		
		if (produto.getProdutoprincipal() == null){
			writeLine("Descri��o: " + produto.getDescricao());
		} else {
			writeLine("Descri��o: " + produto.getProdutoprincipal().getDescricao() + " - " + produto.getDescricao());
			writeLine("Volume: " + produto.getComplementocodigobarras());
		}
		
		writeSeparator();
		
	}
	
	@Override
	public String getTitulo() {
		if (ordemservico != null)
			return ordemservico.getOrdemtipo().getNome();
		else
			return TITULO;
	}
}
