package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Tabelafrete;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.TabelafreteFiltro;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class TabelafreteDAO extends GenericDAO<Tabelafrete>{
	
	@Override
	public void updateListagemQuery(QueryBuilder<Tabelafrete> query,FiltroListagem _filtro) {
		
		TabelafreteFiltro filtro = (TabelafreteFiltro) _filtro;
		
			query.select("tabelafrete.cdtabelafrete, tabelafrete.validadeinicial, tabelafrete.validadefinal, tipotabelafrete.cdtipotabelafrete, " +
						 "tipotabelafrete.nome, tipoentrega.cdtipoentrega, tipoentrega.nome, deposito.cddeposito, deposito.nome");
			query.join("tabelafrete.tipotabelafrete tipotabelafrete");
			query.join("tabelafrete.tipoentrega tipoentrega");
			query.join("tabelafrete.deposito deposito");
			query.where("tabelafrete.cdtabelafrete = ?",filtro.getCdtabelafrete());
			query.where("tipotabelafrete = ?",filtro.getTipotabelafrete());
			query.where("tipoentrega = ?",filtro.getTipoentrega());
			query.where("deposito = ?",filtro.getDeposito());
			query.orderBy("tabelafrete.cdtabelafrete DESC");
		
		super.updateListagemQuery(query, filtro);
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Tabelafrete> query) {
		
			query.select("tabelafrete.cdtabelafrete, tabelafrete.validadeinicial, tabelafrete.validadefinal, transportador.cdpessoa, " +
						 "transportador.nome, tipoveiculo.cdtipoveiculo, tipoveiculo.nome, tipotabelafrete.cdtipotabelafrete, tipotabelafrete.nome, " +
						 "tipoentrega.cdtipoentrega, tipoentrega.nome, deposito.cddeposito, deposito.nome, listaTabelafreterota.cdtabelafreterota, " +
						 "listaTabelafreterota.valorentrega, rota.cdrota, rota.nome, tabelafretehistorico.cdtabelafretehistorico, " +
						 "tabelafretehistorico.dtalteracao, tabelafretehistorico.motivo, usuario.cdpessoa, usuario.nome, tiporotapraca.cdtiporotapraca," +
						 "tiporotapraca.nome, tabelafrete.valorfechado");
			query.join("tabelafrete.deposito deposito");
			query.join("tabelafrete.tipoentrega tipoentrega");
			query.join("tabelafrete.tipotabelafrete tipotabelafrete");
			query.leftOuterJoin("tabelafrete.transportador transportador");
			query.leftOuterJoin("tabelafrete.tipoveiculo tipoveiculo");
			query.leftOuterJoin("tabelafrete.listaTabelafreterota listaTabelafreterota");
			query.leftOuterJoin("listaTabelafreterota.rota rota");
			query.leftOuterJoin("rota.tiporotapraca tiporotapraca");
			query.leftOuterJoin("tabelafrete.listaTabelafretehistorico tabelafretehistorico");
			query.leftOuterJoin("tabelafretehistorico.usuario usuario");
			query.orderBy("tabelafretehistorico.dtalteracao desc");
		
		super.updateEntradaQuery(query);
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaTabelafreterota");
	}
	
	public Tabelafrete loadByTabelafrete(Tabelafrete tabelafrete){
		
		QueryBuilder<Tabelafrete> query = query();
		
		query.select("tabelafrete.cdtabelafrete, tabelafrete.validadeinicial, tabelafrete.validadefinal, transportador.cdpessoa, " +
					 "transportador.nome, tipoveiculo.cdtipoveiculo, tipoveiculo.nome, tipotabelafrete.cdtipotabelafrete, tipotabelafrete.nome," +
					 "tipoentrega.cdtipoentrega, tipoentrega.nome, deposito.cddeposito, deposito.nome, listaTabelafreterota.cdtabelafreterota, " +
					 "listaTabelafreterota.valorentrega, rota.cdrota, rota.nome, listaTabelafretehistorico.cdtabelafretehistorico");
		query.join("tabelafrete.deposito deposito");
		query.join("tabelafrete.tipoentrega tipoentrega");
		query.join("tabelafrete.tipotabelafrete tipotabelafrete");
		query.leftOuterJoin("tabelafrete.transportador transportador");
		query.leftOuterJoin("tabelafrete.tipoveiculo tipoveiculo");
		query.leftOuterJoin("tabelafrete.listaTabelafreterota listaTabelafreterota");
		query.leftOuterJoin("tabelafrete.listaTabelafretehistorico listaTabelafretehistorico");
		query.leftOuterJoin("listaTabelafreterota.rota rota");
		query.where("tabelafrete = ?",tabelafrete);
		
		return query.unique();
		
	}
	
}
