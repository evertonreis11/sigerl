package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ProdutoembalagemDAO extends GenericDAO<Produtoembalagem>{
	
	/**
	 * Busca todos os produtoembalagem do produto
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * 
	 * @param produto
	 * @return
	 */
	public Produtoembalagem findCompraByProduto(Produto produto) {
		if (produto == null || produto.getCdproduto() == null)
			throw new WmsException("O produto n�o deve ser nulo.");
		
		return query()
				.select("produtoembalagem.cdprodutoembalagem,produtoembalagem.descricao," +
					"produtoembalagem.qtde,produtoembalagem.fator,produtoembalagem.compra," +
					"produtoembalagem.origemerp")
				.join("produtoembalagem.produto produto")
				.where("produtoembalagem.compra is true")
				.where("produto = ?",produto)
				.setMaxResults(1)
				.unique();
	}
	
	/**
	 * Busca todos os produtoembalagem do produto no dep�sito
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * 
	 * @param produto
	 * @return
	 */
	public List<Produtoembalagem> findByProduto(Produto produto) {
		if(produto == null || produto.getCdproduto() == null)
			throw new WmsException("O produto n�o deve ser nulo.");
		
		return query()
					.select("produtoembalagem.cdprodutoembalagem, produtoembalagem.descricao," +
							"produtoembalagem.fator,produtoembalagem.compra,produtoembalagem.qtde, " +
							"produtoembalagem.origemerp")
					.join("produtoembalagem.produto produto")
					.where("produto = ?",produto)
					.list();
	}

	/**
	 * Retira o atributo de compra de todas as embalagens do produto
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param bean
	 */
	public void retirarEmbalagemCompra(Produto bean) {
		if(bean == null || bean.getCdproduto() == null)
			throw new WmsException("O produto n�o deve ser nulo.");
		
		getHibernateTemplate().bulkUpdate("update Produtoembalagem pe set pe.compra = 0 where pe.produto = ?",new Object[]{bean});
		
		
	}
	
	/**
	 * Muda as descri��es das embalagens do produto para que o indece n�o
	 * atrapalhe na hora de salvar os registros.
	 * (esse indece n�o pode ser retirado pois os 
	 * dados da tabela vir�o do ERP e isso ir� assegurar que n�o venham dados errados do mesmo)
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param produto
	 */
	public void removeDescricoes(Produto produto) {
		if(produto == null || produto.getCdproduto() == null)
			throw new WmsException("O produto n�o deve ser nulo.");
		
		getHibernateTemplate().bulkUpdate("update Produtoembalagem pe set pe.descricao = pe.cdprodutoembalagem " +
										  "where pe.produto = ?",new Object[]{produto});
	}
	
	/**
	 * Encontra a embalagem do produto com a menor quantidade
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param produto
	 */
	public Produtoembalagem findMenorEmbalagem(Produto produto) {
		if (produto == null || produto.getCdproduto() == null)
			throw new WmsException("O produto n�o deve ser nulo.");
			
		return query()
				.select("produtoembalagem.cdprodutoembalagem,produtoembalagem.descricao," +
						"produtoembalagem.qtde,produtoembalagem.fator,produtoembalagem.compra," +
						"produtoembalagem.origemerp")
				.join("produtoembalagem.produto produto")
				.where("produto = ?",produto)
				.setMaxResults(1)
				.orderBy("produtoembalagem.qtde")
				.unique();
	}

	/**
	 * M�todo que retorna qtde do produto embalagem
	 * 
	 * @param produtoembalagem
	 * @return
	 * @author Tom�s Rabelo
	 */
	public Produtoembalagem getQtdeEmbalagem(Produtoembalagem produtoembalagem) {
		if (produtoembalagem == null || produtoembalagem.getCdprodutoembalagem() == null)
			throw new WmsException("A embalagem n�o deve ser nula.");
		
		return query()
			.select("produtoembalagem.cdprodutoembalagem,produtoembalagem.qtde")
			.where("produtoembalagem = ?",produtoembalagem)
			.unique();
	}

	/**
	 * 
	 * @return
	 */
	public Integer sequenceIdNextval() {
		String sql = "select sq_produtoembalagem.nextval from dual";
		return getJdbcTemplate().queryForInt(sql);
	}
	
}
