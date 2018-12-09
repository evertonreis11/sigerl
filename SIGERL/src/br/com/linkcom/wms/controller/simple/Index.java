package br.com.linkcom.wms.controller.simple;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;

@Bean
@Controller(path = {"/adm/index","/sistema/index","/expedicao/index","/logistica/index","/recebimento/index"})
public class Index extends MultiActionController {
	
	@DefaultAction
	public ModelAndView index(WebRequestContext request){
		return new ModelAndView("index"); 
	}
}
