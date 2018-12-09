package br.com.linkcom.wms.util.expedicao;


public class DadosProduto {
	
	private Double pesoTotal = 0.0;
	private Double cubagemTotal = 0.0;
	private Double valorTotal = 0.0;
	private Long totalProdutos = 0L;
	private Integer totalEntrega = 0;
	
	public Double getPesoTotal() {
		return pesoTotal;
	}
	public Double getCubagemTotal() {
		return cubagemTotal;
	}
	public Double getValorTotal() {
		return valorTotal;
	}
	public Long getTotalProdutos() {
		return totalProdutos;
	}
	public Integer getTotalEntrega() {
		return totalEntrega;
	}
	public void setPesoTotal(Double pesoTotal) {
		this.pesoTotal = pesoTotal;
	}
	public void setCubagemTotal(Double cubagemTotal) {
		this.cubagemTotal = cubagemTotal;
	}
	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}
	public void setTotalProdutos(Long totalProdutos) {
		this.totalProdutos = totalProdutos;
	}
	public void setTotalEntrega(Integer totalEntrega) {
		this.totalEntrega = totalEntrega;
	}
	
}
