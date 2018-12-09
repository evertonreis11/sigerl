package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;
import br.com.linkcom.wms.geral.dao.RecebimentostatusDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;


public class RecebimentostatusService extends GenericService<Recebimentostatus> {

	@SuppressWarnings("unused")
	private RecebimentostatusDAO recebimentostatusDAO;
	
	public void setRecebimentostatusDAO(RecebimentostatusDAO recebimentostatusDAO) {
		this.recebimentostatusDAO = recebimentostatusDAO;
	}

	/* singleton */
	private static RecebimentostatusService instance;
	public static RecebimentostatusService getInstance() {
		if(instance == null){
			instance = Neo.getObject(RecebimentostatusService.class);
		}
		return instance;
	}
}
