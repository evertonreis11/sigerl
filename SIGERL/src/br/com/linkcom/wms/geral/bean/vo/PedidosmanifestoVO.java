package br.com.linkcom.wms.geral.bean.vo;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;

public class PedidosmanifestoVO {
	
	private String loja;
	private String pedido;
	private String tipoentrega;
	private String codigo;
	private String descricao;
	private Long qtde;
	private Date dataNota;
	private Long numeroNota;
	private Integer cdnotasaidaprodutoreferencia;
	private Integer cdnotafiscalsaidaproduto;
	
	
	//Get's
	public String getLoja() {
		return loja;
	}
	public String getPedido() {
		return pedido;
	}
	public String getTipoentrega() {
		return tipoentrega;
	}
	public String getCodigo() {
		return codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public Long getQtde() {
		return qtde;
	}
	@DisplayName("Dt. Chegada")
	public Date getDataNota() {
		return dataNota;
	}
	@DisplayName("Nº Nota")
	public Long getNumeroNota() {
		return numeroNota;
	}
	public Integer getCdnotasaidaprodutoreferencia() {
		return cdnotasaidaprodutoreferencia;
	}
	public Integer getCdnotafiscalsaidaproduto() {
		return cdnotafiscalsaidaproduto;
	}
	
	
	//Set's
	public void setLoja(String loja) {
		this.loja = loja;
	}
	public void setPedido(String pedido) {
		this.pedido = pedido;
	}
	public void setTipoentrega(String tipoentrega) {
		this.tipoentrega = tipoentrega;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	public void setDataNota(Date dataNota) {
		this.dataNota = dataNota;
	}
	public void setNumeroNota(Long numeroNota) {
		this.numeroNota = numeroNota;
	}
	public void setCdnotasaidaprodutoreferencia(Integer cdnotasaidaprodutoreferencia) {
		this.cdnotasaidaprodutoreferencia = cdnotasaidaprodutoreferencia;
	}
	public void setCdnotafiscalsaidaproduto(Integer cdnotafiscalsaidaproduto) {
		this.cdnotafiscalsaidaproduto = cdnotafiscalsaidaproduto;
	}
}
