package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;


public class DepositoFiltro extends FiltroListagem {

	protected String nome;
	
	public String getNome() {
		return nome;
	}
	
	@MaxLength(50)
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
