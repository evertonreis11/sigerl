package br.com.linkcom.wms.geral.service;

import br.com.linkcom.wms.geral.bean.Boxstatus;
import br.com.linkcom.wms.geral.dao.BoxstatusDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class BoxstatusService extends GenericService<Boxstatus> {
	
	private BoxstatusDAO boxstatusDAO;
	
	public void setBoxstatusDAO(BoxstatusDAO boxstatusDAO) {
		this.boxstatusDAO = boxstatusDAO;
	}
	
	/**
	 * Método de referência da DAO
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.dao.BoxstatusDAO.findByBoxStatus(Integer cd)
	 * @param cd
	 * @return
	 */
	public Boxstatus findByBoxStatus(Boxstatus boxstatus) {
		return boxstatusDAO.findByBoxStatus(boxstatus);
	}

}
