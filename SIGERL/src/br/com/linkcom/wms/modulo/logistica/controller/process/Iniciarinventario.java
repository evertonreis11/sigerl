package br.com.linkcom.wms.modulo.logistica.controller.process;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.wms.modulo.logistica.controller.process.filtro.IniciarinventarioFiltro;

@Controller(path="/logistica/process/Iniciarinventario", authorizationModule=ProcessAuthorizationModule.class)
public class Iniciarinventario extends MultiActionController {
	@DefaultAction
	public ModelAndView index(WebRequestContext request, IniciarinventarioFiltro filtro) {
		return null;
	}
}