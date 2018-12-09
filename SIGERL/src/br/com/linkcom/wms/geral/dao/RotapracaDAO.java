package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotapraca;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

/**
 * @author Guilherme Arantes de Oliveira
 *
 */

public class RotapracaDAO extends GenericDAO<Rotapraca> {

	@Override
	public List<Rotapraca> findAll() {
		return super.findAll();
	}
	
	/**
	 * @author Guilherme Arantes de Oliveira
	 * 
	 * Metodo que recupera todas os registros da tabela ROTAPRACA
	 * 
	 * @return
	 */	
	public List<Rotapraca> findAllRotasPracas() {		
		return query()
			.select("rotapraca.cdrotapraca, rota.cdrota, rotapraca.ordem, praca.cdpraca")
			.join("rotapraca.rota rota")
			.join("rotapraca.praca praca")			
			.list();		
	}
	
	/**
	 * 
	 * @param rota
	 */
	public void deleteByRota(Rota rota) {
		if(rota == null || rota.getCdrota() == null) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("delete from Rotapraca rp where rp.rota.id = ?", 
				new Object[]{rota.getCdrota()});
	}

	/**
	 * 
	 * @param cdrota
	 * @return
	 */
	public Rotapraca findUltimaOrdemByRota(Integer cdrota) {
		
		if(cdrota == null){ 
			throw new WmsException("Parâmetros inválidos.");
		}
		
		QueryBuilder<Rotapraca> query = query();
			query.select("rotapraca.cdrotapraca, rotapraca.ordem");
			query.where("rotapraca.rota.cdrota = ?",cdrota);
			query.where("rotapraca.ordem = (select max(rp.ordem) from Rotapraca rp where rp.rota = rotapraca.rota)");

		return query.unique();
	}

	/**
	 * 
	 * @param cdpraca
	 * @param cdrota
	 * @return
	 */
	public Rotapraca findByPracaRota(Integer cdpraca, Integer cdrota) {
		
		if(cdrota == null || cdpraca == null){ 
			throw new WmsException("Parâmetros inválidos.");
		}
		
		return query()
			.select("rotapraca.cdrotapraca, praca.cdpraca, rota.cdrota")
			.join("rotapraca.rota rota")
			.join("rotapraca.praca praca")
			.where("rotapraca.rota.cdrota = ?",cdrota)
			.where("rotapraca.praca.cdpraca = ?",cdpraca)
			.unique();	
	}

	/**
	 * 
	 * @param pracasSelecionadas
	 * @return
	 */
	public List<Rotapraca> findPracasByWhereInPraca(String whereIn){
		
		QueryBuilder<Rotapraca> query = query();
		
			query.select("rotapraca.cdrotapraca, praca.cdpraca, praca.nome");
			query.join("rotapraca.praca praca");
			query.whereIn("praca.cdpraca", whereIn);
		
		return query.list();
	}
	
}
