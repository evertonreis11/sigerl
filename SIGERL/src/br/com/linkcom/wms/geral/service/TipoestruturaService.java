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
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
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
