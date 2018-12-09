package br.com.linkcom.wms.geral.service;

import br.com.linkcom.wms.geral.bean.Tabelafreterota;
import br.com.linkcom.wms.geral.dao.TabelafreterotaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class TabelafreterotaService extends GenericService<Tabelafreterota>{

	private TabelafreterotaDAO tabelafreterotaDAO;
	
	public void setTabelafreterotaDAO(TabelafreterotaDAO tabelafreterotaDAO) {
		this.tabelafreterotaDAO = tabelafreterotaDAO;
	}

	public Tabelafreterota loadForFretePraca(Tabelafreterota tabelafreterota) {
		return tabelafreterotaDAO.loadForFretePraca(tabelafreterota);
	}

}
