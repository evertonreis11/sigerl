package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Agendaverba;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Produtoclasse;
import br.com.linkcom.wms.geral.service.ProdutoclasseService;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.AgendaverbaFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class AgendaverbaDAO extends GenericDAO<Agendaverba> {

	private ProdutoclasseService produtoclasseService;
	
	public void setProdutoclasseService(ProdutoclasseService produtoclasseService) {
		this.produtoclasseService = produtoclasseService;
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Agendaverba> query) {
		super.updateEntradaQuery(query);
		query.joinFetch("agendaverba.deposito deposito");
		query.joinFetch("agendaverba.produtoclasse produtoclasse");
		query.leftOuterJoinFetch("agendaverba.listaAgendaverbafinanceiro agendaverbafinanceiro");
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		super.updateSaveOrUpdate(save);
		save.saveOrUpdateManagedDeleteFirst("listaAgendaverbafinanceiro");
	}
	
	/**
	 * Busca os exercícios cadastrados, retornando uma lista de datas para os meses de Janeiro e Julho, indicando o 1º ou 2° semestre.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Date> findExercicios(Deposito deposito){
		if (deposito == null)
			throw new WmsException("Parâmetros inválidos.");
		
		String sql = "SELECT DISTINCT decode(to_char(dtagendaverba,'Q'),1,1,2,1,3,2,4,2) AS semestre, " +
				"trunc(dtagendaverba, 'YYYY') AS ano " +
				"FROM agendaverba " +
				(deposito.getCddeposito() > 0 ? "WHERE cddeposito = ? " : " " ) +
				"ORDER BY 2,1  ";
		
		Integer[] args;
		if (deposito.getCddeposito() > 0)
			args = new Integer[]{deposito.getCddeposito()};
		else
			args = new Integer[]{};
		
		return getJdbcTemplate().query(sql, args, new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(rs.getDate("ano"));
				if (rs.getInt("semestre") == 1)
					calendar.set(Calendar.MONTH, Calendar.JANUARY);
				else
					calendar.set(Calendar.MONTH, Calendar.JULY);
				
				return calendar.getTime();
			}
		});
	}
	
	/**
	 * Localiza os agendamentos de verbas para a tela de edição.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public List<Agendaverba> findForEdicao(AgendaverbaFiltro filtro) {
		if (filtro == null || filtro.getDeposito() == null || filtro.getExercicio() == null)
			throw new WmsException("Parâmetros inválidos.");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(filtro.getExercicio());
		calendar.add(Calendar.MONTH, 6);
		
		return query()
			.joinFetch("agendaverba.produtoclasse produtoclasse")
			.where("agendaverba.deposito = ?", filtro.getDeposito())
			.where("agendaverba.dtagendaverba >= ? ", filtro.getExercicio())
			.where("agendaverba.dtagendaverba < ? ", calendar.getTime())
			.list();
	}
	
	public List<Agendaverba> findAgendaverba(AgendaverbaFiltro filtro) {
		if (filtro == null || filtro.getDeposito() == null || filtro.getExercicio() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		
		return query()
		.joinFetch("agendaverba.produtoclasse produtoclasse")
		.where("agendaverba.deposito = ?", filtro.getDeposito())
		.where("agendaverba.dtagendaverba >= ? ", WmsUtil.firstDateOfMonth(filtro.getExercicio()))
		.where("agendaverba.dtagendaverba <= ? ", WmsUtil.lastDateOfMonth(filtro.getExercicio()))
	    .orderBy("produtoclasse.nome")	
		.list();
	}
	
	
	/**
	 * Localiza os agendamentos de verbas para a tela de edição.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Agendaverba> findResumoGeral(Date exercicio) {
		if (exercicio == null)
			throw new WmsException("Parâmetros inválidos.");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(exercicio);
		calendar.add(Calendar.MONTH, 6);
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT trunc(av.dtagendaverba, 'MM') as dtagendaverba, SUM(av.verba) AS verba, ");
		sql.append("   pc.cdprodutoclasse, pc.numero, pc.nome ");
		sql.append("FROM agendaverba av ");
		sql.append("   JOIN produtoclasse pc ON pc.cdprodutoclasse = av.cdprodutoclasse ");
		sql.append("WHERE av.dtagendaverba >= ? ");
		sql.append("   AND av.dtagendaverba < ? ");
		sql.append("GROUP BY trunc(av.dtagendaverba, 'MM'), ");
		sql.append("   pc.cdprodutoclasse, pc.numero, pc.nome ");
		
		return getJdbcTemplate().query(sql.toString(), new Date[]{exercicio, calendar.getTime()}, new RowMapper(){

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Agendaverba av = new Agendaverba();
				av.setDtagendaverba(rs.getDate("dtagendaverba"));
				av.setVerba(new Money(rs.getLong("verba"), true));
				av.setProdutoclasse(new Produtoclasse());
				av.getProdutoclasse().setCdprodutoclasse(rs.getInt("cdprodutoclasse"));
				av.getProdutoclasse().setNumero(rs.getString("numero"));
				av.getProdutoclasse().setNome(rs.getString("nome"));
				
				return av;
			}
			
		});
	}

	/**
	 * Busca a lista de agendamento de verba de acordo com os parâmetros passados.
	 *
	 * @param deposito
	 * @param produtoclasse
	 * @param dtagenda
	 * @return
	 * @author Rodrigo Freitas
	 */
	public List<Agendaverba> findListaVerba(Deposito deposito, Produtoclasse produtoclasse, Date dtagenda) {
//		if (deposito == null || deposito.getCddeposito() == null)
//			throw new WmsException("Parâmetros inválidos.");
		
		return query()
					.select("agendaverba.cdagendaverba, agendaverba.verba")
					.join("agendaverba.produtoclasse produtoclasse")
					.where("agendaverba.deposito = ?", deposito)
					.where("agendaverba.deposito.ativo = 1")
					.where("produtoclasse.numero like ?", produtoclasseService.loadWithV_produtoclasse(produtoclasse).getV_produtoclasse().getProdutoclasse().getNumero())
					.where("agendaverba.dtagendaverba >= ?", WmsUtil.firstDateOfMonth(dtagenda))
					.where("agendaverba.dtagendaverba <= ?", WmsUtil.lastDateOfMonth(dtagenda))
					.list();
	}

	/**
	 * Verifica se já existe um controle de verba criado para o exercício/depósito escolhidos.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public boolean existeExercicio(AgendaverbaFiltro filtro) {
		if (filtro == null || filtro.getDeposito() == null || filtro.getExercicio() == null)
			throw new WmsException("Parâmetros inválidos.");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(filtro.getExercicio());
		calendar.add(Calendar.MONTH, 6);
		
		return query()
			.where("agendaverba.deposito = ?", filtro.getDeposito())
			.where("agendaverba.dtagendaverba >= ? ", filtro.getExercicio())
			.where("agendaverba.dtagendaverba < ? ", calendar.getTime())
			.setMaxResults(1)
			.unique() != null;
	}
	
	
	
/*	@SuppressWarnings("unchecked")
	public List<ResumoVerbaFinanceiro> findVerbaFinanceiro(Date exercicio){
		if (exercicio == null)
			throw new WmsException("Parâmetro inválido.");
		
		List<Object> args = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT SUBSTR(pc.numero, 1, 2) AS classeproduto, Trunc(avf.dtagendaverba, 'MM') AS mes, Sum(avf.verba) AS valor ");
		sql.append("FROM agendaverba av ");
		sql.append("join produtoclasse pc on av.cdprodutoclasse = pc.cdprodutoclasse ");
		sql.append("left join agendaverbafinanceiro avf ON av.cdagendaverba = avf.cdagendaverba  ");
		sql.append("WHERE Trunc(avf.dtagendaverba, 'MM') >= ? AND Trunc(avf.dtagendaverba, 'MM') <= ? ");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(exercicio);
		calendar.add(Calendar.MONTH, 5);
		
		args.add(exercicio);
		args.add(calendar.getTime());
		
		sql.append("GROUP BY SUBSTR(pc.numero, 1, 2), Trunc(avf.dtagendaverba, 'MM') ");
		
		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResumoVerbaFinanceiro resumoVerbaFinanceiro = new ResumoVerbaFinanceiro();
				resumoVerbaFinanceiro.setClasseproduto(rs.getString("classeproduto"));
				resumoVerbaFinanceiro.setMes(rs.getDate("mes"));
				resumoVerbaFinanceiro.setValor(new Money(rs.getLong("valor"), true));
				return resumoVerbaFinanceiro;
			}
		});
	}
*/
	
	/*@SuppressWarnings("unchecked")
	public List<ResumoVerbaFinanceiro> findVerbaFinanceiro(Date exercicio){
		if (exercicio == null)
			throw new WmsException("Parâmetro inválido.");
		
		List<Object> args = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT SUBSTR(pc.numero, 1, 2) AS classeproduto, Trunc(avf.dtagendaverba, 'MM') AS mes, Sum(avf.verba) AS valor ");
		sql.append("FROM agendaverba av ");
		sql.append("join produtoclasse pc on av.cdprodutoclasse = pc.cdprodutoclasse ");
		sql.append("left join agendaverbafinanceiro avf ON av.cdagendaverba = avf.cdagendaverba  ");
		sql.append("WHERE Trunc(avf.dtagendaverba, 'MM') >= ? AND Trunc(avf.dtagendaverba, 'MM') <= ? ");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(exercicio);
		calendar.add(Calendar.MONTH, 5);
				
		args.add(exercicio);
		args.add(calendar.getTime());

		sql.append("GROUP BY SUBSTR(pc.numero, 1, 2), Trunc(avf.dtagendaverba, 'MM') ");

		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResumoVerbaFinanceiro resumoVerbaFinanceiro = new ResumoVerbaFinanceiro();
				resumoVerbaFinanceiro.setClasseproduto(rs.getString("classeproduto"));
				resumoVerbaFinanceiro.setMes(rs.getDate("mes"));
				resumoVerbaFinanceiro.setValor(new Money(rs.getLong("valor"), true));
				return resumoVerbaFinanceiro;
			}
		});
	}
	*/
	
	public long getVerbaFinanceiro(Date exercicio, Produtoclasse produtoclasse, Deposito deposito) {
		if (exercicio == null || produtoclasse == null || produtoclasse.getCdprodutoclasse() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Object> args = new ArrayList<Object>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(exercicio);
		calendar.add(Calendar.MONTH, 0);
				
		args.add(exercicio);
		args.add(calendar.getTime());
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(avf.verba) AS valor ");
		sql.append("FROM agendaverba av ");
		sql.append("join produtoclasse pc on av.cdprodutoclasse = pc.cdprodutoclasse ");
		sql.append("left join agendaverbafinanceiro avf ON av.cdagendaverba = avf.cdagendaverba  ");
		sql.append("WHERE Trunc(avf.dtagendaverba, 'MM') >= ? AND Trunc(avf.dtagendaverba, 'MM') <= ? ");
		sql.append(" and pc.cdprodutoclasse = "+ produtoclasse.getCdprodutoclasse());
		if(deposito.getCddeposito() != -1)
			sql.append(" and av.cddeposito = "+ deposito.getCddeposito());
	
		return getJdbcTemplate().queryForLong(sql.toString(), args.toArray());
	}
	
//	return getJdbcTemplate().query(sql.toString(), args.toArray()
}
