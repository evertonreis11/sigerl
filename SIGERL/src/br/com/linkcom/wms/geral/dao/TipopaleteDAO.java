package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Tipopalete;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.TipopaleteFiltro;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("tipopalete.nome")
public class TipopaleteDAO extends GenericDAO<Tipopalete> {
	@Override
	public void updateListagemQuery(QueryBuilder<Tipopalete> query, FiltroListagem _filtro) {
		
		TipopaleteFiltro filtro = (TipopaleteFiltro) _filtro;		
		query.whereLikeIgnoreAll("tipopalete.nome", filtro.getNome())
		.orderBy("tipopalete.nome");
	}
}
