package br.com.linkcom.wms.geral.bean.vo;

public class ProdutoSemEstoqueVO {

	private Integer cdproduto;
	private String codigo;
	private String descricao;
	private Integer qtdepedido;
	private Integer estoque;
	private Integer cdcarregamento;
	private String numeropedido;
	public Integer getCdproduto() {
		return cdproduto;
	}
	public void setCdproduto(Integer cdproduto) {
		this.cdproduto = cdproduto;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getQtdepedido() {
		return qtdepedido;
	}
	public void setQtdepedido(Integer qtdepedido) {
		this.qtdepedido = qtdepedido;
	}
	public Integer getEstoque() {
		return estoque;
	}
	public void setEstoque(Integer estoque) {
		this.estoque = estoque;
	}
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}
	public String getNumeropedido() {
		return numeropedido;
	}
	public void setNumeropedido(String numeropedido) {
		this.numeropedido = numeropedido;
	}
}
