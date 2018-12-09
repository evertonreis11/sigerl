package br.com.linkcom.wms.util;

import java.awt.Image;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import br.com.linkcom.neo.controller.resource.ReportController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.util.NeoFormater;
import br.com.linkcom.neo.util.NeoImageResolver;
import br.com.linkcom.neo.util.Util;

/**
 * Cria um relatório padrão na aplicação.
 * 
 * @author Pedro Gonçalves
 *
 * @param <FILTRO>
 */
public abstract class WmsReport<FILTRO> extends ReportController<FILTRO> {

	private NeoImageResolver neoImageResolver;

	public void setNeoImageResolver(NeoImageResolver neoImageResolver) {
		this.neoImageResolver = neoImageResolver;
	}
	
	/**
	 * Gera um novo relatório, e seta os parâmetros default por requisição.
	 * @see #configurarParametrosWms(WebRequestContext, Report)
	 */
	@Override
	public IReport createReport(WebRequestContext request, FILTRO command)
			throws Exception {

		Report report = (Report) createReportWms(request, command);
		
		configurarParametrosWms(request, report);
		return report;
	}
	
	/**
	 * Seta os parâmetros padrões que serão enviados ao relatório, como A logo, o neo formater entre outros.
	 * 
	 * @param request
	 * @param report
	 */
	protected void configurarParametrosWms(WebRequestContext request, Report report) {
		String nome = WmsUtil.getUsuarioLogado().getNome();
		
		Image image = null;
		try {
			image = neoImageResolver.getImage(WmsUtil.getLogoForReport());
		} catch (IOException e) {
			e.printStackTrace();
		}
		report.addParameter("LOGO", image);
		report.addParameter("NEOFORMATER", NeoFormater.getInstance());
		
		if (getTitulo() != null)
			report.addParameter("TITULO", getTitulo());
		
		report.addParameter("DATA",new Date(System.currentTimeMillis()));
		report.addParameter("HORA", System.currentTimeMillis());
		report.addParameter("USUARIO", nome);
		report.addParameter("EMPRESA_WMS", Util.locale.getBundleKey("aplicacao.titulo"));
		report.addParameter("RODAPE", "Impresso em " + NeoFormater.getInstance().format(new Timestamp(System.currentTimeMillis())) + " por " + WmsUtil.getUsuarioLogado().getNome());
	}
	
	@Override
	protected String getReportName(IReport report) {
		return "WMS_" + super.getReportName(report);
	}

	public abstract IReport createReportWms(WebRequestContext request, FILTRO filtro) throws Exception;

	public abstract String getTitulo();

}
