package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Depositofilial;
import br.com.linkcom.wms.geral.dao.DepositofilialDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class DepositofilialService extends GenericService<Depositofilial>{
	
	private DepositofilialDAO depositofilialDAO;
	
	public void setDepositofilialDAO(DepositofilialDAO depositofilialDAO) {
		this.depositofilialDAO = depositofilialDAO;
	}
	
	
	/**
	 * 
	 * @param codigoerp
	 * @return
	 */
	public Boolean isMultiCDByCodigoERP(Long codigoerp) {
		
		List<Depositofilial> lista = findByCodigoERPFilial(codigoerp);
		
		return lista!=null && !lista.isEmpty() && lista.size() > 1;
		
	}
	
	/**
	 * 
	 * @param codigoerp
	 * @return
	 */
	public List<Depositofilial> findByCodigoERPFilial(Long codigoerp){
		return depositofilialDAO.findByCodigoERPFilial(codigoerp);
	}
	
	/**
	 * 
	 * @param cliente
	 * @return
	 */
	public List<Depositofilial> findByFilial(Cliente cliente) {
		return depositofilialDAO.findByFilial(cliente);
	}
}
