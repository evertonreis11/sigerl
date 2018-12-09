package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Usuariopapel;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class UsuariopapelService extends GenericService<Usuariopapel> {

	

	/* singleton */
	private static UsuariopapelService instance;
	public static UsuariopapelService getInstance() {
		if(instance == null){
			instance = Neo.getObject(UsuariopapelService.class);
		}
		return instance;
	}
	
}
