package br.com.ricardoeletro.sigerl.expedicao.process.filtro;

import br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLoja;
import br.com.ricardoeletro.sigerl.geral.filtro.GenericFilter;

public class ExpedicaoLojaFiltro extends GenericFilter {
	
	private ExpedicaoRetiraLoja expedicaoRetiraLoja;
	
	private String codigoBarras;
	

	public ExpedicaoRetiraLoja getExpedicaoRetiraLoja() {
		return expedicaoRetiraLoja;
	}

	public void setExpedicaoRetiraLoja(ExpedicaoRetiraLoja expedicaoRetiraLoja) {
		this.expedicaoRetiraLoja = expedicaoRetiraLoja;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	
	

}
