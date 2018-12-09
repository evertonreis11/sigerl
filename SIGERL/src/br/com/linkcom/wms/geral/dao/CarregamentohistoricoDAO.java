package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentohistorico;
import br.com.linkcom.wms.geral.bean.Expedicao;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class CarregamentohistoricoDAO extends GenericDAO<Carregamentohistorico>{

	/**
	 * 
	 * @param whereIn
	 */
	public void deleteByCarregamentoWhereIn(String whereIn) {
		if(whereIn == null || whereIn.isEmpty()) 
			throw new WmsException("Parâmetros inválidos.");

		getJdbcTemplate().update("delete from Carregamentohistorico ch where ch.cdcarregamento in (?)", new Object[]{whereIn});
	}

	/**
	 * 
	 * @param carregamento
	 */
	public void deleteByCarregamento(Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("delete from Carregamentohistorico ch where ch.carregamento.id = ?", new Object[]{carregamento.getCdcarregamento()});
	}

	/**
	 * 
	 * @param expedicao
	 * @return
	 */
	public List<Carregamentohistorico> findByExpedicao(Expedicao expedicao) {
		return query()
			.select("carregamentohistorico.cdcarregamentohistorico, carregamentohistorico.dtaltera, " +
					"carregamentohistorico.usuarioaltera, carregamentohistorico.descricao, carregamentostatus.nome, " +
					"carregamento.cdcarregamento, carregamentohistoricotipo.cdcarregamentohistoricotipo, " +
					"carregamentohistoricotipo.nome")
			.join("carregamentohistorico.carregamentostatus carregamentostatus")
			.join("carregamentohistorico.carregamento carregamento")
			.join("carregamento.expedicao expedicao")
			.leftOuterJoin("carregamentohistorico.carregamentohistoricotipo carregamentohistoricotipo")
			.where("expedicao = ?",expedicao)
			.orderBy("carregamentohistorico.dtaltera, carregamentostatus.cdcarregamentostatus")
			.list();
	}

	/**
	 * 
	 * @param cdcarregamentos
	 * @return
	 */
	public List<Carregamentohistorico> findByCarregamentoPreValidados(String cdcarregamentos) {
		
		return query()
			.select("carregamentohistorico.cdcarregamentohistorico, carregamentohistorico.dtaltera, " +
					"carregamentohistorico.descricao, carregamentostatus.nome, carregamentostatus.cdcarregamentostatus, " +
					"carregamentostatus.nome, carregamento.cdcarregamento, usuarioaltera.cdpessoa, usuarioaltera.nome," +
					"carregamentohistoricotipo.cdcarregamentohistoricotipo, carregamentohistoricotipo.nome")
			.join("carregamentohistorico.carregamentostatus carregamentostatus")
			.join("carregamentohistorico.carregamento carregamento")
			.join("carregamentohistorico.usuarioaltera usuarioaltera")
			.leftOuterJoin("carregamentohistorico.carregamentohistoricotipo carregamentohistoricotipo")
			.whereIn("carregamento.cdcarregamento",cdcarregamentos)
			.where("carregamentohistoricotipo.cdcarregamentohistoricotipo = 2")
			.orderBy("carregamentohistorico.dtaltera, carregamentostatus.cdcarregamentostatus")
		.list();
	}

}