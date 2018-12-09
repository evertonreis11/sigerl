package br.com.linkcom.wms.geral.service;

import org.jasypt.util.password.StrongPasswordEncryptor;

import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.vo.Auditoria;
import br.com.linkcom.wms.util.WmsUtil;

public class AuditoriaService {

	/**
	 * 
	 * @param request
	 * @param filtro
	 * @return
	 */
	public static Boolean validaUsuario(WebRequestContext request, Auditoria auditoria, String acao){
		
		StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
		Usuario usuario = UsuarioService.getInstance().findByLoginUsuario(auditoria.getLogin());
		
		if(!WmsUtil.isUserHasAction(acao,usuario)){
			request.addError("Seu usu�rio n�o tem permiss�o para executar essa a��o.");
			return false;
		}else{
			if(usuario==null){
				request.addError("Usu�rio Inv�lido!");
				return false;
			}else{
				if(auditoria.getSenha()!=null && usuario.getSenha()!=null){
					Boolean isSenha = encryptor.checkPassword(auditoria.getSenha(), usuario.getSenha());
					if(!isSenha){
						request.addError("Senha Inv�lida!");
						return false;
					}					
				}else{
					request.addError("Os campos senha e usu�rio s�o obrigat�rios.");
					return false;
				}
			} 
		}
		
		return true;
	}
}
