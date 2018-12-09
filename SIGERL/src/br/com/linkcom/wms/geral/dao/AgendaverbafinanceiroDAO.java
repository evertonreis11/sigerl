package br.com.linkcom.wms.geral.dao;

import java.util.Date;
import java.util.List;

import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Agendaverba;
import br.com.linkcom.wms.geral.bean.Agendaverbafinanceiro;
import br.com.linkcom.wms.geral.bean.Configuracao;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Produtoclasse;
import br.com.linkcom.wms.geral.service.ProdutoclasseService;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;
public class AgendaverbafinanceiroDAO extends GenericDAO<Agendaverbafinanceiro> {

	private ProdutoclasseService produtoclasseService;
	
	public void setProdutoclasseService(ProdutoclasseService produtoclasseService) {
		this.produtoclasseService = produtoclasseService;
	}
	
	/**
	 * Busca a lista de verbas para agendamento de acordo com os parâmetros.
	 *
	 * @param deposito
	 * @param produtoclasse
	 * @param dtprevisao
	 * @return
	 * @author Rodrigo Freitas
	 */
	public List<Agendaverbafinanceiro> findListaVerba(Deposito deposito, Produtoclasse produtoclasse, Date dtagenda, Date dtprevisao) {
		if (produtoclasse == null || produtoclasse.getNumero() == null || dtprevisao == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
					.select("agendaverbafinanceiro.cdagendaverbafinanceiro, agendaverbafinanceiro.verba")
					.join("agendaverbafinanceiro.agendaverba agendaverba")
					.join("agendaverba.produtoclasse produtoclasse")
					.where("agendaverba.deposito = ?", deposito)
					.where("agendaverba.deposito.ativo = 1")
					.where("produtoclasse.numero = ?", produtoclasseService.loadWithV_produtoclasse(produtoclasse).getV_produtoclasse().getProdutoclasse().getNumero())
					.where("agendaverba.dtagendaverba >= ?", WmsUtil.firstDateOfMonth(dtagenda))
					.where("agendaverba.dtagendaverba <= ?", WmsUtil.lastDateOfMonth(dtagenda))
					.where("agendaverbafinanceiro.dtagendaverba >= ?", WmsUtil.firstDateOfMonth(dtprevisao))
					.where("agendaverbafinanceiro.dtagendaverba <= ?", WmsUtil.lastDateOfMonth(dtprevisao))
					.list();
	}

	public List<Agendaverbafinanceiro> verbaFinanceiroByVerbaPeriodo(Deposito deposito, Produtoclasse produtoclasse, Date dtverbaFinanceiro ) {
		if (produtoclasse == null || produtoclasse.getNumero() == null || dtverbaFinanceiro == null)
			throw new WmsException("Parâmetros inválidos.");
		QueryBuilder<Agendaverbafinanceiro> query = query()
		.select("agendaverbafinanceiro.cdagendaverbafinanceiro, agendaverbafinanceiro.verba, agendaverbafinanceiro.dtagendaverba, agendaverba.cdagendaverba")
		.join("agendaverbafinanceiro.agendaverba agendaverba")
		.join("agendaverba.produtoclasse produtoclasse")
		.where("agendaverba.dtagendaverba >= ?", WmsUtil.firstDateOfMonth(dtverbaFinanceiro))
		.where("agendaverba.dtagendaverba <= ?", WmsUtil.lastDateOfMonth(dtverbaFinanceiro));
		
		if(produtoclasse.getAgendaverbaControle() != null && produtoclasse.getAgendaverbaControle().getCdagendaverba() != null){
			query.where("agendaverba  = ?", produtoclasse.getAgendaverbaControle());
		}
		if(deposito != null && deposito.getCddeposito() > 0){
			query.where("agendaverba.deposito = ?", deposito);
		}
		query.where("produtoclasse.numero = ?", produtoclasseService.loadWithV_produtoclasse(produtoclasse).getV_produtoclasse().getProdutoclasse().getNumero());
//		
//		query.where("produtoclasse.numero = ?", produtoclasse.getNumero().substring(0, 2));
//		query.where("agendaverbafinanceiro.dtagendaverba >= ?", WmsUtil.firstDateOfMonth(dtverbaFinanceiro));
//		query.where("agendaverbafinanceiro.dtagendaverba <= ?", WmsUtil.lastDateOfMonth(dtverbaFinanceiro));
		
//		query.where("agendaverba.deposito = ?", deposito)
//		.where("produtoclasse.numero = ?", produtoclasse.getNumero().substring(0, 2));
//		.where("agendaverbafinanceiro.dtagendaverba >= ?", WmsUtil.firstDateOfMonth(dtverbaFinanceiro))
//		.where("agendaverbafinanceiro.dtagendaverba <= ?", WmsUtil.lastDateOfMonth(dtverbaFinanceiro))

		return query.list();
	}

}
