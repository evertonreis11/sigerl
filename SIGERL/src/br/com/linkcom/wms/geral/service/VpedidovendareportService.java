package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.controller.resource.Resource;
import br.com.linkcom.wms.geral.bean.view.Vpedidovendareport;
import br.com.linkcom.wms.geral.dao.VpedidovendareportDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.VpedidovendareportFiltro;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class VpedidovendareportService extends GenericService<Vpedidovendareport>{

	private VpedidovendareportDAO vpedidovendareportDAO;

	public void setVpedidovendareportDAO(VpedidovendareportDAO vpedidovendareportDAO) {
		this.vpedidovendareportDAO = vpedidovendareportDAO;
	}

	/**
	 * Método que referencia o DAO.
	 * 
	 * @param filtro
	 * @return List<Vpedidovendareport>
	 */
	public List<Vpedidovendareport> findForReport(VpedidovendareportFiltro filtro) {
		return vpedidovendareportDAO.findForReport(filtro);
	}
	
	/**
	 * 
	 * @param filtro
	 * @param fields
	 * @return
	 */
	public Resource generateCSV(List<Vpedidovendareport> lista){
		
		StringBuilder csv = new StringBuilder();
		
		csv.append("Depósito").append(";");						
		csv.append("Data da Venda").append(";");					 
		csv.append("Data de Chegada (WMS)").append(";");			 
		csv.append("Previsão de Entrega").append(";");		
		csv.append("Data de Faturamento").append(";");
		csv.append("Turno de Entrega").append(";");
		csv.append("Nº do Pedido").append(";");					
		csv.append("Código do Produto").append(";");				
		csv.append("Descricação do Produto").append(";");			
		csv.append("Quantidade").append(";");						
		csv.append("Cubagem").append(";");							
		csv.append("Tipo de Pedido").append(";");					 
		csv.append("Carga").append(";");			
		csv.append("Status da Carga").append(";");
		csv.append("Box").append(";");								
		csv.append("Status (Box)").append(";");
		csv.append("Cliente").append(";");
		csv.append("Rota").append(";");
		csv.append("Valor do Produto").append(";");
		
		csv.append("\n");
		
		if(lista!=null && !lista.isEmpty()){
			for (Vpedidovendareport vpvr : lista){
				
				csv.append(vpvr.getDeposito()!=null?vpvr.getDeposito():"").append(";");
				csv.append(vpvr.getDtemissao()!=null?WmsUtil.stringToDefaulDateFormat(vpvr.getDtemissao().toString(),"yyyy-MM-dd"):"").append(";");	
				csv.append(vpvr.getDtchegadaerp()!=null?WmsUtil.stringToDefaulDateFormat(vpvr.getDtchegadaerp().toString(), "yyyy-MM-dd"):"").append(";");	 
				csv.append(vpvr.getDtprevisaoentrega()!=null?WmsUtil.stringToDefaulDateFormat(vpvr.getDtprevisaoentrega().toString(), "yyyy-MM-dd"):"").append(";");	 
				if(vpvr.getCdcarregamentostatus()!=null && vpvr.getCdcarregamentostatus() == 6)
					csv.append(vpvr.getDtfaturamento()!=null?WmsUtil.stringToDefaulDateFormat(vpvr.getDtfaturamento().toString(), "yyyy-MM-dd"):"CORTADO").append(";");	
				else
					csv.append(vpvr.getDtfaturamento()!=null?WmsUtil.stringToDefaulDateFormat(vpvr.getDtfaturamento().toString(), "yyyy-MM-dd"):"").append(";");
				csv.append(vpvr.getTurnodeentrega()!=null?vpvr.getTurnodeentrega():"").append(";");
				csv.append(vpvr.getNumero()!=null?vpvr.getNumero().toString():"").append(";");	 
				csv.append(vpvr.getCodigoproduto()!=null?vpvr.getCodigoproduto():"").append(";");	 
				csv.append(vpvr.getDescricaoproduto()!=null?vpvr.getDescricaoproduto():"").append(";");	 
				csv.append(vpvr.getQtde()!=null?vpvr.getQtde().toString():"").append(";");	 
				try{csv.append(Double.parseDouble(vpvr.getCubagem()) * vpvr.getQtde()).append(";");}
				catch (Exception e) {csv.append("").append(";");}
				csv.append(vpvr.getTipooperacao()!=null?vpvr.getTipooperacao():"").append(";");	 
				csv.append(vpvr.getCdcarregamento()!=null?vpvr.getCdcarregamento().toString():"").append(";");	
				csv.append(vpvr.getCarregamentostatus()!=null?vpvr.getCarregamentostatus():"").append(";");	
				csv.append(vpvr.getBox()!=null?vpvr.getBox().toString():"").append(";");	
				csv.append(vpvr.getBoxstatus()!=null?vpvr.getBoxstatus():"").append(";");	
				csv.append(vpvr.getCliente()!=null?vpvr.getCliente():"").append(";");	
				csv.append(vpvr.getRota()!=null?vpvr.getRota():"").append(";");	
				csv.append(vpvr.getValorproduto()!=null?vpvr.getValorproduto().toString():"").append(";");
				
				csv.append("\n");
			}
		}
		
		Resource resource = new Resource();
		resource.setContentType("text/csv");
		resource.setFileName("WMS_Pedido_Venda_" + WmsUtil.getDeposito().getNome() + ".csv");
		resource.setContents(csv.toString().getBytes());
		return resource;
	}
	
}
