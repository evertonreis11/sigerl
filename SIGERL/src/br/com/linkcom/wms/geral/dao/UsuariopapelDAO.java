package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Usuariopapel;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class UsuariopapelDAO extends GenericDAO<Usuariopapel> {
	
	
	@Override
	public void updateListagemQuery(QueryBuilder<Usuariopapel> query, FiltroListagem filtro) {
		query
			.select("usuariopapel.cdusuariopapel,usuariopapel.cdpessoa,usuariopapel.cdpapel")
			.leftOuterJoinFetch("usuariopapel.usuario usuario")
			.leftOuterJoinFetch("usuariopapel.papel papel");
	}
	

	/* singleton */
	private static UsuariopapelDAO instance;
	public static UsuariopapelDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(UsuariopapelDAO.class);
		}
		return instance;
	}
}