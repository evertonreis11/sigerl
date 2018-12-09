package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.wms.geral.bean.Pedidocomprastatus;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("nome")
public class PedidocomprastatusDAO extends GenericDAO<Pedidocomprastatus> {	

}
