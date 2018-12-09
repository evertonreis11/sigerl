package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotacorte;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class RotacorteDAO extends GenericDAO<Rotacorte>{

	/**
	 * 
	 * @param rota
	 */
	public Rotacorte findByRota(Rota rota){
		
		if(rota==null || rota.getCdrota()==null)
			throw new WmsException("Parâmetros inválidos");

		return query()
			.openParentheses()
				.where("rotacorte.cdrota = ?", rota.getCdrota())
				.or()
				.where("rotacorte.cdrota is null")
			.closeParentheses()
			.where("trunc(rotacorte.dt_inclusao) = trunc(sysdate)")
			.where("rotacorte.cddeposito = ?", WmsUtil.getDeposito().getCodigoerp())
			.unique();
	}

	/**
	 * 
	 * @return
	 */
	public Rotacorte corteDiarioRealizado() {
		
		return query()
			.where("rotacorte.cdrota = null")
			.where("rotacorte.cddeposito = ? ",WmsUtil.getDeposito().getCddeposito().longValue())
			.where("trim(rotacorte.dt_inclusao) = trim(sysdate)")
			.setMaxResults(1)
			.unique();
	}

}
