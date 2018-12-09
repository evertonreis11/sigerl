package br.com.linkcom.wms.controller.popup.filter;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Pessoanatureza;


public class FornecedorFiltro extends FiltroListagem{
	protected String nome;
	protected Pessoanatureza pessoanatureza;
	protected Boolean ativo = Boolean.TRUE;
	
	public String getNome() {
		return nome;
	}
	
	@DisplayName("Natureza")
	public Pessoanatureza getPessoanatureza() {
		return pessoanatureza;
	}
	
	@DisplayName("Situação")
	public Boolean getAtivo() {
		return ativo;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setPessoanatureza(Pessoanatureza pessoanatureza) {
		this.pessoanatureza = pessoanatureza;
	}
	
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
}
