package br.com.linkcom.wms.util;

import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;

import com.sun.istack.internal.ByteArrayDataSource;

/**
 * Class to send email with html body and attachs<br>
 * <br>
 * how to use:<br>
 *      String fileAttachment = "path/of/file";<br>
 *		String text = "<simg src='cid:00001'> example to show an image in email body";<br>
 *		<br>
 *		Email email = new Email("from@from.com","to@to.com","subject");<br>
 *		email.addHtmlText(text)<br>
 *			 .attachFile(fileAttachment, "name/of/file", "00001")<br>
 *			 .sendMessage();<br>
 *  <br>
 *	@author Pedro Gonçalves
 */
public class EmailManager {

	protected String from;
	protected String to;
	protected String bcc;
	protected String cc;
	protected String subject;
	protected Multipart multipart;
	protected MimeMessage message;
	private Boolean configured = false;
	protected List<String> listTo;
	protected List<String> listBCC;
	protected List<String> listCC;
	protected Session session;


	public EmailManager setFrom(String from) {
		this.from = from;
		return this;
	}

	public EmailManager setTo(String to) {
		this.to = to;
		return this;
	}
	
	public EmailManager setCc(String cc) {
		this.cc = cc;
		return this;
	}
	
	public EmailManager setBcc(String bcc) {
		this.bcc = bcc;
		return this;
	}
	
	public EmailManager setListBCC(List<String> listBCC) {
		this.listBCC = listBCC;
		return this;
	}
	
	public EmailManager setListCC(List<String> listCC) {
		this.listCC = listCC;
		return this;
	}
	
	public EmailManager setSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public String getFrom() {
		return from;
	}

	public MimeMessage getMessage() {
		return message;
	}

	public Multipart getMultipart() {
		return multipart;
	}

	public String getSubject() {
		return subject;
	}

	public String getTo() {
		return to;
	}
	
	public String getBcc() {
		return bcc;
	}

	public String getCc() {
		return cc;
	}

	public List<String> getListBCC() {
		return listBCC;
	}

	public List<String> getListCC() {
		return listCC;
	}

	public EmailManager setMessage(MimeMessage message) {
		this.message = message;
		return this;
	}
	
	public List<String> getListTo() {
		return listTo;
	}
	
	public EmailManager setListTo(List<String> listTo) {
		this.listTo = listTo;
		return this;
	}

	public EmailManager setMultipart(Multipart multipart) {
		this.multipart = multipart;
		return this;
	}
	
	public EmailManager(String from, String to, String subject) throws Exception {
		this.setFrom(from);
		this.setTo(to);
		this.setSubject(subject);
		this.configure();
	}
	
	public EmailManager(String from, String to, String subject, String bcc) throws Exception {
		this.setFrom(from);
		this.setTo(to);
		this.setSubject(subject);
		this.setBcc(bcc);
		this.configure();
	}
	
	public EmailManager(String from, String to, String subject, List<String> listBCC) throws Exception {
		this.setFrom(from);
		this.setTo(to);
		this.setSubject(subject);
		this.setListBCC(listBCC);
		this.configure();
	}

	
	public EmailManager() {
	}

	private EmailManager configure() throws Exception {
		verify();
		
		session = (Session) InitialContext.doLookup("java:/mail/MaquinaDeVendas");
		message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		
		if (listTo != null) {
			for (String element : listTo) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(element));
			}
		} else {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		}
		
		if (listBCC != null) {
			for (String element : listBCC) {
				message.addRecipient(Message.RecipientType.BCC, new InternetAddress(element));
			}
		} else if(bcc != null){
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
		}
		
		if (listCC != null) {
			for (String element : listCC) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(element));
			}
		} else if(cc != null){
			message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
		}
		
		message.setSubject(subject);
		multipart = new MimeMultipart();
		this.configured = true;
		return this;
	}

	public EmailManager attachFile(String file,String filename,String contentType, String id) throws Exception {
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(file);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setHeader("Content-Type", contentType);
		messageBodyPart.setFileName(filename);
		messageBodyPart.setContentID("<"+id+">");
		addPart(messageBodyPart);
		return this;
	}
	
	public EmailManager attachFileUsingByteArray(byte[] file,String filename,String contentType, String id) throws Exception {
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		DataSource source = new ByteArrayDataSource(file, contentType);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setHeader("Content-Type", contentType);
		messageBodyPart.setFileName(filename);
		messageBodyPart.setContentID("<"+id+">");
		addPart(messageBodyPart);
		return this;
	}
	
	private Boolean verify() {
		if (from == null) {throw new NullPointerException("from can´t be null");}
		else if (to == null && listTo == null) {throw new NullPointerException("to can´t be null");}
		else if (subject == null) {throw new NullPointerException("subject can´t be null");}
		return true;
	}

	
	public void addPart(MimeBodyPart part) throws Exception {
		if (verify() && !configured){configure();}
		multipart.addBodyPart(part);
	}
	
	public EmailManager addHtmlText(String text) throws Exception {
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(text);
		messageBodyPart.setHeader("Content-Type", "text/html");	
		addPart(messageBodyPart);
		return this;
	}
	
	public void sendMessage() throws Exception{
		message.setContent(multipart);
		Transport.send(message);
		this.setFrom(null);
		this.setTo(null);
		this.setListTo(null);
		this.setSubject(null);
		
		session = (Session) InitialContext.doLookup("java:/mail/MaquinaDeVendas");
		message = new MimeMessage(session);
	}
	
}
