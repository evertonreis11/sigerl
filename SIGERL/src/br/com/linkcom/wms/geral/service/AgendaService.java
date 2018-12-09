package br.com.linkcom.wms.geral.service;

import java.awt.Image;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.report.ReportGenerator;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.util.NeoFormater;
import br.com.linkcom.neo.util.NeoImageResolver;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Agendajanela;
import br.com.linkcom.wms.geral.bean.Agendajanelaclasse;
import br.com.linkcom.wms.geral.bean.Agendaparcial;
import br.com.linkcom.wms.geral.bean.Agendapedido;
import br.com.linkcom.wms.geral.bean.Agendastatus;
import br.com.linkcom.wms.geral.bean.Configuracao;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Fornecedor;
import br.com.linkcom.wms.geral.bean.Notafiscalentrada;
import br.com.linkcom.wms.geral.bean.Pedidocompra;
import br.com.linkcom.wms.geral.bean.Pedidocompraproduto;
import br.com.linkcom.wms.geral.bean.Produtoclasse;
import br.com.linkcom.wms.geral.bean.vo.AcompanhamentoAgendaVO;
import br.com.linkcom.wms.geral.bean.vo.ResumoAgendaverba;
import br.com.linkcom.wms.geral.dao.AgendaDAO;
import br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro.AgendajanelaFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.report.filtro.EmitiracompanhamentoagendamentoFiltro;
import br.com.linkcom.wms.modulo.sistema.controller.process.filtro.AgendamentoFiltro;
import br.com.linkcom.wms.util.EmailManager;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;
import br.com.linkcom.wms.util.recebimento.AgendaVO;

public class AgendaService extends GenericService<Agenda> {
	
	private PedidocompraprodutoService pedidocompraprodutoService;
	private AgendapedidoService agendapedidoService;
	private AgendaparcialService agendaparcialService;
	private AgendaDAO agendaDAO;
	@SuppressWarnings("unused")
	private NeoImageResolver neoImageResolver;
	private PedidocompraService pedidocompraService;
	private AgendaverbaService agendaverbaService;
	private AgendaverbafinanceiroService agendaverbafinanceiroService;
	private ProdutoclasseService produtoclasseService;
	private AgendajanelaService agendajanelaService;
	private RecebimentoService recebimentoService;

	public void setAgendapedidoService(AgendapedidoService agendapedidoService) {
		this.agendapedidoService = agendapedidoService;
	}
	
	public void setPedidocompraprodutoService(PedidocompraprodutoService pedidocompraprodutoService) {
		this.pedidocompraprodutoService = pedidocompraprodutoService;
	}
	
	public void setAgendaparcialService(AgendaparcialService agendaparcialService) {
		this.agendaparcialService = agendaparcialService;
	}
	
	public void setAgendaDAO(AgendaDAO agendaDAO) {
		this.agendaDAO = agendaDAO;
	}

	public void setNeoImageResolver(NeoImageResolver neoImageResolver) {
		this.neoImageResolver = neoImageResolver;
	}
	
	public void setPedidocompraService(PedidocompraService pedidocompraService) {
		this.pedidocompraService = pedidocompraService;
	}

	public void setAgendaverbaService(AgendaverbaService agendaverbaService) {
		this.agendaverbaService = agendaverbaService;
	}

	public void setAgendaverbafinanceiroService(
			AgendaverbafinanceiroService agendaverbafinanceiroService) {
		this.agendaverbafinanceiroService = agendaverbafinanceiroService;
	}

	public void setProdutoclasseService(ProdutoclasseService produtoclasseService) {
		this.produtoclasseService = produtoclasseService;
	}
	
	public void setAgendajanelaService(AgendajanelaService agendajanelaService) {
		this.agendajanelaService = agendajanelaService;
	}
	
	public void setRecebimentoService(RecebimentoService recebimentoService) {
		this.recebimentoService = recebimentoService;
	}
	
