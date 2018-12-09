package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtotipoestrutura;
import br.com.linkcom.wms.geral.dao.ProdutotipoestruturaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ProdutotipoestruturaService extends GenericService<Produtotipoestrutura>{
	private ProdutotipoestruturaDAO produtotipoestruturaDAO;
	
	public void setProdutotipoestruturaDAO(ProdutotipoestruturaDAO produtotipoestruturaDAO) {
		this.produtotipoestruturaDAO = produtotipoestruturaDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutotipoestruturaDAO.findByProduto(Produto produto)
	 * 
	 * @param produto
	 * @return
	 */
	public List<Produtotipoestrutura> findByProduto(Produto produto) {
		return produtotipoestruturaDAO.findByProduto(produto);
	}
	
	/* singleton */
	private static ProdutotipoestruturaService instance;
	public static ProdutotipoestruturaService getInstance() {
		if(instance == null){
			instance = Neo.getObject(ProdutotipoestruturaService.class);
		}
		return instance;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param produto
	 * @return
	 */
	public Produtotipoestrutura findBlocado(Produto produto) {
		return produtotipoestruturaDAO.findBlocado(produto);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutotipoestruturaDAO#updateOrdemByProduto(Produto produto, int ordem)
	 * 
	 * @param produto
	 * @param ordem
	 */
	public void updateOrdemByProduto(Produto produto) {
		produtotipoestruturaDAO.updateOrdemByProduto(produto);
	}

}
