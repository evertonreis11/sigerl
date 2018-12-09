package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Agendajanela;
import br.com.linkcom.wms.geral.bean.Agendajanelaclasse;
import br.com.linkcom.wms.geral.dao.AgendajanelaclasseDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class AgendajanelaclasseService extends GenericService<Agendajanelaclasse>{

	private AgendajanelaclasseDAO agendajanelaclasseDAO;
	
	private static AgendajanelaclasseService instance;

	public void setAgendajanelaclasseDAO(AgendajanelaclasseDAO agendajanelaclasseDAO) {this.agendajanelaclasseDAO = agendajanelaclasseDAO;}
	
	public static AgendajanelaclasseService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(AgendajanelaclasseService.class);
		}
		return instance;
	}

	public List<Agendajanelaclasse> findByAgendajanela(Agendajanela agendajanela){
		return agendajanelaclasseDAO.findByAgendajanela(agendajanela);
	}
}
