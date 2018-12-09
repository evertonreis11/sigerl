package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.types.Cep;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Tipoentrega;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.PracaFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("nome")
public class PracaDAO extends GenericDAO<Praca> {
	
	@Override
	public void updateListagemQuery(QueryBuilder<Praca> query, FiltroListagem _filtro) {
		
		PracaFiltro pracaFiltro = (PracaFiltro) _filtro;
		
		query
			.select("praca.cdpraca, praca.nome, praca.cepinicio, praca.cepfim")
			.join("praca.tiporotapraca tiporotapraca")
			.whereLikeIgnoreAll("praca.nome", pracaFiltro.getNome())
			.openParentheses()
				.openParentheses()
					.where("praca.cepinicio >= ?", pracaFiltro.getCepinicio())
					.where("praca.cepfim <= ?", pracaFiltro.getCepfim())
				.closeParentheses()
				.or()
				.openParentheses()
					.where("praca.cepinicio = ?", pracaFiltro.getCepinicio())
					.or()
					.where("praca.cepfim = ?", pracaFiltro.getCepfim())
				.closeParentheses()
				.or()
				.where("? between praca.cepinicio and praca.cepfim", pracaFiltro.getCepinicio())
			.closeParentheses()
			.where("tiporotapraca = ?",pracaFiltro.getTiporotapraca())
			.where("praca.deposito = ?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito());
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Praca> query) {
		query.select("praca.cdpraca, praca.nome, praca.cepinicio, praca.cepfim, praca.vlrfretenormal, praca.vlrfreteagendado, " +
				"praca.valorfreteporentrega, deposito.cddeposito, deposito.nome, tiporotapraca.cdtiporotapraca, tiporotapraca.nome ");
		query.join("praca.deposito deposito");
		query.leftOuterJoin("praca.tiporotapraca tiporotapraca");
	}

	@Override
	public List<Praca> findAll() {
		
		return query()
					.orderBy("praca.nome")
					.where("praca.deposito = ?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
					.list();
	}
	
	/**
	 * Encontra todas as praças que estão associadas a rota especificada.
	 * @param rota
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Praca> findBy(Rota rota) {
		if(rota == null || rota.getCdrota() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		return query()
				.select("praca.cdpraca,praca.nome, praca.cepinicio, praca.cepfim")
				.join("praca.listaRotapraca rotapraca")
				.join("rotapraca.rota rota")
				.where("rota=?",rota)
				.list();
	}
	
	/**
	 * Encontra todas as praças que não possuem rotas
	 * @param cdPracas
	 * @return
	 * @autor Leonardo Guimarães
	 */
	public List<Praca> findForRotaNaoEncontrada(String cdPracas) {
		if(cdPracas == null)
				throw new WmsException("Parâmetro incorreto");
		
		return query()
					.select("praca.cdpraca, praca.nome, praca.cepinicio, praca.cepfim")
					.where("praca.cdpraca not in (" + cdPracas.substring(0, cdPracas.length() - 1) + ")")
					.list();
	}

	/**
	 * Encontra a praça e sua rota
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param praca
	 * @return
	 */
	public Praca findByPraca(Praca praca) {
		if(praca == null || praca.getCdpraca() == null)
			throw new WmsException("A praca não deve ser nula");
		return query()
					.select("praca.cdpraca,praca.nome,rota.cdrota,rota.nome")
					.leftOuterJoin("praca.listaRotapraca listaRotapraca")
					.leftOuterJoin("listaRotapraca.rota rota")
					.where("praca = ?",praca)
					.setMaxResults(1)
					.unique();
		}
	
	@Override
	public List<Praca> findForCombo(String orderby, String... extraFields) {
		return query()
					.select("praca.cdpraca,praca.nome,praca.cepinicio,praca.cepfim")
					.where("praca.deposito = ?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
					.list();
	}
	
	/**
	 * Lista todas as praças que ainda não estão associadas a uma rota.
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param filtro Uma praça para usar como exemplo para montagem das restrições da consulta.
	 * @param pracasParaExcluir Uma string contendo a lista de CDPRACA serparado por vírgula.
	 * @return
	 */
	public List<Praca> findPracasDisponiveis(Praca filtro, String pracasParaExcluir){
		QueryBuilder<Praca> query = queryWithOrderBy()
			.leftOuterJoin("praca.listaRotapraca rotapraca")
			.leftOuterJoin("praca.tiporotapraca tiporotapraca")
			.where("rotapraca.id is null")
			.where("praca.deposito = ?",WmsUtil.getDeposito())
			.whereLikeIgnoreAll("praca.nome", filtro.getNome())
			.where("praca.cepinicio >= ?", filtro.getCepinicio())
			.where("praca.cepfim <= ?", filtro.getCepfim())
			.where("tiporotapraca = ?",filtro.getTiporotapraca())
			.setUseTranslator(false);
		
		if (pracasParaExcluir != null && !pracasParaExcluir.isEmpty())
			query.where("praca.cdpraca not in (" + pracasParaExcluir + ")");
		
		return query.list();
	}
	
	public List<Praca> findPracasByDeposito(Deposito deposito){
		QueryBuilder<Praca> queryBuilder = query()
					.select("praca.cdpraca, praca.nome")
					.where("praca.deposito=?", deposito)
					.orderBy("praca.nome");
				
		return queryBuilder.list();
	}

	/**
	 * Método que busca a praça pelo nome do depósito logado
	 * 
	 * @param nome
	 * @return
	 * @author Tomás Rabelo
	 */
//	public List<Praca> findByNomeAutoComplete(String nome) {
//		return query()
//			.select("praca.cdpraca, praca.nome")
//			.where("praca.deposito=?", WmsUtil.getDeposito())
//			.whereLikeIgnoreAll("praca.nome", nome)
//			.orderBy("praca.nome")
//			.list();
//	}
	
	
	/**
	 * Método  que busca a rota pela praça 
	 * 
	 * @param nome
	 * @return
	 * @author Marcus Vinicius 
	 */
	@SuppressWarnings("unchecked")
	public List<Praca> findByRotaPracaAutoComplete(String nome) {
	
        StringBuilder sql = new StringBuilder();
    	
    	sql.append(" SELECT rt.NOME || ' > ' || p.NOME as ROTAPRACA, p.CDPRACA");
    	sql.append(" FROM PRACA p inner join ROTAPRACA r on p.CDPRACA = r.CDPRACA inner join ROTA rt on rt.CDROTA = r.CDROTA");
        sql.append(" WHERE p.CDDEPOSITO = ? and (UPPER(p.NOME) LIKE '%"+nome.toUpperCase()+"%' or UPPER(rt.NOME) LIKE '%"+nome.toUpperCase()+"%')");
        sql.append(" ORDER BY p.NOME");
	    
	    return (List<Praca>) getJdbcTemplate().query(sql.toString(), new Object[]{WmsUtil.getDeposito().getCddeposito()}, new ResultSetExtractor(){
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					
					List<Praca> list = new ArrayList<Praca>();
					
					while (rs.next()){
						Praca praca = new Praca();
					    praca.setCdpraca(rs.getInt("CDPRACA"));
					    praca.setRotaPraca(rs.getString("ROTAPRACA"));
						
					    list.add(praca);
						
					}
					
					return list;
				}
			}
	    );
	}
	
	/**
	 * 
	 * @param praca
	 * @return
	 */
	public List<Praca> findAllByTiporotapraca(Praca praca) {
		
		return query()
					.orderBy("praca.nome")
					.where("praca.deposito = ?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
					.where("praca.tiporotapraca = 2")
					.list();
	}
	
	/**
	 * Encontra todas as praças que estão associadas a rota especificada.
	 * @param rota
	 * @return
	 * @author Pedro Gonçalves
	 * @param tipoentrega 
	 */
	public List<Praca> findByTabelaFrete(Rota rota, Cep cepinicio, Cep cepfim, String nome, Tipoentrega tipoentrega){
		
		if(rota == null || rota.getCdrota() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		return query()
			.select("praca.cdpraca, praca.nome, praca.cepinicio, praca.cepfim")
			.join("praca.listaRotapraca rotapraca")
			.join("rotapraca.rota rota")
			.join("praca.tiporotapraca tiporotapraca")
			.where("rota=?",rota)
			.where("praca.cepinicio>=?",cepinicio)
			.where("praca.cepfim<=?",cepfim)
			.where("tiporotapraca.cdtiporotapraca = ?",tipoentrega!=null&&tipoentrega.getCdtipoentrega()!=null?tipoentrega.getCdtipoentrega():null)
			.whereLikeIgnoreAll("praca.nome",nome)
			.list();
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Praca> findPracaByNotaAutocomplete(String param) {
		return query()
			.select("praca.cdpraca, praca.nome, tiporotapraca.cdtiporotapraca")
			.join("praca.tiporotapraca tiporotapraca")
			.join("praca.listaRotapraca listaRotapraca")
			.join("listaRotapraca.rota rota")
			.where("praca.deposito = ?",WmsUtil.getDeposito())
			.whereLikeIgnoreAll("praca.nome",param)
			.list();
	}

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Praca> findByFiltro(PracaFiltro filtro) {
		return query()
			.select("praca.cdpraca, praca.nome, praca.cepinicio, praca.cepfim, tiporotapraca.cdtiporotapraca, " +
					"tiporotapraca.nome, deposito.cddeposito, deposito.nome")
			.join("praca.tiporotapraca tiporotapraca")
			.join("praca.deposito deposito")
			.where("praca.deposito = ?",WmsUtil.getDeposito())
			.whereLikeIgnoreAll("praca.nome", filtro.getNome())
			.where("praca.cepinicio <= ?",filtro.getCepinicio())
			.where("praca.cepfim >= ?",filtro.getCepfim())
			.where("tiporotapraca.cdtiporotapraca = ?",filtro.getCdtiporotapraca())
			.where("praca.cdpraca not in (select rp.praca.id from Rotapraca rp where rp.praca = praca)")
			.list();
	}

	/**
	 * 
	 * @param cdrota
	 * @return
	 */
	public List<Praca> findByRota(Integer cdrota){
		
		QueryBuilder<Praca> query = query()
		
			.select("praca.cdpraca, praca.nome, praca.cepinicio, praca.cepfim, praca.vlrfretenormal, tiporotapraca.cdtiporotapraca, tiporotapraca.nome")
			.join("praca.listaRotapraca rotapraca")
			.join("rotapraca.rota rota")
			.join("praca.tiporotapraca tiporotapraca")
			.where("rota.cdrota = ?",cdrota)
			.orderBy("praca.nome");
		
		return query.list();
	}
	
}
