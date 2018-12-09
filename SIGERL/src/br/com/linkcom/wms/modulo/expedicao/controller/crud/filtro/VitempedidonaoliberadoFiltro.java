package br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.Password;
import br.com.linkcom.neo.validation.annotation.MaxLength;


public class VitempedidonaoliberadoFiltro extends FiltroListagem {
	
	protected Integer cdCarregamento;
	protected String numeroPedido;
	protected String codigoProduto;
	protected String login;
	protected String senha;
	protected String movitoLiberacao;
	
	@DisplayName("N� Carregamento")
	@MaxLength(9)
	public Integer getCdCarregamento() {
		return cdCarregamento;
	}
	@DisplayName("N� Pedido")
	@MaxLength(18)
	public String getNumeroPedido() {
		return numeroPedido;
	}
	@MaxLength(20)
	@DisplayName("C�digo produto")
	public String getCodigoProduto() {
		return codigoProduto;
	}
	@DisplayName("Usu�rio")
	public String getLogin() {
		return login;
	}
	@Password
	@DisplayName("Senha")
	public String getSenha() {
		return senha;
	}
	
	public String getMovitoLiberacao() {
		return movitoLiberacao;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public void setMovitoLiberacao(String movitoLiberacao) {
		this.movitoLiberacao = movitoLiberacao;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public void setCdCarregamento(Integer cdcarregamento) {
		this.cdCarregamento = cdcarregamento;
	}
	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	
}
