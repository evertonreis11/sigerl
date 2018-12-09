package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotaatualiza;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class RotaatualizaDAO extends GenericDAO<Rotaatualiza>{
	
	/**
	 * 
	 * @param rota
	 * @return
	 */
	public Rotaatualiza findByRota(Rota rota){
		return query()
			.where("rotaatualiza.rota = ?", rota)
			.where("rotaatualiza.flag_status <> 'C'")
			.unique();
	}
	
}
