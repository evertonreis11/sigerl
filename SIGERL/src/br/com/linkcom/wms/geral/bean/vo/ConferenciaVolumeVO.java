package br.com.linkcom.wms.geral.bean.vo;

public class ConferenciaVolumeVO {

	private Integer cdcarregamentoitem;
	private Integer cdpedidovendaproduto;
	private Integer cdproduto;
	private Integer cdvolume;
	private long qtdeesperada;
	private long qtdeconferida;

	public Integer getCdcarregamentoitem() {
		return cdcarregamentoitem;
	}

	public Integer getCdpedidovendaproduto() {
		return cdpedidovendaproduto;
	}

	public Integer getCdproduto() {
		return cdproduto;
	}

	public Integer getCdvolume() {
		return cdvolume;
	}

	public long getQtdeesperada() {
		return qtdeesperada;
	}

	public long getQtdeconferida() {
		return qtdeconferida;
	}

	public void setCdcarregamentoitem(Integer cdcarregamentoitem) {
		this.cdcarregamentoitem = cdcarregamentoitem;
	}

	public void setCdpedidovendaproduto(Integer cdpedidovendaproduto) {
		this.cdpedidovendaproduto = cdpedidovendaproduto;
	}

	public void setCdproduto(Integer cdproduto) {
		this.cdproduto = cdproduto;
	}

	public void setCdvolume(Integer cdvolume) {
		this.cdvolume = cdvolume;
	}

	public void setQtdeesperada(long qtdeesperada) {
		this.qtdeesperada = qtdeesperada;
	}

	public void setQtdeconferida(long qtdeconferida) {
		this.qtdeconferida = qtdeconferida;
	}

}
