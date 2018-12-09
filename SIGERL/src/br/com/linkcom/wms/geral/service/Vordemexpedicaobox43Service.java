package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Vordemexpedicaobox43;
import br.com.linkcom.wms.geral.dao.Vordemexpedicaobox43DAO;

public class Vordemexpedicaobox43Service extends GenericService<Vordemexpedicaobox43>{
	
	private static Vordemexpedicaobox43Service instance;
	protected Vordemexpedicaobox43DAO vordemexpedicaobox43dao;
	
	public static Vordemexpedicaobox43Service getInstance() {
		if (instance == null) {
			instance = Neo.getObject(Vordemexpedicaobox43Service.class);
		}
		return instance;
	}
	
	public void setVordemexpedicaobox43dao(Vordemexpedicaobox43DAO vordemexpedicaobox43dao) {
		this.vordemexpedicaobox43dao = vordemexpedicaobox43dao;
	}

	/**
	 * Lista os itens para a formação de conferência referente aos carregamentos de um box
	 * 
	 * @author Filipe Santos
	 * @param box
	 * @return
	 */
	public List<Vordemexpedicaobox43> findByBox(Box box) {
		return vordemexpedicaobox43dao.findByBox(box);
	}
}
