package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Agendaparcial;
import br.com.linkcom.wms.geral.bean.Agendapedido;
import br.com.linkcom.wms.geral.bean.Agendastatus;
import br.com.linkcom.wms.geral.bean.Pedidocompra;
import br.com.linkcom.wms.geral.bean.Pedidocompraproduto;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class AgendaparcialDAO extends GenericDAO<Agendaparcial> {
	
	/**
	 * Método que busca uma lista de Agendaparcial através de um pedidocompraproduto
	 * @author Leonardo Guimarães
	 * @param pedidocompraproduto
	 * @return
	 */
	public List<Agendaparcial> findByPedidoCompraProduto(Pedidocompraproduto pedidocompraproduto) {
		if(pedidocompraproduto == null || pedidocompraproduto.getCdpedidocompraproduto() == null){
			throw new WmsException("O pedidocompraproduto não deve ser nulo.");
		}
		return query()
					  .select("agendaparcial.cdagendaparcial, agenda.cdagenda, " +
					  		"pedidocompraproduto.cdpedidocompraproduto, agendaparcial.qtde")
					  .join("agendaparcial.agenda agenda")
					  .join("agendaparcial.pedidocompraproduto pedidocompraproduto")
					  .where("pedidocompraproduto = ?",pedidocompraproduto)
					  .where("agenda.agendastatus <> ?", Agendastatus.CANCELADO)
					  .list();
		}
	
	/**
	 * Método que busca uma lista de Agendaparcial através de um agendapedido
	 * @author Leonardo Guimarães
	 * @param agendapedido
	 * @return
	 */
	public List<Agendaparcial> findByAgendaPedido(Agendapedido agendapedido) {
		try{
			if(agendapedido.getAgenda().getCdagenda() == null || agendapedido.getPedidocompra().getCdpedidocompra() == null)
				throw new WmsException("O agendapedido não deve ser nulo.");
		}catch (Exception e) {
			throw new WmsException("O agendapedido não deve ser nulo.");
		}
		return query()
					 .select("agendaparcial.cdagendaparcial,agenda.cdagenda,pedidocompraproduto.cdpedidocompraproduto," +
					 		"pedidocompra.cdpedidocompra,agendaparcial.qtde," +
					 		"listaPedidocompraprodutolibera.cdpedidocompraprodutolibera, listaPedidocompraprodutolibera.qtdeliberada," +
					 		"listaPedidocompraprodutolibera.dtprevisaorecebimento")
					 .join("agendaparcial.agenda agenda")
					 .leftOuterJoin("agendaparcial.pedidocompraproduto pedidocompraproduto")
					 .join("pedidocompraproduto.pedidocompra pedidocompra")
					 .join("pedidocompraproduto.produto produto")
					 .leftOuterJoin("pedidocompraproduto.listaPedidocompraprodutolibera listaPedidocompraprodutolibera")
					 .where("agenda=?",agendapedido.getAgenda())
					 .where("pedidocompra=?",agendapedido.getPedidocompra())
					 .list();
	}
	
	/**
	 * Busca todos os agendamentos parciais de uma agenda
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param agenda
	 * @return
	 */
	public List<Agendaparcial> findByAgendaPedido(Agenda agenda) {
		if(agenda == null || agenda.getCdagenda() == null){
			throw new WmsException("A agenda não deve ser nula.");
		}
		return query()
					.select("agendaparcial.cdagendaparcial,agendaparcial.qtde,pedidocompraproduto.cdpedidocompraproduto")
					.join("agendaparcial.agenda agenda")
					.join("agendaparcial.pedidocompraproduto pedidocompraproduto")
					.where("agenda = ?",agenda)
					.list()
					;
	}

	/**
	 * Executa a função BUSCAR_QTDEAGENDA_PRODUTO direto no banco de dados.
	 *
	 * @param pcp
	 * @return
	 * @author Rodrigo Freitas
	 */
	public Integer getQtdeAgendada(Pedidocompraproduto pcp) {
		return getJdbcTemplate().queryForInt("SELECT BUSCAR_QTDEAGENDA_PRODUTO(?) FROM DUAL", 
				new Integer[]{pcp.getCdpedidocompraproduto()});
	}

	/**
	 * Método que deleta agenda parcial a partir do nº da agenda e do pedido compra produto 
	 * 
	 * @param agenda
	 * @param pedidocompraproduto
	 * @author Tomás Rabelo
	 */
	public void deleteAgendaParcial(Agenda agenda, Pedidocompraproduto pedidocompraproduto) {
		if(agenda == null || agenda.getCdagenda() == null || pedidocompraproduto == null || pedidocompraproduto.getCdpedidocompraproduto() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("delete from Agendaparcial ap where ap.agenda = ? and ap.pedidocompraproduto = ?", new Object[]{agenda, pedidocompraproduto});
	}
	
	public void insertTrocarParaParcial(Pedidocompra pedidocompra, Agenda agenda, Integer qtde) {
		if(pedidocompra == null || pedidocompra.getCdpedidocompra() == null || agenda == null || agenda.getCdagenda() == null) 
			throw new WmsException("Pedidocompra ou Agenda não podem ser nulos.");
		
		getHibernateTemplate().bulkUpdate("insert into agendaparcial (cdagendaparcial, cdagenda, cdpedidocompraproduto, qtde)" +
					"values (sq_agendaparcial.nextval, ?, ?, ?)", new Object[]{agenda.getCdagenda(), pedidocompra.getCdpedidocompra(), qtde});		
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<Agendaparcial> findItensReduzirQtdeAgendada(Pedidocompraproduto pedidocompraproduto) {
		if(pedidocompraproduto == null || pedidocompraproduto.getCdpedidocompraproduto() == null) 
			throw new WmsException("Pedidocompraproduto não pode ser nulo.");
		
		StringBuilder sql = new StringBuilder();

		sql.append("select ap.cdagendaparcial, ap.qtde, ap.cdpedidocompraproduto, a.dtagenda ");
		sql.append("from agendaparcial ap ");
		sql.append("inner join agenda a on a.cdagenda = ap.cdagenda ");
		sql.append("where ap.cdpedidocompraproduto = ? ");
		sql.append("order by a.dtagenda desc");

		
		return (List<Agendaparcial>) getJdbcTemplate().query(sql.toString(), new Object[]{pedidocompraproduto.getCdpedidocompraproduto()},
				new ResultSetExtractor(){
					public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<Agendaparcial> list = new ArrayList<Agendaparcial>();
						
						while (rs.next()){
							Agendaparcial agendaparcial = new Agendaparcial();
							agendaparcial.setCdagendaparcial(rs.getInt("ap.cdagendaparcial"));
							agendaparcial.setQtde(rs.getInt("ap.qtde"));
							agendaparcial.getPedidocompraproduto().setCdpedidocompraproduto(rs.getInt("ap.cdpedidocompraproduto"));
							agendaparcial.getAgenda().setDtagenda(rs.getDate("a.dtagenda"));
							list.add(agendaparcial);
							
						}
						return list;
					}
				}
			);
		
			
	}
	 
	public void updateAgendaParcial(Agendaparcial agendaparcial) {
		if(agendaparcial == null || agendaparcial.getCdagendaparcial() == null) 
			throw new WmsException("A Agendaparcial não pode ser nula.");
		
		getHibernateTemplate().bulkUpdate("update agendaparcial set qtde = ? where cdagendaparcial = ?", new Object[]{agendaparcial.getCdagendaparcial()});		
	}
}
