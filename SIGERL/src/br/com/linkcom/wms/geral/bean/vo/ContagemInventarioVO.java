package br.com.linkcom.wms.geral.bean.vo;


public class ContagemInventarioVO {

	private Integer[] leituras;
	private String codigoProduto;
	private String descricaoProduto;
	private String endereco;
	private Integer qtdeEsperada;
	private Integer valorFinal;

	public ContagemInventarioVO(int numContagens){
		this.leituras = new Integer[numContagens];
	}
	
	public void setLeitura(int ordem, Integer qtde) {
		this.leituras[ordem - 1] = qtde;
	}

	public void setValorFinal(Integer qtde) {
		this.valorFinal = qtde;
	}

	public void setCodigoProduto(String codigo) {
		this.codigoProduto = codigo;
	}

	public void setDescricaoProduto(String descricao) {
		this.descricaoProduto = descricao;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public void setQtdeEsperada(Integer qtde) {
		this.qtdeEsperada = qtde;
	}

	public Integer[] getLeituras() {
		return leituras;
	}

	public void setLeituras(Integer[] leituras) {
		this.leituras = leituras;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public String getEndereco() {
		return endereco;
	}

	public Integer getQtdeEsperada() {
		return qtdeEsperada;
	}

	public Integer getValorFinal() {
		return valorFinal;
	}

}
