package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Permissao;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class PermissaoDAO extends GenericDAO<Permissao> {

	@Override
	public void updateListagemQuery(QueryBuilder<Permissao> query, FiltroListagem filtro) {
		query.leftOuterJoinFetch("permissao.papel papel");
	}

	/* singleton */
	private static PermissaoDAO instance;
	public static PermissaoDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(PermissaoDAO.class);
		}
		return instance;
	}
}