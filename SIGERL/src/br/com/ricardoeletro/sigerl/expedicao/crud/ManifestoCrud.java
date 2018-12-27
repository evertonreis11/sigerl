package br.com.ricardoeletro.sigerl.expedicao.crud;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.neo.view.ajax.JsonModelAndView;
import br.com.linkcom.wms.geral.bean.Acao;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Logintegracaoae;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestohistorico;
import br.com.linkcom.wms.geral.bean.Manifestonotafiscal;
import br.com.linkcom.wms.geral.bean.Manifestostatus;
import br.com.linkcom.wms.geral.bean.Motorista;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.Rotagerenciadora;
import br.com.linkcom.wms.geral.bean.Statusconfirmacaoentrega;
import br.com.linkcom.wms.geral.bean.Tipoentrega;
import br.com.linkcom.wms.geral.bean.Tipomanifestohistorico;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.geral.bean.Veiculo;
import br.com.linkcom.wms.geral.bean.vo.Auditoria;
import br.com.linkcom.wms.geral.service.AuditoriaService;
import br.com.linkcom.wms.geral.service.ClienteService;
import br.com.linkcom.wms.geral.service.DepositoService;
import br.com.linkcom.wms.geral.service.LogintegracaoaeService;
import br.com.linkcom.wms.geral.service.ManifestoService;
import br.com.linkcom.wms.geral.service.ManifestocodigobarrasService;
import br.com.linkcom.wms.geral.service.ManifestohistoricoService;
import br.com.linkcom.wms.geral.service.ManifestonotafiscalService;
import br.com.linkcom.wms.geral.service.MotoristaService;
import br.com.linkcom.wms.geral.service.NotafiscalsaidaService;
import br.com.linkcom.wms.geral.service.RotaService;
import br.com.linkcom.wms.geral.service.RotagerenciadoraService;
import br.com.linkcom.wms.geral.service.TransportadorService;
import br.com.linkcom.wms.geral.service.UsuarioService;
import br.com.linkcom.wms.geral.service.VeiculoService;
import br.com.linkcom.wms.util.CrudController;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
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
	private RotaService rotaService;
	
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
	public void setRotaService(RotaService rotaService) {
		this.rotaService = rotaService;
	}
	
	
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
			if(manifesto != null && !manifesto.getManifestostatus().equals(Manifestostatus.EM_ELABORACAO)){
				throw new WmsException("Não é possível editar um Manifesto com status diferente de 'Em Elaboração'.");
			}
		}
		
		if(manifesto!=null && manifesto.getManifestostatus()!=null && manifesto.getManifestostatus().equals(Manifestostatus.EM_ELABORACAO)){
			request.setAttribute("isEmElaboracao", Boolean.TRUE);
		}else{
			request.setAttribute("isEmElaboracao", Boolean.FALSE);
		}
		
		if(manifesto!=null && manifesto.getCdae()!=null && manifesto.getManifestostatus()!=null && (manifesto.getManifestostatus().equals(Manifestostatus.EM_ELABORACAO) || manifesto.getManifestostatus().equals(Manifestostatus.IMPRESSO))){
			request.setAttribute("isAutorizado", Boolean.TRUE);
		}else{
			request.setAttribute("isAutorizado", Boolean.FALSE);
		}
		
		if(manifesto!=null && manifesto.getManifestostatus()!=null && manifesto.getManifestostatus().equals(Manifestostatus.IMPRESSO)){
			request.setAttribute("isImpresso", Boolean.TRUE);
		}else{
			request.setAttribute("isImpresso", Boolean.FALSE);
		}
		
		if(manifesto!=null && manifesto.getCdmanifesto()!=null){
			manifesto.setListaManifestohistorico(manifestohistoricoService.findByManifesto(manifesto));
			if(manifesto.getListaManifestonotafiscal()==null || manifesto.getListaManifestonotafiscal().isEmpty()){
				manifesto.setListaManifestonotafiscal(manifestonotafiscalService.findByManifesto(manifesto));
			}
			request.setAttribute("listaLog", logintegracaoaeService.findByManifesto(manifesto));
		}
		
		calcularTotalizadores(request,manifesto);
		
		request.setAttribute("LISTA_DEPOSITO_TRANSBORDO", getListaDeposito());
		
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
			
			if(manifesto.getManifestostatus()==null || !manifesto.getManifestostatus().equals(Manifestostatus.EM_ELABORACAO)){
				throw new WmsException("Não é possível salvar o manifesto. O status está diferente de 'Em Elaboração'.");
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
				solicitarAnalise(manifesto);
				request.setAttribute("showLogList", Boolean.TRUE);
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
			solicitarAnalise(manifesto);
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
				super.excluir(request, manifesto);	
			}
		}else if(request.getParameter("itenstodelete")!=null){
			if(manifestoService.validaManifestoStatusForExcluir(request.getParameter("itenstodelete"))){
				manifestoService.desvincularManifestoFilho(null, request.getParameter("itenstodelete"));
				manifestonotafiscalService.deleteByManifesto(request.getParameter("itenstodelete"));
				manifestohistoricoService.deleteByManifesto(request.getParameter("itenstodelete"));
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
					manifesto.setListaManifestonotafiscal(manifestonotafiscalService.findByManifesto(manifesto));
					String whereIn = CollectionsUtil.listAndConcatenate(manifesto.getListaManifestonotafiscal(), "notafiscalsaida.cdnotafiscalsaida", ",");
					auditoria.setUsuario(usuarioService.findByLoginUsuario(auditoria.getLogin()));
					auditoria.setMotivo(Manifestohistorico.CANCELAR+auditoria.getMotivo());
					manifestohistoricoService.criarHistoricoWithUsuario(manifesto, Manifestostatus.CANCELADO, auditoria, Tipomanifestohistorico.STATUS);
					notafiscalsaidaService.desvincularNotas(whereIn);
					manifestoService.cancelarManifesto(manifesto);
					
					Tipoentrega tipoentrega = manifestoService.findTipoEntregaForManifesto(manifesto);
					if(tipoentrega.equals(Tipoentrega.AGRUPAMENTO)){
						manifestoService.desvincularManifestoFilho(manifesto,null);
					}
					
					request.addMessage("Manifesto(s) cancelado(s) com sucesso!");
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
		manifesto.setManifestostatus(Manifestostatus.EM_ELABORACAO);
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
		
		/*try {
			tiponotafiscal = filtro.getTiponotafiscal().getCdtiponotafiscal(); 
		} catch (Exception e) {
			tiponotafiscal = 1;
		}*/
		
		if(filtro.getCdcarregamento()!=null || filtro.getFilial()!=null || filtro.getCdcargaerp() !=null){
			if(filtro.getCdcarregamento()!=null || (filtro.getCdcargaerp() !=null && !filtro.getCdcargaerp().isEmpty()) ){
				List<Notafiscalsaida> listaNotafiscalsaida = notafiscalsaidaService.findForListagemPopUp(filtro);
				request.setAttribute("NOTAS",listaNotafiscalsaida);			
				/*}else if(manifestonotafiscalService.findNotasSaidaByProcedure(cddeposito, chavenfe, numeronfe, dataemissao, codigoerp, serienfe, tiponotafiscal)){
				
				List<Notafiscalsaida> listaNotafiscalsaida = new ArrayList<Notafiscalsaida>();
				
				// realiza a chamada 3 vezes, devido ao tempo de espera de um serviço assicrono para a inclusao da nota na base WMS;
				for (int i=0; i<3; i++){
					listaNotafiscalsaida = notafiscalsaidaService.findForListagemPopUp(filtro);
					
					if (listaNotafiscalsaida != null && !listaNotafiscalsaida.isEmpty())
						break;
					
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						throw new WmsException(e);
					}
				}
				
				request.setAttribute("NOTAS",listaNotafiscalsaida);*/
			}else{
				List<Notafiscalsaida> listaNotafiscalsaida = notafiscalsaidaService.findForListagemPopUp(filtro);
				request.setAttribute("NOTAS",listaNotafiscalsaida);				
			}
		}
		
		request.setAttribute("FILIAL", clienteService.findFilialByDepositoLogado());
		
		return new ModelAndView("direct:/crud/incluirNotasPopUp", "filtro", filtro);
	}	
	
	/**
	 * 
	 * @param request
	 * @param manifesto
	 * @return
	 * @throws CrudException
	 */
	public ModelAndView inserirNotas (WebRequestContext request, Manifesto manifesto) throws CrudException{
		
		List<Manifestonotafiscal> listaManifestonotafiscal = new ArrayList<Manifestonotafiscal>();
		List<Notafiscalsaida> listaNotafiscalsaida = new ArrayList<Notafiscalsaida>();		
		
		if(manifesto!=null && manifesto.getSelectCdnotafiscalsaida()!=null && !manifesto.getSelectCdnotafiscalsaida().isEmpty()){
			listaNotafiscalsaida = notafiscalsaidaService.findByWhereIn(manifesto.getSelectCdnotafiscalsaida());
		}
		
		validaNotasRepetidas(request, manifesto, listaNotafiscalsaida);
		
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
	public ModelAndView solicitarAnalise(WebRequestContext request, Manifesto manifesto){
		
		Integer cdae = null;
		String retorno = manifestoService.callCriarAE(manifesto);
		List<Logintegracaoae> listaLog = logintegracaoaeService.findByManifesto(manifesto);
		
		if(listaLog!=null && !listaLog.isEmpty()){
			for (Logintegracaoae logintegracaoae : listaLog) {
				if(logintegracaoae!=null && logintegracaoae.getCdae()!=null){
					cdae = logintegracaoae.getCdae();
				}
			}
		}		
		
		if(cdae != null && manifesto!=null && manifesto.getCdmanifesto()!= null){
			manifestoService.updateCDAE(cdae,manifesto.getCdmanifesto());
		}
		
		return new JsonModelAndView().addObject("retorno",retorno).addObject("listaLog", listaLog).addObject("cdae", cdae);
	}
	
	/**
	 * 
	 * @param manifesto
	 */
	private void solicitarAnalise(Manifesto manifesto){
		
		Integer cdae = null;
		manifestoService.callCriarAE(manifesto);
		List<Logintegracaoae> listaLog = logintegracaoaeService.findByManifesto(manifesto);
		
		if(listaLog!=null && !listaLog.isEmpty()){
			for (Logintegracaoae logintegracaoae : listaLog) {
				if(logintegracaoae!=null && logintegracaoae.getCdae()!=null){
					cdae = logintegracaoae.getCdae();
				}
			}
		}		
		
		if(cdae != null && manifesto!=null && manifesto.getCdmanifesto()!= null){
			manifestoService.updateCDAE(cdae,manifesto.getCdmanifesto());
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
			request.setAttribute("msg", "O Manifesto bibpado está com status diferente de impresso ou já está agrupado a outro manifesto.");
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
	
}