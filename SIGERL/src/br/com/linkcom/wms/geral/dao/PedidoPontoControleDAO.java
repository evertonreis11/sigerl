package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.PedidoPontoControle;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class PedidoPontoControleDAO extends GenericDAO<PedidoPontoControle> {

	public List<PedidoPontoControle> recuperaPontoControlePedido(Long numeroPedido) {
		return query().where("pedidoErp = ? ", numeroPedido).orderBy("dtInclusao").list();
	}

}
