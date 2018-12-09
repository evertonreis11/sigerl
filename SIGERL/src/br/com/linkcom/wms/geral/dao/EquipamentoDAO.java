package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Equipamento;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.EquipamentoFiltro;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class EquipamentoDAO extends GenericDAO<Equipamento>{

	@Override
	public void updateListagemQuery(QueryBuilder<Equipamento> query, FiltroListagem _filtro) {
		
		EquipamentoFiltro filtro = (EquipamentoFiltro) _filtro;
		
		query
			.select("equipamento.cdequipamento,equipamento.nome, equipamento.mac, equipamento.rastreado, " +
					"deposito.cddeposito, deposito.nome, empresa.cdempresa, empresa.nome")
			.leftOuterJoin("equipamento.deposito deposito")
			.leftOuterJoin("deposito.empresa empresa")
			.whereLikeIgnoreAll("equipamento.nome", filtro.getNome())
			.whereLikeIgnoreAll("equipamento.mac", filtro.getMac())
			.where("equipamento.rastreado = ? ",filtro.getRastreado())
			.where("deposito = ?",WmsUtil.getDeposito());
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Equipamento> query) {

		query
			.select("equipamento.cdequipamento, equipamento.nome, equipamento.mac, equipamento.rastreado, empresa.cdempresa, "
					+ "empresa.nome, deposito.cddeposito, deposito.nome, equipamento.fonenumero")
			.join("equipamento.empresa empresa")
			.join("equipamento.deposito deposito");
		
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Equipamento> findWithOutMotoristaVinculado(Deposito deposito) {
		return query()
			.select("equipamento.cdequipamento, equipamento.nome, motorista.cdmotorista")
			.leftOuterJoin("equipamento.motorista motorista")
			.where("equipamento.deposito = ?",deposito)
		.list();
	}

	/**
	 * 
	 * @param deposito
	 * @return
	 */
	public List<Equipamento> findByDeposito(Deposito deposito) {
		
		QueryBuilder<Equipamento> query = query();
			
			query.select("equipamento.cdequipamento, equipamento.nome")	
				.join("equipamento.deposito deposito")
				.where("deposito = ?",deposito);
		
		return query.list();
	}
	
}
