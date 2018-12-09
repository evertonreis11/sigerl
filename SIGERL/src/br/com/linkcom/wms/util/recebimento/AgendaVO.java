package br.com.linkcom.wms.util.recebimento;

import java.math.BigDecimal;
import java.util.Date;

import br.com.linkcom.neo.util.NeoFormater;

public class AgendaVO {
	
	// ---------------------------------- Variáveis de instância ----------------------------------
	
	private Integer numeroAgendamento;
	private String data;
	private String dtprevisao;
	private Long numeroPedido;
	private String fornecedor;
	private String tipoCarga;
	private String status;
	private Integer cdproduto;
	private String codigoProduto;
	private String descricaoProduto;
	private Long qtdeProduto;
	private BigDecimal valor;
	private String emailFornecedor;
	private Integer cdfornecedor;
	
	// ---------------------------------- Construtores ----------------------------------
	
	public AgendaVO() {
	
	}
	
	public AgendaVO(Integer numeroAgendamento, Date data, Date dtprevisao, Long numeroPedido, String fornecedor, String tipoCarga, String status, Integer cdproduto, String codigoProduto, String descricaoProduto, Long qtdeProduto, String email) {
		this.numeroAgendamento = numeroAgendamento;
		this.data = NeoFormater.getInstance().format(data);
		this.dtprevisao = NeoFormater.getInstance().format(dtprevisao);
		this.numeroPedido = numeroPedido;
		this.fornecedor = fornecedor;
		this.tipoCarga = tipoCarga;
		this.status = status;
		this.cdproduto = cdproduto;
		this.codigoProduto = codigoProduto;
		this.descricaoProduto = descricaoProduto;
		this.qtdeProduto = qtdeProduto;
		this.emailFornecedor = email;
	}

	// ---------------------------------- Métodos get e set ----------------------------------
	
	public Integer getNumeroAgendamento() {
		return numeroAgendamento;
	}

	public String getData() {
		return data;
	}
	
	public String getDtprevisao() {
		return dtprevisao;
	}

	public Long getNumeroPedido() {
		return numeroPedido;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public String getTipoCarga() {
		return tipoCarga;
	}

	public String getStatus() {
		return status;
	}

	public Integer getCdproduto() {
		return cdproduto;
	}
	
	public String getCodigoProduto() {
		return codigoProduto;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public Long getQtdeProduto() {
		return qtdeProduto;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public String getEmailFornecedor() {
		return emailFornecedor;
	}
	
	public Integer getCdfornecedor() {
		return cdfornecedor;
	}

	public void setNumeroAgendamento(Integer numeroAgendamento) {
		this.numeroAgendamento = numeroAgendamento;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public void setDtprevisao(String dtprevisao) {
		this.dtprevisao = dtprevisao;
	}

	public void setNumeroPedido(Long numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public void setTipoCarga(String tipoCarga) {
		this.tipoCarga = tipoCarga;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCdproduto(Integer cdproduto) {
		this.cdproduto = cdproduto;
	}
	
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public void setQtdeProduto(Long qtdeProduto) {
		this.qtdeProduto = qtdeProduto;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public void setEmailFornecedor(String emailFornecedor) {
		this.emailFornecedor = emailFornecedor;
	}
	
	public void setCdfornecedor(Integer cdfornecedor) {
		this.cdfornecedor = cdfornecedor;
	}
	
}
