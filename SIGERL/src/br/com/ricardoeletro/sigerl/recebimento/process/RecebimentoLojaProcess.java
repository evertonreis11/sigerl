package br.com.ricardoeletro.sigerl.recebimento.process;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
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
		
		recebimentoRetiraLoja = recebimentoRetiraLojaService.findRecebimentoLojaWithCodigoEan(filtro.getValorInicial());
		
		if (recebimentoRetiraLoja == null){
			request.addError("Produto Inv�lido");
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

}
