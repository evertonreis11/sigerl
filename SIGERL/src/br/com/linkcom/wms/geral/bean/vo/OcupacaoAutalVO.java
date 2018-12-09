package br.com.linkcom.wms.geral.bean.vo;

import java.math.BigDecimal;
import java.util.List;

public class OcupacaoAutalVO {

	private String deposito;
	private Long itensArmazenados;
	private Long volumesArmazenados;
	private Double cubagemArmazenada;
	private BigDecimal valorEstoque;
	private Integer capacidadeDeposito;
	private Long itensRecebidos;
	private Long volumesRecebidos;
	private BigDecimal valorRecebido;
	private Long itensExpedidos;
	private Double cubagemExpedida;
	private Long volumesExpedidos;
	private BigDecimal valorExpedido;
	private Double cubagemRecebida;
	private List<TipoenderecoVO> estatisticas;

	public String getDeposito() {
		return deposito;
	}
	
	public Long getItensArmazenados() {
		return itensArmazenados;
	}

	public Long getVolumesArmazenados() {
		return volumesArmazenados;
	}

	public Double getCubagemArmazenada() {
		return cubagemArmazenada;
	}

	public BigDecimal getValorEstoque() {
		return valorEstoque;
	}

	public Integer getCapacidadeDeposito() {
		return capacidadeDeposito;
	}

	public Long getItensRecebidos() {
		return itensRecebidos;
	}

	public Long getVolumesRecebidos() {
		return volumesRecebidos;
	}

	public BigDecimal getValorRecebido() {
		return valorRecebido;
	}

	public Long getItensExpedidos() {
		return itensExpedidos;
	}

	public Double getCubagemExpedida() {
		return cubagemExpedida;
	}

	public Long getVolumesExpedidos() {
		return volumesExpedidos;
	}

	public BigDecimal getValorExpedido() {
		return valorExpedido;
	}

	public Double getCubagemRecebida() {
		return cubagemRecebida;
	}

	public List<TipoenderecoVO> getEstatisticas() {
		return estatisticas;
	}

	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}
	
	public void setItensArmazenados(Long itensArmazenados) {
		this.itensArmazenados = itensArmazenados;
	}

	public void setVolumesArmazenados(Long volumesArmazenados) {
		this.volumesArmazenados = volumesArmazenados;
	}

	public void setCubagemArmazenada(Double cubagemArmazenada) {
		this.cubagemArmazenada = cubagemArmazenada;
	}

	public void setValorEstoque(BigDecimal valorEstoque) {
		this.valorEstoque = valorEstoque;
	}

	public void setCapacidadeDeposito(Integer capacidadeDeposito) {
		this.capacidadeDeposito = capacidadeDeposito;
	}

	public void setItensRecebidos(Long itensRecebidos) {
		this.itensRecebidos = itensRecebidos;
	}

	public void setVolumesRecebidos(Long volumesRecebidos) {
		this.volumesRecebidos = volumesRecebidos;
	}

	public void setValorRecebido(BigDecimal valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public void setItensExpedidos(Long itensExpedidos) {
		this.itensExpedidos = itensExpedidos;
	}

	public void setCubagemExpedida(Double cubagemExpedida) {
		this.cubagemExpedida = cubagemExpedida;
	}

	public void setVolumesExpedidos(Long volumesExpedidos) {
		this.volumesExpedidos = volumesExpedidos;
	}

	public void setValorExpedido(BigDecimal valorExpedido) {
		this.valorExpedido = valorExpedido;
	}

	public void setCubagemRecebida(Double cubagemRecebida) {
		this.cubagemRecebida = cubagemRecebida;
	}

	public void setEstatisticas(List<TipoenderecoVO> estatisticas) {
		this.estatisticas = estatisticas;
	}

}
