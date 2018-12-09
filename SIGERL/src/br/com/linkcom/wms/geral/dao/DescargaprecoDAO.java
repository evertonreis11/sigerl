package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Descargapreco;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro.DescargaprecoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

/**
 * 
 * @author Guilherme Arantes de Oliveira
 *
 */
@DefaultOrderBy("descargapreco.nome")
public class DescargaprecoDAO extends GenericDAO<Descargapreco> {
	
	@Override
	public void updateListagemQuery(QueryBuilder<Descargapreco> query, FiltroListagem _filtro) {
		DescargaprecoFiltro descargaprecoFiltro = (DescargaprecoFiltro) _filtro;
		
		query
			.select("descargapreco.cddescargapreco, descargapreco.nome, descargatipocalculo.cddescargatipocalculo, descargatipocalculo.nome, descargapreco.valor")
			.join("descargapreco.descargatipocalculo descargatipocalculo")
			.where("descargapreco.deposito.id = ? ", WmsUtil.getDeposito().getCddeposito(), WmsUtil.isFiltraDeposito())
			.whereLikeIgnoreAll("descargapreco.nome", descargaprecoFiltro.getNome())
			.where("descargapreco.descargatipocalculo = ?", descargaprecoFiltro.getDescargatipocalculo());
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Descargapreco> query) {
		query
			.joinFetch("descargapreco.descargatipocalculo descargatipocalculo")
			.leftOuterJoinFetch("descargapreco.listaDescargaPrecoVeiculo listaDescargaPrecoVeiculo")
			.leftOuterJoinFetch("listaDescargaPrecoVeiculo.tipoveiculo tipoveiculo");
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaDescargaPrecoVeiculo");
	}
	
	/**
	 * Método que carrega um objeto descargapreco contendo os objetos descargatipocalculo e descargaprecoveiculo
	 * 
	 * @param bean
	 * @return
	 * @author Arantes
	 */
	public Descargapreco loadFull(Descargapreco bean) {
		if((bean == null) || (bean.getCddescargapreco() == null)) {
			throw new WmsException("A cobrança de descarga não deve ser nula.");
		
		}
		
		return query()
					.leftOuterJoinFetch("descargapreco.descargatipocalculo descargatipocalculo")
					.leftOuterJoinFetch("descargapreco.listaDescargaPrecoVeiculo descargaprecoveiculo")
					.where("descargapreco = ?", bean)
					.unique();
	}
	
	/**
	 * Método que carrega uma lista de descargapreco sendo informado um produto
	 * 
	 * @param produto
	 * @return
	 * @author Arantes
	 */
	public List<Descargapreco> findByProduto(Produto produto) {
		if((produto == null) || (produto.getCdproduto() == null)) {
			throw new WmsException("O produto não deve ser nulo.");
		}
		
		return query()
			.select("descargapreco.cddescargapreco, descargapreco.nome")			
			.leftOuterJoin("descargapreco.listaDadologistico dadologistico")
			.join("dadologistico.produto produto")
			.where("dadologistico.deposito = ?", WmsUtil.getDeposito().getCddeposito(), WmsUtil.isFiltraDeposito())
			.where("produto = ?", produto)
			.orderBy("descargapreco.nome")
			.list();
	}
	
	@Override
	public List<Descargapreco> findAll() {
		return query()
					.select("descargapreco.cddescargapreco, descargapreco.nome")
					.where("descargapreco.deposito = ?", WmsUtil.getDeposito())
					.orderBy("descargapreco.nome")
					.list();
	}

	@Override
	public List<Descargapreco> findForCombo(String orderby, String... extraFields) {
		return query()
			.select("descargapreco.cddescargapreco, descargapreco.nome")
			.join("descargapreco.deposito deposito")
			.where("deposito = ?", WmsUtil.getDeposito())
			.orderBy("descargapreco.nome")
			.list();
	}
	
}
