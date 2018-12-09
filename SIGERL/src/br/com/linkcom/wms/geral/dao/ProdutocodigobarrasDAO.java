package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtocodigobarras;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ProdutocodigobarrasDAO extends GenericDAO<Produtocodigobarras>{
	
	/**
	 * Busca todos os c�digos de barras do produto
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param produto
	 * @return
	 */
	public List<Produtocodigobarras> findByProduto(Produto produto) {
		if(produto == null || produto.getCdproduto() == null)
			throw new WmsException("O produto n�o deve ser nulo.");
		
		
		/*Queiroz - 05-05-2014 
		 * Acrescentando o campo Principal que indica qual o codigo de barras 
		 * � o principal do produto*/
		
		return query()
						.select("produtocodigobarras.cdprodutocodigobarras,produtocodigobarras.codigo," +
								"produtocodigobarras.valido,produtocodigobarras.interno," +
								"produtoembalagem.cdprodutoembalagem, produtoembalagem.descricao,produtocodigobarras.principal")
						.leftOuterJoin("produtocodigobarras.produtoembalagem produtoembalagem")
						.leftOuterJoin("produtocodigobarras.produto produto")
						.where("produto = ?",produto)
						.list();
	}
	
	/**
	 * 
	 * @param produto
	 * @return
	 */
	public List<Produtocodigobarras> findByProdutoInterno(Produto produto){
		if(produto == null || produto.getCdproduto() == null)
			throw new WmsException("O produto n�o deve ser nulo.");
		
		return query()
				.select("produtocodigobarras.cdprodutocodigobarras,produtocodigobarras.codigo," +
						"produtocodigobarras.valido,produtocodigobarras.interno")		
				.join("produtocodigobarras.produto produto")
				.where("produto = ?",produto)
				.where("produtocodigobarras.interno = ?",new Boolean(true))
				.list();
	}

	public List<Produtocodigobarras> findProdutosComMesmoCodigo(List<Produtocodigobarras> listaProdutoCodigoDeBarras, Produto bean) {
		return query()
		.leftOuterJoinFetch("produtocodigobarras.produto produto")
		.whereIn("produtocodigobarras.codigo", "'" + CollectionsUtil.listAndConcatenate(listaProdutoCodigoDeBarras, "codigo", "','") + "'")
		.where("produto.cdproduto <> ?", bean.getCdproduto())
		.list();
		
	}

}
