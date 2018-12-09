package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Agendapedido;
import br.com.linkcom.wms.geral.bean.Agendastatus;
import br.com.linkcom.wms.geral.bean.Pedidocompra;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class AgendapedidoDAO extends GenericDAO<Agendapedido> {
	
	
	/**
	 * Método que busca uma lista de agendapedido através de uma agenda
	 * @author Leonardo Guimarães
	 * @param form
	 * @return
	 */
	public List<Agendapedido> findByAgenda(Agenda agenda, String ... campos) {
		if(agenda == null || agenda.getCdagenda() == null){
			throw new WmsException("A agenda não deve ser nula.");
		}
		QueryBuilder<Agendapedido> query = query();
		if(campos != null && campos.length > 0){
			query.setMaxResults(6);
		}
		return query
					.select(WmsUtil.makeSelectClause("agendapedido.cdagendapedido, agenda.cdagenda," +
							"agendapedido.parcial, pedidocompra.cdpedidocompra, pedidocompra.codigoerp," +
							"pedidocompra.numero, fornecedor.nome, listapedidocompraproduto.cdpedidocompraproduto", campos))
					.join("agendapedido.agenda agenda")
					.join("agendapedido.pedidocompra pedidocompra")
					.join("pedidocompra.fornecedor fornecedor")
					.join("pedidocompra.listapedidocompraproduto listapedidocompraproduto")
					.where("agenda=?",agenda)
	   			 	.list();
	}

	public boolean existeAgendamentoParcial(Pedidocompra pedidocompra) {
		return query()
			.where("agendapedido.pedidocompra = ? ", pedidocompra)
			.where("agendapedido.agenda.agendastatus.id <> ?", Agendastatus.CANCELADO.getCdagendastatus())
			.setMaxResults(1)
			.unique() != null;
	}
	
	public Agendapedido findByPedidocompra(Pedidocompra pedidocompra){
		if(pedidocompra == null || pedidocompra.getCdpedidocompra() == null){
			throw new WmsException("O Pedidocompra não deve ser nulo.");
		}
		return query()
		.select("agendapedido.cdagendapedido, agendapedido.parcial, agenda.cdagenda")
		.join("agendapedido.agenda agenda")
		.where("agendapedido.pedidocompra = ?", pedidocompra)
		.unique();
	}

	public void updateTrocarParaParcial(Pedidocompra pedidocompra, Agenda agenda) {
		if(pedidocompra == null || pedidocompra.getCdpedidocompra() == null || agenda == null || agenda.getCdagenda() == null) 
			throw new WmsException("Pedidocompra ou Agenda não podem ser nulos.");
		
		getHibernateTemplate().bulkUpdate("update agendapedido set parcial = 1 where cdagenda = ? and cdpedidocompra = ?", new Object[]{agenda.getCdagenda(), pedidocompra.getCdpedidocompra()});		
	}
}
