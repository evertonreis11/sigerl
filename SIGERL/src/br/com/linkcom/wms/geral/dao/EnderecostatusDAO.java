package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.wms.geral.bean.Enderecostatus;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("nome")
public class EnderecostatusDAO extends GenericDAO<Enderecostatus> {	

}
