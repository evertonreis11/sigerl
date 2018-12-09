package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotaturnoextra;
import br.com.linkcom.wms.geral.dao.RotaturnoextraDAO;
import br.com.linkcom.wms.util.DateUtil;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class RotaturnoextraService extends GenericService<Rotaturnoextra>{

	private RotaturnoextraDAO rotaturnoextraDAO;
	
	public void setRotaturnoextraDAO(RotaturnoextraDAO rotaturnoextraDAO) {
		this.rotaturnoextraDAO = rotaturnoextraDAO;
	}

	/**
	 * 
	 * @param rota
	 * @return
	 */
	public List<Rotaturnoextra> findByRota(Rota rota) {
		List<Rotaturnoextra> lista = rotaturnoextraDAO.findByRota(rota);
		for (Rotaturnoextra rotaturnoextra : lista)
			if(rotaturnoextra.getDtvalidadeinicio().before(DateUtil.limpaMinSegHora(WmsUtil.currentDate())))
				rotaturnoextra.setDtvalidadeinicio(WmsUtil.currentDate());
		return lista;
	}

	/**
	 * 
	 * @param rota
	 */
	public void deleteByRota(Rota rota) {
		rotaturnoextraDAO.deleteByRota(rota);
	}

}
