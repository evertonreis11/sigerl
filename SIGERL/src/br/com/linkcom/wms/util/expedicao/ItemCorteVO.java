package br.com.linkcom.wms.util.expedicao;

import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Produto;

public class ItemCorteVO {
	
	protected Produto produto;
	protected Cliente cliente;
	protected String numeroPedido;
	protected Integer qtdeEsperada;
	protected Integer qtdeColetada;
	protected Integer cdpedidovenda;
	protected Integer qtdeConfirmada;
	protected Boolean faturadoOutraFilial;
	
	public Integer getQtdeConfirmada() {
		return qtdeConfirmada;
	}

	public void setQtdeConfirmada(Integer qtdeConfirmada) {
		this.qtdeConfirmada = qtdeConfirmada;
	}

	public Integer getCdpedidovenda() {
		return cdpedidovenda;
	}
	
	public void setCdpedidovenda(Integer cdpedidovenda) {
		this.cdpedidovenda = cdpedidovenda;
	}

	public ItemCorteVO(){

	}
	
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public String getNumeroPedido() {
		return numeroPedido;
	}
	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	public Integer getQtdeEsperada() {
		return qtdeEsperada;
	}
	public void setQtdeEsperada(Integer qtdeEsperada) {
		this.qtdeEsperada = qtdeEsperada;
	}
	public Integer getQtdeColetada() {
		return qtdeColetada;
	}
	public void setQtdeColetada(Integer qtdeColetada) {
		this.qtdeColetada = qtdeColetada;
	}
	public Boolean getFaturadoOutraFilial() {
		return faturadoOutraFilial;
	}
	public void setFaturadoOutraFilial(Boolean faturadoOutraFilial) {
		this.faturadoOutraFilial = faturadoOutraFilial;
	}

}
