package br.com.ricardoeletro.sigerl.geral.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;

public class GenericFilter extends FiltroListagem{
	
	private String labelDinamico;
	
	private String valorInicial;
	
	public String getValorInicial() {
		return valorInicial;
	}
	
	public void setValorInicial(String valorInicial) {
		this.valorInicial = valorInicial;
	}
	
	public String getLabelDinamico() {
		return labelDinamico;
	}
	
	public void setLabelDinamico(String labelDinamico) {
		this.labelDinamico = labelDinamico;
	}
	
}
