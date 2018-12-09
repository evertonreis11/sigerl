package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Vformacaocarga;
import br.com.linkcom.wms.geral.dao.VformacaocargaDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.CarregamentoFiltro;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class VformacaocargaService extends GenericService<Vformacaocarga>{
	
	private VformacaocargaDAO vformacaocargaDAO;
	
	public void setVformacaocargaDAO(VformacaocargaDAO vformacaocargaDAO) {
		this.vformacaocargaDAO = vformacaocargaDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Vformacaocarga> findForCarregamento(CarregamentoFiltro filtro,String cds) {
		return vformacaocargaDAO.findForCarregamento(filtro,cds);
	}
	
	/* singleton */
	private static VformacaocargaService instance;
	public static VformacaocargaService getInstance() {
		if(instance == null){
			instance = Neo.getObject(VformacaocargaService.class);
		}
		return instance;
	}
	
	
}
