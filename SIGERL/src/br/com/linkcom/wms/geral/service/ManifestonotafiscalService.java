package br.com.linkcom.wms.geral.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestonotafiscal;
import br.com.linkcom.wms.geral.bean.Statusconfirmacaoentrega;
import br.com.linkcom.wms.geral.bean.vo.ManifestoTransbordoVO;
import br.com.linkcom.wms.geral.dao.ManifestonotafiscalDAO;
import br.com.linkcom.wms.sincronizador.IntegradorSqlUtil;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ManifestonotafiscalService extends GenericService<Manifestonotafiscal>{

	private static ManifestonotafiscalService instance;
	private ManifestonotafiscalDAO manifestonotafiscalDAO;

	public void setManifestonotafiscalDAO(ManifestonotafiscalDAO manifestonotafiscalDAO) {
		this.manifestonotafiscalDAO = manifestonotafiscalDAO;
	}
	
	public static ManifestonotafiscalService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(ManifestonotafiscalService.class);
		}
		return instance;
	}
	
	/**
	 *  
	 * @param cddeposito
	 * @param cdcarregamento
	 * @param chavenfe
	 * @param numeronfe
	 * @param serienfe
	 * @return
	 */
	public boolean findNotasSaidaByProcedure(Integer cddeposito, String chavenfe, Long numeronfe, Date dataemissao, Long codigoerp, String serienfe) {

		Connection connection = null;
		CallableStatement cs = null;
		
		try {
			connection = IntegradorSqlUtil.getNewConnection();
			
	        cs = (CallableStatement) connection.prepareCall("{ call PRC_BUSCA_NOTAS_MVLOJAS(?,?,?,?,?,?,?) }");
	        
	        cs.registerOutParameter(1,Types.VARCHAR);
	        
	        if(cddeposito!=null){
	        	cs.setInt(2,cddeposito);
	        }else{
	        	cs.setNull(2, Types.INTEGER);
	        }	      
	        
	        if(chavenfe!=null){
	        	cs.setString(3, chavenfe);
	        }else{
	        	cs.setNull(3, Types.VARCHAR);
	        }
	        
	        if(numeronfe!=null){
	        	cs.setLong(4, numeronfe);
	        }else{
	        	cs.setNull(4, Types.NUMERIC);
	        }
	        
	        if(dataemissao!=null){
	        	cs.setDate(5, dataemissao);
	        }else{
	        	cs.setNull(5, Types.DATE);
	        }	        
	        
	        if(codigoerp!=null){
	        	cs.setLong(6, codigoerp);
	        }else {
				cs.setNull(6, Types.NUMERIC);
			}
	        
	        if(serienfe!=null){
	        	cs.setString(7, serienfe);
	        }else{
	        	cs.setNull(7, Types.VARCHAR);
	        }
	        
	        cs.execute();
	        String resposta = cs.getString(1);	        
	        
	        if(resposta.equals("OK")){
	        	connection.commit();
	        	Thread.sleep(4000);
	        	return true;
	        }else if (resposta.equals("OK-ST")){
	        	connection.commit();
				return true;
	        }else{
	        	connection.rollback();
	        	return false;
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
			//durante a fase de implantação vai retornar true, depois tem que voltar pra false
			return true;
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
	 * @param manifesto
	 * @return
	 */
	public List<Manifestonotafiscal> findByManifesto(Manifesto manifesto) {
		return manifestonotafiscalDAO.findByManifesto(manifesto);
	}

	/**
	 * 
	 * @param manifesto
	 */
	public void updateStatusConfirmacaoEntrega(Manifesto manifesto){
		manifestonotafiscalDAO.updateStatusConfirmacaoEntrega(manifesto);
	}

	/**
	 * 
	 * @param manifesto
	 */
	public void deleteByManifesto(String whereIn) {
		manifestonotafiscalDAO.deleteByManifesto(whereIn);
	}

	/**
	 * 
	 * @param manifestonotafiscal
	 * @param statusconfirmacaoentrega
	 */
	public void updateStatusConfirmacaoEntrega(Manifestonotafiscal manifestonotafiscal,	Statusconfirmacaoentrega statusconfirmacaoentrega) {
		manifestonotafiscalDAO.updateStatusConfirmacaoEntrega(manifestonotafiscal, statusconfirmacaoentrega);
	}

	/**
	 * 
	 * @param manifestonotafiscal
	 */
	public void updateDepositoTransbordo(Manifestonotafiscal manifestonotafiscal) {
		manifestonotafiscalDAO.updateDepositoTransbordo(manifestonotafiscal);
	}

	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public List<Manifestonotafiscal> findByManifestoCodigoBarras(String codigo) {
		return manifestonotafiscalDAO.findByManifestoCodigoBarras(codigo);
	}
	
	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public List<Manifestonotafiscal> findAllNotasSemPracas(Manifesto manifesto) {
		
		List<Manifestonotafiscal> listaManifestonotafiscal =  manifestonotafiscalDAO.findAllNotasSemPracas(manifesto);
		List<Manifestonotafiscal> listaNotasSemPraca = new ArrayList<Manifestonotafiscal>();
		
		if(listaManifestonotafiscal!=null && !listaManifestonotafiscal.isEmpty()){
			for (Manifestonotafiscal mnf : listaManifestonotafiscal) {
				if(mnf.getNotafiscalsaida()!=null){
					if(mnf.getNotafiscalsaida().getPraca() == null || mnf.getNotafiscalsaida().getPraca().getCdpraca() == null){
						listaNotasSemPraca.add(mnf);
						continue;
					}
					else if(mnf.getNotafiscalsaida().getCep()==null || mnf.getNotafiscalsaida().getCep().getValue().equals("0")){
						listaNotasSemPraca.add(mnf);
						continue;						
					}
				}
			}
		}
		
		return listaNotasSemPraca;
	}
	
	/**
	 * 
	 * @param manifesto
	 * @param whereIn
	 * @return
	 */
	public List<Manifestonotafiscal> findAllbyManifesto(Manifesto manifesto, String whereIn) {
		
		 List<Manifestonotafiscal> listaManifestonotafiscal =  manifestonotafiscalDAO.findAllbyManifesto(manifesto,whereIn);
		 
		 for (Manifestonotafiscal manifestonotafiscal: listaManifestonotafiscal) {
			 manifestonotafiscal.setUsuario(WmsUtil.getUsuarioLogado());			
		 }
		 
		 return listaManifestonotafiscal; 
	}

	/**
	 * 
	 * @param manifesto
	 */
	public void updateStatusConfirmacaoEntregaFilhos(Manifesto manifesto) {
		manifestonotafiscalDAO.updateStatusConfirmacaoEntregaFilhos(manifesto);
	}	
	

	public List<Manifestonotafiscal> findTokenByManifesto(Manifesto manifesto) {
		return manifestonotafiscalDAO.findTokenByManifesto(manifesto);
	}
	
	/**
	 *  
	 * @param cddeposito
	 * @param cdcarregamento
	 * @param chavenfe
	 * @param numeronfe
	 * @param serienfe
	 * @param tiponotafiscal 
	 * @return
	 */
	public boolean findNotasSaidaByProcedure(Integer cddeposito, String chavenfe, Long numeronfe, Date dataemissao, Long codigoerp, String serienfe, Integer tiponotafiscal) {

		Connection connection = null;
		CallableStatement cs = null;
		
		try {
			connection = IntegradorSqlUtil.getNewConnection();
			
			//PROCEDURE ORIGINAL: PRC_BUSCA_NOTAS_MVLOJAS, TESTANDO A NOVA CHAMADA COM O TIPO DA NF.
	        cs = (CallableStatement) connection.prepareCall("{ call PRC_BUSCA_NOTAS_MVLOJAS(?,?,?,?,?,?,?,?) }");
	        
	        cs.registerOutParameter(1,Types.VARCHAR);
	        
	        if(cddeposito!=null){
	        	cs.setInt(2,cddeposito);
	        }else{
	        	cs.setNull(2, Types.INTEGER);
	        }	      
	        
	        if(StringUtils.isNotBlank(chavenfe)){
	        	cs.setString(3, chavenfe);
	        }else{
	        	cs.setNull(3, Types.VARCHAR);
	        }
	        
	        if(numeronfe!=null){
	        	cs.setLong(4, numeronfe);
	        }else{
	        	cs.setNull(4, Types.NUMERIC);
	        }
	        
	        if(dataemissao!=null){
	        	cs.setDate(5, dataemissao);
	        }else{
	        	cs.setNull(5, Types.DATE);
	        }	        
	        
	        if(codigoerp!=null){
	        	cs.setLong(6, codigoerp);
	        }else {
				cs.setNull(6, Types.NUMERIC);
			}
	        
	        if(StringUtils.isNotBlank(serienfe)){
	        	cs.setString(7, serienfe);
	        }else{
	        	cs.setNull(7, Types.VARCHAR);
	        }
	        
	        if(tiponotafiscal!=null){
	        	cs.setInt(8, tiponotafiscal);
	        }else{
	        	cs.setNull(8, Types.INTEGER);
	        }
	        
	        cs.execute();
	        String resposta = cs.getString(1);	        
	        
	        if(resposta.equals("OK") || resposta.contains("normal, successful completion")){
	        	connection.commit();
	        	Thread.sleep(4000);
	        	return true;
	        }else if (resposta.equals("OK-ST")){
	        	connection.commit();
				return true;
	        }else{
	        	connection.rollback();
	        	log.error("Erro ao executar PRC_BUSCA_NOTAS_MVLOJAS: " + resposta);
	        	return false;
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
			
			return Boolean.FALSE;
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
	 * Incluir transbordo notas.
	 *
	 * @param listaNotasTransbordo the lista notas transbordo
	 */
	public void incluirTransbordoNotas(List<ManifestoTransbordoVO> listaNotasTransbordo) {
		for (ManifestoTransbordoVO manifestoTransbordoVO : listaNotasTransbordo) {
			Manifestonotafiscal manifestonotafiscal = get(manifestoTransbordoVO.getCdManifestoNotaFiscal());
			
			if (manifestoTransbordoVO.getDepositoTransbordo() != null 
					&& manifestoTransbordoVO.getDepositoTransbordo().getCddeposito() != null){
				manifestonotafiscal.setTemDepositoTransbordo(Boolean.TRUE);
				manifestonotafiscal.setDepositotransbordo(manifestoTransbordoVO.getDepositoTransbordo());
			}else{
				manifestonotafiscal.setTemDepositoTransbordo(Boolean.FALSE);
				manifestonotafiscal.setDepositotransbordo(null);
			}
			
			saveOrUpdate(manifestonotafiscal);
		}
		
	}
	
}
