package br.com.linkcom.wms.modulo.recebimento.controller.report.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Recebimento;

public class EmitirRavEntradaFiltro extends FiltroListagem{
	
	private Recebimento recebimento;

	public Recebimento getRecebimento() {
		return recebimento;
	}

	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}
	
}
