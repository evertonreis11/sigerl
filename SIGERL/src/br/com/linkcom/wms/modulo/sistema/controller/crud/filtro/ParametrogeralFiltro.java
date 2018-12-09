package br.com.linkcom.wms.modulo.sistema.controller.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;


public class ParametrogeralFiltro extends FiltroListagem {
	protected String nome;
	protected String valor;
	
	@DisplayName("Nome")
	@MaxLength(50)
	public String getNome() {
		return nome;
	}
	
	@DisplayName("Valor")
	@MaxLength(250)
	public String getValor() {
		return valor;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
}
