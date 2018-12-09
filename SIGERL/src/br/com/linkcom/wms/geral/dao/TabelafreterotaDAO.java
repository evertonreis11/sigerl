package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Tabelafreterota;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class TabelafreterotaDAO extends GenericDAO<Tabelafreterota>{
	
	public Tabelafreterota loadForFretePraca(Tabelafreterota tabelafreterota) {
		
		QueryBuilder<Tabelafreterota> query = query();
			
			query.select("tabelafreterota.cdtabelafreterota, rota.cdrota, tabelafrete.cdtabelafrete, listaTabelafretepraca.cdtabelafretepraca," +
						 "listaTabelafretepraca.valorentrega, praca.cdpraca, praca.nome, tabelafreterota.valorentrega, tipoentrega.cdtipoentrega ");
			query.join("tabelafreterota.rota rota");
			query.join("tabelafreterota.tabelafrete tabelafrete");
			query.join("tabelafrete.tipoentrega tipoentrega");
			query.leftOuterJoin("tabelafreterota.listaTabelafretepraca listaTabelafretepraca");
			query.leftOuterJoin("listaTabelafretepraca.praca praca");
			query.where("tabelafreterota = ?",tabelafreterota);
		
		return query.unique();
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaTabelafretepraca");
	}
	
}
