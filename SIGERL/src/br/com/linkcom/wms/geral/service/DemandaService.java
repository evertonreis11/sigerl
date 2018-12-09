package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Demanda;
import br.com.linkcom.wms.geral.dao.DemandaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class DemandaService extends GenericService<Demanda>{

	private DemandaDAO demandaDAO;
	
	public void setDemandaDAO(DemandaDAO demandaDAO) {
		this.demandaDAO = demandaDAO;
	}

	public List<Demanda> findLastVersion(){
		return demandaDAO.findLastVersion();
	}
	
}
