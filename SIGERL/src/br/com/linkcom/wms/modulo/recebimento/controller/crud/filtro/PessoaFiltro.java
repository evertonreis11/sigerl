package br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Pessoanatureza;
import br.com.linkcom.wms.geral.bean.Tipopessoa;

public class PessoaFiltro extends FiltroListagem {

	protected String nome;
	protected Pessoanatureza pessoanatureza;
	protected Tipopessoa listatipo;
	protected Boolean ativo = Boolean.TRUE;
	
	public String getNome() {
		return nome;
	}
	
	@DisplayName("Natureza")
	public Pessoanatureza getPessoanatureza() {
		return pessoanatureza;
	}
	
	@DisplayName("Tipo")
	public Tipopessoa getListatipo() {
		return listatipo;
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
	
	public void setListatipo(Tipopessoa listatipo) {
		this.listatipo = listatipo;
	}
	
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	
}
