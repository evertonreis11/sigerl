package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Reabastecimento;
import br.com.linkcom.wms.geral.bean.Reabastecimentolote;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ReabastecimentoloteDAO extends GenericDAO<Reabastecimentolote>{

	/**
	 * Exclui todos os {@link Reabastecimentolote} que estão associados a um determinado {@link Reabastecimento}.
	 * 
	 * @author Giovane Freitas
	 * @param reabastecimento
	 */
	public void deleteByReabastecimento(Reabastecimento reabastecimento) {
		getHibernateTemplate().bulkUpdate("delete from Reabastecimentolote lote where lote.reabastecimento.id = ? ",
				new Object[]{reabastecimento.getCdreabastecimento()});
	}

	/**
	 * Lista todos os {@link Reabastecimentolote} e suas respectivas ordens de serviço.
	 * 
	 * @autor Giovane Freitas
	 * @param reabastecimento
	 * @return
	 */
	public List<Reabastecimentolote> findByReabastecimento(Reabastecimento reabastecimento) {
		if (reabastecimento == null || reabastecimento.getCdreabastecimento() == null)
			throw new WmsException("Parâmetros inválidos.");

		return query()
			.leftOuterJoinFetch("reabastecimentolote.listaOrdemservico ordemservico")
			.leftOuterJoinFetch("ordemservico.ordemstatus ordemstatus")
			.where("reabastecimentolote.reabastecimento = ?", reabastecimento)
			.list();
	}

}
