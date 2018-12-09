package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Controleestoqueproduto;

public class ControleestoqueprodutoService  extends GenericService<Controleestoqueproduto> {

	private static ControleestoqueprodutoService instance;
	public static ControleestoqueprodutoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(ControleestoqueprodutoService.class);
		}
		return instance;
	}
	
}
