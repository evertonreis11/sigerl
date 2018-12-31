package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Depositofilial;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class DepositofilialDAO extends GenericDAO<Depositofilial> {
	
	public List<Depositofilial> findByCodigoERPFilial(Long codigoerp) {

		QueryBuilder<Depositofilial> query = query();
		
		query.select("depositofilial.cddepositofilial, deposito.cddeposito, deposito.nome, filial.cdpessoa, filial.nome");
		query.join("depositofilial.filial filial");
		query.join("depositofilial.deposito deposito");
		query.where("filial.codigoerp = ?",codigoerp);
		
		return query.list();
		
	}
	
	/**
	 * 
	 * @param filial
	 * @return
	 */
	public List<Depositofilial> findByFilial(Cliente filial) {
		
		QueryBuilder<Depositofilial> query = query();
		
		query.select("depositofilial.cddepositofilial, deposito.cddeposito, deposito.nome, filial.cdpessoa, filial.nome");
		query.join("depositofilial.filial filial");
		query.join("depositofilial.deposito deposito");
		query.where("filial = ?",filial);
		
		return query.list();
		
	}
}
