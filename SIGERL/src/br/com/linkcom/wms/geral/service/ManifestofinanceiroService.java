package br.com.linkcom.wms.geral.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestofinanceiro;
import br.com.linkcom.wms.geral.bean.vo.ExtratoFinanceiroVO;
import br.com.linkcom.wms.geral.dao.ManifestofinanceiroDAO;
import br.com.linkcom.wms.sincronizador.IntegradorSqlUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ManifestofinanceiroService extends GenericService<Manifestofinanceiro>{

	private ManifestofinanceiroDAO manifestofinanceiroDAO;
	
	public void setManifestofinanceiroDAO(ManifestofinanceiroDAO manifestofinanceiroDAO) {
		this.manifestofinanceiroDAO = manifestofinanceiroDAO;
	}

	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public Manifestofinanceiro findByManifesto(Manifesto manifesto) {
		return manifestofinanceiroDAO.findByManifesto(manifesto);
	}

	/**
	 * 
	 * @param manifesto
	 */
	public String callCalculaFrete(Manifesto manifesto) {
		
		String resposta = null;
		
		if(manifesto==null || manifesto.getCdmanifesto() == null){
			throw new WmsException("Parâmetros Inválidos. É necessário selecionar um manifesto para realizar o calculo do frete.");
		}
		
		Connection connection = null;
		CallableStatement cs = null;
		
		try {
			connection = IntegradorSqlUtil.getNewConnection();
			
	        cs = connection.prepareCall("{ call CALCULAFRETE_NEW (?,?) }");
	        
	        if(manifesto!=null && manifesto.getCdmanifesto()!=null){
	        	cs.setInt(1,manifesto.getCdmanifesto());
	        }else{
	        	cs.setNull(1, Types.INTEGER);
	        }
	        
	        cs.registerOutParameter(2,Types.VARCHAR);
	        
	        cs.execute();
	        resposta = cs.getString(2);	        
	        
	        if (resposta!=null && !resposta.isEmpty()){
	        	System.out.println(resposta);
	        	connection.rollback();
	        }else{
	        	connection.commit();
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
	 * @param manifesto
	 * @return
	 */
	public IReport findByRelatorioExtratoFinanceiro(Manifesto manifesto){
		
		Report report = new Report("RelatorioExtratoFinanceiro");
		
		List<ExtratoFinanceiroVO> listaExtratoFinanceiroVO = manifestofinanceiroDAO.findByRelatorioExtratoFinanceiro(manifesto);
		
		Integer totalPresvistas = 0;
		Integer totalEntregas = 0;
		Integer totalRetorno = 0;
		Integer totalExclusao = 0;
		
		for (ExtratoFinanceiroVO extratoFinanceiroVO : listaExtratoFinanceiroVO) {
			totalEntregas += extratoFinanceiroVO.getEntregaRotaConfirmada() == null ? 0 : Integer.parseInt(extratoFinanceiroVO.getEntregaRotaConfirmada());
			totalRetorno += extratoFinanceiroVO.getEntregaRotaRetorno() == null ? 0 : Integer.parseInt(extratoFinanceiroVO.getEntregaRotaRetorno());
			totalExclusao += extratoFinanceiroVO.getEntregaRotaExcluidas() == null ? 0 : Integer.parseInt(extratoFinanceiroVO.getEntregaRotaExcluidas());
			totalPresvistas += extratoFinanceiroVO.getEntregaRotaPrevista() == null ? 0 : Integer.parseInt(extratoFinanceiroVO.getEntregaRotaPrevista());
		}
		
		report.addParameter("totalPresvistas",totalPresvistas.toString());
		report.addParameter("totalEntregas",totalEntregas.toString());
		report.addParameter("totalRetorno",totalRetorno.toString());
		report.addParameter("totalExclusao",totalExclusao.toString());
		
		report.setDataSource(listaExtratoFinanceiroVO);
		return report; 
	}

	/**
	 * 
	 * @param manifesto
	 * @param valorMonetario
	 */
	public void updateAcrescimo(Manifesto manifesto, Money valorMonetario) {
		manifestofinanceiroDAO.updateAcrescimo(manifesto,valorMonetario);
	}
	
	/**
	 * 
	 * @param manifesto
	 * @param valorMonetario
	 */
	public void updateDesconto(Manifesto manifesto, Money valorMonetario) {
		manifestofinanceiroDAO.updateDesconto(manifesto,valorMonetario);
	}

	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public IReport findByRelatorioPrestacaoConta(Manifesto manifesto) {
		
		Report report = new Report("RelatorioPrestacaoContas");
		List<ExtratoFinanceiroVO> listaExtratoFinanceiroVO = manifestofinanceiroDAO.findByRelatorioExtratoFinanceiro(manifesto);
		
		Integer totalPresvistas = 0;
		Integer totalEntregas = 0;
		Integer totalRetorno = 0;
		Integer totalExclusao = 0;
		
		for (ExtratoFinanceiroVO extratoFinanceiroVO : listaExtratoFinanceiroVO) {
			totalEntregas += extratoFinanceiroVO.getEntregaRotaConfirmada() == null ? 0 : Integer.parseInt(extratoFinanceiroVO.getEntregaRotaConfirmada());
			totalRetorno += extratoFinanceiroVO.getEntregaRotaRetorno() == null ? 0 : Integer.parseInt(extratoFinanceiroVO.getEntregaRotaRetorno());
			totalExclusao += extratoFinanceiroVO.getEntregaRotaExcluidas() == null ? 0 : Integer.parseInt(extratoFinanceiroVO.getEntregaRotaExcluidas());
			totalPresvistas += extratoFinanceiroVO.getEntregaRotaPrevista() == null ? 0 : Integer.parseInt(extratoFinanceiroVO.getEntregaRotaPrevista());
		}
		
		report.addParameter("totalPresvistas",totalPresvistas.toString());
		report.addParameter("totalEntregas",totalEntregas.toString());
		report.addParameter("totalRetorno",totalRetorno.toString());
		report.addParameter("totalExclusao",totalExclusao.toString());
		
		report.setDataSource(listaExtratoFinanceiroVO);
		return report; 
	}
	
	public void deleteByManifesto(String whereIn) {
		manifestofinanceiroDAO.deleteByManifesto(whereIn);
	}
	
}
