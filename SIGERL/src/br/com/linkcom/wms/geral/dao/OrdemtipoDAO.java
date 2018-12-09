package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("ordemtipo.nome")
public class OrdemtipoDAO extends GenericDAO<Ordemtipo> {

	@Override
	public void updateListagemQuery(QueryBuilder<Ordemtipo> query, FiltroListagem _filtro) {
//		OrdemtipoFiltro filtro = (OrdemtipoFiltro) _filtro;
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Ordemtipo> query) {
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
	}
	
	/* singleton */
	private static OrdemtipoDAO instance;
	public static OrdemtipoDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(OrdemtipoDAO.class);
		}
		return instance;
	}
}