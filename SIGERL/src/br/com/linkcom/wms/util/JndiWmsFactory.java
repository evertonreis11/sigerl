package br.com.linkcom.wms.util;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiTemplate;

import br.com.linkcom.wms.geral.bean.Empresa;

/**
 * Classe para gerenciar a conexão com o banco de acordo url.
 * 
 * @since 22/04/2008
 * @author Pedro Gonçalves
 *
 */
public class JndiWmsFactory implements FactoryBean {
	private Log log = LogFactory.getLog(JndiWmsFactory.class);
	JndiTemplate jndiTemplate = new JndiTemplate();
	
	private DataSource getCurrentDataSource() {
		
		try {
			return (DataSource) jndiTemplate.lookup(getJndiName(), getObjectType());
		} catch (NamingException e) {
			throw new RuntimeException("Erro ao conseguir conexão com o banco de dados!", e);
		}
	}

	private String getJndiName() {
		return "java:/wmsre_OracleDS";
	}

	public Object getObject() throws Exception {
		DataSource source = new DataSource(){

		   public Connection getConnection() throws SQLException {
		    return getCurrentDataSource().getConnection();
		   }

		   public Connection getConnection(String username, String password) throws SQLException {
		    return getCurrentDataSource().getConnection(username, password);
		   }

		   public PrintWriter getLogWriter() throws SQLException {
		    return getCurrentDataSource().getLogWriter();
		   }

		   public int getLoginTimeout() throws SQLException {
		    return getCurrentDataSource().getLoginTimeout();
		   }

		   public void setLogWriter(PrintWriter out) throws SQLException {
		    getCurrentDataSource().setLogWriter(out);
		    
		   }

		   public void setLoginTimeout(int seconds) throws SQLException {
		    getCurrentDataSource().setLoginTimeout(seconds);
		   }

		   public boolean isWrapperFor(Class<?> iface) throws SQLException {
		    return getCurrentDataSource().isWrapperFor(iface);
		   }

		   public <T> T unwrap(Class<T> iface) throws SQLException {
		    return getCurrentDataSource().unwrap(iface);
		   }
		   
		   @Override
		   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		    return getCurrentDataSource().getParentLogger();
		   }
		   
		};
		  
		return source;
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return DataSource.class;
	}

	public boolean isSingleton() {
		return true;
	}

	/**
	 * 
	 * 
	 * @return
	 * @throws NamingException 
	 */
	public static JdbcTemplate getJdbcTemplateMVLojas(Empresa empresa) throws NamingException{
		
		DataSource dataSource = null;
		Context context = new InitialContext();
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String jndiBD = "java:/mvrelo_OracleDS";
		
		if(empresa!=null && WmsUtil.isUrlProducao()){
			if(empresa.getCdempresa() == 5)
				jndiBD = "java:/mvre_OracleDS";	
			else if(empresa.getCdempresa() == 1)
				jndiBD = "java:/mvcs_OracleDS";
			else if(empresa.getCdempresa() == 3)
				jndiBD = "java:/mvin_OracleDS";
		}
		
		try {
			context = new InitialContext();
			dataSource = (DataSource) context.lookup(jndiBD);
			jdbcTemplate.setDataSource(dataSource);
		} catch (Exception e) {
			throw new WmsException(e);
		}
		
		return jdbcTemplate;
	}
	
}
