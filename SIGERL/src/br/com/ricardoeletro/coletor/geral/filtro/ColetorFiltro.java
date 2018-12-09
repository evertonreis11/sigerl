package br.com.ricardoeletro.coletor.geral.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;

public class ColetorFiltro extends FiltroListagem {
	private String labelDinamico;
	private String valorInicial;
	private String resultado;
	
	
	public String getValorInicial() {
		return valorInicial;
	}
	public void setValorInicial(String valorInicial) {
		this.valorInicial = valorInicial;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public String getLabelDinamico() {
		return labelDinamico;
	}
	public void setLabelDinamico(String labelDinamico) {
		this.labelDinamico = labelDinamico;
	}
	
	
}
