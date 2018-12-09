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
@SequenceGenerator(name = "sq_ocupacaoendereco", sequenceName = "sq_ocupacaoendereco")
public class OcupacaoEndereco {

	protected Integer cdocupacaoendereco;
	protected Notafiscalentradaproduto notafiscalentradaproduto;
	protected Endereco endereco;
	protected Produto produto;
	protected Long qtde;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_ocupacaoendereco")
	public Integer getCdocupacaoendereco() {
		return cdocupacaoendereco;
	}

	public void setCdocupacaoendereco(Integer cdocupacaoendereco) {
		this.cdocupacaoendereco = cdocupacaoendereco;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdnotafiscalentradaproduto")
	public Notafiscalentradaproduto getNotafiscalentradaproduto() {
		return notafiscalentradaproduto;
	}

	public void setNotafiscalentradaproduto(Notafiscalentradaproduto notafiscalentradaproduto) {
		this.notafiscalentradaproduto = notafiscalentradaproduto;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdendereco")
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Long getQtde() {
		return qtde;
	}

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}

}
