package br.com.linkcom.wms.geral.bean.vo;

import java.util.Date;

import br.com.linkcom.neo.types.Money;

public class PedidoCompraResumoVO {

	private String fornecedor;
	private Date dataemissao;
	private Date dtrecebimento;
	private String numero;
	private Money valor;
	private Boolean houveValidacao;

	public String getFornecedor() {
		return fornecedor;
	}

	public Date getDataemissao() {
		return dataemissao;
	}

	public Date getDtrecebimento() {
		return dtrecebimento;
	}

	public String getNumero() {
		return numero;
	}

	public Money getValor() {
		return valor;
	}
	
	public Boolean getHouveValidacao() {
		return houveValidacao;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public void setDataemissao(Date dataemissao) {
		this.dataemissao = dataemissao;
	}

	public void setDtrecebimento(Date dtrecebimento) {
		this.dtrecebimento = dtrecebimento;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setValor(Money valor) {
		this.valor = valor;
	}

	public void setHouveValidacao(Boolean houveValidacao) {
		this.houveValidacao = houveValidacao;
	}
	
}
