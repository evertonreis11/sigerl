package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("carregamentostatus.nome")
public class CarregamentostatusDAO extends GenericDAO<Carregamentostatus>{

}
