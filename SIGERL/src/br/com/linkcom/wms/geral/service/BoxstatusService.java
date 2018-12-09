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
	 * M�todo de refer�ncia da DAO
	 * @author Leonardo Guimar�es
	 * @see br.com.linkcom.wms.geral.dao.BoxstatusDAO.findByBoxStatus(Integer cd)
	 * @param cd
	 * @return
	 */
	public Boxstatus findByBoxStatus(Boxstatus boxstatus) {
		return boxstatusDAO.findByBoxStatus(boxstatus);
	}

}
