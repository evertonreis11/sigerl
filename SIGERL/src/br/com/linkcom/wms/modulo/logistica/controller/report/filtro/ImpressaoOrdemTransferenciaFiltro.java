package br.com.linkcom.wms.modulo.logistica.controller.report.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Transferencia;

public class ImpressaoOrdemTransferenciaFiltro extends FiltroListagem{
	
	private Transferencia transferencia;
	
	public Transferencia getTransferencia() {
		return transferencia;
	}
	
	public void setTransferencia(Transferencia transferencia) {
		this.transferencia = transferencia;
	}

}
