package br.com.linkcom.wms.geral.bean.vo;

import br.com.linkcom.neo.types.Money;

public class ProdutosaldoVO {

	private String codigoproduto;
	private String descricaoproduto;
	private Long cddeposito;
	private String nomedeposito;
	private Long qtde;
	private Money valorunitario;
	private Money valortotal;
	private Double peso;
	private Double pesototal;
	private Double cubagem;
	private Long qtdePallet;
	private Long qtdePalletReal;
	private Boolean larguraexcedente = false;

	public Double getPesototal() {
		return pesototal;
	}

	public Double getPeso() {
		return peso;
	}
	
	public String getCodigoproduto() {
		return codigoproduto;
	}

	public void setCodigoproduto(String codigoproduto) {
		this.codigoproduto = codigoproduto;
	}

	public String getDescricaoproduto() {
		return descricaoproduto;
	}

	public void setDescricaoproduto(String descricaoproduto) {
		this.descricaoproduto = descricaoproduto;
	}

	public Long getQtde() {
		return qtde;
	}

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}

	public Money getValorunitario() {
		return valorunitario;
	}

	public void setValorunitario(Money valorunitario) {
		this.valorunitario = valorunitario;
	}

	public Money getValortotal() {
		return valortotal;
	}

	public void setValortotal(Money valortotal) {
		this.valortotal = valortotal;
	}

	public Long getCddeposito() {
		return cddeposito;
	}

	public void setCddeposito(Long cddeposito) {
		this.cddeposito = cddeposito;
	}

	public String getNomedeposito() {
		return nomedeposito;
	}

	public void setNomedeposito(String nomedeposito) {
		this.nomedeposito = nomedeposito;
	}
	
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	public void setPesototal(Double pesototal) {
		this.pesototal = pesototal;
	}
	
	public Double getCubagem() {
		return cubagem;
	}
	
	public void setCubagem(Double cubagem) {
		this.cubagem = cubagem;
	}
	
	public Long getQtdePallet() {
		return qtdePallet;
	}
	
	public void setQtdePallet(Long qtdePallet) {
		this.qtdePallet = qtdePallet;
	}
	
	public Long getQtdePalletReal() {
		return qtdePalletReal;
	}
	
	public void setQtdePalletReal(Long qtdePalletReal) {
		this.qtdePalletReal = qtdePalletReal;
	}

	public Boolean getLarguraexcedente() {
		return larguraexcedente;
	}
	
	public void setLarguraexcedente(Boolean larguraexcedente) {
		this.larguraexcedente = larguraexcedente;
	}
	
}
