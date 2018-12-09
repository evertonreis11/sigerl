package br.com.linkcom.wms.geral.service;

import java.util.List;

import org.hibernate.Hibernate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.dao.ProdutoembalagemDAO;

public class ProdutoembalagemService extends GenericService<Produtoembalagem>{
	private ProdutoembalagemDAO produtoembalagemDAO;
	
	public void setProdutoembalagemDAO(ProdutoembalagemDAO produtoembalagemDAO) {
		this.produtoembalagemDAO = produtoembalagemDAO;
	}
	
	/* singleton */
	private static ProdutoembalagemService instance;
	public static ProdutoembalagemService getInstance() {
		if(instance == null){
			instance = Neo.getObject(ProdutoembalagemService.class);
		}
		return instance;
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param produto
	 * @return
	 */
	public Produtoembalagem findCompraByProduto (Produto produto){
		return produtoembalagemDAO.findCompraByProduto(produto);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param produto
	 * @return
	 */
	public List<Produtoembalagem> findByProduto (Produto produto){
		return produtoembalagemDAO.findByProduto(produto);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutoembalagemDAO#retirarEmbalagemCompra(Produto bean)
	 * 
	 * @param bean
	 */
	public void retirarEmbalagemCompra(Produto bean) {
		produtoembalagemDAO.retirarEmbalagemCompra(bean);		
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutoembalagemDAO#removeDescricoes(Produto produto)
	 * 
	 * @param bean
	 */
	public void removeDescricoes(Produto produto) {
		produtoembalagemDAO.removeDescricoes(produto);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutoembalagemDAO#findMenorEmbalagem(Produto produto)
	 * 
	 * @param produto
	 */
	public Produtoembalagem findMenorEmbalagem(Produto produto) {
		return produtoembalagemDAO.findMenorEmbalagem(produto);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @param produtoembalagem
	 * @return
	 * @author Tom�s Rabelo
	 */
	public Produtoembalagem getQtdeEmbalagem(Produtoembalagem produtoembalagem) {
		return produtoembalagemDAO.getQtdeEmbalagem(produtoembalagem);
	}

	/**
	 * Se endere�o de destino for picking carrega embalagem que possui menor quantidade. Se n�o carrega a embalagem padr�o
	 * 
	 * @param endereco
	 * @param produto
	 * @return
	 * @Author Tom�s Rabelo
	 */
	public Produtoembalagem getEmbalagemEnderecoDestino(Endereco endereco, Produto produto) {
		if(endereco.getEnderecofuncao().equals(Enderecofuncao.PICKING))
			return findMenorEmbalagem(produto);
		else
			return findCompraByProduto(produto);
	}
	
	
	/**
	 * Pega a embalagem de compra do produto. Caso o produto n�o possua uma embalagem para recebimento 
	 * � retornado null.
	 * 
	 * @param produto
	 * @return
	 */
	public Produtoembalagem getEmbalagemCompra(Produto produto){
		return getEmbalagemCompra(produto, false);
	}
	
	
	public Produtoembalagem getEmbalagemCompra(Produto produto, boolean carregarListaProdutoEmbalagem){
		if (produto.getListaProdutoEmbalagem() == null || !Hibernate.isInitialized(produto.getListaProdutoEmbalagem()))
			produto.setListaProdutoEmbalagem(ProdutoembalagemService.getInstance().findByProduto(produto));
		
			
		List<Produtoembalagem> listaProdutoEmbalagem = produto.getListaProdutoEmbalagem();
		if(carregarListaProdutoEmbalagem)
			listaProdutoEmbalagem = ProdutoembalagemService.getInstance().findByProduto(produto);
		for (Produtoembalagem produtoembalagem : listaProdutoEmbalagem) {
			if(produtoembalagem.getCompra() != null && produtoembalagem.getCompra())
				return produtoembalagem;
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public Integer sequenceIdNextval() {
		return produtoembalagemDAO.sequenceIdNextval();
	}
	
}
