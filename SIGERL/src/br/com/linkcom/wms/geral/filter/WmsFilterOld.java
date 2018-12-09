package br.com.linkcom.wms.geral.filter;

import java.io.IOException;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.linkcom.neo.authorization.User;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.DefaultWebRequestContext;
import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariodeposito;
import br.com.linkcom.wms.geral.bean.Usuariopapel;
import br.com.linkcom.wms.geral.dao.WmsAuthorizationDAO;
import br.com.linkcom.wms.geral.service.UsuarioService;
import br.com.linkcom.wms.util.WmsUtil;

/**
 * @author Pedro Gonçalves
 */
public class WmsFilterOld implements Filter {
	
	public void init(FilterConfig config) throws ServletException {

	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		
		request.setAttribute("ctx",request.getContextPath());
		
		String requestURI = request.getRequestURI();
		if (requestURI.endsWith("ricardoeletro_security")) {
			doLogin(request, response, chain);
		} else {
			if(doAuth(request, response, chain)){
				chain.doFilter(request,response);
			}
		}

	}
	public void destroy() {
		
	}
	
	public void doLogin(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws IOException, ServletException {
		String username = request.getParameter("username") != null && !request.getParameter("username").equals("") ? request.getParameter("username") : "0";
		String password = request.getParameter("password") != null && !request.getParameter("password").equals("") ? request.getParameter("password") : "0";
		String deposito = request.getParameter("deposito") !=null ? request.getParameter("deposito") : "0";
		WmsAuthorizationDAO authorizationDAO = (WmsAuthorizationDAO)Neo.getApplicationContext().getBean("authorizationDAO");
		
		User user = authorizationDAO.findUserByLogin(username);
		if (user != null && password.equals(user.getPassword())) {
			DefaultWebRequestContext requestContext = (DefaultWebRequestContext) NeoWeb.getRequestContext();
			Usuario usuario = null;
			if (user instanceof Usuario){				
				usuario = (Usuario) user;
				
				int cdDeposito = Integer.parseInt(deposito);
				Deposito dep = new Deposito();
				dep.setCddeposito(cdDeposito);
				
				usuario = UsuarioService.getInstance().carregarUsuario(usuario,dep);
				
				Set<Usuariodeposito> listaUsuarioDeposito = usuario == null ? new ListSet<Usuariodeposito>(Usuariodeposito.class) : usuario.getListaUsuarioDeposito();
				if(listaUsuarioDeposito == null || listaUsuarioDeposito.isEmpty()){
					request.setAttribute("erro_dep","Este usuário não tem acesso ao depósito informado.");
					request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
					return;
				} else{
					Usuariodeposito selectedDeposito = listaUsuarioDeposito.iterator().next();
					request.getSession().setAttribute(WmsUtil.DEPOSITO_KEY, selectedDeposito.getDeposito());
				}
				
				if(usuario.getAtivo().equals(false)){
						request.setAttribute("login_error",true);
						request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
						return;					
				} else{
					boolean aux=true;
					for(Usuariopapel usuariopapel:usuario.getListaUsuariopapel()){
						if(usuariopapel.getPapel().getAtivo().equals(true)){
							aux=false;
						}
					}
					if(aux){
						request.setAttribute("login_error",true);
						request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
						return;
					}
						
				}
			}
			
			requestContext.setUser(usuario);
			
			Object attribute = request.getSession().getAttribute("originator");
			response.sendRedirect((String)attribute);
		} else {
			request.setAttribute("login_error",true);
			request.getRequestDispatcher("/jsp/login.jsp").forward(request,response);	
			return;
		}
	}
	
	public Boolean doAuth(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws IOException, ServletException {
		String requestURI = request.getRequestURI();
		String queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";
		String contextPath = request.getContextPath() != null ? request.getContextPath() : "";
		request.getSession().setAttribute("originator",contextPath + requestURI.substring(contextPath.length()) + queryString);
		User user = NeoWeb.getRequestContext().getUser();
		if (user != null) {
			return true;
		}
		else {
			request.getRequestDispatcher("/jsp/login.jsp").forward(request,response);
		}
		return false;
	}
}
