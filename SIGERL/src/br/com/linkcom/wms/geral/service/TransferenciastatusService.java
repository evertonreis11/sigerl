package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Transferencia;
import br.com.linkcom.wms.geral.bean.Transferenciastatus;
import br.com.linkcom.wms.geral.dao.TransferenciaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class TransferenciastatusService extends GenericService<Transferenciastatus> {
	
	private TransferenciaDAO transferenciaDAO;
	
	public void setTransferenciaDAO(TransferenciaDAO transferenciaDAO) {
		this.transferenciaDAO = transferenciaDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.TransferenciaDAO#updateStatusTransferencia(Transferencia transferencia)

	 * 
	 * @param transferencia
	 */
	public void updateStatusTransferencia(Transferencia transferencia){
		transferenciaDAO.updateStatusTransferencia(transferencia);
	}
	
	/* singleton */
	private static TransferenciastatusService instance;
	public static TransferenciastatusService getInstance() {
		if(instance == null){
			instance = Neo.getObject(TransferenciastatusService.class);
		}
		return instance;
	}

}
