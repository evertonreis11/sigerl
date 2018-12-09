package br.com.linkcom.wms.modulo.recebimento.controller.process.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Descargapreco;
import br.com.linkcom.wms.geral.bean.Descargaprecoveiculo;
import br.com.linkcom.wms.geral.bean.Produto;

public class CalculocobrancaFiltro {

	protected Integer recebimento;
	protected Produto produto;
	protected Descargapreco descargapreco;
	protected Descargaprecoveiculo descargaprecoveiculo;
	protected Money valor;
	protected Double percentualConfiguracao;
	protected Money valorReceber;
	protected String obs;
	
	@DisplayName("Recebimento")
	@Required
	@MaxLength(9)
	public Integer getRecebimento() {
		return recebimento;
	}
	
	@DisplayName("Produto")
	@Required
	public Produto getProduto() {
		return produto;
	}
	
	@DisplayName("Tabela de cobrança")
	@Required
	public Descargapreco getDescargapreco() {
		return descargapreco;
	}
	
	@DisplayName("Valor da descarga")	
	@Required
	public Money getValor() {
		return valor;
	}
	
	@DisplayName("Percentual")
	@Required
	public Double getPercentualConfiguracao() {
		return percentualConfiguracao;
	}
	
	@DisplayName("Valor a receber")	
	@Required
	public Money getValorReceber() {
		return valorReceber;
	}
	
	@DisplayName("Observação")
	public String getObs() {
		return obs;
	}
	
	@DisplayName("Tipo de veículo")
	@Required
	public Descargaprecoveiculo getDescargaprecoveiculo() {
		return descargaprecoveiculo;
	}
	
	public void setRecebimento(Integer recebimento) {
		this.recebimento = recebimento;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setDescargapreco(Descargapreco descargapreco) {
		this.descargapreco = descargapreco;
	}
	
	public void setValor(Money valor) {
		this.valor = valor;
	}
	
	public void setPercentualConfiguracao(Double percentualConfiguracao) {
		this.percentualConfiguracao = percentualConfiguracao;
	}
	
	public void setValorReceber(Money valorReceber) {
		this.valorReceber = valorReceber;
	}
	
	public void setObs(String obs) {
		this.obs = obs;
	}
	
	public void setDescargaprecoveiculo(Descargaprecoveiculo descargaprecoveiculo) {
		this.descargaprecoveiculo = descargaprecoveiculo;
	}
}
