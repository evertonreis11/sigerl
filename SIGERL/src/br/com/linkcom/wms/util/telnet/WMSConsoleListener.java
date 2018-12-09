package br.com.linkcom.wms.util.telnet;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import br.com.linkcom.neo.bean.annotation.Bean;
import net.wimpi.telnetd.TelnetD;

@Bean
public class WMSConsoleListener implements ApplicationListener {

	private TelnetD daemon;
	private Boolean status = null;
	
	public void onApplicationEvent(ApplicationEvent event) {
		if(status==null && event instanceof ContextRefreshedEvent) {
			Properties properties = new Properties();
			InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("wmsconsole.properties");
			try {
				properties.load(resourceAsStream);

				//ATEN��O:
				//Se voc� estiver vendo um erro de classe n�o encontrada para o c�digo abaixo,
				//Certifique-se que existe uma User Library chamada JBOSS_LIB e que ela cont�m
				//o arquivo C:\java\jboss\lib\jboss-jmx.jar
				//Se esta Library n�o existe crie ela e adicione o arquivo acima

				/* Comentado para solucionar erro ao buscar servidor.
				 * Everton Reis - 15/02/2016

				    Copiando a configura��o de BindAddress do JBoss para o Telnet,
				    Para que ambos sejam ouvintes do mesmo IP.
				    MBeanServer mbs = MBeanServerLocator.locateJBoss();

				    Outra forma de recupera o Objeto MBeanServer - Everton Reis - 17/02/2016
				    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 

				    String jmxLocation = "java:service=Naming";
				    ObjectName objectName = new ObjectName(jmxLocation);

					String bindAddress = String.valueOf(mbs.getAttribute(objectName, "BindAddress"));
				 */

				String bindAddress = System.getProperty("jboss.bind.address");

				//O JBoss usa 0.0.0.0 para dizer que todos os IPs ser�o v�lidos.
				//Neste caso o Telnet N�O dever� receber este valor
				if (!bindAddress.equals("0.0.0.0"))
					properties.put("std.host", bindAddress);

				daemon = TelnetD.createTelnetD(properties);
				System.out.println("WMS console: iniciando...");
				daemon.start();
				status = true;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(status!=null && status==true && event instanceof ContextClosedEvent) {
			System.out.println("WMS console: parando...");
			daemon.stop();
			status = false;
		}
	}

}
