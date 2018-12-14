package br.com.linkcom.wms.geral.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;

import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.Pedidovenda;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtocodigobarras;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaProduto;
import br.com.linkcom.wms.geral.bean.TipoEstoque;
import br.com.linkcom.wms.geral.bean.vo.RecebimentoLojaVO;
import br.com.linkcom.wms.geral.dao.ProdutoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class RecebimentoRetiraLojaProdutoService extends GenericService<RecebimentoRetiraLojaProduto> {
	
	private ProdutoDAO produtoDAO; 
	
	public void setProdutoDAO(ProdutoDAO produtoDAO) {
		this.produtoDAO = produtoDAO;
	}
	

	public void confirmarProdutoRecebido(String codigoEan,
			List<RecebimentoRetiraLojaProduto> listaRecebimentoRetiraLojaProduto, TipoEstoque tipoEstoque) {
		
		Produto produto = produtoDAO.loadByCodigoBarras(codigoEan);
		
		RecebimentoRetiraLojaProduto recebimentoRetiraLojaProduto = (RecebimentoRetiraLojaProduto) CollectionUtils.find(listaRecebimentoRetiraLojaProduto,
				new BeanPropertyValueEqualsPredicate("produto.cdproduto", produto.getCdproduto()));
		
		if (TipoEstoque.EXTRAVIADO.equals(recebimentoRetiraLojaProduto.getTipoEstoque())
				|| TipoEstoque.AVARIADO.equals(tipoEstoque)){
			recebimentoRetiraLojaProduto.setTipoEstoque(tipoEstoque);
			saveOrUpdate(recebimentoRetiraLojaProduto);
		}
	}


	/**
	 * Cria recebimento loja produto.
	 *
	 * @param vo the vo
	 * @return the recebimento retira loja produto
	 */
	public RecebimentoRetiraLojaProduto criaRecebimentoLojaProduto(RecebimentoLojaVO vo) {
		
		RecebimentoRetiraLojaProduto produtoRetiraLoja = new RecebimentoRetiraLojaProduto();
		
		produtoRetiraLoja.setTipoEstoque(TipoEstoque.EXTRAVIADO);
		produtoRetiraLoja.setQtde(vo.getQtde());
		
		Produto produto = new Produto(vo.getCdProduto(), vo.getDescricaoProduto());
		
		produto.setCodigo(vo.getCodigoProduto());
		
		Produtocodigobarras produtocodigobarras = new Produtocodigobarras();
		produtocodigobarras.setCdprodutocodigobarras(vo.getCdProdutoCodigoBarras());
		produtocodigobarras.setCodigo(vo.getCodigoBarras());
		produto.setListaProdutoCodigoDeBarras(new ListSet<Produtocodigobarras>(Produtocodigobarras.class, Arrays.asList(produtocodigobarras)));
		
		produtoRetiraLoja.setProduto(produto);
		
		Notafiscalsaida notaFiscalSaida = new Notafiscalsaida();
		notaFiscalSaida.setCdnotafiscalsaida(vo.getCdNotaFiscalSaida());		
		notaFiscalSaida.setNumeropedido(vo.getNumeroPedido());
		
		produtoRetiraLoja.setNotaFiscalSaida(notaFiscalSaida);
		
		return produtoRetiraLoja;
	}

}
