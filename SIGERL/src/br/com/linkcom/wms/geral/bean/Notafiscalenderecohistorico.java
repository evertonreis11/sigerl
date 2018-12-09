package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_notafiscalenderecohistorico", sequenceName = "sq_notafiscalenderecohistorico")
public class Notafiscalenderecohistorico {

	protected Integer cdnotafiscalenderecohistorico;
	protected Notafiscalentradaproduto notafiscalentradaproduto;
	protected Endereco endereco;
	protected Integer  qtde;
	protected Timestamp dtentrada;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_notafiscalenderecohistorico")
	public Integer getCdnotafiscalenderecohistorico() {
		return cdnotafiscalenderecohistorico;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdnotafiscalentradaproduto")
	@Required
	public Notafiscalentradaproduto getNotafiscalentradaproduto() {
		return notafiscalentradaproduto;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdendereco")
	@Required
	public Endereco getEndereco() {
		return endereco;
	}
	
	@Required
	@DisplayName("Qtde.")
	@MaxLength(9)
	public Integer getQtde() {
		return qtde;
	}
	
	@Required
	@DisplayName("Data entrada")
	public Timestamp getDtentrada() {
		return dtentrada;
	}
	
	public void setCdnotafiscalenderecohistorico(
			Integer cdnotafiscalenderecohistorico) {
		this.cdnotafiscalenderecohistorico = cdnotafiscalenderecohistorico;
	}
	public void setNotafiscalentradaproduto(
			Notafiscalentradaproduto notafiscalentradaproduto) {
		this.notafiscalentradaproduto = notafiscalentradaproduto;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	public void setDtentrada(Timestamp dtentrada) {
		this.dtentrada = dtentrada;
	}
	
}
