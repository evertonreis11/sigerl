package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Vmapaseparacao;
import br.com.linkcom.wms.geral.dao.VmapaseparacaoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class VmapaseparacaoService extends GenericService<Vmapaseparacao>{
	
	private VmapaseparacaoDAO vmapaseparacaoDAO;
	
	public void setVmapaseparacaoDAO(VmapaseparacaoDAO vmapaseparacaoDAO) {
		this.vmapaseparacaoDAO = vmapaseparacaoDAO;
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
	public List<Vmapaseparacao> findAllBy(Carregamento carregamento){
		return vmapaseparacaoDAO.findAllBy(carregamento);
	}
	
	/* singleton */
	private static VmapaseparacaoService instance;
	public static VmapaseparacaoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(VmapaseparacaoService.class);
		}
		return instance;
	}
	
}
