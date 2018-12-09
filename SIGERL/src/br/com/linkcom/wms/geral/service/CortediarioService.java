package br.com.linkcom.wms.geral.service;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Cortediario;
import br.com.linkcom.wms.geral.dao.CortediarioDAO;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.CortediarioFiltro;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class CortediarioService extends GenericService<Cortediario>{

	private CortediarioDAO cortediarioDAO;
	private static CortediarioService instance;

	public static CortediarioService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(CortediarioService.class);
		}
		return instance;
	}

	public void setCortediarioDAO(CortediarioDAO cortediarioDAO) {
		this.cortediarioDAO = cortediarioDAO;
	}
	
	public SqlRowSet getDadosExportacao(CortediarioFiltro filtro) {
		return cortediarioDAO.getDadosExportacao(filtro);
	}
	
}
