package br.com.linkcom.wms.modulo.sistema.controller.process.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Password;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Usuario;

public class AlterarsenhaFiltro {
	protected Usuario usuario;
	protected Boolean admin; 
	protected String senhavelha;
	protected String senhanova;
	protected String confirmasenha;
	
	@DisplayName("Usuário")
	@Required
	public Usuario getUsuario() {
		return usuario;
	}

	public Boolean getAdmin() {
		return admin;
	}
	
	@DisplayName("Digite a sua senha")
	@MaxLength(15)
	@Password
	@Required
	public String getSenhavelha() {
		return senhavelha;
	}
	
	@DisplayName("Senha nova")
	@MaxLength(15)
	@Password
	@Required
	public String getSenhanova() {
		return senhanova;
	}
	
	@DisplayName("Confirme a senha")
	@MaxLength(15)
	@Password
	@Required
	public String getConfirmasenha() {
		return confirmasenha;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	
	public void setSenhavelha(String senhavelha) {
		this.senhavelha = senhavelha;
	}
	
	public void setSenhanova(String senhanova) {
		this.senhanova = senhanova;
	}
	
	public void setConfirmasenha(String confirmasenha) {
		this.confirmasenha = confirmasenha;
	}		
}
