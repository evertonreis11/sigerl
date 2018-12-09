package br.com.linkcom.wms.geral.dao;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Reabastecimento;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.ReabastecimentoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ReabastecimentoDAO extends GenericDAO<Reabastecimento>{

	@Override
	public void updateListagemQuery(QueryBuilder<Reabastecimento> query,
			FiltroListagem _filtro) {

		ReabastecimentoFiltro filtro = (ReabastecimentoFiltro)_filtro;
		query
			.select("reabastecimento.cdreabastecimento,reabastecimento.dtreabastecimento, " +
					"reabastecimentostatus.cdreabastecimentostatus,reabastecimentostatus.nome")
			.join("reabastecimento.reabastecimentostatus reabastecimentostatus")
			.join("reabastecimento.deposito deposito")
			.where("trunc(reabastecimento.dtreabastecimento) >= trunc(?)",filtro.getDatainicial())
			.where("trunc(reabastecimento.dtreabastecimento) <= trunc(?)",filtro.getDatafinal())
			.where("reabastecimentostatus = ?",filtro.getSituacao())
			.where("reabastecimento.cdreabastecimento = ?",filtro.getCdreabastecimento())
			.where("deposito = ?",WmsUtil.getDeposito())
			.orderBy("reabastecimento.dtreabastecimento DESC");
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Reabastecimento> query) {
		super.updateEntradaQuery(query);
		
		query.select("reabastecimento.cdreabastecimento,reabastecimento.dtreabastecimento," +
				"status.cdreabastecimentostatus,status.nome,lote.cdreabastecimentolote," +
				"lote.ruainicial,lote.predioinicial,lote.nivelinicial,lote.aptoinicial," +
				"lote.ruafinal,lote.prediofinal,lote.nivelfinal,lote.aptofinal,lote.pontoreposicao," +
				"produto.cdproduto,produto.codigo,produto.descricao,pp.cdproduto," +
				"pp.codigo,pp.descricao,area.cdarea,area.codigo,enderecolado.cdenderecolado," +
				"enderecolado.nome,tiporeposicao.cdtiporeposicao,tiporeposicao.nome," +
				"ordemservico.cdordemservico,ordemstatus.cdordemstatus,ordemstatus.nome")
			.join("reabastecimento.reabastecimentostatus status")
			.leftOuterJoin("reabastecimento.listaReabastecimentolote lote")
			.leftOuterJoin("lote.produto produto")
			.leftOuterJoin("produto.produtoprincipal pp")
			.leftOuterJoin("lote.area area")
			.leftOuterJoin("lote.enderecolado enderecolado")
			.leftOuterJoin("lote.tiporeposicao tiporeposicao")
			.leftOuterJoin("lote.listaOrdemservico ordemservico")
			.leftOuterJoin("ordemservico.ordemstatus ordemstatus");
	}
	
	@Override
	public Reabastecimento load(Reabastecimento bean) {
		return query()
			.leftOuterJoinFetch("reabastecimento.reabastecimentostatus status")
			.entity(bean)
			.unique();
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaReabastecimentolote");
	}
	
	/**
	 * Chama a procedure 'INICIAR_REABASTECIMENTO' no banco de dados do sistema
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param reabastecimento
	 * @throws WmsException - caso o parâmetro seja nulo ou caso a procedure retorne algum erro.
	 */
	public void iniciarReabastecimento(final Reabastecimento reabastecimento) {
		if(reabastecimento == null || reabastecimento.getCdreabastecimento() == null)
			throw new WmsException("O reabastecimento não pode ser nulo.");

		try{
			getHibernateTemplate().execute(new HibernateCallback(){

				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					session.getNamedQuery("iniciar_reabastecimento")
						.setInteger(0, reabastecimento.getCdreabastecimento())
						.executeUpdate();
					
					return null;
				}
				
			});
		}catch(Exception e){
			Pattern padrao = Pattern.compile(".*###(.*)###.*",Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
			Matcher matcher = padrao.matcher(e.getMessage());
			String msgRetorno;
			if (matcher.matches()) {
				msgRetorno = matcher.group(1);
			} else {
				msgRetorno = e.getMessage();
			}
			throw new WmsException(msgRetorno, e);
		}
	}

	/**
	 * Atualiza o status de um determinado reabastecimento.
	 * 
	 * @param reabastecimento
	 */
	public void atualizarStatus(Reabastecimento reabastecimento) {
		if( reabastecimento == null || reabastecimento.getCdreabastecimento() == null ||
				reabastecimento.getReabastecimentostatus() == null || reabastecimento.getReabastecimentostatus().getCdreabastecimentostatus() == null){
				throw new WmsException("Dados insuficientes para alterar o status do reabastecimento.");
			}
			
		getHibernateTemplate().bulkUpdate("update Reabastecimento reabastecimento set reabastecimento.reabastecimentostatus = ? where reabastecimento = ?",
												new Object[]{reabastecimento.getReabastecimentostatus(),reabastecimento});
	}

	/**
	 * Localiza um reabastecimento a partir da ordem de serviço.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Reabastecimento findByOrdemservico(Ordemservico ordemservico) {
		return query()
			.join("reabastecimento.listaReabastecimentolote lote")
			.join("lote.listaOrdemservico ordemservico")
			.where("ordemservico = ?", ordemservico)
			.unique();
	}	
	
}
