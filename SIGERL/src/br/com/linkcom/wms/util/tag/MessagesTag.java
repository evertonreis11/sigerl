package br.com.linkcom.wms.util.tag;

/*
 * Neo Framework http://www.neoframework.org
 * Copyright (C) 2007 the original author or authors.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * You may obtain a copy of the license at
 *
 *     http://www.gnu.org/copyleft/lesser.html
 *
 */
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import br.com.linkcom.neo.bean.BeanDescriptor;
import br.com.linkcom.neo.controller.Message;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.exception.UsefullNeoException;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.neo.view.BaseTag;

/**
 * @author rogelgarcia
 * @since 02/02/2006
 * @version 1.1
 */
public class MessagesTag extends BaseTag {

	Set<String> printedErrors = new HashSet<String>();

	protected String titleClass = "messagetitle";

	protected String itemClass = "messageitem";

	protected String exceptionClass = "exceptionitem";

	protected String exceptionCauseClass = "causeitem";

	protected String fieldName = "fieldname";

	protected String bindErrorClass = "binderror";

	protected String validationErrorClass = "validationerror";

	protected String globalErrorclass = "globalerror";

	protected String debugClass = "debug;";

	protected String traceClass = "alert alert-warning alert-dismissable;glyphicon glyphicon-record";

	protected String infoClass = "alert alert-success alert-dismissable;glyphicon glyphicon-ok";

	protected String warnClass = "warn";

	protected String errorClass = "alert alert-danger alert-dismissable;glyphicon glyphicon-hand-right";

	@Override
	protected void doComponent() throws Exception {
		WebRequestContext requestContext = NeoWeb.getRequestContext();
		Message[] messages = requestContext.getMessages();
		BindException errors = requestContext.getBindException();

		if (errors.hasErrors() && !"true".equalsIgnoreCase(getRequest().getParameter(MultiActionController.SUPPRESS_ERRORS))) {
			getOut().println("<div class='bindblock' id='messageBlock'>");
			//getOut().println("<ul>");
			//getOut().print("<li>");

			if (Util.config.getDefaultConfigBoolean(true, "showValidationMessageTitle"))
				getOut().println("<span class=\""+titleClass+"\">Valores incorretos encontrados em '" + errors.getObjectName()+"'.</span>");
			if (errors.getGlobalErrorCount() > 0) {
				getOut().println("<ul>");
				List<?> globalErrors = errors.getGlobalErrors();
				for (Object object : globalErrors) {
					getOut().println("<li class=\"" + globalErrorclass + "\">" + ((ObjectError)object).getDefaultMessage() + "</li>");
				}
				getOut().println("</ul>");
			}
			List<?> allErrors = errors.getAllErrors();
			if (allErrors.size() > 0) {
				getOut().println("<ul>");
				for (Object object : allErrors) {
					if (object instanceof FieldError) {
						FieldError fieldError = (FieldError) object;
						// TODO MELHORAR A MENSAGEM
						BeanDescriptor<Object> beanDescriptor = NeoWeb.getApplicationContext().getBeanDescriptor(errors.getTarget());
						String field = fieldError.getField();
						field = beanDescriptor.getPropertyDescriptor(field).getDisplayName();
						if (fieldError.isBindingFailure()) {
							getOut().println("<li class=\"" + bindErrorClass + "\"> <span class=\"" + fieldName + "\">" + field + "</span> " + fieldError.getDefaultMessage() + "</li>");
						} else {
							getOut().println("<li class=\"" + validationErrorClass + "\"> <span class=\"" + fieldName + "\">" + field + "</span> " + fieldError.getDefaultMessage() + "</li>");
						}
					}
				}
				getOut().println("</ul>");
			}
			//getOut().println("</li>");
			//getOut().println("</ul>");
			getOut().println("</div>");
		}
		if (messages.length > 0) {
			if(errors.hasErrors()){

			}
			getOut().println("<div class='messageblock' id='messageBlock'>");
			for (Message message : messages) {
				String[] clazz = new String[2];
				switch (message.getType()) {
				case DEBUG: clazz = debugClass.split(";");
					break;
				case TRACE: clazz = traceClass.split(";");
					break;
				case INFO: clazz = infoClass.split(";");
					break;
				case WARN: clazz = warnClass.split(";");
					break;
				case ERROR: clazz = errorClass.split(";");
					break;
				}
				renderItem(message.getSource(), clazz);
			}
			getOut().println("</div>");
			getOut().println("<script language='javascript'>function clearMessages(){document.getElementById('messageBlock').style.display = 'none';}</script>");
		}
		requestContext.clearMessages();
	}

