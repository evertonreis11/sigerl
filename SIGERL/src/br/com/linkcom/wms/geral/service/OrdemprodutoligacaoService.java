package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemprodutoligacao;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.dao.OrdemprodutoligacaoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class OrdemprodutoligacaoService extends GenericService<Ordemprodutoligacao>{
	
	protected OrdemprodutoligacaoDAO ordemprodutoligacaoDAO;
	
	public void setOrdemprodutoligacaoDAO(OrdemprodutoligacaoDAO ordemprodutoligacaoDAO) {
		this.ordemprodutoligacaoDAO = ordemprodutoligacaoDAO;
	}
	
	/**
	 * Salva as ordens de servico do recebimento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordemServico
	 * @param listaOrdemServicoProduto
	 */
	public void salvarOrdemProdutoLigacao(Ordemservico ordemservico,List<Ordemservicoproduto> listaOrdemServicoProduto) {
		for (Ordemservicoproduto ordemservicoproduto : listaOrdemServicoProduto) {
			for (Ordemprodutoligacao ordemprodutoligacao : ordemservicoproduto.getListaOrdemprodutoLigacao()) {
				ordemprodutoligacao.setOrdemservico(ordemservico);
				saveOrUpdateNoUseTransaction(ordemprodutoligacao);
			}			
		}
	}
	
	/**
	 * Salva as ordens de servico do recebimento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordemServico
	 * @param listaOrdemProdutoHistorico
	 */
	public void salvarOrdemProdutoLigacaoByHistorico(Ordemservico ordemservico,List<Ordemprodutohistorico> listaOrdemProdutoHistorico) {
		for (Ordemprodutohistorico ordemprodutohistorico : listaOrdemProdutoHistorico) {
			for (Ordemprodutoligacao ordemprodutoligacao : ordemprodutohistorico.getOrdemservicoproduto().getListaOrdemprodutoLigacao()) {
				ordemprodutoligacao.setOrdemservico(ordemservico);
				saveOrUpdateNoUseTransaction(ordemprodutoligacao);
			}			
		}
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemprodutoligacaoDAO#deleteOrdemProdutoLigacao(Ordemservico ordemservico)
	 * 
	 * @param ordemservico
	 */
	public void deleteOrdemProdutoLigacao(Ordemservico ordemservico) {
		ordemprodutoligacaoDAO.deleteOrdemProdutoLigacao(ordemservico);
	}
	
	/**
	 * excluir todos os Ordemprodutoligacao através da ordemservico produto.
	 * @author Pedro gonçalves
	 * @param listaOSP
	 */
	public void deleteAllBy(String listaOSP) {
		ordemprodutoligacaoDAO.deleteAllBy(listaOSP);
	}
	
	/* singleton */
	private static OrdemprodutoligacaoService instance;
	public static OrdemprodutoligacaoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(OrdemprodutoligacaoService.class);
		}
		return instance;
	}

}
