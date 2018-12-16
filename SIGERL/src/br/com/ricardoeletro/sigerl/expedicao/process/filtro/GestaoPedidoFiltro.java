package br.com.ricardoeletro.sigerl.expedicao.process.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Deposito;

public class GestaoPedidoFiltro extends FiltroListagem{
	
	private Deposito loja;
	
	private String numeroPedido;
	
	private String numeroNota;
	
	private Date dtChegadaInicial;
	
	private Date dtChegadaFinal;
	
	private String codigoProduto;
	
	private String nomeCliente;
	
	@DisplayName("Loja")
	public Deposito getLoja() {
		return loja;
	}
	
	public void setLoja(Deposito loja) {
		this.loja = loja;
	}
	
	@DisplayName("Número do Pedido")
	public String getNumeroPedido() {
		return numeroPedido;
	}
	
	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	
	@DisplayName("Número da Nota")
	public String getNumeroNota() {
		return numeroNota;
	}
	
	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
	}
	
	public Date getDtChegadaInicial() {
		return dtChegadaInicial;
	}
	
	public void setDtChegadaInicial(Date dtChegadaInicial) {
		this.dtChegadaInicial = dtChegadaInicial;
	}
	
	public Date getDtChegadaFinal() {
		return dtChegadaFinal;
	}
	
	public void setDtChegadaFinal(Date dtChegadaFinal) {
		this.dtChegadaFinal = dtChegadaFinal;
	}
	
	@DisplayName("Código do Produto")
	public String getCodigoProduto() {
		return codigoProduto;
	}
	
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	@DisplayName("Nome do Cliente")
	public String getNomeCliente() {
		return nomeCliente;
	}
	
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	
}
