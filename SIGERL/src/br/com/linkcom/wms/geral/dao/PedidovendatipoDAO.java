package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Pedidovendatipo;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.PedidovendatipoFiltro;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class PedidovendatipoDAO  extends GenericDAO<Pedidovendatipo> {

	@Override
	public void updateListagemQuery(QueryBuilder<Pedidovendatipo> query, FiltroListagem _filtro) {
		PedidovendatipoFiltro filtro = (PedidovendatipoFiltro) _filtro;
		query
		.select("pedidovendatipo.cdpedidovendatipo, pedidovendatipo.nome")
		.whereLikeIgnoreAll("pedidovendatipo.nome", filtro.getNome())
		.orderBy("pedidovendatipo.nome asc");
	}
	
	/* singleton */
	private static NotafiscaltipoDAO instance;
	public static NotafiscaltipoDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(NotafiscaltipoDAO.class);
		}
		return instance;
	}
}