package br.com.linkcom.wms.util.expedicao;

import java.sql.Date;

public class ItemSeparacaoVO {

	private Integer cdordemservico;
	private Integer cdcarregamento;
	private Integer cdexpedicao;
	private Integer cdprodutoembalagem;
	private String linhaseparacao;
	private Date data;
	private String box;
	private String codigo;
	private String descricaoPrincipal;
	private String descricao;
	private String complementocodigobarras;
	private Long qtdeesperada;
	private String area;
	private String endereco;
	private Long qtdeendereco;
	private String tipoPedido;
	private String filialEntrega;
	private Double peso;
	private Double cubagem;
	private Integer qtdeembalagem;
	

	public Integer getCdordemservico() {
		return cdordemservico;
	}

	public Integer getCdcarregamento() {
		return cdcarregamento;
	}

	public Integer getCdexpedicao() {
		return cdexpedicao;
	}

	public String getLinhaseparacao() {
		return linhaseparacao;
	}

	public Date getData() {
		return data;
	}

	public String getBox() {
		return box;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricaoPrincipal() {
		return descricaoPrincipal;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getComplementocodigobarras() {
		return complementocodigobarras;
	}

	public Long getQtdeesperada() {
		return qtdeesperada;
	}

	public String getArea() {
		return area;
	}

	public String getEndereco() {
		return endereco;
	}

	public Long getQtdeendereco() {
		return qtdeendereco;
	}
	
	public String getTipoPedido() {
		return tipoPedido;
	}
	
	public String getFilialEntrega() {
		return filialEntrega;
	}
	
	public Double getPeso() {
		return peso;
	}
	
	public Double getCubagem() {
		return cubagem;
	}
	
	public Integer getCdprodutoembalagem() {
		return cdprodutoembalagem;
	}

	public Integer getQtdeembalagem() {
		return qtdeembalagem;
	}

	public void setCdordemservico(Integer cdordemservico) {
		this.cdordemservico = cdordemservico;
	}

	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}

	public void setCdexpedicao(Integer cdexpedicao) {
		this.cdexpedicao = cdexpedicao;
	}

	public void setLinhaseparacao(String linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setBox(String box) {
		this.box = box;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescricaoPrincipal(String descricaoPrincipal) {
		this.descricaoPrincipal = descricaoPrincipal;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setComplementocodigobarras(String complementocodigobarras) {
		this.complementocodigobarras = complementocodigobarras;
	}

	public void setQtdeesperada(Long qtdeesperada) {
		this.qtdeesperada = qtdeesperada;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public void setQtdeendereco(Long qtdeendereco) {
		this.qtdeendereco = qtdeendereco;
	}
	
	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido;
	}

	public void setFilialEntrega(String filialEntrega) {
		this.filialEntrega = filialEntrega;
	}
	
	public void setCubagem(Double cubagem) {
		this.cubagem = cubagem;
	}
	
	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public void setCdprodutoembalagem(Integer cdprodutoembalagem) {
		this.cdprodutoembalagem = cdprodutoembalagem;
	}

	public void setQtdeembalagem(Integer qtdeembalagem) {
		this.qtdeembalagem = qtdeembalagem;
	}
	
}