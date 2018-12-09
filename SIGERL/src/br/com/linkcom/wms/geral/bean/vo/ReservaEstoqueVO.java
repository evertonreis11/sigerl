package br.com.linkcom.wms.geral.bean.vo;

public class ReservaEstoqueVO {

	private int produto;
	private int enderecoOrigem;
	private long qtdeReservada;
	private long qtdeConfirmada;

	public int getProduto() {
		return produto;
	}

	public void setProduto(int produto) {
		this.produto = produto;
	}

	public int getEnderecoOrigem() {
		return enderecoOrigem;
	}

	public void setEnderecoOrigem(int enderecoOrigem) {
		this.enderecoOrigem = enderecoOrigem;
	}

	public long getQtdeReservada() {
		return qtdeReservada;
	}

	public void setQtdeReservada(long qtdeReservada) {
		this.qtdeReservada = qtdeReservada;
	}

	public long getQtdeConfirmada() {
		return qtdeConfirmada;
	}

	public void setQtdeConfirmada(long qtdeConfirmada) {
		this.qtdeConfirmada = qtdeConfirmada;
	}

}
