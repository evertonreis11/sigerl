package br.com.ricardoeletro.sigerl.expedicao.report;

import br.com.linkcom.neo.authorization.report.ReportAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.wms.geral.service.ExpedicaoRetiraLojaService;
import br.com.linkcom.wms.util.WmsReport;
import br.com.ricardoeletro.sigerl.expedicao.report.filtro.TermoEntregaFiltro;

@Controller(path = "/expedicao/report/termoentrega", authorizationModule = ReportAuthorizationModule.class)
public class TermoEntregaReport extends WmsReport<TermoEntregaFiltro> {
	
	private ExpedicaoRetiraLojaService expedicaoRetiraLojaService;
	
	public void setExpedicaoRetiraLojaService(ExpedicaoRetiraLojaService expedicaoRetiraLojaService) {
		this.expedicaoRetiraLojaService = expedicaoRetiraLojaService;
	}
	
	@Override
	public IReport createReportWms(WebRequestContext request, TermoEntregaFiltro filtro) throws Exception {
		return expedicaoRetiraLojaService.criarRelatorioTermoEntrega(filtro.getChaveNotaFiscal(), filtro.getImpressaoFinalizarExpedicao());
	}

	@Override
	public String getTitulo() {
		return "Termo de Entrega";
	}

}
