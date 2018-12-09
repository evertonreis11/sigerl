package br.com.linkcom.wms.modulo.expedicao.controller.report.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Expedicao;

public class EtiquetaexpedicaoFiltro extends FiltroListagem{
	
	private Expedicao expedicao;

	public Expedicao getExpedicao() {
		return expedicao;
	}

	public void setExpedicao(Expedicao expedicao) {
		this.expedicao = expedicao;
	}
	
}
