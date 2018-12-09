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

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.MinValue;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_produtoembalagem", sequenceName = "sq_produtoembalagem")
public class Produtoembalagem {
	
	// constantes
	public static final Integer COMPRA = new Integer(1);
	
	// variáveis de instância
	protected Integer cdprodutoembalagem;
	protected Produto produto;
	protected String descricao;
	protected Long qtde;
	protected Double fator;
	protected Boolean compra;
	protected Boolean origemerp;

	// Construtores
	public Produtoembalagem() {
	
	}
	
	public Produtoembalagem(Integer cd) {
		this.cdprodutoembalagem = cd;
	}
	
	public Produtoembalagem(Integer cdprodutoembalagem, String descricaoProdutoembalagem) {
		this.cdprodutoembalagem = cdprodutoembalagem;
		this.descricao = descricaoProdutoembalagem;
	}
	
	public Produtoembalagem(Integer cdprodutoembalagem, String descricaoProdutoembalagem, Long qtde) {
		this.cdprodutoembalagem = cdprodutoembalagem;
		this.descricao = descricaoProdutoembalagem;
		this.qtde = qtde;
	}
	
	// Métodos get e set
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_produtoembalagem")
	public Integer getCdprodutoembalagem() {
		return cdprodutoembalagem;
	}
	public void setCdprodutoembalagem(Integer id) {
		this.cdprodutoembalagem = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	public Produto getProduto() {
		return produto;
	}
	
	@Required
	@MaxLength(30)
	public String getDescricao() {
		return descricao;
	}
	
	@DisplayName("Qtde. de unidades")
	@MinValue(1)
	@Required
	public Long getQtde() {
		return qtde;
	}
	
	public Double getFator() {
		return fator;
	}
	
	@DisplayName("Embalagem de recebimento")
	public Boolean getCompra() {
		return compra;
	}
	
	@Transient
	@DisplayName("Descrição")
	@MaxLength(30)
	public String getDescricaotransiente() {
		return descricao;
	}

	@Transient
	@DisplayName("Qtde. de unidades")
	public Long getQtetransiente() {
		return qtde;
	}
	
	
	public Boolean getOrigemerp() {
		return origemerp;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	
	public void setFator(Double fator) {
		this.fator = fator;
	}
	
	public void setCompra(Boolean compra) {
		this.compra = compra;
	}
	
	public void setOrigemerp(Boolean origemerp) {
		this.origemerp = origemerp;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Produtoembalagem) {
			Produtoembalagem produtoembalagem = (Produtoembalagem) obj;
			
			return this.cdprodutoembalagem.equals(produtoembalagem.getCdprodutoembalagem());
		}
		
		return super.equals(obj);
	}
	
	@Transient
	@DescriptionProperty
	public String getDescricaoEmbalagem(){
		if (qtde != null)
			return descricao + " - " + qtde;
		else
			return descricao;
	}
	
}
