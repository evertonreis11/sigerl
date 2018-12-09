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
import br.com.ricardoeletro.coletor.geral.as.ConferenciaRecebimentoAS;
import br.com.ricardoeletro.coletor.geral.process.ColetorProcess;
import br.com.ricardoeletro.coletor.modulo.recebimento.process.filtro.ConferenciaRecebimentoFiltro;
import br.com.ricardoeletro.coletor.modulo.recebimento.process.filtro.ConferenciaRecebimentoFiltro.TipoColeta;

@Controller(path="/recebimento/process/Conferenciarecebimento",authorizationModule=ProcessAuthorizationModule.class)
public class ConferenciarecebimentoProcess extends ColetorProcess<ConferenciaRecebimentoFiltro, ConferenciaRecebimentoAS> {

	@Override
	protected String recuperaValorLabelDinamico() {
		return ColetorConstantes.Recebimento.ConferenciaRecebimento.LABEL_INICIAL;
	}

	@Override
	protected String pathJspController() {
		return ColetorConstantes.Recebimento.ConferenciaRecebimento.PATH_INICIAL;
	}

	@Override
	protected String consultarItem(WebRequestContext request, ConferenciaRecebimentoFiltro filtro) {

		Map<String, String> retorno = getAS(ConferenciaRecebimentoAS.class.getName()).recuperaRecebimento(filtro);
		String produto = null;

		if (retorno.containsKey("mensagem")){
			request.addError(retorno.get("mensagem"));
		}else{
			produto = retorno.get("dados");
		}

		return produto;
	}

	@Action("iniciarConferencia")
	public ModelAndView iniciarConferencia(WebRequestContext request, ConferenciaRecebimentoFiltro filtro){
		filtro.setResultado(null);
		filtro.setValorInicial(null);

		filtro.setTipoColeta(TipoColeta.FRACIONADA); 

		filtro.setLabelDinamico(ColetorConstantes.Recebimento.ConferenciaRecebimento.LABEL_COLETA_PRODUTO);

		return new ModelAndView(ColetorConstantes.Recebimento.ConferenciaRecebimento.PATH_COLETA_PRODUTO,"filtro",filtro);
	}

	@Action("coletarProduto")
	public ModelAndView coletarProduto(WebRequestContext request, ConferenciaRecebimentoFiltro filtro){

		if (StringUtils.isBlank(filtro.getValorInicial())){

			StringBuilder sb = new StringBuilder();
			sb.append("O preechimento do campo ")
			.append(ColetorConstantes.Recebimento.ConferenciaRecebimento.LABEL_COLETA_PRODUTO.replace(":", ""))
			.append(" é obrigatório!!!");

			request.addError(sb.toString());

			filtro.setResultado(null);
		}else{
			String msg = getAS(ConferenciaRecebimentoAS.class.getName()).startCollectProduct(filtro);

			Boolean mesmaPagina = ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETA_QUANT_AUTO_RECEB, WmsUtil.getDeposito());

			if (StringUtils.isNotBlank(msg) && !"OK".equals(msg)){
				request.addError(msg);
				mesmaPagina = Boolean.TRUE;
			}	

			filtro.setValorInicial(null);

			if (!mesmaPagina){
				filtro.setLabelDinamico(ColetorConstantes.Recebimento.ConferenciaRecebimento.LABEL_COLETA_QTDE);

				return new ModelAndView(ColetorConstantes.Recebimento.ConferenciaRecebimento.PATH_COLETA_QTDE,"filtro",filtro);
			}

		}

		filtro.setLabelDinamico(ColetorConstantes.Recebimento.ConferenciaRecebimento.LABEL_COLETA_PRODUTO);

		return new ModelAndView(ColetorConstantes.Recebimento.ConferenciaRecebimento.PATH_COLETA_PRODUTO,"filtro",filtro);

	}

	@Action("coletarQuantidade")
	public ModelAndView coletarQuantidade(WebRequestContext request, ConferenciaRecebimentoFiltro filtro){
		if (StringUtils.isBlank(filtro.getValorInicial())){

			StringBuilder sb = new StringBuilder();
			sb.append("O preechimento do campo ")
			.append(ColetorConstantes.Recebimento.ConferenciaRecebimento.LABEL_COLETA_QTDE.replace(":", ""))
			.append(" é obrigatório!!!");

			request.addError(sb.toString());

			filtro.setResultado(null);
			
			filtro.setLabelDinamico(ColetorConstantes.Recebimento.ConferenciaRecebimento.LABEL_COLETA_QTDE);
			
			return new ModelAndView(ColetorConstantes.Recebimento.ConferenciaRecebimento.PATH_COLETA_QTDE,"filtro",filtro);
			
		}else{

			String msg = getAS(ConferenciaRecebimentoAS.class.getName()).collectQte(filtro);

			if (StringUtils.isNotBlank(msg) && !"OK".equals(msg)){
				request.addError(msg);

				filtro.setLabelDinamico(ColetorConstantes.Recebimento.ConferenciaRecebimento.LABEL_COLETA_QTDE);

				return new ModelAndView(ColetorConstantes.Recebimento.ConferenciaRecebimento.PATH_COLETA_QTDE,"filtro",filtro);
			}

			filtro.setValorInicial(null);
			filtro.setResultado(null);
		}

		filtro.setLabelDinamico(ColetorConstantes.Recebimento.ConferenciaRecebimento.LABEL_COLETA_PRODUTO);

		return new ModelAndView(ColetorConstantes.Recebimento.ConferenciaRecebimento.PATH_COLETA_PRODUTO,"filtro",filtro);

	}

	@Action("trocarCarregamento")
	public ModelAndView trocarCarregamento(WebRequestContext request, ConferenciaRecebimentoFiltro filtro){
		getAS(ConferenciaRecebimentoAS.class.getName()).configurarOSUDataFim(filtro.getOrdemservicoUsuario());
		return index(request, new ConferenciaRecebimentoFiltro());
	}

	@Action("cancelarCarregamento")
	public ModelAndView cancelarCarregamento(WebRequestContext request, ConferenciaRecebimentoFiltro filtro){

		String msg = getAS(ConferenciaRecebimentoAS.class.getName()).desassociarOSU(filtro);

		if (StringUtils.isNotBlank(msg)){
			request.addError(msg);

			filtro.setLabelDinamico(ColetorConstantes.Recebimento.ConferenciaRecebimento.LABEL_COLETA_PRODUTO);

			return new ModelAndView(ColetorConstantes.Recebimento.ConferenciaRecebimento.PATH_COLETA_PRODUTO,"filtro",filtro);
		}

		return index(request, new ConferenciaRecebimentoFiltro());
	}

	@Action("finalizarCarregamento")
	public ModelAndView finalizarCarregamento(WebRequestContext request, ConferenciaRecebimentoFiltro filtro){

		String msg = getAS(ConferenciaRecebimentoAS.class.getName()).processarOSP(filtro);

		if (StringUtils.isNotBlank(msg)){
			if (msg.contains("OK")){
				request.addMessage(msg);
			}else{
				request.addError(msg);
				if (!msg.contains("Recebimento")){
					filtro.setLabelDinamico(ColetorConstantes.Recebimento.ConferenciaRecebimento.LABEL_COLETA_PRODUTO);
					
					return new ModelAndView(ColetorConstantes.Recebimento.ConferenciaRecebimento.PATH_COLETA_PRODUTO,"filtro",filtro);
				}
			}
		}

		return index(request, new ConferenciaRecebimentoFiltro());
	}

}
