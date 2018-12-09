package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.wms.geral.bean.Ordemprodutoligacao;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class OrdemprodutoligacaoDAO extends GenericDAO<Ordemprodutoligacao>{
	
	
	/**
	 * Remove todas as ordens de liga��o da ordem de servi�o
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param ordemservico
	 */
	public void deleteOrdemProdutoLigacao(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null){
			throw new WmsException("A ordem de servi�o n�o deve ser nula.");
		}
		getHibernateTemplate().bulkUpdate("delete Ordemprodutoligacao opl where opl.ordemservico = ? ",new Object[]{ordemservico} );
	}
	
	/**
	 * excluir todos os Ordemprodutoligacao atrav�s da ordemservico produto.
	 * @author Pedro gon�alves
	 * @param listaOSP
	 */
	public void deleteAllBy(String listaOSP) {
		if(listaOSP == null){
			throw new WmsException("Par�metros inv�lidos");
		}
		
		getHibernateTemplate().bulkUpdate("delete Ordemprodutoligacao opl where opl.ordemservicoproduto.id in ("+listaOSP+")");
	}
	
	
	
}
