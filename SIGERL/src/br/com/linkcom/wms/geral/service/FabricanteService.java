package br.com.linkcom.wms.geral.service;

import br.com.linkcom.wms.geral.bean.Fabricante;
import br.com.linkcom.wms.geral.dao.FabricanteDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class FabricanteService extends GenericService<Fabricante> {

	private FabricanteDAO fabricanteDAO;
	
	public void setFabricanteDAO(FabricanteDAO fabricanteDAO) {
		this.fabricanteDAO = fabricanteDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * @param cd
	 * @return
	 */
	public Fabricante findByCd(Integer cd){
		return fabricanteDAO.findByCd(cd); 
	}
	
}
