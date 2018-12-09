package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Vordemexpedicaoboxcm;
import br.com.linkcom.wms.geral.dao.VordemexpedicaoboxcmDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class VordemexpedicaoboxcmService extends GenericService<Vordemexpedicaoboxcm>{

	private static VordemexpedicaoboxcmService instance;
	private VordemexpedicaoboxcmDAO vordemexpedicaoboxcmDAO;
	
	public void setVordemexpedicaoboxcmDAO(VordemexpedicaoboxcmDAO vordemexpedicaoboxcmDAO) {
		this.vordemexpedicaoboxcmDAO = vordemexpedicaoboxcmDAO;
	}

	public static VordemexpedicaoboxcmService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(VordemexpedicaoboxcmService.class);
		}
		return instance;
	}

	
	public List<Vordemexpedicaoboxcm> findByBox(Box box) {
		return vordemexpedicaoboxcmDAO.findByBox(box);
	}	
}
