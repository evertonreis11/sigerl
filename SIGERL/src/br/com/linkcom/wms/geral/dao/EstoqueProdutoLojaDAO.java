package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.wms.geral.bean.EstoqueProdutoLoja;
import br.com.linkcom.wms.geral.bean.TipoEstoque;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class EstoqueProdutoLojaDAO extends GenericDAO<EstoqueProdutoLoja> {

	/**
	 * Recuperar estoque produto.
	 *
	 * @param cdProduto the cd produto
	 * @param tipoEstoque the tipo estoque
	 * @param cdDeposito the cd deposito
	 * @return the estoque produto loja
	 */
	public EstoqueProdutoLoja recuperarEstoqueProduto(Integer cdProduto, TipoEstoque tipoEstoque, Integer cdDeposito) {
		return query()
				.select("estoqueProdutoLoja.cdEstoqueProdutoLoja, estoqueProdutoLoja.qtde, estoqueProdutoLoja.dtInclusao, "+
						 "estoqueProdutoLoja.dtAlteracao, produto.cdproduto, tipoEstoque.cdTipoEstoque, deposito.cddeposito")
				.join("estoqueProdutoLoja.produto produto")
				.join("estoqueProdutoLoja.deposito deposito")
				.join("estoqueProdutoLoja.tipoEstoque tipoEstoque")
				.where("produto.cdproduto = ?", cdProduto)
				.where("deposito.cddeposito = ?", cdDeposito)
				.where("tipoEstoque = ?", tipoEstoque)
				.unique();
	}

}
