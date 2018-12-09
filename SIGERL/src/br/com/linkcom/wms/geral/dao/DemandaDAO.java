package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.modulo.sistema.controller.crud.filtro.DemandaFiltro;
import br.com.linkcom.wms.geral.bean.Demanda;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class DemandaDAO extends GenericDAO<Demanda>{

	@Override
	public void updateListagemQuery(QueryBuilder<Demanda> query, FiltroListagem _filtro){
		
		DemandaFiltro filtro = (DemandaFiltro) _filtro;
		
		query.select("demanda.cddemanda, demanda.funcionalidade, demanda.enderecourl, demanda.dtprevisao, "+
					"demanda.dthomologacao, demanda.dtimplantacao, demanda.numeroversao");
		
		query.where("demanda.dtprevisao = ?", filtro.getDtprevisao());
		query.where("demanda.dthomologacao = ?", filtro.getDthomologacao());
		query.where("demanda.dtimplantacao = ?", filtro.getDtimplantacao());
		query.where("demanda.enderecourl = ?", filtro.getEnderecourl());
		query.where("demanda.numeroversao = ?", filtro.getNumeroversao());
		query.whereLikeIgnoreAll("demanda.funcionalidade",filtro.getFuncionalidade());
		
		query.orderBy("demanda.dtimplantacao DESC");
		
	}

	/**
	 * 
	 * @return
	 */
	public List<Demanda> findLastVersion() {
		
		QueryBuilder<Demanda> query = query();
		
		query.select("demanda.cddemanda, demanda.funcionalidade, demanda.enderecourl, demanda.dtprevisao, demanda.dthomologacao, demanda.dtimplantacao, demanda.numeroversao");
		query.where("demanda.numeroversao = (select max(d.numeroversao) from demanda d where demanda.dtimplantacao is not null)");
		
		return query.list();
	}

	
	
}
