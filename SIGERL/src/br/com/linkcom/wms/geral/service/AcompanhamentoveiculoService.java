package br.com.linkcom.wms.geral.service;

import java.awt.Image;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.neo.util.NeoImageResolver;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculo;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculostatus;
import br.com.linkcom.wms.geral.bean.Configuracao;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.dao.AcompanhamentoveiculoDAO;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.AcompanhamentoveiculoFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.report.filtro.EmitirRavEntradaFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class AcompanhamentoveiculoService extends GenericService<Acompanhamentoveiculo>{
	
	private AcompanhamentoveiculoDAO acompanhamentoveiculoDAO;
	private NeoImageResolver neoImageResolver;

	public void setNeoImageResolver(NeoImageResolver neoImageResolver) {
		this.neoImageResolver = neoImageResolver;
	}
	public void setAcompanhamentoveiculoDAO(AcompanhamentoveiculoDAO acompanhamentoveiculoDAO){
		this.acompanhamentoveiculoDAO = acompanhamentoveiculoDAO;
	}
	
	public Map<Integer,Integer> getPorcentagens(AcompanhamentoveiculoFiltro filtro){
		Map<Integer, Integer> percents = new HashMap<Integer, Integer>();
//		acompanhamentoveiculoDAO.getPorcentagens(filtro);
		return percents;
	}
	
	/**
	 * 
	 * @param numero
	 * @return
	 */
	public List<Acompanhamentoveiculo>loadAllInfoAcompVeiculo(String numero) {
		return acompanhamentoveiculoDAO.loadAllInfoAcompVeiculo(numero);
	}
	
	/**
	 * 
	 * @param numerorav
	 * @param Tipo
	 * @param data
	 * @param Lacre
	 * @return
	 * @throws SQLException
	 */
	public String callAtualizaRAV(String numerorav,String Tipo,Date data,String Lacre) throws SQLException {
		return acompanhamentoveiculoDAO.callAtualizaRAV(numerorav,Tipo,data,Lacre); 
	}

	/**
	 * 
	 * @param acompanhamentoveiculo
	 * @param tipo
	 * @return
	 * @throws SQLException
	 */
	public String callAtualizaRAV(Acompanhamentoveiculo acompanhamentoveiculo, String tipo) throws SQLException {
		if(acompanhamentoveiculo==null || acompanhamentoveiculo.getCdacompanhamentoveiculo()==null)
			throw new WmsException("Parâmetros Inválidos");
		
		String numeroRav = acompanhamentoveiculo.getCdacompanhamentoveiculo().toString();
		return acompanhamentoveiculoDAO.callAtualizaRAV(numeroRav,tipo,null,null);
	}
	
	/**
	 * Gera a impressão das etiquetas de RAV de Entrada
	 * 
	 * @author Jose de Queiroz
	 * @param recebimento
	 * @return
	 */
	public IReport createReportRavEntrada(EmitirRavEntradaFiltro filtro,WebRequestContext request) {
		Report report = new Report("RelatorioEtiquetaRAVEntrada");
		
		List<Acompanhamentoveiculo> list = acompanhamentoveiculoDAO.FindEtiquetaEntrada(filtro.getRecebimento().getCdrecebimento());
		
		if (list == null || list.isEmpty())
			throw new WmsException("Não foi encontrado nenhum dado para o Recebimento.");
		
		Image image = null;
		
		try {
			image = neoImageResolver.getImage(WmsUtil.getLogoForReport());
		} catch (IOException e) {
			throw new WmsException("Erro ao capturar o logotipo do relatório");
		}
		
		report.setDataSource(list);
		report.addParameter("DEPOSITO", WmsUtil.getDeposito().getNome());
		report.addParameter("LOGO", image);
		
		return report;
	}
	
	
	/**
	 * Gera a impressão das etiquetas de RAV de Saída
	 * 
	 * @author Jose de Queiroz
	 * @param recebimento
	 * @return
	 */
	public IReport createReportRavSaida(EmitirRavEntradaFiltro filtro,WebRequestContext request) {
		Report report = new Report("RelatorioEtiquetaRAVSaida");
		
		List<Acompanhamentoveiculo> list = acompanhamentoveiculoDAO.FindEtiquetaEntrada(filtro.getRecebimento().getCdrecebimento());
		
		if (list == null || list.isEmpty())
			throw new WmsException("Não foi encontrado nenhum dado para o Recebimento.");
		
		Image image = null;
		
		try {
			image = neoImageResolver.getImage(WmsUtil.getLogoForReport());
		} catch (IOException e) {
			throw new WmsException("Erro ao capturar o logotipo do relatório");
		
		}
		
		report.setDataSource(list);
		report.addParameter("DEPOSITO", WmsUtil.getDeposito().getNome());
		report.addParameter("LOGO", image);
		
		return report;
	}
	
	/* singleton */
	private static AcompanhamentoveiculoService instance;
	public static AcompanhamentoveiculoService getInstance(){
		if(instance == null){
			instance = Neo.getObject(AcompanhamentoveiculoService.class);
		}
		return instance;
	}
	
	/**
	 * 
	 * @return
	 */
	public Acompanhamentoveiculo findByNumeroRav(String numeroRav){
		return acompanhamentoveiculoDAO.findByNumeroRav(numeroRav);
	}

	/**
	 * 
	 * @param av
	 */
	public void registarSaida(Acompanhamentoveiculo av) {
		acompanhamentoveiculoDAO.registarSaida(av);
	}

	/**
	 * 
	 * @param av
	 */
	public void registarEntrada(Acompanhamentoveiculo av) {
		acompanhamentoveiculoDAO.registarEntrada(av);
		
	}

	/**
	 * 
	 * @param recebimento
	 * @param list 
	 */
	public void vincularRecebimento(Recebimento recebimento, List<Acompanhamentoveiculo> list) {
		acompanhamentoveiculoDAO.vincularRecebimento(recebimento,list);
	}
	
	/**
	 * 
	 * @param acompanhamentoveiculo
	 * @return
	 */
	public Boolean isVeiculoRecebido(Acompanhamentoveiculo av){
		Boolean isEditavel = Boolean.TRUE;
		av = acompanhamentoveiculoDAO.isVeiculoRecebido(av);
		if(av!=null && av.getRecebimento()!=null && av.getRecebimento().getCdrecebimento()!=null){
			isEditavel = Boolean.FALSE;
		}
		return isEditavel;
	}
	
	/**
	 * 
	 * @param recebimento
	 */
	public void removerRecebimento(Recebimento recebimento) {
		acompanhamentoveiculoDAO.removerRecebimento(recebimento);
	}
	
	/**
	 * 
	 * @param acompanhamentoveiculo
	 * @return
	 */
	public Boolean isVeiculoRecebido(Recebimento recebimento){
		Boolean isVeiculoVinculado = Boolean.FALSE;
		Acompanhamentoveiculo av = acompanhamentoveiculoDAO.isVeiculoRecebido(recebimento);
		if(av!=null && av.getRecebimento()!=null && av.getRecebimento().getCdrecebimento()!=null){
			isVeiculoVinculado = Boolean.TRUE;
		}
		return isVeiculoVinculado;
	}
	
	/**
	 * 
	 * @param recebimento
	 * @param temDevolucao 
	 */
	public void checkTemDevolucao(Recebimento recebimento, Integer temDevolucao) {
		acompanhamentoveiculoDAO.checkTemDevolucao(recebimento,temDevolucao);
	}

	
	/**
	 * 
	 * @param recebimento
	 * @return
	 */
	public List<Acompanhamentoveiculo> findByRecebimento(Recebimento recebimento) {
		return acompanhamentoveiculoDAO.findByRecebimento(recebimento);
	}
	
	/**
	 * 
	 * @param recebimento
	 * @return
	 */
	public String getNumerosRavByRecebimento(Recebimento recebimento) {
		if(recebimento==null || recebimento.getCdrecebimento()==null){
			return null;
		}

		List<Acompanhamentoveiculo> lista = acompanhamentoveiculoDAO.findByRecebimento(recebimento);
		
 		if(lista==null || lista.isEmpty()){
			return "";
		}else{
			return CollectionsUtil.listAndConcatenate(lista, "numerorav", ",");	
		}
	}
	
	/**
	 * 
	 * @param recebimento
	 * @return
	 */
	public Integer getNumerosNotasRavByRecebimento(Recebimento recebimento) {
		if(recebimento==null || recebimento.getCdrecebimento()==null){
			return 0;
		}
		
		return acompanhamentoveiculoDAO.getTotalNotasRavByRecebimento(recebimento);
	}
	
	/**
	 * 
	 * @param av
	 * @param recebimentoFinalizado
	 */
	public void atualizaStatus(Acompanhamentoveiculo av, Acompanhamentoveiculostatus status) {
		acompanhamentoveiculoDAO.atualizaStatus(av,status);
	}
	
	/**
	 * 
	 * @param list 
	 * @return
	 */
	public boolean validaRavForRecebimento(List<Acompanhamentoveiculo> lista) {
		
		Integer totalRavs = lista.size();
		Integer ravValidas = 0;
		
		List<Acompanhamentoveiculo> listaRavValidas = acompanhamentoveiculoDAO.findRavForRecebimento(lista);
		
		if(listaRavValidas!=null && !listaRavValidas.isEmpty()){
			 ravValidas = listaRavValidas.size();
		}
		
		//Após filtrar via HQL as RAV's validas para o recebimetno, comparo se todas as RAV's estão validas.
		if(ravValidas == totalRavs){	
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 
	 * @param acompanhamentoveiculo 
	 * @return
	 */
	public String validarPlacaVeiculoForSave(Acompanhamentoveiculo acompanhamentoveiculo){
		
		List<Acompanhamentoveiculo> ravsAbertas = acompanhamentoveiculoDAO.findByVeiculo(acompanhamentoveiculo);
		StringBuilder ravs = new StringBuilder();
		
		if(ravsAbertas!=null && !ravsAbertas.isEmpty()){
			ravs.append("Um veículo de mesma placa está vinculado a uma RAV aberta. Segue abaixo a lista de Depósitos / RAVs vinculadas a placa:");
			ravs.append("<ul>");
			for (Acompanhamentoveiculo av : ravsAbertas) {
				ravs.append("<li class='globalerror'><span class='exceptionitem'>Depósito: ").append(av.getDeposito().getNome()).append(" &nbsp / &nbsp RAV: ").append(av.getCdacompanhamentoveiculo()).append("</span></li>");
			}
			ravs.append("</ul>");
		}
		
		return ravs.toString();
		
	}
	
	/**
	 * Método que cancelará automaticamente todas as RAV's que permaneceram abertas por 3 dias.
	 * 
	 * @author Filipe
	 * @since 13/04/2016
	 * @see WmsUtil.#atualizaInformacoesPrimeiroLoginDia(javax.servlet.http.HttpServletRequest)
	 */
	public void cancelaRAVsAntigos() {
		Configuracao config = ConfiguracaoService.getInstance().findByName(null, ConfiguracaoVO.NUM_DIAS_RAV_CANCELAMENTO);
		if(config != null){
			try{
				Integer num = Integer.parseInt(config.getValor());
				num = num * (-1);
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.add(Calendar.DAY_OF_MONTH, num);
				
				List<Acompanhamentoveiculo> listaRavs = this.findForCancelamento(new java.sql.Date(calendar.getTimeInMillis()));
				
				for (Acompanhamentoveiculo av : listaRavs) {
					av.setAcompanhamentoveiculostatus(Acompanhamentoveiculostatus.CANCELADO);
					this.atualizaStatus(av, av.getAcompanhamentoveiculostatus());
					AcompanhamentoveiculohistoricoService.getInstance().criarHistorico(av,null, null);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public List<Acompanhamentoveiculo> findForCancelamento(Date date){
		return acompanhamentoveiculoDAO.findForCancelamento(date);
	}
	
}
