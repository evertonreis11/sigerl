package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Pedidovendatipo;
import br.com.linkcom.wms.geral.dao.PedidovendatipoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class PedidovendatipoService  extends GenericService<Pedidovendatipo> {

	@SuppressWarnings("unused")
	private PedidovendatipoDAO pedidovendatipoDAO;
	
	public void setPedidovendatipoDAO(PedidovendatipoDAO pedidovendatipoDAO) {
		this.pedidovendatipoDAO = pedidovendatipoDAO;
	}

	/* singleton */
	private static PedidovendatipoService instance;
	public static PedidovendatipoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(PedidovendatipoService.class);
		}
		return instance;
	}
}
