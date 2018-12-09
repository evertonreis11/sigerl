package br.com.linkcom.wms.modulo.logistica.controller.process;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.vo.MovimentacaoAberta;
import br.com.linkcom.wms.geral.service.OrdemservicoService;

@Controller(path = "/logistica/process/VisualizarOSAberta")
public class VisualizarOSAberta extends MultiActionController {

	OrdemservicoService ordemservicoService;
	
	public void setOrdemservicoService(OrdemservicoService ordemservicoService) {
		this.ordemservicoService = ordemservicoService;
	}
	
	@DefaultAction
	public ModelAndView index(WebRequestContext request) {
	
		Produto produto = new Produto(Integer.valueOf(request.getParameter("cdproduto")));
		Endereco endereco = new Endereco(Integer.valueOf(request.getParameter("cdendereco")));
		boolean exibirEntradas = Boolean.valueOf(request.getParameter("entradas"));
		
		if (exibirEntradas)
			request.setAttribute("tipo", "Entradas");
		else
			request.setAttribute("tipo", "Saídas");
		
		List<MovimentacaoAberta> movimentacoesAbertas = ordemservicoService.findMovimentacoesAbertas(produto, endereco, exibirEntradas);
		
		return new ModelAndView("direct:process/visualizarOSAberta", "movimentacoesAbertas", movimentacoesAbertas);	
	}
	
}
