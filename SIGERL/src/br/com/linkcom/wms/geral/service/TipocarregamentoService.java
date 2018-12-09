package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Tipocarregamento;
import br.com.linkcom.wms.geral.dao.TipocarregamentoDAO;

public class TipocarregamentoService extends GenericService<Tipocarregamento>{
	
	private TipocarregamentoDAO tipocarregamentoDAO;

	public void setTipocarregamentoDAO(TipocarregamentoDAO tipocarregamentoDAO) {
		this.tipocarregamentoDAO = tipocarregamentoDAO;
	}
	
}
