package br.com.ricardoeletro.coletor.modulo.recebimento.process;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.ricardoeletro.coletor.constantes.ColetorConstantes;
import br.com.ricardoeletro.coletor.geral.as.ReconferenciaRecebimentoAS;
import br.com.ricardoeletro.coletor.geral.process.ColetorProcess;
import br.com.ricardoeletro.coletor.modulo.recebimento.process.filtro.ConferenciaRecebimentoFiltro.TipoColeta;
import br.com.ricardoeletro.coletor.modulo.recebimento.process.filtro.ReconferenciaRecebimentoFiltro;

@Controller(path="/recebimento/process/Reconferenciarecebimento",authorizationModule=ProcessAuthorizationModule.class)
public class ReconferenciarecebimentoProcess extends ColetorProcess<ReconferenciaRecebimentoFiltro, ReconferenciaRecebimentoAS> {

	@Override
	protected String recuperaValorLabelDinamico() {
		return ColetorConstantes.Recebimento.ReconferenciaRecebimento.LABEL_INICIAL;
	}

	@Override
	protected String pathJspController() {
		return ColetorConstantes.Recebimento.ReconferenciaRecebimento.PATH_INICIAL;
	}

	@Override
	protected String consultarItem(WebRequestContext request, ReconferenciaRecebimentoFiltro filtro) {

		Map<String, String> retorno = getAS(ReconferenciaRecebimentoAS.class.getName()).recuperaRecebimento(filtro);
		String produto = null;

		if (retorno.containsKey("mensagem")){
			request.addError(retorno.get("mensagem"));
		}else{
			produto = retorno.get("dados");
		}

		return produto;
	}
	
	@Action("iniciarConferencia")
	public ModelAndView iniciarConferencia(WebRequestContext request, ReconferenciaRecebimentoFiltro filtro){
		filtro.setResultado(null);
		filtro.setValorInicial(null);

		filtro.setTipoColeta(TipoColeta.PADRAO); 
		
		getAS(ReconferenciaRecebimentoAS.class.getName()).startCollectProduct(filtro, Boolean.FALSE);

		filtro.setLabelDinamico(ColetorConstantes.Recebimento.ReconferenciaRecebimento.LABEL_COLETA_PRODUTO);

		return new ModelAndView(ColetorConstantes.Recebimento.ReconferenciaRecebimento.PATH_COLETA_PRODUTO,"filtro",filtro);
	}
	
	@Action("coletarProduto")
	public ModelAndView coletarProduto(WebRequestContext request, ReconferenciaRecebimentoFiltro filtro){
		
		if (StringUtils.isBlank(filtro.getValorInicial())){

			StringBuilder sb = new StringBuilder();
			sb.append("O preechimento do campo ")
			.append(ColetorConstantes.Recebimento.ReconferenciaRecebimento.LABEL_COLETA_PRODUTO.replace(":", ""))
			.append(" é obrigatório!!!");

			request.addError(sb.toString());

			
		}else{
			String msg = getAS(ReconferenciaRecebimentoAS.class.getName()).validarProduto(filtro);

			Boolean mesmaPagina = ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETA_QUANT_AUTO_RECEB, WmsUtil.getDeposito());

			if (StringUtils.isNotBlank(msg) && !"OK".equals(msg)){
				request.addError(msg);
				mesmaPagina = Boolean.TRUE;
			}	

			filtro.setValorInicial(null);
			
			if (!mesmaPagina){
				
				filtro.setLabelDinamico(ColetorConstantes.Recebimento.ReconferenciaRecebimento.LABEL_COLETA_QTDE);

				return new ModelAndView(ColetorConstantes.Recebimento.ReconferenciaRecebimento.PATH_COLETA_QTDE,"filtro",filtro);
			}

		}

		filtro.setLabelDinamico(ColetorConstantes.Recebimento.ReconferenciaRecebimento.LABEL_COLETA_PRODUTO);

