package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_produtocodigobarras", sequenceName = "sq_produtocodigobarras")
public class Produtocodigobarras {

	protected Integer cdprodutocodigobarras;
	protected Produto produto;
	protected String codigo;
	protected Boolean valido;
	protected Boolean interno;
	protected Produtoembalagem produtoembalagem;
	/*Queiroz - 05-05-2014 
	 * Acrescentando o campo Principal que indica qual o codigo de barras 
	 * é o principal do produto*/
	protected Boolean principal;
	
	//Transientes
	protected String tipo;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_produtocodigobarras")
	public Integer getCdprodutocodigobarras() {
		return cdprodutocodigobarras;
	}
	public void setCdprodutocodigobarras(Integer id) {
		this.cdprodutocodigobarras = id;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	public Produto getProduto() {
		return produto;
	}
	
	@Required
	@MaxLength(20)
	public String getCodigo() {
		return codigo;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdprodutoembalagem")
	@DisplayName("Descrição da embalagem")
	public Produtoembalagem getProdutoembalagem() {
		return produtoembalagem;
	}
	
	@Transient
	public String getTipo() {
		return tipo;
	}
	
	public Boolean getValido() {
		return valido;
	}
	
	public Boolean getPrincipal() {
		return principal;
	}
	
	public Boolean getInterno() {
		return interno;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public void setProdutoembalagem(Produtoembalagem produtoembalagem) {
		this.produtoembalagem = produtoembalagem;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public void setValido(Boolean valido) {
		this.valido = valido;
	}
	public void setInterno(Boolean interno) {
		this.interno = interno;
	}
	
	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}
}
