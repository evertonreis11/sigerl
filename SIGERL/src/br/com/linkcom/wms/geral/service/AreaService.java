package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Area;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.dao.AreaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class AreaService extends GenericService<Area> {
	private AreaDAO areaDAO;
	
	private static AreaService instance;
	public static AreaService getInstance() {
		if (instance == null)
			instance = Neo.getObject(AreaService.class);
		
		return instance;
	}
	
	public void setAreaDAO(AreaDAO areaDAO) {
		this.areaDAO = areaDAO;
	}
	
	/**
	 * 
	 * Método de referência ao DAO. 
	 * Método que retorna todas as áreas que não são virtuais.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.AreaDAO#findAllNaovirtual()
	 * 
	 * @author Arantes
	 *  
	 * @return List<Area>
	 * 
	 */
	public List<Area> findAllNaovirtual() {
		return areaDAO.findAllNaovirtual(); 
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.service.AreaService#findByCodigo(Area bean)
	 * 
	 * @param bean
	 * @return
	 */
	public Area findByCodigo(Long codigo, Deposito deposito ) {
		return areaDAO.findByCodigo(codigo, deposito);
	}
	

	/**
	 * 
	 * Método que retorna todas as áreas para o depósito atual
	 *  
	 * @author Giovane Freitas
	 * 
	 * @return List<Area>
	 * 
	 */
	public List<Area> findAllByDepositoLogado() {
		return areaDAO.findAllByDepositoLogado();
	}

	/**
	 * Localica uma área a partir do endereço.
	 * 
	 * @author Giovane Freitas
	 * @param endereco
	 * @return
	 */
	public Area findByEndereco(Endereco endereco) {
		return areaDAO.findByEndereco(endereco);
	}

}
