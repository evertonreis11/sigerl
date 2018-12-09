package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.vo.RotaOcupacaoVO;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.RotaFiltro;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

/**
 * 
 * @author Guilherme Arantes de Oliveira
 *
 */
@DefaultOrderBy("rota.nome")
public class RotaDAO extends GenericDAO<Rota> {

	@Override
	public void updateListagemQuery(QueryBuilder<Rota> query, FiltroListagem _filtro) {
		RotaFiltro rotaFiltro = (RotaFiltro) _filtro;
		
		query
			.select("rota.cdrota, rota.nome")
			.where("rota.deposito = ?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
			.whereLikeIgnoreAll("rota.nome", rotaFiltro.getNome())
			.where(" exists (from Rotapraca rp where rp.rota = rota and rp.praca = ?)", rotaFiltro.getPraca())
			.where(" rota.tiporotapraca = ? ",rotaFiltro.getTiporotapraca());
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Rota> query) {
		query
			.leftOuterJoinFetch("rota.listaRotapraca rotapraca")
			.leftOuterJoinFetch("rotapraca.praca praca")
			.orderBy("rotapraca.ordem asc");
	}
	
	@Override
	public void updateSaveOrUpdate(final SaveOrUpdateStrategy save) {
		getTransactionTemplate().execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				save
//					.saveOrUpdateManaged("listaRotapraca")
					.saveOrUpdateManaged("listaRotaturnodeentrega")
					.saveOrUpdateManaged("listaRotaturnoextra");
				return null;
			}
		});
	}
	
	/**
	 * Busca todas as rotas cadastradas para o depósito atual
	 * @author Leonardo Guimarães
	 * 		   Giovane Freitas
	 * @return
	 */
	public List<Rota> findAllForCarregamento(){
		QueryBuilder<Rota> queryBuilder = query()
					.select("rota.cdrota, rota.nome")
					.where("rota.deposito=?", WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
					.orderBy("rota.nome");
				
		return queryBuilder.list();
	}
	
	/**
	 * Busca todas as rotas cadastradas para o depósito atual
	 * @return
	 */
	public List<Rota> findAllForFlex(){
		return query()
					.select("rota.cdrota, rota.nome")
					.where("rota.deposito=?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
					.list();
	}
	
	/**
	 * Método responsável por criar a lista de acorodo com as informações selecionadas na base MVLOJAS.
	 * 
	 * @see RotaCrud#gerarGrafico(br.com.linkcom.neo.core.web.WebRequestContext, Rota)
	 * @param RotaFiltro filtro 
	 * @param JdbcTemplate jdbcMVLOJAS 
	 * @return List<RotaOcupacaoVO> 
	 */
	@SuppressWarnings("unchecked")
	public List<RotaOcupacaoVO> getInfoRotaReloja(JdbcTemplate jdbcMVLOJAS, RotaFiltro filtro){
		
		StringBuilder sql = new StringBuilder();
		
			sql.append("	SELECT * FROM (");
			sql.append("		SELECT TO_CHAR(RELATORIO.DATA,'DD/MM/YYYY') AS DATA_FORMATADO, RELATORIO.*, ROWNUM as LINHA FROM ( ");	
			sql.append(" 			SELECT DE.DATA AS DATA, ");
			sql.append("				DE.NRO_GRUPO_FAIXA_CEP CDROTA, ");
			sql.append("				ROUND(((NVL(DE.QUANTIDADE_CONSUMIDA, 0) / DE.QUANTIDADE_TOTAL) * 100),2) as PERC_OCUPACAO_ENTREGAS, ");
			sql.append("				ROUND(((NVL(DE.CUBAGEM_CONSUMIDA, 0) / DE.CUBAGEM_TOTAL) * 100),2) as  PERC_OCUPACAO_CUBAGEM, ");
			sql.append("				ROUND(((NVL(DE.PESO_CONSUMIDO, 0) / DE.PESO_TOTAL) * 100),2) as PERC_OCUPACAO_PESO, ");
			sql.append("				ROUND(((NVL(DE.VALOR_CONSUMIDO, 0) / DE.VALOR_TOTAL) * 100),2) as PERC_OCUPACAO_VALOR, ");
			sql.append("				NVL(DE.QUANTIDADE_CONSUMIDA,0) as QUANTIDADE_CONSUMIDA, ");
			sql.append("				NVL(DE.QUANTIDADE_TOTAL,0) as QUANTIDADE_TOTAL, ");
			sql.append("				NVL(DE.CUBAGEM_CONSUMIDA,0) as CUBAGEM_CONSUMIDA, ");
			sql.append("				NVL(DE.CUBAGEM_TOTAL,0) as CUBAGEM_TOTAL, ");
			sql.append("				NVL(DE.PESO_CONSUMIDO,0) as PESO_CONSUMIDO, ");
			sql.append("				NVL(DE.PESO_TOTAL,0) as PESO_TOTAL, ");
			sql.append("				NVL(DE.VALOR_CONSUMIDO,0) as VALOR_CONSUMIDO, ");
			sql.append("				NVL(DE.VALOR_TOTAL,0) as VALOR_TOTAL ");
			sql.append("			 FROM MV_DATA_ENTREGA DE, MV_GRUPO_FAIXA_CEP GP ");
			sql.append("			 WHERE GP.NRO_GRUPO_FAIXA_CEP = DE.NRO_GRUPO_FAIXA_CEP ");
			sql.append("				AND TRUNC(DE.DATA) >= TRUNC(SYSDATE) ");
			sql.append("				AND DE.QUANTIDADE_TOTAL > 0 ");
			sql.append("				AND GP.NRO_CD = ").append(WmsUtil.getDeposito().getCodigoerp());
			sql.append("				AND GP.NRO_GRUPO_FAIXA_CEP = ").append(filtro.getRota().getCdrota());
			sql.append("				AND DE.IND_ATIVO = 1 ");
			sql.append("				ORDER BY DE.DATA ");
			sql.append(" 		) RELATORIO WHERE ROWNUM <= 16 ");
			sql.append("	) WHERE LINHA BETWEEN ").append(filtro.getLinhaInicial()).append(" AND ").append(filtro.getLinhaFinal());
		
		return jdbcMVLOJAS.query(sql.toString(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				RotaOcupacaoVO rotaOcupacaoVO = new RotaOcupacaoVO();
					
					//Definindo padrões de entrada
					rotaOcupacaoVO.setData(rs.getString("DATA_FORMATADO"));
					rotaOcupacaoVO.setListaPercentual(new ArrayList<Double>());					
					rotaOcupacaoVO.setListaValores(new ArrayList<List<Double>>());
					
					//Adicionando o Array de Percentuais					
					rotaOcupacaoVO.getListaPercentual().add(rs.getDouble("PERC_OCUPACAO_ENTREGAS"));
					rotaOcupacaoVO.getListaPercentual().add(rs.getDouble("PERC_OCUPACAO_CUBAGEM"));
					rotaOcupacaoVO.getListaPercentual().add(rs.getDouble("PERC_OCUPACAO_PESO"));
					rotaOcupacaoVO.getListaPercentual().add(rs.getDouble("PERC_OCUPACAO_VALOR"));					
					
					//Adicionando o Array de Detalhes: Entregas
					List<Double> entregas = new ArrayList<Double>();
					entregas.add(rs.getDouble("QUANTIDADE_TOTAL"));
					entregas.add(rs.getDouble("QUANTIDADE_CONSUMIDA"));
					rotaOcupacaoVO.getListaValores().add(entregas);
					
					//Adicionando o Array de Detalhes: Cubagem
					List<Double> cubagem = new ArrayList<Double>();
					cubagem.add(rs.getDouble("CUBAGEM_TOTAL"));
					cubagem.add(rs.getDouble("CUBAGEM_CONSUMIDA"));
					rotaOcupacaoVO.getListaValores().add(cubagem);
					
					//Adicionando o Array de Detalhes: Peso
					List<Double> peso = new ArrayList<Double>();
					peso.add(rs.getDouble("PESO_TOTAL"));
					peso.add(rs.getDouble("PESO_CONSUMIDO"));
					rotaOcupacaoVO.getListaValores().add(peso);
					
					//Adicionando o Array de Detalhes: Valores
					List<Double> valor = new ArrayList<Double>();
					valor.add(rs.getDouble("VALOR_TOTAL"));
					valor.add(rs.getDouble("VALOR_CONSUMIDO"));
					rotaOcupacaoVO.getListaValores().add(valor);
					
				return rotaOcupacaoVO;	
			}
		});
	}

	/**
	 * 
	 * @param whereIn
	 * @return
	 */
	public Map<Integer, Date> findRotasCortadas(String whereIn) {
		
		StringBuilder sql = new StringBuilder();
		
			sql.append(" select rota.cdrota as cdrota, rotacorte.dt_inclusao as data");
			sql.append(" from rota rota ");
			sql.append(" join tb_int_fila_tms_re_rotacorte rotacorte on rotacorte.cdrota = rota.cdrota ");
			sql.append(" where trim(rotacorte.dt_inclusao) = trim(sysdate) ");
			sql.append(" and rota.cdrota in ( ").append(whereIn).append(" ) ") ;
		
		final Map<Integer,Date> mapaRotasCortadas = new HashMap<Integer,Date>();
		
		getJdbcTemplate().query(sql.toString(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				Integer cdrota = rs.getInt("cdrota");
				Date data = rs.getDate("data");
				
				if(cdrota!=null && data!=null){
					mapaRotasCortadas.put(cdrota, data);	
				}
				
				return null;
			}
		});
		
		return mapaRotasCortadas;
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Rota> findRotaWithTipoAutocomplete(String param) {
		
		QueryBuilder<Rota> query = query();
		
			query.select("rota.cdrota, rota.nome, tiporotapraca.cdtiporotapraca, tiporotapraca.nome")
				.join("rota.tiporotapraca tiporotapraca")
				.whereLikeIgnoreAll("rota.nome", param)
				.where("rota.deposito = ?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito());
		
		return query.list();
		
	}

}
