package br.com.linkcom.wms.geral.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.util.CollectionsUtil;
//import br.com.linkcom.wms.geral.bean.Custoextrafrete;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestohistorico;
import br.com.linkcom.wms.geral.bean.Manifestonotafiscal;
import br.com.linkcom.wms.geral.bean.Manifestostatus;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Tipoentrega;
import br.com.linkcom.wms.geral.bean.Tipomanifestohistorico;
import br.com.linkcom.wms.geral.bean.Transportador;
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
import br.com.linkcom.wms.util.EmailManager;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ManifestoService extends GenericService<Manifesto>{
	
	private static ManifestoService instance;
	private ManifestoDAO manifestoDAO;
	private ManifestohistoricoService manifestohistoricoService;
	private ManifestonotafiscalService manifestonotafiscalService;
	private TransactionTemplate transactionTemplate;
//	private NotafiscalsaidaService notafiscalsaidaService;
//	private ConfiguracaoService configuracaoService;
//	private CustoextrafreteService custoextrafreteService;
//	private UsuarioService usuarioService;
//	private ManifestofinanceiroService manifestofinanceiroService;
	
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
	/*public void setNotafiscalsaidaService(NotafiscalsaidaService notafiscalsaidaService) {
		this.notafiscalsaidaService = notafiscalsaidaService;
	}
	public void setConfiguracaoService(ConfiguracaoService configuracaoService) {
		this.configuracaoService = configuracaoService;
	}
	public void setCustoextrafreteService(CustoextrafreteService custoextrafreteService) {
		this.custoextrafreteService = custoextrafreteService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setManifestofinanceiroService(ManifestofinanceiroService manifestofinanceiroService) {
		this.manifestofinanceiroService = manifestofinanceiroService;
	}*/
	
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
				if(manifesto!=null && manifesto.getManifestostatus()!=null 
						&& (manifesto.getManifestostatus().equals(Manifestostatus.EM_ELABORACAO)
							|| manifesto.getManifestostatus().equals(Manifestostatus.AGUARDANDO_LIBERACAO))){
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
	public Manifesto findByCodigoBarrasForEntrada(String codigo, Deposito deposito){
		Boolean isBuscaPorNumeroManifesto = validaAcessoConsultaCodigoBarras(WmsUtil.getUsuarioLogado());
		
		return manifestoDAO.findByCodigoBarrasForEntrada(codigo, deposito, isBuscaPorNumeroManifesto);
	}

	/**
	 * 
	 * @param codigo
	 * @param deposito
	 * @return
	 */
	public Manifesto findByCodigoBarrasForEntrega(String codigo, Deposito deposito, List<Manifestostatus> status){
		Boolean isBuscaPorNumeroManifesto = validaAcessoConsultaCodigoBarras(WmsUtil.getUsuarioLogado());
		
		return manifestoDAO.findByCodigoBarrasForEntrega(codigo, deposito, isBuscaPorNumeroManifesto, status);
	}
	/**
	 * 
	 * @param codigo
	 * @param deposito
	 * @return
	 */
	public Manifesto findByStatusImpressoOuSuperior(Integer codigo, Deposito deposito){
		return manifestoDAO.findByStatusImpressoOuSuperior(codigo, deposito);
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
		Boolean isBuscaPorNumeroManifesto = validaAcessoConsultaCodigoBarras(WmsUtil.getUsuarioLogado());
		
		return manifestoDAO.findByPrestacaoConta(codigo,manifesto,isBuscaPorNumeroManifesto);
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
		Boolean isBuscaPorNumeroManifesto = validaAcessoConsultaCodigoBarras(WmsUtil.getUsuarioLogado());
		
		return manifestoDAO.findForFechamentoFinanceiro(codigo, manifesto, isBuscaPorNumeroManifesto);
	}
	
	/**
	 * Valida acesso ao caso de uso de consulta do codigo de barras do manifesto. 
	 *
	 * @param usuario the usuario
	 * @return the boolean
	 */
	public Boolean validaAcessoConsultaCodigoBarras(Usuario usuario) {
		
		return  Neo.getApplicationContext()
				.getAuthorizationManager()
				.isAuthorized("/expedicao/process/ConsultarCodigoBarras", null, usuario);
		
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
		
		Boolean isBuscaPorNumeroManifesto = validaAcessoConsultaCodigoBarras(WmsUtil.getUsuarioLogado());

		return manifestoDAO.findStatusForReimpressaoByCodigobarras(codigo, isBuscaPorNumeroManifesto);
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
	 *//*
	public SqlRowSet getDadosListagem(ManifestoPlanilhaFiltro filtro) {
		
		List<Custoextrafrete> listaCustoFrete = custoextrafreteService.findAllExigeAprovacao();
		Boolean isCustoExtraFreteHabilitado = configuracaoService.isTrue(ConfiguracaoVO.CUSTO_EXTRA_FRETE, WmsUtil.getDeposito());
		
		return manifestoDAO.getDadosListagem(filtro,listaCustoFrete,isCustoExtraFreteHabilitado);
		
	}*/
	
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
		
		if(listaManifesto!=null && !listaManifesto.isEmpty()){
			
			updateManifestoFilhoStatus(manifesto,usuario);
			criarHistoricoLiberacaoEntregaManifestosFilhos(msg, usuario, listaManifesto);
			
		}else{
			
			throw new WmsException("Não foi possível encontrar os manifestos filhos deste agrupamento, para atualizar os seus respectivos status.");
			
		}
		
	}
	
	/**
	 * Atualiza o status do manifesto de acordo com os seus respectivos tipos de entrega.
	 * Caso o manifesto seja do tipo: Entrega Cliente, o fluxo seguirá normal. O usuário ainda deverá Prestar contas.
	 * Caso o manifesto seja do tipo: Transferência, independente de seu status atual, ele será confirmado e faturado nesse método.
	 * 
	 * @param manifesto
	 * @param usuario
	 */
	public void updateManifestoFilhoStatus(Manifesto manifesto, Usuario usuario) {
		manifestoDAO.updateManifestoFilhoStatus(manifesto, usuario);
	}
	
	/**
	 * Inserindo o histórico de Liberação de Entrega nos filhos...
	 * 
	 * @param msg
	 * @param usuario
	 * @param listaManifesto
	 */
	private void criarHistoricoLiberacaoEntregaManifestosFilhos(String msg, Usuario usuario, List<Manifesto> listaManifesto) {
		
		if(listaManifesto!=null && !listaManifesto.isEmpty()){
			
			for (Manifesto manifestoFilho : listaManifesto) {
				
				if(manifestoFilho.getTipoentrega().equals(Tipoentrega.TRANSFERENCIA))
					manifestohistoricoService.criarHistorico(manifestoFilho,msg,Manifestostatus.FATURADO,usuario,Tipomanifestohistorico.STATUS);	
					
				else if(manifestoFilho.getTipoentrega().equals(Tipoentrega.ENTREGA_CLIENTE))
					manifestohistoricoService.criarHistorico(manifestoFilho,msg,Manifestostatus.ENTREGA_EM_ANDAMENTO,usuario,Tipomanifestohistorico.STATUS);					
				
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
		
		if(manifesto!=null && !manifesto.isEmpty()){
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
	 * Método que carregará os manifestos filhos de um manifesto agrupado.
	 * Caso a List<Manifesto> retorne sem registros, o sistema irá parar a atualização imediatamente.
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
				updateManifestoStatus(manifesto, Manifestostatus.ENTREGA_EM_ANDAMENTO, "Liberação de um Agrupamento de Manifesto.", WmsUtil.getUsuarioLogado());
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
				
				//criando validação para manifesto filhos...
				
				
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
		Boolean isBuscaPorNumeroManifesto = validaAcessoConsultaCodigoBarras(WmsUtil.getUsuarioLogado());
		
		return manifestoDAO.findManifestoPaiWhenTransferencia(codigoBarras, isBuscaPorNumeroManifesto);
	}
	
	/**
	 * 
	 * @param cdae
	 * @param cdmanifesto 
	 * @param integer 
	 * @return
	 */
	public String callVerificaCombonhoOpenTech(Integer cdae, Integer cddeposito) {
		
		if(cdae==null){
			throw new WmsException("Parametros Inválidos.");
		}
		
		String resposta = "";
		Connection connection = null;
		CallableStatement cs = null;
		
		try {
			
			connection = IntegradorSqlUtil.getNewConnection();
			
	        cs = connection.prepareCall("{ call PRC_VERIFICA_COMBONHO_OPENTECH (?,?,?) }");
	        
	       	cs.setInt(1, cdae);	        
	       	cs.setInt(2, cddeposito);
	       	cs.registerOutParameter(3,Types.VARCHAR);
	        	       
	        cs.execute();
	        
	        resposta = cs.getString(3);	        
	        
	        if (!resposta.isEmpty()){
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
	 * @param integer 
	 * @return
	 */
	public String buscaTagArquivoCte(String chave, String arquivo) {
		
		if(arquivo==null){
			throw new WmsException("Parametros Inválidos.");
		}
		
		String resposta = "";
		Connection connection = null;
		CallableStatement cs = null;
		
		try {
			
			connection = IntegradorSqlUtil.getNewConnection();
			
			cs = connection.prepareCall("{ ? = call BUSCA_TAG_XML (?,?) }");
			
			cs.setString(2, chave);	        
			cs.setString(3, arquivo);
			cs.registerOutParameter(1,Types.VARCHAR);
			
			cs.execute();
			
			resposta = cs.getString(1);	        
			
			if (!resposta.isEmpty()){
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
	 * @param codigo 
	 * @return
	 */
	public boolean validaManifestoVincualdoEmAgrupamento(String codigo) {
		
		Manifesto manifesto = findManifestoFilhoByCodigoBarras(codigo);
		
		if(manifesto!=null){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public Manifesto findManifestoFilhoByCodigoBarras(String codigo) {
		
		Boolean isBuscaPorNumeroManifesto = validaAcessoConsultaCodigoBarras(WmsUtil.getUsuarioLogado());
		
		return manifestoDAO.findManifestoFilhoByCodigoBarras(codigo, isBuscaPorNumeroManifesto);
	}
	
		
	/**
	 * 
	 * @param manifesto
	 */
	public void atualizarInfoConfirmacaoTransito(Manifesto manifesto) {
		manifestoDAO.atualizarInfoConfirmacaoTransito(manifesto);
	}

	/**
	 * Valida se o manifesto terá seu status atualizado para Disponivel de Re-Faturamento.
	 * 
	 * @author Filipe Santos
	 * @param recebimento
	 *//*
	public void atualizaManifestoByRecebimento(final Recebimento recebimento) {
		
		List<Manifesto> listaManifesto = findByRecebimento(recebimento);
		
		if(listaManifesto!=null && !listaManifesto.isEmpty()){
			
			for (Manifesto manifesto : listaManifesto) {
				
				List<Manifestonotafiscal> listaNotas = manifestonotafiscalService.findByManifesto(manifesto);
				List<Manifestonotafiscal> listaNotasRecebidas = manifestonotafiscalService.findNotasRecebidasByManifesto(manifesto);
				
				if(listaNotas.size() == listaNotasRecebidas.size()){
					
					String msg = "Alteração de Status. O manifesto foi totalmente recebido.";
					
					//Atualizando o manifesto...
					updateManifestoStatus(manifesto, Manifestostatus.DISPONIVEL_REFATORAMENTO, msg, WmsUtil.getUsuarioLogado());
					
				}
				
			}
			
		}
		
	}*/
	
	/**
	 * 
	 * @param recebimento
	 * @return
	 */
	public List<Manifesto> findByRecebimento(Recebimento recebimento) {
		return manifestoDAO.findByRecebimento(recebimento);
	}
	
	/**
	 * 
	 * @param codigo
	 * @param deposito
	 * @return
	 */
	public Manifesto findManifestoStatusByManifesto(Integer cdmanifesto){
		return manifestoDAO.findManifestoStatusByManifesto(cdmanifesto);
	}
	
	
	@SuppressWarnings("rawtypes")
	public List recuperaUFOrigemDestinoManifesto(Manifesto manifesto) {
		return manifestoDAO.recuperaUFOrigemDestinoManifesto(manifesto);
	}
	
	/**
	 * 
	 * @param transportador
	 * @param depositos
	 * @return
	 */
	public List<Manifesto> findByTransportadorAndDepositos(Transportador transportador, String depositos) {
		return manifestoDAO.findByTransportadorAndDepositos(transportador,depositos);
	}
	
	public Manifesto findManifestoWithDeposito(Integer cdManifesto) {
		return manifestoDAO.findManifestoWithDeposito(cdManifesto) ;
	}
	
	
	/**
	 * Envia email solicitando autorização para manifestar os pedidos sem frete.
	 *
	 * @param manifesto the manifesto
	 */
/*	public void enviarEmailPedidosSemFrete(Manifesto manifesto) {
		
		String valorPrevistoFrete = null;
		List<Manifestonotafiscal> valoresPrevisaoManifestoNota = null;
		
		// calcula a previsão para poder enviar ao usuario do email o valor previsto de frete. 
		String retorno = manifestofinanceiroService.calcularPrevisao(manifesto);

		if (StringUtils.isNotBlank(retorno) && !"OK".equalsIgnoreCase(retorno)){
			valorPrevistoFrete = "Não foi possível calcular o frete para esse pedido";
		}else{
			valoresPrevisaoManifestoNota =  manifestonotafiscalService.findValorPrevisaoByManifesto(manifesto);
		}
		
		for (Manifestonotafiscal manifestonotafiscal : manifesto.getListaManifestonotafiscal()) {
			if (valoresPrevisaoManifestoNota != null && !valoresPrevisaoManifestoNota.isEmpty()){
				Manifestonotafiscal manifestoNotaPrevisao = (Manifestonotafiscal) CollectionUtils.find(valoresPrevisaoManifestoNota, 
						new BeanPropertyValueEqualsPredicate("cdmanifestonotafiscal",manifestonotafiscal.getCdmanifestonotafiscal()));
				
				if (manifestoNotaPrevisao != null){
					manifestonotafiscal.setValorprevisao(manifestoNotaPrevisao.getValorprevisao());
				}
			}
			
			if (manifestonotafiscal.getToken() == null){
				manifestonotafiscal.setToken(new Random().nextInt(1000000));
				manifestonotafiscalService.saveOrUpdate(manifestonotafiscal);
			}
		}
		
		manifesto.setDeposito(DepositoService.getInstance().get(manifesto.getDeposito().getCddeposito()));
		
		StringBuilder corpoEmail = criaCorpoEmailNotaSemFrete(manifesto, valorPrevistoFrete);
		
		List<Usuario> aprovadores = usuarioService.findAprovadoresNotaClienteSemFrete(manifesto.getDeposito().getCddeposito());
		
		for (Usuario usuario : aprovadores) {
			try {
				enviarEmail(manifesto.getCdmanifesto(), corpoEmail, usuario.getEmail());
			} catch (Exception e) {
				throw new WmsException(e);
			}
		}
	}*/
	
	/**
	 * Cria corpo email para autorização de manifesto com nota sem frete.
	 *
	 * @param manifesto the manifesto
	 * @param valorPrevistoFrete 
	 * @return the string builder
	 */
/*	public StringBuilder criaCorpoEmailNotaSemFrete(Manifesto manifesto, String valorPrevistoFrete) {
		
		StringBuilder conteudo = new StringBuilder();
		final SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
		
		conteudo.append("<table border=2 cellspacing=3 cellpadding=3 bordercolor=\"black\"> ");
		conteudo.append("<tr><td><h2><b>Autorização para inclusão de nota sem pagamento de frete.</b></h2></td></tr>");
		conteudo.append("<tr><td>");
		
		conteudo.append("<p><b>Manifesto:</b> ").append(manifesto.getCdmanifesto()).append("</p>");
		conteudo.append("<p><b>Transportador:</b> ").append(manifesto.getTransportador().getNome()).append("</p>");
		conteudo.append("<p><b>Solicitante:</b> ").append(WmsUtil.getUsuarioLogado().getNome()).append("</p>");
		conteudo.append("<p><b>E-mail Solicitante:</b> ").append(WmsUtil.getUsuarioLogado().getEmail() == null ? 
				StringUtils.EMPTY : WmsUtil.getUsuarioLogado().getEmail()).append("</p>");
		conteudo.append("<p><b>Depósito:</b> ").append(manifesto.getDeposito().getNome()).append("</p>");
		conteudo.append("<p><b>Pedidos:</b></p> ");
		conteudo.append("<p align =\"center\"><table border=1 cellspacing=1 cellpadding=1 bordercolor=\"black\"> ");
		conteudo.append("<tr><th>Pedido/Loja</th><th>Valor do Pedido</th> ");
		conteudo.append("<th>Num. Nota</th><th>Série</th><th>Dt.Emissão</th>");
		conteudo.append("<th>Valor Previsto p/ Frete</th><th>Senha de Aprovação</th></tr> ");
		
		for (Manifestonotafiscal manifestonotafiscal : manifesto.getListaManifestonotafiscal()) {
			
			if (!manifestonotafiscal.getExisteFreteClienteNota()){
				conteudo.append("<tr><td>")
						.append(manifestonotafiscal.getNotafiscalsaida().getNumeropedido())
						.append("/")
						.append(manifestonotafiscal.getNotafiscalsaida().getLojapedido()).append("</td>");
				
				conteudo.append("<td> ").append(manifestonotafiscal.getNotafiscalsaida().getVlrtotalnf().toString()).append("</td>");
				
				conteudo.append("<td> ").append(manifestonotafiscal.getNotafiscalsaida().getNumero().toString()).append("</td>");
				conteudo.append("<td> ").append(manifestonotafiscal.getNotafiscalsaida().getSerie()).append("</td>");
				conteudo.append("<td> ").append(sdf.format(manifestonotafiscal.getNotafiscalsaida().getDtemissao())).append("</td>");
				
				conteudo.append("<td> ")
						.append(StringUtils.isBlank(valorPrevistoFrete)?  
													manifestonotafiscal.getValorprevisao().toString():
													valorPrevistoFrete)
						.append("</td>");
				
				conteudo.append("<td> ").append(manifestonotafiscal.getToken()).append("</td></tr>");
			}
			
		}
		
		conteudo.append("</table></p></tr></td><br>");
		conteudo.append("</table>");
		
		return conteudo;
	}*/
	
	/**
	 * Enviar email.
	 *
	 * @param bean the bean
	 * @param destinatario the destinatario
	 * @throws Exception the exception
	 */
	public void enviarEmail(Integer cdManifesto, StringBuilder conteudo, String destinatario) throws Exception {

		EmailManager emailManager = new EmailManager();
		
		log.info("Enviando email para o destintario "+destinatario+" da autorização: " + cdManifesto);
		
		emailManager.setFrom("autorizacao@maquinadevendas.com.br");
		/*busca os emails dos aprovadores*/
		emailManager.setTo(destinatario);
		
		emailManager.setSubject("Autorização para inclusão de nota sem pagamento de frete no manifesto " + cdManifesto);
		
		emailManager.addHtmlText( conteudo.toString() );
		emailManager.sendMessage();
	}
	public SqlRowSet findByManifestoToExportacao(Manifesto manifesto) {
		return manifestoDAO.findByManifestoToExportacao(manifesto);
		
	}
	
	public String reagendaPedidoSite(Integer codigo) {
		Connection connection = null;
		CallableStatement cs = null;
		
		try {
			connection = IntegradorSqlUtil.getNewConnection();
			
	        cs = (CallableStatement) connection.prepareCall("{ call PRC_REAGENDA_PEDIDO_SITE(?,?) }");
	        
	        cs.registerOutParameter(2,Types.VARCHAR);
	        
	        if(codigo!=null){
	        	cs.setInt(1,codigo);
	        }else{
	        	cs.setNull(1, Types.INTEGER);
	        }	      
	        
	        cs.execute();
	        String resposta = cs.getString(2);	        
        
        	connection.commit();
        	return resposta;
	        
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			}
			catch (SQLException e2) {
				e2.printStackTrace();
			}
			return "Erro ao finalizar!";
			
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
	
	public IReport createReportManifestoConsolidacao(Manifesto manifesto) {
		Report report = new Report("RelatorioImpressaoConsolidacaoManifesto");
		List<ManifestoVO> list = manifestoDAO.findForReportConsolidacao(manifesto);
		report.setDataSource(list);
		return report;
	}	
	
	
	/**
	 * Validar transbordo notas.
	 *
	 * @param manifesto the manifesto
	 * @return the boolean
	 */
	public Boolean validarTransbordoNotas(Manifesto manifesto) {
		Long qtdeNotasTransbordo = manifestoDAO.recuperaQuantNotasTransbordoManifesto(manifesto);
		return qtdeNotasTransbordo != null && qtdeNotasTransbordo > NumberUtils.LONG_ZERO;
	}
}