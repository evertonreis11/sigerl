package br.com.linkcom.wms.geral.service;

import java.sql.Timestamp;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Agendahistorico;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class AgendahistoricoService extends GenericService<Agendahistorico> {

	private static AgendahistoricoService instance;

	public static AgendahistoricoService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(AgendahistoricoService.class);
		}
		return instance;
	}
	
	/**
	 * Método  criarHistoricoAlteracaoReduzirQuantidadeAgendada que é invocado no Webservice
	 * pelo método reduzirQtdeAgendada.
	 * @author Marcus Vinicius Fernandino Fonseca
	 */
	public void criarHistoricoAlteracaoReduzirQuantidadeAgendada(int novaQuantidade,int cdAgenda,String codigo,String descricao) {
		Agenda agenda = new Agenda(cdAgenda);
		Agendahistorico historico = new Agendahistorico();
		historico.setAgenda(agenda);
		historico.setUsuarioaltera(null);
		historico.setDtalteracao(new Timestamp(System.currentTimeMillis()));
		historico.setDescricao("Produto " + codigo + " - " + descricao + " removido do agendamento porque sua quantidade foi alterada para " + novaQuantidade + ".");
		saveOrUpdate(historico);
	}
	
}
