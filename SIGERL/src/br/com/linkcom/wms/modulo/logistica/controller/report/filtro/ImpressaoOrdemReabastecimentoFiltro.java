package br.com.linkcom.wms.modulo.logistica.controller.report.filtro;

import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Expedicao;

public class ImpressaoOrdemReabastecimentoFiltro {
	
	private Carregamento carregamento;
	private Expedicao expedicao;
	private String ordens;
	
	public Carregamento getCarregamento() {
		return carregamento;
	}
	
	public Expedicao getExpedicao() {
		return expedicao;
	}
	
	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}

	public String getOrdens() {
		return ordens;
	}

	public void setOrdens(String ordens) {
		this.ordens = ordens;
	}
	
	public void setExpedicao(Expedicao expedicao) {
		this.expedicao = expedicao;
	}
	
}
