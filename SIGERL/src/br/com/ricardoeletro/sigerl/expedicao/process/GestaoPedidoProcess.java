package br.com.ricardoeletro.sigerl.expedicao.process;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.wms.geral.bean.vo.GestaoPedidoVO;
import br.com.linkcom.wms.geral.service.DepositoService;
import br.com.linkcom.wms.geral.service.NotafiscalsaidaService;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.ricardoeletro.sigerl.expedicao.process.filtro.GestaoPedidoFiltro;

@Controller(path="/expedicao/process/gestaopedido",authorizationModule=ProcessAuthorizationModule.class)
public class GestaoPedidoProcess extends MultiActionController{
	
	private DepositoService depositoService;
	private NotafiscalsaidaService notaFiscalSaidaService;
	
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
	}
	
	public void setNotaFiscalSaidaService(NotafiscalsaidaService notaFiscalSaidaService) {
		this.notaFiscalSaidaService = notaFiscalSaidaService;
	}
	
	
	@DefaultAction
	public ModelAndView index(WebRequestContext request, GestaoPedidoFiltro filtro){
		
		request.setAttribute("lista_deposito", depositoService.findAtivos());
		
		filtro.setLoja(WmsUtil.getDeposito());
		
		return new ModelAndView("/process/gestaopedido","filtro",filtro);
		
	}
	
	@Action("limpar")
	public ModelAndView limparFiltro(WebRequestContext request, GestaoPedidoFiltro filtro){
		filtro = new GestaoPedidoFiltro();		
		return index(request, filtro);
	}
	
	/**
	 * 
	 * @param request
	 * @param filtro
	 * @return
	 */
	@Action("filtrar")
	public ModelAndView getListagem(WebRequestContext request, GestaoPedidoFiltro filtro){
		
		List<GestaoPedidoVO> lista = notaFiscalSaidaService.findForGestaoPedido(filtro);
		
		request.setAttribute("lista",lista);
		
		return new ModelAndView("/process/gestaopedido","filtro",filtro);
		
	}
	
}
