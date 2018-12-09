package br.com.linkcom.wms.geral.bean.vo;

import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.types.Password;
import br.com.linkcom.wms.geral.bean.Usuario;

public class Auditoria {

	private Integer id;
	private String login;
	private String senha;
	private String motivo;
	private Usuario usuario;
	private String codigo;
	private Money valorMonetario;
	
	//Get's
	public Integer getId() {
		return id;
	}
	public String getLogin() {
		return login;
	}
	@Password
	public String getSenha() {
		return senha;
	}
	public String getMotivo() {
		return motivo;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public String getCodigo() {
		return codigo;
	}
	public Money getValorMonetario() {
		return valorMonetario;
	}
	
	
	//Set's
	public void setId(Integer id) {
		this.id = id;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setValorMonetario(Money valorMonetario) {
		this.valorMonetario = valorMonetario;
	}
	
}
