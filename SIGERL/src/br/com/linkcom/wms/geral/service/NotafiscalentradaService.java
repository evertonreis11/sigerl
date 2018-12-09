package br.com.linkcom.wms.geral.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.util.NeoFormater;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Notafiscalentrada;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.dao.NotafiscalentradaDAO;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.RelatorioposicionamentoFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.RecebimentoFiltro;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;


public class NotafiscalentradaService extends GenericService<Notafiscalentrada> {

	private NotafiscalentradaDAO notafiscalentradaDAO;
	private ProdutoService produtoService;
	
	public void setNotafiscalentradaDAO(NotafiscalentradaDAO notafiscalentradaDAO) {
		this.notafiscalentradaDAO = notafiscalentradaDAO;
	}
	
	public void setProdutoService(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscalentradaDAO#loadInfoForPopUp
	 * @param notafiscalentrada
	 * @return
	 * @author Pedro Gonçalves
	 */
	public Notafiscalentrada loadInfoForPopUp(Notafiscalentrada notafiscalentrada) {
		return notafiscalentradaDAO.loadInfoForPopUp(notafiscalentrada);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscalentradaDAO#getProdutos
	 * @param notafiscalentrada
	 * @return
	 * @author Pedro Gonçalves
	 */
	public Notafiscalentrada getProdutos(Notafiscalentrada notafiscalentrada) {
		return notafiscalentradaDAO.getProdutos(notafiscalentrada);
	}
	
	/* singleton */
	private static NotafiscalentradaService instance;
	public static NotafiscalentradaService getInstance() {
		if(instance == null){
			instance = Neo.getObject(NotafiscalentradaService.class);
		}
		return instance;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscalentradaDAO#findPlacas()
	 * @return
	 */
	public List<String> findPlacas(String whereIn) {
		return notafiscalentradaDAO.findPlacas(whereIn);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscalentradaDAO#findPlacas()
	 * @return
	 */
	public List<String> findPlacas() {
		return notafiscalentradaDAO.findPlacas();
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscalentradaDAO#findBy(RecebimentoFiltro filtro)
	 * @param filtro
	 * @return
	 */
	public List<Notafiscalentrada> findBy(RecebimentoFiltro filtro){
		return notafiscalentradaDAO.findBy(filtro);
	}
	

	/**
	 * Agrupa uma lista de notas fiscais por veículo
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * 
	 * @param listaNotasFiscais
	 * @return
	 */
	public List<List<Notafiscalentrada>> groupNotasFiscais(List<Notafiscalentrada> listaNotasFiscais){
		List<List<Notafiscalentrada>> listaAgrupada = new ArrayList<List<Notafiscalentrada>>();
		int i = 0;
		while(i < listaNotasFiscais.size()){
			List<Notafiscalentrada> listaAux = new ArrayList<Notafiscalentrada>();
			Notafiscalentrada notafiscalentrada = new Notafiscalentrada();
			do{
				notafiscalentrada = listaNotasFiscais.get(i);
				listaAux.add(notafiscalentrada);
				i++;
			}while(i < listaNotasFiscais.size() && notafiscalentrada.getVeiculo().equals(listaNotasFiscais.get(i).getVeiculo()));
			listaAgrupada.add(listaAux);
		}
		return listaAgrupada;
	}
	
	/**
	 * Método de referência ao DAO.
	 * Carrega as informações da nota fiscal para apresentação na tela de devolução.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscalentradaDAO#loadInfoForDevolucao(String numero)
	 * 
	 * @author Pedro Gonçalves
	 * @param notafiscalentrada
	 * @return
	 */
	public List<Notafiscalentrada>loadInfoForDevolucao(String numero) {
		return notafiscalentradaDAO.loadInfoForDevolucao(numero);
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscalentradaDAO#getTotalNfe(Notafiscalentrada)
	 * @param nfe
	 * @return
	 * 
	 * 
	 * @author Pedro Gonçalves / Arantes 
	 * 
	 */
	public Long getTotalNfe(Notafiscalentrada nfe){
		return notafiscalentradaDAO.getTotalNfe(nfe);
	}
	
	/**
	 * Método de referência ao DAO.
	 * Executa um update no campo devolvida.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscalentradaDAO#devolverNotaFiscalEntrada(Notafiscalentrada notafiscalentrada)
	 * @author Pedro Gonçalves
	 * @param notafiscalentrada
	 */
	public void devolverNotaFiscalEntrada(Notafiscalentrada notafiscalentrada){
		notafiscalentradaDAO.devolverNotaFiscalEntrada(notafiscalentrada);
	}

	/**
	 * Método de referência ao DAo
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscalentradaDAO#findByRecebimento(Recebimento recebimento)
	 * 
	 * @param recebimento
	 * @return
	 */
	public List<Notafiscalentrada> findByRecebimento(Recebimento recebimento) {
		return notafiscalentradaDAO.findByRecebimento(recebimento);
	}
	
	/**
	 * Método de referencia ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscalentradaDAO#devolverNotaFiscal(Recebimento recebimento)
	 * 
	 * @param recebimento
	 */
	public void devolverNotaFiscal(Recebimento recebimento) {
		notafiscalentradaDAO.devolverNotaFiscal(recebimento);
	}

	/**
	 * Verifica se uma nota de entrada está relacionada a algum recebimento.
	 * 
	 * @author Giovane Freitas
	 * @param notaentrada
	 * @return
	 */
	public Boolean isRecebido(Notafiscalentrada notaentrada) {
		return notafiscalentradaDAO.isRecebido(notaentrada);
	}

	/**
	 * Verifica se uma nota de entrada está cancelada.
	 * 
	 * @author Filipe Araujo
	 * @param notaentrada
	 * @return
	 */
	public Boolean isCancelado(Notafiscalentrada notaentrada) {
		return notafiscalentradaDAO.isCancelado(notaentrada);
	}
	
	/**
	 * Cancela a nota fiscal de entrada;
	 * 
	 * @see NotafiscalentradaDAO#cancelar(Notafiscalentrada)
	 * @author Giovane Freitas
	 * @param notafiscalentrada
	 */
	public void cancelar(Notafiscalentrada notafiscalentrada) {
		notafiscalentradaDAO.cancelar(notafiscalentrada);
	}

	
	/**
	 * Método que retorna relatório de posicionamento
	 * 
	 * @see #findForPosicionamentoReport(RelatorioposicionamentoFiltro)
	 * @see #adicionaParametrosRelatorioPosicionamento(Report, RelatorioposicionamentoFiltro)
	 * @param filtro
	 * @return
	 * @author Tomás Rabelo
	 */
	public IReport createReportPosicionamento(RelatorioposicionamentoFiltro filtro) {
		Report report = new Report("RelatorioPosicionamento");
		Report subreport1 = new Report("SubRelatorioPosicionamento");
		Report subreport2 = new Report("SubSubRelatorioPosicionamento");
		report.addSubReport("SUB_RELATORIOPOSICIONAMENTO", subreport1);
		subreport1.addSubReport("SUBSUB_RELATORIOPOSICIONAMENTO", subreport2);
		
		List<Notafiscalentrada> list = findForPosicionamentoReport(filtro);
		
		report.setDataSource(list);
		
		adicionaParametrosRelatorioPosicionamento(report, filtro);
		return report;
	}

	/**
	 * Método que adiciona parametros escolhidos no filtro no relatório
	 * 
	 * @param report
	 * @param filtro
	 * @author Tomás Rabelo
	 */
	private void adicionaParametrosRelatorioPosicionamento(Report report,RelatorioposicionamentoFiltro filtro) {
		if(filtro.getProduto() != null && filtro.getProduto().getCdproduto() != null)
			report.addParameter("PRODUTO", produtoService.getProdutoDescriptionProperty(filtro.getProduto()));
		if(filtro.getDocumentoentrada() != null && !filtro.getDocumentoentrada().equals(""))
			report.addParameter("DOCUMENTOENTRADA", filtro.getDocumentoentrada());
		if(filtro.getDtvalidadede() != null || filtro.getDtvalidadeate() != null)
			report.addParameter("DATAVALIDADE", (WmsUtil.getDescricaoPeriodo(filtro.getDtvalidadede(), filtro.getDtvalidadeate())));
		if(filtro.getLote() != null && !filtro.getLote().equals(""))
			report.addParameter("LOTE", filtro.getLote());
		
		report.addParameter("nomeusuario", WmsUtil.getUsuarioLogado().getNome());
		report.addParameter("data", NeoFormater.getInstance().format(new Timestamp(System.currentTimeMillis())));
		
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param filtro
	 * @return
	 * @author Tomás Rabelo
	 */
	private List<Notafiscalentrada> findForPosicionamentoReport(RelatorioposicionamentoFiltro filtro) {
		return notafiscalentradaDAO.findForPosicionamentoReport(filtro);
	}
	
	/**
	 * Método de referencia ao DAO
	 * 
	 * @see NotafiscalentradaDAO#atualizarDataChegada(Notafiscalentrada)
	 * @author Giovane Freitas
	 * @param notafiscalentrada
	 * @param dataChegada
	 */
	public void atualizarDataChegada(Recebimento recebimento, Timestamp dataChegada) {
		notafiscalentradaDAO.atualizarDataChegada(recebimento, dataChegada);
	}
	
	public void updateVinculoAgendamento(Map<Notafiscalentrada, Agenda> map) {
		if(map != null){
			Set<Entry<Notafiscalentrada, Agenda>> entrySet = map.entrySet();
			if(entrySet != null){
				for (Entry<Notafiscalentrada, Agenda> entry : entrySet) {
					Agenda agenda = entry.getValue();
					notafiscalentradaDAO.updateVinculoAgendamento(entry.getKey(), agenda);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param codigoERP
	 * @return
	 */
	public Notafiscalentrada findByCodigoERP(Long codigoERP){
		return notafiscalentradaDAO.findByCodigoERP(codigoERP);
	}
	
	/**
	 * 
	 * @param notafiscalentrada
	 */
	public void updateNotafiscalentrada(Notafiscalentrada notafiscalentrada) {
		notafiscalentradaDAO.updateNotafiscalentrada(notafiscalentrada);
	}
	
	/**
	 * 
	 * @param notafiscalentrada
	 * @return
	 */
	public Notafiscalentrada loadNotaPedido(Notafiscalentrada notafiscalentrada) {
		return notafiscalentradaDAO.loadNotaPedido(notafiscalentrada);
	}
	
	/**
	 * 
	 * @param whereIn
	 * @return
	 */
	public List<String> findPlacasAcompanhamento(String whereIn){
		return notafiscalentradaDAO.findPlacasAcompanhamento(whereIn);
	}

	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscalentradaDAO#findBy(RecebimentoFiltro filtro)
	 * @param filtro
	 * @return
	 */
	public List<Notafiscalentrada> findByGerarRecebimento(RecebimentoFiltro filtro, Boolean isAgenda){
		return notafiscalentradaDAO.findByGerarRecebimento(filtro,isAgenda);
	}
	
	/**
	 * 
	 * @param dataSource
	 * @param whereIn
	 * @return
	 */
	public SqlRowSet gerarRelatorioNF(JdbcTemplate jdbcTemplate, String whereIn){
		return notafiscalentradaDAO.gerarRelatorioNF(jdbcTemplate,whereIn);
	} 
}
