package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Enderecostatus;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class EnderecostatusService  extends GenericService<Enderecostatus> {
	
	/* singleton */
	private static EnderecostatusService instance;
	public static EnderecostatusService getInstance() {
		if(instance == null){
			instance = Neo.getObject(EnderecostatusService.class);
		}
		return instance;
	}
}
