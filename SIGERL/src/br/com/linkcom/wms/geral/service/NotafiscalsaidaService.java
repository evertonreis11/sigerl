package br.com.linkcom.wms.geral.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.dao.NotafiscalsaidaDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.ManifestoFiltro;
import br.com.linkcom.wms.sincronizador.IntegradorSqlUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class NotafiscalsaidaService extends GenericService<Notafiscalsaida>{

	private NotafiscalsaidaDAO notafiscalsaidaDAO;
	
	public void setNotafiscalsaidaDAO(NotafiscalsaidaDAO notafiscalsaidaDAO) {
		this.notafiscalsaidaDAO = notafiscalsaidaDAO;
	}

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Notafiscalsaida> findForListagemPopUp(ManifestoFiltro filtro) {
		return notafiscalsaidaDAO.findForListagemPopUp(filtro);
	}

	/**
	 * 
	 * @param selectCdnotafiscalsaida
	 * @return
	 */
	public List<Notafiscalsaida> findByWhereIn(String selectCdnotafiscalsaida) {
		return notafiscalsaidaDAO.findByWhereIn(selectCdnotafiscalsaida);
	}

	/**
	 * 
	 * @param listAndConcatenate
	 */
	public void desvincularNotas(String whereIn) {
		if(whereIn == null || whereIn.isEmpty()){
			throw new WmsException("Não foi possível desvincular as notas para cancelar o manifesto.");
		}
		notafiscalsaidaDAO.desvincularNotas(whereIn);
	}

	/**
	 * 
	 * @param cdmanifesto
	 * @param deposito
	 */
	public String callDesvincularNotasTransbordo(Integer cdmanifesto, Deposito deposito){
		
		Connection connection = null;
		CallableStatement cs = null;
		String retorno = null;
		
		try {
			connection = IntegradorSqlUtil.getNewConnection();
			
	        cs = (CallableStatement) connection.prepareCall("{ call CONFIRMACAO_TRANSITO_NFS(?,?,?) }");
	        
	        cs.registerOutParameter(3,Types.VARCHAR);
	        
	        if(deposito!=null && deposito.getCddeposito()!=null){
	        	cs.setInt(2,deposito.getCddeposito());
	        }else{
	        	cs.setNull(2,Types.INTEGER);
	        }	      
	        
	        if(cdmanifesto!=null){
	        	cs.setInt(1,cdmanifesto);
	        }else{
	        	cs.setNull(1,Types.INTEGER);
	        }
	        
	        cs.execute();
	        String resposta = cs.getString(3);	        
	        retorno = resposta;
	        
	        if(resposta.equals("OK")){
	        	connection.commit();
	        }else{
	        	connection.rollback();
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
			try{
				connection.rollback();
			}catch (SQLException e2){
				e2.printStackTrace();
			}
		}
		finally{
			try{
				cs.close();
				connection.close();
			}catch (Exception e){
				System.out.println("Erro ao fechar a conexção do banco.\n");
				e.printStackTrace();
			}
		}
		
		return retorno;
	}

	/**
	 * 
	 * @param cdnotafiscalsaida
	 * @param cdpraca
	 */
	public void vincularPraca(Integer cdnotafiscalsaida, Integer cdpraca) {
		notafiscalsaidaDAO.vincularPraca(cdnotafiscalsaida,cdpraca);
	}
	
	/**
	 * 
	 * @param listaNotasDevolucao
	 */
	public void habilitarRemanifestacao(List<Notafiscalsaida> listaNotasRemanifestada) {
		notafiscalsaidaDAO.habilitarRemanifestacao(listaNotasRemanifestada);
	}

}
