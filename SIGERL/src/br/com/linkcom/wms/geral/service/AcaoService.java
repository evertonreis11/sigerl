package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Acao;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class AcaoService extends GenericService<Acao> {
	
	/* singleton */
	private static AcaoService instance;
	
	public static AcaoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(AcaoService.class);
		}
		return instance;
	}
	
}
