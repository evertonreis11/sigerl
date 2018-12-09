package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotaturnodeentrega;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class RotaturnodeentregaDAO extends GenericDAO<Rotaturnodeentrega>{

	public List<Rotaturnodeentrega> findByRota(Rota rota) {
		return query()			
			.where("rotaturnodeentrega.rota = ?",rota)
			.list();
	}

	public Object deleteByRota(Rota rota) {
		
		if(rota == null || rota.getCdrota() == null) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("delete from Rotaturnodeentrega rtde where rtde.rota.id = ?", 
				new Object[]{rota.getCdrota()});

		return null;
	}
}
