package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Lancarcorte;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class LancarcorteDAO extends GenericDAO<Lancarcorte> {
	@Override
	public void updateListagemQuery(QueryBuilder<Lancarcorte> query, FiltroListagem _filtro) {
		
	}
}
