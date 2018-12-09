package br.com.linkcom.wms.geral.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculo;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculostatus;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.AcompanhamentoveiculoFiltro;
import br.com.linkcom.wms.util.DateUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;
import oracle.jdbc.driver.OracleTypes;

public class AcompanhamentoveiculoDAO extends GenericDAO<Acompanhamentoveiculo>{
	
	public Object getPorcentagens(AcompanhamentoveiculoFiltro filtro) {
		return null;
	}
	
	@Override
	public void updateListagemQuery(QueryBuilder<Acompanhamentoveiculo> query,FiltroListagem _filtro) {
		
		AcompanhamentoveiculoFiltro filtro = (AcompanhamentoveiculoFiltro) _filtro;
		if(filtro==null){
			filtro = new AcompanhamentoveiculoFiltro();
		}
		
		query.select("acompanhamentoveiculo.cdacompanhamentoveiculo, acompanhamentoveiculo.numerorav," +
				"acompanhamentoveiculo.nomemotorista, acompanhamentoveiculo.cnhmotorista, acompanhamentoveiculo.primeirotelefone," +
				"acompanhamentoveiculo.segundotelefone, acompanhamentoveiculo.terceirotelefone, acompanhamentoveiculo.placaveiculo, "+
				"acompanhamentoveiculo.dataentrada, acompanhamentoveiculo.datasaida, acompanhamentoveiculo.numeronota, " +
				"acompanhamentoveiculo.temDevolucao, agendaacompanhamentoveiculo.cdagenda, tipoveiculo.cdtipoveiculo, tipoveiculo.nome," +
				"deposito.cddeposito, deposito.nome")
			.join("acompanhamentoveiculo.acompanhamentoveiculostatus acompanhamentoveiculostatus")
			
			.join("acompanhamentoveiculo.deposito deposito")
			.join("deposito.listaUsuarioDeposito usuarioDeposito")
			.join("usuarioDeposito.usuario usuario")
			
			.leftOuterJoin("acompanhamentoveiculo.listaAgendaacompanhamentoveiculo agendaacompanhamentoveiculo")
			.leftOuterJoin("acompanhamentoveiculo.tipoveiculo tipoveiculo")
			.where("acompanhamentoveiculo.numerorav = ?",filtro.getNumerorav())
			.where("acompanhamentoveiculostatus = ?",filtro.getAcompanhamentoveiculostatus());
			
			if(filtro.getPlaca()!=null && !filtro.getPlaca().isEmpty()){
				query.where("UPPER(acompanhamentoveiculo.placaveiculo) = ?",filtro.getPlaca().toUpperCase());
			}
		
			if(filtro.getDeposito()!=null && filtro.getDeposito().getCddeposito()!=null)
				query.where("deposito = ?",filtro.getDeposito());
			else 
				query.where("deposito = ?",WmsUtil.getDeposito());
					
			if(filtro.getDtinicio()!=null)
				query.where("acompanhamentoveiculo.dataentrada >= ?",DateUtil.dataToBeginOfDay(filtro.getDtinicio()));
			if(filtro.getDtfim()!=null)
				query.where("acompanhamentoveiculo.dataentrada < ?",DateUtil.incrementaDia(filtro.getDtfim(), 1));
			
			query.where("usuario = ?",WmsUtil.getUsuarioLogado());
			
			query.orderBy("acompanhamentoveiculo.cdacompanhamentoveiculo desc");
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Acompanhamentoveiculo> query) {
		query.select("acompanhamentoveiculo.cdacompanhamentoveiculo, acompanhamentoveiculo.numerorav," +
				"acompanhamentoveiculo.nomemotorista, acompanhamentoveiculo.cnhmotorista, acompanhamentoveiculo.primeirotelefone," +
				"acompanhamentoveiculo.segundotelefone, acompanhamentoveiculo.terceirotelefone, acompanhamentoveiculo.placaveiculo, "+
				"acompanhamentoveiculo.dataentrada, acompanhamentoveiculo.datasaida, acompanhamentoveiculo.numeronota, " +
				"acompanhamentoveiculo.temDevolucao, agendaacompanhamentoveiculo.cdagenda, tipoveiculo.cdtipoveiculo, tipoveiculo.nome," +
				"deposito.cddeposito, deposito.nome, acompanhamentoveiculohistorico.cdravhistorico, acompanhamentoveiculohistorico.dtaltera," +
				"acompanhamentoveiculohistorico.descricao, usuario_historico.nome, recebimento_historico.cdrecebimento, " +
				"acompanhamentoveiculostatus_historico.nome, acompanhamentoveiculostatus_historico.cdstatusrav, " +
				"acompanhamentoveiculo_historico.numerorav, acompanhamentoveiculostatus.cdstatusrav ")
			.leftOuterJoin("acompanhamentoveiculo.listaAgendaacompanhamentoveiculo agendaacompanhamentoveiculo")
			.leftOuterJoin("acompanhamentoveiculo.tipoveiculo tipoveiculo")
			.leftOuterJoin("acompanhamentoveiculo.deposito deposito")
			.leftOuterJoin("acompanhamentoveiculo.recebimento recebimento")
			.leftOuterJoin("acompanhamentoveiculo.acompanhamentoveiculostatus acompanhamentoveiculostatus")
			.leftOuterJoin("acompanhamentoveiculo.listAcompanhamentoveiculohistorico acompanhamentoveiculohistorico")
			.leftOuterJoin("acompanhamentoveiculohistorico.usuarioAltera usuario_historico")
			.leftOuterJoin("acompanhamentoveiculohistorico.recebimento recebimento_historico")
			.leftOuterJoin("acompanhamentoveiculohistorico.acompanhamentoveiculo acompanhamentoveiculo_historico")
			.leftOuterJoin("acompanhamentoveiculohistorico.acompanhamentoveiculostatus acompanhamentoveiculostatus_historico")
			.orderBy("acompanhamentoveiculohistorico.dtaltera");
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManagedNormal("listaAgendaacompanhamentoveiculo", "acompanhamentoveiculo");
	}
	
