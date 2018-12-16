package br.com.ricardoeletro.sigerl.expedicao.report.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;

public class TermoEntregaFiltro extends FiltroListagem {
	
	private String chaveNotaFiscal;
	private Boolean impressaoFinalizarExpedicao;

	public String getChaveNotaFiscal() {
		return chaveNotaFiscal;
	}

	public void setChaveNotaFiscal(String chaveNotaFiscal) {
		this.chaveNotaFiscal = chaveNotaFiscal;
	}

	public Boolean getImpressaoFinalizarExpedicao() {
		return impressaoFinalizarExpedicao;
	}

	public void setImpressaoFinalizarExpedicao(Boolean impressaoFinalizarExpedicao) {
		this.impressaoFinalizarExpedicao = impressaoFinalizarExpedicao;
	}
	
	

}
