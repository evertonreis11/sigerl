package br.com.linkcom.wms.geral.bean.vo;

public class DescargaprodutoVO {

	private String codigo;
	private String descricao;
	private String voltagem;
	private String status;
	private Long qtde;
	private String loja;
	private String manifesto;
	
	//Get's
	public String getCodigo() {
		return codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public String getVoltagem() {
		return voltagem;
	}
	public String getStatus() {
		return status;
	}
	public Long getQtde() {
		return qtde;
	}
	public String getLoja() {
		return loja;
	}
	public String getManifesto() {
		return manifesto;
	}
	
	
	//Set's
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setVoltagem(String voltagem) {
		this.voltagem = voltagem;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	public void setLoja(String loja) {
		this.loja = loja;
	}
	public void setManifesto(String manifesto) {
		this.manifesto = manifesto;
	}
	
}
