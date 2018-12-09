package br.com.linkcom.wms.geral.bean.vo;

public class TipoenderecoVO {

	private String nome;
	private Integer totalEnderecos;
	private Integer ocupados;

	public String getNome() {
		return nome;
	}

	public Integer getTotalEnderecos() {
		return totalEnderecos;
	}

	public Integer getOcupados() {
		return ocupados;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setTotalEnderecos(Integer totalEnderecos) {
		this.totalEnderecos = totalEnderecos;
	}

	public void setOcupados(Integer ocupados) {
		this.ocupados = ocupados;
	}

}
