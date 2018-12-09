package br.com.linkcom.wms.geral.service;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Resumorecebimento;
import br.com.linkcom.wms.geral.dao.ResumorecebimentoDAO;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.ResumorecebimentoFiltro;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ResumorecebimentoService extends GenericService<Resumorecebimento>{

	private static ResumorecebimentoService instance;
	private ResumorecebimentoDAO resumorecebimentoDAO;

	public static ResumorecebimentoService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(ResumorecebimentoService.class);
		}
		return instance;
	}

	public void setResumorecebimentoDAO(
			ResumorecebimentoDAO resumorecebimentoDAO) {
		this.resumorecebimentoDAO = resumorecebimentoDAO;
	}
	
	public SqlRowSet getDadosExportacao(ResumorecebimentoFiltro filtro) {
		return resumorecebimentoDAO.getDadosExportacao(filtro);
	}

}
