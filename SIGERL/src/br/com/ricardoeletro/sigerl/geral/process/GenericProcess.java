package br.com.ricardoeletro.sigerl.geral.process;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.ricardoeletro.sigerl.geral.filtro.GenericFilter;

public abstract class GenericProcess<FILTRO extends GenericFilter> extends MultiActionController {
	
	@DefaultAction
	public ModelAndView index(WebRequestContext request, FILTRO filtro){
		filtro.setLabelDinamico(recuperaValorLabelDinamico());
		return new ModelAndView(pathJspController(),"filtro",filtro);
	}

	@Action("consultar")
	public ModelAndView consultar(WebRequestContext request, FILTRO filtro){
		if (StringUtils.isBlank(filtro.getValorInicial())){
			
			StringBuilder sb = new StringBuilder();
			sb.append("O preechimento do campo ")
				.append(recuperaValorLabelDinamico().replace(":", ""))
				.append(" é obrigatório!!!");
			
			request.addError(sb.toString());
			
		}else{
			consultarItem(request,filtro);
		}
		
		
		return index(request, filtro);
	}
	
	protected abstract String recuperaValorLabelDinamico();
	
	protected abstract String pathJspController();
	
	protected abstract void consultarItem(WebRequestContext request, FILTRO filtro);
	
}
