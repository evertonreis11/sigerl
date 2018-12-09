package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;

public class ProdutoclasseFiltro extends FiltroListagem {
	
	private String nome;
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
}
