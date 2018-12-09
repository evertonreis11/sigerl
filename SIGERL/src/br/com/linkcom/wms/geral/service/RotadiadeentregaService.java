package br.com.linkcom.wms.geral.service;

import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotadiadeentrega;
import br.com.linkcom.wms.geral.dao.RotadiadeentregaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class RotadiadeentregaService extends GenericService<Rotadiadeentrega>{

	private RotadiadeentregaDAO rotadiadeentregaDAO;
	
	public void setRotadiadeentregaDAO(RotadiadeentregaDAO rotadiadeentregaDAO) {
		this.rotadiadeentregaDAO = rotadiadeentregaDAO;
	}

	/**
	 * 
	 * @param rota
	 * @return
	 */
	public Rotadiadeentrega findUltimaData(Rota rota) {
		return rotadiadeentregaDAO.findUltimaData(rota);
	}

	/**
	 * 
	 * @param rota
	 */
	public void deleteByRota(Rota rota) {
		rotadiadeentregaDAO.deleteByRota(rota);
	}

}
