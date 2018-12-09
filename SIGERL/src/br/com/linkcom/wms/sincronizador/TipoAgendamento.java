package br.com.linkcom.wms.sincronizador;

public enum TipoAgendamento {

	PARCIAL, COMPLETO, INEXISTENTE;
	
	private Integer cdAgendamento;

	public Integer getCdAgendamento() {
		return cdAgendamento;
	}

	public void setCdAgendamento(Integer cdAgendamento) {
		this.cdAgendamento = cdAgendamento;
	}
}