		return new ModelAndView(ColetorConstantes.Recebimento.ReconferenciaRecebimento.PATH_COLETA_PRODUTO,"filtro",filtro);
	}
	
	@Action("coletarQuantidade")
	public ModelAndView coletarQuantidade(WebRequestContext request, ReconferenciaRecebimentoFiltro filtro){
		if (StringUtils.isBlank(filtro.getValorInicial())){

			StringBuilder sb = new StringBuilder();
			sb.append("O preechimento do campo ")
			.append(ColetorConstantes.Recebimento.ReconferenciaRecebimento.LABEL_COLETA_QTDE.replace(":", ""))
			.append(" é obrigatório!!!");

			request.addError(sb.toString());

			filtro.setResultado(null);
			
			filtro.setLabelDinamico(ColetorConstantes.Recebimento.ReconferenciaRecebimento.LABEL_COLETA_QTDE);
			
			return new ModelAndView(ColetorConstantes.Recebimento.ReconferenciaRecebimento.PATH_COLETA_QTDE,"filtro",filtro);
			
		}else{

			String msg = getAS(ReconferenciaRecebimentoAS.class.getName()).collectQte(filtro);

			if (StringUtils.isNotBlank(msg) && !"OK".equals(msg)){
				request.addError(msg);

				filtro.setLabelDinamico(ColetorConstantes.Recebimento.ReconferenciaRecebimento.LABEL_COLETA_QTDE);

				return new ModelAndView(ColetorConstantes.Recebimento.ReconferenciaRecebimento.PATH_COLETA_QTDE,"filtro",filtro);
			}

			filtro.setValorInicial(null);
			filtro.setResultado(null);
		}


		return iniciarConferencia(request, filtro);

	}
	
	@Action("trocarCarregamento")
	public ModelAndView trocarCarregamento(WebRequestContext request, ReconferenciaRecebimentoFiltro filtro){
		getAS(ReconferenciaRecebimentoAS.class.getName()).configurarOSUDataFim(filtro.getOrdemservicoUsuario());
		return index(request, new ReconferenciaRecebimentoFiltro());
	}

	@Action("cancelarCarregamento")
	public ModelAndView cancelarCarregamento(WebRequestContext request, ReconferenciaRecebimentoFiltro filtro){

		String msg = getAS(ReconferenciaRecebimentoAS.class.getName()).desassociarOSU(filtro);

		if (StringUtils.isNotBlank(msg)){
			request.addError(msg);

			filtro.setLabelDinamico(ColetorConstantes.Recebimento.ReconferenciaRecebimento.LABEL_COLETA_PRODUTO);

			return new ModelAndView(ColetorConstantes.Recebimento.ReconferenciaRecebimento.PATH_COLETA_PRODUTO,"filtro",filtro);
		}

		return index(request, new ReconferenciaRecebimentoFiltro());
	}

	@Action("finalizarCarregamento")
	public ModelAndView finalizarCarregamento(WebRequestContext request, ReconferenciaRecebimentoFiltro filtro){

		String msg = getAS(ReconferenciaRecebimentoAS.class.getName()).processarOSP(filtro);

		if (StringUtils.isNotBlank(msg)){
			if (msg.contains("Recebimento")){
				request.addMessage("Reconferência finalizada.");
			}else{
				request.addError(msg);

				filtro.setLabelDinamico(ColetorConstantes.Recebimento.ReconferenciaRecebimento.LABEL_COLETA_PRODUTO);

				return new ModelAndView(ColetorConstantes.Recebimento.ReconferenciaRecebimento.PATH_COLETA_PRODUTO,"filtro",filtro);
			}
		}

		return index(request, new ReconferenciaRecebimentoFiltro());
	}
	
	@Action("trocarProduto")
	public ModelAndView trocarProduto(WebRequestContext request, ReconferenciaRecebimentoFiltro filtro){

		String msg = getAS(ReconferenciaRecebimentoAS.class.getName()).startCollectProduct(filtro, Boolean.TRUE);
		
		if (StringUtils.isNotBlank(msg)){
			if (msg.contains("Recebimento")){
				filtro.setResultado(null);
				filtro.setValorInicial(null);
				
				request.addMessage("Reconferência finalizada.");
				
				return index(request, new ReconferenciaRecebimentoFiltro());
				
			}else{
				request.addError(msg);
			}
		}

		filtro.setLabelDinamico(ColetorConstantes.Recebimento.ReconferenciaRecebimento.LABEL_COLETA_PRODUTO);

		return new ModelAndView(ColetorConstantes.Recebimento.ReconferenciaRecebimento.PATH_COLETA_PRODUTO,"filtro",filtro);
	}

}
