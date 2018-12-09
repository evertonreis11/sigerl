package br.com.linkcom.wms.geral.bean.vo;

import br.com.linkcom.neo.types.Money;

public class TotalVO {

	private Money valorTotal = new Money(0);
	private Long qtdeTotal = 0L;
	private Long numeroVolumes = 0L;
	private Double cubagemTotal = 0.0;

	public Money getValorTotal() {
		return valorTotal;
	}

	public Long getQtdeTotal() {
		return qtdeTotal;
	}

	public Long getNumeroVolumes() {
		return numeroVolumes;
	}

	public Double getCubagemTotal() {
		return cubagemTotal;
	}

	public void setValorTotal(Money valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void setQtdeTotal(Long qtdeTotal) {
		this.qtdeTotal = qtdeTotal;
	}

	public void setNumeroVolumes(Long numeroVolumes) {
		this.numeroVolumes = numeroVolumes;
	}

	public void setCubagemTotal(Double cubagemTotal) {
		this.cubagemTotal = cubagemTotal;
	}

}
