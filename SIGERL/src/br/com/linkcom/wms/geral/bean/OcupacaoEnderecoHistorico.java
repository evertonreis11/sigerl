package br.com.linkcom.wms.geral.bean;

import java.sql.Date;
import java.sql.Timestamp;

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

@Entity
@SequenceGenerator(name = "sq_ocupacaoenderecohistorico", sequenceName = "sq_ocupacaoenderecohistorico")
public class OcupacaoEnderecoHistorico {

	protected Integer cdocupacaoenderecohistorico;
//	protected Notafiscalentradaproduto notafiscalentradaproduto;
	protected Endereco endereco;
	protected Produto produto;
	protected Long qtde;
	protected Timestamp dtentrada;
	protected Ordemservico ordemservico;
	protected Integer acumula;
	protected Date dtestoque;
	
	//Propriedades transientes
	protected Long qtdeEntrada;
	protected Long qtdeSaida;

	
	public OcupacaoEnderecoHistorico(){
	}

	public OcupacaoEnderecoHistorico(Endereco endereco, Produto produto, Timestamp dtentrada, Long qtde){
		this.endereco = endereco;
		this.produto = produto;
		this.qtde = qtde;
		this.dtentrada = dtentrada;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_ocupacaoenderecohistorico")
	public Integer getCdocupacaoenderecohistorico() {
		return cdocupacaoenderecohistorico;
	}

	public void setCdocupacaoenderecohistorico(Integer cdocupacaoenderecohistorico) {
		this.cdocupacaoenderecohistorico = cdocupacaoenderecohistorico;
	}

/*	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdnotafiscalentradaproduto")
	public Notafiscalentradaproduto getNotafiscalentradaproduto() {
		return notafiscalentradaproduto;
	}

	public void setNotafiscalentradaproduto(Notafiscalentradaproduto notafiscalentradaproduto) {
		this.notafiscalentradaproduto = notafiscalentradaproduto;
	}*/

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

	public Timestamp getDtentrada() {
		return dtentrada;
	}

	public void setDtentrada(Timestamp dtentrada) {
		this.dtentrada = dtentrada;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdordemservico")
	public Ordemservico getOrdemservico() {
		return ordemservico;
	}
	
	public Integer getAcumula() {
		return acumula;
	}

	public Date getDtestoque() {
		return dtestoque;
	}

	public void setAcumula(Integer acumula) {
		this.acumula = acumula;
	}

	public void setDtestoque(Date dtestoque) {
		this.dtestoque = dtestoque;
	}

	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}

	//Propriedades transientes
	@Transient
	public Long getQtdeEntrada() {
		return qtdeEntrada;
	}

	public void setQtdeEntrada(Long qtdeEntrada) {
		this.qtdeEntrada = qtdeEntrada;
	}

	@Transient
	public Long getQtdeSaida() {
		return qtdeSaida;
	}

	public void setQtdeSaida(Long qtdeSaida) {
		this.qtdeSaida = qtdeSaida;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof OcupacaoEnderecoHistorico){
			OcupacaoEnderecoHistorico ocupacaoEnderecoHistorico = (OcupacaoEnderecoHistorico)obj;
			if(this.endereco != null && this.endereco.cdendereco != null && ocupacaoEnderecoHistorico.getEndereco() != null && ocupacaoEnderecoHistorico.getEndereco().getCdendereco() != null)
				return this.endereco.equals(ocupacaoEnderecoHistorico.getEndereco()) && this.produto.equals(ocupacaoEnderecoHistorico.getProduto());
			else
				return this.produto.equals(ocupacaoEnderecoHistorico.getProduto());
		}
		return super.equals(obj);
	}
	
}
