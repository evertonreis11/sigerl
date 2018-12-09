package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Vmapaseparacaoboxcm;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class VmapaseparacaoboxcmDAO extends GenericDAO<Vmapaseparacaoboxcm>{

	/**
	 * Busca os produtos para a geração do mapa de separação.
	 * 
	 * @author Giovane Freitas
	 * @param listaBoxes
	 */
	@SuppressWarnings("static-access")
	public List<Vmapaseparacaoboxcm> findAllByBox(List<Box> listaBoxes) {
		return query()
			.joinFetch("vmapaseparacaoboxcm.carregamento carregamento")
			.where("vmapaseparacaoboxcm.qtde > 0")
			.whereIn("vmapaseparacaoboxcm.box.id", Util.collections.listAndConcatenate(listaBoxes, "cdbox", ","))			
			.orderBy("vmapaseparacaoboxcm.linhaseparacao.id, vmapaseparacaoboxcm.box.id, vmapaseparacaoboxcm.tipooperacao.id, vmapaseparacaoboxcm.cliente.id")
			.list();
	}

}
