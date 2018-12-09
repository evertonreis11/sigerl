package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Vordemexpedicaoboxcm;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class VordemexpedicaoboxcmDAO extends GenericDAO<Vordemexpedicaoboxcm>{

	@SuppressWarnings("static-access")
	public List<Vordemexpedicaoboxcm> findByBox(Box box){
		if(box == null || box.getCdbox() == null)
			throw new WmsException("Box inválido.");

		return query()
			.joinFetch("vordemexpedicaoboxcm.linhaseparacao linhaseparacao")
			.joinFetch("vordemexpedicaoboxcm.carregamento carregamento")
			.joinFetch("vordemexpedicaoboxcm.cliente cliente")
			.joinFetch("vordemexpedicaoboxcm.tipooperacao tipooperacao")
			.leftOuterJoinFetch("vordemexpedicaoboxcm.produtoembalagem produtoembalagem")
			.where("vordemexpedicaoboxcm.qtde > 0")
			.whereIn("vordemexpedicaoboxcm.carregamento.id", Util.collections.listAndConcatenate(box.getListaCarregamentos(), "cdcarregamento", ","))
			.orderBy("vordemexpedicaoboxcm.box.id, vordemexpedicaoboxcm.carregamento.id, vordemexpedicaoboxcm.cliente.id, vordemexpedicaoboxcm.tipooperacao.id, " +
					"vordemexpedicaoboxcm.linhaseparacao.usacheckout,vordemexpedicaoboxcm.linhaseparacao.nome, vordemexpedicaoboxcm.produtoprincipal.id, vordemexpedicaoboxcm.volume.id")
			.list();
	}
	
}
