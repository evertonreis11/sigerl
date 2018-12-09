package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.dao.OrdemtipoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;


public class OrdemtipoService extends GenericService<Ordemtipo> {

	@SuppressWarnings("unused")
	private OrdemtipoDAO ordemtipoDAO;
	
	public void setOrdemtipoDAO(OrdemtipoDAO ordemtipoDAO) {
		this.ordemtipoDAO = ordemtipoDAO;
	}
	
	/* singleton */
	private static OrdemtipoService instance;
	public static OrdemtipoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(OrdemtipoService.class);
		}
		return instance;
	}
}
