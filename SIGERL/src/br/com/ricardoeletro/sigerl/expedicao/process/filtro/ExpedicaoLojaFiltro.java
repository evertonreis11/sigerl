package br.com.ricardoeletro.sigerl.expedicao.process.filtro;

import br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLoja;
import br.com.ricardoeletro.sigerl.geral.filtro.GenericFilter;

public class ExpedicaoLojaFiltro extends GenericFilter {
	
	private ExpedicaoRetiraLoja expedicaoRetiraLoja;
	
	private String codigoBarras;
	
	private Integer expedicaoImprimirTermo;
	

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

	public Integer getExpedicaoImprimirTermo() {
		return expedicaoImprimirTermo;
	}

	public void setExpedicaoImprimirTermo(Integer expedicaoImprimirTermo) {
		this.expedicaoImprimirTermo = expedicaoImprimirTermo;
	}

}