package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.PedidoPontoControle;
import br.com.linkcom.wms.geral.dao.PedidoPontoControleDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class PedidoPontoControleService extends GenericService<PedidoPontoControle> {
	private PedidoPontoControleDAO pedidoPontoControleDAO;
	
	public void setPedidoPontoControleDAO(PedidoPontoControleDAO pedidoPontoControleDAO) {
		this.pedidoPontoControleDAO = pedidoPontoControleDAO;
	}
	
	public List<PedidoPontoControle> recuperaPontoControlePedido(Long numeroPedido) {
		return pedidoPontoControleDAO.recuperaPontoControlePedido(numeroPedido);
	}
	

}
