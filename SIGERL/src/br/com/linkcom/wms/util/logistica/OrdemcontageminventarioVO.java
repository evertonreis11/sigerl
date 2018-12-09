package br.com.linkcom.wms.util.logistica;

public class OrdemcontageminventarioVO {
	private Integer cdordemservico;
	private Integer cdinventario;
	private Integer ordem;
	private EnderecoAux enderecoaux;
	private String codigodescricaoproduto;
	private String descricaoprodutoembalagem;
	
	public EnderecoAux getEnderecoaux() {
		return enderecoaux;
	}

	public String getCodigodescricaoproduto() {
		return codigodescricaoproduto;
	}

	public String getDescricaoprodutoembalagem() {
		return descricaoprodutoembalagem;
	}

	public Integer getCdordemservico() {
		return cdordemservico;
	}
	
	public Integer getCdinventario() {
		return cdinventario;
	}
	
	public Integer getOrdem() {
		return ordem;
	}
	
	public void setCdordemservico(Integer cdordemservico) {
		this.cdordemservico = cdordemservico;
	}
	
	public void setCdinventario(Integer cdinventario) {
		this.cdinventario = cdinventario;
	}
	
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	
	public void setEnderecoaux(EnderecoAux enderecoaux) {
		this.enderecoaux = enderecoaux;
	}

	public void setCodigodescricaoproduto(String codigodescricaoproduto) {
		this.codigodescricaoproduto = codigodescricaoproduto;
	}

	public void setDescricaoprodutoembalagem(String descricaoprodutoembalagem) {
		this.descricaoprodutoembalagem = descricaoprodutoembalagem;
	}
}
