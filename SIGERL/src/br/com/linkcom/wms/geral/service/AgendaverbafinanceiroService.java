package br.com.linkcom.wms.geral.service;

import java.util.Date;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Agendaverbafinanceiro;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Produtoclasse;
import br.com.linkcom.wms.geral.dao.AgendaverbafinanceiroDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class AgendaverbafinanceiroService extends GenericService<Agendaverbafinanceiro> {

	private AgendaverbafinanceiroDAO agendaverbafinanceiroDAO;
	
	public void setAgendaverbafinanceiroDAO(
			AgendaverbafinanceiroDAO agendaverbafinanceiroDAO) {
		this.agendaverbafinanceiroDAO = agendaverbafinanceiroDAO;
	}
	
	private static AgendaverbafinanceiroService instance;

	public static AgendaverbafinanceiroService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(AgendaverbafinanceiroService.class);
		}
		return instance;
	}

	/**
	 * Busca o somatório da verba financeira de acordo com os parâmetros.
	 *
	 * @param deposito
	 * @param produtoclasse
	 * @param dtprevisao
	 * @return
	 * @author Rodrigo Freitas
	 */
	public Money findVerbaFinanceiro(Deposito deposito, Produtoclasse produtoclasse, Date dtagenda, Date dtprevisao) {
		List<Agendaverbafinanceiro> lista = agendaverbafinanceiroDAO.findListaVerba(deposito, produtoclasse, dtagenda, dtprevisao);
		Money verba = new Money();
		for (Agendaverbafinanceiro af : lista) {
			if(af.getVerba() != null){
				verba = verba.add(af.getVerba());
			}
		}
		return verba;
	}
	
	public List<Agendaverbafinanceiro> verbaFinanceiroByVerbaPeriodo(Deposito deposito, Produtoclasse produtoclasse, Date dtverbaFinanceiro ) {
		List<Agendaverbafinanceiro> lista = agendaverbafinanceiroDAO.verbaFinanceiroByVerbaPeriodo(deposito, produtoclasse, dtverbaFinanceiro);
//		Money verba = new Money(0);
//		if(lista != null && lista.size() > 0){
//			for (Agendaverbafinanceiro af : lista) {
//				if(af.getVerba() != null){
//					verba = verba.add(af.getVerba());
//				}
//			}
//		}
		return lista;
	}
	
}
