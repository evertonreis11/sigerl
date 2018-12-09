package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Vseparacaoreport {
	
	protected Integer cdordemservico;
	protected Integer cdcarregamento;
	protected Carregamento carregamento;
	protected Integer cdbox;
	protected String nomebox;
	protected Integer cdproduto;
	protected String codigo;
	protected String descricao;
	protected Integer cdprodutoprincipal;
	protected String codigoprincipal;
	protected String descricaoprincipal;
	protected Integer cdlinhaseparacao;
	protected String nomelinha;
	protected Endereco endereco;
	protected Integer cdordemservicoprodutoendereco;
	protected Long qtde;
	protected Integer cdordemservicoproduto;
	protected Ordemservicoproduto ordemservicoproduto;
	protected String complementocodigobarras;
	protected Integer cddeposito;
	protected Double peso;
	protected Double cubagem;
	protected Long qtdeesperada;
	
	public Double getCubagem() {
		return cubagem;
	}
	public void setCubagem(Double cubagem) {
		this.cubagem = cubagem;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	public Integer getCdordemservico() {
		return cdordemservico;
	}
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	public Integer getCdbox() {
		return cdbox;
	}
	public String getNomebox() {
		return nomebox;
	}
	public Integer getCdproduto() {
		return cdproduto;
	}
	public String getCodigo() {
		return codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public Integer getCdprodutoprincipal() {
		return cdprodutoprincipal;
	}
	public String getCodigoprincipal() {
		return codigoprincipal;
	}
	public String getDescricaoprincipal() {
		return descricaoprincipal;
	}
	public Integer getCdlinhaseparacao() {
		return cdlinhaseparacao;
	}
	public String getNomelinha() {
		return nomelinha;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdendereco")
	public Endereco getEndereco() {
		return endereco;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdcarregamento", insertable=false, updatable=false)
	public Carregamento getCarregamento() {
		return carregamento;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdordemservicoproduto", insertable=false, updatable=false)
	public Ordemservicoproduto getOrdemservicoproduto() {
		return ordemservicoproduto;
	}
	
	@Id
	public Integer getCdordemservicoprodutoendereco() {
		return cdordemservicoprodutoendereco;
	}
	public Long getQtde() {
		return qtde;
	}
	public Integer getCdordemservicoproduto() {
		return cdordemservicoproduto;
	}
	public String getComplementocodigobarras() {
		return complementocodigobarras;
	}
	public Integer getCddeposito() {
		return cddeposito;
	}
	public void setCdordemservico(Integer cdordemservico) {
		this.cdordemservico = cdordemservico;
	}
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}
	public void setCdbox(Integer cdbox) {
		this.cdbox = cdbox;
	}
	public void setNomebox(String nomebox) {
		this.nomebox = nomebox;
	}
	public void setCdproduto(Integer cdproduto) {
		this.cdproduto = cdproduto;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setCdprodutoprincipal(Integer cdprodutoprincipal) {
		this.cdprodutoprincipal = cdprodutoprincipal;
	}
	public void setCodigoprincipal(String codigoprincipal) {
		this.codigoprincipal = codigoprincipal;
	}
	public void setDescricaoprincipal(String descricaoprincipal) {
		this.descricaoprincipal = descricaoprincipal;
	}
	public void setCdlinhaseparacao(Integer cdlinhaseparacao) {
		this.cdlinhaseparacao = cdlinhaseparacao;
	}
	public void setNomelinha(String nomelinha) {
		this.nomelinha = nomelinha;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public void setCdordemservicoprodutoendereco(Integer cdordemservicoprodutoendereco) {
		this.cdordemservicoprodutoendereco = cdordemservicoprodutoendereco;
	}
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	public void setCdordemservicoproduto(Integer cdordemservicoproduto) {
		this.cdordemservicoproduto = cdordemservicoproduto;
	}
	public void setComplementocodigobarras(String complementocodigobarras) {
		this.complementocodigobarras = complementocodigobarras;
	}
	public void setCddeposito(Integer cddeposito) {
		this.cddeposito = cddeposito;
	}
	public Long getQtdeesperada() {
		return qtdeesperada;
	}
	public void setQtdeesperada(Long qtdeesperada) {
		this.qtdeesperada = qtdeesperada;
	}
	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}
	public void setOrdemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		this.ordemservicoproduto = ordemservicoproduto;
	}
	
	
}
