package br.com.linkcom.wms.geral.service;

import br.com.linkcom.wms.geral.bean.Tipoestrutura;
import br.com.linkcom.wms.geral.dao.TipoestruturaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class TipoestruturaService extends GenericService<Tipoestrutura> {

	private TipoestruturaDAO tipoestruturaDAO;
	
	public void setTipoestrutura(TipoestruturaDAO tipoestruturaDAO) {
		this.tipoestruturaDAO = tipoestruturaDAO;
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.TipoestruturaDAO#utilizaPicking(Tipoestrutura tipoestrutura)
	 * 
	 * @param tipoestrutura
	 * @return
	 */
	public Boolean utilizaPicking(Tipoestrutura tipoestrutura){
		return tipoestruturaDAO.utilizaPicking(tipoestrutura);
	}
}
