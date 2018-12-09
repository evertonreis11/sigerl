package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Reabastecimento;
import br.com.linkcom.wms.geral.bean.Reabastecimentolote;
import br.com.linkcom.wms.geral.dao.ReabastecimentoloteDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ReabastecimentoloteService extends GenericService<Reabastecimentolote> {

	private ReabastecimentoloteDAO reabastecimentoloteDAO;
	
	public void setReabastecimentoloteDAO(
			ReabastecimentoloteDAO reabastecimentoloteDAO) {
		this.reabastecimentoloteDAO = reabastecimentoloteDAO;
	}
	
	/**
	 * Exclui todos os {@link Reabastecimentolote} que estão associados a um determinado {@link Reabastecimento}.
	 * 
	 * @author Giovane Freitas
	 * @param reabastecimento
	 */
	public void deleteByReabastecimento(Reabastecimento reabastecimento) {
		reabastecimentoloteDAO.deleteByReabastecimento(reabastecimento);
	}

	/**
	 * Lista todos os {@link Reabastecimentolote} e suas respectivas ordens de serviço.
	 * 
	 * @autor Giovane Freitas
	 * @param reabastecimento
	 * @return
	 */
	public List<Reabastecimentolote> findByReabastecimento(Reabastecimento reabastecimento) {
		return reabastecimentoloteDAO.findByReabastecimento(reabastecimento);
	}

}
