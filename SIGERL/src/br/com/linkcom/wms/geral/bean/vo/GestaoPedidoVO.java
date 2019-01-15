package br.com.linkcom.wms.geral.bean.vo;

import java.sql.Timestamp;

public class GestaoPedidoVO {
	
	private String numeroNota;
	
	private String numeroPedido;
	
	private Timestamp dataPedido;
	
	private Timestamp dataChegada;
	
	private String situacao;
	
	private String cliente;
	
	private Long ultimaNota;
	
	private String chaveNfe;

	public String getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
	}

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public Timestamp getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(Timestamp dataPedido) {
		this.dataPedido = dataPedido;
	}

	public Timestamp getDataChegada() {
		return dataChegada;
	}

	public void setDataChegada(Timestamp dataChegada) {
		this.dataChegada = dataChegada;
	}

	public Long getUltimaNota() {
		return ultimaNota;
	}

	public void setUltimaNota(Long ultimaNota) {
		this.ultimaNota = ultimaNota;
	}

	public String getChaveNfe() {
		return chaveNfe;
	}

	public void setChaveNfe(String chaveNfe) {
		this.chaveNfe = chaveNfe;
	}
	
}
