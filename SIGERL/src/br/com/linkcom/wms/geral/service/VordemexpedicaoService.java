package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Vordemexpedicao;
import br.com.linkcom.wms.geral.dao.VordemexpedicaoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class VordemexpedicaoService extends GenericService<Vordemexpedicao>{
	
	private VordemexpedicaoDAO vordemexpedicaoDAO;
	
	public void setVordemexpedicaoDAO(VordemexpedicaoDAO vordemexpedicaoDAO) {
		this.vordemexpedicaoDAO = vordemexpedicaoDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.VordemexpedicaoDAO#findByCarregamento(Carregamento carregamento)
	 * 
	 * @param carregamento
	 * @return
	 */
	public List<Vordemexpedicao> findByCarregamento(Carregamento carregamento){
		return vordemexpedicaoDAO.findByCarregamento(carregamento);
	}
	
	/* singleton */
	private static VordemexpedicaoService instance;
	public static VordemexpedicaoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(VordemexpedicaoService.class);
		}
		return instance;
	}
	
}
