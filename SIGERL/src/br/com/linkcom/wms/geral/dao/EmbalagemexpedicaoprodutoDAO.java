package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.wms.geral.bean.Embalagemexpedicaoproduto;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class EmbalagemexpedicaoprodutoDAO extends GenericDAO<Embalagemexpedicaoproduto>{

	/**
	 * Exclui todas as embalagens associadas a uma determinada ordem de serviço.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 */
	public void deleteByOrdem(Ordemservico ordemservico) {
		String hql = "delete from Embalagemexpedicaoproduto eep " +
				"where eep.embalagemexpedicao.id in (select ee.id from Embalagemexpedicao ee where ee.ordemservico.id = ? ) ";
		getHibernateTemplate().bulkUpdate(hql, ordemservico.getCdordemservico());		
	}

}
