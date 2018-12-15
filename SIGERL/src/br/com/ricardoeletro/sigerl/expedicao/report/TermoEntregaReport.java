package br.com.ricardoeletro.sigerl.expedicao.report;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.report.ReportAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.resource.ResourceGenerationException;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.wms.util.WmsReport;
import br.com.ricardoeletro.sigerl.geral.filtro.GenericFilter;

@Controller(path = "/expedicao/report/termoentrega", authorizationModule = ReportAuthorizationModule.class)
public class TermoEntregaReport extends WmsReport<GenericFilter> {

	@Override
	public IReport createReportWms(WebRequestContext request, GenericFilter filtro) throws Exception {
		return null;
	}

	@Override
	public String getTitulo() {
		return "Termo de Entrega";
	}
	
	@Override
	public ModelAndView doFiltro(WebRequestContext request, GenericFilter filtro) throws ResourceGenerationException {
		filtro.setLabelDinamico("Informe a Expedição:");
		return super.doFiltro(request, filtro);
	}

}
