package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Departamentocodigogerenciadora;
import br.com.linkcom.wms.geral.dao.DepartamentocodigogerenciadoraDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class DepartamentocodigogerenciadoraService extends GenericService<Departamentocodigogerenciadora>{

	private DepartamentocodigogerenciadoraDAO departamentocodigogerenciadoraDAO;
	
	public void setDepartamentocodigogerenciadoraDAO(DepartamentocodigogerenciadoraDAO departamentocodigogerenciadoraDAO) {
		this.departamentocodigogerenciadoraDAO = departamentocodigogerenciadoraDAO;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Departamentocodigogerenciadora> findByListagem(){
		return departamentocodigogerenciadoraDAO.findByListagem();
	}
	
	/**
	 * 
	 * @param dcg
	 */
	public void updateProdutoclasseAndDepartamentogerenciadora(Integer cdprodutoclasse, Integer cddepartamentogerenciadora, Integer cddepto){
		departamentocodigogerenciadoraDAO.updateProdutoclasseAndDepartamentogerenciadora(cdprodutoclasse,cddepartamentogerenciadora,cddepto);
	}
	
}
