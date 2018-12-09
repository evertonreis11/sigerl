package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Municipio;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class MunicipioService extends GenericService<Municipio> {

	private static MunicipioService instance;

	public static MunicipioService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(MunicipioService.class);
		}
		return instance;
	}
}
