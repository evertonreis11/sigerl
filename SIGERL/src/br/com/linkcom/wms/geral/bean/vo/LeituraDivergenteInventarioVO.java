package br.com.linkcom.wms.geral.bean.vo;

public class LeituraDivergenteInventarioVO {

	private int cdenderecodestino;
	private int cdproduto;
	private long qtdeesperada;
	private long qtdelida;

	public int getCdenderecodestino() {
		return cdenderecodestino;
	}

	public void setCdenderecodestino(int cdenderecodestino) {
		this.cdenderecodestino = cdenderecodestino;
	}

	public int getCdproduto() {
		return cdproduto;
	}

	public void setCdproduto(int cdproduto) {
		this.cdproduto = cdproduto;
	}

	public long getQtdeesperada() {
		return qtdeesperada;
	}

	public void setQtdeesperada(long qtdeesperada) {
		this.qtdeesperada = qtdeesperada;
	}

	public long getQtdelida() {
		return qtdelida;
	}

	public void setQtdelida(long qtdelida) {
		this.qtdelida = qtdelida;
	}

}
