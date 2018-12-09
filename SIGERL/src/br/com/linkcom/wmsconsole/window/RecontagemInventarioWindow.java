package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoendereco;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.service.ProdutoService;

public class RecontagemInventarioWindow extends ContagemInventarioWindow {
	
	private static final String TITULO = "Recontagem de inventário";

	public RecontagemInventarioWindow(){
		this.mensagemProximoItem = "Próximo produto/endereço";
		this.tipoHabilitado = Ordemtipo.RECONTAGEM_INVENTARIO;
	}
	
	@Override
	protected Boolean verificarOS(Ordemservico ordem) throws IOException {
		this.tipoHabilitado = Ordemtipo.RECONTAGEM_INVENTARIO;
		return super.verificarOS(ordem);
	}

	@Override
	public String getTitulo() {
		return TITULO;
	}
	
	@Override
	protected void collectProdutos(Endereco enderecoAtual) throws IOException {
		Iterator<Ordemprodutohistorico> lista = carregarListaOPHBipe(enderecoAtual);
		Ordemprodutohistorico next = lista.next();
		do {			
			
			drawEsqueleto(null);
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			
			makeOsHeader(); //adiciona o header
			
			//Mostrar endereço destino.
			makeEnderecoHeader(enderecoAtual);
			
			showInfoResumidoProduto(next.getOrdemservicoproduto().getProduto());
			String codigoDigitado = readBarcode("Produto:");
			
			if(exibirMenu){
				Acao acaoExecutada = executarAcao();
				
				if (Acao.PROXIMO_ENDERECO.equals(acaoExecutada)){
					if(!lista.hasNext())
						break;
					next = lista.next();
					continue;
				}else if (Acao.FINALIZAR_OS.equals(acaoExecutada) || Acao.CANCELAR.equals(acaoExecutada))
					break;
				else if (Acao.RETORNAR.equals(acaoExecutada))
					continue;
			}
			
			Ordemprodutohistorico produtoPertence = produtoPertence(codigoDigitado, enderecoAtual);
			if(produtoPertence == null || !next.getCdordemprodutohistorico().equals(produtoPertence.getCdordemprodutohistorico())){
				alertError("Produto inválido!");
				continue;
			}
			
			if(produtoPertence.getOrdemservicoproduto().getProduto().getModificado() == null || !produtoPertence.getOrdemservicoproduto().getProduto().getModificado()){
				//carrega o produto caso não tenha sido carregado ainda.
				produtoPertence.getOrdemservicoproduto().setProduto(ProdutoService.getInstance().findDadosLogisticosProdutoRF(produtoPertence.getOrdemservicoproduto().getProduto(), deposito));
				produtoPertence.getOrdemservicoproduto().getProduto().setModificado(true);
			}
			
			coletarInformacoesProduto(enderecoAtual, produtoPertence);
			atualizaOSP(produtoPertence);
			
			if(!lista.hasNext())
				break;
			
			next = lista.next();
			
		} while (true);
	}
	
	/**
	 * Mostra as informações simplificadas do produto
	 * 
	 * @param produto
	 * @throws IOException
	 */
	private void showInfoResumidoProduto(Produto produto) throws IOException{
		
		String codigoP = "";
		try {
			codigoP = produto.getCodigo();
		} catch (Exception e) {}
		
		writeLine("Código: " + codigoP);
		writeLine("Descrição: " + produto.getDescriptionProperty());
		writeSeparator();
	}

	/**
	 * @param enderecoAtual
	 * @return 
	 */
	private Iterator<Ordemprodutohistorico> carregarListaOPHBipe(Endereco enderecoAtual) {
		List<Ordemprodutohistorico> listaOPHBipe = new ArrayList<Ordemprodutohistorico>(); 
		for (Ordemprodutohistorico oph : listaOPH) {
			//Se o OrdemServicoProduto já foi concluído vou ignorá-lo
			//Tratamento necessário para o caso do usuário interromper a contagem e depois voltar para concluir
			if (!oph.getOrdemservicoproduto().getOrdemprodutostatus().equals(Ordemprodutostatus.NAO_CONCLUIDO))
				continue;
			
			List<Ordemservicoprodutoendereco> listaOrdemservicoprodutoendereco = oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco();
			for (Ordemservicoprodutoendereco ordemservicoprodutoendereco : listaOrdemservicoprodutoendereco) {
				//caso a etiqueta expedição seja nula, ignorar ela e pegar a primeira que aparecer.
				if(ordemservicoprodutoendereco.getEnderecodestino() != null && ordemservicoprodutoendereco.getEnderecodestino().equals(enderecoAtual)){
					listaOPHBipe.add(oph);
				}
			}
		}
		return listaOPHBipe.iterator();
	}

}
