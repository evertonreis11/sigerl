package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Pedidocompra;
import br.com.linkcom.wms.geral.bean.Pedidocompraprodutolibera;
import br.com.linkcom.wms.geral.dao.PedidocompraprodutoliberaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class PedidocompraprodutoliberaService extends GenericService<Pedidocompraprodutolibera> {
	private PedidocompraprodutoliberaDAO pedidocompraprodutoliberaDAO;
	
	public void setPedidocompraprodutoliberaDAO(PedidocompraprodutoliberaDAO pedidocompraprodutoliberaDAO) {
		this.pedidocompraprodutoliberaDAO = pedidocompraprodutoliberaDAO;
	}
	
	/**
	 * Faz referência ao DAO.
	 * @param pedidocompra
	 * @return
	 */
	public List<Pedidocompraprodutolibera> findPedidocompraliberado(Pedidocompra pedidocompra) {
		return pedidocompraprodutoliberaDAO.findPedidocompraliberado(pedidocompra);
	}
	
	/**
	 * Faz referência ao DAO.
	 * @param pedidocompra
	 * @return
	 */
	public Pedidocompraprodutolibera findPedidocompraliberado(Pedidocompraprodutolibera pedidocompraprodutolibera) {
		return pedidocompraprodutoliberaDAO.findPedidocompraliberado(pedidocompraprodutolibera);
	}
	
	/**
	 * Faz referência ao DAO.
	 * @param pedidocompra
	 * @return
	 */
	public void excluirPedidocompraprodutolibera(Pedidocompraprodutolibera pedidocompraprodutolibera){
		pedidocompraprodutoliberaDAO.excluirPedidocompraprodutolibera(pedidocompraprodutolibera);
	}
}
