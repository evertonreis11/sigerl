package br.com.linkcom.wms.geral.service;

import br.com.linkcom.wms.geral.bean.Carregamentohistoricotipo;
import br.com.linkcom.wms.geral.dao.CarregamentohistoricotipoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class CarregamentohistoricotipoService extends GenericService<Carregamentohistoricotipo>{

	private CarregamentohistoricotipoDAO carregamentohistoricotipoDAO;

	public void setCarregamentohistoricotipoDAO(CarregamentohistoricotipoDAO carregamentohistoricotipoDAO) {
		this.carregamentohistoricotipoDAO = carregamentohistoricotipoDAO;
	}
	
}
