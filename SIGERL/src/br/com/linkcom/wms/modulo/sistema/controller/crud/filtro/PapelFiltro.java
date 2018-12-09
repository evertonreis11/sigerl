package br.com.linkcom.wms.modulo.sistema.controller.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;


public class PapelFiltro extends FiltroListagem {

	protected String nome;
	protected Boolean administrador;
	protected Boolean ativo= Boolean.TRUE;
	
	@DisplayName("Nome")
	@MaxLength(15)
	public String getNome() {
		return nome;
	}
	
	@DisplayName("Situação")
	public Boolean getAtivo() {
		return ativo;
	}
	
	public Boolean getAdministrador() {
		return administrador;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}
	
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	
	
}
