package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtotipoestrutura;
import br.com.linkcom.wms.geral.bean.Tipoestrutura;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ProdutotipoestruturaDAO extends GenericDAO<Produtotipoestrutura>{
	
	/**
	 * Busca todos os produtotipoestrutura do produto no depósito
	 * selecionado
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param produto
	 * @return
	 */
	public List<Produtotipoestrutura> findByProduto(Produto produto) {
		if(produto == null || produto.getCdproduto() == null)
			throw new WmsException("O produto não deve ser nulo.");
		
		return query()
				.select("produtotipoestrutura.cdprodutotipoestrutura, produtotipoestrutura.restricaonivel," +
						"produtotipoestrutura.ordem,tipoestrutura.cdtipoestrutura,tipoestrutura.nome,produto.cdproduto,deposito.cddeposito")
				.leftOuterJoin("produtotipoestrutura.tipoestrutura tipoestrutura")
				.leftOuterJoin("produtotipoestrutura.produto produto")
				.leftOuterJoin("produtotipoestrutura.deposito deposito")
				.where("produto=?",produto)
				.where("deposito = ?",WmsUtil.getDeposito())
				.orderBy("produtotipoestrutura.ordem")
				.list();
	}

	/**
	 * Busca o tipo de estrutura blocado do produto
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param produto
	 * @return
	 */
	public Produtotipoestrutura findBlocado(Produto produto) {
		if(produto == null || produto.getCdproduto() == null)
			throw new WmsException("O produto não deve ser nulo.");
		return query()
				.select("produtotipoestrutura.cdprodutotipoestrutura, produtotipoestrutura.restricaonivel," +
						"tipoestrutura.cdtipoestrutura")
				.join("produtotipoestrutura.tipoestrutura tipoestrutura")
				.join("produtotipoestrutura.produto produto")
				.where("tipoestrutura = ?",Tipoestrutura.BLOCADO)
				.where("produto = ?",produto)
				.unique();
		}
	
	/**
	 * Atualiza o valor do campo ordem de todos os registros de estrutura do produto.
	 * O valor setado no campo ordem equivale ao cdtipoestrutura.
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param produto
	 * @param ordem
	 */
	public void updateOrdemByProduto(Produto produto) {
		if(produto == null || produto.getCdproduto() == null)
			throw new WmsException("O produto não deve ser nulo.");
		
		getHibernateTemplate().bulkUpdate("update Produtotipoestrutura set ordem = cdprodutotipoestrutura " +
										  "where produto = ? and deposito = ?",new Object[]{produto, WmsUtil.getDeposito()});
		
	}

}
