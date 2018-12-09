package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Acao;
import br.com.linkcom.wms.modulo.sistema.controller.crud.filtro.AcaoFiltro;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class AcaoDAO extends GenericDAO<Acao> {
	
	@Override
	public void updateListagemQuery(QueryBuilder<Acao> query,FiltroListagem _filtro) {
		AcaoFiltro filtro = (AcaoFiltro) _filtro;
		
		query
			.whereLikeIgnoreAll("acao.descricao", filtro.getDescricao())
			.whereLikeIgnoreAll("acao.key", filtro.getKey())
			.orderBy("acao.descricao")
		;
		
	}
	
}
