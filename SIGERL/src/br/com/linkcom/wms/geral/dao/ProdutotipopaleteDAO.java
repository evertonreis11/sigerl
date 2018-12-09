package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtotipopalete;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;

public class ProdutotipopaleteDAO extends br.com.linkcom.wms.util.neo.persistence.GenericDAO<Produtotipopalete> {

	@Override
	public void updateListagemQuery(QueryBuilder<Produtotipopalete> query, FiltroListagem _filtro) {
//		ProdutotipopaleteFiltro filtro = (ProdutotipopaleteFiltro) _filtro;
		query.leftOuterJoinFetch("produtotipopalete.produto produto");
		query.leftOuterJoinFetch("produtotipopalete.deposito deposito");
		query.leftOuterJoinFetch("produtotipopalete.tipopalete tipopalete");
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Produtotipopalete> query) {
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
	}
	
	/**
	 * Busca todos os produtotipopalete deste produto e do 
	 * depósito logado
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param produto
	 * @return
	 */
	public List<Produtotipopalete> findByProduto(Produto produto,Deposito deposito) {
		if(produto == null || produto.getCdproduto() == null)
			throw new WmsException("O produto não deve ser nulo.");
		
		return query()
					.select("produtotipopalete.cdprodutotipopalete, produtotipopalete.lastro,produtotipopalete.camada,produtotipopalete.padrao," +
							"tipopalete.cdtipopalete, tipopalete.nome,produto.cdproduto,deposito.cddeposito")
					.join("produtotipopalete.tipopalete tipopalete")
					.join("produtotipopalete.produto produto")
					.join("produtotipopalete.deposito deposito")
					.where("produto=?",produto)
					.where("deposito=?",deposito)
					.list();
	}
	
	/**
	 * 
	 * Método que verifica se um determinado produto possui características de tipo palete e se ele está presente no depósito 
	 * escolhido no 'login' do sistema.
	 * 
	 * @param produto
	 * @return Boolean
	 * 
	 * @author Arantes
	 * 
	 */
	public Boolean hasProdutotipopaletadeposito(Produto produto) {
		if((produto == null) || (produto.getCdproduto() == null))
			throw new WmsException("O produto não deve ser nulo.");
		
		Produtotipopalete produtotipopalete = 
			query()
				.where("produtotipopalete.produto = ?", produto)
				.where("produtotipopalete.deposito = ?", WmsUtil.getDeposito())
				.setMaxResults(1)
				.unique();
		
		return (produtotipopalete == null ? Boolean.FALSE: Boolean.TRUE);
	}
	
	/* singleton */
	private static ProdutotipopaleteDAO instance;
	public static ProdutotipopaleteDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(ProdutotipopaleteDAO.class);
		}
		return instance;
	}	

	/**
	 * 
	 * Busca as informações do palete do produto
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param produto
	 * @param deposito
	 * @return
	 */
	public Produtotipopalete findPaletePadrao(Produto produto,Deposito deposito) {
		return query()
					.select("produtotipopalete.cdprodutotipopalete,produtotipopalete.lastro,produtotipopalete.camada," +
							"tipopalete.cdtipopalete")
					.join("produtotipopalete.deposito deposito")
					.join("produtotipopalete.tipopalete tipopalete")
					.where("produtotipopalete.padrao is true")
					.where("deposito = ?",deposito)
					.where("produtotipopalete.produto=?", produto)
					.setMaxResults(1)
					.unique();
		
	}
}