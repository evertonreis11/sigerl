package br.com.linkcom.wms.geral.service;

import br.com.linkcom.wms.geral.bean.Tabelafrete;
import br.com.linkcom.wms.geral.dao.TabelafreteDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class TabelafreteService extends GenericService<Tabelafrete>{
	
	private TabelafreteDAO tabelafreteDAO;
	
	public void setTabelafreteDAO(TabelafreteDAO tabelafreteDAO) {
		this.tabelafreteDAO = tabelafreteDAO;
	}
	
	public Tabelafrete loadByTabelafrete (Tabelafrete tabelafrete){
		return tabelafreteDAO.loadByTabelafrete(tabelafrete);
	}
	
}
