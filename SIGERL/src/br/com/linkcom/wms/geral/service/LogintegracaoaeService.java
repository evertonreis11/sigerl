package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Logintegracaoae;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.dao.LogintegracaoaeDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class LogintegracaoaeService extends GenericService<Logintegracaoae> {

	private LogintegracaoaeDAO logintegracaoaeDAO;
	
	public void setLogintegracaoaeDAO(LogintegracaoaeDAO logintegracaoaeDAO) {
		this.logintegracaoaeDAO = logintegracaoaeDAO;
	}

	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public List<Logintegracaoae> findByManifesto(Manifesto manifesto) {
		return logintegracaoaeDAO.findByManifesto(manifesto);
	}
	
	/**
	 * 
	 * @param cdae
	 * @param manifesto
	 * @param menssagem
	 * @param cderro 
	 */
	public void criarLogIntegracaoOpenTech(Integer cdae, Manifesto manifesto, String dserro, Integer cderro) {
		
		Logintegracaoae log = new Logintegracaoae(cdae,manifesto,dserro,cderro);
		saveOrUpdate(log);
		
	}

}
