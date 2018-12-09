package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.BoxFiltro;
import br.com.linkcom.wms.util.WmsUtil;

public class BoxexpedicaoDAO extends GenericDAO<Box> {

	@Override
	public void updateListagemQuery(QueryBuilder<Box> query,FiltroListagem _filtro) {
		BoxFiltro filtro = (BoxFiltro) _filtro;
		query
			.select("box.cdbox,box.nome, deposito.cddeposito,deposito.nome, boxstatus.cdboxstatus, boxstatus.nome")
			.join("box.boxstatus boxstatus")
			.leftOuterJoin("box.deposito deposito")
			.whereLikeIgnoreAll("box.nome",filtro.getNome())
			.where("box.deposito=?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
			.where("boxstatus=?",filtro.getBoxstatus());
	}

}
