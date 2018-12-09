package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.dao.TipooperacaoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;


public class TipooperacaoService extends GenericService<Tipooperacao> {

	private TipooperacaoDAO tipooperacaoDAO;
	
	public void setTipooperacaoDAO(TipooperacaoDAO tipooperacaoDAO) {
		this.tipooperacaoDAO = tipooperacaoDAO;
	}
	
	/* singleton */
	private static TipooperacaoService instance;
	public static TipooperacaoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(TipooperacaoService.class);
		}
		return instance;
	}
	
	/**
	 * Método de referência ao DAO
	 * Encontra todos os tipos de operação
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @see br.com.linkcom.wms.geral.dao.TipooperacaoDAO#findAllForFlex()
	 * 
	 * @param tipooperacao
	 * @return
	 */
	public List<Tipooperacao> findAllForFlex() {
		return tipooperacaoDAO.findAllForFlex();
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.TipooperacaoDAO.findByCd(Integer cdtipooperacao)
	 * 
	 * @param cdtipooperacao
	 * @return
	 */
	public Tipooperacao findByCd(Integer cdtipooperacao) {
		return tipooperacaoDAO.findByCd(cdtipooperacao);
	}

	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.TipooperacaoDAO#findByOrdemServico(Ordemservico ordemservico)
	 * 
	 * @param ordemservico
	 * @return
	 */
	public Tipooperacao findByOrdemServico(Ordemservico ordemservico) {
		return tipooperacaoDAO.findByOrdemServico(ordemservico);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * 
	 * @param tipooperacao
	 * @return
	 */
	public Boolean imprimeEtiqueta(Tipooperacao tipooperacao) {
		return tipooperacaoDAO.imprimeEtiqueta(tipooperacao);
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @return
	 * @author Tomás Rabelo
	 */
	public List<Tipooperacao> getTipoOperacaoEntregaClienteTransferenciaFilial() {
		return tipooperacaoDAO.getTipoOperacaoEntregaClienteTransferenciaFilial();
	}

	/**
	 * Analisa se na lista de pedidos de venda produtos não existe nenhum item com 
	 * o tipo operação de autorização junto com algum outro tipo.
	 * 
	 * @param listaPedidoVendaProduto
	 * @author Filipe Santos
	 * @return
	 */
	public boolean existeToAutorizacao(String listaPvp) {		
		List<Tipooperacao> listaTipooperacao = tipooperacaoDAO.findByPedidovendaproduto(listaPvp);
		Boolean autorizacao = false;
		for (Tipooperacao tipooperacao : listaTipooperacao) {
			if(tipooperacao.getCdtipooperacao()==7){
				autorizacao = true;
				break;
			}
		}
		if(autorizacao == true){
			for (Tipooperacao tipooperacao : listaTipooperacao) {
				if(tipooperacao.getCdtipooperacao()!=7)
					return true;
			}	
		}
		return false;
	}
}
