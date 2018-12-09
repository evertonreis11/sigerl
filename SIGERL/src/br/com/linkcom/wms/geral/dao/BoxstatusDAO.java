package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.wms.geral.bean.Boxstatus;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("boxstatus.nome")
public class BoxstatusDAO extends GenericDAO<Boxstatus> {
	
	/**
	 * Busca um boxstatus através do boxstatus fornecido
	 * @author Leonardo Guimarães
	 * @param boxstatus
	 * @return
	 */
	public Boxstatus findByBoxStatus(Boxstatus boxstatus) {
		if(boxstatus == null || boxstatus.getCdboxstatus() == null){
			throw new WmsException("O box não deve ser nulo");
		}
		return query()
					 .select("boxstatus.cdboxstatus, boxstatus.nome, boxstatus.bloqueado")
					 .entity(boxstatus)
					 .unique();
	}

}
