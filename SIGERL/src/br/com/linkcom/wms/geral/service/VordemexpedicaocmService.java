package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Vordemexpedicaocm;
import br.com.linkcom.wms.geral.dao.VordemexpedicaocmDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class VordemexpedicaocmService extends GenericService<Vordemexpedicaocm>{

	private VordemexpedicaocmDAO vordemexpedicaocmDAO;
	
	public void setVordemexpedicaocmDAO(VordemexpedicaocmDAO vordemexpedicaocmDAO) {
		this.vordemexpedicaocmDAO = vordemexpedicaocmDAO;
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.VordemexpedicaoDAO#findByCarregamento(Carregamento carregamento)
	 * 
	 * @param carregamento
	 * @return
	 */
	public List<Vordemexpedicaocm> findByCarregamento(Carregamento carregamento){
		return vordemexpedicaocmDAO.findByCarregamento(carregamento);
	}
	
	
	/* singleton */
	private static VordemexpedicaocmService instance;
	public static VordemexpedicaocmService getInstance() {
		if(instance == null){
			instance = Neo.getObject(VordemexpedicaocmService.class);
		}
		return instance;
	}
}
