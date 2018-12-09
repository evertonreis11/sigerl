package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Agendajanela;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro.AgendajanelaFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class AgendajanelaDAO extends GenericDAO<Agendajanela> {

	@Override
	public void updateEntradaQuery(QueryBuilder<Agendajanela> query) {
		super.updateEntradaQuery(query);
		query.joinFetch("agendajanela.listaAgendajanelaclasse agendajanelaclasse");
		query.joinFetch("agendajanelaclasse.produtoclasse produtoclasse");
	}
	
	@Override
	public void updateListagemQuery(QueryBuilder<Agendajanela> query, FiltroListagem _filtro) {
		super.updateListagemQuery(query, _filtro);

		AgendajanelaFiltro agendajanelaFiltro = ((AgendajanelaFiltro) _filtro);
		
		query.joinFetch("agendajanela.listaAgendajanelaclasse agendajanelaclasse");
		query.joinFetch("agendajanelaclasse.produtoclasse produtoclasse");
		query.joinFetch("agendajanela.deposito deposito");
		query.where("deposito = ?", agendajanelaFiltro.getDeposito());
		
		if (!StringUtils.isEmpty(_filtro.getOrderBy()))
			_filtro.setOrderBy(_filtro.getOrderBy() + ", produtoclasse.nome");
		else
			_filtro.setOrderBy("deposito.nome, produtoclasse.nome");
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		super.updateSaveOrUpdate(save);
		save.saveOrUpdateManaged("listaAgendajanelaclasse");
	}
	
	/**
	 * Verifica se existe uma janela de agendamento que atenda a todas as classes listadas.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @param listaProdutoclasse
	 * @return
	 */
	public boolean existeJanela(Deposito deposito, List<String> listaProdutoclasse) {
		String paramLista = WmsUtil.joinWithScape(listaProdutoclasse.iterator(), ", ", "'");
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM agendajanela a ");
		sql.append("WHERE a.cddeposito = ? ");
		sql.append("	AND NOT EXISTS ( ");
		sql.append("	    SELECT pc.cdprodutoclasse  ");
		sql.append("	    FROM produtoclasse pc  ");
		sql.append("	    WHERE pc.numero IN (" + paramLista + ") ");
		sql.append("	    MINUS ");
		sql.append("	    SELECT ajc.cdprodutoclasse ");
		sql.append("	    FROM agendajanelaclasse ajc  ");
		sql.append("	      join produtoclasse pc ON pc.cdprodutoclasse = ajc.cdprodutoclasse ");
		sql.append("	    WHERE ajc.cdagendajanela = a.cdagendajanela ");
		sql.append("	    	AND EXISTS (SELECT pc.cdprodutoclasse FROM produtoclasse pc WHERE pc.numero IN (" + paramLista + ")) ");
		sql.append("	) ");
		
		List<?> list = getJdbcTemplate().queryForList(sql.toString(), new Object[]{deposito.getCddeposito()});
		
		return list != null && !list.isEmpty();
	}
	
	/**
	 * Lista o número de janelas ocupadas para cada classe de produto principal
	 * em um determinado dia.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @param data
	 * @param agenda 
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public List<Agendajanela> getAgendasPorClasse(Deposito deposito, Date data, Agenda agenda) {
		if (deposito == null || deposito.getCddeposito() == null || data == null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT AJ.CDAGENDAJANELA as janela, AJ.OCORRENCIAS AS ocorrencias, D.NOME as deposito, ");
		sql.append("  (SELECT COUNT(DISTINCT A.CDAGENDA) ");
		sql.append("   FROM AGENDA A JOIN AGENDAPEDIDO AP ON (AP.CDAGENDA = A.CDAGENDA) ");
		sql.append("   JOIN PEDIDOCOMPRAPRODUTO PCP ON (PCP.CDPEDIDOCOMPRA = AP.CDPEDIDOCOMPRA) ");
		sql.append("   LEFT JOIN AGENDAPARCIAL APARC ON (APARC.CDAGENDA = A.CDAGENDA AND PCP.CDPEDIDOCOMPRAPRODUTO = APARC.CDPEDIDOCOMPRAPRODUTO) "); 
		sql.append("   JOIN PRODUTO P ON (P.CDPRODUTO = PCP.CDPRODUTO) ");
		sql.append("   JOIN V_PRODUTOCLASSE VPC ON (VPC.CDPRODUTOCLASSE = P.CDPRODUTOCLASSE) ");
		sql.append("   JOIN AGENDAJANELACLASSE AJC ON (AJC.CDPRODUTOCLASSE = VPC.CDCLASSE) ");
		sql.append("   WHERE AJC.CDAGENDAJANELA  = AJ.CDAGENDAJANELA ");
		sql.append("   AND A.CDDEPOSITO = AJ.CDDEPOSITO ");
		sql.append("   AND TRUNC(A.DTAGENDA) = ? ");
		if(agenda != null && agenda.getCdagenda() != null)
				sql.append(" AND a.cdagenda <> "+agenda.getCdagenda());
		sql.append("   AND A.CDAGENDASTATUS <> 3) AS agendas ");
		sql.append(" FROM AGENDAJANELA AJ JOIN DEPOSITO D ON (AJ.CDDEPOSITO = D.CDDEPOSITO) ");
		sql.append(" WHERE AJ.CDDEPOSITO = ? ");

		return (List<Agendajanela>) getJdbcTemplate().query(sql.toString(), new Object[]{data, deposito.getCddeposito()},
			new ResultSetExtractor(){
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Agendajanela> list = new ArrayList<Agendajanela>();
					
					while (rs.next()){
						Agendajanela agendajanela = new Agendajanela();
						Deposito deposito = new Deposito();
						deposito.setNome(rs.getString("deposito"));
						agendajanela.setDeposito(deposito);
						agendajanela.setCdagendajanela(rs.getInt("janela"));
						agendajanela.setDisponivel(rs.getInt("ocorrencias") - rs.getInt("agendas"));
						agendajanela.setOcorrencias(rs.getInt("ocorrencias"));
						list.add(agendajanela);
						
					}
					return list;
				}
			}
		);
	}
	
	
	
	/*
	@SuppressWarnings("unchecked")
	public Map<String, Integer> getAgendasPorClasse(Deposito deposito, Date data, Agenda agenda) {
		if (deposito == null || deposito.getCddeposito() == null || data == null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT classe, Count(DISTINCT agenda) AS agendas ");
		sql.append("FROM ( ");
		sql.append("  SELECT SubStr(cla.numero, 1, 2) AS classe, ");
		sql.append("    (CASE WHEN ap.parcial = 1 THEN aparc.cdagenda WHEN ap.parcial = 0 THEN a.cdagenda ELSE NULL END ) AS agenda ");
		sql.append("  FROM agenda a ");
		sql.append("    join agendapedido ap ON ap.cdagenda = a.cdagenda ");
		sql.append("    join pedidocompraproduto pcp ON pcp.cdpedidocompra = ap.cdpedidocompra ");
		sql.append("    left join agendaparcial aparc ON aparc.cdagenda = a.cdagenda AND pcp.cdpedidocompraproduto = aparc.cdpedidocompraproduto ");
		sql.append("    join produto p ON p.cdproduto = pcp.cdproduto ");
		sql.append("    join produtoclasse cla ON cla.cdprodutoclasse = p.cdprodutoclasse ");
		sql.append("  WHERE a.cddeposito = ? ");
		sql.append("    AND Trunc(a.dtagenda) = ? ");
		sql.append("    AND a.cdagendastatus <> 3 ");
		if(agenda != null && agenda.getCdagenda() != null)
			sql.append(" AND a.cdagenda <> "+agenda.getCdagenda());
		sql.append(") ");
		sql.append("GROUP BY classe ");
		
		return (Map<String, Integer>) getJdbcTemplate().query(sql.toString(), new Object[]{deposito.getCddeposito(), data},
				new ResultSetExtractor(){
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, Integer> result = new HashMap<String, Integer>();
				
				while (rs.next())
					result.put(rs.getString("classe"), rs.getInt("agendas"));
				
				return result;
			}
		}
		);
	}
*/
}
