package br.com.linkcom.wms.geral.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.LongType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Notafiscalentrada;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Recebimentonotafiscal;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;
import br.com.linkcom.wms.geral.bean.Tipoenderecamento;
import br.com.linkcom.wms.geral.bean.vo.ResumoAgendaverba;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.RecebimentoFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.report.filtro.EmitirDivergenciaRecebimentoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class RecebimentoDAO extends GenericDAO<Recebimento> {
	
	@Override
	public void updateListagemQuery(QueryBuilder<Recebimento> query, FiltroListagem _filtro) {
		if(_filtro == null)
			throw new WmsException("Parâmetros inválidos.");
		
		RecebimentoFiltro filtro = (RecebimentoFiltro) _filtro;
		
		query
			.select("distinct recebimento.cdrecebimento, recebimento.dtrecebimento, recebimentostatus.cdrecebimentostatus, recebimentostatus.nome, " +
					"box.cdbox, box.nome")
			.join("recebimento.box box")
			.join("recebimento.recebimentostatus recebimentostatus")
			.where("box=?", filtro.getBox())
			.where("trunc(recebimento.dtrecebimento) >=?", filtro.getMontadade())
			.where("trunc(recebimento.dtrecebimento) <= ?", filtro.getMontadaate())
			.where("recebimentostatus=?", filtro.getRecebimentostatus())
			.where("recebimento.cdrecebimento=?", filtro.getNumeroRecebimento())
			.where("recebimento.deposito=?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
			.where("exists (from Notafiscalentrada nfe join nfe.listaRecebimentonotafiscalentrada rnf where nfe.numero = ? and rnf.recebimento.id = recebimento.id)", filtro.getNotafiscal())
			.where("exists (from Notafiscalentradaproduto nfep join nfep.notafiscalentrada nfe join nfe.listaRecebimentonotafiscalentrada rnf where nfep.produto = ? and rnf.recebimento.id = recebimento.id)", filtro.getProduto())
			.orderBy("recebimento.cdrecebimento DESC")
			.setPageNumberAndSize(filtro.getCurrentPage(), filtro.getPageSize());
	}
	
	/**
	 * Encontra todos os recebimentos a partir do status.
	 * Utilizar as constantes existentes na classe Recebimentostatus
	 * 
	 * @see br.com.linkcom.wms.geral.bean.Recebimentostatus
	 * 
	 * @param recebimentostatus
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Recebimento> findByStatusAbertoPendente(){
		
		return query()
				.select("recebimento.cdrecebimento,status.cdrecebimentostatus")
				.join("recebimento.recebimentostatus status")
				.join("recebimento.listaOrdemservico ordemservico with ordemservico.ordemtipo.id in (1, 2)")
				.openParentheses()
					.where("status=?",Recebimentostatus.EM_ANDAMENTO)
					.or()
					.openParentheses()
						.openParentheses()
							.where("status=?",Recebimentostatus.EM_ENDERECAMENTO)
							.or()
							.where("status=?",Recebimentostatus.ENDERECADO)
						.closeParentheses()
						.openParentheses()
							.where("ordemservico.ordemstatus=?", Ordemstatus.EM_ABERTO)
							.or()
							.where("ordemservico.ordemstatus=?", Ordemstatus.EM_EXECUCAO)
						.closeParentheses()
					.closeParentheses()
				.closeParentheses()
				.where("recebimento.deposito = ?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
				.orderBy("recebimento.cdrecebimento DESC")
				.list();
	}
	
	/**
	 * Atualiza o status do recebimento e a data e usuário (exceto quando estiver atualizando o status para ENDERECADO.
	 * 
	 * @author Pedro Gonçalves
	 * @author Giovane Freitas
	 * @param recebimento
	 */
	public void gravaStatusRecebimento(Recebimento recebimento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null || 
				recebimento.getRecebimentostatus() == null || recebimento.getRecebimentostatus().getCdrecebimentostatus() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		if (recebimento.getUsuariofinalizacao() != null && recebimento.getDtfinalizacao() != null &&
				!Recebimentostatus.ENDERECADO.equals(recebimento.getRecebimentostatus()) && 
				!Recebimentostatus.EM_ENDERECAMENTO.equals(recebimento.getRecebimentostatus())){
			
			getHibernateTemplate().bulkUpdate("update Recebimento recebimento " +
					"set recebimento.recebimentostatus.id=?, recebimento.dtfinalizacao=?, recebimento.usuariofinalizacao.id=? where recebimento.id=? ", 
					new Object[]{recebimento.getRecebimentostatus().getCdrecebimentostatus(),
								recebimento.getDtfinalizacao(),
								recebimento.getUsuariofinalizacao().getCdpessoa(),
								recebimento.getCdrecebimento()});
		}else{
			getHibernateTemplate().bulkUpdate("update Recebimento recebimento set recebimento.recebimentostatus.id=? where recebimento.id=? ", 
					new Object[]{recebimento.getRecebimentostatus().getCdrecebimentostatus(),recebimento.getCdrecebimento()});
		}
	}
	
	public void gravaStatusRecebimentoDireto(Recebimento recebimento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null || 
				recebimento.getRecebimentostatus() == null || recebimento.getRecebimentostatus().getCdrecebimentostatus() == null)
			throw new WmsException("Parâmetros inválidos.");
		
			getHibernateTemplate().bulkUpdate("update Recebimento recebimento " +
					"set recebimento.recebimentostatus.id=?, recebimento.dtfinalizacao=?, recebimento.usuariofinalizacao.id=? where recebimento.id=? ", 
					new Object[]{recebimento.getRecebimentostatus().getCdrecebimentostatus(),
					recebimento.getDtfinalizacao(),
					recebimento.getUsuariofinalizacao().getCdpessoa(),
					recebimento.getCdrecebimento()});
	}
	
	@Override
	public Recebimento load(Recebimento bean) {
		return query()
				.joinFetch("recebimento.recebimentostatus status")
				.joinFetch("recebimento.tipoenderecamento tipoenderecamento")
				.leftOuterJoinFetch("recebimento.deposito deposito")
				.leftOuterJoinFetch("recebimento.tipoveiculo tipoveiculo")
				.entity(bean)
				.unique();
	}
	
	/**
	 * 
	 * Método que recupera todos os recebimentos não calculados.
	 * 
	 * @return
	 * @author Arantes 
	 */
	public List<Recebimento> findRecebimentosNaoCalculados() {
		return query()
					.select("recebimento.cdrecebimento, recebimento.cdrecebimento")
					.where("recebimento.valordescarga is null")
					.join("recebimento.recebimentostatus recebimentostatus")
					.openParentheses()
						.where("recebimentostatus=?",Recebimentostatus.CONCLUIDO)
						.or()
						.where("recebimentostatus=?",Recebimentostatus.CONCLUIDO_COM_DIVERGENCIAS)
					.closeParentheses()
					.where("recebimento.deposito=?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
					.orderBy("recebimento.cdrecebimento")
					.list();
	}
	
	/**
	 * 
	 * Verifica se recebimento está concluído com divergência
	 * 
	 * @param filtro
	 * @return
	 * 
	 * @author Arantes
	 * 
	 */
	public Boolean isconcluidodivergencia(Recebimento filtro) {
		Recebimento recebimento = query()
									.join("recebimento.recebimentostatus recebimentostatus")
									.where("recebimento = ?", filtro)
									.where("recebimentostatus = ?", Recebimentostatus.CONCLUIDO_COM_DIVERGENCIAS)
									.setMaxResults(1)
									.unique();
		
		return (recebimento != null) ? Boolean.TRUE: Boolean.FALSE;
	}
	
	/**
	 * Encontra o recebimento em aberto a partir do box. O recebimento status deve ser em andamento, disponível ou em endereçamento.
	 * 
	 * @param box
	 * @return
	 * @author Pedro Gonçalves
	 */
	public Recebimento findRecebimentoByBoxRF(Recebimento recebimento,Deposito deposito){
		if(recebimento == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
				.select("recebimento.cdrecebimento,nfe.veiculo,box.nome, recebimentostatus.cdrecebimentostatus")
				.join("recebimento.box box")
				.join("recebimento.deposito deposito")
				.join("recebimento.recebimentostatus recebimentostatus")
				.leftOuterJoin("recebimento.listaRecebimentoNF rnf")
				.leftOuterJoin("rnf.notafiscalentrada nfe")
				.where("recebimento=?",recebimento)
				.where("deposito=?",deposito)
				.openParentheses()
					.where("recebimentostatus=?",Recebimentostatus.EM_ANDAMENTO)
					.or()
					.where("recebimentostatus=?",Recebimentostatus.DISPONIVEL)
					.or()
					.where("recebimentostatus=?",Recebimentostatus.EM_ENDERECAMENTO)
					.or()
					.where("recebimentostatus=?",Recebimentostatus.ENDERECADO)
				.closeParentheses()
				.unique();
	}
	
	/**
	 * 
	 * Método que recupera uma lista de recebimentos
	 * 
	 * @author Arantes
	 * 
	 * @param recebimento
	 * @return List<Recebimento>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Recebimento> findByRecebimento(EmitirDivergenciaRecebimentoFiltro filtro) {
		if(filtro == null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();
		
		List<Object> args = new ArrayList<Object>();
		
		sql.append("select distinct r.cdrecebimento, r.dtrecebimento,rnf.cdrecebimentonotafiscal, nfe.numero ");
		sql.append("from ordemservico os  ");
		sql.append("  join ordemprodutohistorico oph on oph.cdordemservico = os.cdordemservico ");
		sql.append("  join ordemservicoproduto osp on osp.cdordemservicoproduto = oph.cdordemservicoproduto ");
		sql.append("  join recebimento r on r.cdrecebimento = os.cdrecebimento ");
		sql.append("  join recebimentonotafiscal rnf on rnf.cdrecebimento = r.cdrecebimento ");
		sql.append("  join notafiscalentrada nfe on rnf.cdnotafiscalentrada = nfe.cdnotafiscalentrada ");
		sql.append("where osp.qtdeesperada <> (oph.qtde + oph.qtdeavaria + oph.qtdefracionada) ");

		if (WmsUtil.isFiltraDeposito()){
			sql.append("  and r.cddeposito = ? ");
			args.add(WmsUtil.getDeposito().getCddeposito());
			
			sql.append("  and os.cddeposito = ? ");
			args.add(WmsUtil.getDeposito().getCddeposito());
		}
		
		if (filtro.getRecebimento() != null){
			sql.append("  and r.cdrecebimento = ?");
			args.add(filtro.getRecebimento());
		}
		
		if (filtro.getEmissaode() != null){
			sql.append("  and trunc(r.dtrecebimento) >= trunc(?) ");
			args.add(filtro.getEmissaode());
		}
		
		if (filtro.getEmissaoate() != null){
			sql.append("  and trunc(r.dtrecebimento) <= trunc(?) ");
			args.add(filtro.getEmissaoate());
		}

		if (filtro.getFornecedor() != null){
			sql.append("  and nfe.cdpessoa = ? ");
			args.add(filtro.getFornecedor().getCdpessoa());
		}

		if (filtro.getNotafiscal() != null && !filtro.getNotafiscal().trim().isEmpty()){
			sql.append("  and nfe.numero = ? ");
			args.add(filtro.getNotafiscal());
		}
		
		sql.append("  and oph.cdordemprodutohistorico = ( ");
		sql.append("      select max(oph2.cdordemprodutohistorico) ");
		sql.append("			from Ordemprodutohistorico oph2 ");
		sql.append("			where oph2.cdordemservicoproduto = osp.cdordemservicoproduto ");
		sql.append(") ");

		return (List<Recebimento>) getJdbcTemplate().query(sql.toString(), args.toArray() , new ResultSetExtractor(){
			
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				Map<Integer, Recebimento> listaRecebimento = new HashMap<Integer, Recebimento>();

				while (rs.next()){
					Recebimento recebimento = listaRecebimento.get(rs.getInt("cdrecebimento"));

					if (recebimento == null){
						recebimento = new Recebimento();
						recebimento.setCdrecebimento(rs.getInt("cdrecebimento"));
						recebimento.setDtrecebimento(rs.getTimestamp("dtrecebimento"));
						listaRecebimento.put(recebimento.getCdrecebimento(), recebimento);
					}
					
					Recebimentonotafiscal rnf = new Recebimentonotafiscal();
					rnf.setCdrecebimentonotafiscal(rs.getInt("cdrecebimentonotafiscal"));
					rnf.setNotafiscalentrada(new Notafiscalentrada());
					rnf.getNotafiscalentrada().setNumero(rs.getString("numero"));
					recebimento.getListaRecebimentoNF().add(rnf);
				}
				
				return new ArrayList<Recebimento>(listaRecebimento.values());
			}
			
		});
		
	}
	
	/**
	 * Busca os dados do recebimento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param recebimento
	 * @return
	 */
	public Recebimento findByRecebimento(Recebimento recebimento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("O recebimento não deve ser nulo.");
		return query()
					.select("recebimento.cdrecebimento, recebimento.saidaliberada, recebimentostatus.cdrecebimentostatus,recebimentostatus.nome," +
							"box.cdbox,box.nome,notafiscalentrada.cdnotafiscalentrada,notafiscalentrada.transportador," +
							"notafiscalentrada.numero," +
							"notafiscalentrada.veiculo,listaRecebimentoNF.cdrecebimentonotafiscal,tipoenderecamento.cdtipoenderecamento," +
							"tipoenderecamento.nome,recebimento.dtfinalizacao,usuariofinalizacao.cdpessoa,usuariofinalizacao.nome")
					.leftOuterJoin("recebimento.listaRecebimentoNF listaRecebimentoNF")
					.leftOuterJoin("listaRecebimentoNF.notafiscalentrada notafiscalentrada")
					.leftOuterJoin("recebimento.tipoenderecamento tipoenderecamento")
					.leftOuterJoin("recebimento.usuariofinalizacao usuariofinalizacao")
					.join("recebimento.recebimentostatus recebimentostatus")
					.join("recebimento.box box")
					.where("recebimento = ?",recebimento)
					.unique();
	}
	
	/**
	 * Executa a procedure enderecar_recebimento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param recebimento
	 * @param forcarUmaManual Forçar criação de etiquetas de UMA quando for endereçamento manual.
	 * @throws SQLException 
	 */
	public void enderecarRecebimento(Recebimento recebimento,Integer automatico, boolean forcarUmaManual) throws SQLException {
		if(recebimento == null || recebimento.getCdrecebimento() == null || automatico == null || automatico > 1)
			throw new WmsException("Erro ao executar a procedure enderecar_recebimento.");
		Connection conn = getJdbcTemplate().getDataSource().getConnection();
		CallableStatement cstmt = null;  
		try{
			cstmt = (CallableStatement) conn.prepareCall("BEGIN ENDERECAR_RECEBIMENTO(:1, :2, :3, :4); END;");
			cstmt.setInt(1, WmsUtil.getDeposito().getCddeposito());
			cstmt.setInt(2, recebimento.getCdrecebimento());
			cstmt.setInt(3, automatico);
			
			if (forcarUmaManual)
				cstmt.setInt(4, 1);
			else
				cstmt.setInt(4, 0);
			
			cstmt.execute();
		}catch(Exception e){
			e.printStackTrace();

			Pattern padrao = Pattern.compile(".*###(.*)###.*",Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
			Matcher matcher = padrao.matcher(e.getMessage());
			String msgRetorno;
			if (matcher.matches()) {
				msgRetorno = matcher.group(1);
			}
			else {
				msgRetorno = e.getMessage();
			}

			
			throw new WmsException("Erro ao gerar o endereçamento.\n" + msgRetorno);
		}
		finally{
			cstmt.close();
			conn.close();
		}
	}
	
	/* singleton */
	private static RecebimentoDAO instance;
	public static RecebimentoDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(RecebimentoDAO.class);
		}
		return instance;
	}
	
	/**
	 * Busca o recebimento e os dados que deverão ser mostrados na tela
	 * de endereçamento no coletor.
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param recebimento
	 * @return
	 */
	public Recebimento findForEnderecamento(Recebimento recebimento,Deposito deposito,Tipoenderecamento tipoenderecamento) {
		
		if(recebimento == null || recebimento.getCdrecebimento() == null || deposito == null || deposito.getCddeposito() == null
				|| tipoenderecamento == null || tipoenderecamento.getCdtipoenderecamento() == null)
			throw new WmsException("O recebimento não deve ser nulo.");
		
		QueryBuilder<Recebimento> query = query()
					.select("recebimento.cdrecebimento, box.cdbox,box.nome,nfe.cdnotafiscalentrada,nfe.veiculo," +
							"tipoenderecamento.cdtipoenderecamento,recebimentostatus.cdrecebimentostatus")
					.leftOuterJoin("recebimento.listaRecebimentoNF rnf")
					.leftOuterJoin("rnf.notafiscalentrada nfe")
					.join("recebimento.recebimentostatus recebimentostatus")
					.join("recebimento.box box")
					.join("recebimento.tipoenderecamento tipoenderecamento")
					.join("recebimento.listaOrdemservico os")
					.join("recebimento.deposito deposito")
					.join("os.ordemtipo ordemtipo")
					.join("os.ordemstatus ordemstatus")
					.where("recebimento = ?",recebimento)
					.where("deposito = ?", deposito)
					.where("tipoenderecamento = ?",tipoenderecamento)
					.openParentheses()
						.openParentheses();
		
		if(tipoenderecamento.equals(Tipoenderecamento.AUTOMATICO)){
			query
					.where("ordemtipo = ?",Ordemtipo.ENDERECAMENTO_FRACIONADO)
					.or()
					.where("ordemtipo = ?",Ordemtipo.ENDERECAMENTO_AVARIADO)
					.or()
					.where("ordemtipo = ?",Ordemtipo.ENDERECAMENTO_PADRAO)
				.closeParentheses()
				.where("ordemstatus <> ?",Ordemstatus.FINALIZADO_SUCESSO);
		}else{
			query
					.openParentheses()
						.where("ordemtipo = ?",Ordemtipo.ENDERECAMENTO_FRACIONADO)
						.or()
						.where("ordemtipo = ?",Ordemtipo.ENDERECAMENTO_AVARIADO)
						.or()
						.where("ordemtipo = ?",Ordemtipo.ENDERECAMENTO_PADRAO)
					.closeParentheses()
					.where("ordemstatus <> ?",Ordemstatus.FINALIZADO_SUCESSO)
				.closeParentheses()
				.or()
				.where("exists(select oph.cdordemprodutohistorico from Ordemprodutohistorico oph " +
					"where oph.qtde is not null and oph.qtde > 0 and oph.cdordemprodutohistorico = (" +
					"select max(oph2.cdordemprodutohistorico) from Ordemprodutohistorico oph2 " +
					"where oph2.ordemservico = os))");
		}
		
		return query
					.closeParentheses()
					.setMaxResults(1)
					.unique()
					;
	}

	/**
	 * Localiza um recebimento a partir da ordem de serviço.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Recebimento findByOrdemservico(Ordemservico ordemservico) {
		return query().select("recebimento.cdrecebimento,tipoenderecamento.cdtipoenderecamento")
						.join("recebimento.tipoenderecamento tipoenderecamento")
						.join("recebimento.listaOrdemservico ordemservico")
						.where("ordemservico=?", ordemservico).unique();
	}	
	
	/**
	 * Método que verifica se as ordens de serviço do recebimentos estão finalizadas.
	 * Caso todas estejam finalizadas o valor da contagem é 0, caso contrário existem ordens em aberto
	 * 
	 * @param recebimento
	 * @return
	 * @author Tomás Rabelo
	 */
	public boolean isOrdensServicoDoRecebimentoFinalizadas(final Recebimento recebimento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Parametros inválidos.");
		
		final String sql = "SELECT count(*) as count "+
					 "FROM Recebimento r "+ 
					 "JOIN Ordemservico os ON os.cdrecebimento = r.cdrecebimento "+ 
					 "WHERE r.cdrecebimento = :cdrecebimento and os.cdordemservico NOT IN "+ 
					 	"(SELECT os1.cdordemservico "+
					 	"FROM Ordemservico os1 "+ 
					 	"JOIN Recebimento r1 ON os1.cdrecebimento = r1.cdrecebimento "+ 
					 	"JOIN Ordemstatus ost ON ost.cdordemstatus = os1.cdordemstatus "+ 
					 	"WHERE  ost.cdordemstatus = 3 OR ost.cdordemstatus = 4 OR ost.cdordemstatus = 5)";
		
		return (Boolean) getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				query.addScalar("count", new LongType());
				query.setParameter("cdrecebimento", recebimento.getCdrecebimento());
				Long result = (Long) query.uniqueResult();
				return (result == null || result.longValue() == 0L) ? Boolean.TRUE : Boolean.FALSE;
			}
		});
	}

	/**
	 * Método que verifica se o recebimento esta elegivel para cobrança de descarga
	 * 
	 * @param recebimento
	 * @return
	 * @author Tomás Rabelo
	 */
	public boolean isRecebimentoAvailableForCobrancaDescarga(Recebimento recebimento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Parametros inválidos.");
		
		QueryBuilder<Long> query = newQueryBuilderWithFrom(Long.class)
			.select("count(*)")
			.join("recebimento.deposito deposito with deposito.id = "+WmsUtil.getDeposito().getCddeposito())
			.join("recebimento.recebimentostatus recebimentostatus")
			.where("recebimento = ?", recebimento)
			.openParentheses()
				.where("recebimentostatus <> ?", Recebimentostatus.CANCELADO)
				.where("recebimentostatus <> ?", Recebimentostatus.REJEITADO)
			.closeParentheses()
			.where("recebimento.valordescarga is null");
		
		return query.unique() > 0L;
	}
	
	/**
	 * Chama a procedure 'atualizar_valormedio' no banco de dados do sistema
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param enderecoproduto
	 * @param carregamento
	 * @throws SQLException 
	 */
	public void atualizarValorMedio(final Recebimento recebimento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Dados insuficientes para invocar a função 'atualizar_valormedio'");

		try{
			getHibernateTemplate().execute(new HibernateCallback(){

				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					session.getNamedQuery("atualizar_valormedio")
						.setInteger(0, recebimento.getCdrecebimento())
						.executeUpdate();
					
					return null;
				}
				
			});
		}catch(Exception e){
			throw new WmsException("Erro ao atualizar o valor médio dos produtos.", e);
		}
	}
	
	/**
	 * Busca os totais recebidos por mês dentro de um semestre.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ResumoAgendaverba> findResumoRecebimento(Deposito deposito, Date exercicio){
		if (deposito == null || exercicio == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Object> args = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct cla.numero AS classeproduto, Trunc(r.dtfinalizacao, 'MM') AS mes, Sum(nfep.valor * nfep.qtde) AS valor ");
		sql.append("FROM recebimento r ");
		sql.append("  join recebimentonotafiscal rnf ON rnf.cdrecebimento = r.cdrecebimento ");
		sql.append("  join notafiscalentrada nfe ON rnf.cdnotafiscalentrada = nfe.cdnotafiscalentrada ");
		sql.append("  join notafiscalentradaproduto nfep ON nfep.cdnotafiscalentrada = rnf.cdnotafiscalentrada ");
		sql.append("  join produto p ON p.cdproduto = nfep.cdproduto  ");
		sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
		sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
		sql.append("WHERE trunc(dtfinalizacao, 'MM') >= ? AND trunc(dtfinalizacao, 'MM') <= ? ");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(exercicio);
		calendar.add(Calendar.MONTH, 6);
		
		args.add(exercicio);
		args.add(calendar.getTime());

		if (deposito.getCddeposito() > 0){
			sql.append(" AND r.cddeposito = ? ");
			args.add(deposito.getCddeposito());
		}

		sql.append("  AND nfe.cdpedidocompra is not null  AND r.cdrecebimentostatus in (3,4,7,8) ");
		sql.append("GROUP BY cla.numero, Trunc(r.dtfinalizacao, 'MM') ");
		
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
	public List<ResumoAgendaverba> findResumoRecebimentoVerbaNovo(Deposito deposito, Date exercicio){
		if (exercicio == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Object> args = new ArrayList<Object>();
		
		/*SELECT pc.numero AS classeproduto, Trunc(dtfinalizacao, 'MM') AS mes, Sum(nfep.valor * nfep.qtde) AS valor 
		FROM recebimento r 
		 join recebimentonotafiscal rnf ON rnf.cdrecebimento = r.cdrecebimento 
		 join notafiscalentrada nfe ON rnf.cdnotafiscalentrada = nfe.cdnotafiscalentrada 
		 join notafiscalentradaproduto nfep ON nfep.cdnotafiscalentrada = rnf.cdnotafiscalentrada 
		 join produto p ON p.cdproduto = nfep.cdproduto 
		 join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse 
		 join produtoclasse pc on pc.cdprodutoclasse = v.cdclasse
		 join agendapedido ap on ap.cdpedidocompra = nfe.cdpedidocompra
		 join agenda a on (a.cdagenda = ap.cdagenda)
		WHERE trunc(dtfinalizacao, 'MM') = trunc(to_date('01/04/2011'), 'MM') 
		AND r.cdrecebimentostatus in (3,4,7,8) 
		--and trunc(dtprevisao, 'MM') < trunc(to_date('01/04/2011'), 'MM') 
		GROUP BY pc.numero, Trunc(dtfinalizacao, 'MM')
		*/
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct cla.numero AS classeproduto, Trunc(dtfinalizacao, 'MM') AS mes, Sum(nfep.valor * nfep.qtde) AS valor ");
		sql.append("FROM recebimento r ");
		sql.append("  join recebimentonotafiscal rnf ON rnf.cdrecebimento = r.cdrecebimento ");
		sql.append("  join notafiscalentrada nfe ON rnf.cdnotafiscalentrada = nfe.cdnotafiscalentrada ");
		sql.append("  join notafiscalentradaproduto nfep ON nfep.cdnotafiscalentrada = rnf.cdnotafiscalentrada ");
		sql.append("  join produto p ON p.cdproduto = nfep.cdproduto  ");
		sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
		sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
