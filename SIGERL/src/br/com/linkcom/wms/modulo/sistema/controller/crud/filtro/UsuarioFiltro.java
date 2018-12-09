package br.com.linkcom.wms.modulo.sistema.controller.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Papel;

public class UsuarioFiltro extends FiltroListagem {
	
	protected String nome;
	protected String login;
	protected Papel papel;
	protected Deposito deposito;
	protected Boolean ativo = Boolean.TRUE;
	protected Linhaseparacao linhaseparacao;
	
	@DisplayName("Nome")
	public String getNome() {
		return nome;
	}
	@MaxLength(15)
	public String getLogin() {
		return login;
	}
	
	@DisplayName("Perfil")
	public Papel getPapel() {
		return papel;
	}
	
	@DisplayName("Situação")
	public Boolean getAtivo() {
		return ativo;
	}
	
	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@DisplayName("Linha de separação")
	public Linhaseparacao getLinhaseparacao() {
		return linhaseparacao;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public void setPapel(Papel papel) {
		this.papel = papel;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	public void setLinhaseparacao(Linhaseparacao linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}
		
}
