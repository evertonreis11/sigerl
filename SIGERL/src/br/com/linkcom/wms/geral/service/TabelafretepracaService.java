package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Tabelafretepraca;
import br.com.linkcom.wms.geral.dao.TabelafretepracaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class TabelafretepracaService extends GenericService<Tabelafretepraca>{

	private TabelafretepracaDAO tabelafretepracaDAO;
	
	public void setTabelafretepracaDAO(TabelafretepracaDAO tabelafretepracaDAO) {
		this.tabelafretepracaDAO = tabelafretepracaDAO;
	}

	/**
	 * 
	 * @param rota
	 * @return
	 */
	public List<Tabelafretepraca> findByRota(Rota rota){
		return tabelafretepracaDAO.findByRota(rota);
	}

}
