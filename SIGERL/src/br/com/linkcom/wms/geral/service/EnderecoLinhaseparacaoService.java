package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.EnderecoLinhaseparacao;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class EnderecoLinhaseparacaoService extends GenericService<EnderecoLinhaseparacao> {

	private static EnderecoLinhaseparacaoService instance = null;
	public static EnderecoLinhaseparacaoService getInstance() {
		if (instance == null)
			instance = Neo.getObject(EnderecoLinhaseparacaoService.class);
		
		return instance;
	}

}
