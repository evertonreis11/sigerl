package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "sq_estoqueprodutoloja", sequenceName = "sq_estoqueprodutoloja")
public class EstoqueProdutoLoja {
	
	private Integer cdEstoqueProdutoLoja;
	
	private Produto produto;
	
	private Integer qtde;
	
	private TipoEstoque tipoEstoque;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_estoqueprodutoloja")
	public Integer getCdEstoqueProdutoLoja() {
		return cdEstoqueProdutoLoja;
	}

	public void setCdEstoqueProdutoLoja(Integer cdEstoqueProdutoLoja) {
		this.cdEstoqueProdutoLoja = cdEstoqueProdutoLoja;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Integer getQtde() {
		return qtde;
	}

	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipoestoque")
	public TipoEstoque getTipoEstoque() {
		return tipoEstoque;
	}

	public void setTipoEstoque(TipoEstoque tipoEstoque) {
		this.tipoEstoque = tipoEstoque;
	}
	
}
