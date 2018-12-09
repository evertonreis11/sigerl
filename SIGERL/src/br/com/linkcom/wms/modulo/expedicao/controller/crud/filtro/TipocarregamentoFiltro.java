package br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;

public class TipocarregamentoFiltro extends FiltroListagem{

	private String nome;
	private Boolean prioridade;
	
	public String getNome() {
		return nome;
	}
	public Boolean getPrioridade() {
		return prioridade;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setPrioridade(Boolean prioridade) {
		this.prioridade = prioridade;
	}
}
