package br.com.ricardoeletro.sigerl.expedicao.process.filtro;

import java.sql.Date;
import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.ProblemaPedidoLoja;

public class GestaoPedidoFiltro extends FiltroListagem{
	
	private Deposito loja;
	
	private String numeroPedido;
	
	private String numeroNota;
	
	private Date dtChegadaInicial;
	
	private Date dtChegadaFinal;
	
	private String codigoProduto;
	
	private String nomeCliente;
	
	private List<ProblemaPedidoLoja> problemasPedido;
	
	private String notasInfoProblema;
	
	private Integer cdProblemaPedidoLoja;
	
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

	public List<ProblemaPedidoLoja> getProblemasPedido() {
		return problemasPedido;
	}

	public void setProblemasPedido(List<ProblemaPedidoLoja> problemasPedido) {
		this.problemasPedido = problemasPedido;
	}

	public String getNotasInfoProblema() {
		return notasInfoProblema;
	}

	public void setNotasInfoProblema(String notasInfoProblema) {
		this.notasInfoProblema = notasInfoProblema;
	}

	public Integer getCdProblemaPedidoLoja() {
		return cdProblemaPedidoLoja;
	}

	public void setCdProblemaPedidoLoja(Integer cdProblemaPedidoLoja) {
		this.cdProblemaPedidoLoja = cdProblemaPedidoLoja;
	}
	
}
