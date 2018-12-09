package br.com.linkcom.wms.modulo.logistica.controller.report.filtro;

public enum Ordenacao {

	CODIGO("Código"), DESCRICAO("Descrição");

	private String descricao;

	private Ordenacao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}
}