package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicaogrupo;
import br.com.linkcom.wms.geral.dao.EtiquetaexpedicaogrupoDAO;

public class EtiquetaexpedicaogrupoService extends br.com.linkcom.wms.util.neo.persistence.GenericService<Etiquetaexpedicaogrupo> {

	private EtiquetaexpedicaogrupoDAO etiquetaexpedicaogrupoDAO;
	
	public void setEtiquetaexpedicaogrupoDAO(
			EtiquetaexpedicaogrupoDAO etiquetaexpedicaogrupoDAO) {
		this.etiquetaexpedicaogrupoDAO = etiquetaexpedicaogrupoDAO;
	}
	
	/* singleton */
	private static EtiquetaexpedicaogrupoService instance;
	public static EtiquetaexpedicaogrupoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(EtiquetaexpedicaogrupoService.class);
		}
		return instance;
	}
	
	/**
	 * Método com referência no DAO
	 * 
	 * @param etiquetaexpedicao
	 * @return
	 * @author Tomás Rabelo
	 */
	public List<Etiquetaexpedicaogrupo> carregaListaEtiquetaExpedicaoGrupo(Etiquetaexpedicao etiquetaexpedicao) {
		return etiquetaexpedicaogrupoDAO.carregaListaEtiquetaExpedicaoGrupo(etiquetaexpedicao);
	}
	
}
