package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Produtotipopalete;
import br.com.linkcom.wms.geral.dao.ProdutotipopaleteDAO;


public class ProdutotipopaleteService extends br.com.linkcom.wms.util.neo.persistence.GenericService<Produtotipopalete> {

	@SuppressWarnings("unused")
	private ProdutotipopaleteDAO produtotipopaleteDAO;
	private ProdutoembalagemService produtoembalagemService;
	
	public void setProdutotipopaleteDAO(ProdutotipopaleteDAO produtotipopaleteDAO) {
		this.produtotipopaleteDAO = produtotipopaleteDAO;
	}
	public void setProdutoembalagemService(
			ProdutoembalagemService produtoembalagemService) {
		this.produtoembalagemService = produtoembalagemService;
	}

	/* singleton */
	private static ProdutotipopaleteService instance;
	public static ProdutotipopaleteService getInstance() {
		if(instance == null){
			instance = Neo.getObject(ProdutotipopaleteService.class);
		}
		return instance;
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutotipopaleteDAO#findByProduto(Produto produto)
	 * 
	 * @param produto
	 * @return
	 */
	public List<Produtotipopalete> findByProduto(Produto produto,Deposito deposito) {
		return produtotipopaleteDAO.findByProduto(produto,deposito);
	}
	
	/**
	 * 
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Arantes
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutotipopaleteDAO#hasProdutotipopaletadeposito(Produto) 
	 * @param produto
	 * @return Boolean
	 * 
	 */
	public Boolean hasProdutotipopaletadeposito(Produto produto) {
		return produtotipopaleteDAO.hasProdutotipopaletadeposito(produto);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param produto
	 * @param deposito
	 * @return
	 */
	public Produtotipopalete findPaletePadrao(Produto produto,Deposito deposito) {
		return produtotipopaleteDAO.findPaletePadrao(produto,deposito);
	}
	
	/**
	 * Retorna a ocupa��o de um produto em um pallet.
	 * 
	 * @author Tom�s Rabelo
	 * @param produto
	 * @param deposito
	 * @return
	 */
	public Long getPalletsOcupados(Produto produto, Deposito deposito) {
		Produtotipopalete produtotipopalete = findPaletePadrao(produto, deposito);
		if(produtotipopalete != null && produtotipopalete.getCdprodutotipopalete() != null){
			Produtoembalagem produtoembalagem = produtoembalagemService.findCompraByProduto(produto);
			if(produtoembalagem != null && produtoembalagem.getCdprodutoembalagem() != null)
				return produtotipopalete.getCamada() * produtotipopalete.getLastro() * produtoembalagem.getQtde();
		}
		return null;
	}

	
}
