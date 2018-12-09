package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Enderecosentido;
import br.com.linkcom.wms.geral.dao.EnderecosentidoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class EnderecosentidoService extends GenericService<Enderecosentido> {
	private EnderecosentidoDAO enderecosentidoDAO;
	
	public void setEnderecosentidoDAO(EnderecosentidoDAO enderecosentidoDAO) {
		this.enderecosentidoDAO = enderecosentidoDAO;
		
	}
	
	/**
	 * 
	 * M�todo de refer�ncia ao DAO.
	 * M�todo que recupera uma lista de todos os registros da tab endere�o sentido ordenado pelo c�digo da �rea e pelo sentido da rua
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecosentidoDAO#findAll() 
	 * 
	 * @author Arantes
	 * 
	 * @return List<Enderecosentido>
	 * 
	 */
	public List<Enderecosentido> findAll() {
		return enderecosentidoDAO.findAll();
		
	}
}
