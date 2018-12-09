package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;

public class TipopaleteFiltro extends FiltroListagem {
	protected String nome;
	
	@DisplayName("Nome")
	@MaxLength(50)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
