package br.com.ricardoeletro.sigerl.expedicao.process;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.ricardoeletro.sigerl.constantes.AppConstantes;
import br.com.ricardoeletro.sigerl.expedicao.process.filtro.ExpedicaoLojaFiltro;
import br.com.ricardoeletro.sigerl.geral.process.GenericProcess;

@Controller(path="/expedicao/process/expedicaoloja",authorizationModule=ProcessAuthorizationModule.class)
public class ExpedicaoLojaProcess extends GenericProcess<ExpedicaoLojaFiltro> {

	@Override
	protected String recuperaValorLabelDinamico() {
		return AppConstantes.Expedicao.LABEL;
	}

	@Override
	protected String pathJspController() {
		return AppConstantes.Expedicao.PATH;
	}

	@Override
	protected void consultarItem(WebRequestContext request, ExpedicaoLojaFiltro filtro) {
		//TODO IMPLEMENTAR
	}
	
	@Action("limpar")
	public ModelAndView limparFiltro(WebRequestContext request,  ExpedicaoLojaFiltro filtro){
		filtro = new ExpedicaoLojaFiltro();		
		return index(request, filtro);
	}
	
	public ModelAndView conferirProduto(WebRequestContext request,  ExpedicaoLojaFiltro filtro){
		//TODO IMPLEMENTAR
		return consultar(request, filtro);
	}
	
	@Action("finalizar")
	public ModelAndView finalizarExpedicao(WebRequestContext request,  ExpedicaoLojaFiltro filtro){
		
		return index(request, filtro);
	}

}
