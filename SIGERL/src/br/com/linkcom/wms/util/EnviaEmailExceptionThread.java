package br.com.linkcom.wms.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EnviaEmailExceptionThread extends Thread {

	static final Log log = LogFactory.getLog(EnviaEmailExceptionThread.class);
	private static final List<String> desenvolvedores = Arrays.asList(new String[]{"igor.costa@linkcom.com.br", "filipe.santos@linkcom.com.br"});
	private String titulo;
	private Exception exception;
	private Object[] objects;
	
	public EnviaEmailExceptionThread(String titulo, Exception exception, Object...objetos) {
		super();		
		this.titulo = titulo;
		this.exception = exception;
		this.objects = objetos;
	}


	@Override
	public void run() {
		try{
			StringWriter sw = new StringWriter();
			exception.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			StringBuilder conteudo = new StringBuilder(exception.getMessage())
			.append("<br/><br/>")
			.append(stacktrace)
			.append("<br/><br/>");
			
			for (Object object : objects) {
				  conteudo.append(ToStringBuilder.reflectionToString(object)).append("</br>");
			}
						
			EmailManager emailManager = new EmailManager();
			emailManager.setFrom("wms@linkcom.com.br");
			emailManager.setListTo(desenvolvedores);
			emailManager.setSubject("[WMS] ERRO em " + titulo);
			emailManager.addHtmlText( conteudo.toString());
			emailManager.sendMessage();
			
		}catch(Exception e){
			log.error("Erro ao enviar email:" , e);
		}
	}
	
}
