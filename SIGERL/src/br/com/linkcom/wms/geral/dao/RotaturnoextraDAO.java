package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotaturnoextra;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class RotaturnoextraDAO extends GenericDAO<Rotaturnoextra>{

	/**
	 * 
	 * @param rota
	 * @return
	 */
	public List<Rotaturnoextra> findByRota(Rota rota) {
		return query()			
			.where("rotaturnoextra.rota = ?",rota)
			.where("rotaturnoextra.dtvalidadefim > ?",WmsUtil.currentDate())
			.list();
	}

	/**
	 * 
	 * @param rota
	 */
	public void deleteByRota(Rota rota) {
		if(rota == null || rota.getCdrota() == null) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("delete from Rotaturnoextra rte where rte.rota.id = ?", 
				new Object[]{rota.getCdrota()});
	}

}
