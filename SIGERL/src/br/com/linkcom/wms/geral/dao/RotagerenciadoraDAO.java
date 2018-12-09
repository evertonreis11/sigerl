package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Rotagerenciadora;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class RotagerenciadoraDAO extends GenericDAO<Rotagerenciadora>{

	/**
	 * 
	 * @return
	 */
	public List<Rotagerenciadora> findAllByDepositoLogado() {
		return query()
			.select("rotagerenciadora.cdrotagerenciadora, rotagerenciadora.descricao, deposito.cddeposito, deposito.nome")
			.join("rotagerenciadora.deposito deposito")
			.where("deposito = ?",WmsUtil.getDeposito())
			.list();
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Rotagerenciadora> findByAutocomplete(String param){
		
		QueryBuilder<Rotagerenciadora> query = query();
		
			query.select("rotagerenciadora.cdrotagerenciadora, rotagerenciadora.descricao")
				.join("rotagerenciadora.deposito deposito")
				.openParentheses()
					.whereLikeIgnoreAll("rotagerenciadora.descricao",param)
				.closeParentheses()
				.where("deposito = ?",WmsUtil.getDeposito());
		
		return query.list();
	}

}
