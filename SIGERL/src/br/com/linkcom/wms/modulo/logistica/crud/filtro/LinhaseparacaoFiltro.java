package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;


public class LinhaseparacaoFiltro extends FiltroListagem{

	protected String nome;
	protected Boolean usacheckout;
	
	@MaxLength(20)
	public String getNome() {
		return nome;
	}
	@DisplayName("Usa checkout")
	public Boolean getUsacheckout() {
		return usacheckout;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setUsacheckout(Boolean usacheckout) {
		this.usacheckout = usacheckout;
	}
	
	
	
}
