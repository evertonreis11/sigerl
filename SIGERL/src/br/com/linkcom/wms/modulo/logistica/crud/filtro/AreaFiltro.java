package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;

public class AreaFiltro extends FiltroListagem {
	
	protected String nome;
	
	@MaxLength(50)
	@DisplayName("Nome")
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
}
