package br.com.linkcom.wms.geral.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import br.com.linkcom.wms.geral.bean.Departamentogerenciadora;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.sincronizador.IntegradorSqlUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class DepartamentogerenciadoraService extends GenericService<Departamentogerenciadora>{
	
	/**
	 * 
	 * @param deposito
	 * @return
	 */
	public String callBuscaProdutoGerenciadora(Deposito deposito){
		
		if(deposito==null || deposito.getCddeposito()==null){
			throw new WmsException("Parametros Inválidos. É obrigatório informar o depósito.");
		}
		
		Connection connection = null;
		CallableStatement cs = null;
		String resposta = "";
		
		try {
			connection = IntegradorSqlUtil.getNewConnection();
			
	        cs = connection.prepareCall("{ call PRC_BUSCAPRODUTOGERENCIADORA (?,?) }");
	        
	        cs.setInt(1,deposito.getCddeposito());
	        cs.registerOutParameter(2,Types.VARCHAR);
	        
	        cs.execute();
	        resposta = cs.getString(2);
	        
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
	
}