	/**
	 * Cria uma string com os pedido do agendamento e seta na agenda
	 * @author Leonardo Guimarães
	 * @param agenda
	 */
	public void preparePedidos(Agenda agenda) {
		if(agenda == null){
			throw new WmsException("A agenda não deve ser nulo");
		}
		
		List<Agendapedido> listaAgendaPedido = agendapedidoService.findByAgenda(agenda);
		agenda.setListaagendaparcial(new ListSet<Agendaparcial>(Agendaparcial.class,agendaparcialService.findByAgenda(agenda)));
//		if(agenda.getListaagendaparcial() != null && !agenda.getListaagendaparcial().isEmpty()){
//			for(Agendaparcial agendaparcial : agenda.getListaagendaparcial()){
//				agenda.setTotalAgendado(agendaparcial.getQtde() + agenda.getTotalAgendado());
//			}
//		} 
//		else{
//			if(listaAgendaPedido != null && !listaAgendaPedido.isEmpty()){
//				for(Agendapedido agendapedido : listaAgendaPedido){
//					if (!agendapedido.getParcial()){
////						agenda.setTotalAgendado(0);
//						agendapedido.getPedidocompra().setListapedidocompraproduto(new ListSet<Pedidocompraproduto>(Pedidocompraproduto.class,pedidocompraprodutoService.findByPedidoCompra(agendapedido.getPedidocompra())));
//						for(Pedidocompraproduto pedidocompraproduto : agendapedido.getPedidocompra().getListapedidocompraproduto()){
//							agenda.setTotalAgendado(pedidocompraproduto.getQtde() + agenda.getTotalAgendado());
//						}
//					}
//				}
//			}
//		}
		agenda.setTotalAgendado(agendaDAO.getTotalAgendado(agenda));
		Collections.sort(listaAgendaPedido,new Comparator<Agendapedido>(){
			public int compare(Agendapedido o1, Agendapedido o2) {
				return o1.getPedidocompra().getCodigoerp().compareTo(o2.getPedidocompra().getCodigoerp());
			}
		});
		agenda.setListaagendapedido(new ListSet<Agendapedido>(Agendapedido.class,listaAgendaPedido));
		
		if (agenda.getListaagendapedido() != null && !agenda.getListaagendapedido().isEmpty()){
			agenda.setPedidos(WmsUtil.concatenateWithLimit(agenda.getListaagendapedido(), "pedidocompra.numero", 5));
			agenda.setFornecedor(agenda.getListaagendapedido().iterator().next().getPedidocompra().getFornecedor());
		}
		if(agenda.getListanotafiscalentrada() != null && !agenda.getListanotafiscalentrada().isEmpty()){
			for (Notafiscalentrada nfe : agenda.getListanotafiscalentrada()) {
				if(nfe.getListaRecebimentonotafiscalentrada() != null && !nfe.getListaRecebimentonotafiscalentrada().isEmpty()){
					agenda.setRecebimentos(WmsUtil.concatenateWithLimit(nfe.getListaRecebimentonotafiscalentrada(), "recebimento.cdrecebimento", 5));
				}
				
			}
		}
	}
	
	/**
	 * Método que ordena a lista de acordo com o valor do
	 * campo total agendado
	 * @author Leonardo Guimarães
	 * @param listaAgenda - Lista que sera ordenada
	 */
	public void ordenaByTotalAgendado(List<Agenda> listaAgenda, boolean isASC) {
		Comparator<Agenda> asc = new Comparator<Agenda>(){
			 public int compare(Agenda o1, Agenda o2) {
				 if(o1.getTotalAgendado() > o2.getTotalAgendado())
					 return 1;
				 else if(o1.getTotalAgendado() < o2.getTotalAgendado())
					 return -1;
					 else 
						 return 0;
			}
		};
		Comparator<Agenda> dec = new Comparator<Agenda>(){
			public int compare(Agenda o1, Agenda o2) {
				if(o1.getTotalAgendado() > o2.getTotalAgendado())
					return -1;
				else if(o1.getTotalAgendado() < o2.getTotalAgendado())
					return 1;
				else
					return 0;
			}
		};
		
		if(isASC)
			Collections.sort(listaAgenda,asc);
		else Collections.sort(listaAgenda,dec);
	}
	