	private void renderItem(Object source, String[] clazz) throws IOException {
		if(source != null) {
			String convertToMessage = convertToMessage(source);
			if (Util.strings.isNotEmpty(convertToMessage)) {
				getOut().println("<div class=\"" + clazz[0] + "\">");
				getOut().println("<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">x</a> ");
				getOut().println("<span class=\""+clazz[1]+"\"></span>");
				getOut().println(convertToMessage + "</div>");
			}
		}
	}

	protected String convertToMessage(Object source) {
		if(source instanceof String){
			return source.toString();
		} else if (source instanceof Exception){
			//TODO FAZER ARVORE DE EXCECOES

			Exception exception = (Exception) source;
			StringBuilder builder = new StringBuilder();
			getResumedStack(exception, true);
			if(exception instanceof DataAccessException){
				if(exception instanceof DataIntegrityViolationException) {
					builder.append("<span class=\"" + exceptionClass + "\">Integridade de dados violada</span>");
					String message = exception.getMessage();
					printedErrors.add(message);
					builder.append("<ul><li><span class=\""+exceptionCauseClass+"\">"+message+"</span></li></ul>");
				} else
				if(exception instanceof DataRetrievalFailureException) {
					builder.append("<span class=\"" + exceptionClass + "\">Erro ao ler dados</span>");
					String message = exception.getMessage();
					printedErrors.add(message);
					builder.append("<ul><li><span class=\""+exceptionCauseClass+"\">"+message+"</span></li></ul>");
				} else
				if(exception instanceof ConcurrencyFailureException) {
					builder.append("<span class=\"" + exceptionClass + "\">Problema com uso concorrente de dados</span>");
					String message = exception.getMessage();
					printedErrors.add(message);
					builder.append("<ul><li><span class=\""+exceptionCauseClass+"\">"+message+"</span></li></ul>");
				} else {
					String message = exception.getMessage();
					printedErrors.add(message);
					builder.append("<span class=\""+exceptionClass+"\">"+message+"</span>");
				}
			} else if(exception instanceof UsefullNeoException){
				String message = exception.getMessage();
				printedErrors.add(message);
				builder.append("<span class=\""+exceptionClass+"\">"+message+"</span>");
				//printApplicationStack(builder, exception);
			} else if (exception.getClass().getName().startsWith("java.lang")){
				String message = exception.getMessage();
				printedErrors.add(message);
				builder.append("<span class=\""+exceptionClass+"\"> "+exception.getClass().getSimpleName()+": "+message+"</span>");
				//printApplicationStack(builder, exception);
			} else {
				String message = exception.getMessage();
				printedErrors.add(message);
				builder.append("<span class=\""+exceptionClass+"\">"+message+"</span>");
			}

			Throwable cause = exception;
			boolean first = true;
			while((cause = cause.getCause()) != null){
				if (first) {
					getResumedStack(cause, true);
					first = false;
				}
				if(cause instanceof DataAccessException){
					if(cause instanceof DataIntegrityViolationException) {
						builder.append("<ul><li class=\"" + exceptionCauseClass + "\"><b>Integridade de dados violada</b></li></ul>");
					}
					if(cause instanceof DataRetrievalFailureException) {
						builder.append("<ul><li class=\"" + exceptionCauseClass + "\"><b>Erro ao ler dados</b></li></ul>");
					}
					if(cause instanceof ConcurrencyFailureException) {
						builder.append("<ul><li class=\"" + exceptionCauseClass + "\"><b>Problema com uso concorrente de dados</b></li></ul>");
					}
				}

				if(cause instanceof SQLException){
					SQLException exception2 = (SQLException) cause;
					if(exception2.getNextException()!= null){
						String message = cause.getMessage();
						String message2 = exception2.getNextException().getMessage();
						if (!printedErrors.contains(message)) {
							printedErrors.add(message);
							builder.append("<ul><li class=\"" + exceptionCauseClass + "\">" + message + "</li></ul>");
						}
						if(!printedErrors.contains(message2)){
							printedErrors.add(message2);
							builder.append("<ul><li class=\"" + exceptionCauseClass + "\"><b>"+message2+"</b></li></ul>");
						}
					} else if(cause.getCause() == null){
						String message = cause.getMessage();
						if (!printedErrors.contains(message)) {
							printedErrors.add(message);
							builder.append("<ul><li class=\"" + exceptionCauseClass + "\"><b>" + message + "</b></li></ul>");
						}
					} else {
						String message = cause.getMessage();
						if (!printedErrors.contains(message)) {
							printedErrors.add(message);
							builder.append("<ul><li class=\"" + exceptionCauseClass + "\">" + message + "</li></ul>");
						}
					}
				} else if(cause instanceof UsefullNeoException){
					String message = cause.getMessage();
					if(!printedErrors.contains(message)){
						printedErrors.add(message);
						builder.append("<ul><li class=\"" + exceptionCauseClass + "\"><b>"+message.replaceAll("\n", "<BR>")+"</b></li></ul>");
					}
					//printApplicationStack(builder, exception);
				} else if (cause.getClass().getName().startsWith("java.lang")){
					String message = cause.getMessage();
					if(!printedErrors.contains(message) || message == null){
						printedErrors.add(message);
						builder.append("<ul><li class=\"" + exceptionCauseClass + "\"><b>"+cause.getClass().getSimpleName()+": "+message+"</b></li></ul>");
						//printApplicationStack(builder, cause);
					}
				} else {
					String message = cause.getMessage();
					if(!printedErrors.contains(message)){
						printedErrors.add(message);
						builder.append("<ul><li class=\"" + exceptionCauseClass + "\">"+message+"</li></ul>");
					}

				}
			}
			return builder.toString();
		}
		return source.toString();
	}

	@SuppressWarnings("unused")
	private void printApplicationStack(StringBuilder builder, Throwable cause) {
		List<StackTraceElement> elementsToPrint = getResumedStack(cause, false);

		builder.append("<ul> ");

		for (StackTraceElement element : elementsToPrint) {
			builder.append("<ul><li class=\"" + exceptionCauseClass + "\">"+element+"</li></ul>");
		}
		builder.append("</ul>");
	}

	private List<StackTraceElement> getResumedStack(Throwable cause, boolean printResume) {
		List<StackTraceElement> elementsToPrint = new ArrayList<StackTraceElement>();
		StackTraceElement[] stackTrace = cause.getStackTrace();
		List<String> fromClasses = new ArrayList<String>();
		for (int i = stackTrace.length-1; i >= 0; i--) {
			StackTraceElement element = stackTrace[i];
			if(!( //tentar colocar o stackTrace somente da aplicação
					element.getClassName().startsWith("br.com.linkcom.neo") ||
					element.getClassName().startsWith("org.apache") ||
					element.getClassName().startsWith("org.jboss") ||
					element.getClassName().startsWith("java") ||
					element.getClassName().startsWith("org.springframework") ||
					element.getClassName().startsWith("sun") ||
					element.getClassName().startsWith("org.hibernate") ||
					element.getClassName().startsWith("net.sf")
				)){
				if (fromClasses.contains(element.getClassName())) {
					int indexOf = fromClasses.indexOf(element.getClassName());
					fromClasses.remove(indexOf);
					elementsToPrint.remove(indexOf);
				}
				elementsToPrint.add(element);
				fromClasses.add(element.getClassName());
			}
		}

		if (printResume) {
			StackTraceElement[] last = cause.getStackTrace();
			Throwable exception = cause;
			StackTraceElement[] toArray = elementsToPrint.toArray(new StackTraceElement[elementsToPrint.size()]);
			exception.setStackTrace(toArray);
			//log.error("\n", exception);
			//log.error("\n"+cause.getClass().getName()+": "+cause.getMessage());
			for (StackTraceElement element : elementsToPrint) {

				log.error("Stack Resumido:\n\n\t"+element);
			}
			exception.setStackTrace(last);
		}
		return elementsToPrint;
	}

	public String getBindErrorClass() {
		return bindErrorClass;
	}

	public String getDebugClass() {
		return debugClass;
	}

	public String getErrorClass() {
		return errorClass;
	}

	public String getGlobalErrorclass() {
		return globalErrorclass;
	}

	public String getInfoClass() {
		return infoClass;
	}

	public String getTraceClass() {
		return traceClass;
	}

	public String getValidationErrorClass() {
		return validationErrorClass;
	}

	public String getWarnClass() {
		return warnClass;
	}

	public void setBindErrorClass(String bindErrorClass) {
		this.bindErrorClass = bindErrorClass;
	}

	public void setDebugClass(String debugClass) {
		this.debugClass = debugClass;
	}

	public void setErrorClass(String errorClass) {
		this.errorClass = errorClass;
	}

	public void setGlobalErrorclass(String globalErrorclass) {
		this.globalErrorclass = globalErrorclass;
	}

	public void setInfoClass(String infoClass) {
		this.infoClass = infoClass;
	}

	public void setTraceClass(String traceClass) {
		this.traceClass = traceClass;
	}

	public void setValidationErrorClass(String validationErrorClass) {
		this.validationErrorClass = validationErrorClass;
	}

	public void setWarnClass(String warnClass) {
		this.warnClass = warnClass;
	}

}

