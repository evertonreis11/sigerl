package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.view.Vitempedido;
import br.com.linkcom.wms.geral.dao.VitempedidoDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.VitempedidoFiltro;

public class VitempedidoService extends GenericService<Vitempedido> {

	private VitempedidoDAO vitempedidoDAO;
	
	public void setVitempedidoDAO(VitempedidoDAO vitempedidoDAO) {
		this.vitempedidoDAO = vitempedidoDAO;
	}

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public IReport findByRelatorioConsultarPedido(VitempedidoFiltro filtro){
		
		Report report = new Report("RelatorioConsultarPedido");
		List<Vitempedido> listaVitempedido = vitempedidoDAO.findByRelatorioConsultarPedido(filtro);
		
		for (Vitempedido vitempedido : listaVitempedido) {
			if(vitempedido.getNumerosegundanota()==null){
				vitempedido.setNumerosegundanotaReport("N/A");
			}else{
				vitempedido.setNumerosegundanotaReport(vitempedido.getNumerosegundanota().toString());
			}				
		}
		
		String situacaoItem = "";
		
		if(filtro.getSituacaoItem()==null){
			situacaoItem = "Todos";
		}else if(filtro.getSituacaoItem()){
			situacaoItem = "Faturado";
		}else if(!filtro.getSituacaoItem()){
			situacaoItem = "Pendente de Faturamento";
		}
		
		report.addParameter("carregamento",filtro.getCdCarregamento());
		report.addParameter("numeropedido",filtro.getNumeroPedido());
		report.addParameter("situacaoitem", situacaoItem);
		report.addParameter("expedicao", filtro.getCdExpedicao());
		
		report.setDataSource(listaVitempedido);
		
		return report;
	}
	
}
