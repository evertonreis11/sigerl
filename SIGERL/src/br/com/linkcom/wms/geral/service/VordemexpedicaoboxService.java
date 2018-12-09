package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Vordemexpedicaobox;
import br.com.linkcom.wms.geral.dao.VordemexpedicaoboxDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class VordemexpedicaoboxService extends GenericService<Vordemexpedicaobox>{

	private static VordemexpedicaoboxService instance;
	private VordemexpedicaoboxDAO vordemexpedicaoboxDAO;

	public static VordemexpedicaoboxService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(VordemexpedicaoboxService.class);
		}
		return instance;
	}

	public void setVordemexpedicaoboxDAO(
			VordemexpedicaoboxDAO vordemexpedicaoboxDAO) {
		this.vordemexpedicaoboxDAO = vordemexpedicaoboxDAO;
	}

	/**
	 * Lista os itens para a formação de conferência referente aos carregamentos de um box
	 * 
	 * @author Giovane Freitas
	 * @param box
	 * @return
	 */
	public List<Vordemexpedicaobox> findByBox(Box box) {
		return vordemexpedicaoboxDAO.findByBox(box);
	}

	public String getFiliaisClientes(String itensOrdem, Carregamento carregamento) {
		return vordemexpedicaoboxDAO.getFiliaisClientes(itensOrdem, carregamento);
	}

	public String getTiposPedidos(String itensOrdem, Carregamento carregamento) {
		return vordemexpedicaoboxDAO.getTiposPedidos(itensOrdem, carregamento);
	}
	
}
