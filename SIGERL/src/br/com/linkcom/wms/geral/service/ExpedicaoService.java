package br.com.linkcom.wms.geral.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Boxstatus;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentohistorico;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.geral.bean.Expedicao;
import br.com.linkcom.wms.geral.bean.Expedicaostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.dao.ExpedicaoDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.EmitirliberacaoveiculoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ExpedicaoService extends GenericService<Expedicao> {

	private ExpedicaoDAO expedicaoDAO;
	private OrdemservicoService ordemservicoService;
	private CarregamentoService carregamentoService;
	private BoxService boxService;
	private PedidovendaprodutoService pedidovendaprodutoService;
	private CarregamentohistoricoService carregamentohistoricoService;
	
	public void setExpedicaoDAO(ExpedicaoDAO expedicaoDAO) {
		this.expedicaoDAO = expedicaoDAO;
	}
	public void setOrdemservicoService(OrdemservicoService ordemservicoService) {
		this.ordemservicoService = ordemservicoService;
	}
	public void setCarregamentoService(CarregamentoService carregamentoService) {
		this.carregamentoService = carregamentoService;
	}
	public void setBoxService(BoxService boxService) {
		this.boxService = boxService;
	}
	public void setCarregamentohistoricoService(CarregamentohistoricoService carregamentohistoricoService) {
		this.carregamentohistoricoService = carregamentohistoricoService;
	}
	public void setPedidovendaprodutoService(PedidovendaprodutoService pedidovendaprodutoService) {
		this.pedidovendaprodutoService = pedidovendaprodutoService;
	}

	/* singleton */
	private static ExpedicaoService instance;
	public static ExpedicaoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(ExpedicaoService.class);
		}
		return instance;
	}
	
	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param box
	 * @return
	 * @author Tom�s Rabelo
	 */
	public Expedicao loadExpedicaoForGerenciamento(Box box) {
		return expedicaoDAO.loadExpedicaoForGerenciamento(box);
	}

	/**
	 * M�todo que cria etiquetas para ordens de servi�o do tipo expedi��o
	 * 
	 * @param whereInOrdemServicos
	 * @return
	 * @author Tom�s Rabelo
	 */
	public IReport createReportExpedicao(Expedicao expedicao) {
		Report report = new Report("RelatorioEtiquetaExpedicao");
		
		List<Ordemservico> list = ordemservicoService.findByExpedicao(expedicao, null);
		if(list != null && !list.isEmpty()){
			Iterator<Ordemservico> iterator = list.iterator();
			while (iterator.hasNext()){ 
				Ordemservico ordemservico = iterator.next();
				if(ordemservico.getOrdemtipo().equals(Ordemtipo.REABASTECIMENTO_PICKING) || ordemservico.getOrdemtipo().equals(Ordemtipo.MAPA_SEPARACAO))
					iterator.remove();
			}
		}
		report.setDataSource(list);
		return report;
	}
	
	/**
	 * M�todo que cria expedi��es e atualiza a refer�ncia dos carregamentos
	 * 
	 * @param listaBoxes
	 * @author Tom�s Rabelo
	 */
	public void criaExpedicoesAtualizaCarregamentos(final List<Box> listaBoxes) {
		for (Box box : listaBoxes) {
			Expedicao expedicao = new Expedicao(new Box(box.getCdbox()), new Timestamp(System.currentTimeMillis()), Expedicaostatus.EM_ANDAMENTO);
			saveOrUpdateNoUseTransaction(expedicao);
			box.getListaExpedicoes().add(expedicao);
			
			for (Carregamento carregamento : box.getListaCarregamentos()) 
				carregamentoService.atualizarExpedicao(expedicao, carregamento);
			
			boxService.updateStatusBox(box, Boxstatus.OCUPADO);
		}
	}
	
	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param box
	 * @return
	 * @author Tom�s Rabelo
	 */
	public Boolean possuiExpedicaoEmAndamento(Box box) {
		return expedicaoDAO.possuiExpedicaoEmAndamento(box);
	}
	
	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param expedicao
	 * @return
	 * @author Tom�s Rabelo
	 */
	public Expedicao loadExpedicaoForGerenciamento(Expedicao expedicao) {
		return expedicaoDAO.loadExpedicaoForGerenciamento(expedicao);
	}
	
	/**
	 * M�todo com ref�rencia no DAO
	 * 
	 * @param expedicao
	 * @return
	 * @author Tom�s Rabelo
	 */
	public Expedicao getExpedicao(Expedicao expedicao) {
		return expedicaoDAO.getExpedicao(expedicao);
	}
	
	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param expedicaoAux
	 * @param status
	 * @author Tom�s Rabelo
	 */
	public void updateStatusExpedicao(Expedicao expedicaoAux, Expedicaostatus status) {
		expedicaoDAO.updateStatusExpedicao(expedicaoAux, status);
		
		String msg = "";
		if(status.equals(Expedicaostatus.FINALIZADO))
			msg = Carregamentohistorico.FINALIZA_EXPEDICAO;
		else if(status.equals(Expedicaostatus.CANCELADO))
			msg = Carregamentohistorico.CANCELA_EXPEDICAO; 
	} 
	
	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param box
	 * @return
	 * @author Tom�s Rabelo
	 */
	public boolean existeExpedicaoEmAndamentoParaBox(Box box) {
		return expedicaoDAO.existeExpedicaoEmAndamentoParaBox(box);
	}
	
	/**
	 * M�todo que muda a situa��o da expedi��o para finalizado e verifica a
	 * situa��o do BOX, caso n�o exista nenhuma ordem de servi�o pendente. Se
	 * existir alguma aberta, n�o far� nada.
	 * 
	 * @param expedicao
	 * @author Tom�s Rabelo
	 */
	public void processaSituacaoExpedicao(final Expedicao expedicao, final Usuario user) {

		List<Ordemservico> listaOrdemservico = OrdemservicoService.getInstance().findByExpedicao(expedicao, null);
		
		if (!OrdemservicoService.getInstance().existeOSAberta(expedicao)){
			
			List<Carregamento> carregamentos = carregamentoService.findByExpedicao(expedicao);
			for (Carregamento c : carregamentos){
				if (!c.getCarregamentostatus().equals(Carregamentostatus.FINALIZADO)){
					carregamentoService.finalizar(c, ordemservicoService.findByCarregamento(c));
				}
			}
				
			for (Ordemservico ordemservico : listaOrdemservico){
				if (ordemservico.getOrdemtipo().equals(Ordemtipo.MAPA_SEPARACAO) &&	ordemservico.getOrdemstatus().equals(Ordemstatus.EM_ABERTO)){
					ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_MANUALMENTE);
					ordemservicoService.atualizarStatusordemservico(ordemservico);
				}
			}
			
			updateStatusExpedicao(expedicao, Expedicaostatus.FINALIZADO);
			
			gerarHistoricoExpedicao(expedicao, user);
			
			if(!existeExpedicaoEmAndamentoParaBox(expedicao.getBox()))
				boxService.updateStatusBox(expedicao.getBox(), Boxstatus.DISPONIVEL);
			
			if (WmsUtil.isFinalizadoBaixaEstoque())
				expedicaoDAO.baixarEstoqueExpedicao(expedicao);
			
		}
	}
	
	/**
	 * M�todo respons�vel por cancelar a expedi��o, as ordens de servi�o do tipo Mapa de separa��o e os carregamentos
	 * 
	 * @param expedicao
	 * @author Tom�s Rabelo
	 */
	public void cancelarExpedicao(final Expedicao expedicao) {
		Expedicao expedicaoAux = getExpedicao(expedicao);
		for (Ordemservico ordemservico : expedicaoAux.getListaOrdensservico()){
			if(ordemservico.getOrdemtipo().equals(Ordemtipo.MAPA_SEPARACAO) || ordemservico.getOrdemtipo().equals(Ordemtipo.CONFERENCIA_CHECKOUT))
				ordemservicoService.cancelar(ordemservico);
			
			if(ordemservico.getOrdemtipo().equals(Ordemtipo.CONFERENCIA_CHECKOUT)){
				EmbalagemexpedicaoprodutoService.getInstance().deleteByOrdem(ordemservico);
			}
		}
		
		for (Carregamento carregamento : expedicaoAux.getListaCarregamento()) 
			carregamentoService.cancelarCarregamento(carregamento);
		
		updateStatusExpedicao(expedicaoAux, Expedicaostatus.CANCELADO);
		if(!existeExpedicaoEmAndamentoParaBox(expedicaoAux.getBox()))
			boxService.updateStatusBox(expedicaoAux.getBox(), Boxstatus.DISPONIVEL);
	}
	
	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param box
	 * @return
	 * @author Tom�s Rabelo
	 */
	public List<Expedicao> findExpedicoesDoBoxEmAndamento(Box box) {
		return expedicaoDAO.findExpedicoesDoBoxEmAndamento(box);
	}
	
	/**
	 * Gera a impress�o das etiquetas de embalagens de expedi��o.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public IReport createReportLiberacaoVeiculo(EmitirliberacaoveiculoFiltro filtro) {
		Report report = new Report("RelatorioLiberacaoVeiculo");

		List<Carregamento> list = carregamentoService.findForLiberacao(filtro, Carregamentostatus.FATURADO);
		
		if (list == null || list.isEmpty())
			throw new WmsException("N�o foi encontrado nenhum carregamento faturado.");
		
		for (Carregamento carregamento : list)
			carregamento.setListaNotasFiscais(pedidovendaprodutoService.findNumerosNotas(carregamento));
		
		report.setDataSource(list);
		report.addParameter("DEPOSITO", WmsUtil.getDeposito().getNome());
		
		return report;
	}
	
	/**
	 * Realiza o endere�amento dos mapas de separa��o.
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param deposito
	 * @param carregamento
	 * @throws SQLException 
	 */
	public void enderecarExpedicao(final Expedicao expedicao) {
		expedicaoDAO.enderecarExpedicao(expedicao);
	}
	
	/**
	 * M�todo respons�vel pela valida��o e cria��o dos Hist�ricos nas Expedi��es. 
	 * 
	 * @param ordemservico
	 * @param user
	 */
	private void gerarHistoricoExpedicao(Expedicao expedicao, Usuario user) {
		List<Carregamento> listCarregamento = carregamentoService.findByExpedicao(expedicao);
		for (Carregamento carregamento : listCarregamento){
			carregamento.setCarregamentostatus(Carregamentostatus.FINALIZADO);
			carregamentohistoricoService.criaHistorico(carregamento, null, WmsUtil.getUsuarioLogado(user));
		}
		for (Carregamento carregamento : listCarregamento){
			carregamento.setCarregamentostatus(Carregamentostatus.FINALIZADO);
			carregamentohistoricoService.criaHistorico(carregamento, Carregamentohistorico.FINALIZA_EXPEDICAO, WmsUtil.getUsuarioLogado(user));
		}
	}
}
