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
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_controleestoqueproduto", sequenceName = "sq_controleestoqueproduto")
public class Controleestoqueproduto {

	protected Integer cdcontroleestoqueproduto;
	protected Controleestoque controleestoque;
	protected Produto produto;
	protected Long qtdeesperada;
	protected Long qtde;
	protected Long qtdevolumesdivergentes;
	protected Long qtdeavaria;

	public Controleestoqueproduto(Integer cd){
		this.cdcontroleestoqueproduto = cd;
	}
	
	public Controleestoqueproduto() {
	}

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_controleestoqueproduto")
	public Integer getCdcontroleestoqueproduto() {
		return cdcontroleestoqueproduto;
	}

	@Required
	@DisplayName("Comparação de estoque")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdcontroleestoque")
	public Controleestoque getControleestoque() {
		return controleestoque;
	}

	@Required
	@DisplayName("Produto")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdproduto")
	public Produto getProduto() {
		return produto;
	}

	@Required
	@DisplayName("Qtde. esperada")
	public Long getQtdeesperada() {
		return qtdeesperada;
	}

	@Required
	@DisplayName("Qtde.")
	public Long getQtde() {
		return qtde;
	}	
	
	@DisplayName("Qtde. Avaria")
	public Long getQtdeavaria() {
		return qtdeavaria;
	}
	
	@DisplayName("Qtde. Volumes divergentes")
	public Long getQtdevolumesdivergentes() {
		return qtdevolumesdivergentes;
	}

	public void setQtdevolumesdivergentes(Long qtdevolumesdivergentes) {
		this.qtdevolumesdivergentes = qtdevolumesdivergentes;
	}

	public void setQtdeavaria(Long qtdeavaria) {
		this.qtdeavaria = qtdeavaria;
	}

	public void setCdcontroleestoqueproduto(Integer cdcontroleestoqueproduto) {
		this.cdcontroleestoqueproduto = cdcontroleestoqueproduto;
	}

	public void setControleestoque(Controleestoque controleestoque) {
		this.controleestoque = controleestoque;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setQtdeesperada(Long qtdeesperada) {
		this.qtdeesperada = qtdeesperada;
	}

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}

}
