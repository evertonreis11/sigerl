
package br.com.linkcom.wms.geral.dao;


import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Agendaacompanhamentoveiculo;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;


public class AgendaacompanhamentoveiculoDAO extends GenericDAO<Agendaacompanhamentoveiculo> {

	@Override
	public void updateListagemQuery(QueryBuilder<Agendaacompanhamentoveiculo> query, FiltroListagem _filtro) {

		query.leftOuterJoinFetch("agendaacompanhamentoveiculo.acompanhamentoveiculo acompanhamentoveiculo");
		
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		
	}
	
	
	
}
