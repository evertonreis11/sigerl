package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Tabelafretepraca;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class TabelafretepracaDAO extends GenericDAO<Tabelafretepraca>{
	
	/**
	 * 
	 * @param rota
	 * @return
	 */
	public List<Tabelafretepraca> findByRota(Rota rota){
		
		if(rota == null || rota.getCdrota() == null || rota.getDeposito() == null || rota.getDeposito().getCddeposito() == null){
			throw new WmsException("Parâmetros Inválidos!");
		}
		
		QueryBuilder<Tabelafretepraca> query = query()
		
			.select("tabelafretepraca.cdtabelafretepraca, tabelafretepraca.valorentrega, rota.cdrota, " +
					"rota.nome, praca.cdpraca, praca.nome, deposito.cddeposito, deposito.nome ")
			.join("tabelafretepraca.rota rota")
			.join("tabelafretepraca.praca praca")
			.join("rota.deposito deposito")
			.where("rota = ?",rota)
			.where("deposito = ?",rota.getDeposito());
		
		return query.list();
		
	}
	
}
