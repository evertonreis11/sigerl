package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Vmapaseparacaoboxcm;
import br.com.linkcom.wms.geral.dao.VmapaseparacaoboxcmDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class VmapaseparacaoboxcmService extends GenericService<Vmapaseparacaoboxcm>{

	private static VmapaseparacaoboxcmService instance;
	private VmapaseparacaoboxcmDAO vmapaseparacaoboxcmDAO;

	public static VmapaseparacaoboxcmService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(VmapaseparacaoboxcmService.class);
		}
		return instance;
	}

	public void setVmapaseparacaoboxcmDAO(VmapaseparacaoboxcmDAO vmapaseparacaoboxcmDAO) {
		this.vmapaseparacaoboxcmDAO = vmapaseparacaoboxcmDAO;
	}

	/**
	 * Busca os produtos para a geração do mapa de separação.
	 * 
	 * @author Giovane Freitas
	 * @param listaBoxes
	 */
	public List<Vmapaseparacaoboxcm> findAllByBox(List<Box> listaBoxes) {
		return vmapaseparacaoboxcmDAO.findAllByBox(listaBoxes);
	}
}
