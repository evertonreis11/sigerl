package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Pessoanatureza;
import br.com.linkcom.wms.geral.dao.PessoanaturezaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class PessoanaturezaService extends GenericService<Pessoanatureza> {

	private PessoanaturezaDAO pessoanaturezaDAO;

	public void setPessoanaturezaDAO(PessoanaturezaDAO pessoanaturezaDAO) {
		this.pessoanaturezaDAO = pessoanaturezaDAO;
	}

	/**
	 * Método de acesso ao DAO
	 * @see br.com.linkcom.wms.geral.dao.PessoanaturezaDAO#findPessoanatureza
	 * @param cdpessoanatureza
	 * @return
	 * @author Thiago Gonçalves
	 */
	public Pessoanatureza findPessoanatureza(Pessoanatureza pessoanatureza){
		return pessoanaturezaDAO.findPessoanatureza(pessoanatureza);
	}

	/* singleton */
	private static PessoanaturezaService instance;
	public static PessoanaturezaService getInstance() {
		if(instance == null){
			instance = Neo.getObject(PessoanaturezaService.class);
		}
		return instance;
	}
}
