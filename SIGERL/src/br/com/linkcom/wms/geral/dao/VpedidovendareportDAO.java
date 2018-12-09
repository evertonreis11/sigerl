package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.view.Vpedidovendareport;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.VpedidovendareportFiltro;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class VpedidovendareportDAO extends GenericDAO<Vpedidovendareport>{
	
	/**
	 * 
	 * 
	 * @author Filipe Santos
	 * @param filtro 
	 * @return List<Vpedidovendareport>
	 */
	public List<Vpedidovendareport> findForReport(VpedidovendareportFiltro filtro) {
		
		Integer cdcarregamentostatus = null;
		Integer cddeposito = null;
		Integer cdtipooperacao = null;
		Integer cdtiporotapraca = null;
		
		if(filtro.getCarregamentostatus()!=null && filtro.getCarregamentostatus().getCdcarregamentostatus()!=null)
			cdcarregamentostatus = filtro.getCarregamentostatus().getCdcarregamentostatus();
		if(filtro.getDeposito()!=null && filtro.getDeposito().getCddeposito()!=null)
			cddeposito = filtro.getDeposito().getCddeposito();
		if(filtro.getTipooperacao()!=null && filtro.getTipooperacao().getCdtipooperacao()!=null)
			cdtipooperacao = filtro.getTipooperacao().getCdtipooperacao();
		if(filtro.getTiporotapraca()!=null && filtro.getTiporotapraca().getCdtiporotapraca()!=null)
			cdtiporotapraca = filtro.getTiporotapraca().getCdtiporotapraca();
		
		return query()
			.where("vpedidovendareport.dtemissao >= ?",filtro.getDataEmissaoInicial())
			.where("vpedidovendareport.dtemissao <= ?",filtro.getDataEmissaoFinal())
			.where("vpedidovendareport.cddeposito = ?",cddeposito)
			.where("vpedidovendareport.cdtipooperacao = ?",cdtipooperacao)
			.where("vpedidovendareport.cdcarregamentostatus = ?", cdcarregamentostatus)
			.where("vpedidovendareport.dtprevisaoentrega >= ?",filtro.getDataPrevisaoInicial())
			.where("vpedidovendareport.dtprevisaoentrega <= ?",filtro.getDataPrevisaoFinal())
			.where("vpedidovendareport.cdtiporotapraca = ?",cdtiporotapraca)
			.list();
	}
	
}
