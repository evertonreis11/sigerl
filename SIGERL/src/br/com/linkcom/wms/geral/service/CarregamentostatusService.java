package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class CarregamentostatusService extends GenericService<Carregamentostatus>{

	/* singleton */
	private static CarregamentostatusService instance;
	public static CarregamentostatusService getInstance() {
		if(instance == null){
			instance = Neo.getObject(CarregamentostatusService.class);
		}
		return instance;
	}

}
