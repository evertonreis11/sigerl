package br.com.ricardoeletro.sigerl.expedicao.crud;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.Message;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.neo.view.ajax.JsonModelAndView;
import br.com.linkcom.wms.geral.bean.Acao;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Depositofilial;
import br.com.linkcom.wms.geral.bean.Logintegracaoae;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestohistorico;
import br.com.linkcom.wms.geral.bean.Manifestonotafiscal;
import br.com.linkcom.wms.geral.bean.Manifestostatus;
import br.com.linkcom.wms.geral.bean.Motorista;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.Notafiscaltipo;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotagerenciadora;
import br.com.linkcom.wms.geral.bean.Statusconfirmacaoentrega;
import br.com.linkcom.wms.geral.bean.Tipoentrega;
import br.com.linkcom.wms.geral.bean.Tipomanifestohistorico;
import br.com.linkcom.wms.geral.bean.Tipovenda;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.geral.bean.Veiculo;
import br.com.linkcom.wms.geral.bean.vo.Auditoria;
import br.com.linkcom.wms.geral.service.AuditoriaService;
import br.com.linkcom.wms.geral.service.ClienteService;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.DepositoService;
import br.com.linkcom.wms.geral.service.DepositofilialService;
import br.com.linkcom.wms.geral.service.LogintegracaoaeService;
import br.com.linkcom.wms.geral.service.ManifestoService;
import br.com.linkcom.wms.geral.service.ManifestocodigobarrasService;
import br.com.linkcom.wms.geral.service.ManifestofinanceiroService;
import br.com.linkcom.wms.geral.service.ManifestohistoricoService;
import br.com.linkcom.wms.geral.service.ManifestonotafiscalService;
import br.com.linkcom.wms.geral.service.MotoristaService;
import br.com.linkcom.wms.geral.service.NotafiscalsaidaService;
import br.com.linkcom.wms.geral.service.RotaService;
import br.com.linkcom.wms.geral.service.RotagerenciadoraService;
import br.com.linkcom.wms.geral.service.TiponotafiscalService;
import br.com.linkcom.wms.geral.service.TransportadorService;
import br.com.linkcom.wms.geral.service.UsuarioService;
import br.com.linkcom.wms.geral.service.VeiculoService;
import br.com.linkcom.wms.util.CrudController;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.ricardoeletro.sigerl.expedicao.crud.filtro.ManifestoFiltro;

