package br.com.linkcom.wms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

import br.com.linkcom.neo.core.web.NeoWeb;

/**
 * Classe responsável por processar o templates e substituir variáveis atribuídas.
 * Default prefix: {
 * Degault sufix: }
 * 
 * ex: new TemplateManager("/WEB-INF/template/teste.tpl")
 *     .assign("key","teste valor")
 *     .assign("key002","teste Valor 2")
 *     .getTemplate();
 *     
 * Devolve uma String com o resultado da substituição das chaves.
 * 
 * @author Pedro Gonçalves
 */
public class TemplateManager {
	private String template;
	private String texto;
	private String prefix = "{";
	private String sufix = "}";
	private HashMap<String, Object> mapaKeys = new HashMap<String, Object>();
	
	/**
	 * Construtor default da classe
	 * @param template - ex: /web-inf/template/teste.tpl
	 */
	public TemplateManager(String template) {
		this.template = template;
	}
	
	/**
	 * Construtor usado para alterar o sufixo e o prefixo default
	 * 
	 * @param template
	 * @param prefix - default "{"
	 * @param sufix - default "}"
	 */
	public TemplateManager(String template,String prefix,String sufix) {
		this.template = template;
		this.prefix = prefix;
		this.sufix = sufix;
	}
	
	/**
	 * método utilizado para setar o prefixo
	 * 
	 * @param prefix
	 * @return
	 */
	public TemplateManager setPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}
	
	/**
	 * método utilizado para setar o sufixo
	 * 
	 * @param sufix
	 * @return
	 */
	public TemplateManager setSufix(String sufix) {
		this.sufix = sufix;
		return this;
	}
	
	/**
	 * Adiciona uma variável para ser localizada e processada no template
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public TemplateManager assign(String key, String value) {
		mapaKeys.put(prefix+key+sufix, value);
		return this;
	}
	
	/**
	 * Processa as variáveis e retorna uma String com o conteúdo do template já modificado.
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getTemplate() throws IOException{
		readTemplate();
		processaTemplate();
		return texto;
	}
	
	private void readTemplate() throws IOException{
		InputStream resourceAsStream = null;
		try {
			resourceAsStream = NeoWeb.getRequestContext().getSession().getServletContext().getResourceAsStream(template);
			
			if(resourceAsStream == null){
				throw new IllegalArgumentException("Template de "+template+" não foi encontrado!");
			}
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
			String line = null;
			String texto = "";
			while((line = bufferedReader.readLine()) != null){
				texto += line+"\n";
			}
			
			this.texto = texto;
		}
		finally {
			try {
				resourceAsStream.close();
			}
			catch (Exception e) {
			}
		}
	}
	
	private void processaTemplate() {
		Set<String> keys = mapaKeys.keySet();
		String value = "";
		for (String key : keys) {
			value = mapaKeys.get(key) != null ? mapaKeys.get(key).toString() : "";
			texto = texto.replace(key, value);
		}
	}
}
