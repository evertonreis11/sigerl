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
package br.com.linkcom.neo.core.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.util.LocaleUtils;
import br.com.linkcom.neo.view.SelecionarCadastrarServlet;
import br.com.linkcom.neo.view.menu.MenuTag;

/**
 * @author rogelgarcia
 * @since 21/01/2006
 * @version 1.1
 */
public class NeoFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {
	
		long inicio = System.currentTimeMillis();
//		
		String requestURI = req.getRequestURI();
//		HttpServletRequest servletRequest = NeoWeb.getRequestContext().getServletRequest();
		String urlRequest = req.getRequestURL().toString();
		String[] urlDividida = urlRequest.split("/");
		String servidor = urlDividida[2];
		
		System.out.println("ID início: " + inicio + " (" + requestURI + ") (" + servidor + ")");
		
//		
		HttpServletRequest request = req;
		HttpServletResponse response = res;
		
		request.setAttribute("ctx", request.getContextPath());
		
		//I18N
		String locale = request.getParameter("locale");
		if (locale != null){
			request.getSession().setAttribute(LocaleUtils.LOCALE_SESSION_KEY, locale);
			request.getSession().setAttribute(MenuTag.MENU_CACHE_MAP, null);
		}
		
		//colocar um flag na requisição indicando que esta é uma página selectone ou cadastrar
		String parameter = request.getParameter(SelecionarCadastrarServlet.INSELECTONE);
		if("true".equals(parameter)){
			request.setAttribute(SelecionarCadastrarServlet.INSELECTONE, true);
		}
		
		//cria o contexto de requisicao NEO
		NeoWeb.createRequestContext(request, response);
		try{
		
			
			if(canMakeCache(requestURI)){
				response.addHeader("pragma", "no-cache");
				response.addHeader("cache-control", "no-cache");
				response.addHeader("expires", "0");
			}
		
			if(requestURI.equals(request.getContextPath()+"/neo")){
				String url = "/WEB-INF/classes/br/com/linkcom/neo/resource/neo.jsp";
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				PrintWriter writer = new PrintWriter(arrayOutputStream);
			
				RequestDispatcher requestDispatcher = null;
				requestDispatcher = request.getRequestDispatcher(url);
				requestDispatcher.include(request, response);
				writer.flush();
				response.getWriter().write(arrayOutputStream.toString());
				response.setStatus(HttpServletResponse.SC_OK);
			}
			else {
				String uri = requestURI;
				if (uri.matches("/.+?/.+?/.*")) {
					request.setAttribute("NEO_MODULO", uri.split("/")[2]);
				}
				
				chain.doFilter(request, response);
				long fim = System.currentTimeMillis();
				long tempo = (fim - inicio);
				if (tempo > 200) {
					String acao = request.getParameter("ACAO");
					System.out.println("Tempo: " + requestURI + "  " + (tempo < 10?" ":"") + tempo + " ms (ACAO=)"+acao);
				}
			}
			System.out.println("ID fim: " + inicio + " (" + requestURI + ") (" + servidor + ")");
		}finally{
			//Removendo o contexto do ThreadLocal, senão haverá vazamento de memória.
			Neo.removeRequestContext();
			Neo.removeApplicationContext();
		}
	}

	@Override
	public void destroy() {
	}
	
	/**
	 * Verifica se a requisição é imagem ou css ou js. Caso seja algum desses, será feito cache, caso contrário
	 * as requisições obrigatoriamente não podem sofrer efeito de cache.
	 * 
	 * @param url
	 * @return true - Caso a requisição não seja de imagem.
	 */
	private boolean canMakeCache(String url){
		String [] ext = new String[]{"gif","jpg","png","js","bmp","jpeg","png","css","pdf"};
		if(url == null)
			return false;
		else{
			url = url.toLowerCase();
			for (String string : ext) {
				if(url.endsWith(string))
					return false;
			}
			return true;
		}
	}
}