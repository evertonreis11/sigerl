package br.com.linkcom.wms.geral.service;

import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.dao.CarregamentoexpedicaoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class CarregamentoexpedicaoService extends GenericService<Carregamento> {

	private CarregamentoexpedicaoDAO carregamentoexpedicaoDAO;

	public void setCarregamentoexpedicaoDAO(CarregamentoexpedicaoDAO carregamentoexpedicaoDAO) {
		this.carregamentoexpedicaoDAO = carregamentoexpedicaoDAO;
	}
	
	/**
	 * Busca o número de carregamentos abertos para um determinado box.
	 * 
	 * @author Giovane Freitas
	 * @param box
	 * @return
	 */
	public Integer getCountCarregamentos(Box box) {
		return carregamentoexpedicaoDAO.getCountCarregamentos(box);
	}

}
