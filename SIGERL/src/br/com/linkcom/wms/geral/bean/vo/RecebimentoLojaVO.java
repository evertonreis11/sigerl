package br.com.linkcom.wms.geral.bean.vo;

public class RecebimentoLojaVO {
	
	private Integer cdManifesto;

	private Integer cdProduto;
	
	private String codigoProduto;
	
	private String descricaoProduto;
	
	private Integer cdProdutoCodigoBarras;
	
	private String codigoBarras;
	
	private Integer cdNotaFiscalSaida;
	
	private String numeroPedido;
	
	private Integer qtde;

	public Integer getCdManifesto() {
		return cdManifesto;
	}

	public void setCdManifesto(Integer cdManifesto) {
		this.cdManifesto = cdManifesto;
	}

	public Integer getCdProduto() {
		return cdProduto;
	}

	public void setCdProduto(Integer cdProduto) {
		this.cdProduto = cdProduto;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public Integer getCdProdutoCodigoBarras() {
		return cdProdutoCodigoBarras;
	}

	public void setCdProdutoCodigoBarras(Integer cdProdutoCodigoBarras) {
		this.cdProdutoCodigoBarras = cdProdutoCodigoBarras;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public Integer getCdNotaFiscalSaida() {
		return cdNotaFiscalSaida;
	}

	public void setCdNotaFiscalSaida(Integer cdNotaFiscalSaida) {
		this.cdNotaFiscalSaida = cdNotaFiscalSaida;
	}

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public Integer getQtde() {
		return qtde;
	}

	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}

}
