package br.com.linkcom.wms.geral.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.wms.geral.bean.vo.AgendaitemVO;

public class AgendaitemVODAO extends GenericDAO<AgendaitemVO>{

	@SuppressWarnings("unchecked")
	public List<AgendaitemVO> findByAgenda(String listaAgenda){		
		String sentenca = " select sum(qtde) as qtde, cdproduto from "+
							  " (select sum(apc.qtde) as qtde, p.cdproduto "+
							  " from agenda a "+
							  " join agendapedido ap on a.cdagenda = ap.cdagenda "+
							  " join agendaparcial apc on a.cdagenda = apc.cdagenda "+
							  " join pedidocompraproduto pcp on pcp.cdpedidocompra = ap.cdpedidocompra and apc.cdpedidocompraproduto = pcp.cdpedidocompraproduto "+
							  " join produto p on p.cdproduto = pcp.cdproduto "+
							  " where ap.parcial = 1 " +
							  "	and a.cdagenda in ("+listaAgenda+") " +
							  " Group By P.Cdproduto "+
						  " union all "+
						  	  " select sum(pcp.qtde) as qtde, p.cdproduto "+
						  	  " from agenda a "+
						  	  " join agendapedido ap on a.cdagenda = ap.cdagenda "+
						  	  " join pedidocompra pc on ap.cdpedidocompra = pc.cdpedidocompra "+
						  	  " join pedidocompraproduto pcp on pc.cdpedidocompra = pcp.cdpedidocompra "+
						  	  " join produto p on p.cdproduto = pcp.cdproduto "+
						  	  " where ap.parcial = 0" +
						  	  "	and a.cdagenda in ("+listaAgenda+") " +
						  	  " Group By P.Cdproduto) "+
						  " Group By cdproduto "; 
		
		List<AgendaitemVO> lista = getJdbcTemplate().query(sentenca,new RowMapper(){		
			public Object mapRow(java.sql.ResultSet rs, int currentItem) throws SQLException {
				AgendaitemVO agendaitemVO = new AgendaitemVO();
				agendaitemVO.setQtde(rs.getLong("qtde"));
				agendaitemVO.setCdproduto(rs.getInt("cdproduto"));				
				return agendaitemVO;
			}
		});	
		
		return lista;
	}
}