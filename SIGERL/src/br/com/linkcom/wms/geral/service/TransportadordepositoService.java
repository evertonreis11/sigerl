package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Transportadordeposito;
import br.com.linkcom.wms.geral.dao.TransportadordepositoDAO;

public class TransportadordepositoService extends GenericService<Transportadordeposito>{

	private TransportadordepositoDAO transportadordepositoDAO;

	public void setTransportadordepositoDAO(TransportadordepositoDAO transportadordepositoDAO) {
		this.transportadordepositoDAO = transportadordepositoDAO;
	}
	
	
}
