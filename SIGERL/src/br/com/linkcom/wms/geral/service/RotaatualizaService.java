package br.com.linkcom.wms.geral.service;

import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotaatualiza;
import br.com.linkcom.wms.geral.dao.RotaatualizaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class RotaatualizaService extends GenericService<Rotaatualiza>{

	private RotaatualizaDAO rotaatualizaDAO;

	public void setRotaatualizaDAO(RotaatualizaDAO rotaatualizaDAO) {
		this.rotaatualizaDAO = rotaatualizaDAO;
	}

	/**
	 * 
	 * @param rota
	 * @return
	 */
	public Rotaatualiza findByRota(Rota rota){
		return rotaatualizaDAO.findByRota(rota);
	}
}
