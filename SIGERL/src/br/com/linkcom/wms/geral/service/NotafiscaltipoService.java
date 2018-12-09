package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Notafiscaltipo;
import br.com.linkcom.wms.geral.dao.NotafiscaltipoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;


public class NotafiscaltipoService extends GenericService<Notafiscaltipo> {

	private NotafiscaltipoDAO notafiscaltipoDAO;
	
	public void setNotafiscaltipoDAO(NotafiscaltipoDAO notafiscaltipoDAO) {
		this.notafiscaltipoDAO = notafiscaltipoDAO;
	}

	/* singleton */
	private static NotafiscaltipoService instance;
	public static NotafiscaltipoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(NotafiscaltipoService.class);
		}
		return instance;
	}
	
	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscaltipoDAO#findExigeAgendamento
	 *
	 * @return
	 * @author Rodrigo Freitas
	 */
	public List<Notafiscaltipo> findExigeAgendamento() {
		return notafiscaltipoDAO.findExigeAgendamento();
	}

	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscaltipoDAO#atualizaExigeAgendamento
	 *
	 * @param whereInNF
	 * @author Rodrigo Freitas
	 */
	public void atualizaExigeAgendamento(String whereInNF) {
		notafiscaltipoDAO.atualizaExigeAgendamento(whereInNF);
	}

	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscaltipoDAO#atualizaNotExigeAgendamento
	 * 
	 * @param whereNotInNF
	 * @author Rodrigo Freitas
	 */
	public void atualizaNotExigeAgendamento(String whereNotInNF) {
		notafiscaltipoDAO.atualizaNotExigeAgendamento(whereNotInNF);
	}
}
