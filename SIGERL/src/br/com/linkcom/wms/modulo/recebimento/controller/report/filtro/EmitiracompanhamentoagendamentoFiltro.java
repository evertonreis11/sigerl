package br.com.linkcom.wms.modulo.recebimento.controller.report.filtro;

import java.util.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Fornecedor;

public class EmitiracompanhamentoagendamentoFiltro {

	private Deposito deposito;
	private Date dtinicioagendamento;
	private Date dtfimagendamento;
	private Date dtiniciorecebimento;
	private Date dtfimrecebimento;
	private Fornecedor fornecedor;
	private String numeropedido;

	@Required
	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}

	public Date getDtinicioagendamento() {
		return dtinicioagendamento;
	}

	public Date getDtfimagendamento() {
		return dtfimagendamento;
	}

	public Date getDtiniciorecebimento() {
		return dtiniciorecebimento;
	}

	public Date getDtfimrecebimento() {
		return dtfimrecebimento;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	@MaxLength(10)
	@DisplayName("Número do pedido")
	public String getNumeropedido() {
		return numeropedido;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setDtinicioagendamento(Date dtinicioagendamento) {
		this.dtinicioagendamento = dtinicioagendamento;
	}

	public void setDtfimagendamento(Date dtfimagendamento) {
		this.dtfimagendamento = dtfimagendamento;
	}

	public void setDtiniciorecebimento(Date dtiniciorecebimento) {
		this.dtiniciorecebimento = dtiniciorecebimento;
	}

	public void setDtfimrecebimento(Date dtfimrecebimento) {
		this.dtfimrecebimento = dtfimrecebimento;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public void setNumeropedido(String numeropedido) {
		this.numeropedido = numeropedido;
	}

}
