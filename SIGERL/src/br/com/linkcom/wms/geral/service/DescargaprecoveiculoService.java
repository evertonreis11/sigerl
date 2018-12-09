package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Descargapreco;
import br.com.linkcom.wms.geral.bean.Descargaprecoveiculo;
import br.com.linkcom.wms.geral.dao.DescargaprecoveiculoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

/**
 * 
 * @author Guilherme Arantes de Oliveira
 *
 */
public class DescargaprecoveiculoService extends GenericService<Descargaprecoveiculo> {
	
	DescargaprecoveiculoDAO descargaprecoveiculoDAO;
	
	public void setDescargaprecoveiculoDAO(DescargaprecoveiculoDAO descargaprecoveiculoDAO) {
		this.descargaprecoveiculoDAO = descargaprecoveiculoDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.dao.DescargaprecoveiculoDAO#findByDescargapreco(Descargapreco)
	 * @param descargapreco
	 * @return
	 * @author Arantes
	 */
	public List<Descargaprecoveiculo> findByDescargapreco(Descargapreco descargapreco) {
		return descargaprecoveiculoDAO.findByDescargapreco(descargapreco);
	}
}