//		sql.append("  join agendapedido ap on ap.cdpedidocompra = nfe.cdpedidocompra ");
//		sql.append("  join agenda a on (a.cdagenda = ap.cdagenda and a.cdagenda = nfe.cdagenda) ");
		sql.append("  join agenda a on (a.cdagenda = nfe.cdagenda) ");
		sql.append("  join deposito d ON r.cddeposito = d.cddeposito ");
//		sql.append(" WHERE trunc(dtfinalizacao, 'MM') = trunc( ? , 'MM') ");		
		sql.append("WHERE trunc(dtfinalizacao, 'MM') >= trunc( ? , 'MM') AND trunc(dtfinalizacao, 'MM') <= trunc( ? , 'MM') ");
		sql.append(" AND d.ativo = 1 ");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(exercicio);
		calendar.add(Calendar.MONTH, 6);
		
		args.add(exercicio);
		args.add(calendar.getTime());
		
		if (deposito != null &&  deposito.getCddeposito() > 0){
			sql.append(" AND r.cddeposito = ? ");
			args.add(deposito.getCddeposito());
		}
		
		sql.append("  AND nfe.cdpedidocompra is not null  AND r.cdrecebimentostatus in (3,4,7,8) ");
		sql.append(" GROUP BY cla.numero, Trunc(dtfinalizacao, 'MM') ");
		
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
	public List<ResumoAgendaverba> findResumoRecebimentoFinanceiroNovo(Deposito deposito, Date dtagenda, Date dtprevisao){
		if (dtagenda == null || dtprevisao == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Object> args = new ArrayList<Object>();
		
		/*
		 SELECT pc.numero AS classeproduto, Trunc(dtprevisao, 'MM') AS mes, Sum(nfep.valor * nfep.qtde) AS valor 
			FROM recebimento r 
			 join recebimentonotafiscal rnf ON rnf.cdrecebimento = r.cdrecebimento 
			 join notafiscalentrada nfe ON rnf.cdnotafiscalentrada = nfe.cdnotafiscalentrada 
			 join notafiscalentradaproduto nfep ON nfep.cdnotafiscalentrada = rnf.cdnotafiscalentrada 
			 join produto p ON p.cdproduto = nfep.cdproduto 
			 join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse 
			 join produtoclasse pc on pc.cdprodutoclasse = v.cdclasse
			 join agendapedido ap on ap.cdpedidocompra = nfe.cdpedidocompra
			 join agenda a on (a.cdagenda = ap.cdagenda)
			WHERE trunc(dtfinalizacao, 'MM') = trunc(to_date('01/04/2011'), 'MM') 
			AND r.cdrecebimentostatus in (3,4,7,8) 
			and trunc(dtprevisao, 'MM') = trunc(to_date('01/04/2011'), 'MM') 
			GROUP BY pc.numero, Trunc(dtprevisao, 'MM')

*/

		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cla.numero AS classeproduto, Trunc(dtprevisao, 'MM') AS mes, Sum(nfep.valor * nfep.qtde) AS valor ");
		sql.append(" FROM recebimento r ");
		sql.append("  join recebimentonotafiscal rnf ON rnf.cdrecebimento = r.cdrecebimento ");
		sql.append("  join notafiscalentrada nfe ON rnf.cdnotafiscalentrada = nfe.cdnotafiscalentrada ");
		sql.append("  join notafiscalentradaproduto nfep ON nfep.cdnotafiscalentrada = rnf.cdnotafiscalentrada ");
		sql.append("  join produto p ON p.cdproduto = nfep.cdproduto  ");
		sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
		sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
//		sql.append("  join agendapedido ap on ap.cdpedidocompra = nfe.cdpedidocompra ");
//		sql.append("  join agenda a on (a.cdagenda = ap.cdagenda and a.cdagenda = nfe.cdagenda) ");
		sql.append("  join agenda a on (a.cdagenda = nfe.cdagenda)");
		sql.append("  WHERE trunc(dtfinalizacao, 'MM') = trunc( ? , 'MM')  ");
		sql.append("  AND r.cdrecebimentostatus in (3,4,7,8)  ");
		sql.append("  and trunc(dtprevisao, 'MM') = trunc( ? , 'MM') ");
		
		args.add(dtagenda);
		args.add(dtprevisao);
		
		if (deposito != null && deposito.getCddeposito() > 0){
			sql.append(" AND cddeposito = ? ");
			args.add(deposito.getCddeposito());
		}
		
		//sql.append("  AND nfe.cdpedidocompra is not null  AND r.cdrecebimentostatus in (3,4,7,8) ");
		sql.append("GROUP BY cla.numero, Trunc(dtprevisao, 'MM') ");
		
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
	public List<ResumoAgendaverba> findResumoRecebido(Deposito deposito, Date exercicio){
		if (exercicio == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Object> args = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cla.numero AS classeproduto, Trunc(r.dtfinalizacao, 'MM') AS mes, Sum(nfep.valor * nfep.qtde) AS valor ");
		sql.append("FROM recebimento r ");
		sql.append("  join recebimentonotafiscal rnf ON rnf.cdrecebimento = r.cdrecebimento ");
		sql.append("  join notafiscalentrada nfe ON rnf.cdnotafiscalentrada = nfe.cdnotafiscalentrada ");
		sql.append("  join notafiscalentradaproduto nfep ON nfep.cdnotafiscalentrada = rnf.cdnotafiscalentrada ");
		sql.append("  join produto p ON p.cdproduto = nfep.cdproduto  ");
		sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
		sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
		sql.append("WHERE trunc(dtfinalizacao, 'MM') >= ? AND trunc(dtfinalizacao, 'MM') <= ? ");
		
		/*Calendar calendar = Calendar.getInstance();
		calendar.setTime(exercicio);
		calendar.add(Calendar.MONTH, 0);*/
		
		args.add(WmsUtil.firstDateOfMonth(exercicio));
		args.add(WmsUtil.lastDateOfMonth(exercicio));
		
		if (deposito != null && deposito.getCddeposito() > 0){
			sql.append(" AND cddeposito = ? ");
			args.add(deposito.getCddeposito());
		}
		
		sql.append("  AND nfe.cdpedidocompra is not null  AND r.cdrecebimentostatus in (3,4,7,8) ");
		sql.append("GROUP BY cla.numero, Trunc(r.dtfinalizacao, 'MM') ");
		
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
	
	public void updateRecebimento(Recebimento recebimento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("update Recebimento set dtsincronizacao=SYSDATE where cdrecebimento=?", 
				new Object[]{recebimento.getCdrecebimento()});
	}

	/** Queiroz - 22/07/13 - CRIANDO UMA FUNCAO PARA REPROVAR A SOLICITAÇÃO
	 * @param cdpredisponivelexcessao
	 */
	public String callLiberaVeiculo(Integer tipo,Recebimento recebimento) throws SQLException {
		String msgRetorno = "";
		
		if(recebimento == null) {
			throw new WmsException("Dados insuficientes para invocar a função 'LIBERA VEICULO'");
		}
		
		Connection conn = getJdbcTemplate().getDataSource().getConnection();
		CallableStatement cstmt = null;  
		try {
			cstmt = (CallableStatement) conn.prepareCall("BEGIN LIBERA_VEICULO(:1,:2,:3); END;");
			cstmt.setInt(1,tipo);
			cstmt.setInt(2,recebimento.getCdrecebimento());
			cstmt.registerOutParameter(3, Types.VARCHAR);
			cstmt.execute();
			msgRetorno =cstmt.getString(3);  
		}
		catch(SQLException e){
			e.printStackTrace();
			msgRetorno = "Erro ao fazer a chamada da funcao LIBERA_VEICULO";
		}
		finally {
			cstmt.close();
			conn.close();
		}
		if (msgRetorno ==null || msgRetorno.equals("OK")){
			msgRetorno = "";
		}
		return msgRetorno;
	}
	
}