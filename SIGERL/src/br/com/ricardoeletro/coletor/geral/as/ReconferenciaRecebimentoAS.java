package br.com.ricardoeletro.coletor.geral.as;

import java.io.IOException;
import java.util.List;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;

import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtocodigobarras;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.ProdutoService;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.ricardoeletro.coletor.modulo.recebimento.process.filtro.ConferenciaRecebimentoFiltro.TipoColeta;
import br.com.ricardoeletro.coletor.modulo.recebimento.process.filtro.ReconferenciaRecebimentoFiltro;

public class ReconferenciaRecebimentoAS extends ConferenciaRecebimentoAS {
	
	/**
	 * Caso a ordem seja maior que 1 caracteriza uma reconferência.
	 * Assim a partir desta tela o usuário poderá acessá-lo.
	 */
	@Override
	protected boolean validateOS(Ordemservico ordemServico) {
		if(ordemServico != null && ordemServico.getOrdem() > 1)
			return true;
		
		return false;
	}
	
	public String startCollectProduct(ReconferenciaRecebimentoFiltro filtro, Boolean trocarProduto) {
		
		String msg = null;
		List<Ordemprodutohistorico> listaOPH = recuperaObjetos(filtro);
		Ordemprodutohistorico oph = null;
		

		if (filtro.getOrdemprodutohistorico() == null 
				|| filtro.getOrdemprodutohistorico().getCdordemprodutohistorico() == null){
			oph = listaOPH.get(0);
		}else{
			oph = (Ordemprodutohistorico) CollectionUtils.find(listaOPH, new BeanPropertyValueEqualsPredicate("cdordemprodutohistorico", 
					filtro.getOrdemprodutohistorico().getCdordemprodutohistorico()));
			
			if (trocarProduto){
				Integer indexProduto = listaOPH.indexOf(oph);
				indexProduto++;
				
				if (indexProduto < listaOPH.size()){
					oph = listaOPH.get(indexProduto);
				}else{
					msg = processarOSP(filtro);
					return msg;
				}
			}
		}

		filtro.setOrdemprodutohistorico(oph);

		if(oph.getOrdemservicoproduto().getProduto().getModificado() == null || !oph.getOrdemservicoproduto().getProduto().getModificado()){
			//carrega o produto caso não tenha sido carregado ainda.
			oph.getOrdemservicoproduto().setProduto(ProdutoService.getInstance().findDadosLogisticosProdutoRF(oph.getOrdemservicoproduto().getProduto(),deposito));
			oph.getOrdemservicoproduto().getProduto().setModificado(true);
		}

		//mostra as informações resumidas do produto.
		showInfoResumidoProduto(filtro, oph.getOrdemservicoproduto().getProduto());
		
		return msg;
	}
	
	/**
	 * Mostra as informações simplificadas do produto
	 * 
	 * @param produto
	 * @throws IOException
	 */
	private void showInfoResumidoProduto(ReconferenciaRecebimentoFiltro filtro, Produto produto){
		
		StringBuilder sb = new StringBuilder();
		
		writeLine(sb, "Recebimento: " + filtro.getRecebimento().getCdrecebimento());
		writeLine(sb, "Veículo: " + filtro.getVeiculo());
		writeSeparator(sb);
		
		String codigoP = "";
		try {
			codigoP = produto.getCodigo();
		} catch (Exception e) {}
		
		writeLine(sb, "Código: " + codigoP);
		if (produto.getProdutoprincipal() != null){
			writeLine(sb, "Descrição: " + produto.getProdutoprincipal().getDescricao());
			writeLine(sb, "Volume: " + produto.getComplementocodigobarras().substring(0, 2) + "/" + 
					produto.getComplementocodigobarras().substring(2));
		}else
			writeLine(sb, "Descrição: " + produto.getDescricao());

		Produtoembalagem produtoembalagem = null;
		if(produto.getListaProdutoEmbalagem() != null && !produto.getListaProdutoEmbalagem().isEmpty()){
			produtoembalagem = produto.getListaProdutoEmbalagem().get(0);
		}
		
		if (TipoColeta.PADRAO.equals(filtro.getTipoColeta()))
			writeLine(sb, "Embalagem: " + (produtoembalagem == null ? "" : produtoembalagem.getDescricao()));

		writeSeparator(sb);
		
		filtro.setResultado(sb.toString());

	}

	public String validarProduto(ReconferenciaRecebimentoFiltro filtro) {
		
		List<Ordemprodutohistorico> listaOPH = recuperaObjetos(filtro);
		
		Ordemprodutohistorico oph = (Ordemprodutohistorico) CollectionUtils.find(listaOPH, new BeanPropertyValueEqualsPredicate("cdordemprodutohistorico", 
				filtro.getOrdemprodutohistorico().getCdordemprodutohistorico()));
		
		//verifica se o produto pertence a osp.			
		Boolean produtoPertence = produtoPertence(filtro, oph);

		if(produtoPertence){
			//mostra as informações do produto
			produtoInformations(oph, filtro);
		} else {
			return "O código de barras não pertence ao produto.";
		}
		
		return "OK";
	}
	
	/**
	 * Verifica se o código de barras bipado pertence ao produto.
	 * 
	 * @param codigo
	 * @param produto
	 * @return
	 */
	private Boolean produtoPertence(ReconferenciaRecebimentoFiltro filtro, Ordemprodutohistorico oph) {
		String codigo = filtro.getValorInicial();
		Produto produto = oph.getOrdemservicoproduto().getProduto();
		List<Produtocodigobarras> listaProdutoCodigoDeBarras = produto.getListaProdutoCodigoDeBarras();
		
		String codigo2 = getCodigoProduto(produto.getCodigo().trim()).toUpperCase();
		
		if((!configuracaoService.isTrue(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS, deposito))
				&& codigo2.equalsIgnoreCase(codigo.toUpperCase())){
			oph.setCodigoBarrasIndex(null);//Não encontrou através do código de barras
			return true;
		}
		for (int i = 0;i < listaProdutoCodigoDeBarras.size(); i++) {
			Produtocodigobarras produtocodigobarras = listaProdutoCodigoDeBarras.get(i);
			codigo2 = produtocodigobarras.getCodigo();
			if(codigo.trim().equalsIgnoreCase(codigo2.trim())){
				oph.setCodigoBarrasIndex(i);//Indice que encontrou o código de barras
				filtro.setCodigoBarrasConferencia(codigo2); // codigo de barras utilizado para identificar o produto.
				return true;
			}
		}
		return false;
	}
}
