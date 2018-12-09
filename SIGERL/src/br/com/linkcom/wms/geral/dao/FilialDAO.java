package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Filial;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class FilialDAO extends GenericDAO<Filial>{

	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Filial> findFilial(String param) {
		
		QueryBuilder<Filial> query = query();
		
			query.select("filial.cdfilial, filial.nome")
				 .join("filial.empresa empresa")
				 .whereLikeIgnoreAll("filial.nome", param)
				 .where("empresa = ?",WmsUtil.getEmpresa());
		
		return query.list();
		
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Filial> findForAll() {
		
		QueryBuilder<Filial> query = query();
		
			query.select("filial.cdfilial, filial.nome")
				 .join("filial.empresa empresa")
				 .where("empresa = ?",WmsUtil.getEmpresa());
		
		return query.list();
		
	}
	
	/**
	 * 
	 * @param filial
	 * @return
	 */
	public Filial findCepByFilial(Filial filial) {
		
		if(filial==null || filial.getCdfilial()==null)
			return null;
		
		QueryBuilder<Filial> query = query();
		
			query.select("filial.cep")
				 .where("filial.cdfilial = ?",filial.getCdfilial());
		
		return query.unique();
	}

	
	/**
	 * 
	 * @param nomeFilial
	 * @return
	 */
	public Filial findFilialForPraca(String nomeFilial) {
		
		QueryBuilder<Filial> query = query();
		
		query.select("filial.cdfilial, filial.nome")
			 .join("filial.empresa empresa")
			 .where("filial.nome = ?", nomeFilial)
			 .where("empresa = ?",WmsUtil.getEmpresa());
		
		return query.unique();
	}

	/**
	 * 
	 * @param filial
	 * @return
	 */
	public Filial findCodigoerp(Filial filial) {
		
		QueryBuilder<Filial> query = query();
		
		query.select("filial.cdfilial, filial.codigoerp")
			.where("cdfilial = ?",filial.getCdfilial());
		
		return query.unique();
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Filial> findForAutocompleteByEmpresa(String param) {
		
		QueryBuilder<Filial> query = query();
		
			query.select("filial.cdfilial, filial.nome")
				.join("filial.empresa empresa")
				.whereLikeIgnoreAll("filial.nome", param)
				.where("empresa = ?",WmsUtil.getEmpresa());
		
		return query.list();
	}
	
}
