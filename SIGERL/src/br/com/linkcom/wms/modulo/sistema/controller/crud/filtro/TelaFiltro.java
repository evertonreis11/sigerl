package br.com.linkcom.wms.modulo.sistema.controller.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;


public class TelaFiltro extends FiltroListagem {
	protected String descricao;
	
	@DisplayName("Nome")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
