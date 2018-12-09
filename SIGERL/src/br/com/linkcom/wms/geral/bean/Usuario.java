package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import br.com.linkcom.neo.authorization.User;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.types.Password;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;


@Entity
@DisplayName("Usuário")
public class Usuario extends Pessoa implements User {
	
	protected String senha;
	protected String login;
	protected String logincoletor;
	protected Boolean trocasenha;
	protected Set<Usuariopapel> listaUsuariopapel = new ListSet<Usuariopapel>(Usuariopapel.class);
	protected Set<Usuariodeposito> listaUsuarioDeposito = new ListSet<Usuariodeposito>(Usuariodeposito.class);
	protected Set<Usuariolinhaseparacao> listaUsuarioLinhaSeparacao = new ListSet<Usuariolinhaseparacao>(Usuariolinhaseparacao.class);
	protected Set<OrdemservicoUsuario> listaOrdemservicousuario = new ListSet<OrdemservicoUsuario>(OrdemservicoUsuario.class);
	
	//Transientes
	protected List<Deposito> listadeposito = new ArrayList<Deposito>();
	protected List<Papel> listaPapel = new ArrayList<Papel>();
	protected Set<Linhaseparacao> listaLinhaSeparacao = new ListSet<Linhaseparacao>(Linhaseparacao.class);
	protected String confirmasenha;
	
	public Usuario(){}
	
	public Usuario(Integer cdpessoa){
		this.cdpessoa = cdpessoa;
	}
	
	@Required
	@MaxLength(15)
	@Password
	public String getSenha() {
		return senha;
	}
	
	public Boolean getTrocasenha() {
		return trocasenha;
	}
	
	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY)
	public Set<Usuariopapel> getListaUsuariopapel() {
		return listaUsuariopapel;
	}	

	@OneToMany(mappedBy="usuario")
	public Set<Usuariodeposito> getListaUsuarioDeposito() {
		return listaUsuarioDeposito;
	}
	
	@DisplayName("Login do coletor")
	@MaxLength(15)
	public String getLogincoletor() {
		return logincoletor;
	}
	
	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY)
	public Set<OrdemservicoUsuario> getListaOrdemservicousuario() {
		return listaOrdemservicousuario;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
		
	public void setTrocasenha(Boolean trocasenha) {
		this.trocasenha = trocasenha;
	}
	
	public void setListaUsuariopapel(Set<Usuariopapel> listaUsuariopapel) {
		this.listaUsuariopapel = listaUsuariopapel;
	}
	
	
	@Transient
	public List<Papel> getListaPapel() {
		return listaPapel;
	}
	
	@Transient
	@Password
	@Required
	@MaxLength(15)
	@DisplayName("Confirmar senha")
	public String getConfirmasenha() {
		return confirmasenha;
	}
		
	@Transient
	public List<Deposito> getListadeposito() {
		return listadeposito;
	}
	
	@OneToMany(mappedBy="usuario")
	public Set<Usuariolinhaseparacao> getListaUsuarioLinhaSeparacao() {
		return listaUsuarioLinhaSeparacao;
	}
	
	@Transient
	public Set<Linhaseparacao> getListaLinhaSeparacao() {
		return listaLinhaSeparacao;
	}
	
	public void setListaPapel(List<Papel> listaPapel) {
		this.listaPapel = listaPapel;
	}
		
	public void setConfirmasenha(String confirmasenha) {
		this.confirmasenha = confirmasenha;
	}
	
	public void setListaUsuarioDeposito(Set<Usuariodeposito> listaUsuarioDeposito) {
		this.listaUsuarioDeposito = listaUsuarioDeposito;
	}
	
	public void setListadeposito(List<Deposito> listadeposito) {
		this.listadeposito = listadeposito;
	}
	
	@Transient
	public String getDepositos(){
		return WmsUtil.concatenateWithLimit(getListaUsuarioDeposito(),"deposito.nome", 5);
	}
	
	/* API - INICIO*/
	@Transient
	public String getPassword() {
		return senha;
	}
	
	@MaxLength(40)
	@Required
	public String getLogin() {
		return this.login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public void setLogincoletor(String logincoletor) {
		this.logincoletor = logincoletor;
	}
	
	@Transient
	public String getNomeForBase() throws WmsException{
		if(nome != null && nome.length() > 17){
			return WmsUtil.abreviaNome(nome, 17); 
		}
		else return nome;
	}
	
	public void setListaUsuarioLinhaSeparacao(Set<Usuariolinhaseparacao> listaUsuarioLinhaSeparacao) {
		this.listaUsuarioLinhaSeparacao = listaUsuarioLinhaSeparacao;
	}
	
	public void setListaLinhaSeparacao(Set<Linhaseparacao> listaLinhaSeparacao) {
		this.listaLinhaSeparacao = listaLinhaSeparacao;
	}
	
	public void setListaOrdemservicousuario(Set<OrdemservicoUsuario> listaOrdemservicousuario) {
		this.listaOrdemservicousuario = listaOrdemservicousuario;
	}
	
	/* API - FIM*/
}
