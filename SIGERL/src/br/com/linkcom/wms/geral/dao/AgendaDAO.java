package br.com.linkcom.wms.geral.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.util.NeoFormater;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Agendastatus;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.vo.AcompanhamentoAgendaVO;
import br.com.linkcom.wms.geral.bean.vo.ResumoAgendaverba;
import br.com.linkcom.wms.modulo.recebimento.controller.report.filtro.EmitiracompanhamentoagendamentoFiltro;
import br.com.linkcom.wms.modulo.sistema.controller.process.filtro.AgendamentoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;
import br.com.linkcom.wms.util.recebimento.AgendaVO;

public class AgendaDAO extends GenericDAO<Agenda> {
	
	@Override
	public void delete(Agenda bean) {
		if (bean == null || bean.getCdagenda() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("delete from Agendapedido ap where ap.agenda.id = ?", 
				new Integer[]{bean.getCdagenda()});
		
		getHibernateTemplate().bulkUpdate("delete from Agendaparcial ap where ap.agenda.id = ?", 
				new Integer[]{bean.getCdagenda()});
		
		super.delete(bean);
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaagendapedido");
		save.saveOrUpdateManaged("listaagendaparcial");
	}
	
	@Override
	public void updateListagemQuery(QueryBuilder<Agenda> query,FiltroListagem _filtro) {
		AgendamentoFiltro filtro = (AgendamentoFiltro)_filtro;
		
		
		query
				.select("distinct agenda.cdagenda,agenda.dtagenda,agenda.dtprevisao, agenda.dtAgendaGera ,tipocarga.cdtipocarga,tipocarga.nome, " +
						"agendastatus.cdagendastatus,agendastatus.nome,depositoTransferencia.cddeposito,depositoTransferencia.nome,agenda.observacao")
				.leftOuterJoin("agenda.tipocarga tipocarga")
				.leftOuterJoin("agenda.agendastatus agendastatus")
				.leftOuterJoin("agenda.listaagendapedido listaagendapedido")
				.leftOuterJoin("listaagendapedido.pedidocompra pedidocompra")
				.leftOuterJoin("pedidocompra.fornecedor fornecedor")
				.leftOuterJoin("agenda.deposito deposito")
				.leftOuterJoin("agenda.listaagendaparcial listaagendaparcial")
				.leftOuterJoin("listaagendaparcial.pedidocompraproduto pedidocompraproduto")
				.leftOuterJoin("agenda.depositoTransferencia depositoTransferencia")
				.leftOuterJoin("pedidocompraproduto.produto produto")	
				.where("listaagendapedido.pedidocompra=?",filtro.getPedidocompra())
				.where("agenda.cdagenda=?",filtro.getCdagendamento())
				.where("tipocarga=?",filtro.getTipocarga())
				.where("agendastatus = ?", filtro.getAgendastatus())
				.where("pedidocompra.fornecedor=?",filtro.getFornecedor())
				.where("pedidocompra.numero = ?",filtro.getNumeroPedido() != null ? filtro.getNumeroPedido().toString() : null)
//				.where("produto.codigo=?",filtro.getCodigoProduto())
//				.where("agenda.cdagenda=?",filtro.getCdagenda())
				.where("deposito=?",filtro.getDeposito())
				.where("trunc(agenda.dtagenda) >= ?",filtro.getDatainicial())
				.where("trunc(agenda.dtagenda) <= ?",filtro.getDatafinal())
				.where("trunc(agenda.dtAgendaGera) = ?", filtro.getDtAgendaGera())
				.whereWhen("listaagendapedido is null",(filtro.getParcial() != null && !filtro.getParcial()) ? true : false)
				.where("listaagendapedido.parcial=?",(filtro.getParcial() == null || !filtro.getParcial()) ? null : filtro.getParcial())
				.where("agenda.depositoTransferencia = ? ", filtro.getDepositoTransferencia())
				.orderBy("agenda.dtagenda");
		
		if(filtro.getWhereInAgendaPopup() != null && !filtro.getWhereInAgendaPopup().isEmpty()){
			query.whereIn("agendastatus.id ", filtro.getWhereInAgendaPopup());
		}else{
			query.where("agendastatus = ?", filtro.getAgendastatus());
		}
		if (filtro.getProdutoclasse() != null){
			query.join("produto.produtoclasse produtoclasse")
			.join("produtoclasse.v_produtoclasse v_produtoclasse")
			.where("v_produtoclasse.produtoclasse = ?", filtro.getProdutoclasse().getV_produtoclasse().getProdutoclasse()); 
		}
		if(filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().trim().equals("")){
			query
				.openParentheses()
					.openParentheses()
						.where("listaagendapedido.parcial = 1 and pedidocompra.id in (select pcp.pedidocompra.id from Agendaparcial ap join ap.pedidocompraproduto pcp where ap.agenda = agenda and pcp.produto.codigo = ?)", filtro.getCodigoProduto())
					.closeParentheses().or()
					.openParentheses()
						.where("listaagendapedido.parcial = 0 and pedidocompra.id in (select pcp.pedidocompra.id from Pedidocompraproduto pcp where pcp.produto.codigo = ?)", filtro.getCodigoProduto())
					.closeParentheses()
				.closeParentheses();
		}
		
		if (filtro.getProdutoclasse() != null)
			query.ignoreJoin("deposito", "listaagendapedido", "pedidocompra", "fornecedor", "listaagendaparcial", "pedidocompraproduto", "produto", "produtoclasse", "v_produtoclasse");
		else
			query.ignoreJoin("deposito", "listaagendapedido", "pedidocompra", "fornecedor", "listaagendaparcial", "pedidocompraproduto", "produto");
		
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Agenda> query) {
		query.select("agenda.cdagenda, agenda.dtagenda,agenda.dtprevisao, " +
				     "agendastatus.cdagendastatus, agendastatus.nome, agenda.dtAgendaGera, " +
				     "tipocarga.cdtipocarga, tipocarga.nome, agenda.observacao," +
				     "deposito.cddeposito, deposito.nome," +
				     "depositoTransferencia.cddeposito,depositoTransferencia.nome, " +
				     "historico.cdagendahistorico, historico.dtalteracao,historico.descricao, " +
				     "usuario.cdpessoa, usuario.login, usuario.nome");
		query.leftOuterJoin("agenda.tipocarga tipocarga");
		query.leftOuterJoin("agenda.agendastatus agendastatus");
		query.leftOuterJoin("agenda.deposito deposito");
		query.leftOuterJoin("agenda.depositoTransferencia depositoTransferencia");
		query.leftOuterJoin("agenda.listaHistorico historico");
		query.leftOuterJoin("historico.usuarioaltera usuario");
	}
	
	
	/**
	 * 
	 * Método que recupera os produtos dos pedidos que foram agendados.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @param agendamentos 
	 * @return List<AgendaVO>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<AgendaVO> findForReportAgenda(AgendamentoFiltro filtro, String agendamentos) {
		if(filtro == null)
			throw new WmsException("Parâmetros inválidos.");
		
		String sentenca = "SELECT AG.DTAGENDA AS DTAGENDAMENTO, " +
						"		AG.DTPREVISAO AS DTPREVISAO, " +
						"		AG.CDAGENDA AS AGENDA, " +
						"       PC.NUMERO AS NUMERO, " +
						"		TC.NOME AS TIPO, " +
						"		S.NOME AS STATUS, " +
						"		P.CDPRODUTO AS CDPRODUTO, P.CODIGO AS CODIGO, P.DESCRICAO AS DESCRICAO, " +
						"       PARCIAL.QTDE AS QTDE, " +
						"       PES.NOME AS FORNECEDOR, " +
						"       PCP.VALOR, " +
						"       PES.EMAIL, " +
						"       PES.CDPESSOA AS CDFORNECEDOR " +
						"FROM AGENDA AG " +
						"JOIN AGENDAPEDIDO AP " +
						"  ON AG.CDAGENDA = AP.CDAGENDA " +
						"JOIN PEDIDOCOMPRA PC " +
						"  ON AP.CDPEDIDOCOMPRA = PC.CDPEDIDOCOMPRA " +
						"JOIN PESSOA PES " +
						"  ON PES.CDPESSOA = PC.CDPESSOA " +
						"JOIN PEDIDOCOMPRAPRODUTO PCP " +
						"  ON PC.CDPEDIDOCOMPRA = PCP.CDPEDIDOCOMPRA " +
						"JOIN AGENDAPARCIAL PARCIAL " +
						"  ON AG.CDAGENDA = PARCIAL.CDAGENDA " +
						" AND PARCIAL.CDPEDIDOCOMPRAPRODUTO = PCP.CDPEDIDOCOMPRAPRODUTO " +
						"JOIN PRODUTO P " +
						"  ON PCP.CDPRODUTO = P.CDPRODUTO " +
						"JOIN TIPOCARGA TC " +
						"  ON AG.CDTIPOCARGA = TC.CDTIPOCARGA " +
						"JOIN AGENDASTATUS S " +
						"  ON AG.CDAGENDASTATUS = S.CDAGENDASTATUS " +
						"WHERE AP.PARCIAL = 1 " +
						"  AND AG.CDDEPOSITO = " + filtro.getDeposito().getCddeposito();
						
						if(agendamentos == null){
							if(filtro.getCdagendamento() != null)
								sentenca += " AND AG.CDAGENDA = " + filtro.getCdagendamento();
							
							if((filtro.getTipocarga() != null) && (filtro.getTipocarga().getCdtipocarga() != null))
								sentenca += " AND TC.CDTIPOCARGA = " + filtro.getTipocarga().getCdtipocarga();
							
							if((filtro.getAgendastatus() != null) && (filtro.getAgendastatus().getCdagendastatus() != null))
								sentenca += " AND S.CDAGENDASTATUS = " + filtro.getAgendastatus().getCdagendastatus();
							
							if((filtro.getFornecedor() != null) && (filtro.getFornecedor().getCdpessoa() != null))
								sentenca += " AND PC.CDPESSOA =  " + filtro.getFornecedor().getCdpessoa();
							
							if((filtro.getPedidocompra() != null) && (filtro.getPedidocompra().getCodigoerp() != null))
								sentenca += " AND PC.CODIGOERP = " + filtro.getPedidocompra().getCodigoerp();
							
							if((filtro.getCodigoProduto()) != null && (!filtro.getCodigoProduto().isEmpty()))
								sentenca += " AND P.CODIGO = '" + filtro.getCodigoProduto() + "'";
							
							if(filtro.getDatainicial() != null)
								sentenca += " AND AG.DTAGENDA >= TO_DATE('" + filtro.getDatainicial() + "', 'yyyy/mm/ddhh24miss')";
							
							if(filtro.getDatafinal() != null)
								sentenca += " AND AG.DTAGENDA <= TO_DATE('" + filtro.getDatafinal() + "', 'yyyy/mm/ddhh24miss')";
						}						
						else
							sentenca += " AND AG.CDAGENDA IN (" + agendamentos + ")";
  
				sentenca += " " +
							"UNION ALL " +
							" ";
						
						
				sentenca += "SELECT AG.DTAGENDA AS DTAGENDAMENTO, " +
						"		  AG.DTPREVISAO AS DTPREVISAO, " +
						"		  AG.CDAGENDA AS AGENDA, " +
						"         PC.NUMERO AS NUMERO, " +
						"         TC.NOME AS TIPO, " +
						"         S.NOME AS STATUS, " +
						"         P.CDPRODUTO AS CDPRODUTO, P.CODIGO AS CODIGO, P.DESCRICAO AS DESCRICAO, " +
						"         PCP.QTDE AS QTDE, " +
						"         PES.NOME AS FORNECEDOR, " +  
						"         PCP.VALOR, " +
						"         PES.EMAIL, " +
						"         PES.CDPESSOA AS CDFORNECEDOR " +
						"FROM AGENDA AG " +
						"JOIN AGENDAPEDIDO AP " +
						"  ON AG.CDAGENDA = AP.CDAGENDA " +
						"JOIN PEDIDOCOMPRA PC " +
						"  ON AP.CDPEDIDOCOMPRA = PC.CDPEDIDOCOMPRA " +
						"JOIN PESSOA PES " + 
						"  ON PES.CDPESSOA = PC.CDPESSOA " +
						"JOIN PEDIDOCOMPRAPRODUTO PCP " +
						"  ON PC.CDPEDIDOCOMPRA = PCP.CDPEDIDOCOMPRA " + 
						"JOIN PRODUTO P " +
						"  ON PCP.CDPRODUTO = P.CDPRODUTO " +
						"JOIN TIPOCARGA TC " +
						"  ON AG.CDTIPOCARGA = TC.CDTIPOCARGA " + 
						"JOIN AGENDASTATUS S " +
						"  ON AG.CDAGENDASTATUS = S.CDAGENDASTATUS " + 
						"WHERE AP.PARCIAL = 0 " +
						"  AND AG.CDDEPOSITO = " + filtro.getDeposito().getCddeposito();
				
						if(agendamentos == null){
							if(filtro.getCdagendamento() != null)
								sentenca += " AND AG.CDAGENDA = " + filtro.getCdagendamento();
							
							if((filtro.getTipocarga() != null) && (filtro.getTipocarga().getCdtipocarga() != null))
								sentenca += " AND TC.CDTIPOCARGA = " + filtro.getTipocarga().getCdtipocarga();
							
							if((filtro.getAgendastatus() != null) && (filtro.getAgendastatus().getCdagendastatus() != null))
								sentenca += " AND S.CDAGENDASTATUS = " + filtro.getAgendastatus().getCdagendastatus();
							
							if((filtro.getFornecedor() != null) && (filtro.getFornecedor().getCdpessoa() != null))
								sentenca += " AND PC.CDPESSOA =  " + filtro.getFornecedor().getCdpessoa();
							
							if((filtro.getPedidocompra() != null) && (filtro.getPedidocompra().getCodigoerp() != null))
								sentenca += " AND PC.CODIGOERP = " + filtro.getPedidocompra().getCodigoerp();
							
							if((filtro.getCodigoProduto() != null) && (!filtro.getCodigoProduto().isEmpty()))
								sentenca += " AND P.CODIGO = '" + filtro.getCodigoProduto() + "'";
							
							if(filtro.getDatainicial() != null)
								sentenca += " AND AG.DTAGENDA >= TO_DATE('" + filtro.getDatainicial() + "', 'yyyy/mm/ddhh24miss')";
							
							if(filtro.getDatafinal() != null)
								sentenca += " AND AG.DTAGENDA <= TO_DATE('" + filtro.getDatafinal() + "', 'yyyy/mm/ddhh24miss')";
							
						}
						else
							sentenca += " AND AG.CDAGENDA IN (" + agendamentos + ")";
						
						sentenca += " ORDER BY DTAGENDAMENTO, AGENDA";

				//System.out.println(sentenca);
				
				List<AgendaVO> lista = getJdbcTemplate().query(
						sentenca,
						new RowMapper() {
							
							public Object mapRow(java.sql.ResultSet rs, int currentItem) throws SQLException {
								AgendaVO agendaVO = new AgendaVO();
								
								agendaVO.setNumeroAgendamento(rs.getInt("AGENDA"));
								agendaVO.setData(NeoFormater.getInstance().format(rs.getDate("DTAGENDAMENTO")));
								agendaVO.setDtprevisao(NeoFormater.getInstance().format(rs.getDate("DTPREVISAO")));
								agendaVO.setStatus(rs.getString("STATUS"));
								agendaVO.setTipoCarga(rs.getString("TIPO"));
								agendaVO.setCdfornecedor(rs.getInt("CDFORNECEDOR"));
								agendaVO.setFornecedor(rs.getString("FORNECEDOR"));
								agendaVO.setEmailFornecedor(rs.getString("EMAIL"));
								agendaVO.setNumeroPedido(rs.getLong("NUMERO"));
								agendaVO.setCdproduto(rs.getInt("CDPRODUTO"));
								agendaVO.setCodigoProduto(rs.getString("CODIGO"));
								agendaVO.setDescricaoProduto(rs.getString("DESCRICAO"));
								agendaVO.setQtdeProduto(rs.getLong("QTDE"));
								agendaVO.setValor(new Money(rs.getLong("VALOR"), true).getValue().multiply(new BigDecimal(agendaVO.getQtdeProduto())));
								
								return agendaVO;
							}
						});
		
		return lista;
	}
	
	/**
	 * 
	 * Método que atualiza o status de um agendamento para finalizado.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * 
	 */
	public void atualizaStatusAgendamento(Agenda filtro) {
		if((filtro == null) || (filtro.getCdagenda() == null) || (filtro.getAgendastatus() == null)) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("update Agenda agenda set agenda.agendastatus.id=? where agenda.id=? ", new Object[]{filtro.getAgendastatus().getCdagendastatus(), filtro.getCdagenda()});		
	}
	
	/**
	 * Busca os totais agendados por mês dentro de um semestre.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ResumoAgendaverba> findResumoAgenda(Deposito deposito, Date dtinicio, Date dtfim, boolean filtrarPorVencimentoFinanceiro, List<String> classes, Agenda agenda){
		if (dtinicio == null || dtfim == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Object> args = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct cla.numero AS classeproduto, Trunc(a.dtagenda, 'MM') AS mes, "); 
		sql.append("  Sum(CASE WHEN (ap.parcial = 1) THEN aparc.qtde * pcp.valor ELSE pcp.qtde * pcp.valor END) AS valor ");
		sql.append("FROM agenda a ");
		sql.append("  join agendapedido ap ON ap.cdagenda = a.cdagenda ");
		sql.append("  join pedidocompraproduto pcp ON pcp.cdpedidocompra = ap.cdpedidocompra ");
		sql.append("  join produto p ON p.cdproduto = pcp.cdproduto  ");
		sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
		sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
		sql.append("  left join agendaparcial aparc ON aparc.cdagenda = a.cdagenda AND aparc.cdpedidocompraproduto = pcp.cdpedidocompraproduto AND ap.parcial = 1 ");
		sql.append("  join deposito d ON d.cddeposito = a.cddeposito ");
		sql.append(" WHERE A.CDAGENDASTATUS = 1 ");
//		sql.append(" AND d.ativo = 1 ");
		
		if (filtrarPorVencimentoFinanceiro){
			sql.append(" AND a.dtprevisao >= ? AND a.dtprevisao <= ? ");
			args.add(dtinicio);
			args.add(dtfim);
		}
		sql.append(" AND a.dtagenda >= ? AND a.dtagenda <= ? ");
		
		args.add(dtinicio);
		args.add(WmsUtil.lastDateOfMonth(dtfim));
		
		if (deposito != null && deposito.getCddeposito() > 0){
			sql.append("  AND a.cddeposito = ? ");
			args.add(deposito.getCddeposito());
		}
		
		if(agenda != null && agenda.getCdagenda() != null)
			sql.append(" AND a.cdagenda <> "+agenda.getCdagenda());
		
		if (classes != null && !classes.isEmpty()){
			String classesStr = "";
			for (String classe : classes) {
				classesStr += "'" + classe + "',";
			}
			classesStr = classesStr.substring(0, classesStr.length() - 1);
			
			sql.append(" AND cla.numero in (").append(classesStr).append(") ");
		}
		
		sql.append("GROUP BY cla.numero, Trunc(a.dtagenda, 'MM')  ");

		
		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResumoAgendaverba resumoAgendaverba = new ResumoAgendaverba();
				resumoAgendaverba.setClasseproduto(rs.getString("classeproduto"));
				resumoAgendaverba.setMes(rs.getDate("mes"));
				resumoAgendaverba.setValor(rs.getLong("valor") == 0 ? null : new Money(rs.getLong("valor"), true));
				return resumoAgendaverba;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<ResumoAgendaverba> findResumoAgendaFinanceiro(Deposito deposito, Date dtagenda, Date dtprevisao, List<String> classes, Agenda agenda){
		if (dtagenda == null || dtprevisao == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Object> args = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct cla.numero AS classeproduto, Trunc(a.dtagenda, 'MM') AS mes, "); 
		sql.append("  Sum(CASE WHEN (ap.parcial = 1) THEN aparc.qtde * pcp.valor ELSE pcp.qtde * pcp.valor END) AS valor ");
		sql.append("FROM agenda a ");
		sql.append("  join agendapedido ap ON ap.cdagenda = a.cdagenda ");
		sql.append("  join pedidocompraproduto pcp ON pcp.cdpedidocompra = ap.cdpedidocompra ");
		sql.append("  join produto p ON p.cdproduto = pcp.cdproduto  ");
		sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
		sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
		sql.append("  left join agendaparcial aparc ON aparc.cdagenda = a.cdagenda AND aparc.cdpedidocompraproduto = pcp.cdpedidocompraproduto AND ap.parcial = 1 ");
		sql.append("WHERE A.CDAGENDASTATUS = 1 ");
		sql.append(" AND a.dtprevisao >= ? AND a.dtprevisao <= ? ");
		sql.append(" AND a.dtagenda >= ? AND a.dtagenda <= ? ");
		
		args.add(WmsUtil.firstDateOfMonth(dtprevisao));
		args.add(WmsUtil.lastDateOfMonth(dtprevisao));
		
		args.add(WmsUtil.firstDateOfMonth(dtagenda));
		args.add(WmsUtil.lastDateOfMonth(dtagenda));
		
		if (deposito != null && deposito.getCddeposito() > 0){
			sql.append("  AND a.cddeposito = ? ");
			args.add(deposito.getCddeposito());
		}
		
		if(agenda != null && agenda.getCdagenda() != null)
			sql.append(" AND a.cdagenda <> "+agenda.getCdagenda());
		
		if (classes != null && !classes.isEmpty()){
			String classesStr = "";
			for (String classe : classes) {
				classesStr += "'" + classe + "',";
			}
			classesStr = classesStr.substring(0, classesStr.length() - 1);
			
			sql.append(" AND cla.numero in (").append(classesStr).append(") ");
		}
		
		sql.append("GROUP BY cla.numero, Trunc(a.dtagenda, 'MM')  ");
		
		
		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResumoAgendaverba resumoAgendaverba = new ResumoAgendaverba();
				resumoAgendaverba.setClasseproduto(rs.getString("classeproduto"));
				resumoAgendaverba.setMes(rs.getDate("mes"));
				resumoAgendaverba.setValor(new Money(rs.getLong("valor"), true));
				return resumoAgendaverba;
			}
		});
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ResumoAgendaverba> findResumoAgendaFinanceiro2(Deposito deposito, Date dtagenda, Date dtprevisao, List<String> classes){
		if (dtagenda == null || dtprevisao == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Object> args = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct cla.numero AS classeproduto, Trunc(a.dtprevisao, 'MM') AS mes, "); 
		sql.append("  Sum(CASE WHEN (ap.parcial = 1) THEN aparc.qtde * pcp.valor ELSE pcp.qtde * pcp.valor END) AS valor ");
		sql.append("FROM agenda a ");
		sql.append("  join agendapedido ap ON ap.cdagenda = a.cdagenda ");
		sql.append("  join pedidocompraproduto pcp ON pcp.cdpedidocompra = ap.cdpedidocompra ");
		sql.append("  join produto p ON p.cdproduto = pcp.cdproduto  ");
		sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
		sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
		sql.append("  left join agendaparcial aparc ON aparc.cdagenda = a.cdagenda AND aparc.cdpedidocompraproduto = pcp.cdpedidocompraproduto AND ap.parcial = 1 ");
		sql.append("WHERE A.CDAGENDASTATUS = 1 ");
		sql.append(" AND a.dtagenda >= ? AND a.dtagenda <= ? ");
		sql.append(" AND a.dtprevisao >= ? AND a.dtprevisao <= ? ");
		
		args.add(dtagenda);
		args.add(WmsUtil.lastDateOfMonth(dtagenda));
		args.add(WmsUtil.firstDateOfMonth(dtagenda));
		args.add(dtprevisao);
		
		if (deposito != null && deposito.getCddeposito() > 0){
			sql.append("  AND a.cddeposito = ? ");
			args.add(deposito.getCddeposito());
		}
		
		if (classes != null && !classes.isEmpty()){
			String classesStr = "";
			for (String classe : classes) {
				classesStr += "'" + classe + "',";
			}
			classesStr = classesStr.substring(0, classesStr.length() - 1);
			
			sql.append(" AND cla.numero in (").append(classesStr).append(") ");
		}
		
		sql.append("GROUP BY cla.numero, Trunc(a.dtprevisao, 'MM')  ");
		
		
		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResumoAgendaverba resumoAgendaverba = new ResumoAgendaverba();
				resumoAgendaverba.setClasseproduto(rs.getString("classeproduto"));
				resumoAgendaverba.setMes(rs.getDate("mes"));
				resumoAgendaverba.setValor(new Money(rs.getLong("valor"), true));
				return resumoAgendaverba;
			}
		});
	}


	/**
	 * Verifica se existe o agendamento no depósito logado e com o status 'AGENDADO'.
	 *
	 * @param agenda
	 * @return
	 * @author Rodrigo Freitas
	 */
	public boolean existsDepositoAgendado(Agenda agenda) {
		if (agenda == null || agenda.getCdagenda() == null){
			
			throw new WmsException("Parâmetros inválidos.");
		}
		
		return new QueryBuilder<Long>(getHibernateTemplate())
					.select("count(*)")
					.from(Agenda.class)
					.setUseTranslator(false)
					.where("agenda.deposito = ?", agenda.getDeposito())
					.where("agenda.agendastatus <> ?", Agendastatus.CANCELADO)
					.where("agenda = ?", agenda)
					.unique() > 0;
	}

	/**
	 * Busca os agendamentos que não estão finalizados com 
	 * data menor ou igual a data passado por parâmetro.
	 *
	 * @param date
	 * @return
	 * @author Rodrigo Freitas
	 */
	public List<Agenda> findForCancelamento(java.sql.Date date) {
		return query()
					.select("agenda.cdagenda, agenda.dtagenda")
					.where("agenda.dtagenda <= ?", date)
					.where("agenda.agendastatus <> ?", Agendastatus.FINALIZADO)
					.where("agenda.agendastatus <> ?", Agendastatus.CANCELADO)
					.list();
	}

	/**
	 * Busca os dados para o relatório de acompanhamento de agendamento.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AcompanhamentoAgendaVO> findForAcompanhamento(EmitiracompanhamentoagendamentoFiltro filtro) {
		List<Object> args = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT distinct cla.numero AS numeroClasse, claP.nome AS classeProduto, a.dtagenda,  ");
		sql.append("  (CASE WHEN (ap.parcial = 1) THEN aparc.qtde ELSE pcp.qtde END) AS qtdeAgenda, ");
		sql.append("  p.cdproduto, pc.numero AS numeroPedido, f.nome AS fornecedor, ");
		sql.append("  p.codigo AS codigoProduto, p.descricao AS produto, ");
		sql.append("  r.dtrecebimento, nfp.qtde AS qtdeRecebida, nfe.cdagenda ");
		sql.append("FROM agenda a ");
		sql.append("  join agendapedido ap ON ap.cdagenda = a.cdagenda ");
		sql.append("  join pedidocompra pc ON pc.cdpedidocompra = ap.cdpedidocompra ");
		sql.append("  join pedidocompraproduto pcp ON pcp.cdpedidocompra = ap.cdpedidocompra ");
		sql.append("  join produto p ON p.cdproduto = pcp.cdproduto ");
		sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
		sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
		sql.append("  join pessoa f ON f.cdpessoa = pc.cdpessoa ");
		sql.append("  join produtoclasse claP ON cla.numero = claP.numero ");
		sql.append("  left join agendaparcial aparc ON aparc.cdagenda = a.cdagenda AND aparc.cdpedidocompraproduto = pcp.cdpedidocompraproduto AND ap.parcial = 1 ");

		sql.append("  left join Notafiscalentrada nfe ON nfe.cdagenda = a.cdagenda ");
		sql.append("  left join recebimentonotafiscal rnf ON rnf.cdnotafiscalentrada = nfe.cdnotafiscalentrada ");
		sql.append("  left join recebimento r ON r.cdrecebimento = rnf.cdrecebimento ");
		sql.append("  left join notafiscalentradaproduto nfp ON nfp.cdnotafiscalentrada = nfe.cdnotafiscalentrada AND nfp.cdproduto = pcp.cdproduto ");

		sql.append("WHERE  ");
		sql.append("  a.cddeposito = ? ");
		args.add(filtro.getDeposito().getCddeposito());
		sql.append("  AND A.CDAGENDASTATUS <> 3 ");
		
		if (filtro.getDtinicioagendamento() != null){
			sql.append("  AND a.dtagenda >= ?");
			args.add(filtro.getDtinicioagendamento());
		}
			
		if (filtro.getDtfimagendamento() != null){
			sql.append("  AND a.dtagenda <= ?");
			args.add(WmsUtil.dataToEndOfDay(filtro.getDtfimagendamento()));
		}
		
		if (filtro.getDtiniciorecebimento() != null){
			sql.append("  AND r.dtrecebimento >= ?");
			args.add(filtro.getDtiniciorecebimento());
		}
		
		if (filtro.getDtfimrecebimento() != null){
			sql.append("  AND r.dtrecebimento <= ?");
			args.add(WmsUtil.dataToEndOfDay(filtro.getDtfimrecebimento()));
		}
		
		if (filtro.getFornecedor() != null && filtro.getFornecedor().getCdpessoa() != null){
			sql.append("  AND f.cdpessoa = ?");
			args.add(filtro.getFornecedor().getCdpessoa());
		}

		if (filtro.getNumeropedido() != null && !filtro.getNumeropedido().trim().isEmpty()){
			sql.append("  AND pc.numero = ?");
			args.add(filtro.getNumeropedido());
		}

		sql.append("ORDER BY f.nome, cla.numero, a.dtagenda, pc.numero, p.codigo ");

		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				AcompanhamentoAgendaVO item = new AcompanhamentoAgendaVO();
				item.setNumeroClasse(rs.getString("numeroClasse"));
				item.setClasseProduto(rs.getString("classeProduto"));
				item.setDtprevisaoagenda(rs.getDate("dtagenda"));
				item.setQtdeAgenda(rs.getInt("qtdeAgenda"));
				item.setNumeroPedido(rs.getString("numeroPedido"));
				item.setFornecedor(rs.getString("fornecedor"));
				item.setCodigoProduto(rs.getString("codigoProduto"));
				item.setProduto(rs.getString("produto"));
				item.setQtdeRecebida(rs.getInt("qtdeRecebida"));
				item.setDtrecebimento(rs.getDate("dtrecebimento"));
				
				return item;
			}
		});
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Agenda> recebimentosByAgenda(String whereIn) {
		if (whereIn == null || whereIn ==  "")
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select distinct a.cdagenda, r.cdrecebimento as recebimento ");
		sql.append("from agenda a ");
		sql.append("left join notafiscalentrada nfe on(a.cdagenda = nfe.cdagenda) ");
		sql.append("left join recebimentonotafiscal rnf on(nfe.cdnotafiscalentrada = rnf.cdnotafiscalentrada) ");
		sql.append("left join recebimento r on(rnf.cdrecebimento = r.cdrecebimento) ");
		sql.append("where a.cdagenda in(" +whereIn + ") ");
		
		return (List<Agenda>) getJdbcTemplate().query(sql.toString(), new Object[]{},
				new ResultSetExtractor(){
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Agenda> list = new ArrayList<Agenda>();
				
				while (rs.next()){
					Agenda agenda = new Agenda();
					agenda.setCdagenda(rs.getInt("cdagenda"));
					agenda.setRecebimentos(rs.getString("recebimento"));
					list.add(agenda);
				}
				return list;
			}
		}
		);
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<ResumoAgendaverba> findResumoAgendaFinanceiro(Deposito deposito, Date dtinicio, Date dtfim, boolean filtrarPorVencimentoFinanceiro, List<String> classes){
		if (dtinicio == null || dtfim == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Object> args = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct cla.numero AS classeproduto, Trunc(a.dtprevisao, 'MM') AS mes, "); 
		sql.append("  Sum(CASE WHEN (ap.parcial = 1) THEN aparc.qtde * pcp.valor ELSE pcp.qtde * pcp.valor END) AS valor ");
		sql.append("FROM agenda a ");
		sql.append("  join agendapedido ap ON ap.cdagenda = a.cdagenda ");
		sql.append("  join pedidocompraproduto pcp ON pcp.cdpedidocompra = ap.cdpedidocompra ");
		sql.append("  join produto p ON p.cdproduto = pcp.cdproduto  ");
		sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
		sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
		sql.append("  left join agendaparcial aparc ON aparc.cdagenda = a.cdagenda AND aparc.cdpedidocompraproduto = pcp.cdpedidocompraproduto AND ap.parcial = 1 ");
		sql.append("WHERE A.CDAGENDASTATUS = 1 ");
		
		if (filtrarPorVencimentoFinanceiro){
			sql.append(" AND a.dtprevisao >= ? AND a.dtprevisao <= ? ");
			args.add(dtinicio);
			args.add(dtfim);
		}
		sql.append(" AND a.dtagenda >= ? AND a.dtagenda <= ? ");
		
		args.add(dtinicio);
		args.add(WmsUtil.lastDateOfMonth(dtinicio));
		
		if (deposito != null && deposito.getCddeposito() > 0){
			sql.append("  AND a.cddeposito = ? ");
			args.add(deposito.getCddeposito());
		}
		
		if (classes != null && !classes.isEmpty()){
			String classesStr = "";
			for (String classe : classes) {
				classesStr += "'" + classe + "',";
			}
			classesStr = classesStr.substring(0, classesStr.length() - 1);
			
			sql.append(" AND cla.numero in (").append(classesStr).append(") ");
		}
		
		sql.append("GROUP BY cla.numero, Trunc(a.dtprevisao, 'MM')  ");
		
		
		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResumoAgendaverba resumoAgendaverba = new ResumoAgendaverba();
				resumoAgendaverba.setClasseproduto(rs.getString("classeproduto"));
				resumoAgendaverba.setMes(rs.getDate("mes"));
				resumoAgendaverba.setValor(new Money(rs.getLong("valor"), true));
				return resumoAgendaverba;
			}
		});
	}

	/**
	 * Carrega uma agenda com os dados necessários para o registro de hist'rocio de alteraç±ao do CRUD de Agenda
	 * @param agendaOriginal
	 * @return
	 */
	public Agenda loadForHistoricoAlteracao(Agenda agendaOriginal) {
		return query()
		.select("agenda.cdagenda, agenda.dtagenda, agenda.dtprevisao, pedidocompra.cdpedidocompra, pedidocompra.numero, " +
				"pcp.cdpedidocompraproduto, agendastatus.cdagendastatus, agendastatus.nome," +
				"pcp.qtde, produto.descricao, agendaparcial.cdagendaparcial, agendaparcial.qtde, listaAP.cdagendapedido")
		.join("agenda.agendastatus agendastatus")		
		.leftOuterJoin("agenda.listaagendapedido listaAP")
		.leftOuterJoin("agenda.listaagendaparcial agendaparcial")
		.leftOuterJoin("agendaparcial.pedidocompraproduto pcp")
		.leftOuterJoin("pcp.pedidocompra pedidocompra")
		.leftOuterJoin("pcp.produto produto")
		.entity(agendaOriginal)
		.unique();
	}
	
	/**
	 * Retorna o total agendado de uma agenda
	 * @param agenda
	 * @return
	 */
	public Integer getTotalAgendado(Agenda agenda) {
//		return 0;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT SUM(qtde) FROM( ");
			sql.append("SELECT APARC.QTDE ");
			sql.append("FROM AGENDAPEDIDO AP JOIN AGENDAPARCIAL APARC ON (AP.CDAGENDA = APARC.CDAGENDA) ");
			sql.append("JOIN PEDIDOCOMPRAPRODUTO PCP ON (APARC.CDPEDIDOCOMPRAPRODUTO = PCP.CDPEDIDOCOMPRAPRODUTO) ");
			sql.append("WHERE AP.PARCIAL = 1 ");
			sql.append("AND AP.CDAGENDA = ? ");
			sql.append("UNION ALL ");
			sql.append("SELECT PCP.QTDE ");
			sql.append("FROM AGENDAPEDIDO AP JOIN PEDIDOCOMPRA PC ON (AP.CDPEDIDOCOMPRA = PC.CDPEDIDOCOMPRA) ");
			sql.append("JOIN PEDIDOCOMPRAPRODUTO PCP ON (AP.CDPEDIDOCOMPRA = PCP.CDPEDIDOCOMPRA) ");
			sql.append("WHERE AP.PARCIAL = 0 ");
			sql.append("AND AP.CDAGENDA = ? ");
		sql.append(") ");
		return getJdbcTemplate().queryForInt(sql.toString(), new Object[]{agenda.getCdagenda(), agenda.getCdagenda()});	 
	}
	
	public Integer agendaContemRecebimento(Agenda agenda){
		StringBuilder sql = new StringBuilder();
			sql.append(" select count(r.cdrecebimento) as recebimento ");
			sql.append(" from agenda a ");
			sql.append(" join notafiscalentrada nfe on(a.cdagenda = nfe.cdagenda) ");
			sql.append(" join recebimentonotafiscal rnf on(nfe.cdnotafiscalentrada = rnf.cdnotafiscalentrada) ");
			sql.append(" join recebimento r on(rnf.cdrecebimento = r.cdrecebimento) ");
			sql.append(" where a.cdagenda = ? ");
		return getJdbcTemplate().queryForInt(sql.toString(), new Object[]{agenda.getCdagenda()});	 
	}
 
	public Agenda loadWithAgendaStatus(Agenda agenda) {
		return query()
			.select("agenda.cdagenda, agenda.dtagenda,agenda.dtprevisao, " +
				     "agendastatus.cdagendastatus, agendastatus.nome, agenda.dtAgendaGera")
			.leftOuterJoin("agenda.tipocarga tipocarga")
			.leftOuterJoin("agenda.agendastatus agendastatus")
			.where("agenda.cdagenda = ?",agenda.getCdagenda())
			.unique();
	}
	
		public Integer findByDate(Deposito deposito, java.sql.Date inicio, java.sql.Date fim) {
		StringBuilder sql = new StringBuilder();
			sql.append(" SELECT DISTINCT COUNT(ag.cdagenda) ");
			sql.append(" FROM AGENDA AG ");
			sql.append(" JOIN AGENDAPEDIDO AP ON (AG.CDAGENDA = AP.CDAGENDA) ");
			sql.append(" JOIN PEDIDOCOMPRA PC ON (AP.CDPEDIDOCOMPRA = PC.CDPEDIDOCOMPRA) ");
			sql.append(" left join notafiscalentrada nfe on (nfe.cdpedidocompra = pc.cdpedidocompra and nfe.cdagenda = ag.cdagenda) ");
			sql.append(" left join recebimentonotafiscal rnf ON (nfe.cdnotafiscalentrada = rnf.cdnotafiscalentrada) ");
			sql.append(" left join recebimento r on (rnf.cdrecebimento = r.cdrecebimento) ");
			sql.append(" WHERE AG.CDDEPOSITO =  ? ");
			sql.append(" AND AG.DTAGENDA BETWEEN ? AND ? ");
		return getJdbcTemplate().queryForInt(sql.toString(), new Object[]{deposito.getCddeposito(),inicio,fim});
	}
	
	public Integer findByDateRecebido(Deposito deposito, java.sql.Date inicio, java.sql.Date fim) {
		StringBuilder sql = new StringBuilder();
			sql.append(" SELECT DISTINCT COUNT(ag.cdagenda) ");
			sql.append(" FROM AGENDA AG ");
			sql.append(" JOIN AGENDAPEDIDO AP ON (AG.CDAGENDA = AP.CDAGENDA) ");
			sql.append(" JOIN PEDIDOCOMPRA PC ON (AP.CDPEDIDOCOMPRA = PC.CDPEDIDOCOMPRA) ");
			sql.append(" left join notafiscalentrada nfe on (nfe.cdpedidocompra = pc.cdpedidocompra and nfe.cdagenda = ag.cdagenda) ");
			sql.append(" left join recebimentonotafiscal rnf ON (nfe.cdnotafiscalentrada = rnf.cdnotafiscalentrada) ");
			sql.append(" left join recebimento r on (rnf.cdrecebimento = r.cdrecebimento) ");
			sql.append(" WHERE AG.CDDEPOSITO =  ? ");
			sql.append(" AND AG.DTAGENDA BETWEEN ? AND ? ");
			sql.append(" AND (to_date(R.DTRECEBIMENTO,'dd/mm/yyyy') = to_date(AG.DTAGENDA,'dd/mm/yyyy') ");
			sql.append("   OR to_date(R.DTRECEBIMENTO,'dd/mm/yyyy') < to_date(AG.DTAGENDA,'dd/mm/yyyy')) ");
		return getJdbcTemplate().queryForInt(sql.toString(), new Object[]{deposito.getCddeposito(),inicio,fim});
	}
	
	public Integer findByDateFuro(Deposito deposito, java.sql.Date inicio, java.sql.Date fim) {
		StringBuilder sql = new StringBuilder();
			sql.append(" SELECT DISTINCT COUNT(ag.cdagenda) ");
			sql.append(" FROM AGENDA AG ");
			sql.append(" JOIN AGENDAPEDIDO AP ON (AG.CDAGENDA = AP.CDAGENDA) ");
			sql.append(" JOIN PEDIDOCOMPRA PC ON (AP.CDPEDIDOCOMPRA = PC.CDPEDIDOCOMPRA) ");
			sql.append(" left join notafiscalentrada nfe on (nfe.cdpedidocompra = pc.cdpedidocompra and nfe.cdagenda = ag.cdagenda) ");
			sql.append(" left join recebimentonotafiscal rnf ON (nfe.cdnotafiscalentrada = rnf.cdnotafiscalentrada) ");
			sql.append(" left join recebimento r on (rnf.cdrecebimento = r.cdrecebimento) ");
			sql.append(" WHERE AG.CDDEPOSITO =  ? ");
			sql.append(" AND AG.DTAGENDA BETWEEN ? AND ? ");
			sql.append(" AND to_date(R.DTRECEBIMENTO,'dd/mm/yyyy') > to_date(AG.DTAGENDA,'dd/mm/yyyy') ");
		return getJdbcTemplate().queryForInt(sql.toString(), new Object[]{deposito.getCddeposito(),inicio,fim});
	}
	
	public Integer findByDateNaoComparecido(Deposito deposito, java.sql.Date inicio, java.sql.Date fim) {
		StringBuilder sql = new StringBuilder();
			sql.append(" SELECT DISTINCT COUNT(ag.cdagenda) ");
			sql.append(" FROM AGENDA AG ");
			sql.append(" JOIN AGENDAPEDIDO AP ON (AG.CDAGENDA = AP.CDAGENDA) ");
			sql.append(" JOIN PEDIDOCOMPRA PC ON (AP.CDPEDIDOCOMPRA = PC.CDPEDIDOCOMPRA) ");
			sql.append(" left join notafiscalentrada nfe on (nfe.cdpedidocompra = pc.cdpedidocompra and nfe.cdagenda = ag.cdagenda) ");
			sql.append(" left join recebimentonotafiscal rnf ON (nfe.cdnotafiscalentrada = rnf.cdnotafiscalentrada) ");
			sql.append(" left join recebimento r on (rnf.cdrecebimento = r.cdrecebimento) ");
			sql.append(" WHERE AG.CDDEPOSITO =  ? ");
			sql.append(" AND AG.DTAGENDA BETWEEN ? AND ? ");
			sql.append(" AND R.DTRECEBIMENTO IS NULL ");
		return getJdbcTemplate().queryForInt(sql.toString(), new Object[]{deposito.getCddeposito(),inicio,fim});
	}

	/**
	 * Buscando as Agendas Vinculadas ao RAV e Nota
	 * @author jose queiroz
	 * @return Lista de Status
	 */
	public Integer findAgendaRAV(Integer numeroRav ){
	
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT AG.CDAGENDA ");
		sql.append(" FROM AGENDAACOMPANHAMENTOVEICULO AG ");
		sql.append(" WHERE AG.CDACOMPANHAMENTOVEICULO = ? ");
		sql.append("  AND ROWNUM <= 1 ");

		return getJdbcTemplate().queryForInt(sql.toString(), new Object[]{numeroRav});
	}

	/**
	 * 
	 * @param agenda
	 * @param deposito
	 * @return
	 */
	public Agenda findByAgendaDeposito(Agenda agenda, Deposito deposito) {
		return query()
			.select("agenda.cdagenda, deposito.cddeposito")
			.join("agenda.deposito deposito")
			.where("deposito = ?",deposito)
			.where("agenda = ?",agenda)
			.unique();
	}
}