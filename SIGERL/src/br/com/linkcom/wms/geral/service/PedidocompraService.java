package br.com.linkcom.wms.geral.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Agendaparcial;
import br.com.linkcom.wms.geral.bean.Agendapedido;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Pedidocompra;
import br.com.linkcom.wms.geral.bean.Pedidocompraproduto;
import br.com.linkcom.wms.geral.bean.Pedidocomprastatus;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtoclasse;
import br.com.linkcom.wms.geral.bean.vo.PedidoCompraResumoVO;
import br.com.linkcom.wms.geral.bean.vo.ResumoAgendaverba;
import br.com.linkcom.wms.geral.dao.PedidocompraDAO;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.LiberarPedidoCompraFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.PedidocompraFiltro;
import br.com.linkcom.wms.modulo.sistema.controller.process.filtro.AgendamentoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class PedidocompraService extends GenericService<Pedidocompra> {
	
	private PedidocompraDAO pedidocompraDAO;
	private AgendapedidoService agendapedidoService;
	private PedidocompraprodutoService pedidocompraprodutoService;
	private AgendaparcialService agendaparcialService;
	private PedidocompraService pedidocompraService;
	private AgendaverbaService agendaverbaService;
	private AgendaverbafinanceiroService agendaverbafinanceiroService;
	private AgendaService agendaService;
	private ProdutoclasseService produtoclasseService;
	
	public void setPedidocompraDAO(PedidocompraDAO pedidocompraDAO) {
		this.pedidocompraDAO = pedidocompraDAO;
	}
	
	public void setAgendapedidoService(AgendapedidoService agendapedidoService) {
		this.agendapedidoService = agendapedidoService;
	}
	
	public void setPedidocompraprodutoService(PedidocompraprodutoService pedidocompraprodutoService) {
		this.pedidocompraprodutoService = pedidocompraprodutoService;
	}
	
	public void setAgendaparcialService(AgendaparcialService agendaparcialService) {
		this.agendaparcialService = agendaparcialService;
	}
	
	public void setPedidocompraService(PedidocompraService pedidocompraService) {
		this.pedidocompraService = pedidocompraService;
	}
	
	public void setAgendaverbaService(AgendaverbaService agendaverbaService) {
		this.agendaverbaService = agendaverbaService;
	}
	
	public void setAgendaverbafinanceiroService(AgendaverbafinanceiroService agendaverbafinanceiroService) {
		this.agendaverbafinanceiroService = agendaverbafinanceiroService;
	}
	
	public void setAgendaService(AgendaService agendaService) {
		this.agendaService = agendaService;
	}
	
	public void setProdutoclasseService(ProdutoclasseService produtoclasseService) {
		this.produtoclasseService = produtoclasseService;
	}
	
	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PedidocompraDAO#findForLiberarPedido
	 *  
	 * @param filtro
	 * @return
	 * @author Rodrigo Freitas
	 */
	public List<Pedidocompra> findForLiberarPedido(LiberarPedidoCompraFiltro filtro){
		return pedidocompraDAO.findForLiberarPedido(filtro);
	}
	
	/**
	 * Faz referência ao DAO.
	 *
	 * @see br.com.linkcom.wms.geral.dao.PedidocompraDAO#findForLiberarPedido
	 *
	 * @param pedidos
	 * @return
	 * @author Rodrigo Freitas
	 */
	public List<Pedidocompra> findForLiberarPedido(String pedidos) {
		return pedidocompraDAO.findForLiberarPedido(pedidos);
	}
	
	/**
	 * Método de referência ao DAO
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.dao.PedidocompraDAO#filtrar(AgendamentoFiltro agendamentoFiltro)
	 * @param agendamentoFiltro
	 * @return
	 */
	public List<Pedidocompra> filtrar(AgendamentoFiltro agendamentoFiltro){
		return pedidocompraDAO.filtrar(agendamentoFiltro);
	}
	
	/**
	 * Método de referência ao DAO
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.dao.PedidocompraDAO#findByPedidocompra
	 * @param pedidocompra
	 * @return
	 */
	public Pedidocompra findByPedidocompra(Pedidocompra pedidocompra) {
		return pedidocompraDAO.findByPedidocompra(pedidocompra);
	}
	
	/**
	 * Método que retorna false se o pedido tiver algum agendamento que não é parcial
	 * @author Leonardo Guimarães
	 * @param pedidocompra
	 * @return
	 */
	public boolean isParcial(Pedidocompra pedidocompra) {
		if(pedidocompra == null){
			throw new WmsException("O pedidocompra não deve ser nulo.");
		}
		List<Agendapedido> listaagendapedido = agendapedidoService.findBy(pedidocompra,"parcial");
		if(!listaagendapedido.isEmpty()){
			for (Agendapedido agendapedido : listaagendapedido) {
				if(!agendapedido.getParcial()){
					return false;
				}
			}
		}
		return true;	
	}
	
	/**
	 * Prepara um lista de pedidos de compra através dos cds contidos
	 * na string mandada como parâmentro e prepara uma lista de Boolean
	 * dependendo se o pedidocompra é ou não parcial 
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.service.PedidocompraService#findByPedidocompra(Pedidocompra pedidocompra)
	 * @see br.com.linkcom.wms.util.neo.persistence.GenericService#findBy(Object o, String... extraFields)
	 * @param cds
	 * @param listaPedidoCompra
	 * @param listaParcial 
	 */
	public void makeListaPedidoCompra(String cds,List<Pedidocompra> listaPedidoCompra,List<Boolean> listaParcial){
		if(cds == null){
			throw new WmsException("O cd não deve ser nulo");
		}
		String[] cheksSelecionados = cds.split(",");
		for (String checkValue : cheksSelecionados) {
			Pedidocompra pedidocompra = new Pedidocompra();
			pedidocompra.setCdpedidocompra(Integer.parseInt(checkValue));
			pedidocompra = findByPedidocompra(pedidocompra);
		/*	if(listaParcial != null){
				boolean existeAgendamentoParcial = agendapedidoService.existeAgendamentoParcial(pedidocompra);
				if(!existeAgendamentoParcial){
					listaParcial.add(!pedidocompraprodutoService.isLiberadoIntegral(pedidocompra));
				}else{
					listaParcial.add(existeAgendamentoParcial);
				}
			}*/
			listaPedidoCompra.add(pedidocompra);
		}
	}
	
	/**
	 * Carrega os dados para abrir o pop up de incluir produtos no agendamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param pedidocompra
	 * @param agenda
	 */
	public void carregarDadosPopUp(Pedidocompra pedidocompra, Agenda agenda) {
		List<Pedidocompraproduto> listaPedidoCompraProduto = pedidocompraprodutoService.findNotIncluded(pedidocompra,agenda);
		List<Agendaparcial> listaagendaparcial = new ArrayList<Agendaparcial>();
		for (Pedidocompraproduto pedidocompraproduto : listaPedidoCompraProduto) {
			listaagendaparcial = agendaparcialService.findByPedidoCompraProduto(pedidocompraproduto);
			pedidocompraproduto.setQtdetotal(pedidocompraproduto.getQtde());
			if(!listaagendaparcial.isEmpty()){
				for (Agendaparcial agendaparcial : listaagendaparcial) {
					pedidocompraproduto.setQtde(pedidocompraproduto.getQtde()-agendaparcial.getQtde());
				}
			}	
			pedidocompraproduto.setQtdedisponivel(pedidocompraproduto.getQtde());
		}
		pedidocompra.setListapedidocompraproduto(new ListSet<Pedidocompraproduto>(Pedidocompraproduto.class,listaPedidoCompraProduto));
	}

	/**
	 * Faz referência ao DAO.
	 *
	 * @see br.com.linkcom.wms.geral.dao.PedidocompraDAO#updateStatusPedidoCompra
	 *
	 * @param pc
	 * @param status
	 * @author Rodrigo Freitas
	 */
	public void updateStatusPedidoCompra(Pedidocompra pc, Pedidocomprastatus status) {
		pedidocompraDAO.updateStatusPedidoCompra(pc, status);
	}
	
	/**
	 * Busca os totais liberados por mês dentro de um semestre.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ResumoAgendaverba> findResumoLiberado(Deposito deposito, Date exercicio){
		return pedidocompraDAO.findResumoLiberado(deposito, exercicio);
	}
	
	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PedidocompraDAO#loadForValidacaoAgendamento
	 *
	 * @param pc
	 * @return
	 * @author Rodrigo Freitas
	 */
	public Pedidocompra loadForValidacaoAgendamento(Pedidocompra pc) {
		return pedidocompraDAO.loadForValidacaoAgendamento(pc);
	}

	/**
	 * Lista os pedidos de compra para um determinado mês de acordo com o seu status.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public List<PedidoCompraResumoVO> filtrarMesStatus(PedidocompraFiltro filtro) {
		return pedidocompraDAO.filtrarMesStatus(filtro);
	}

	/**
	 * Método que faz a validação das verbas financeiras e de recebimento no agendamento.
	 * 
	 * @param filtro
	 * @return
	 * @author Giovane Freitas
	 * @param request 
	 */
	public List<String> validacaoVerbaAgendamento(LiberarPedidoCompraFiltro filtro, WebRequestContext request) {
			
		Map<String, Money> mapRecebimento = new HashMap<String, Money>();
		Map<String, Money> mapFinanceiro = new HashMap<String, Money>();
		Map<String, Money> mapLiberado = new HashMap<String, Money>();
		Map<String, Money> mapLiberadoRecebimento = new HashMap<String, Money>();
		Map<String, Money> mapLiberadoFinanceiro = new HashMap<String, Money>();
		
		List<String> listaPedidoParcial = new ArrayList<String>();
		
		List<Pedidocompra> listaPedidocompra = new ArrayList<Pedidocompra>();
		
		if (filtro.getTotal() != null && !filtro.getTotal().isEmpty()){
			listaPedidocompra.addAll(pedidocompraService.findForLiberarPedido(filtro.getTotal()));
		}
		
		if (filtro.getParcial() != null && !filtro.getParcial().isEmpty()){
			listaPedidocompra.addAll(pedidocompraService.findForLiberarPedido(filtro.getParcial()));
			listaPedidoParcial.addAll(Arrays.asList(filtro.getParcial().split(",")));
		}
		
		Deposito deposito = null;
		if (!ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.VALIDACAO_VERBA_DEPOSITO, null)){
			deposito = null;
		}else{
			deposito = filtro.getDeposito();
		}
		for (Pedidocompra pedidocompra : listaPedidocompra) {
			if (filtro.getDeposito() == null || !filtro.getDeposito().getCddeposito().equals(pedidocompra.getDeposito().getCddeposito()))
				filtro.setDeposito(pedidocompra.getDeposito());
			
			
			if(pedidocompra.getListapedidocompraproduto() != null){
				for (Pedidocompraproduto pcp : pedidocompra.getListapedidocompraproduto()) {
						Produtoclasse produtoclasse = produtoclasseService.loadWithV_produtoclasse(pcp.getProduto().getProdutoclasse());
						if(produtoclasse==null)
							throw new WmsException("O produto <"+pcp.getProduto().getDescriptionProperty()+"> está cadastrado com uma classe inválida.");
						String numeroClassePrincipal = produtoclasse.getV_produtoclasse().getProdutoclasse().getNumero();
					
					Money verba;
					if(!mapRecebimento.containsKey(numeroClassePrincipal)){
						verba = agendaverbaService.findVerbaRecebimento(deposito, pcp.getProduto().getProdutoclasse(), filtro.getDtprevisaorecebimento());
						mapRecebimento.put(numeroClassePrincipal, verba);
					}
					if(!mapFinanceiro.containsKey(numeroClassePrincipal)){
						verba = agendaverbafinanceiroService.findVerbaFinanceiro(deposito, pcp.getProduto().getProdutoclasse(),filtro.getDtprevisaorecebimento(), filtro.getDtprevisaovencimentofinanceiro());
						mapFinanceiro.put(numeroClassePrincipal, verba);
					}
					
					int qtdeLiberada;
					if (listaPedidoParcial.contains(pedidocompra.getCdpedidocompra().toString())){
						String qtdeLiberadaStr = request.getParameter("qqq" + pcp.getCdpedidocompraproduto());
						qtdeLiberada = Integer.parseInt(qtdeLiberadaStr);
					}else{
						qtdeLiberada = pcp.getQtde();
					}
					
					if(qtdeLiberada == 0) continue;
					
					verba = pcp.getValor().multiply(new Money(qtdeLiberada, false));
					
					if(mapLiberado.containsKey(numeroClassePrincipal)){
						Money valor = mapLiberado.get(numeroClassePrincipal);
						valor = valor.add(verba);
						mapLiberado.put(numeroClassePrincipal, valor);
					} else {
						//Money valorJaLiberado = pedidocompraprodutoService.getValorLiberado(filtro.getDeposito(), "10", filtro.getDtprevisaorecebimento());
						//verba = verba.add(valorJaLiberado);
						mapLiberado.put(numeroClassePrincipal, verba);
					}
					
					Money valorJaLiberado = pedidocompraprodutoService.getValorLiberado(deposito, numeroClassePrincipal, filtro.getDtprevisaorecebimento());
					Money valorRecebimento = new Money();
					if(mapLiberadoRecebimento.containsKey(numeroClassePrincipal)){
						valorRecebimento = mapLiberadoRecebimento.get(numeroClassePrincipal);
						mapLiberadoRecebimento.put(numeroClassePrincipal, valorRecebimento);
					}else{
						mapLiberadoRecebimento.put(numeroClassePrincipal, valorJaLiberado == null ? new Money(0) : valorJaLiberado);
					}
					
					Money valorJaLiberadoFinanceiro = pedidocompraprodutoService.getValorLiberadoFinanceiro(deposito, numeroClassePrincipal, filtro.getDtprevisaorecebimento(), filtro.getDtprevisaovencimentofinanceiro());
					Money valorFinanceiro = new Money();
					if(mapLiberadoFinanceiro.containsKey(numeroClassePrincipal)){
						valorFinanceiro = mapLiberadoFinanceiro.get(numeroClassePrincipal);
						mapLiberadoFinanceiro.put(numeroClassePrincipal, valorFinanceiro);
					}else{
						mapLiberadoFinanceiro.put(numeroClassePrincipal, valorJaLiberadoFinanceiro == null ? new Money(0) : valorJaLiberadoFinanceiro);
					}
					
				}
			}
		}
		
		List<String> listaClasses = new ArrayList<String>();
		Set<Entry<String, Money>> es = mapLiberado.entrySet();
		for (Entry<String, Money> entry : es) {
			listaClasses.add(entry.getKey());
		}
		
		List<ResumoAgendaverba> listaResumoAgenda = agendaService.findResumoAgenda(deposito, WmsUtil.firstDateOfMonth(filtro.getDtprevisaorecebimento()), WmsUtil.lastDateOfMonth(filtro.getDtprevisaorecebimento()), false, listaClasses, null);
		Map<String, Money> mapRecebimentoAgendado = new HashMap<String, Money>();
		for (ResumoAgendaverba resumo : listaResumoAgenda){
			mapRecebimentoAgendado.put(resumo.getClasseproduto(), resumo.getValor());
		}

		List<ResumoAgendaverba> listaResumoFinanceiro = agendaService.findResumoAgenda(deposito, WmsUtil.firstDateOfMonth(filtro.getDtprevisaovencimentofinanceiro()), WmsUtil.lastDateOfMonth(filtro.getDtprevisaovencimentofinanceiro()), true, listaClasses, null);
		Map<String, Money> mapFinanceiroAgendado = new HashMap<String, Money>();
		for (ResumoAgendaverba resumo : listaResumoFinanceiro){
			mapFinanceiroAgendado.put(resumo.getClasseproduto(), resumo.getValor());
		}
		
		String classe;
		Produtoclasse produtoclasse;
		Money agendadoAgora, agendadoRecebimento =null, permitidoRecebimento , agendadoFinanceiro =null, permitidoFinanceiro;
		
		List<String> listaErros = new ArrayList<String>();
		
		
		
		for (Entry<String, Money> entry : es) {
			classe = entry.getKey();
			produtoclasse = null;
			agendadoAgora = entry.getValue();
			
			if(agendadoRecebimento == null) agendadoRecebimento = new Money();
			agendadoRecebimento = mapLiberadoRecebimento.get(classe);
		//	agendadoRecebimento = mapRecebimentoAgendado.get(classe);
		//	agendadoRecebimento = agendadoRecebimento.add(mapRecebimentoAgendado.get(classe) == null ? new Money(0) : mapRecebimentoAgendado.get(classe) );
			agendadoRecebimento = agendadoRecebimento.add(agendadoAgora);
			
			if(agendadoFinanceiro == null) agendadoFinanceiro = new Money();
			agendadoFinanceiro = mapLiberadoFinanceiro.get(classe) == null ? new Money(0) : mapLiberadoFinanceiro.get(classe);
			agendadoFinanceiro = agendadoFinanceiro.add(mapFinanceiroAgendado.get(classe) == null ? new Money(0) : mapFinanceiroAgendado.get(classe) );
			agendadoFinanceiro = agendadoFinanceiro.add(agendadoAgora);
			
			permitidoRecebimento = mapRecebimento.get(classe);
			permitidoFinanceiro = mapFinanceiro.get(classe);
			
			if (ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.VALIDACAO_VERBA_LIBERACAO_RECEBIMENTO, null)){
				if(permitidoRecebimento == null || permitidoRecebimento.getValue().doubleValue() < agendadoRecebimento.getValue().doubleValue()){
					if(produtoclasse == null) 
						produtoclasse = produtoclasseService.findByNumero(classe);
					listaErros.add("A liberação não pode ser feita porque estoura a verba de recebimento para a classe de produto " + produtoclasse.getNome() + " no período em R$ " + new Money(agendadoRecebimento.getValue().doubleValue() - permitidoRecebimento.getValue().doubleValue()) + ".");
				}
			}
			
			if (ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.VALIDACAO_VERBA_LIBERACAO_FINANCEIRA, null)){
				if(permitidoFinanceiro == null || permitidoFinanceiro.getValue().doubleValue() < agendadoFinanceiro.getValue().doubleValue()){
					if(produtoclasse == null) 
						produtoclasse = produtoclasseService.findByNumero(classe);
					listaErros.add("A liberação não pode ser feita porque estoura a verba financeira para a classe de produto " + produtoclasse.getNome() + " no período em R$ " + new Money(agendadoFinanceiro.getValue().doubleValue() - permitidoFinanceiro.getValue().doubleValue()) + ".");
				}
			}
		}
		return listaErros;
	}
	
	public Pedidocompra findByCodigoERP(Long codigoERP){
		return pedidocompraDAO.findByCodigoERP(codigoERP);
	}
	
	/* singleton */
	private static PedidocompraService instance;
	public static PedidocompraService getInstance() {
		if(instance == null){
			instance = Neo.getObject(PedidocompraService.class);
		}
		
		return instance;
	}
	
	public void setValoresPedicompra(List<Pedidocompra> listaPedidocompra){
		Map<Pedidocompra, List<Produto>> mapa = new HashMap<Pedidocompra, List<Produto>>();
		if(listaPedidocompra != null && listaPedidocompra.size() > 0){
			for (Pedidocompra pedidocompra : listaPedidocompra) {
				List<Pedidocompraproduto> listaPedidocompraproduto = pedidocompraprodutoService.findByPedidoCompra(pedidocompra);
				for (Pedidocompraproduto pedidocompraproduto : listaPedidocompraproduto) {
					List<Produto> listaProduto = new ArrayList<Produto>();
					if(mapa.containsKey(pedidocompra)){
						listaProduto = mapa.get(pedidocompra);
						listaProduto.add(pedidocompraproduto.getProduto());
					}else{
						listaProduto.add(pedidocompraproduto.getProduto());
					}
					mapa.put(pedidocompraproduto.getPedidocompra(), listaProduto);
				}
			}	
				
			for (Pedidocompra item : listaPedidocompra) {
				item.setQtdetotal((int) pedidocompraprodutoService.getQtdeTotal(item, WmsUtil.concatenateWithLimit(mapa.get(item), "cdproduto", mapa.get(item).size())));
				item.setQtdeliberada((int) pedidocompraprodutoService.getQtdeLiberada(item, WmsUtil.concatenateWithLimit(mapa.get(item), "cdproduto", mapa.get(item).size())));
				item.setQtdeagenda(this.getQtdeAgendada(item));
				item.setQtdedisponivel(item.getQtdeliberada() - item.getQtdeagenda());
			}
		}
	}
	
	public Integer getQtdeAgendada(Pedidocompra pedidocompra) {
		return pedidocompraDAO.getQtdeAgendada(pedidocompra);
	}
		
}
