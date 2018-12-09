package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Vordemexpedicaobox43;
import br.com.linkcom.wms.util.WmsException;

public class Vordemexpedicaobox43DAO extends GenericDAO<Vordemexpedicaobox43>{
	
	/**
	 * Lista os itens para a formação de conferência referente aos carregamentos de um box
	 * 
	 * @author Filipe Santos
	 * @param box
	 * @return
	 */
	@SuppressWarnings("static-access")
	public List<Vordemexpedicaobox43> findByBox(Box box) {
		if(box == null || box.getCdbox() == null)
			throw new WmsException("Box inválido.");

		return query()
			.joinFetch("vordemexpedicaobox43.linhaseparacao linhaseparacao")
			.joinFetch("vordemexpedicaobox43.carregamento carregamento")
			.joinFetch("vordemexpedicaobox43.cliente cliente")
			.joinFetch("vordemexpedicaobox43.tipooperacao tipooperacao")
			.whereIn("vordemexpedicaobox43.carregamento.id", Util.collections.listAndConcatenate(box.getListaCarregamentos(), "cdcarregamento", ","))
			.orderBy("vordemexpedicaobox43.box.id, vordemexpedicaobox43.carregamento.id, vordemexpedicaobox43.tipooperacao.id, vordemexpedicaobox43.linhaseparacao.usacheckout, " +
					"vordemexpedicaobox43.cliente.id, vordemexpedicaobox43.linhaseparacao.nome, vordemexpedicaobox43.produtoprincipal.id, vordemexpedicaobox43.volume.id")
			.list();
	}
	
}
