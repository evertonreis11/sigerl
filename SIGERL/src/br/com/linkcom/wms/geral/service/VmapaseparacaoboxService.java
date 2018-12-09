package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Vmapaseparacaobox;
import br.com.linkcom.wms.geral.dao.VmapaseparacaoboxDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class VmapaseparacaoboxService extends GenericService<Vmapaseparacaobox>{

	private static VmapaseparacaoboxService instance;
	private VmapaseparacaoboxDAO vmapaseparacaoBoxDAO;

	public static VmapaseparacaoboxService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(VmapaseparacaoboxService.class);
		}
		return instance;
	}
	
	public void setVmapaseparacaoBoxDAO(
			VmapaseparacaoboxDAO vmapaseparacaoBoxDAO) {
		this.vmapaseparacaoBoxDAO = vmapaseparacaoBoxDAO;
	}


	/**
	 * Busca os produtos para a geração do mapa de separação.
	 * 
	 * @author Giovane Freitas
	 * @param listaBoxes
	 */
	public List<Vmapaseparacaobox> findAllByBox(List<Box> listaBoxes) {
		return vmapaseparacaoBoxDAO.findAllByBox(listaBoxes);
	}
	
}
