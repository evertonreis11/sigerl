package br.com.linkcom.wms.geral.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestohistorico;
import br.com.linkcom.wms.geral.bean.Manifestonotafiscal;
import br.com.linkcom.wms.geral.bean.Manifestostatus;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.Tipoentrega;
import br.com.linkcom.wms.geral.bean.Tipomanifestohistorico;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.vo.DescargaprodutoVO;
import br.com.linkcom.wms.geral.bean.vo.ManifestoVO;
import br.com.linkcom.wms.geral.bean.vo.PedidosmanifestoVO;
import br.com.linkcom.wms.geral.dao.ManifestoDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.BorderoFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.ManifestoPlanilhaFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.EmitirPedidosManifestoFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.EmitirdescargaprodutoFiltro;
import br.com.linkcom.wms.sincronizador.IntegradorSqlUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ManifestoService extends GenericService<Manifesto>{
	
	private static ManifestoService instance;
	private ManifestoDAO manifestoDAO;
	private ManifestohistoricoService manifestohistoricoService;
	private ManifestonotafiscalService manifestonotafiscalService;
	private TransactionTemplate transactionTemplate;
	
	public void setManifestohistoricoService(ManifestohistoricoService manifestohistoricoService) {
		this.manifestohistoricoService = manifestohistoricoService;
	}
	public void setManifestoDAO(ManifestoDAO manifestoDAO) {
		this.manifestoDAO = manifestoDAO;
	}
	public void setManifestonotafiscalService(ManifestonotafiscalService manifestonotafiscalService) {
		this.manifestonotafiscalService = manifestonotafiscalService;
	}
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	
	/**
	 * 
	 * @param filtro
	 */
	public void cancelarManifesto(Manifesto manifesto) {
		manifestoDAO.cancelarManifesto(manifesto);
	}

	/**
	 * Método que valida se exite algum Manifesto com status diferente de "Em Elaboração".
	 * Caso positivo, o sistema não deverá excluir nada.
	 * 
	 * @param parameter
	 */
	public boolean validaManifestoStatusForExcluir(String whereIn) {
		
		List<Manifesto> listaManifesto = manifestoDAO.findManifestoStatusByWhereIn(whereIn);
		
		if(listaManifesto!=null && !listaManifesto.isEmpty()){
			for (Manifesto manifesto : listaManifesto) {
				if(manifesto!=null && manifesto.getManifestostatus()!=null && manifesto.getManifestostatus().equals(Manifestostatus.EM_ELABORACAO)){
					continue;
				}else{
					return false;
				}
			}
		}else{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param manifesto
	 * @param manifestostatus 
	 * @param msgStatus 
	 */
	public void updateManifestoStatus(Manifesto manifesto, Manifestostatus manifestostatus, String msgStatus, Usuario usuario) {
		if(manifesto==null || manifestostatus == null || manifestostatus.getCdmanifestostatus() == null){
			throw new WmsException("Parametros Inválidos. Para imprimir é necessário");
		}else{
			manifestohistoricoService.criarHistorico(manifesto, msgStatus, manifestostatus, usuario, Tipomanifestohistorico.STATUS);
			manifestoDAO.updateManifestoStatus(manifesto,manifestostatus);
		}
	}
	
	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public IReport createReportManifesto(Manifesto manifesto) {
		Report report = new Report("RelatorioImpressaoManifesto");
		List<ManifestoVO> list = manifestoDAO.findForReport(manifesto);
		report.setDataSource(list);
		return report;
	}

	public static ManifestoService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(ManifestoService.class);
		}
		return instance;
	}
	
	/**
	 * 
	 * @param codigo
	 * @param deposito
	 * @return
	 */
	public Manifesto findByCodigoBarras(String codigo, Deposito deposito, Manifestostatus manifestostatus){
		return manifestoDAO.findByCodigoBarras(codigo, deposito, manifestostatus);
	}
	
	/**
	 * 
	 * @param manifesto
	 */
	public void callGerarEntregasManifesto(Manifesto manifesto) {
		
		if(manifesto==null || manifesto.getCdmanifesto() == null){
			throw new WmsException("Parametros Inválidos. Para imprimir é necessário");
		}
		
		Connection connection = null;
		CallableStatement cs = null;
		
		try {
			connection = IntegradorSqlUtil.getNewConnection();
			
	        cs = connection.prepareCall("{ call GERAR_ENTREGAS_MANIFESTO (?) }");
	        
	        cs.registerOutParameter(1,Types.NUMERIC);
	        
	        if(manifesto!=null && manifesto.getCdmanifesto()!=null){
	        	cs.setInt(1,manifesto.getCdmanifesto());
	        }else{
	        	cs.setNull(1, Types.INTEGER);
	        }
	        
	        cs.execute();
	        Integer resposta = cs.getInt(1);	        
	        
	        if (resposta.equals(1)){
	        	connection.commit();
	        }else{
	        	connection.rollback();
	        }
	        
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			}
			catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		finally {
			try {
				cs.close();
				connection.close();
			}
			catch (Exception e) {
				System.out.println("Erro ao fechar a conexção do banco.\n");
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 
	 * @param codigo
	 * @param deposito
	 * @return
	 */
	public Manifesto findByPrestacaoConta(String codigo, Manifesto manifesto) {
		return manifestoDAO.findByPrestacaoConta(codigo,manifesto);
	}
	
	/**
	 * 
	 * @param manifesto
	 * @param kminicial 
	 */
	public void updateKmInicialManifesto(Manifesto manifesto, Long kminicial) {
		manifestoDAO.updateKmInicialManifesto(manifesto, kminicial);
	}
	
	/**
	 * 
	 * @param manifesto
	 * @param kmfinal
	 */
	public void updateKmFinalManifesto(Manifesto manifesto, Long kmfinal) {
		manifestoDAO.updateKmFinalManifesto(manifesto, kmfinal);
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public Manifesto loadWithManifestostatus(String codigo) {
		return manifestoDAO.loadWithManifestostatus(codigo);
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public Manifesto findForFechamentoFinanceiro(String codigo, Manifesto manifesto) {
		return manifestoDAO.findForFechamentoFinanceiro(codigo, manifesto);
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Manifesto> findForBordero(BorderoFiltro filtro) {
		return manifestoDAO.findForBordero(filtro);
	}
	
	/**
	 * 
	 * @param whereIn
	 * @return
	 */
	public List<Manifesto> findByWhereIn(String whereIn) {
		return manifestoDAO.findByWhereIn(whereIn);
	}

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public SqlRowSet getDadosExportacao(ManifestoPlanilhaFiltro filtro) {
		return manifestoDAO.getDadosExportacao(filtro);
	}
	
	/**
	 * 
	 */
	public void updateDtprestacaocontas(Manifesto manifesto) {
		manifestoDAO.updateDtprestacaocontas(manifesto);
	}

	/**
	 * @param usuario 
	 * 
	 */
	public void updateDtfinalizacao(Manifesto manifesto, Usuario usuario) {
		manifestoDAO.updateDtfinalizacao(manifesto,usuario);
	}
	
	/**
	 * 
	 * @param manifesto
	 * @param usuarioLogado
	 */
	public void updateDtsaidaveiculo(Manifesto manifesto, Usuario usuario) {
		manifestoDAO.updateDtsaidaveiculo(manifesto,usuario);
	}
	
	/**
	 * 
	 * @param manifesto
	 */
	public String callCriarAE (Manifesto manifesto) {
		
		if(manifesto==null || manifesto.getCdmanifesto() == null){
			throw new WmsException("Parametros Inválidos. Para imprimir é necessário");
		}
		
		String resposta = "";
		Connection connection = null;
		CallableStatement cs = null;
		
		try {
			connection = IntegradorSqlUtil.getNewConnection();
			
	        cs = connection.prepareCall("{ call PRC_CALL_CRIAR_AE (?,?,?) }");
	        
	        if(manifesto!=null && manifesto.getDeposito()!=null && manifesto.getDeposito().getCddeposito()!=null){
	        	cs.setInt(1,manifesto.getDeposito().getCddeposito());
	        }else{
	        	cs.setNull(1, Types.INTEGER);
	        }
	        
	        if(manifesto!=null && manifesto.getCdmanifesto()!=null){
	        	cs.setInt(2,manifesto.getCdmanifesto());
	        }else{
	        	cs.setNull(2, Types.INTEGER);
	        }
	        
	        cs.registerOutParameter(3,Types.VARCHAR);
	        
	        cs.execute();
	        resposta = cs.getString(3);	        
	        
	        if (resposta.equals("OK")){
	        	connection.commit();
	        }else{
	        	connection.rollback();
	        }
	        
		}
		catch (Exception e) {			
			e.printStackTrace();
			try {
				connection.rollback();
			}
			catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		finally {
			try {
				cs.close();
				connection.close();
			}
			catch (Exception e) {
				System.out.println("Erro ao fechar a conexção do banco.\n");
				e.printStackTrace();
			}
		}
		return resposta;
	}
	
	/**
	 * 
	 * @param cdae
	 * @param cdmanifesto
	 */
	public void updateCDAE(Integer cdae, Integer cdmanifesto) {
		manifestoDAO.updateCDAE(cdae,cdmanifesto);
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public Manifesto findStatusForReimpressaoByCodigobarras(String codigo){
		return manifestoDAO.findStatusForReimpressaoByCodigobarras(codigo);
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public Manifesto findStatusForExtratoByCodigobarras(String codigo) {
		return manifestoDAO.findStatusForExtratoByCodigobarras(codigo);
	}

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public SqlRowSet getDadosListagem(ManifestoPlanilhaFiltro filtro) {
		return manifestoDAO.getDadosListagem(filtro);
	}
	
	/**
	 * 
	 */
	public void callSincronizarMyRoute(){
	
		Connection connection = null;
		CallableStatement cs = null;
		
		try {
			connection = IntegradorSqlUtil.getNewConnection();
			
	        cs = connection.prepareCall("{ call CALL_BPEL_MR_RE_RETROTEIRO() }");	       	       
	        cs.execute();	       	        
	        connection.commit();
		}
		catch (Exception e) {			
			e.printStackTrace();
			try {
				connection.rollback();
			}
			catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		finally {
			try {
				cs.close();
				connection.close();
			}
			catch (Exception e) {
				System.out.println("Erro ao fechar a conexção do banco.\n");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public IReport findByDescargaProdutos (EmitirdescargaprodutoFiltro filtro){
		
		Report report = new Report("RelatorioDescargaProdutos");
		List<DescargaprodutoVO> listaDescargaprodutoVO = manifestoDAO.findByDescargaProdutos(filtro);
		
		denifirVoltagemForRelatorioDescarga(listaDescargaprodutoVO);
		
		report.setDataSource(listaDescargaprodutoVO);
		return report;
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public IReport findByRelatorioPedidosManifesto(EmitirPedidosManifestoFiltro filtro) {
		Report report = new Report("RelatorioPedidosManifesto");
		List<PedidosmanifestoVO> listaPedidosmanifestoVOs = manifestoDAO.findByPedidosManifesto(filtro);
		report.setDataSource(listaPedidosmanifestoVOs);
		return report;
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public List<PedidosmanifestoVO> findByPedidosManifesto(EmitirPedidosManifestoFiltro filtro) {
		return manifestoDAO.findByPedidosManifesto(filtro);
	}
	
	/**
	 * 
	 * @param object
	 * @param whereInNotas
	 * @return
	 */
	public List<Manifesto> findAllManifestosVinculados(Notafiscalsaida notafiscalsaida, String whereInNotas){
		return manifestoDAO.findAllManifestosVinculados(notafiscalsaida,whereInNotas);
	}
	
	/**
	 * 
	 * @param manifesto 
	 * @param whereIManifesto
	 */
	public void updateManifestosFilhos(Manifesto manifesto, String whereIManifesto) {
		manifestoDAO.updateManifestosFilhos(manifesto,whereIManifesto);
	}
	
	/**
	 * 
	 * @param manifesto
	 * @param whereIn 
	 */
	public void desvincularManifestoFilho(Manifesto manifesto, String whereIn) {
		manifestoDAO.desvincularManifestoFilho(manifesto,whereIn);
	}
	
	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public Tipoentrega findTipoEntregaForManifesto(Manifesto manifesto) {
		Manifesto m = manifestoDAO.findTipoEntregaForManifesto(manifesto);
		return m.getTipoentrega();
	}
	
	/**
	 * 
	 * @param manifesto
	 * @param manifestostatus
	 * @param msg
	 * @param usuarioLogado
	 */
	public void updateManifestoFilhoStatus(Manifesto manifesto, String msg, Usuario usuario) {
		
		List<Manifesto> listaManifesto = findAllManifestosFilhos(manifesto);

		manifestoDAO.updateManifestoFilhoStatus(manifesto,usuario);
		
		if(listaManifesto!=null && !listaManifesto.isEmpty()){
			for (Manifesto manifestoFilho : listaManifesto) {
				if(manifestoFilho.getTipoentrega().equals(Tipoentrega.TRANSFERENCIA)){
					manifestohistoricoService.criarHistorico(manifestoFilho,msg,Manifestostatus.FATURADO,usuario,Tipomanifestohistorico.STATUS);
				}else if(manifestoFilho.getTipoentrega().equals(Tipoentrega.ENTREGA_CLIENTE)){
					manifestohistoricoService.criarHistorico(manifestoFilho,msg,Manifestostatus.ENTREGA_EM_ANDAMENTO,usuario,Tipomanifestohistorico.STATUS);					
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @param codigobarras
	 * @param deposito
	 * @param iMPRESSO
	 * @return
	 */
	public Manifesto findByCodigoBarrasByAgrupamento(String codigobarras,Deposito deposito, Manifestostatus manifestostatus) {
		return manifestoDAO.findByCodigoBarrasByAgrupamento(codigobarras,deposito,manifestostatus);
	}
	
	/**
	 * 
	 * @param listaManifestonotafiscal
	 * @return
	 */
	public boolean validarNotasVinculadas(List<Manifestonotafiscal> listaManifestonotafiscal, Integer cdmanifesto) {
		
		String whereIn = CollectionsUtil.listAndConcatenate(listaManifestonotafiscal, "notafiscalsaida.cdnotafiscalsaida", ",");
		List<Manifesto> manifesto = manifestoDAO.validarNotasVinculadas(whereIn,cdmanifesto);
		
		if(manifesto!=null && !manifesto.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param listaManifestonotafiscal
	 * @param cdmanifesto
	 * @param string 
	 * @return
	 */
	public boolean validarNotasVinculadasAgrupamento(List<Manifestonotafiscal> listaManifestonotafiscal, Integer cdmanifesto, String whereInManifestos) {
		
		String whereIn = CollectionsUtil.listAndConcatenate(listaManifestonotafiscal, "notafiscalsaida.cdnotafiscalsaida", ",");
		List<Manifesto> manifesto = manifestoDAO.validarNotasVinculadasAgrupamento(whereIn,cdmanifesto,whereInManifestos);
		
		if(manifesto!=null && !manifesto.isEmpty() && manifesto.size()>1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public IReport findByDescargaProdutosByLoja(EmitirdescargaprodutoFiltro filtro) {

		Report report = new Report("RelatorioDescargaProdutosByLoja");
		List<DescargaprodutoVO> listaDescargaprodutoVO = manifestoDAO.findByDescargaProdutosByLoja(filtro);
		
		denifirVoltagemForRelatorioDescarga(listaDescargaprodutoVO);
		
		report.setDataSource(listaDescargaprodutoVO);
		return report;
	}
	
	/**
	 * @param listaDescargaprodutoVO
	 */
	private void denifirVoltagemForRelatorioDescarga(List<DescargaprodutoVO> listaDescargaprodutoVO) {
		
		for (DescargaprodutoVO descargaprodutoVO : listaDescargaprodutoVO) {
			if(descargaprodutoVO!=null && descargaprodutoVO.getVoltagem()!=null){
				if(descargaprodutoVO.getVoltagem().equals("B")){
					descargaprodutoVO.setVoltagem("Bivolte");
				}else if(descargaprodutoVO.getVoltagem().equals("0")){
					descargaprodutoVO.setVoltagem("Sem Voltagem");
				}else if(descargaprodutoVO.getVoltagem().equals("1")){
					descargaprodutoVO.setVoltagem("110 V");
				}else if(descargaprodutoVO.getVoltagem().equals("2")){
					descargaprodutoVO.setVoltagem("220 V");
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @param cdmanifesto
	 * @return
	 */
	public String findCodigoByManifesto(Integer cdmanifesto) {
		Manifesto manifesto = manifestoDAO.findCodigoByManifesto(cdmanifesto);
		String codigoBarras = "";
		if(manifesto!=null){ 
			if(manifesto.getListaManifestocodigobarra()!=null && !manifesto.getListaManifestocodigobarra().isEmpty()){
				codigoBarras = manifesto.getListaManifestocodigobarra().get(0).getCodigo(); 
			}
		}
		
		return codigoBarras;
	}
	
	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public Boolean existeNotaTransferencia(Manifesto manifesto) {
		
		Manifesto manifestoAux = manifestoDAO.findForExisteNotaTransferencia(manifesto);
		
		if(manifestoAux!=null && manifestoAux.getCdmanifesto()!=null){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public List<Manifesto> findAllManifestosFilhos(Manifesto manifesto) {
		return manifestoDAO.findAllManifestosFilhos(manifesto);
	}

	/**
	 * Método responsável pelas mudanças dos Status e Informações nos Manifestos Agrupados.
	 *  
	 * @param manifesto
	 */
	public void liberarEntregaManifestoAgrupado(final Manifesto manifesto, final Long kmInicial) {
		
		transactionTemplate.execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				
				updateKmInicialManifesto(manifesto,kmInicial);
				updateManifestoStatus(manifesto, Manifestostatus.ENTREGA_EM_ANDAMENTO, "Agrupamento de Manifesto.", WmsUtil.getUsuarioLogado());
				updateDtsaidaveiculo(manifesto,WmsUtil.getUsuarioLogado());
				
				updateManifestoFilhoStatus(manifesto, "Manifestos agrupado no manifesto: "+manifesto.getCdmanifesto(), WmsUtil.getUsuarioLogado());
				
				manifestonotafiscalService.updateStatusConfirmacaoEntrega(manifesto);
				manifestonotafiscalService.updateStatusConfirmacaoEntregaFilhos(manifesto);
				
				return null;
			}
		});
		
	}
	
	/**
	 * 
	 * @param manifesto
	 */
	public void liberarEntregaManifesto(final Manifesto manifesto, final Long kmInicial) {
		
		transactionTemplate.execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				
				updateKmInicialManifesto(manifesto,kmInicial);
				updateManifestoStatus(manifesto, Manifestostatus.ENTREGA_EM_ANDAMENTO, Manifestohistorico.ENTREGA_EM_ANDAMENTO, WmsUtil.getUsuarioLogado());
				updateDtsaidaveiculo(manifesto,WmsUtil.getUsuarioLogado());
				
				manifestonotafiscalService.updateStatusConfirmacaoEntrega(manifesto);
				
				return null;
				
			}
		});
		
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public Manifesto findManifestoPaiWhenTransferencia(String codigoBarras) {
		return manifestoDAO.findManifestoPaiWhenTransferencia(codigoBarras);
	}
	
	
}