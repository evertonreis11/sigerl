package br.com.linkcom.wms.util.recebimento;

public class RecebimentoPapelReportVO {

	private String codigo;
	private String descricao;
	private String norma = "";
	private Boolean exigevalidade;
	private Boolean exigelote;

	public Boolean getExigevalidade() {
		return exigevalidade;
	}

	public Boolean getExigelote() {
		return exigelote;
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

	public String getNorma() {
		return norma;
	}

	public void setNorma(String norma) {
		this.norma = norma;
	}

	public void setExigevalidade(Boolean exigevalidade) {
		this.exigevalidade = exigevalidade;
	}

	public void setExigelote(Boolean exigelote) {
		this.exigelote = exigelote;
	}
}
