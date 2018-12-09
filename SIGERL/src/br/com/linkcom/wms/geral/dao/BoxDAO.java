package br.com.linkcom.wms.geral.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Boxstatus;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.BoxFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;

public class BoxDAO extends GenericDAO<Box> {

	@Override
	public void updateListagemQuery(QueryBuilder<Box> query,FiltroListagem _filtro) {
		BoxFiltro filtro = (BoxFiltro) _filtro;
		query
			.select("box.cdbox,box.nome, deposito.cddeposito,deposito.nome, boxstatus.cdboxstatus, boxstatus.nome")
			.join("box.boxstatus boxstatus")
			.leftOuterJoin("box.deposito deposito")
			.whereLikeIgnoreAll("box.nome",filtro.getNome())
			.where("box.deposito=?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
			.where("boxstatus=?",filtro.getBoxstatus())
			.orderBy("box.nome");
	}
	

	/**
	 * Encontra todos os boxes do dep�sito que est�o com o status dispon�vel.
	 * 
	 * @author Pedro Gon�alves
	 * @return
	 */
	public List<Box> findAllDisponiveis() {
		return query()
				.select("box.cdbox, box.nome")
				.join("box.boxstatus boxstatus")
				.where("boxstatus=?",Boxstatus.DISPONIVEL)
				.where("box.deposito =?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
				.orderBy("box.nome")
				.list();
	}
	
	@Override
	public List<Box> findForCombo(String orderby, String... extraFields) {
		return query()
				.select("box.cdbox, box.nome")
				.join("box.boxstatus boxstatus")
				.where("box.deposito =?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
//				.where("boxstatus = ?",Boxstatus.DISPONIVEL)
				.orderBy("box.nome")
				.list();
	}
	
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Box> query) {
		query.leftOuterJoin("box.deposito deposito");
	}


	/**
	 * M�todo que carrega o box e seus respectivos carregamentos para separa��o
	 * 
	 * @param whereIn
	 * @return
	 */
	public List<Box> getAllBoxesForSeparacao(String whereIn) {
		List<Carregamentostatus> lista = new ArrayList<Carregamentostatus>();
		lista.add(Carregamentostatus.EM_MONTAGEM);
		lista.add(Carregamentostatus.MONTADO);
		
		return query()
			.select("box.cdbox, box.nome, listaCarregamentos.cdcarregamento, listaCarregamentos.paletesdisponiveis, veiculo.cdveiculo, veiculo.placa, tipoveiculo.cdtipoveiculo, tipoveiculo.nome")
			.join("box.listaCarregamentos listaCarregamentos")
			.join("listaCarregamentos.deposito deposito")
			.leftOuterJoin("listaCarregamentos.veiculo veiculo")
			.leftOuterJoin("veiculo.tipoveiculo tipoveiculo")
			.leftOuterJoin("listaCarregamentos.carregamentostatus carregamentostatus")
			.where("deposito = ?", WmsUtil.getDeposito())
			.whereIn("box.cdbox", whereIn)
			.whereIn("carregamentostatus.cdcarregamentostatus", CollectionsUtil.listAndConcatenate(lista,"cdcarregamentostatus", ","))
			.orderBy("box.cdbox, listaCarregamentos.cdcarregamento")
			.list();
	}


	/**
	 * M�todo que atualiza status do box
	 * 
	 * @param box
	 * @param boxstatus
	 * @author Tom�s Rabelo
	 */
	public void updateStatusBox(Box box, Boxstatus boxstatus) {
		if(box == null || box.getCdbox() == null || boxstatus == null || boxstatus.getCdboxstatus() == null)
			throw new WmsException("Par�metros inv�lidos.");
		
		getHibernateTemplate().bulkUpdate("update Box box set box.boxstatus.id=? where box.id=? ", new Object[]{boxstatus.getCdboxstatus(), box.getCdbox()});		
	}
	
}
