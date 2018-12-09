package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Motivoretornoentrega;
import br.com.linkcom.wms.geral.bean.Statusconfirmacaoentrega;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class MotivoretornoentregaDAO extends GenericDAO<Motivoretornoentrega>{

	/**
	 *	Recupera todos os motivos que exige observação. 
	 */
	public List<Motivoretornoentrega> findAllExibeObservacao() {
		return query()
			.where("motivoretornoentrega.exigeobservacao = 1")
			.list();
	}

	/**
	 * 
	 * @param statusconfirmacaoentrega
	 * @return
	 */
	public List<Motivoretornoentrega> findByStatusconfirmacaoentrega(Statusconfirmacaoentrega statusconfirmacaoentrega) {
		return query()
			.where("motivoretornoentrega.statusconfirmacaoentrega = ?",statusconfirmacaoentrega)
			.list();
	}

}
