package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotaturnodeentrega;
import br.com.linkcom.wms.geral.dao.RotaturnodeentregaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class RotaturnodeentregaService extends GenericService<Rotaturnodeentrega>{

	private RotaturnodeentregaDAO rotaturnodeentregaDAO;
	
	public void setRotaturnodeentregaDAO(RotaturnodeentregaDAO rotaturnodeentregaDAO) {
		this.rotaturnodeentregaDAO = rotaturnodeentregaDAO;
	}

	public List<Rotaturnodeentrega> findByRota(Rota rota) {
		return rotaturnodeentregaDAO.findByRota(rota);
	}

	public void deleteByRota(Rota rota) {
		rotaturnodeentregaDAO.deleteByRota(rota);
	}
}