@Controller(path="/expedicao/crud/Manifesto", authorizationModule=CrudAuthorizationModule.class)
public class ManifestoCrud extends CrudController<ManifestoFiltro, Manifesto, Manifesto>{
	private ManifestoService manifestoService;
	private DepositoService depositoService;
	private MotoristaService motoristaService;
	private VeiculoService veiculoService;
	private ManifestohistoricoService manifestohistoricoService;
	private UsuarioService usuarioService;
	private TransportadorService transportadorService;
	private NotafiscalsaidaService notafiscalsaidaService;
	private ManifestonotafiscalService manifestonotafiscalService;
	private RotagerenciadoraService rotagerenciadoraService;
	private LogintegracaoaeService logintegracaoaeService;
	private ClienteService clienteService;
	private ManifestocodigobarrasService manifestocodigobarrasService;
	private TiponotafiscalService tiponotafiscalService;
	private RotaService rotaService;
	private DepositofilialService depositofilialService;
	private ManifestofinanceiroService manifestofinanceiroService;
	private ConfiguracaoService configuracaoService;
//	private ImportacaocargaService importacaocargaService;

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
	}
	public void setMotoristaService(MotoristaService motoristaService) {
		this.motoristaService = motoristaService;
	}
	public void setVeiculoService(VeiculoService veiculoService) {
		this.veiculoService = veiculoService;
	}
	public void setManifestohistoricoService(ManifestohistoricoService manifestohistoricoService) {
		this.manifestohistoricoService = manifestohistoricoService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setTransportadorService(TransportadorService transportadorService) {
		this.transportadorService = transportadorService;
	}
	public void setNotafiscalsaidaService(NotafiscalsaidaService notafiscalsaidaService) {
		this.notafiscalsaidaService = notafiscalsaidaService;
	}
	public void setManifestonotafiscalService(ManifestonotafiscalService manifestonotafiscalService) {
		this.manifestonotafiscalService = manifestonotafiscalService;
	}
	public void setRotagerenciadoraService(RotagerenciadoraService rotagerenciadoraService) {
		this.rotagerenciadoraService = rotagerenciadoraService;
	}
	public void setLogintegracaoaeService(LogintegracaoaeService logintegracaoaeService) {
		this.logintegracaoaeService = logintegracaoaeService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setManifestocodigobarrasService(ManifestocodigobarrasService manifestocodigobarrasService) {
		this.manifestocodigobarrasService = manifestocodigobarrasService;
	}
	public void setTiponotafiscalService(TiponotafiscalService tiponotafiscalService) {
		this.tiponotafiscalService = tiponotafiscalService;
	}
	public void setRotaService(RotaService rotaService) {
		this.rotaService = rotaService;
	}
	public void setDepositofilialService(DepositofilialService depositofilialService) {
		this.depositofilialService = depositofilialService;
	}
	public void setManifestofinanceiroService(ManifestofinanceiroService manifestofinanceiroService) {
		this.manifestofinanceiroService = manifestofinanceiroService;
	}
	public void setConfiguracaoService(ConfiguracaoService configuracaoService) {
		this.configuracaoService = configuracaoService;
	}
/*	public void setImportacaocargaService(ImportacaocargaService importacaocargaService) {
		this.importacaocargaService = importacaocargaService;
	}*/
	
	@Override
	protected void listagem(WebRequestContext request, ManifestoFiltro filtro) throws Exception {
		
		if(filtro!=null && filtro.getDeposito()==null){
			filtro.setDeposito(WmsUtil.getDeposito());
		}
		
		if(filtro!=null && filtro.getDefaultValues()){
			filtro.setUsuario(WmsUtil.getUsuarioLogado());
			filtro.setDtemissaoinicio(WmsUtil.currentDate());
		}
		
		request.setAttribute("LISTA_DEPOSITOS", depositoService.findAtivosIndexByUsuario(WmsUtil.getUsuarioLogado()));
		request.setAttribute("DEPOSITO_LOGADO", WmsUtil.getDeposito());
		
		if(filtro!=null && filtro.getTransportador()==null){
			filtro.setMotorista(null);
			filtro.setVeiculo(null);
		}
		super.listagem(request, filtro);
	}
	
	@Override
	protected void entrada(WebRequestContext request, Manifesto manifesto)throws Exception {
		
		String acao = request.getParameter("ACAO");
		
		if(acao != null && acao.toLowerCase().equals("editar")){
			if(manifesto != null && 
					!manifesto.getManifestostatus().equals(Manifestostatus.EM_ELABORACAO) &&
					!manifesto.getManifestostatus().equals(Manifestostatus.AGUARDANDO_LIBERACAO)){
				throw new WmsException("Não é possível editar um Manifesto com status diferente de 'Em Elaboração' ou 'Aguardando Liberação'");
			}
		}
		request.setAttribute("LISTA_DEPOSITO", depositoService.findAtivos());
		

		if(manifesto!=null && manifesto.getCdmanifesto()!=null){
			manifesto.setListaManifestohistorico(manifestohistoricoService.findByManifesto(manifesto));
			if(manifesto.getListaManifestonotafiscal()==null || manifesto.getListaManifestonotafiscal().isEmpty()){
				manifesto.setListaManifestonotafiscal(manifestonotafiscalService.findByManifesto(manifesto));
			}
			request.setAttribute("listaLog", logintegracaoaeService.findByManifesto(manifesto));
		}
		
		if (Tipoentrega.ENTREGA_CLIENTE.equals(manifesto.getTipoentrega()) 
				&& manifesto.getListaManifestonotafiscal() != null 
				&& !manifesto.getListaManifestonotafiscal().isEmpty()
				&& (Manifestostatus.EM_ELABORACAO.equals(manifesto.getManifestostatus()) || 
					 (Manifestostatus.AGUARDANDO_LIBERACAO.equals(manifesto.getManifestostatus())))){
			
			validaNotasPedidoSemFrete(manifesto);
			
			if (CollectionUtils.exists(manifesto.getListaManifestonotafiscal(), 
					new BeanPropertyValueEqualsPredicate("existeFreteClienteNota", Boolean.FALSE))){
				
				StringBuilder msg = new StringBuilder();
				msg.append("Atenção!!! Existem notas inclusas neste manifesto, vinculadas a pedidos que não ")
				.append("tiveram contratação de frete, por este motivo o manifesto somente poderá ")
				.append("ser impresso após autorização");
				
				request.addMessage(msg.toString(), MessageType.TRACE);
			}
		}
		
		if(manifesto!=null && manifesto.getManifestostatus()!=null && manifesto.getManifestostatus().equals(Manifestostatus.EM_ELABORACAO)){
			request.setAttribute("isEmElaboracao", Boolean.TRUE);
		}else{
			request.setAttribute("isEmElaboracao", Boolean.FALSE);
		}
		
		request.setAttribute("isAguardandoLiberacao", manifesto!=null && 
				manifesto.getManifestostatus()!=null && 
				manifesto.getManifestostatus().equals(Manifestostatus.AGUARDANDO_LIBERACAO));
		
		if(manifesto!=null 
				&& manifesto.getCdae()!=null 
				&& manifesto.getManifestostatus()!=null 
				&& (manifesto.getManifestostatus().equals(Manifestostatus.EM_ELABORACAO) || 
					manifesto.getManifestostatus().equals(Manifestostatus.IMPRESSO) || 
					manifesto.getManifestostatus().equals(Manifestostatus.AGUARDANDO_LIBERACAO))){
			request.setAttribute("isAutorizado", Boolean.TRUE);
		}else{
			request.setAttribute("isAutorizado", Boolean.FALSE);
		}
		
		if(manifesto!=null && manifesto.getManifestostatus()!=null && manifesto.getManifestostatus().equals(Manifestostatus.IMPRESSO)){
			request.setAttribute("isImpresso", Boolean.TRUE);
		}else{
			request.setAttribute("isImpresso", Boolean.FALSE);
		}
		
		calcularTotalizadores(request,manifesto);
		
		if (acao != null && acao.toLowerCase().equals("salvar")){
			if (Tipoentrega.TRANSFERENCIA.equals(manifesto.getTipoentrega())
					|| Tipoentrega.CONSOLIDACAO.equals(manifesto.getTipoentrega())){
				manifesto.setTemTransbordo(manifestoService.validarTransbordoNotas(manifesto));
				
				if (manifesto.getTemTransbordo()){
					request.setAttribute("LISTA_DEPOSITO_TRANSBORDO", getListaDeposito());
					manifesto.setListaNotasTransbordo(notafiscalsaidaService.recuperaNotasTransbordoPopUp(manifesto.getCdmanifesto()));
				}
			}
		}
		
		
		if(manifesto!=null && manifesto.getTipoentrega()!=null && manifesto.getTipoentrega().equals(Tipoentrega.AGRUPAMENTO)){
			request.setAttribute("isAgrupamento", "true");
		}else{
			request.setAttribute("isAgrupamento", "false");
		}
		
		Boolean isNotaTransferencia = false;
		
		if(manifesto!=null && manifesto.getListaManifestonotafiscal()!=null && !manifesto.getListaManifestonotafiscal().isEmpty()){
			isNotaTransferencia = manifestoService.existeNotaTransferencia(manifesto);
		}
		
		if(manifesto!=null && manifesto.getTipoentrega()!=null && manifesto.getTipoentrega().equals(Tipoentrega.AGRUPAMENTO) && isNotaTransferencia){
			request.setAttribute("isAgrupadoComTransferencia", "true");
		}else if(manifesto!=null && manifesto.getTipoentrega()!=null && manifesto.getTipoentrega().equals(Tipoentrega.AGRUPAMENTO)){
			request.setAttribute("isAgrupadoComTransferencia", "false");
		}
		
		super.entrada(request, manifesto);
	}
	
	/**
	 * Valida notas com o pedido preenchido para verificar sem tem frete de cliente.
	 *
	 * @param listaManifestonotafiscal the lista manifestonotafiscal
	 * @param deposito the deposito
	 */
	@SuppressWarnings("unchecked")
	private void validaNotasPedidoSemFrete(Manifesto manifesto) {
		
		Boolean validaFreteCliente = ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.VALIDAR_FRETE_CLIENTE, manifesto.getDeposito());
		manifesto.setListaNotaFiscalSemFrete(new ArrayList<Manifestonotafiscal>());
		ArrayList<Integer> listIdCliente = new ArrayList<Integer>();
		Boolean peloMenosUmaNotaComFrete = false; 
		Integer idcliente;
		for (Manifestonotafiscal manifestonotafiscal : manifesto.getListaManifestonotafiscal()) {
			idcliente = manifestonotafiscal.getNotafiscalsaida().getCliente().getCdpessoa();
			if(!listIdCliente.contains(idcliente)){
				listIdCliente.add(idcliente);
			}
		}
		for (Integer codigo : listIdCliente){	
			List<Manifestonotafiscal> listaNotaCliente = (List<Manifestonotafiscal>) CollectionUtils.select(
					manifesto.getListaManifestonotafiscal(), 
					new BeanPropertyValueEqualsPredicate("notafiscalsaida.cliente.cdpessoa", codigo)
			);
			for (Manifestonotafiscal manifestonotafiscalCliente : listaNotaCliente) {
				if(manifestonotafiscalCliente.getNotafiscalsaida().getValorfretecliente().getValue() != null && 
						manifestonotafiscalCliente.getNotafiscalsaida().getValorfretecliente().getValue().compareTo(BigDecimal.ZERO) > 0){
					peloMenosUmaNotaComFrete =  Boolean.TRUE;
				}
			}
			if(peloMenosUmaNotaComFrete.equals(Boolean.TRUE)){
				for (Manifestonotafiscal manifestonotafiscalCliente : listaNotaCliente) {
					manifestonotafiscalCliente.setTemFretePago(Boolean.TRUE);
				}
			}
			peloMenosUmaNotaComFrete  = Boolean.FALSE;
		}		
		for (Manifestonotafiscal manifestonotafiscal : manifesto.getListaManifestonotafiscal()) {
			if (validaFreteCliente){
				manifestonotafiscal.setExisteFreteClienteNota(isNotaSemFreteCliente(manifestonotafiscal));
				
				if (!manifestonotafiscal.getExisteFreteClienteNota()){
					manifesto.getListaNotaFiscalSemFrete().add(manifestonotafiscal);
				}
			}
		}
		
		setManifestoStatus(manifesto);
	}
	
	/**
	 * Valida se a nota tem o valor de frete pago pelo cliente ou foi autorizada a ser manifestada.
	 *
	 * @param manifestonotafiscal the manifestonotafiscal
	 * @return the boolean
	 */
	public Boolean isNotaSemFreteCliente(Manifestonotafiscal manifestonotafiscal) {
		return !(manifestonotafiscal.getTemFretePago() == Boolean.FALSE && 
				manifestonotafiscal.getNotafiscalsaida().getNumeropedido() != null &&
				Tipovenda.LOJA_FISICA.getCdtipovenda().equals(manifestonotafiscal.getNotafiscalsaida().getTipovenda().getCdtipovenda()) &&
				 (manifestonotafiscal.getNotafiscalsaida().getNotaautorizada() == null || 
				   Boolean.FALSE.equals(manifestonotafiscal.getNotafiscalsaida().getNotaautorizada())) && 
				(manifestonotafiscal.getNotafiscalsaida().getValorfretecliente() == null ||  
				 manifestonotafiscal.getNotafiscalsaida().getValorfretecliente().getValue().compareTo(BigDecimal.ZERO) <= 0) &&
				!manifestonotafiscal.getNotafiscalsaida().getTemtroca() &&
				!Notafiscaltipo.DEVOLUCAO.getCdnotafiscaltipo().equals(manifestonotafiscal.getNotafiscalsaida().getNotafiscaltipo().getCdnotafiscaltipo()));
	}
	/**
	 * 
	 * @param manifesto
	 */
	private void calcularTotalizadores(WebRequestContext request, Manifesto manifesto) {
		
		if(manifesto!=null && manifesto.getListaManifestonotafiscal()!=null && !manifesto.getListaManifestonotafiscal().isEmpty()){
			
			Money valorTotal = new Money(0);
			Long qtdeItens = 0L;
			Long qtdeNotas = 0L;
			
			for (Manifestonotafiscal manifestonotafiscal : manifesto.getListaManifestonotafiscal()) {
				if(manifestonotafiscal.getNotafiscalsaida()!=null){
					Notafiscalsaida notafiscalsaida = manifestonotafiscal.getNotafiscalsaida();
					qtdeItens = qtdeItens + (notafiscalsaida.getQtdeitens()==null?0L:notafiscalsaida.getQtdeitens());
					valorTotal = valorTotal.add(notafiscalsaida.getVlrtotalnf()==null?new Money(0):notafiscalsaida.getVlrtotalnf());
					qtdeNotas++;
				}
			}
			
			request.setAttribute("valorTotal", valorTotal);
			request.setAttribute("qtdeItens", qtdeItens);
			request.setAttribute("qtdeNotas", qtdeNotas);
			
		}
	}
	
	@Override
	public Manifesto criar(WebRequestContext request, Manifesto manifesto) throws Exception {
		manifesto = super.criar(request, manifesto);
		setDefaultValuesManifesto(manifesto);
		return manifesto;
	}
	
	@Override
	protected void salvar(WebRequestContext request, Manifesto manifesto) throws Exception {
		

		if(manifesto.getListaManifestonotafiscal()==null || manifesto.getListaManifestonotafiscal().isEmpty()){
			throw new WmsException("Não é possível salvar o manifesto sem ao menos 1 nota vinculada.");
		}else{
			
			Integer cdmanifesto = manifesto.getCdmanifesto();
			
			if(!manifesto.getTipoentrega().equals(Tipoentrega.AGRUPAMENTO) && manifestoService.validarNotasVinculadas(manifesto.getListaManifestonotafiscal(),cdmanifesto)){
				
				request.setAttribute("isRedirectToListagem", "true");	
				throw new WmsException("Esse manifesto não pode ser salvo, as notas estão vinculados a outro manifesto.");
				
			}else if(manifesto.getTipoentrega().equals(Tipoentrega.AGRUPAMENTO) && manifestoService.validarNotasVinculadasAgrupamento(manifesto.getListaManifestonotafiscal(),cdmanifesto,manifesto.getSelectCdmanifesto())){
				
				request.setAttribute("isRedirectToListagem", "true");	
				throw new WmsException("Esse manifesto não pode ser salvo, as notas estão vinculados a outro manifesto.");
				
			}
			
			if(manifesto.getManifestostatus()==null || 
					(!manifesto.getManifestostatus().equals(Manifestostatus.EM_ELABORACAO) && 
					!manifesto.getManifestostatus().equals(Manifestostatus.AGUARDANDO_LIBERACAO))){
				
				throw new WmsException("Não é possível salvar o manifesto. O status está diferente de 'Em Elaboração' e 'Aguardando Liberação'.");
				
			}
			
			if(manifesto!=null && manifesto.getCdmanifesto()!=null){
				String codigo = manifestocodigobarrasService.findCodigoByManifesto(manifesto);
				if(codigo!=null && !codigo.isEmpty()){
					throw new WmsException("Não é possível salvar o manifesto, ele já foi impresso.");
				}
			}
			
			// valida se as informações de Transportador, veiculo e Motorista estão preenchidas.
			if (manifesto.getVeiculo() == null 
					|| manifesto.getVeiculo().getCdveiculo() == null 
					|| manifesto.getTransportador() == null 
					|| manifesto.getTransportador().getCdpessoa() == null
					|| manifesto.getMotorista() == null 
					|| manifesto.getMotorista().getCdmotorista() == null){
				
				throw new WmsException("Não é possível salvar o manifesto, pois as informações de Transportador,"
						+ " Veículo e Motorista são obrigatorias para geração do manifesto");
				
			}
			
			manifesto.setQtdtotalnf(manifesto.getListaManifestonotafiscal().size());
			manifesto.setQtdtotalitensnf(somatorioItensNotas(manifesto.getListaManifestonotafiscal()));
			
			setarStatusEntregaNotas(manifesto);
			
			if (Tipoentrega.ENTREGA_CLIENTE.equals(manifesto.getTipoentrega()) 
					&& (Manifestostatus.EM_ELABORACAO.equals(manifesto.getManifestostatus()) || 
						 (Manifestostatus.AGUARDANDO_LIBERACAO.equals(manifesto.getManifestostatus())))){
				
				validaNotasPedidoSemFrete(manifesto);
				
				if (manifesto.getCdmanifesto() != null && manifesto.getCdmanifesto() > 0){
					setTokenInManifestoNota(manifesto);
				}
			}
			super.salvar(request, manifesto);
			
			if(cdmanifesto==null){
				manifestohistoricoService.criarHistorico(manifesto, Manifestohistorico.CRIAR, manifesto.getManifestostatus(), WmsUtil.getUsuarioLogado(), Tipomanifestohistorico.STATUS);
			}else{
				manifestohistoricoService.criarHistorico(manifesto, Manifestohistorico.EDITAR, manifesto.getManifestostatus(), WmsUtil.getUsuarioLogado(), Tipomanifestohistorico.STATUS);
			}
			
			if(manifesto.getTipoentrega()!=null && manifesto.getTipoentrega().equals(Tipoentrega.AGRUPAMENTO)){
				String whereInNotas = CollectionsUtil.listAndConcatenate(manifesto.getListaManifestonotafiscal(), "notafiscalsaida.cdnotafiscalsaida", ",");
				List<Manifesto> listaManifesto = manifestoService.findAllManifestosVinculados(null,whereInNotas);
				String whereIManifesto = CollectionsUtil.listAndConcatenate(listaManifesto, "cdmanifesto", ",");
				manifestoService.updateManifestosFilhos(manifesto, whereIManifesto);
			}
			
			if(manifesto.getIsSolicitarAprovacao()){
				solicitarAnalise(request, manifesto);
				request.setAttribute("showLogList", Boolean.TRUE);
			}

			/*if (Manifestostatus.AGUARDANDO_LIBERACAO.equals(manifesto.getManifestostatus())){
				manifestoService.enviarEmailPedidosSemFrete(manifesto);
			}*/
		}
	}
	
	/**
	 * Sets the token in manifesto nota.
	 *
	 * @param manifesto the new token in manifesto nota
	 */
	private void setTokenInManifestoNota(Manifesto manifesto) {
		List<Manifestonotafiscal> tokens = manifestonotafiscalService.findTokenByManifesto(manifesto);
		
		for (Manifestonotafiscal manifestonotafiscal : manifesto.getListaManifestonotafiscal()) {
			if (tokens != null && !tokens.isEmpty()){
				Manifestonotafiscal manifestoNotaPrevisao = (Manifestonotafiscal) CollectionUtils.find(tokens, 
						new BeanPropertyValueEqualsPredicate("cdmanifestonotafiscal", manifestonotafiscal.getCdmanifestonotafiscal()));
				
				if (manifestoNotaPrevisao != null){
					manifestonotafiscal.setToken(manifestoNotaPrevisao.getToken());
				}
			}
		}
		
	}
	/**
	 * Método que garante que o transbordo será sempre preenchido quando utilizado.
	 * 
	 * @param listaManifestonotafiscal
	 */
	private void validarTransbordoNotas(List<Manifestonotafiscal> listaManifestonotafiscal) {
		for (Manifestonotafiscal manifestonotafiscal : listaManifestonotafiscal) {
			if (manifestonotafiscal.getNotafiscalsaida().getPraca() != null 
					&& manifestonotafiscal.getNotafiscalsaida().getPraca().getCdpraca() != null){
				
				Rota rota = rotaService.recuperaRotaPorPracaParaValidacaoTransbordo(manifestonotafiscal.getNotafiscalsaida().getPraca());
				
				Boolean temDepositoTransbordoManifesto = manifestonotafiscal.getTemDepositoTransbordo();
				
				Boolean temDepositoTransbordoRota = (rota != null && rota.getTemDepositoTransbordo() == null) ? Boolean.FALSE: rota.getTemDepositoTransbordo();
				
				Deposito depositoTransbordo = rota.getDepositotransbordo();
				
				if (temDepositoTransbordoRota && !temDepositoTransbordoRota.equals(temDepositoTransbordoManifesto)){
					manifestonotafiscal.setTemDepositoTransbordo(temDepositoTransbordoRota);
					manifestonotafiscal.setDepositotransbordo(depositoTransbordo);
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @param request
	 * @param manifesto
	 * @return
	 * @throws CrudException
	 */
	public ModelAndView analisarRota(WebRequestContext request, Manifesto manifesto) throws CrudException {
		
		if(manifesto.getIsSolicitarAprovacao()){
			manifesto = manifestoService.loadForEntrada(manifesto);
			solicitarAnalise(request, manifesto);
			request.setAttribute("showLogList", Boolean.TRUE);
		}
		
		return doEntrada(request, manifesto);
	}
	
	/**
	 * Prepara as notas com informações iniciais.
	 * 
	 * @author Filipe
	 * @param manifesto
	 */
	private void setarStatusEntregaNotas(Manifesto manifesto) {
		List<Manifestonotafiscal> listaManifestonotafiscal = new ArrayList<Manifestonotafiscal>();
		for (Manifestonotafiscal manifestonotafiscal : manifesto.getListaManifestonotafiscal()) {
			if(manifestonotafiscal.getNotafiscalsaida()!=null && manifestonotafiscal.getNotafiscalsaida().getCdnotafiscalsaida()!=null){
				manifestonotafiscal.setStatusconfirmacaoentrega(Statusconfirmacaoentrega.ENTREGA_EM_ANDAMENTO);
				listaManifestonotafiscal.add(manifestonotafiscal);
			}
			if(manifestonotafiscal.getDt_inclusao()==null){
				manifestonotafiscal.setDt_inclusao(WmsUtil.currentDate());
			}
		}
		manifesto.setListaManifestonotafiscal(listaManifestonotafiscal);
	}
	
	/**
	 * 
	 * @param list 
	 * @return
	 */
	private Integer somatorioItensNotas(List<Manifestonotafiscal> list) {
		
		Integer totalItens = 0;
		
		for (Manifestonotafiscal manifestonotafiscal : list) {
			if(manifestonotafiscal.getNotafiscalsaida()!=null && manifestonotafiscal.getNotafiscalsaida().getQtdeitens()!=null){
				totalItens += manifestonotafiscal.getNotafiscalsaida().getQtdeitens();
			}
		}
		
		return totalItens;
	}
	
	@Override
	protected void excluir(WebRequestContext request, Manifesto manifesto) throws Exception {
		
		if(manifesto!=null && manifesto.getCdmanifesto()!=null){
			if(manifestoService.validaManifestoStatusForExcluir(manifesto.getCdmanifesto().toString())){
				manifestoService.desvincularManifestoFilho(manifesto, null);
				manifestonotafiscalService.deleteByManifesto(manifesto.getCdmanifesto().toString());
				manifestohistoricoService.deleteByManifesto(manifesto.getCdmanifesto().toString());
				manifestofinanceiroService.deleteByManifesto(manifesto.getCdmanifesto().toString());
				super.excluir(request, manifesto);	
			}
		}else if(request.getParameter("itenstodelete")!=null){
			if(manifestoService.validaManifestoStatusForExcluir(request.getParameter("itenstodelete"))){
				manifestoService.desvincularManifestoFilho(null, request.getParameter("itenstodelete"));
				manifestonotafiscalService.deleteByManifesto(request.getParameter("itenstodelete"));
				manifestohistoricoService.deleteByManifesto(request.getParameter("itenstodelete"));
				manifestofinanceiroService.deleteByManifesto(request.getParameter("itenstodelete"));
				super.excluir(request, manifesto);
			}else{
				request.addError("Existem manifesto com status diferente de 'Em Elaboração'.");
			}
		}else{
			request.addMessage("Não foi possível executar a exclusão. Por favor, tente novamente.");
		}
		
	}
	
	/**
	 * Método que carrega listas JSON para popular os combos Motorista e Veiculo.
	 * 
	 * @param request
	 * @param filtro
	 * @return JSON
	 */
	public ModelAndView carregaMotoristaVeiculoListagem (WebRequestContext request, ManifestoFiltro filtro){
		
		Transportador transportador = filtro.getTransportador();
		Deposito deposito = filtro.getDeposito();
		
		return carregaMotoristaVeiculo(transportador, deposito);
	}
	
	/**
	 * Método que carrega listas JSON para popular os combos Motorista e Veiculo na tela de Entrada.
	 * 
	 * @param request
	 * @param filtro
	 * @return JSON
	 */
	public ModelAndView carregaMotoristaVeiculoEntrada (WebRequestContext request, Manifesto manifesto){
		
		Transportador transportador = manifesto.getTransportador();
		Deposito deposito = manifesto.getDeposito();
		
		return carregaMotoristaVeiculo(transportador, deposito);
	}
	
	/**
 	 * Método que carrega listas JSON para popular os combos Motorista e Veiculo na tela de Listagem.
	 * 
	 * @param transportador
	 * @param deposito
	 * @return
	 */
	private ModelAndView carregaMotoristaVeiculo(Transportador transportador, Deposito deposito) {
		
		List<Motorista> listaMotorista = motoristaService.findForComboByTransportador(transportador, deposito);
		List<Veiculo> listaVeiculo = veiculoService.findForComboByTransportador(transportador, deposito);
		
		return new JsonModelAndView().addObject("listaMotorista", listaMotorista).addObject("listaVeiculo", listaVeiculo);
	}
	
	/**
	 * Método que altera o status do Manifesto para 'cancelado'.
	 * 
	 * @param request
	 * @param filtro
	 * @return JSON
	 */
	public ModelAndView cancelarManifesto (WebRequestContext request, Auditoria auditoria){
		
		if(auditoria!=null && auditoria.getMotivo()!=null && !auditoria.getMotivo().isEmpty() && auditoria.getId()!=null){
			if(AuditoriaService.validaUsuario(request, auditoria, Acao.CANCELAR_MANIFESTO)){
				try{
					Manifesto manifesto = new Manifesto(auditoria.getId());
					
					Tipoentrega tipoentrega = manifestoService.findTipoEntregaForManifesto(manifesto);
					
					if(tipoentrega.equals(Tipoentrega.AGRUPAMENTO)){
						request.addError("Não é permitido realizar o cancelamento de Manifesto do tipo AGRUPAMENTO pelo sistema. "
								+ "Gentileza entrar em contato com o suporte do sistema para efetuar a ação.");
						
						
						/* Metodo comentado devido a problema de cancelamento para manifesto agrupado.
						 * Onde há a necessidade de desvincular as notas do manifesto de transferencia mas não é realizado.
						 * 
						 * Everton Reis - 13/09/2018
						manifestoService.desvincularManifestoFilho(manifesto,null);*/
					}else{
						manifesto.setListaManifestonotafiscal(manifestonotafiscalService.findByManifesto(manifesto));
						auditoria.setUsuario(usuarioService.findByLoginUsuario(auditoria.getLogin()));
						auditoria.setMotivo(Manifestohistorico.CANCELAR+auditoria.getMotivo());
						manifestohistoricoService.criarHistoricoWithUsuario(manifesto, Manifestostatus.CANCELADO, auditoria, Tipomanifestohistorico.STATUS);
						String whereIn = CollectionsUtil.listAndConcatenate(manifesto.getListaManifestonotafiscal(), "notafiscalsaida.cdnotafiscalsaida", ",");
						notafiscalsaidaService.desvincularNotas(whereIn);
						manifestoService.cancelarManifesto(manifesto);
						
						request.addMessage("Manifesto(s) cancelado(s) com sucesso!");
					}
					
				}catch (Exception e) {
					e.printStackTrace();
					request.addError("Operação não realizada.");
				}
			}
		}else{
			request.addError("Operação não realizada.");
		}
		
		return new JsonModelAndView();
	}
	
	/**
	 * Inicializando os valor padrões do manifesto.
	 * 
	 * @param manifesto
	 */
	private void setDefaultValuesManifesto(Manifesto manifesto) {
		
		manifesto.setDeposito(WmsUtil.getDeposito());
		
		setManifestoStatus(manifesto);
		
		manifesto.setDtemissao(new Timestamp(System.currentTimeMillis()));
		manifesto.setUsuarioemissor(WmsUtil.getUsuarioLogado());
		
		if(manifesto.getTransportador()!=null && manifesto.getTransportador().getCdpessoa()!=null){
			manifesto.setTransportador(transportadorService.load(manifesto.getTransportador()));
			if(manifesto.getMotorista()!=null && manifesto.getMotorista().getCdmotorista()!=null)
				manifesto.setMotorista(motoristaService.load(manifesto.getMotorista()));
			if(manifesto.getVeiculo()!=null && manifesto.getVeiculo().getCdveiculo()!=null)
				manifesto.setVeiculo(veiculoService.load(manifesto.getVeiculo()));
		}
	}
	
	/**
	 * Sets the manifesto status.
	 *
	 * @param manifesto the manifesto
	 * @param existeNotaSemFrete the existe nota sem frete
	 */
	public void setManifestoStatus(Manifesto manifesto) {
		Boolean existeNotaSemFrete = Boolean.FALSE;
		
		
		if (manifesto.getListaManifestonotafiscal() != null && !manifesto.getListaManifestonotafiscal().isEmpty()) {
			existeNotaSemFrete = CollectionUtils.exists(manifesto.getListaManifestonotafiscal(), 
					new BeanPropertyValueEqualsPredicate("existeFreteClienteNota", Boolean.FALSE));
		}
		
		if (existeNotaSemFrete)
			manifesto.setManifestostatus(Manifestostatus.AGUARDANDO_LIBERACAO);
		else
			manifesto.setManifestostatus(Manifestostatus.EM_ELABORACAO);
		}
		
	/**
	 * Chama o popUp de inclusão de pedidos
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param request
	 * @param agenda
	 * @return
	 */
	public ModelAndView buscarNotas(WebRequestContext request, ManifestoFiltro filtro){
		
		Long codigoerp;
		Integer cddeposito;
		String serienfe = filtro.getSerieNota()!=null && !filtro.getSerieNota().isEmpty() ? filtro.getSerieNota().toUpperCase() : null;
		String chavenfe = filtro.getChavenfe();
		Long numeronfe = null;
		Date dataemissao = filtro.getDtemissaoNotaSaida();
		Integer tiponotafiscal;
		
		try{
			 cddeposito = filtro.getDeposito().getCddeposito();
		}catch (Exception e){
			cddeposito = WmsUtil.getDeposito().getCddeposito();
		}
		
		try{
			codigoerp = clienteService.getCodigoerp(filtro.getFilial());
		}catch (Exception e) {
			codigoerp = null;
		}
		
		try {
			numeronfe = Long.parseLong(filtro.getNroNotaSaida());
		} catch (Exception e) {
			numeronfe = null;
		}
		
		try {
			tiponotafiscal = filtro.getTiponotafiscal().getCdtiponotafiscal(); 
		} catch (Exception e) {
			tiponotafiscal = 1;
		}
		
		if(filtro.getCdcarregamento()!=null || filtro.getFilial()!=null || filtro.getCdcargaerp() !=null){
			
			if(filtro.getCdcarregamento()!=null || (filtro.getCdcargaerp() !=null && !filtro.getCdcargaerp().isEmpty()) ){
				
				List<Notafiscalsaida> listaNotafiscalsaida = notafiscalsaidaService.findForListagemPopUp(filtro,false);
				request.setAttribute("NOTAS",listaNotafiscalsaida);			
				
			}else if(manifestonotafiscalService.findNotasSaidaByProcedure(cddeposito, chavenfe, numeronfe, dataemissao, codigoerp, serienfe, tiponotafiscal)){
				
				List<Notafiscalsaida> listaNotafiscalsaida = new ArrayList<Notafiscalsaida>();
				Boolean isMultiCDByCodigoERP = Boolean.FALSE; 
				
				if(filtro.getDepositoSelecionado()!=null && filtro.getDepositoSelecionado().getCddeposito()!=null){
					isMultiCDByCodigoERP = depositofilialService.isMultiCDByCodigoERP(codigoerp);
					System.out.println("faça a atualização");
				}
				
				// realiza a chamada 3 vezes, devido ao tempo de espera de um serviço assicrono para a inclusao da nota na base WMS;
				for (int i=0; i<3; i++){
					
					listaNotafiscalsaida = notafiscalsaidaService.findForListagemPopUp(filtro,isMultiCDByCodigoERP);
					
					if(listaNotafiscalsaida != null && !listaNotafiscalsaida.isEmpty()){
						if (isMultiCDByCodigoERP){
							listaNotafiscalsaida = alteraDepositoFilialMultiCd(filtro, listaNotafiscalsaida);
						}
						
						break;
						
					}
					
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						throw new WmsException(e);
					}
				}
				
				request.setAttribute("NOTAS",listaNotafiscalsaida);
				
			}else{
				
				List<Notafiscalsaida> listaNotafiscalsaida = notafiscalsaidaService.findForListagemPopUp(filtro,false);
				request.setAttribute("NOTAS",listaNotafiscalsaida);
				
			}
		}
		
		request.setAttribute("FILIAL", clienteService.findFilialByDepositoLogado());
		request.setAttribute("TIPONF", tiponotafiscalService.findAll());
		
		return new ModelAndView("direct:/crud/incluirNotasPopUp", "filtro", filtro);
	}
	/**
	 * @param request
	 * @return
	 *//*
	public ModelAndView buscarPacklist(WebRequestContext request, ManifestoFiltro filtro){
		
		if(filtro.getCodigo() !=null){
			
			try {
				
				boolean valid = notafiscalsaidaService.validaPacklist(filtro.getCodigo());
				
				if(!valid){
					
					String result = importacaocargaService.importarNotas(Integer.parseInt(filtro.getCodigo()));
					
					if (!result.equals("OK")){
						request.addError("Erro ao importar as notas para o sistema. Erro :" + result);
					}
				}
				
				filtro.setCodigosImportacaoCarga(filtro.getCodigosImportacaoCarga() + ',' +filtro.getCodigo());
				
				List<ImportacaoCargaSiteVO> registros = importacaocargaService.findimportacaoCargaForIds(filtro.getCodigosImportacaoCarga().substring(1));
				
				if((registros == null || registros.isEmpty() ||
					!CollectionUtils.exists(registros, new BeanPropertyValueEqualsPredicate("cdCarga", NumberUtils.toLong(filtro.getCodigo()))))
					&& request.getMessages().length <= 0){
					request.addError("A Carga "+ filtro.getCodigo()+" já foi importada e as suas notas utilizadas em um manifesto!");
				}
				
				request.setAttribute("REGISTROS", registros);		
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new WmsException ("Erro ao realizar a importação da notas.", e);
			}
				
		}
		
		
		return new ModelAndView("direct:/crud/incluirPackingListPopUp", "filtro", filtro);
	}*/
	
	/**
	 * Altera deposito filial multi cd.
	 *
	 * @param filtro the filtro
	 * @param listaNotafiscalsaida the lista notafiscalsaida
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public List<Notafiscalsaida> alteraDepositoFilialMultiCd(ManifestoFiltro filtro,
			List<Notafiscalsaida> listaNotafiscalsaida) {
		
		if (CollectionUtils.exists(listaNotafiscalsaida, 
				new BeanPropertyValueEqualsPredicate("deposito.cddeposito", filtro.getDepositoSelecionado().getCddeposito()))){
			
			listaNotafiscalsaida = (List<Notafiscalsaida>) CollectionUtils.select(listaNotafiscalsaida, 
					new BeanPropertyValueEqualsPredicate("deposito.cddeposito", filtro.getDepositoSelecionado().getCddeposito()));
		}else{
			notafiscalsaidaService.atualizarDepositoNota(listaNotafiscalsaida,filtro.getDepositoSelecionado());
		}
		return listaNotafiscalsaida;
	}	
	
	/**
	 * 
	 * 
	 * 
	 * @param request
	 * @param manifesto
	 * @return
	 * @throws CrudException
	 */
	public ModelAndView inserirNotas (WebRequestContext request, Manifesto manifesto) throws CrudException{
		
		List<Manifestonotafiscal> listaManifestonotafiscal = new ArrayList<Manifestonotafiscal>();
		List<Notafiscalsaida> listaNotafiscalsaida = null;		
		
		if(manifesto!=null){
			listaNotafiscalsaida = recuperaNotas(manifesto); 
		}
		
		
		if (configuracaoService.isTrue(ConfiguracaoVO.VALIDA_DEVOLUCAO_NO_MANIFESTO, WmsUtil.getDeposito())){
			int tam = listaNotafiscalsaida.size();
			addNotasDevelucao(listaNotafiscalsaida, manifesto);
			if(listaNotafiscalsaida.size() > tam){
				manifesto.setTemNotaDevolucao(true);
			}
		}		
		
		validaNotasRepetidas(request, manifesto, listaNotafiscalsaida);
		if(manifesto.getListaManifestonotafiscal()!=null && !manifesto.getListaManifestonotafiscal().isEmpty())
		removeNotaNula(manifesto.getListaManifestonotafiscal());
		
		if(listaNotafiscalsaida!=null && !listaNotafiscalsaida.isEmpty()) {
			
			for(Notafiscalsaida notafiscalsaida: listaNotafiscalsaida) {
				Manifestonotafiscal manifestonotafiscal = new Manifestonotafiscal();
				manifestonotafiscal.setDt_inclusao(WmsUtil.currentDate());
				manifestonotafiscal.setNotafiscalsaida(notafiscalsaida);
				
				try{
					manifestonotafiscal.setPraca(notafiscalsaida.getPraca());
				}catch (Exception e) {
					System.out.println("Praça não encontrada...");
				}
				manifestonotafiscal.setUsuario(WmsUtil.getUsuarioLogado());
				listaManifestonotafiscal.add(manifestonotafiscal);
			}
			
			if(manifesto!=null && manifesto.getListaManifestonotafiscal()!=null && !manifesto.getListaManifestonotafiscal().isEmpty()){
				manifesto.getListaManifestonotafiscal().addAll(listaManifestonotafiscal);
			}else{
				manifesto.setListaManifestonotafiscal(listaManifestonotafiscal);
			}
			
		}
		
		setDefaultValuesManifesto(manifesto);
		return super.doEntrada(request, manifesto);
	}
	
	/**
	 * Recupera notas fiscais de acordo com o filtro.
	 *
	 * @param manifesto the manifesto
	 * @return the list
	 */
	private List<Notafiscalsaida> recuperaNotas(Manifesto manifesto) {
		if (StringUtils.isNotBlank(manifesto.getSelectCdImportacaoCarga())){
			return notafiscalsaidaService.findByImportacaoCarga(manifesto.getSelectCdImportacaoCarga());
		}
		
		if (StringUtils.isNotBlank(manifesto.getSelectCdnotafiscalsaida())){
			return notafiscalsaidaService.findByWhereIn(manifesto.getSelectCdnotafiscalsaida());
		}
	
		return new ArrayList<Notafiscalsaida>();
	}
	
	public ModelAndView excluirNotas(WebRequestContext request, Manifesto manifesto) throws CrudException{
		return super.doEntrada(request, manifesto);
	}
	
	private void addNotasDevelucao(List<Notafiscalsaida> listaNotafiscalsaida, Manifesto manifesto) {
		List<Notafiscalsaida> listNotaDevolucao = new  ArrayList<Notafiscalsaida>();
		
		for (Notafiscalsaida notafiscalsaida : listaNotafiscalsaida) {
			if( notafiscalsaidaService.isPedidoTroca(notafiscalsaida.getCdnotafiscalsaida()) ){
				Notafiscalsaida notaDevolucao = notafiscalsaidaService.findNotaDevolucao(notafiscalsaida);
				if( notaDevolucao != null){
					notaDevolucao.setCdnotafiscalsaidareferencia(notafiscalsaida.getCdnotafiscalsaida());
					listNotaDevolucao.add(notaDevolucao);
				}
			}
		}
		
		if (listNotaDevolucao != null && !listNotaDevolucao.isEmpty()) {
			for (Notafiscalsaida notafiscalsaida : listNotaDevolucao) {
				listaNotafiscalsaida.add(notafiscalsaida);
			}
			
			if(manifesto!=null && manifesto.getListaNotaFiscalDevolucao()!=null && !manifesto.getListaNotaFiscalDevolucao().isEmpty()){
				manifesto.getListaNotaFiscalDevolucao().addAll(listNotaDevolucao);
			}else{
				manifesto.setListaNotaFiscalDevolucao(listNotaDevolucao);
			}	

		}
	}
	
	/**
	 * Método que autentica usuário para não incluir nota de devolução no manifesto
	 * 
	 * @param request
	 * @param filtro
	 * @return JSON
	 */
	public ModelAndView naoIncluirNotaDevolucao (WebRequestContext request, Auditoria auditoria){
		
		StringBuilder txt = new StringBuilder();
		if(auditoria!=null && auditoria.getMotivo()!=null && !auditoria.getMotivo().isEmpty()){
			if(!AuditoriaService.validaUsuario(request, auditoria, Acao.CANCELAR_MANIFESTO)){
				for (Message msg : request.getMessages()) {
					txt.append( msg.getSource().toString() + " ");
				}
			}
			
		}else{
			txt.append("Preencha todos os dados corretamente! ");
		}
		request.clearMessages();
		return new JsonModelAndView().addObject("error", txt.toString());
	}	
	/**
	 * @param manifesto
	 * @param listaNotafiscalsaida
	 */
	private void validaNotasRepetidas(WebRequestContext request, Manifesto manifesto, List<Notafiscalsaida> listaNotafiscalsaida){
		if(manifesto.getListaManifestonotafiscal()!=null && !manifesto.getListaManifestonotafiscal().isEmpty()){
			for (Manifestonotafiscal manifestonotafiscal : manifesto.getListaManifestonotafiscal()){
				if(manifestonotafiscal.getNotafiscalsaida()!=null && manifestonotafiscal.getNotafiscalsaida().getCdnotafiscalsaida()!=null){
					Notafiscalsaida nfs = manifestonotafiscal.getNotafiscalsaida();
					removeNotaInvalida(listaNotafiscalsaida,nfs);
				}
			}
		}
	}
	
	/**
	 * @param request
	 * @param listaNotafiscalsaida
	 * @param notafiscalsaida
	 */
	private void removeNotaInvalida(List<Notafiscalsaida> listaNotafiscalsaida, Notafiscalsaida notafiscalsaida) {
		Iterator<Notafiscalsaida> iterator = listaNotafiscalsaida.iterator();
		while(iterator.hasNext()){
			Notafiscalsaida nfs = iterator.next();
			if(nfs.equals(notafiscalsaida))	{
				iterator.remove();
			}
		}
	}
	/**
	 * @param request
	 * @param listaNotafiscalsaida
	 * @param notafiscalsaida
	 */
	private void removeNotaNula(List<Manifestonotafiscal> listaManifestonotafiscal) {
		Iterator<Manifestonotafiscal> iterator = listaManifestonotafiscal.iterator();
		while(iterator.hasNext()){
			Manifestonotafiscal mnfs = iterator.next();
			if(mnfs.getNotafiscalsaida() != null && mnfs.getNotafiscalsaida().getCdnotafiscalsaida() == null)	{
				iterator.remove();
			}
		}
	}
		
	/**
	 * 
	 * @param request
	 * @param manifesto
	 * @return
	 */
	public ModelAndView updateTransbordo(WebRequestContext request, Manifestonotafiscal manifestonotafiscal){
		
		String error = null;
		
		try{
			manifestonotafiscalService.updateDepositoTransbordo(manifestonotafiscal);	
		}catch (Exception e) {
			e.printStackTrace();
			error = "Erro ao atualizar as informações de transbordo da nota. Por favor, tente novemente.";
		}
		
		return new JsonModelAndView().addObject("error", error);
	}

	/**
	 * Recupera todos os depósitos ativos para seleção de transbordo.
	 * O depósito atual será removido da lista para evitar a seleção de um CD de Transbordo identico a Origem.
	 * @return lista Deposito
	 */
	private List<Deposito> getListaDeposito(){
		
		List<Deposito> listaDeposito = depositoService.findAtivos();
		Iterator<Deposito> iteratorDeposito = listaDeposito.iterator();
		
		while(iteratorDeposito.hasNext()){
			Deposito deposito = iteratorDeposito.next();
			if(deposito.getCddeposito().equals(WmsUtil.getDeposito().getCddeposito())){
				iteratorDeposito.remove();
			}
		}
		
		return listaDeposito;
	}
	
	/**
	 * 
	 * @param request
	 * @param deposito
	 * @return
	 */
	public ModelAndView atualizarRotaGerenciadora(WebRequestContext request, Deposito deposito){		
		
		String retorno = rotagerenciadoraService.callAtualizaRotaGerenciadora(deposito);
		List<Rotagerenciadora> listaRotagerenciadora = rotagerenciadoraService.findAllByDepositoLogado();
		
		return new JsonModelAndView().addObject("retorno",retorno).addObject("listaRotagerenciadora", listaRotagerenciadora);
		
	}
	
	/**
	 * 
	 * @param request
	 * @param deposito
	 * @return
	 */
	private void solicitarAnalise(WebRequestContext request, Manifesto manifesto){
		
		Integer cdae = null;
		String retorno = manifestoService.callCriarAE(manifesto);
		String retornoCombonho = null;
		
		List<Logintegracaoae> listaLog = logintegracaoaeService.findByManifesto(manifesto);
		
		if(listaLog!=null && !listaLog.isEmpty()){
			for (Logintegracaoae logintegracaoae : listaLog) {
				if(logintegracaoae!=null && logintegracaoae.getCdae()!=null){
					cdae = logintegracaoae.getCdae();
				}
			}
		}
		
		if(cdae != null && manifesto!=null && manifesto.getCdmanifesto()!= null){

			try {
				
				retornoCombonho = manifestoService.callVerificaCombonhoOpenTech(cdae,WmsUtil.getDeposito().getCddeposito());
				
				if(retornoCombonho!=null && !retornoCombonho.isEmpty())
					logintegracaoaeService.criarLogIntegracaoOpenTech(cdae,manifesto,retornoCombonho,1);
				
			} catch (Exception e) {
				
				e.printStackTrace();
				System.out.println(retornoCombonho);
				
				request.addError("Ocorreu um erro durante a execução do procedimento de Verificação de Combonho da OpenTech.");
				
			}
			
			manifestoService.updateCDAE(cdae,manifesto.getCdmanifesto());
			
			request.addMessage(retorno);
			
		}else{
			
			request.addError(retorno);
			
		}
		
	}

	/**
	 * Chama o popUp de inclusão de Manifestos
	 * 
	 * @author Filipe Santos
	 * @param request
	 * @param agenda
	 * @return
	 */
	public ModelAndView buscarManifesto(WebRequestContext request, Manifesto manifesto){	
		return new ModelAndView("direct:/crud/incluirManifestoPopUp", "manifesto", manifesto);
	}	
	
	/**
	 * Chama o popUp de inclusão de Manifestos
	 * 
	 * @author Filipe Santos
	 * @param request
	 * @param agenda
	 * @return
	 */
	public ModelAndView buscarPackingList(WebRequestContext request, Manifesto manifesto){	
		return new ModelAndView("direct:/crud/incluirPackingListPopUp", "manifesto", manifesto);
	}	
	
	/**
	 * 
	 * @return
	 */
	public ModelAndView carregarManifestoForAgrupamento(WebRequestContext request, Manifesto manifesto){
		
		Manifesto manifestofilho = manifestoService.findByCodigoBarrasByAgrupamento(manifesto.getCodigobarras(), WmsUtil.getDeposito(), Manifestostatus.IMPRESSO);
		
		if(manifestofilho!=null && manifestofilho.getCdmanifesto()!=null && manifestofilho.getManifestostatus().equals(Manifestostatus.IMPRESSO)){
			if(manifesto.getListaManifesto()==null || manifesto.getListaManifesto().isEmpty()){
				List<Manifesto> listaManifesto = new ArrayList<Manifesto>();
				listaManifesto.add(manifestofilho);
				manifesto.setListaManifesto(listaManifesto);
			}else{
				manifesto.getListaManifesto().add(manifestofilho);
			}
		}else {
			request.setAttribute("msg", "O Manifesto bipado está com status diferente de impresso ou já está agrupado a outro manifesto.");
		}
		
		return new ModelAndView("direct:/crud/incluirManifestoPopUp", "manifesto", manifesto);
	}

	/**
	 * 
	 * @param request
	 * @param manifesto
	 * @return
	 * @throws CrudException 
	 */
	public ModelAndView inserirManifestos (WebRequestContext request, Manifesto manifesto) throws CrudException{
		if(manifesto.getSelectCdmanifesto()==null || manifesto.getSelectCdmanifesto().isEmpty()){
			request.addError("Para inlcuir é necessário vincular ao menos 1 manifesto.");
		}else{
			List<Manifestonotafiscal> listaManifestonotafiscal = manifestonotafiscalService.findAllbyManifesto(null,manifesto.getSelectCdmanifesto());
			if(manifesto.getListaManifestonotafiscal()!=null && !manifesto.getListaManifestonotafiscal().isEmpty()){
				removerNotasRepetidas(listaManifestonotafiscal,manifesto);
				manifesto.getListaManifestonotafiscal().addAll(listaManifestonotafiscal);
			}else{
				manifesto.setListaManifestonotafiscal(listaManifestonotafiscal);
			}
		}
		setDefaultValuesManifesto(manifesto);
		return super.doEntrada(request, manifesto);
	}

	/**
	 * 
	 * @param listaManifestonotafiscal
	 * @param manifesto 
	 */
	private void removerNotasRepetidas(List<Manifestonotafiscal> listaManifestonotafiscal, Manifesto manifesto) {
		
		for (Manifestonotafiscal mnf : manifesto.getListaManifestonotafiscal()) {
			if(mnf.getNotafiscalsaida()==null || mnf.getNotafiscalsaida().getCdnotafiscalsaida()==null){
				continue;
			}else{
				Integer notaIncluida = mnf.getNotafiscalsaida().getCdnotafiscalsaida();
				Iterator<Manifestonotafiscal> iteratorListaNota = listaManifestonotafiscal.iterator();
				while(iteratorListaNota.hasNext()){
					Manifestonotafiscal mnfNovo = iteratorListaNota.next();
					Integer notaNova = mnfNovo.getNotafiscalsaida().getCdnotafiscalsaida();
					if(notaIncluida.equals(notaNova))
						iteratorListaNota.remove();
				}
			}
		}
		
	}
	
	@Override
	protected boolean listagemVaziaPrimeiraVez() {
		return true;
	}
	
	/**
	 * 
	 * @param request
	 * @param cliente
	 * @return
	 */
	public ModelAndView buscarDepositoByFilial(WebRequestContext request, Cliente cliente){
		
		Boolean isShowModal = Boolean.FALSE;
		List<Depositofilial> lista = depositofilialService.findByFilial(cliente);
		
		if(lista!=null && !lista.isEmpty() && lista.size() > 1){
			isShowModal = Boolean.TRUE;
		}
		
		return new JsonModelAndView().addObject("isShowModal",isShowModal).addObject("listaDepositoFilial", lista); 
		
	}

	/**
	 * 
	 * @param request
	 * @param deposito
	 * @return
	 */
	public ModelAndView getNomeDepositoSelecionado(WebRequestContext request, Deposito deposito){
		
		if(deposito!=null && deposito.getCddeposito()!=null)
			deposito = depositoService.load(deposito);
		
		return new JsonModelAndView().addObject("nomeDeposito",deposito.getNome());
		
	}
	
	/**
	 * Liberar notas para manifestar sem frete.
	 *
	 * @param request the request
	 * @param manifesto the manifesto
	 * @return the model and view
	 * @throws Exception 
	 */
	public ModelAndView liberarNotas (WebRequestContext request, Manifesto manifesto) throws Exception{
		
		List<Manifestonotafiscal> notasSemFrete = manifesto.getListaNotaFiscalSemFrete();
		
		StringBuilder notasAutorizadas = new StringBuilder();
		
		manifesto = manifestoService.loadForEntrada(manifesto);
		
		manifesto.setListaManifestonotafiscal(manifestonotafiscalService.findByManifesto(manifesto));
		
		if (notasSemFrete != null && !notasSemFrete.isEmpty()){
			for (Manifestonotafiscal manifestonotafiscal : manifesto.getListaManifestonotafiscal()) {
				Manifestonotafiscal notaSemFrete = (Manifestonotafiscal) CollectionUtils.find(notasSemFrete, new BeanPropertyValueEqualsPredicate("cdmanifestonotafiscal",
						manifestonotafiscal.getCdmanifestonotafiscal()));
				
				if (notaSemFrete != null){
					if (manifestonotafiscal.getToken().equals(notaSemFrete.getSenhaAutorizacao())){
						
						manifestonotafiscal.getNotafiscalsaida().setNotaautorizada(Boolean.TRUE);
						notasAutorizadas.append(manifestonotafiscal.getNotafiscalsaida().getCdnotafiscalsaida()).append( ",");
						
						manifestohistoricoService.criarHistorico(manifesto, "Nota "+ manifestonotafiscal.getNotafiscalsaida().getNumero()+ " autorizada pelo usuário "+WmsUtil.getUsuarioLogado().getNome(), 
								manifesto.getManifestostatus(), WmsUtil.getUsuarioLogado(), Tipomanifestohistorico.STATUS);
					}else{
						request.addError("A senha informada para a nota " + manifestonotafiscal.getNotafiscalsaida().getNumero()+ " está incorreta.");
					}
				}
			}
		}
		
		if (StringUtils.isNotBlank(notasAutorizadas.toString())){
			notafiscalsaidaService.autorizarNotasSemFreteCliente(notasAutorizadas.toString().substring(0, notasAutorizadas.toString().length() - 1));
		}
		
		salvar(request, manifesto);
		
		request.setAttribute(CONSULTAR, true);
		return super.doEntrada(request, manifesto);
	}
	
	/**
	 *
	 * @param request the request
	 * @param manifesto the manifesto
	 * @return the model and view
	 * @throws Exception 
	 */
	public ModelAndView incluirTransbordoNotas (WebRequestContext request, Manifesto manifesto) throws Exception{
		
		manifestonotafiscalService.incluirTransbordoNotas(manifesto.getListaNotasTransbordo());
		
		manifesto = manifestoService.loadForEntrada(manifesto);
		
		return super.doConsultar(request, manifesto);
	}
	
	
}