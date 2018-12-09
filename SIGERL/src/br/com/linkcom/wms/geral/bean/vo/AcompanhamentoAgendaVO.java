package br.com.linkcom.wms.geral.bean.vo;

import java.util.Date;

public class AcompanhamentoAgendaVO {

	private String numeroClasse;
	private String classeProduto;
	private String codigoProduto;
	private String produto;
	private String numeroPedido;
	private String fornecedor;
	private Date dtprevisaoagenda;
	private Date dtrecebimento;
	private Integer qtdeAgenda;
	private Integer qtdeRecebida;

	public String getNumeroClasse() {
		return numeroClasse;
	}

	public String getClasseProduto() {
		return classeProduto;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public String getProduto() {
		return produto;
	}

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public Date getDtprevisaoagenda() {
		return dtprevisaoagenda;
	}

	public Date getDtrecebimento() {
		return dtrecebimento;
	}

	public Integer getQtdeAgenda() {
		return qtdeAgenda;
	}

	public Integer getQtdeRecebida() {
		return qtdeRecebida;
	}

	public void setNumeroClasse(String numeroClasse) {
		this.numeroClasse = numeroClasse;
	}

	public void setClasseProduto(String classe) {
		this.classeProduto = classe;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public void setDtprevisaoagenda(Date dtagenda) {
		this.dtprevisaoagenda = dtagenda;
	}

	public void setDtrecebimento(Date dtrecebimento) {
		this.dtrecebimento = dtrecebimento;
	}

	public void setQtdeAgenda(Integer qtdeAgenda) {
		this.qtdeAgenda = qtdeAgenda;
	}

	public void setQtdeRecebida(Integer qtdeRecebida) {
		this.qtdeRecebida = qtdeRecebida;
	}

}
