package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Filial;
import br.com.linkcom.wms.geral.dao.FilialDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class FilialService extends GenericService<Filial>{
	
	private FilialDAO filialDAO;
	
	public void setFilialDAO(FilialDAO filialDAO) {
		this.filialDAO = filialDAO;
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public Filial findFilialForPraca (String nomeFilial){
		return filialDAO.findFilialForPraca(nomeFilial);
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Filial> findFilial (String param){
		return filialDAO.findFilial(param);
	}

	/**
	 * 
	 * @param filial
	 * @return
	 */
	public Filial findCepByFilial(Filial filial) {
		return filialDAO.findCepByFilial(filial);
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Filial> findForAll(){
		return filialDAO.findForAll();
	}

	/**
	 * 
	 * @param filial
	 * @return
	 */
	public Long getCodigoerp(Filial filial) {
		filial = filialDAO.findCodigoerp(filial);
		return filial.getCodigoerp();
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Filial> findForAutocompleteByEmpresa(String param){
		return filialDAO.findForAutocompleteByEmpresa(param);
	}
}
