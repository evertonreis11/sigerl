package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Pedidocomprastatus;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class PedidocomprastatusService  extends GenericService<Pedidocomprastatus> {
	
	/* singleton */
	private static PedidocomprastatusService instance;
	public static PedidocomprastatusService getInstance() {
		if(instance == null){
			instance = Neo.getObject(PedidocomprastatusService.class);
		}
		return instance;
	}
}
