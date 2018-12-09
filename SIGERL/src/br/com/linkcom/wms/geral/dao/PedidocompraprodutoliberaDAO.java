package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Pedidocompra;
import br.com.linkcom.wms.geral.bean.Pedidocompraprodutolibera;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class PedidocompraprodutoliberaDAO extends GenericDAO<Pedidocompraprodutolibera> {
	
	/**
	 * Carrega pedidodecompraprodutoliberada conforme pedidocompra informado como parâmetro
	 * @param pedidocompra
	 * @return
	 * @author Taidson
	 */
	public List<Pedidocompraprodutolibera> findPedidocompraliberado(Pedidocompra pedidocompra) {
		if(pedidocompra == null || pedidocompra.getCdpedidocompra() == null){
			throw new WmsException("O pedidocompra não deve ser nulo");
		}
		return query()
					.select("pedidocompraprodutolibera.cdpedidocompraprodutolibera, pedidocompraprodutolibera.qtdeliberada, " +
							"pedidocompraprodutolibera.dtprevisaorecebimento, pedidocompraprodutolibera.dtprevisaofinanceiro, pedidocompraprodutolibera.qtdeliberada, pedidocompraproduto.cdpedidocompraproduto, pedidocompraproduto.qtde, " +
							"pedidocompra.cdpedidocompra, pedidocompra.numero, produto.cdproduto, produto.codigo, produto.descricao")
		  			.join("pedidocompraprodutolibera.pedidocompraproduto pedidocompraproduto")
		  			.join("pedidocompraproduto.pedidocompra pedidocompra")
		  			.join("pedidocompraproduto.produto produto")
					.where("pedidocompra = ? ", pedidocompra)
					.orderBy("pedidocompraprodutolibera.dtprevisaorecebimento desc")
					.list();
	}
	
	/**
	 * Carrega os dados Pedidocompraprodutolibera
	 * @param pedidocompraprodutolibera
	 * @return
	 * @author Taidson
	 */
	public Pedidocompraprodutolibera findPedidocompraliberado(Pedidocompraprodutolibera pedidocompraprodutolibera) {
		if(pedidocompraprodutolibera == null || pedidocompraprodutolibera.getCdpedidocompraprodutolibera() == null){
			throw new WmsException("O pedidocompra não deve ser nulo");
		}
		return query()
		.select("pedidocompraprodutolibera.cdpedidocompraprodutolibera, pedidocompraprodutolibera.qtdeliberada, " +
				"pedidocompraprodutolibera.dtprevisaorecebimento")
		.where("pedidocompraprodutolibera = ? ", pedidocompraprodutolibera)
		.unique();
	}
	
	/**
	 * Excluir pedidocompraprodutolibera 
	 * conforme exclusão realizada na tela de edição de pedidos liberados.
	 * @param pedidocompraprodutolibera
	 * @author Taidson
	 */
	public void excluirPedidocompraprodutolibera(Pedidocompraprodutolibera pedidocompraprodutolibera){
		if(pedidocompraprodutolibera == null || pedidocompraprodutolibera.getCdpedidocompraprodutolibera() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		getHibernateTemplate().bulkUpdate("delete Pedidocompraprodutolibera pcpl where pcpl.id=?",new Object[]{pedidocompraprodutolibera.getCdpedidocompraprodutolibera()});
	}
	


	
}
