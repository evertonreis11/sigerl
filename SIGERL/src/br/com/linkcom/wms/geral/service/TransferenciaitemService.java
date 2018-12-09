package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Transferenciaitem;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class TransferenciaitemService extends GenericService<Transferenciaitem> {

	private static TransferenciaitemService instance;

	public static TransferenciaitemService getInstance() {
		if (instance == null)
			instance = Neo.getObject(TransferenciaitemService.class);
		
		return instance;
	}
	
}
