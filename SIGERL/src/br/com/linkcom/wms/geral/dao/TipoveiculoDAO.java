package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Tipoveiculo;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.TipoveiculoFiltro;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("tipoveiculo.nome")
public class TipoveiculoDAO extends GenericDAO<Tipoveiculo>{

	@Override
	public void updateListagemQuery(QueryBuilder<Tipoveiculo> query, FiltroListagem _filtro) {
		TipoveiculoFiltro filtro = (TipoveiculoFiltro) _filtro;
		query
			  .select("tipoveiculo.cdtipoveiculo, tipoveiculo.nome")
			  .whereLikeIgnoreAll("tipoveiculo.nome", filtro.getNome())
			  .orderBy("tipoveiculo.nome");
	}
	
}
