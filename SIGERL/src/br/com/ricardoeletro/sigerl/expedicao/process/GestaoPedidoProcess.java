package br.com.ricardoeletro.sigerl.expedicao.process;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.view.ajax.JsonModelAndView;
import br.com.linkcom.wms.geral.bean.PedidoPontoControle;
import br.com.linkcom.wms.geral.bean.vo.GestaoPedidoVO;
import br.com.linkcom.wms.geral.service.DepositoService;
import br.com.linkcom.wms.geral.service.NotafiscalsaidaService;
import br.com.linkcom.wms.geral.service.PedidoPontoControleService;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.ricardoeletro.sigerl.expedicao.process.filtro.GestaoPedidoFiltro;

@Controller(path="/expedicao/process/gestaopedido",authorizationModule=ProcessAuthorizationModule.class)
public class GestaoPedidoProcess extends MultiActionController{
	
	private DepositoService depositoService;
	private NotafiscalsaidaService notaFiscalSaidaService;
	private PedidoPontoControleService pedidoPontoControleService;
	
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
	}
	
	public void setNotaFiscalSaidaService(NotafiscalsaidaService notaFiscalSaidaService) {
		this.notaFiscalSaidaService = notaFiscalSaidaService;
	}
	
	public void setPedidoPontoControleService(PedidoPontoControleService pedidoPontoControleService) {
		this.pedidoPontoControleService = pedidoPontoControleService;
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
	
	public ModelAndView getInfoPedido(WebRequestContext request, GestaoPedidoFiltro filtro){
		List<PedidoPontoControle> registros = new ArrayList<PedidoPontoControle>();
		
		if (StringUtils.isNotBlank(filtro.getNumeroPedido())){
			registros = pedidoPontoControleService.recuperaPontoControlePedido(NumberUtils.toLong(filtro.getNumeroPedido(), NumberUtils.LONG_ZERO));
		}
		
		return new JsonModelAndView().addObject("listaLog", registros);
		
	}
}
