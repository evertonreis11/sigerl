package br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro;

import java.sql.Date;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Empresa;
import br.com.linkcom.wms.geral.bean.Pedidovendahistoricomotivo;
import br.com.linkcom.wms.geral.bean.Pedidovendaprodutostatus;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Tipooperacao;

public class PedidovendaprodutoFiltro extends FiltroListagem{

	private String numeroPedido;
	private Empresa empresa;
	private String numeroLoja;
	private Integer cdpedidovendaproduto;
	private String numeroPedidoMV;
	private String motivo;
	private String pedidosSelecionados;
	private Deposito deposito;
	private Boolean corte;
	private Pedidovendaprodutostatus pedidovendaprodutostatus;
	private Date dtemissaoInicio;
	private Date dtemissaoFim;
	private Tipooperacao tipooperacao;
	private Produto produto;
	private Pedidovendahistoricomotivo pedidovendahistoricomotivo;
	
	
	//Get's
	public String getNumeroPedido() {
		return numeroPedido;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public String getNumeroLoja() {
		return numeroLoja;
	}
	public Integer getCdpedidovendaproduto() {
		return cdpedidovendaproduto;
	}
	public String getNumeroPedidoMV() {
		return numeroPedidoMV;
	}
	public String getMotivo() {
		return motivo;
	}
	public String getPedidosSelecionados() {
		return pedidosSelecionados;
	}
	public Deposito getDeposito() {
		return deposito;
	}
	public Boolean getCorte() {
		return corte;
	}
	public Pedidovendaprodutostatus getPedidovendaprodutostatus() {
		return pedidovendaprodutostatus;
	}
	public Date getDtemissaoInicio() {
		return dtemissaoInicio;
	}
	public Date getDtemissaoFim() {
		return dtemissaoFim;
	}
	public Tipooperacao getTipooperacao() {
		return tipooperacao;
	}
	public Produto getProduto() {
		return produto;
	}
	public Pedidovendahistoricomotivo getPedidovendahistoricomotivo() {
		return pedidovendahistoricomotivo;
	}
	
	
	//Set's
	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public void setNumeroLoja(String numeroLoja) {
		this.numeroLoja = numeroLoja;
	}
	public void setCdpedidovendaproduto(Integer cdpedidovendaproduto) {
		this.cdpedidovendaproduto = cdpedidovendaproduto;
	}
	public void setNumeroPedidoMV(String numeroPedidoMV) {
		this.numeroPedidoMV = numeroPedidoMV;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public void setPedidosSelecionados(String pedidosSelecionados) {
		this.pedidosSelecionados = pedidosSelecionados;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setCorte(Boolean corte) {
		this.corte = corte;
	}
	public void setPedidovendaprodutostatus(Pedidovendaprodutostatus pedidovendaprodutostatus) {
		this.pedidovendaprodutostatus = pedidovendaprodutostatus;
	}
	public void setDtemissaoInicio(Date dtemissaoInicio) {
		this.dtemissaoInicio = dtemissaoInicio;
	}
	public void setDtemissaoFim(Date dtemissaoFim) {
		this.dtemissaoFim = dtemissaoFim;
	}
	public void setTipooperacao(Tipooperacao tipooperacao) {
		this.tipooperacao = tipooperacao;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public void setPedidovendahistoricomotivo(Pedidovendahistoricomotivo pedidovendahistoricomotivo) {
		this.pedidovendahistoricomotivo = pedidovendahistoricomotivo;
	}
	
}
