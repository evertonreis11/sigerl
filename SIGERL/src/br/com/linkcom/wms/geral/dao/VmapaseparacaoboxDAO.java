package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Vmapaseparacaobox;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class VmapaseparacaoboxDAO extends GenericDAO<Vmapaseparacaobox> {

	/**
	 * Busca os produtos para a geração do mapa de separação.
	 * 
	 * @author Giovane Freitas
	 * @param listaBoxes
	 */
	@SuppressWarnings("static-access")
	public List<Vmapaseparacaobox> findAllByBox(List<Box> listaBoxes) {
		return query()
			.joinFetch("vmapaseparacaobox.carregamento carregamento")
			.whereIn("vmapaseparacaobox.box.id", Util.collections.listAndConcatenate(listaBoxes, "cdbox", ","))
			.orderBy("vmapaseparacaobox.linhaseparacao.id, vmapaseparacaobox.box.id, vmapaseparacaobox.tipooperacao.id, vmapaseparacaobox.cliente.id")
			.list();
	}


}
