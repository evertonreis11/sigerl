package br.com.ricardoeletro.sigerl.recebimento.process;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLoja;
import br.com.linkcom.wms.geral.bean.TipoEstoque;
import br.com.linkcom.wms.geral.service.RecebimentoRetiraLojaProdutoService;
import br.com.linkcom.wms.geral.service.RecebimentoRetiraLojaService;
import br.com.ricardoeletro.sigerl.constantes.AppConstantes;
import br.com.ricardoeletro.sigerl.geral.process.GenericProcess;
import br.com.ricardoeletro.sigerl.recebimento.process.filtro.RecebimentoLojaFiltro;

@Controller(path="/recebimento/process/recebimentoloja",authorizationModule=ProcessAuthorizationModule.class)
public class RecebimentoLojaProcess extends GenericProcess<RecebimentoLojaFiltro> {
	
	private RecebimentoRetiraLojaService recebimentoRetiraLojaService;
	private RecebimentoRetiraLojaProdutoService recebimentoRetiraLojaProdutoService;
	
	public void setRecebimentoRetiraLojaService(RecebimentoRetiraLojaService recebimentoRetiraLojaService) {
		this.recebimentoRetiraLojaService = recebimentoRetiraLojaService;
	}
	
	public void setRecebimentoRetiraLojaProdutoService(
			RecebimentoRetiraLojaProdutoService recebimentoRetiraLojaProdutoService) {
		this.recebimentoRetiraLojaProdutoService = recebimentoRetiraLojaProdutoService;
	}
	
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
		RecebimentoRetiraLoja recebimentoRetiraLoja = null;
		
		Integer cdRecebimentoRetiraLoja = null;
		
		if (filtro.getRecebimentoRetiraLoja() != null)
			cdRecebimentoRetiraLoja = filtro.getRecebimentoRetiraLoja().getCdRecebimentoRetiraLoja();
		
		recebimentoRetiraLoja = recebimentoRetiraLojaService.findRecebimentoLoja(filtro.getValorInicial(), cdRecebimentoRetiraLoja);
		
		if (recebimentoRetiraLoja == null){
			request.addError("O produto informado não pertence ao recebimento destinado a esta filial.");
		}else{
			TipoEstoque tipoEstoque = null;
			
			if (filtro.getAvaria())
				tipoEstoque = TipoEstoque.AVARIADO;
			else
				tipoEstoque = TipoEstoque.PERFEITO;
			
			recebimentoRetiraLojaProdutoService.confirmarProdutoRecebido(filtro.getValorInicial(), 
					recebimentoRetiraLoja.getListaRecebimentoRetiraLojaProduto(), tipoEstoque);
			
			filtro.setRecebimentoRetiraLoja(recebimentoRetiraLoja);
			
			request.addMessage("Produto conferido com sucesso!!!");
		}
	}
	
	@Action("finalizar")
	public ModelAndView finalizarRecebimento(WebRequestContext request,  RecebimentoLojaFiltro filtro){
		if (filtro.getRecebimentoRetiraLoja() == null ||
				filtro.getRecebimentoRetiraLoja().getCdRecebimentoRetiraLoja() == null){
			request.addError("Para finalizar um recebimento é necessário selecionar um lote de produtos(recebimento). ");
			
		}else{
			recebimentoRetiraLojaService.finalizarRecebimento(filtro.getRecebimentoRetiraLoja());
			request.addMessage("O recebimento foi finalizado com sucesso.");
			return limparFiltro(request, filtro);
		}
		
		return index(request, filtro);
	}
	
	@Action("limpar")
	public ModelAndView limparFiltro(WebRequestContext request,  RecebimentoLojaFiltro filtro){
		filtro = new RecebimentoLojaFiltro();		
		return index(request, filtro);
	}

}
