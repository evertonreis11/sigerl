package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Conferenciaordemrecebimento;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ConferenciaordemrecebimentoDAO extends GenericDAO<Conferenciaordemrecebimento> {
	
	public List<Conferenciaordemrecebimento> recuperaConferenciasPorRecebimento(Recebimento recebimento){
		return query()
				.join("conferenciaordemrecebimento.ordemprodutohistorico ordemprodutohistorico")
				.join("conferenciaordemrecebimento.recebimento recebimento")
				.joinFetch("conferenciaordemrecebimento.produtoembalagem embalagem")
				.where("recebimento = ?", recebimento)
				.where("coalesce(conferenciaordemrecebimento.conferenciafinalizada, 0) = ?", Boolean.FALSE)
				.orderBy("ordemprodutohistorico")
				.list();
	}

	/**
	 * Exclui os registros da conferencias pelo cdrecebimento.
	 *
	 * @param recebimento the recebimento
	 */
	public void excluiConferenciasPorRecebimento(Recebimento recebimento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder hql = new StringBuilder();
		hql.append("delete from Conferenciaordemrecebimento c where c.recebimento.cdrecebimento = ? ")
			.append(" and coalesce(c.conferenciafinalizada, 0) = ?");
		
		getHibernateTemplate().bulkUpdate(hql.toString(),
										  new Object[]{recebimento.getCdrecebimento(), Boolean.FALSE});
		
	}

}
