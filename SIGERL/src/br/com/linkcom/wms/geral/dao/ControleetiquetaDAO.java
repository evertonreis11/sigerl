package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Controleetiqueta;

public class ControleetiquetaDAO extends GenericDAO<Controleetiqueta> {

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		super.updateSaveOrUpdate(save);
	}
}
