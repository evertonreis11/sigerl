package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Logintegracaoae;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class LogintegracaoaeDAO extends GenericDAO<Logintegracaoae>{

	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public List<Logintegracaoae> findByManifesto(Manifesto manifesto) {
		return query()
			.select("logintegracaoae.cdlogintegracaoae, logintegracaoae.cderro, logintegracaoae.dserro, logintegracaoae.cdae, manifesto.cdmanifesto")
			.join("logintegracaoae.manifesto manifesto")
			.where("manifesto = ?",manifesto)
			.list();
	}

}
