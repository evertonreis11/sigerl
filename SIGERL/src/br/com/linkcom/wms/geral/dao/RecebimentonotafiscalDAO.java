package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Notafiscalentrada;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Recebimentonotafiscal;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class RecebimentonotafiscalDAO extends GenericDAO<Recebimentonotafiscal> {

	@Override
	public void updateListagemQuery(QueryBuilder<Recebimentonotafiscal> query, FiltroListagem _filtro) {
		query.leftOuterJoinFetch("recebimentonotafiscal.recebimento recebimento");
		query.leftOuterJoinFetch("recebimentonotafiscal.notafiscalentrada notafiscalentrada");
	}

	/**
	 * Remove os as notas fiscais associadas ao recebimento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param recebimento
	 */
	public void deleteByRecebimento(Recebimento recebimento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("O recebimento não deve ser nulo.");
		
		getHibernateTemplate().bulkUpdate("delete Recebimentonotafiscal rf where rf.recebimento = ?",new Object[]{recebimento});		
	}
	
	/**
	 * Busca as notas ficas de um recebimento
	 * 
	 * @author Arantes
	 * 
	 * @param recebimento
	 * @return List<Recebimentonotafiscal>
	 * 
	 */
	public List<Recebimentonotafiscal> findByRecebimento(Recebimento filtro) {
		if(filtro == null)
			throw new WmsException("Parâmetros inválidos.");
			
		return query()
					.select("notafiscalentrada.cdnotafiscalentrada, notafiscalentrada.numero")
					.join("recebimentonotafiscal.recebimento recebimento")
					.join("recebimentonotafiscal.notafiscalentrada notafiscalentrada")
					.where("recebimento = ?", filtro)
					.orderBy("notafiscalentrada.numero")
					.list();
	}

	/* singleton */
	private static RecebimentonotafiscalDAO instance;
	public static RecebimentonotafiscalDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(RecebimentonotafiscalDAO.class);
		}
		return instance;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Recebimentonotafiscal> loadRecebimentonotafiscal(String listaStatus, Integer cddeposito) {
		
		StringBuilder sql = new StringBuilder();
		
		//corrigir query
		
		sql.append("select /*+ USE_NL(rnf r nfe) */ rnf.cdnotafiscalentrada, nfe.codigoerp, nfe.devolvida, ");
		sql.append("r.cdrecebimento, r.cdrecebimentostatus from recebimentonotafiscal rnf ");
		sql.append("inner join recebimento r on rnf.cdrecebimento = r.cdrecebimento ");
		sql.append("inner join notafiscalentrada nfe on nfe.cdnotafiscalentrada = rnf.cdnotafiscalentrada ");
		sql.append("where r.cddeposito = ? and r.cdrecebimentostatus  in ( ? )  ");
		sql.append("union all select nfe.cdnotafiscalentrada, nfe.codigoerp, nfe.devolvida, null, null from notafiscalentrada nfe ");
		sql.append("where nfe.cddeposito = ? and nfe.devolvida = 1 and nfe.dtsincronizacao is null ");
		
		return (List<Recebimentonotafiscal>) getJdbcTemplate().query(sql.toString(), new Object[]{cddeposito, listaStatus, cddeposito},
				new ResultSetExtractor(){
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Recebimentonotafiscal> list = new ArrayList<Recebimentonotafiscal>();
				
				while (rs.next()){
					Recebimentonotafiscal recebimentonotafiscal = new Recebimentonotafiscal();
					Notafiscalentrada notafiscalentrada = new Notafiscalentrada();
					Recebimento recebimento = new Recebimento();
					Recebimentostatus recebimentostatus = new Recebimentostatus();
					
//					recebimentonotafiscal.setCdrecebimentonotafiscal(rs.getInt("cdnotafiscalentrada"));
					notafiscalentrada.setCdnotafiscalentrada(rs.getInt("cdnotafiscalentrada"));
					notafiscalentrada.setCodigoerp(rs.getLong("codigoerp"));
					notafiscalentrada.setDevolvida(rs.getBoolean("devolvida"));
					recebimento.setCdrecebimento(rs.getInt("cdrecebimento"));
					recebimentostatus.setCdrecebimentostatus(rs.getInt("cdrecebimentostatus"));

					recebimento.setRecebimentostatus(recebimentostatus);
					recebimentonotafiscal.setNotafiscalentrada(notafiscalentrada);
					recebimentonotafiscal.setRecebimento(recebimento);
					
					list.add(recebimentonotafiscal);
				}
				return list;
			}
		}
		);
	}
	
	
	
}