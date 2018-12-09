package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Controleestoquestatus;

public class ControleestoquestatusService extends GenericService<Controleestoquestatus> {

	private static ControleestoquestatusService instance;
	public static ControleestoquestatusService getInstance() {
		if(instance == null){
			instance = Neo.getObject(ControleestoquestatusService.class);
		}
		return instance;
	}
	
}
