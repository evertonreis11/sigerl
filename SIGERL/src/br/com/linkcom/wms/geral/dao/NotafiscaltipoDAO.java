package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Notafiscaltipo;
import br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro.NotafiscaltipoFiltro;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class NotafiscaltipoDAO extends GenericDAO<Notafiscaltipo> {

	@Override
	public void updateListagemQuery(QueryBuilder<Notafiscaltipo> query, FiltroListagem _filtro) {
		NotafiscaltipoFiltro filtro = (NotafiscaltipoFiltro) _filtro;
		
		query
		.select("notafiscaltipo.cdnotafiscaltipo, notafiscaltipo.nome, notafiscaltipo.exigeagenda")
		.whereLikeIgnoreAll("notafiscaltipo.nome", filtro.getNome());
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Notafiscaltipo> query) {
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
	}
	

	/* singleton */
	private static NotafiscaltipoDAO instance;
	public static NotafiscaltipoDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(NotafiscaltipoDAO.class);
		}
		return instance;
	}

	/**
	 * Busca os tipos de nota fiscais que exigem o agendamento.
	 *
	 * @return
	 * @author Rodrigo Freitas
	 */
	public List<Notafiscaltipo> findExigeAgendamento() {
		return query()
					.select("notafiscaltipo.cdnotafiscaltipo, notafiscaltipo.nome, notafiscaltipo.exigeagenda")
					.where("notafiscaltipo.exigeagenda = ?", Boolean.TRUE)
					.list();
	}

	/**
	 * Atualiza os tipos passados por parametro para exigir agendamento.
	 *
	 * @param whereInNF
	 * @author Rodrigo Freitas
	 */
	public void atualizaExigeAgendamento(String whereInNF) {
		getHibernateTemplate().bulkUpdate("update Notafiscaltipo nt set nt.exigeagenda = ? where nt.cdnotafiscaltipo in (" + whereInNF + ")", 
				new Object[]{Boolean.TRUE});
	}

	/**
	 * Atualiza todos os outros tipos para não exigir agendamento.
	 *
	 * @param whereInNF
	 * @author Rodrigo Freitas
	 */
	public void atualizaNotExigeAgendamento(String whereNotInNF) {
		if(whereNotInNF != null){
			getHibernateTemplate().bulkUpdate("update Notafiscaltipo nt set nt.exigeagenda = ? where nt.cdnotafiscaltipo not in (" + whereNotInNF + ")",
			new Object[]{Boolean.FALSE});
		}else{
			getHibernateTemplate().bulkUpdate("update Notafiscaltipo nt set nt.exigeagenda = ? ",
			new Object[]{Boolean.FALSE});
		}
	}
}