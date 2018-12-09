package br.com.ricardoeletro.coletor.geral.process;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.wms.util.WmsException;
import br.com.ricardoeletro.coletor.geral.as.ColetorAS;
import br.com.ricardoeletro.coletor.geral.filtro.ColetorFiltro;

public abstract class ColetorProcess<FILTRO extends ColetorFiltro, AS extends ColetorAS> extends MultiActionController {
	
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
			
			filtro.setResultado(null);
		}else{
			filtro.setResultado(consultarItem(request,filtro));
		}
		
		
		return index(request, filtro);
	}
	
	protected abstract String recuperaValorLabelDinamico();
	
	protected abstract String pathJspController();
	
	protected abstract String consultarItem(WebRequestContext request, FILTRO filtro);
	
	
	@SuppressWarnings("unchecked")
	public AS getAS(String nomeClasse) throws WmsException {

		Object objRetorno = null;

		Class<AS> as = null;
		
		try {
			as = (Class<AS>) Class.forName(nomeClasse);
			objRetorno = as.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (AS) objRetorno;

	}
	
}
