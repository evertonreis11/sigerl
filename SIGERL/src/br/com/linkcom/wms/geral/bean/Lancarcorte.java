package br.com.linkcom.wms.geral.bean;

public class Lancarcorte {
	
	protected Carregamento carregamento;
	protected Ordemtipo ordemtipo;
	protected Ordemservico ordemservico;
	protected Produto produto;
	
	public Carregamento getCarregamento() {
		return carregamento;
	}
	
	public Ordemtipo getOrdemtipo() {
		return ordemtipo;
	}
	
	public Ordemservico getOrdemservico() {
		return ordemservico;
	}
	
	public Produto getProduto() {
		return produto;
	}
	
	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}
	
	public void setOrdemtipo(Ordemtipo ordemtipo) {
		this.ordemtipo = ordemtipo;
	}
	
	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
}
