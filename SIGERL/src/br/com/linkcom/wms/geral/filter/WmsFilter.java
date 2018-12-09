package br.com.linkcom.wms.geral.filter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import br.com.linkcom.neo.authorization.AuthenticationControlFilter;
import br.com.linkcom.neo.authorization.User;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariodeposito;
import br.com.linkcom.wms.geral.bean.Usuariopapel;
import br.com.linkcom.wms.geral.service.DepositoService;
import br.com.linkcom.wms.geral.service.UsuarioService;
import br.com.linkcom.wms.util.WmsUtil;

/**
 * @author Pedro Gonçalves
 */
public class WmsFilter extends AuthenticationControlFilter {
	
	private String basePath;

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		
		basePath = config.getServletContext().getRealPath(File.separator+"css"+File.separator);
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,ServletException {
		
		//Atributo utilizado para saber qual é o cliente que está acessando o WMS no servidor da LinkCom
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String clienteFromUrl = WmsUtil.getClienteFromUrl(httpServletRequest);
		File file = new File(basePath + File.separator + clienteFromUrl);
		if (file.exists())
			request.setAttribute("tema_wms", clienteFromUrl);
		else
			request.setAttribute("tema_wms", "default");

		super.doFilter(request, response, chain);
		
		HttpServletRequest httpReq = httpServletRequest;
		WmsUtil.atualizaInformacoesPrimeiroLoginDia(httpReq);
	}
	
	@Override
	public User updateUserInfo(User user, HttpServletRequest request,HttpServletResponse response){
		
		String deposito = null;
		
		if(request.getParameter("deposito") != null){
			deposito = request.getParameter("deposito"); 
		}else{
			deposito = NumberUtils.INTEGER_ZERO.toString();
		}
		
		// exibe modal de seleção de deposito
		deposito = exibeModalDeposito(user, request, deposito);
		
		if (StringUtils.isBlank(deposito)){
			return null;
		}
		
		if (user instanceof Usuario){
			Usuario usuario = (Usuario) user;
			
			int cdDeposito = Integer.parseInt(deposito);
			Deposito dep = new Deposito();
			dep.setCddeposito(cdDeposito);
			
			usuario = UsuarioService.getInstance().carregarUsuario(usuario,dep);
			
			Set<Usuariodeposito> listaUsuarioDeposito = usuario == null ? new ListSet<Usuariodeposito>(Usuariodeposito.class) : usuario.getListaUsuarioDeposito();
			if(listaUsuarioDeposito == null || listaUsuarioDeposito.isEmpty()){
				request.setAttribute("erro_dep","Este usuário não tem acesso ao depósito informado.");
				return null;
			} else{
				Usuariodeposito selectedDeposito = listaUsuarioDeposito.iterator().next();
				request.getSession().setAttribute(WmsUtil.DEPOSITO_KEY, selectedDeposito.getDeposito());
				request.getSession().setAttribute(WmsUtil.EMPRESA_KEY, DepositoService.getInstance().findEmpresaByDeposito(selectedDeposito.getDeposito()));				
			}
			
			if(usuario.getAtivo().equals(false)){
				return null;
				
			} else{
				boolean aux=true;
				for(Usuariopapel usuariopapel:usuario.getListaUsuariopapel()){
					if(usuariopapel.getPapel().getAtivo().equals(true)){
						aux=false;
					}
				}
				if(aux){
					return null;
				}
					
			}
			user = usuario;
		}
		
		return user;
	}
	
	/**
	 * Realiza validações para exibição da modal de seleção de deposito no login.
	 * 
	 * @param user
	 * @param request
	 * @param deposito
	 * @return
	 */
	private String exibeModalDeposito(User user, HttpServletRequest request, String deposito) {
		
		String habilitaModal = StringUtils.isNotEmpty(request.getParameter("habilitaModal"))? request.getParameter("habilitaModal") : "0";
		
		if ("<null>".equals(deposito)){
			List<Deposito> depositos = DepositoService.getInstance().findAtivosIndexByUsuario((Usuario) user);
			
			if (depositos == null || depositos.isEmpty()){
				request.setAttribute("erro_dep","Não existem depósitos ativos para este usuário.");
				request.setAttribute("habilitaModal", 0);
				return null;
			}else if (NumberUtils.INTEGER_ONE.equals(new Integer(depositos.size()))){
				deposito = depositos.get(0).getCddeposito().toString();
			}else{
				request.setAttribute("listaDP", depositos);
				request.setAttribute("habilitaModal", 1);
				request.setAttribute("username", request.getParameter("username"));
				request.setAttribute("password", request.getParameter("password"));
				
				if("1".equals(habilitaModal)){
					request.setAttribute("erro_dep","O preenchimento do depósito é obrigatório para efetuar o login");
				}
				
				return null;
			}
			
		}
		
		return deposito;
	}
	
	@Override
	public void updatePasswordOnDataBase(User user, String hashSenha) {
		if (user instanceof Usuario) {
			Usuario usuario = (Usuario) user;
			usuario.setSenha(hashSenha);
			UsuarioService.getInstance().alterarSenhaUsuario(usuario);			
		}
	}
	
	@Override
	public boolean doLogin() {
		return true;
	}
}
