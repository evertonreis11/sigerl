package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestocodigobarras;
import br.com.linkcom.wms.geral.bean.Manifestostatus;
import br.com.linkcom.wms.geral.dao.ManifestocodigobarrasDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ManifestocodigobarrasService extends GenericService<Manifestocodigobarras>{

	private static ManifestocodigobarrasService instance;
	private ManifestocodigobarrasDAO manifestocodigobarrasDAO;
	
	public void setManifestocodigobarrasDAO(ManifestocodigobarrasDAO manifestocodigobarrasDAO) {
		this.manifestocodigobarrasDAO = manifestocodigobarrasDAO;
	}

	@Override
	public void saveOrUpdate(Manifestocodigobarras bean) {
		manifestocodigobarrasDAO.desativaCodigoAntigo(bean.getManifesto());
		super.saveOrUpdate(bean);
	}
	
	/**
	 * 
	 * @param numeroManifesto
	 * @param deposito
	 * @return
	 */
	public Manifestocodigobarras findByCodigo(String numeroManifesto,Deposito deposito, Manifestostatus manifestostatus) {
		return manifestocodigobarrasDAO.findByCodigo(numeroManifesto, deposito, manifestostatus);
	}

	/**
	 * 
	 * @return
	 */
	public static ManifestocodigobarrasService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(ManifestocodigobarrasService.class);
		}
		return instance;
	}

	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public String findCodigoByManifesto(Manifesto manifesto) {
		Manifestocodigobarras mcb = manifestocodigobarrasDAO.findCodigoByManifesto(manifesto);
		String codigo = null;
		if(mcb!=null && mcb.getCodigo()!=null && !mcb.getCodigo().isEmpty()){
			codigo = mcb.getCodigo();
		}
		return codigo; 
	}
}
