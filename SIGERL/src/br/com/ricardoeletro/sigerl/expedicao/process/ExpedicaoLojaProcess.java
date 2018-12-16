package br.com.ricardoeletro.sigerl.expedicao.process;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLojaProduto;
import br.com.linkcom.wms.geral.service.ExpedicaoRetiraLojaProdutoService;
import br.com.linkcom.wms.geral.service.ExpedicaoRetiraLojaService;
import br.com.ricardoeletro.sigerl.constantes.AppConstantes;
import br.com.ricardoeletro.sigerl.expedicao.process.filtro.ExpedicaoLojaFiltro;
import br.com.ricardoeletro.sigerl.geral.process.GenericProcess;

@Controller(path="/expedicao/process/expedicaoloja",authorizationModule=ProcessAuthorizationModule.class)
public class ExpedicaoLojaProcess extends GenericProcess<ExpedicaoLojaFiltro> {
	
	private ExpedicaoRetiraLojaService expedicaoRetiraLojaService;
	private ExpedicaoRetiraLojaProdutoService expedicaoRetiraLojaProdutoService;
	
	public void setExpedicaoRetiraLojaService(ExpedicaoRetiraLojaService expedicaoRetiraLojaService) {
		this.expedicaoRetiraLojaService = expedicaoRetiraLojaService;
	}
	
	public void setExpedicaoRetiraLojaProdutoService(ExpedicaoRetiraLojaProdutoService expedicaoRetiraLojaProdutoService) {
		this.expedicaoRetiraLojaProdutoService = expedicaoRetiraLojaProdutoService;
	}

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
		filtro.setExpedicaoRetiraLoja(expedicaoRetiraLojaService.findExpedicaoLoja(filtro.getValorInicial()));
		
		if (filtro.getExpedicaoRetiraLoja() == null)
			request.addError("A Nota informada não está disponível na filial para realizar entrega.");
	}
	
	@Action("limpar")
	public ModelAndView limparFiltro(WebRequestContext request, ExpedicaoLojaFiltro filtro){
		filtro = new ExpedicaoLojaFiltro();		
		return index(request, filtro);
	}
	
	public ModelAndView conferirProduto(WebRequestContext request, ExpedicaoLojaFiltro filtro){
		String mensagem = null;
		
		if (StringUtils.isBlank(filtro.getCodigoBarras())){
			mensagem = "O preenchimento do codigo de barras é obrigatório para realizar a conferência!!!";
		}else{
			mensagem = expedicaoRetiraLojaProdutoService.conferirProduto(filtro.getCodigoBarras(), filtro.getExpedicaoRetiraLoja().getCdExpedicaoRetiraLoja(),
							filtro.getExpedicaoRetiraLoja().getNotaFiscalSaida().getCdnotafiscalsaida());
		}
		
		if (StringUtils.contains(mensagem, "sucesso"))
			request.addMessage(mensagem);
		else 
			request.addError(mensagem);
		
		return consultar(request, filtro);
	}
	
	@Action("finalizar")
	public ModelAndView finalizarExpedicao(WebRequestContext request,  ExpedicaoLojaFiltro filtro){
		List<ExpedicaoRetiraLojaProduto> produtos = expedicaoRetiraLojaProdutoService.recuperaProdutosSemConferenciaPorExpedicao(filtro.getExpedicaoRetiraLoja().getCdExpedicaoRetiraLoja());
		
		
		if (produtos != null && produtos.isEmpty()){
			request.addError("Não é permitido entrega parcial de produtos ao cliente. Gentileza realizar a conferência de todos os produtos");
			return consultar(request, filtro);
		}
		String chaveNotaImpressaoTermo = filtro.getExpedicaoRetiraLoja().getNotaFiscalSaida().getChavenfe();
			
		expedicaoRetiraLojaService.finalizarExpedicao(filtro.getExpedicaoRetiraLoja());
		filtro = new ExpedicaoLojaFiltro();
		
		filtro.setChaveNotaImpressaoTermo(chaveNotaImpressaoTermo);
		request.addMessage("A expedição foi finalizada com sucesso. O sistema gerou um termo para confirmação de entrega para assinatura do cliente.");

		return index(request, filtro);
	}
}
