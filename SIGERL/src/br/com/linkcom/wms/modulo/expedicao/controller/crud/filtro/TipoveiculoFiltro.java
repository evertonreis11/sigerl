package br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;


public class TipoveiculoFiltro extends FiltroListagem {
	
	protected String nome;
	@MaxLength(20)
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
	
}
