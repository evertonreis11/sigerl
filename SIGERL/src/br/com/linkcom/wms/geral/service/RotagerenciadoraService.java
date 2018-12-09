package br.com.linkcom.wms.geral.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Rotagerenciadora;
import br.com.linkcom.wms.geral.dao.RotagerenciadoraDAO;
import br.com.linkcom.wms.sincronizador.IntegradorSqlUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class RotagerenciadoraService extends GenericService<Rotagerenciadora>{

	private RotagerenciadoraDAO rotagerenciadoraDAO;
	
	public void setRotagerenciadoraDAO(RotagerenciadoraDAO rotagerenciadoraDAO) {
		this.rotagerenciadoraDAO = rotagerenciadoraDAO;
	}

	/**
	 * 
	 * @param deposito
	 * @return
	 */
	public String callAtualizaRotaGerenciadora(Deposito deposito){
		
		Connection connection = null;
		CallableStatement cs = null;
		String retorno = null;
		
		try {
			connection = IntegradorSqlUtil.getNewConnection();
			
	        cs = (CallableStatement) connection.prepareCall("{ call PRC_ATUALIZAROTAGERENCIADORA(?,?) }");
	        
	        if(deposito!=null && deposito.getCddeposito()!=null){
	        	cs.setInt(1,deposito.getCddeposito());
	        }else{
	        	cs.setNull(1,Types.INTEGER);
	        }	      
	        
	        cs.registerOutParameter(2,Types.VARCHAR);
	        
	        cs.execute();
	        String resposta = cs.getString(2);	        
	        retorno = resposta;
	        
	        if(resposta.equals("OK")){
	        	connection.commit();
	        }else{
	        	connection.rollback();
	        }
		}
		catch (Exception e) {
			retorno = "Erro durante a atualização. Por favor, tente novamente.";
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
	 * @return
	 */
	public List<Rotagerenciadora> findAllByDepositoLogado() {
		return rotagerenciadoraDAO.findAllByDepositoLogado();
	}  
	
	/**
	 * 
	 * @return
	 */
	public List<Rotagerenciadora> findByAutocomplete(String param){
		return rotagerenciadoraDAO.findByAutocomplete(param);
	}
	
	
}
