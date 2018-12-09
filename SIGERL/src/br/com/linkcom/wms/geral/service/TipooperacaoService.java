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
	 * M�todo de refer�ncia ao DAO
	 * Encontra todos os tipos de opera��o
	 * 
	 * @author Pedro Gon�alves
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
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
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
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
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
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * 
	 * @param tipooperacao
	 * @return
	 */
	public Boolean imprimeEtiqueta(Tipooperacao tipooperacao) {
		return tipooperacaoDAO.imprimeEtiqueta(tipooperacao);
	}

	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @return
	 * @author Tom�s Rabelo
	 */
	public List<Tipooperacao> getTipoOperacaoEntregaClienteTransferenciaFilial() {
		return tipooperacaoDAO.getTipoOperacaoEntregaClienteTransferenciaFilial();
	}

	/**
	 * Analisa se na lista de pedidos de venda produtos n�o existe nenhum item com 
	 * o tipo opera��o de autoriza��o junto com algum outro tipo.
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