	@SuppressWarnings("unchecked")
	public List<Acompanhamentoveiculo> FindEtiquetaEntrada(Integer pcdrecebimento) {

		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT DISTINCT AV.DT_INCLUSAO  as   DATA, ");
		sql.append(" 	AV.NUMERORAV,  ");
		sql.append(" 	RC.CDRECEBIMENTO,  ");
		sql.append(" 	B.NOME as BOX,  ");
		sql.append(" 	AV.PLACAVEICULO, ");
		sql.append(" 	AV.NOMEMOTORISTA, ");
		sql.append(" 	AV.CNHMOTORISTA, ");
		sql.append(" 	AV.TEMDEVOLUCAO, ");
		sql.append(" 	AV.PRIMEIROTELEFONE, ");
		sql.append(" 	AV.NUMERONOTA, ");
		sql.append(" 	TP.NOME AS TIPOVEICULONOME, ");
		sql.append(" 	LISTAGG(AGV.CDAGENDA, ',') WITHIN GROUP (ORDER BY AGV.CDAGENDA) AS SENHA");
		sql.append(" FROM ACOMPANHAMENTOVEICULO       AV ");
		sql.append(" JOIN RECEBIMENTO                 RC ON RC.CDRECEBIMENTO = AV.CDRECEBIMENTO ");
		sql.append(" JOIN BOX                         B ON B.CDBOX = RC.CDBOX ");
		sql.append(" LEFT OUTER JOIN AGENDAACOMPANHAMENTOVEICULO AGV ON AV.CDACOMPANHAMENTOVEICULO = AGV.CDACOMPANHAMENTOVEICULO ");
		sql.append(" LEFT OUTER JOIN TIPOVEICULO 	  TP ON TP.CDTIPOVEICULO = AV.CDTIPOVEICULO ");		
		sql.append(" WHERE AV.CDRECEBIMENTO = ? ");
		sql.append(" GROUP BY AV.DT_INCLUSAO,AV.NUMERORAV,RC.CDRECEBIMENTO,B.NOME,AV.PLACAVEICULO,AV.NOMEMOTORISTA,");
		sql.append(" 		AV.CNHMOTORISTA,AV.TEMDEVOLUCAO,AV.PRIMEIROTELEFONE,AV.NUMERONOTA,TP.NOME ");


		return (List<Acompanhamentoveiculo>) getJdbcTemplate().query(sql.toString(), new Object[]{pcdrecebimento},
			new ResultSetExtractor(){
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Acompanhamentoveiculo> list = new ArrayList<Acompanhamentoveiculo>();
					
					while (rs.next()){
						Acompanhamentoveiculo acompanhamentoveiculo = new Acompanhamentoveiculo();
						acompanhamentoveiculo.setDataCadastro(WmsUtil.stringToDefaulDateFormat(rs.getString("DATA"),"yyyy-MM-dd hh:mm:ss.SSS"));
						acompanhamentoveiculo.setNumerorav(rs.getString("NUMERORAV"));
						acompanhamentoveiculo.setCdrecebimento(rs.getInt("CDRECEBIMENTO"));
						acompanhamentoveiculo.setBox(rs.getString("BOX"));
						acompanhamentoveiculo.setPlacaveiculo(rs.getString("PLACAVEICULO"));
						acompanhamentoveiculo.setNomemotorista(rs.getString("NOMEMOTORISTA"));
						acompanhamentoveiculo.setCnhmotorista(rs.getString("CNHMOTORISTA"));
						acompanhamentoveiculo.setPrimeirotelefone(rs.getString("PRIMEIROTELEFONE"));
						acompanhamentoveiculo.setSenha(rs.getString("SENHA"));
						acompanhamentoveiculo.setTipoVeiculoNome(rs.getString("TIPOVEICULONOME"));
						acompanhamentoveiculo.setNumeronota(rs.getString("NUMERONOTA"));
						
						Integer temDevolucao = rs.getInt("TEMDEVOLUCAO");
						if(temDevolucao!=null && temDevolucao==1)
							acompanhamentoveiculo.setItemDevolvido("SIM");
						else
							acompanhamentoveiculo.setItemDevolvido("NÃO");
						
						list.add(acompanhamentoveiculo);
					}
					return list;
				}
			}
		);
	}
	
	@SuppressWarnings("unchecked")
	public List<Acompanhamentoveiculo> loadAllInfoAcompVeiculo(String numero) {

		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT TO_CHAR(AV.DT_INCLUSAO,'dd/mm/yyyy hh24:mi:ss')  as   DATA, ");
		sql.append("  AV.NUMERORAV,  ");
		sql.append("  RC.CDRECEBIMENTO,  ");
		sql.append("  B.NOME as BOX,  ");
		sql.append("  AV.PLACAVEICULO, ");
		sql.append("  AV.NOMEMOTORISTA, ");
		sql.append("  AV.CNHMOTORISTA, ");
		sql.append("  AV.PRIMEIROTELEFONE, ");
		sql.append("  AGV.CDAGENDA AS SENHA ");
		sql.append(" FROM ACOMPANHAMENTOVEICULO        AV ");
		sql.append(" JOIN AGENDAACOMPANHAMENTOVEICULO AGV ON AV.CDACOMPANHAMENTOVEICULO = AGV.CDACOMPANHAMENTOVEICULO ");
		sql.append(" LEFT JOIN RECEBIMENTO             RC ON RC.CDRECEBIMENTO = AV.CDRECEBIMENTO ");
		sql.append(" LEFT JOIN BOX                      B ON B.CDBOX = RC.CDBOX ");
		sql.append(" WHERE AV.NUMERORAV = ? ");


		return (List<Acompanhamentoveiculo>) getJdbcTemplate().query(sql.toString(), new Object[]{numero},
			new ResultSetExtractor(){
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Acompanhamentoveiculo> list = new ArrayList<Acompanhamentoveiculo>();
					
					while (rs.next()){
						Acompanhamentoveiculo acompanhamentoveiculo = new Acompanhamentoveiculo();
						acompanhamentoveiculo.setDataCadastro(rs.getString("DATA"));
						acompanhamentoveiculo.setNumerorav(rs.getString("NUMERORAV"));
						acompanhamentoveiculo.setCdrecebimento(rs.getInt("CDRECEBIMENTO"));
						acompanhamentoveiculo.setBox(rs.getString("BOX"));
						acompanhamentoveiculo.setPlacaveiculo(rs.getString("PLACAVEICULO"));
						acompanhamentoveiculo.setNomemotorista(rs.getString("NOMEMOTORISTA"));
						acompanhamentoveiculo.setCnhmotorista(rs.getString("CNHMOTORISTA"));
						acompanhamentoveiculo.setPrimeirotelefone(rs.getString("PRIMEIROTELEFONE"));
						acompanhamentoveiculo.setSenha(rs.getString("SENHA"));
						list.add(acompanhamentoveiculo);
					}
					return list;
				}
			}
		);
	}
	
	/** Queiroz - 22/07/13 - CRIANDO UMA FUNCAO PARA ATUALIZAR O RAV-E
	 * 
	 */
	public String callAtualizaRAV(String numerorav,String Tipo,Date data,String Lacre) throws SQLException {
		String msgRetorno = "";
		
		if(numerorav == null) {
			throw new WmsException("Dados insuficientes para invocar a função 'ATUALIZA RAV'");
		}
		
		Connection conn = getJdbcTemplate().getDataSource().getConnection();
		CallableStatement cstmt = null;  
		try {
			cstmt = (CallableStatement) conn.prepareCall("BEGIN  p_atualiza_rav(:1,:2,:3,:4,:5); END;");
			cstmt.setString(1,numerorav);
			cstmt.setDate(2,data);
			cstmt.setString(3, Tipo);
			cstmt.setString(4, Lacre);  
			cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
			cstmt.execute();
			msgRetorno = cstmt.getString(5);
		}
		catch(SQLException e){
			e.printStackTrace();
			msgRetorno = "Erro ao fazer a chamada da funcao REPROVA_PREDISPONIVEL";
		}
		finally {
			cstmt.close();
			conn.close();
		}
		if (msgRetorno.equals("True"))
			msgRetorno = "OK";
		
		return msgRetorno;
	}

	/**
	 * 
	 * @param numeroRav
	 * @return
	 */
	public Acompanhamentoveiculo findByNumeroRav(String numeroRav) {
		return query()
			.select("acompanhamentoveiculo.cdacompanhamentoveiculo, acompanhamentoveiculo.numerorav," +
				"acompanhamentoveiculo.nomemotorista, acompanhamentoveiculo.cnhmotorista, acompanhamentoveiculo.primeirotelefone," +
				"acompanhamentoveiculo.segundotelefone, acompanhamentoveiculo.terceirotelefone, acompanhamentoveiculo.placaveiculo, "+
				"acompanhamentoveiculo.dataentrada, acompanhamentoveiculo.datasaida, acompanhamentoveiculo.numeronota, " +
				"acompanhamentoveiculo.temDevolucao, tipoveiculo.cdtipoveiculo, tipoveiculo.nome, recebimento.cdrecebimento, " +
				"deposito.cddeposito, deposito.nome, acompanhamentoveiculostatus.cdstatusrav, recebimento.saidaliberada ")
			.join("acompanhamentoveiculo.recebimento recebimento")	
			.join("acompanhamentoveiculo.acompanhamentoveiculostatus acompanhamentoveiculostatus")
			.leftOuterJoin("acompanhamentoveiculo.tipoveiculo tipoveiculo")
			.leftOuterJoin("acompanhamentoveiculo.deposito deposito")
			.where("acompanhamentoveiculo.numerorav = ? ",numeroRav)
		.unique();
	}

	/**
	 * 
	 * @param av
	 */
	public void registarSaida(Acompanhamentoveiculo av) {
		Integer id = av.getCdacompanhamentoveiculo();
		getJdbcTemplate().update("update acompanhamentoveiculo set datasaida = SYSDATE, cdstatusrav = 4 where cdacompanhamentoveiculo = "+id);
	}

	/**
	 * 
	 * @param av
	 */
	public void registarEntrada(Acompanhamentoveiculo av) {
		Integer id = av.getCdacompanhamentoveiculo();
		getJdbcTemplate().update("update acompanhamentoveiculo set dataentrada = SYSDATE, cdstatusrav = 1 where cdacompanhamentoveiculo = "+id);
	}

	/**
	 * 
	 * @param recebimento
	 * @param list 
	 */
	public void vincularRecebimento(Recebimento recebimento, List<Acompanhamentoveiculo> list) {
		StringBuilder sql = new StringBuilder();
			sql.append(" update acompanhamentoveiculo set cdrecebimento = ");
			sql.append(recebimento.getCdrecebimento());
			sql.append(" ,cdstatusrav = 2 where cdacompanhamentoveiculo in (");
			sql.append(CollectionsUtil.listAndConcatenate(list, "cdacompanhamentoveiculo", ","));
			sql.append(") "); 
		getJdbcTemplate().update(sql.toString());
	}

	/**
	 * 
	 * @param acompanhamentoveiculo
	 * @return
	 */
	public Acompanhamentoveiculo isVeiculoRecebido(Acompanhamentoveiculo acompanhamentoveiculo) {
		return query()
			.select("acompanhamentoveiculo.cdacompanhamentoveiculo, recebimento.cdrecebimento, acompanhamentoveiculo.numerorav")
			.join("acompanhamentoveiculo.recebimento recebimento")
			.where("acompanhamentoveiculo.cdacompanhamentoveiculo = ?",acompanhamentoveiculo.getCdacompanhamentoveiculo())
			.unique();
	}

	/**
	 * 
	 * @param acompanhamentoveiculo
	 * @return
	 */
	public Acompanhamentoveiculo isVeiculoRecebido(Recebimento recebimento) {
		return query()
			.select("acompanhamentoveiculo.cdacompanhamentoveiculo, recebimento.cdrecebimento, acompanhamentoveiculo.numerorav")
			.join("acompanhamentoveiculo.recebimento recebimento")
			.where("recebimento = ?",recebimento)
			.unique();
	}
	
	/**
	 * 
	 * @param recebimento
	 */
	public void removerRecebimento(Recebimento recebimento) {
		if(recebimento==null || recebimento.getCdrecebimento()==null){
			throw new WmsException("Não é possível remover o vinculo do Recebimento no Veículo. Motivo: Parâmetros Inválidos.");
		}
		getJdbcTemplate().update("update acompanhamentoveiculo set cdrecebimento = null, cdstatusrav = 0, dataentrada = null, datasaida = null, dtentradaportaria=null "
								+"where cdrecebimento = "+recebimento.getCdrecebimento());
	}

	/**
	 * 
	 * @param recebimento
	 * @param temDevolucao 
	 */
	public void checkTemDevolucao(Recebimento recebimento, Integer temDevolucao) {
		if(recebimento==null || recebimento.getCdrecebimento()==null){
			throw new WmsException("Não é possível remover o vinculo do Recebimento no Veículo. Motivo: Parâmetros Inválidos.");
		}
		getJdbcTemplate().update("update acompanhamentoveiculo set temdevolucao = "+temDevolucao+", cdstatusrav = 5 where cdrecebimento = "+recebimento.getCdrecebimento());
	}

	/**
	 * 
	 * @param recebimento
	 * @return
	 */
	public List<Acompanhamentoveiculo> findByRecebimento(Recebimento recebimento) {
		return query()
			.select("acompanhamentoveiculo.cdacompanhamentoveiculo, acompanhamentoveiculo.numerorav," +
				"acompanhamentoveiculo.nomemotorista, acompanhamentoveiculo.cnhmotorista, acompanhamentoveiculo.primeirotelefone," +
				"acompanhamentoveiculo.segundotelefone, acompanhamentoveiculo.terceirotelefone, acompanhamentoveiculo.placaveiculo, "+
				"acompanhamentoveiculo.dataentrada, acompanhamentoveiculo.datasaida, acompanhamentoveiculo.numeronota, " +
				"acompanhamentoveiculo.temDevolucao, tipoveiculo.cdtipoveiculo, tipoveiculo.nome," +
				"deposito.cddeposito, deposito.nome, acompanhamentoveiculostatus.cdstatusrav, recebimento.cdrecebimento ")
			.join("acompanhamentoveiculo.recebimento recebimento")	
			.join("acompanhamentoveiculo.acompanhamentoveiculostatus acompanhamentoveiculostatus")
			.leftOuterJoin("acompanhamentoveiculo.tipoveiculo tipoveiculo")
			.leftOuterJoin("acompanhamentoveiculo.deposito deposito")
			.where("recebimento = ? ",recebimento)
		.list();
	}

	/***
	 * 
	 * @param recebimento
	 * @return
	 */
	public Integer getTotalNotasRavByRecebimento(Recebimento recebimento) {
		return getJdbcTemplate().queryForInt("SELECT SUM(NUMERONOTA) FROM ACOMPANHAMENTOVEICULO AV WHERE AV.CDRECEBIMENTO = "+recebimento.getCdrecebimento());
	}
	
	/**
	 * 
	 * @param av
	 * @param status
	 */
	public void atualizaStatus(Acompanhamentoveiculo av, Acompanhamentoveiculostatus status){
		getJdbcTemplate().update("UPDATE ACOMPANHAMENTOVEICULO SET CDSTATUSRAV = "+status.getCdstatusrav()+" WHERE CDACOMPANHAMENTOVEICULO = "+av.getCdacompanhamentoveiculo());
	}
	
	/**
	 * 
	 * @param lista
	 * @return
	 */
	public List<Acompanhamentoveiculo> findRavForRecebimento(List<Acompanhamentoveiculo> lista) {
		return query()
				.select("acompanhamentoveiculo.cdacompanhamentoveiculo, acompanhamentoveiculo.numerorav")
				.join("acompanhamentoveiculo.acompanhamentoveiculostatus acompanhamentoveiculostatus")
				.where("acompanhamentoveiculo.dataentrada is not null")
				.where("acompanhamentoveiculo.datasaida is null")
				.where("acompanhamentoveiculostatus = ?",Acompanhamentoveiculostatus.ENTRADA)
				.whereIn("acompanhamentoveiculo.cdacompanhamentoveiculo",CollectionsUtil.listAndConcatenate(lista, "cdacompanhamentoveiculo", ","))
				.list();
	}

	/**
	 * 
	 * @param acompanhamentoveiculo
	 * @return
	 */
	public List<Acompanhamentoveiculo> findByVeiculo(Acompanhamentoveiculo acompanhamentoveiculo) {
		
		return query()
				.select("acompanhamentoveiculo.cdacompanhamentoveiculo, acompanhamentoveiculo.numerorav, deposito.cddeposito, deposito.nome")
				.join("acompanhamentoveiculo.acompanhamentoveiculostatus acompanhamentoveiculostatus")
				.join("acompanhamentoveiculo.deposito deposito")
				.where("upper(acompanhamentoveiculo.placaveiculo) = ?",acompanhamentoveiculo.getPlacaveiculo().toUpperCase())
				.where("acompanhamentoveiculo.cdacompanhamentoveiculo <> ?",acompanhamentoveiculo.getCdacompanhamentoveiculo())
				.whereIn("acompanhamentoveiculostatus", CollectionsUtil.listAndConcatenate(Arrays.asList(Acompanhamentoveiculostatus.GERADO, Acompanhamentoveiculostatus.AGUARDANDO_SENHA), "cdstatusrav", ","))
				.list();
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public List<Acompanhamentoveiculo> findForCancelamento(Date date) {
		
		return query()
				.select("acompanhamentoveiculo.cdacompanhamentoveiculo, acompanhamentoveiculo.numerorav")
				.join("acompanhamentoveiculo.acompanhamentoveiculostatus acompanhamentoveiculostatus")
				.openParentheses()
					.where("acompanhamentoveiculostatus = ?",Acompanhamentoveiculostatus.GERADO)
					.or()
					.where("acompanhamentoveiculostatus = ?",Acompanhamentoveiculostatus.AGUARDANDO_SENHA)
				.closeParentheses()
				.where("acompanhamentoveiculo.dtinclusao <= ?",date)
				.list();
	}
	
}