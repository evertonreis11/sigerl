package br.com.linkcom.wms.geral.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Reabastecimento;
import br.com.linkcom.wms.geral.bean.Reabastecimentolote;
import br.com.linkcom.wms.geral.bean.Reabastecimentostatus;
import br.com.linkcom.wms.geral.dao.ReabastecimentoDAO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ReabastecimentoService extends GenericService<Reabastecimento> {
	
	private ReabastecimentoDAO reabastecimentoDAO;
	private ReabastecimentoloteService reabastecimentoloteService;
	private OrdemservicoService ordemservicoService;
	
	private static ReabastecimentoService instance;
	public static ReabastecimentoService getIntance() {
		if (instance == null)
			instance = Neo.getObject(ReabastecimentoService.class);
		
		return instance;
	} 	
	
	public void setReabastecimentoDAO(ReabastecimentoDAO reabastecimentoDAO) {
		this.reabastecimentoDAO = reabastecimentoDAO;
	}
	
	public void setReabastecimentoloteService(
			ReabastecimentoloteService reabastecimentoloteService) {
		this.reabastecimentoloteService = reabastecimentoloteService;
	}
	
	public void setOrdemservicoService(OrdemservicoService ordemservicoService) {
		this.ordemservicoService = ordemservicoService;
	}
	
	@Override
	public void delete(Reabastecimento bean) {
		if (bean.getReabastecimentostatus() == null)
			bean = load(bean);
		
		if (bean.getReabastecimentostatus().equals(Reabastecimentostatus.NAO_INICIADO)){
			reabastecimentoloteService.deleteByReabastecimento(bean);
			super.delete(bean);
		}else
			throw new WmsException("Não é permitido excluir reabastecimento com situação diferente de \"Não iniciado\".");
	}
	
	@Override
	public void saveOrUpdate(Reabastecimento bean) {

		if (!bean.getReabastecimentostatus().equals(Reabastecimentostatus.NAO_INICIADO))
			throw new WmsException("Não é permitido editar reabastecimento com situação diferente de \"Não iniciado\".");
		
		super.saveOrUpdate(bean);
	}
	
	@Override
	public void saveOrUpdateNoUseTransaction(Reabastecimento bean) {

		if (!bean.getReabastecimentostatus().equals(Reabastecimentostatus.NAO_INICIADO))
			throw new WmsException("Não é permitido editar reabastecimento com situação diferente de \"Não iniciado\".");
		
		super.saveOrUpdateNoUseTransaction(bean);
	}

	/**
	 * Verifica se o os lotes possuem alguma ordem em execução
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param listaInventariolote
	 * @return
	 */
	public boolean containsOrdemEmExecucao(Collection<Reabastecimentolote> lotes) {
		List<Ordemservico> listaOS = null;
		for(Reabastecimentolote lote : lotes){
			listaOS = ordemservicoService.findBy(lote, "ordemstatus.cdordemstatus");
			if(listaOS != null && !listaOS.isEmpty()){
				for (Ordemservico ordem : listaOS)
					if (!ordem.getOrdemstatus().equals(Ordemstatus.EM_ABERTO) && !ordem.getOrdemstatus().equals(Ordemstatus.CANCELADO))
						return true;
			}
		}
		return false;
	}

	/**
	 * Cancela todas as ordens de serviço de um determinado reabastecimento.
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param listaInventariolote
	 * @return
	 */
	public void cancelaOrdensServico(Reabastecimento reabastecimento) {
		List<Reabastecimentolote> lotes = reabastecimentoloteService.findByReabastecimento(reabastecimento);
		for (Reabastecimentolote lote : lotes){
			List<Ordemservico> listaOS = ordemservicoService.findBy(lote);
			for(Ordemservico ordemservico : listaOS){
				ordemservicoService.cancelar(ordemservico);
			}
		}
		
		reabastecimento.setReabastecimentostatus(Reabastecimentostatus.CANCELADO);
		this.atualizarStatus(reabastecimento);
	}

	/**
	 * Atualiza o status de um determinado reabastecimento.
	 * 
	 * @param reabastecimento
	 */
	public void atualizarStatus(Reabastecimento reabastecimento) {
		reabastecimentoDAO.atualizarStatus(reabastecimento);
	}
	
	/**
	 * Método de referencia ao DAO
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param reabastecimento
	 * @throws SQLException 
	 */
	public void iniciarReabastecimento(Reabastecimento reabastecimento) {
		reabastecimentoDAO.iniciarReabastecimento(reabastecimento);
	}

	/**
	 * Localiza um reabastecimento a partir da ordem de serviço.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Reabastecimento findByOrdemservico(Ordemservico ordemservico) {
		return reabastecimentoDAO.findByOrdemservico(ordemservico);
	}

	/**
	 * Verifica se a ordem de serviço está associada a um
	 * {@link Reabastecimento}, se estiver, verifica se todas a ordens já foram
	 * finalizadas, se já foram finalizadas então altera o status do
	 * reabastecimento para finalizado.
	 * 
	 * @author Giovane Freitas
	 * @param ordemFinalizada
	 */
	public void finalizarSeAcabou(Ordemservico ordemFinalizada) {
		//Se for uma ordem associada a um reabastecimento preventivo atualiza o status dele
		Reabastecimentostatus status = null;
		boolean todasFinalizadas = true;
		Reabastecimento reabastecimento = this.findByOrdemservico(ordemFinalizada);
		
		if((reabastecimento != null)) {
			boolean divergencia = false;
			List<Reabastecimentolote> listaLote = Neo.getObject(ReabastecimentoloteService.class).findByReabastecimento(reabastecimento);
			
			// verifica se o reabastecimento pode ser finalizado.
			for (Reabastecimentolote lote : listaLote) {
				//Alguns lotes podem não ter ordem de serviço
				if (lote.getListaOrdemservico() == null)
					lote.setListaOrdemservico(new ArrayList<Ordemservico>());
				
				for (Ordemservico ordemservico : lote.getListaOrdemservico()){
					if(!ordemservico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_SUCESSO) && !ordemservico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_DIVERGENCIA) && !ordemservico.getOrdemstatus().equals(Ordemstatus.CANCELADO)){
						todasFinalizadas = false;
					}
					if (ordemservico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_DIVERGENCIA))
						divergencia = true;
				}
			}
			
			if (todasFinalizadas){
				if (divergencia)
					status = Reabastecimentostatus.FINALIZADO_DIVERGENCIA;
				else
					status = Reabastecimentostatus.FINALIZADO_SUCESSO;
				
				// atualiza o status do inventario
				reabastecimento.setReabastecimentostatus(status);
				this.atualizarStatus(reabastecimento);
			}
		}

	}
	
}
