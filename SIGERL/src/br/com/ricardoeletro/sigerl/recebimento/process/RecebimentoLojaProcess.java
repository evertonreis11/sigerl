package br.com.ricardoeletro.sigerl.recebimento.process;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.ricardoeletro.sigerl.constantes.AppConstantes;
import br.com.ricardoeletro.sigerl.geral.process.GenericProcess;
import br.com.ricardoeletro.sigerl.recebimento.process.filtro.RecebimentoLojaFiltro;

@Controller(path="/recebimento/process/recebimentoloja",authorizationModule=ProcessAuthorizationModule.class)
public class RecebimentoLojaProcess extends GenericProcess<RecebimentoLojaFiltro> {

	@Override
	protected String recuperaValorLabelDinamico() {
		return AppConstantes.Recebimento.LABEL;
	}

	@Override
	protected String pathJspController() {
		return AppConstantes.Recebimento.PATH;
	}

	@Override
	protected void consultarItem(WebRequestContext request, RecebimentoLojaFiltro filtro) {
	} 
	
}