	/**
	 * 
	 * Método que constrói o relatório dos agendamentos dos pedidos dos produtos.
	 * 
	 * @see br.com.linkcom.wms.geral.service.AgendaService#findForReportAgenda(AgendamentoFiltro)
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @param agendamentos 
	 * @return IReport
	 * 
	 */
	public IReport generateReportAgenda(AgendamentoFiltro filtro, String agendamentos) {
		
		Report report = new Report("RelatorioAgendamento");
		
		List<AgendaVO> listaAgenda = this.findForReportAgenda(filtro,agendamentos);
		
		Image image = null;
		
		try {
			image = neoImageResolver.getImage(WmsUtil.getLogoForReport());
		
		} catch (IOException e) {
			throw new WmsException("Erro ao capturar o logotipo do relatório");
		
		}
		
		report.addParameter("LOGO", image);
		report.setDataSource(listaAgenda);
		
		return report;		
	}
	
	/**
	 * Método de referência ao DAO.
	 * Método que recupera os produtos dos pedidos que foram agendados.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.AgendaDAO#findForReportAgenda(AgendamentoFiltro)
	 * 
	 * @author Arantes 
	 * 
	 * @param filtro
	 * @param agendamentos 
	 * @return List<AgendaVO>
	 * 
	 */
	public List<AgendaVO> findForReportAgenda(AgendamentoFiltro filtro, String agendamentos) {
		return agendaDAO.findForReportAgenda(filtro,agendamentos);
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * Método que atualiza o status do agendamento
	 * 
	 * @see br.com.linkcom.wms.geral.dao.AgendaDAO#atualizaStatusAgendamento(Agenda)
	 * 
	 * @author Arantes
	 * 
	 * @param agenda
	 * 
	 */
	public void atualizaStatusAgendamento(Agenda agenda) {
		agendaDAO.atualizaStatusAgendamento(agenda);
	}
	
	/**
	 * Atualiza o agendamento com o status passado por parâmetro.
	 *
	 * @param agenda
	 * @param agendastatus
	 * @author Rodrigo Freitas
	 */
	public void atualizaStatusAgendamento(Agenda agenda, Agendastatus agendastatus) {
		agenda.setAgendastatus(agendastatus);
		agendaDAO.atualizaStatusAgendamento(agenda);
	}
	
	/**
	 * Busca os totais agendados por mês dentro de um semestre.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @param filtrarPorVencimentoFinanceiro 
	 * @param classes 
	 * @return
	 */
	public List<ResumoAgendaverba> findResumoAgenda(Deposito deposito, Date dtinicio, Date dtfim, boolean filtrarPorVencimentoFinanceiro, List<String> classes, Agenda agenda){
		return agendaDAO.findResumoAgenda(deposito, dtinicio, dtfim, filtrarPorVencimentoFinanceiro, classes, agenda);
	}
	public List<ResumoAgendaverba> findResumoAgendaFinanceiro2(Deposito deposito, Date dtinicio, Date dtfim, List<String> classes){
		return agendaDAO.findResumoAgendaFinanceiro2(deposito, dtinicio, dtfim, classes);
	}

	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.AgendaDAO#existsDepositoAgendado
	 *
	 * @param agenda
	 * @return
	 * @author Rodrigo Freitas
	 */
	public boolean existsDepositoAgendado(Agenda agenda) {
		return agendaDAO.existsDepositoAgendado(agenda);
	}

	/**
	 * Envia um email para cada fornecedor informando sobre o agendamento realizado.
	 * 
	 * @author Giovane Freitas
	 * @param agenda
	 */
	@SuppressWarnings("unchecked")
	public List<String> enviarEmailFornecedor(Agenda agenda) {
		AgendamentoFiltro filtro = new AgendamentoFiltro();
		filtro.setDeposito(agenda.getDeposito());
		filtro.setCdagenda(agenda.getCdagenda());
		filtro.setCdagendamento(agenda.getCdagenda());
		
		Map<Fornecedor, List<AgendaVO>> agendaPorFornecedor = new HashMap<Fornecedor, List<AgendaVO>>();

		List<AgendaVO> dadosAgendamento = this.findForReportAgenda(filtro, null);
		
		//Agrupando os itens por fornecedor
		Collections.sort(dadosAgendamento, new BeanComparator("cdfornecedor"));
		Fornecedor fornecedor = null;
		for (AgendaVO item : dadosAgendamento){
			if (fornecedor == null || !fornecedor.getCdpessoa().equals(item.getCdfornecedor())){
				fornecedor = new Fornecedor();
				fornecedor.setCdpessoa(item.getCdfornecedor());
				fornecedor.setNome(item.getFornecedor());
				fornecedor.setEmail(item.getEmailFornecedor());
				
				List<AgendaVO> itens = new ArrayList<AgendaVO>();
				itens.add(item);
				
				agendaPorFornecedor.put(fornecedor, itens);
			} else {
				agendaPorFornecedor.get(fornecedor).add(item);
			}
		}

		List<String> erros = new ArrayList<String>();

		for (Fornecedor fornecedorAux : agendaPorFornecedor.keySet()){
			if (fornecedorAux.getEmail() == null || fornecedorAux.getEmail().trim().isEmpty()){
				erros.add("O fornecedor " + fornecedorAux.getNome() + " não possui e-mail cadastrado.");
			}else{
				try {
					enviarEmail(fornecedorAux, agendaPorFornecedor.get(fornecedorAux));
					System.out.println("E-mail enviado com sucesso ao Fornecedor! AGENDAMENTO= "+agenda.getCdagenda());
				} catch (Exception e) {
					e.printStackTrace();
					erros.add("Ocorreu um erro ao enviar o e-mail para o fornecedor " + fornecedorAux.getNome());
				}
			}
		}
		
		return erros;
	}

	/**
	 * Rotina que faz o envio de email para o fornecedor.
	 * @param fornecedorAux
	 * @param list
	 * @throws Exception 
	 */
	private void enviarEmail(Fornecedor fornecedor, List<AgendaVO> list) throws Exception {
		EmailManager emailManager = new EmailManager();
		emailManager.setFrom(ConfiguracaoService.getInstance().getConfigValue(ConfiguracaoVO.REMETENTE_EMAIL_WMS, null));
		emailManager.setTo(fornecedor.getEmail());
		emailManager.setSubject("Agendamento de pedido de compra");
		
        StringBuilder conteudo = new StringBuilder();
        conteudo.append("<p><b>Agendamento de pedido de compra.</b></p>");
        conteudo.append("<p>Segue em anexo a confirmação de agendamento de pedido de compra.</p>");
        emailManager.addHtmlText( conteudo.toString() );
        
        
        //Gerando o PDF
        ReportGenerator reportGenerator = Neo.getApplicationContext().getReportGenerator();
        Report report = new Report("RelatorioConfirmacaoAgendamentoFornecedor");
		Image image = null;
		try {
			image = neoImageResolver.getImage(WmsUtil.getLogoForReport());
		} catch (IOException e) {
			e.printStackTrace();
		}
		report.addParameter("LOGO", image);
		report.addParameter("NEOFORMATER", NeoFormater.getInstance());
		report.addParameter("TITULO", "Confirmação de agendamento");
		report.addParameter("DATA",new Date(System.currentTimeMillis()));
		report.addParameter("HORA", System.currentTimeMillis());
		report.addParameter("USUARIO", WmsUtil.getUsuarioLogado().getNome());
		report.addParameter("EMPRESA_WMS", Util.locale.getBundleKey("aplicacao.titulo"));
		report.addParameter("RODAPE", "Gerado em " + NeoFormater.getInstance().format(new Timestamp(System.currentTimeMillis())) + " por " + WmsUtil.getUsuarioLogado().getNome());
		report.addParameter("FORNECEDOR", fornecedor);
		report.setDataSource(list);

        emailManager.attachFileUsingByteArray(reportGenerator.toPdf(report), "confirmacao_agendamento.pdf", "application/pdf", "confirmacao_pdf");
		
		emailManager.sendMessage();
	}
	
	/* singleton */
	private static AgendaService instance;

	public static AgendaService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(AgendaService.class);
		}
		return instance;
	}

	public void cancelaAgendamentosAntigos() {
		Configuracao config = ConfiguracaoService.getInstance().findByName(null, ConfiguracaoVO.NUM_DIAS_AGENDA_CANCELAMENTO);
		if(config != null){
			try{
				Integer num = Integer.parseInt(config.getValor());
				num = num * (-1);
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.add(Calendar.DAY_OF_MONTH, num);
				
				List<Agenda> listaAgendamentos = this.findForCancelamento(new java.sql.Date(calendar.getTimeInMillis()));
				
				for (Agenda agenda : listaAgendamentos) {
					this.atualizaStatusAgendamento(agenda, Agendastatus.CANCELADO);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.AgendaDAO#findForCancelamento
	 *
	 * @param date
	 * @return
	 * @author Rodrigo Freitas
	 */
	private List<Agenda> findForCancelamento(java.sql.Date date) {
		return agendaDAO.findForCancelamento(date);
	}
	
	/**
	 * Método que faz a validação das verbas financeiras e de recebimento no agendamento.
	 * 
	 * @see br.com.linkcom.wms.geral.service.PedidocompraService#loadForValidacaoAgendamento
	 * @see br.com.linkcom.wms.geral.service.AgendaverbaService#findVerbaRecebimento
	 * @see br.com.linkcom.wms.geral.service.AgendaverbafinanceiroService#findVerbaFinanceiro
	 * @see br.com.linkcom.wms.geral.service.AgendaService#findClassesAgendadas
	 * @see br.com.linkcom.wms.geral.service.ProdutoclasseService#findByNumero
	 *
	 * @param agenda
	 * @return
	 * @author Rodrigo Freitas
	 */
	/**
	 * @param agenda
	 * @return
	 */
	public List<String> validacaoAgendamento(Agenda agenda) {
		List<String> listaErros = new ArrayList<String>();

		Set<Agendaparcial> listaagendaparcial = agenda.getListaagendaparcial();
		Set<Agendapedido> listaagendapedido = agenda.getListaagendapedido();
		
		//Coletando as classes de produto presente na agenda
		List<String> listaProdutoclasse = new ArrayList<String>();
		for (Agendapedido ap : listaagendapedido) {
			if(ap.getParcial() != null && ap.getParcial() && listaagendaparcial != null && listaagendaparcial.size() > 0){
				for (Agendaparcial par : listaagendaparcial) {
					String numeroClasse = par.getPedidocompraproduto().getProduto().getProdutoclasse().getV_produtoclasse().getProdutoclasse().getNumero();
					//metodo para carregar o numero do produto classe quando ele estiver nulo. nâo é o ideal, o ideal seria já vir carregado da lista
					//Se possivel, refatorar depois.
					if(numeroClasse==null) numeroClasse = produtoclasseService.loadWithV_produtoclasse(par.getPedidocompraproduto().getProduto().getProdutoclasse()).getV_produtoclasse().getProdutoclasse().getNumero();
					if (!listaProdutoclasse.contains(numeroClasse)){
						listaProdutoclasse.add(numeroClasse);
					}
				}
			} else {
				if (ap.getPedidocompra().getListapedidocompraproduto() == null || ap.getPedidocompra().getListapedidocompraproduto().isEmpty())
					ap.getPedidocompra().setListapedidocompraproduto(pedidocompraprodutoService.findByPedidoCompra(ap.getPedidocompra()));
				
				for (Pedidocompraproduto pcp : ap.getPedidocompra().getListapedidocompraproduto()){
					String numeroClasse = pcp.getProduto().getProdutoclasse().getV_produtoclasse().getProdutoclasse().getNumero();
					if(numeroClasse==null) numeroClasse = produtoclasseService.loadWithV_produtoclasse(pcp.getProduto().getProdutoclasse()).getV_produtoclasse().getProdutoclasse().getNumero();
					
					if (!listaProdutoclasse.contains(numeroClasse)){
						listaProdutoclasse.add(numeroClasse);
					}
				}
			}
		}

		AgendajanelaFiltro filtro = new AgendajanelaFiltro();
		filtro.setDeposito(agenda.getDeposito());
		filtro.setDataAgenda(agenda.getDtagenda());
		List<Agendajanela> listaJanelas = agendajanelaService.findForValidacao(filtro, agenda );
		for (Agendajanela aj : listaJanelas){
			if (aj.getDisponivel() > 0){
				for (Agendajanelaclasse ajc : aj.getListaAgendajanelaclasse())
					listaProdutoclasse.remove(ajc.getProdutoclasse().getNumero());
			}
		}
		
		if (!listaProdutoclasse.isEmpty()){
			Map<String, Produtoclasse> classes = new HashMap<String, Produtoclasse>();
			List<Produtoclasse> listaTodasClasses = produtoclasseService.findForAgendaverba();
			for (Produtoclasse pc : listaTodasClasses)
				classes.put(pc.getNumero(), pc);
			
			for (String classe : listaProdutoclasse)
				listaErros.add("Não existe uma janela de agendamento para a classe de produto " + classes.get(classe).getNome() + ".");
		}
		
		/*
		if (!ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.VALIDAR_VERBA_AGENDAMENTO, null)){
			agenda.setValidado(false);
			return listaErros;
		}*/
		
		agenda.setValidado(true);
		
		Map<String, Money> mapRecebimento = new HashMap<String, Money>();
		Map<String, Money> mapFinanceiro = new HashMap<String, Money>();
		Map<String, Money> mapAgendado = new HashMap<String, Money>();
		
		Money verba, valor;
		
		Deposito deposito;
		if (!ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.VALIDACAO_VERBA_DEPOSITO, null)){
			deposito = null;
		}else{
			deposito = filtro.getDeposito();
		}
		
		for (Agendapedido ap : listaagendapedido) {
			Pedidocompra pc = ap.getPedidocompra();
			if(pc != null && pc.getCdpedidocompra() != null){
				pc = pedidocompraService.loadForValidacaoAgendamento(pc);
				if(pc.getListapedidocompraproduto() != null){
					List<Pedidocompraproduto> listapedidocompraproduto = pc.getListapedidocompraproduto();
					for (Pedidocompraproduto pcp : listapedidocompraproduto) {
						String numeroClasse = produtoclasseService.loadWithV_produtoclasse(pcp.getProduto().getProdutoclasse()).getV_produtoclasse().getProdutoclasse().getNumero();
						
						if(!mapRecebimento.containsKey(numeroClasse)){
							verba = agendaverbaService.findVerbaRecebimento(deposito, pcp.getProduto().getProdutoclasse(), agenda.getDtagenda());
							mapRecebimento.put(numeroClasse, verba);
						}
						if(!mapFinanceiro.containsKey(numeroClasse)){
							verba = agendaverbafinanceiroService.findVerbaFinanceiro(deposito, pcp.getProduto().getProdutoclasse(), agenda.getDtagenda(), agenda.getDtprevisao());
							mapFinanceiro.put(numeroClasse, verba);
						}
						
						
						if(ap.getParcial() != null && ap.getParcial() && listaagendaparcial != null && listaagendaparcial.size() > 0){
							verba = null;
							for (Agendaparcial par : listaagendaparcial) {
								if(par.getPedidocompraproduto().equals(pcp)){
									verba = pcp.getValor().multiply(new Money(par.getQtde(), false));
									break;
								}
							}
							
							if(verba != null){
								if(mapAgendado.containsKey(numeroClasse)){
									valor = mapAgendado.get(numeroClasse);
									valor = valor.add(verba);
									mapAgendado.put(numeroClasse, valor);
								} else {
									mapAgendado.put(numeroClasse, verba);
								}
							}
							
						} else {
							verba = pcp.getValor().multiply(new Money(pcp.getQtdeliberada() != null ? pcp.getQtdeliberada() : 0, false));
							
							if(mapAgendado.containsKey(numeroClasse)){
								valor = mapAgendado.get(numeroClasse);
								valor = valor.add(verba);
								mapAgendado.put(numeroClasse, valor);
							} else {
								mapAgendado.put(numeroClasse, verba);
							}
						}
					}
				}
			}
		}
		
		List<String> listaClasses = new ArrayList<String>();
		Set<Entry<String, Money>> es = mapAgendado.entrySet();
		for (Entry<String, Money> entry : es) {
			listaClasses.add(entry.getKey());
		}
		
		List<ResumoAgendaverba> listaResumoRecebido = recebimentoService.findResumoRecebimentoVerbaNovo(deposito, agenda.getDtagenda());
		Map<String, Money> mapRecebido = new HashMap<String, Money>();
		for (ResumoAgendaverba resumo : listaResumoRecebido){
			mapRecebido.put(resumo.getClasseproduto(), resumo.getValor());
		}

		List<ResumoAgendaverba> listaResumoRecebidoFinanceiro = recebimentoService.findResumoRecebimentoFinanceiroNovo(deposito, agenda.getDtagenda(), agenda.getDtprevisao());
		Map<String, Money> mapRecebidoFinanceiro = new HashMap<String, Money>();
		for (ResumoAgendaverba resumo : listaResumoRecebidoFinanceiro){
			mapRecebidoFinanceiro.put(resumo.getClasseproduto(), resumo.getValor());
		}
		
		List<ResumoAgendaverba> listaResumoAgenda = this.findResumoAgenda(deposito, WmsUtil.firstDateOfMonth(agenda.getDtagenda()), WmsUtil.lastDateOfMonth(agenda.getDtagenda()), false, listaClasses, agenda);
		Map<String, Money> mapRecebimentoAgendado = new HashMap<String, Money>();
		for (ResumoAgendaverba resumo : listaResumoAgenda){
			mapRecebimentoAgendado.put(resumo.getClasseproduto(), resumo.getValor());
		}
		
		List<ResumoAgendaverba> listaResumoFinanceiro = this.findResumoAgendaFinanceiro(deposito, agenda.getDtagenda(), agenda.getDtprevisao(),listaClasses, agenda);
		Map<String, Money> mapFinanceiroAgendado = new HashMap<String, Money>();
		for (ResumoAgendaverba resumo : listaResumoFinanceiro){
			mapFinanceiroAgendado.put(resumo.getClasseproduto(), resumo.getValor());
		}
		
		String classe;
		Produtoclasse produtoclasse;
		Money agendadoAgora, agendadoRecebimento, agendadoFinanceiro, permitidoRecebimento, permitidoFinanceiro, recebido, recebidoFinanceiro;
		
		for (Entry<String, Money> entry : es) {
			classe = entry.getKey();
			produtoclasse = null;
			agendadoAgora = entry.getValue();
			
			recebido = mapRecebido.get(classe);
			if(recebido == null) recebido = new Money(0);
			
			recebidoFinanceiro = mapRecebidoFinanceiro.get(classe);
			if(recebidoFinanceiro == null) recebidoFinanceiro = new Money(0);
			
			agendadoRecebimento = mapRecebimentoAgendado.get(classe);
			if(agendadoRecebimento == null) agendadoRecebimento = new Money();
			agendadoRecebimento = agendadoRecebimento.add(agendadoAgora).add(recebido);
			
			agendadoFinanceiro = mapFinanceiroAgendado.get(classe);
			if(agendadoFinanceiro == null) agendadoFinanceiro = new Money();
			agendadoFinanceiro = agendadoFinanceiro.add(agendadoAgora).add(recebidoFinanceiro);
			
			permitidoRecebimento = mapRecebimento.get(classe);
			permitidoFinanceiro = mapFinanceiro.get(classe);
			
			if (ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.VALIDACAO_VERBA_AGENDAMENTO_RECEBIMENTO, null)){
				if(permitidoRecebimento == null || permitidoRecebimento.getValue().doubleValue() < agendadoRecebimento.getValue().doubleValue()){
					if(produtoclasse == null) produtoclasse = produtoclasseService.findByNumero(classe);
					listaErros.add("O agendamento não pode ser gerado porque estoura a verba de recebimento para a classe de produto " + produtoclasse.getNome() + " no período.");
				}
			}
			
			if (ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.VALIDACAO_VERBA_AGENDAMENTO_FINANCEIRA, null)){
				if(permitidoFinanceiro == null || permitidoFinanceiro.getValue().doubleValue() < agendadoFinanceiro.getValue().doubleValue()){
					if(produtoclasse == null) produtoclasse = produtoclasseService.findByNumero(classe);
					listaErros.add("O agendamento não pode ser gerado porque estoura a verba financeira para a classe de produto " + produtoclasse.getNome() + " no período.");
				}
			}
		}
		return listaErros;
	}

	/**
	 * Busca os dados para o relatório de acompanhamento de agendamento.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public List<AcompanhamentoAgendaVO> findForAcompanhamento(EmitiracompanhamentoagendamentoFiltro filtro) {
		return agendaDAO.findForAcompanhamento(filtro);
	}
	
	public List<Agenda> recebimentosByAgenda(String whereIn) {
		return agendaDAO.recebimentosByAgenda(whereIn);
	}
	
	public void inserirRecebimentos(List<Agenda> listaAgenda){
		String  whereIn = "";
		
		if(listaAgenda != null && listaAgenda.size() > 0){
			whereIn = WmsUtil.concatenateWithLimit(listaAgenda, "cdagenda", listaAgenda.size());
			if(whereIn != ""){
				List<Agenda> listaAgendaRecebimentos = this.recebimentosByAgenda(whereIn.toString());
				if(listaAgendaRecebimentos != null && listaAgendaRecebimentos.size() > 0){
					for (Agenda agenda : listaAgenda) {
						for (Agenda agendaRecebimento : listaAgendaRecebimentos) {
							int i = 0;
							if(agenda.equals(agendaRecebimento)){
								if(agenda.getRecebimentos() != null && agenda.getRecebimentos() != "")
									agenda.setRecebimentos(agendaRecebimento.getRecebimentos() != null ? agenda.getRecebimentos() + ", " + agendaRecebimento.getRecebimentos() : "");
								else
									agenda.setRecebimentos(agendaRecebimento.getRecebimentos() != null ? agendaRecebimento.getRecebimentos() : "");
								i++;
							}
						}
					}
				}
			}
		}
		
	}
	
	
	public List<ResumoAgendaverba> findResumoAgendaFinanceiro(Deposito deposito, Date dtagenda, Date dtprevisao, List<String> classes, Agenda agenda){
		return agendaDAO.findResumoAgendaFinanceiro(deposito, dtagenda, dtprevisao, classes, agenda);
	}
	
	public List<ResumoAgendaverba> findResumoAgendaFinanceiro(Deposito deposito, Date dtinicio, Date dtfim, boolean filtrarPorVencimentoFinanceiro, List<String> classes){
		return agendaDAO.findResumoAgendaFinanceiro(deposito, dtinicio, dtfim, filtrarPorVencimentoFinanceiro, classes);
	}

	/**
	 * Carrega uma agenda com os dados necessários para o registro de hist'rocio de alteraç±ao do CRUD de Agenda
	 * @param agendaOriginal
	 * @return
	 */
	public Agenda loadForHistoricoAlteracao(Agenda agendaOriginal) {
		return agendaDAO.loadForHistoricoAlteracao(agendaOriginal);
	}

	public Agenda loadWithAgendaStatus(Agenda agenda) {
		return agendaDAO.loadWithAgendaStatus(agenda);
	}

	/**
	 * 
	 * @param agenda
	 * @return
	 */
	public boolean agendaContemRecebimento(Agenda agenda) {
		Integer existeRecebimento = agendaDAO.agendaContemRecebimento(agenda);
		if(existeRecebimento>0)
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @param inicio
	 * @param fim
	 * @return
	 */
	public Integer findByDate(Deposito deposito, java.sql.Date inicio, java.sql.Date fim) {
		return agendaDAO.findByDate(deposito,inicio,fim);
	}

	/**
	 * 
	 * @param inicio
	 * @param fim
	 * @return
	 */
	public Integer findByDateRecebido(Deposito deposito, java.sql.Date inicio, java.sql.Date fim) {
		return agendaDAO.findByDateRecebido(deposito,inicio,fim);
	}

	/**
	 * 
	 * @param inicio
	 * @param fim
	 * @return
	 */
	public Integer findByDateFuro(Deposito deposito, java.sql.Date inicio, java.sql.Date fim) {
		return agendaDAO.findByDateFuro(deposito,inicio,fim);
	}
	
	/**
	 * 
	 * @param inicio
	 * @param fim
	 * @return
	 */
	public Integer findByDateNaoComparecido(Deposito deposito, java.sql.Date inicio, java.sql.Date fim) {
		return agendaDAO.findByDateNaoComparecido(deposito,inicio,fim);
	}
	
	/**
	 * 
	 * @param numeroRav
	 * @return
	 */
	public Integer findAgendaRAV(Integer numeroRav ){
		Integer cdagenda = agendaDAO.findAgendaRAV(numeroRav);
		return cdagenda==0?null:cdagenda;
	}
	
	/**
	 * 
	 * @param agenda
	 * @param deposito
	 * @return
	 */
	public Agenda findByAgendaDeposito(Agenda agenda, Deposito deposito){
		return agendaDAO.findByAgendaDeposito(agenda,deposito);
	}

}
