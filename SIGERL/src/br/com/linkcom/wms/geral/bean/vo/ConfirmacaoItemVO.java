package br.com.linkcom.wms.geral.bean.vo;

public class ConfirmacaoItemVO {

	private Integer cdcarregamentoitem;
	private Long qtdeconfirmada;

	public Integer getCdcarregamentoitem() {
		return cdcarregamentoitem;
	}

	public Long getQtdeconfirmada() {
		return qtdeconfirmada;
	}

	public void setCdcarregamentoitem(Integer cdordemservicoproduto) {
		this.cdcarregamentoitem = cdordemservicoproduto;
	}

	public void setQtdeconfirmada(Long qtdeconfirmada) {
		this.qtdeconfirmada = qtdeconfirmada;
	}

}
