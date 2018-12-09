package br.com.ricardoeletro.coletor.modulo.produto.process;

import java.util.Map;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.ricardoeletro.coletor.constantes.ColetorConstantes;
import br.com.ricardoeletro.coletor.geral.as.ConsultarProdutoAS;
import br.com.ricardoeletro.coletor.geral.filtro.ColetorFiltro;
import br.com.ricardoeletro.coletor.geral.process.ColetorProcess;
import br.com.ricardoeletro.coletor.modulo.produto.process.filtro.ConsultaProdutoFiltro;

@Controller(path="/produto/process/Consultarproduto",authorizationModule=ProcessAuthorizationModule.class)
public class ConsultarProdutoProcess extends ColetorProcess<ConsultaProdutoFiltro, ConsultarProdutoAS> {
	
	@Override
	protected String recuperaValorLabelDinamico() {
		return ColetorConstantes.Produto.ConsultaProduto.LABEL;
	}

	@Override
	protected String pathJspController() {
		return ColetorConstantes.Produto.ConsultaProduto.PATH;
	}

	@Override
	protected String consultarItem(WebRequestContext request, ConsultaProdutoFiltro filtro) {
		
		Map<String, String> retorno = getAS(ConsultarProdutoAS.class.getName()).consultaProduto(filtro);
		String produto = null;
		
		if (retorno.containsKey("mensagem")){
			request.addError(retorno.get("mensagem"));
		}else{
			produto = retorno.get("dados");
		}
		
		return produto;
	}
	
}
