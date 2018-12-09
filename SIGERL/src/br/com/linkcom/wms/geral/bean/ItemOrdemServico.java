package br.com.linkcom.wms.geral.bean;

public class ItemOrdemServico {

	protected Integer cdordemservicoproduto;
	protected Long qtdeesperada;
	protected Long qtdeconfirmada;

	public Integer getCdordemservicoproduto() {
		return cdordemservicoproduto;
	}

	public void setCdordemservicoproduto(Integer cdordemservicoproduto) {
		this.cdordemservicoproduto = cdordemservicoproduto;
	}

	public Long getQtdeesperada() {
		return qtdeesperada;
	}

	public void setQtdeesperada(Long qtdeesperada) {
		this.qtdeesperada = qtdeesperada;
	}

	public Long getQtdeconfirmada() {
		return qtdeconfirmada;
	}

	public void setQtdeconfirmada(Long qtdeconfirmada) {
		this.qtdeconfirmada = qtdeconfirmada;
	}
}
