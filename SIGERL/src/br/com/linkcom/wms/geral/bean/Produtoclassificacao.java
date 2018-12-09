package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;


@Entity
@SequenceGenerator(sequenceName = "sq_produtoclassificacao", name = "sq_produtoclassificacao")
public class Produtoclassificacao {

	protected Integer cdprodutoclassificacao;
	protected Produto produto;
	protected Classificacao classificacao;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_produtoclassificacao")
	public Integer getCdprodutoclassificacao() {
		return cdprodutoclassificacao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdproduto")
	public Produto getProduto() {
		return produto;
	}
	
	@DisplayName("Classificação")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdclassificacao")
	public Classificacao getClassificacao() {
		return classificacao;
	}
	
	public void setCdprodutoclassificacao(Integer cdprodutoclassificacao) {
		this.cdprodutoclassificacao = cdprodutoclassificacao;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public void setClassificacao(Classificacao classificacao) {
		this.classificacao = classificacao;
	}
}
