package br.com.linkcom.wms.modulo.logistica.controller.report.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Recebimento;

public class ImpressaoOrdemRecebimentoFiltro extends FiltroListagem{
	
	protected Recebimento recebimento;
	protected String cdOs;
	
	public Recebimento getRecebimento() {
		return recebimento;
	}
	
	public String getCdOs() {
		return cdOs;
	}
	
	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}
	
	public void setCdOs(String cdOs) {
		this.cdOs = cdOs;
	}

}
