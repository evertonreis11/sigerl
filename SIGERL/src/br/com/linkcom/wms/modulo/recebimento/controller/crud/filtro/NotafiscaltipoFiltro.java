package br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;

public class NotafiscaltipoFiltro extends FiltroListagem{
	
	protected String nome;

	@MaxLength(30)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
