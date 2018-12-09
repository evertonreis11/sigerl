package br.com.linkcom.wms.modulo.logistica.controller.process.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;

@Bean
public class OrdemReconferenciaService {
	
	protected TransactionTemplate transactionTemplate;
	protected OrdemservicoService ordemservicoService;
	
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	public void setOrdemservicoService(OrdemservicoService ordemservicoService) {
		this.ordemservicoService = ordemservicoService;
	}
	
	/**
	 * Processa os dados e cria uma ordem de recontagem
	 * 
	 * @author Leonardo Guimarães
	 * @param responsavel 
	 * @param ordemservico 
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoService#updateStatusordemservico(Ordemservico ordemservico)
	 * @see br.com.linkcom.wms.geral.service.InventarioloteService#findByOS(Ordemservico ordemservico)
	 * 
	 * @param filtro
	 */
	public String gerarReconferencia(final Usuario responsavel, final Ordemservico ordemservico, final Timestamp horaInicio, final Timestamp horaFim) {
		
		//Se a O.S. já foi finalizada via coletor não precisarei do responsável
		if(responsavel == null && !Ordemstatus.AGUARDANDO_CONFIRMACAO.equals(ordemservico.getOrdemstatus()))
			return "O campo Responsável é obrigatório.";
		
		final boolean exigeDuasContagens = ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.EXIGIR_DUAS_CONTAGENS_INVENTARIO, null);
		final boolean isPrimeiraContagem = ordemservicoService.isPrimeiraOrdemServicoContagemInventario(ordemservico);

		if( exigeDuasContagens || verificaDivergencias(ordemservico.getListaOrdemProdutoHistorico())){
			transactionTemplate.execute(new TransactionCallback(){
				public Object doInTransaction(TransactionStatus status) {
					ordemservicoService.gravarContagemInventario(ordemservico, responsavel, horaInicio, horaFim, false);
					
					if ( (exigeDuasContagens && isPrimeiraContagem) || Ordemstatus.FINALIZADO_DIVERGENCIA.equals(ordemservico.getOrdemstatus()))
						ordemservicoService.criarContagemRecontagem(ordemservico, null);
					
					return null;
				}

			});
			
		} else			
			return "Não foram encontradas divergências neste lote.";
		
		return "";
	}
	
	/**
	 * Verifica a existência de divergencias entre os produtos do lote
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param listaOrdemProdutoHistorico
	 * @return
	 */
	private boolean verificaDivergencias(List<Ordemprodutohistorico> listaOrdemProdutoHistorico) {
		for (Ordemprodutohistorico oph : listaOrdemProdutoHistorico) {
			Long qtdeesperada = oph.getOrdemservicoproduto().getQtdeesperada() != null ? oph.getOrdemservicoproduto().getQtdeesperada() : 0L;

			if (oph.getQtde() == null || !qtdeesperada.equals(oph.getQtde()))
				return true;
		}
		return false;
	}

}
