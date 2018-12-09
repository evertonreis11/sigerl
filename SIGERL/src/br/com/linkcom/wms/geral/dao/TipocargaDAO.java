package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.wms.geral.bean.Tipocarga;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("nome")
public class TipocargaDAO extends GenericDAO<Tipocarga> {

}
