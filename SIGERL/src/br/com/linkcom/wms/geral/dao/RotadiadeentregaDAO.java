package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotadiadeentrega;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class RotadiadeentregaDAO extends GenericDAO<Rotadiadeentrega>{

	/**
	 * 
	 * @param rota
	 * @return
	 */
	public Rotadiadeentrega findUltimaData(Rota rota) {
		Rotadiadeentrega rotadiadeentrega = new Rotadiadeentrega();
		QueryBuilder<Rotadiadeentrega> query = newQueryBuilderWithFrom(Rotadiadeentrega.class)
			.select("diadeentrega.dia,diadeentrega.segunda,diadeentrega.terca,diadeentrega.quarta,diadeentrega.quinta,diadeentrega.sexta," +
					"diadeentrega.sabado,diadeentrega.domingo")
			.join("rotadiadeentrega.rota rota")
			.join("rotadiadeentrega.diadeentrega diadeentrega")			
			.where("rota.cdrota=?",rota.getCdrota())
			.orderBy("diadeentrega.cddiadeentrega desc")
			.setMaxResults(1);
		
		if(query.unique()!=null)
			return query.unique(); 
		else{
			return rotadiadeentrega;
		}
	}

	/**
	 * 
	 * @param rota
	 */
	public void deleteByRota(Rota rota) {
		if(rota == null || rota.getCdrota() == null) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("delete from Rotadiadeentrega rde where rde.rota.id = ?", 
				new Object[]{rota.getCdrota()});		
	}
}
