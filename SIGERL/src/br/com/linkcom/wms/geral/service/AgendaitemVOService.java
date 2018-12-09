package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.vo.AgendaitemVO;
import br.com.linkcom.wms.geral.dao.AgendaitemVODAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class AgendaitemVOService extends GenericService<AgendaitemVO>{

	AgendaitemVODAO agendaitemVODAO;	
	public void setAgendaitemVODAO(AgendaitemVODAO agendaitemVODAO) {this.agendaitemVODAO = agendaitemVODAO;}

	public List<AgendaitemVO> findByAgenda(String listaAgenda){
		return agendaitemVODAO.findByAgenda(listaAgenda);
	}
}
