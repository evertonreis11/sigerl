package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Permissao;
import br.com.linkcom.wms.geral.dao.PermissaoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class PermissaoService extends GenericService<Permissao> {
	
	protected PermissaoDAO permissaoDAO;

	public void setPermissaoDAO(PermissaoDAO permissaoDAO) {
		this.permissaoDAO = permissaoDAO;
	}
	
	/* singleton */
	private static PermissaoService instance;
	public static PermissaoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(PermissaoService.class);
		}
		return instance;
	}
}
