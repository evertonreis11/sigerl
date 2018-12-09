package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Tela;
import br.com.linkcom.wms.geral.dao.TelaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;


public class TelaService extends GenericService<Tela> {
	private TelaDAO telaDAO;
	
	public void setTelaDAO(TelaDAO telaDAO) {
		this.telaDAO = telaDAO;
	}
	
	/**
	 * Referência ao método no dao
	 * @param url
	 * @return 
	 * @author Pedro Gonçalves
	 * @see br.com.ideagri.web.geral.dao.TelaDAO#getTelaDescriptionByUrl
	 */
	public String getTelaDescriptionByUrl(String url) {
		return telaDAO.getTelaDescriptionByUrl(url);
	}
	
	/**
	 * Referência ao método no dao
	 * @param url
	 * @return 
	 * @author Pedro Gonçalves
	 * @see br.com.ideagri.web.geral.dao.TelaDAO#clearTelaCache
	 */
	public void clearTelaCache(){
		telaDAO.clearTelaCache();
	}

	/* singleton */
	private static TelaService instance;
	public static TelaService getInstance() {
		if(instance == null){
			instance = Neo.getObject(TelaService.class);
		}
		return instance;
	}
}
