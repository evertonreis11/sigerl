package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Uf;
import br.com.linkcom.wms.geral.dao.AgendajanelaclasseDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class UfService extends GenericService<Uf> {

	private static UfService instance;

	public static UfService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(UfService.class);
		}
		return instance;
	}

}
