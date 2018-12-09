package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("recebimentostatus.nome")
public class RecebimentostatusDAO extends GenericDAO<Recebimentostatus> {

	@Override
	public void updateListagemQuery(QueryBuilder<Recebimentostatus> query, FiltroListagem _filtro) {
//		RecebimentostatusFiltro filtro = (RecebimentostatusFiltro) _filtro;
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Recebimentostatus> query) {
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
	}

	/* singleton */
	private static RecebimentostatusDAO instance;
	public static RecebimentostatusDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(RecebimentostatusDAO.class);
		}
		return instance;
	}
}