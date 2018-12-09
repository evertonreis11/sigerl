package br.com.linkcom.wms.modulo.recebimento.controller.process.filtro;

import java.sql.Date;
import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Fornecedor;
import br.com.linkcom.wms.geral.bean.Pedidocompra;
import br.com.linkcom.wms.geral.bean.Pedidocomprastatus;

public class LiberarPedidoCompraFiltro {
	
	private Deposito deposito;
	private Date dtemissao1;
	private Date dtemissao2;
	private Fornecedor fornecedor;
	private String numeroPedido;
	private String codigoProduto;
	private Pedidocomprastatus pedidocomprastatus;
	
	private Date dtprevisaorecebimento;
	private Date dtprevisaovencimentofinanceiro;
	private String pedidos;
	private List<Pedidocompra> listaPedidoCompra;
	
	private String total;
	private String parcial;
	
	public LiberarPedidoCompraFiltro() {
		this.dtemissao1 = new Date(System.currentTimeMillis());
	}
	
	@Required
	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}
	public Date getDtemissao1() {
		return dtemissao1;
	}
	public Date getDtemissao2() {
		return dtemissao2;
	}
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	@DisplayName("Número do pedido")
	@MaxLength(10)
	public String getNumeroPedido() {
		return numeroPedido;
	}
	@DisplayName("Código do produto")
	@MaxLength(20)
	public String getCodigoProduto() {
		return codigoProduto;
	}
	@DisplayName("Status do pedido")
	public Pedidocomprastatus getPedidocomprastatus() {
		return pedidocomprastatus;
	}
	public String getPedidos() {
		return pedidos;
	}
	public List<Pedidocompra> getListaPedidoCompra() {
		return listaPedidoCompra;
	}
	public String getTotal() {
		return total;
	}
	public String getParcial() {
		return parcial;
	}
	@DisplayName("Previsão de recebimento")
	public Date getDtprevisaorecebimento() {
		return dtprevisaorecebimento;
	}
	@DisplayName("Previsão de vencimento financeiro")
	public Date getDtprevisaovencimentofinanceiro() {
		return dtprevisaovencimentofinanceiro;
	}
	public void setDtprevisaorecebimento(Date dtprevisaorecebimento) {
		this.dtprevisaorecebimento = dtprevisaorecebimento;
	}
	public void setDtprevisaovencimentofinanceiro(Date dtprevisaovencimentofinanceiro) {
		this.dtprevisaovencimentofinanceiro = dtprevisaovencimentofinanceiro;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public void setParcial(String parcial) {
		this.parcial = parcial;
	}
	public void setListaPedidoCompra(List<Pedidocompra> listaPedidoCompra) {
		this.listaPedidoCompra = listaPedidoCompra;
	}
	public void setPedidos(String pedidos) {
		this.pedidos = pedidos;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setDtemissao1(Date dtemissao1) {
		this.dtemissao1 = dtemissao1;
	}
	public void setDtemissao2(Date dtemissao2) {
		this.dtemissao2 = dtemissao2;
	}
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public void setPedidocomprastatus(Pedidocomprastatus pedidocomprastatus) {
		this.pedidocomprastatus = pedidocomprastatus;
	}
	
}
