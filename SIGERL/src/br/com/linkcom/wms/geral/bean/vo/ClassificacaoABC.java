package br.com.linkcom.wms.geral.bean.vo;

public class ClassificacaoABC {

	private String codigo;
	private String descricao;
	private String codigoPrincipal;
	private String descricaoPrincipal;
	private Double valor;
	private Double valorAcumulado;
	private Integer posicao;
	private Double percentual;
	private Double percentualAcumulado;
	private String classificacao;

	public Double getValorAcumulado() {
		return valorAcumulado;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getCodigoPrincipal() {
		return codigoPrincipal;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getDescricaoPrincipal() {
		return descricaoPrincipal;
	}

	public Double getPercentualAcumulado() {
		return percentualAcumulado;
	}
	
	public Double getPercentual() {
		return percentual;
	}

	public Integer getPosicao() {
		return posicao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValorAcumulado(Double acumulado) {
		this.valorAcumulado = acumulado;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setCodigoPrincipal(String codigoPrincipal) {
		this.codigoPrincipal = codigoPrincipal;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setDescricaoPrincipal(String descricaoPrincipal) {
		this.descricaoPrincipal = descricaoPrincipal;
	}

	public void setPercentualAcumulado(Double percentualAcumulado) {
		this.percentualAcumulado = percentualAcumulado;
	}
	
	public void setPercentual(Double percentualContribuicao) {
		this.percentual = percentualContribuicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
