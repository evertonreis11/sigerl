package br.com.linkcom.wmsconsole;

import java.io.InputStream;
import java.util.Properties;

import br.com.linkcom.neo.core.standard.NeoStandard;

public class TelnetMain {

	public TelnetMain(String url, String usuario, String senha) throws Exception {
		//29 x 30
//		Properties propertyLog = new Properties();
//		propertyLog.setProperty("log4j.defaultInitOverride", "false");
//		propertyLog.setProperty("log4j.rootCategory", "INFO, console");
//		propertyLog.setProperty("log4j.appender.console", "org.apache.log4j.ConsoleAppender");
//		propertyLog.setProperty("log4j.appender.console.layout", "org.apache.log4j.PatternLayout");
//		propertyLog.setProperty("log4j.appender.console.layout.ConversionPattern", "%-5p %c %x - %m%n");
//		
//		PropertyConfigurator.configure(propertyLog);
		NeoStandard.configureDataSource("oracle.jdbc.OracleDriver", url, usuario, senha);
		NeoStandard.createNeoContext();
		
		Properties properties = new Properties();
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("wmsconsole.properties");
		properties.load(resourceAsStream);
		//TelnetD daemon = TelnetD.createTelnetD(properties);
		//daemon.start();
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			args = new String[3];
			args[0] = "jdbc:oracle:thin:@piedade:1521:orcl"; 
			args[1] = "wms_des"; 
			args[2] = "jogacijetu"; 
		}
		
		new TelnetMain(args[0], args[1], args[2]);
	}

}
