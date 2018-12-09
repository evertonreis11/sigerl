package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Pessoanatureza;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;


public class PessoanaturezaDAO extends GenericDAO<Pessoanatureza> {
	
	/* singleton */
	private static PessoanaturezaDAO instance;
	public static PessoanaturezaDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(PessoanaturezaDAO.class);
		}
		return instance;
	}
	/**
	 * Método que busca uma natureza atravéz de cdpessoanatureza
	 * @param cdpessoanatureza
	 * @return Pessoanatureza
	 */
	public Pessoanatureza findPessoanatureza(Pessoanatureza pessoanatureza){
		if(pessoanatureza == null || pessoanatureza.getCdpessoanatureza() == null){
			throw new WmsException("O parâmetro cdpessoanatureza não pode ser nulo");
		}
		return query()
					.select("pessoanatureza.cdpessoanatureza, pessoanatureza.nome")
					.entity(pessoanatureza)
					.unique();
					
	}
}
