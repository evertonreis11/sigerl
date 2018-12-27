package br.com.linkcom.wms.sincronizador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import br.com.linkcom.wms.util.WmsException;

public class IntegradorSqlUtil {

	public static void closeConnection(Connection conn){
		if (conn != null){
			try {
				conn.setAutoCommit(true);
				conn.close();
			} catch (SQLException e) {
				throw new WmsException("Erro ao fechar Conexao com o banco de dados.", e);
			}
		}
	}
	public static void closeResultSet(ResultSet resultSet){
		if (resultSet != null){
			try {
				resultSet.close();
			} catch (SQLException e) {
				throw new WmsException("Erro ao fechar ResultSet.", e);
			}	
		}
	}
	public static void closePreparedStatement(PreparedStatement preparedStatement){
		
		if (preparedStatement != null){
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				throw new WmsException("Erro ao fechar sessoes com o banco de dados.", e);
			}	
		}
	}
	public static void rollbackConnection(Connection conn){
		if (conn != null){
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new WmsException("Erro ao realizar rollback.", e);
			}
		}
	}
	
	public static Connection getNewConnection(){
	    String servletContext = "wmsre";
		String jndiBD = "java:/" + servletContext + "_OracleDS";
		Connection connection = null;
		Context context;
		try {
			context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup(jndiBD);
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
		} catch (Exception e) {
			throw new WmsException(e);
		}
		
		return connection;
	}

}
