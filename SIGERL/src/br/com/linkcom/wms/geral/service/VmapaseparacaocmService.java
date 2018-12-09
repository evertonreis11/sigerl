package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Vmapaseparacaocm;
import br.com.linkcom.wms.geral.dao.VmapaseparacaocmDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class VmapaseparacaocmService extends GenericService<Vmapaseparacaocm>{

	private VmapaseparacaocmDAO vmapaseparacaocmDAO;
		
	public void setVmapaseparacaocmDAO(VmapaseparacaocmDAO vmapaseparacaocmDAO) {
		this.vmapaseparacaocmDAO = vmapaseparacaocmDAO;
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * Recupera todos os resultados da view "vmapaseparacao" a partir do carregamento
	 * especificado.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.VmapaseparacaoDAO#findAllBy(Carregamento carregamento)
	 * @param carregamento
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Vmapaseparacaocm> findAllBy(Carregamento carregamento){
		return vmapaseparacaocmDAO.findAllBy(carregamento);
	}
	
	/* singleton */
	private static VmapaseparacaocmService instance;
	public static VmapaseparacaocmService getInstance() {
		if(instance == null){
			instance = Neo.getObject(VmapaseparacaocmService.class);
		}
		return instance;
	}
}
