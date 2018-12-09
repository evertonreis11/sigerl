package br.com.linkcom.wms.geral.bean.vo;

import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Produto;

public class CodigobarrasVO {
	protected Ordemservicoproduto ordemservicoproduto;
	protected Produto produto;
	protected Produto produtoprincipal;
	protected String codigo;
	protected String codigoProduto;
	protected String embalgem;
	protected Integer cdprodutoembalagem;
	protected Integer qtde;
	protected Ordemprodutohistorico ordemprodutohistorico;
	
	//atributo para marcar os bipes do volume.
	protected Integer qtdeBipe = 0;
	 
	protected Etiquetaexpedicao etiquetaexpedicao;
	
	public Ordemservicoproduto getOrdemservicoproduto() {
		return ordemservicoproduto;
	}
	public Produto getProduto() {
		return produto;
	}
	public Produto getProdutoprincipal() {
		return produtoprincipal;
	}
	public String getCodigo() {
		return codigo;
	}
	public String getEmbalgem() {
		return embalgem;
	}
	public Integer getQtde() {
		return qtde;
	}
	public Integer getQtdeBipe() {
		return qtdeBipe;
	}
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public Etiquetaexpedicao getEtiquetaexpedicao() {
		return etiquetaexpedicao;
	}
	public Integer getCdprodutoembalagem() {
		return cdprodutoembalagem;
	}
	public void setOrdemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		this.ordemservicoproduto = ordemservicoproduto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public void setProdutoprincipal(Produto produtoprincipal) {
		this.produtoprincipal = produtoprincipal;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setEmbalgem(String embalgem) {
		this.embalgem = embalgem;
	}
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	public void setQtdeBipe(Integer qtdeBipe) {
		this.qtdeBipe = qtdeBipe;
	}
	public void setEtiquetaexpedicao(Etiquetaexpedicao etiquetaexpedicao) {
		this.etiquetaexpedicao = etiquetaexpedicao;
	}
	public Ordemprodutohistorico getOrdemprodutohistorico() {
		return ordemprodutohistorico;
	}
	public void setOrdemprodutohistorico(Ordemprodutohistorico ordemprodutohistorico) {
		this.ordemprodutohistorico = ordemprodutohistorico;
	}
	public void setCdprodutoembalagem(Integer cdprodutoembalagem) {
		this.cdprodutoembalagem = cdprodutoembalagem;
	}
}
