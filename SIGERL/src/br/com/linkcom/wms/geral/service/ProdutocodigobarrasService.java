package br.com.linkcom.wms.geral.service;

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtocodigobarras;
import br.com.linkcom.wms.geral.dao.ProdutocodigobarrasDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ProdutocodigobarrasService extends GenericService<Produtocodigobarras>{
	
	private ProdutocodigobarrasDAO produtocodigobarrasDAO;
	
	public void setProdutocodigobarrasDAO(ProdutocodigobarrasDAO produtocodigobarrasDAO) {
		this.produtocodigobarrasDAO = produtocodigobarrasDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutocodigobarrasDAO#findByProduto(Produto produto)
	 * 
	 * @param produto
	 * @return
	 */
	public List<Produtocodigobarras> findByProduto(Produto produto) {
		return produtocodigobarrasDAO.findByProduto(produto);
	}
	
	/**
	 * 
	 * @param produto
	 * @return
	 */
	public List<Produtocodigobarras> findByProdutoInterno(Produto produto){
		return produtocodigobarrasDAO.findByProdutoInterno(produto);
	}
	
	/* singleton */
	private static ProdutocodigobarrasService instance;
	public static ProdutocodigobarrasService getInstance() {
		if(instance == null){
			instance = Neo.getObject(ProdutocodigobarrasService.class);
		}
		return instance;
	}

	public List<Produto> findProdutosComMesmoCodigo(List<Produtocodigobarras> listaProdutoCodigoDeBarras, Produto bean) {
		List<Produtocodigobarras> lista = produtocodigobarrasDAO.findProdutosComMesmoCodigo(listaProdutoCodigoDeBarras, bean);
		List<Produto> listaProduto = new ArrayList<Produto>();
		for (Produtocodigobarras pcb : lista) {
			listaProduto.add(pcb.getProduto());
		}
		return listaProduto;
	}
	
}
