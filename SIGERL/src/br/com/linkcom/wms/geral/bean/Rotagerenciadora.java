package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
public class Rotagerenciadora {

	private Integer cdrotagerenciadora;
	private Deposito deposito;
	private String descricao;
	
	//Get's
	@Id
	public Integer getCdrotagerenciadora() {
		return cdrotagerenciadora;
	}
	@Required
    @DisplayName("Depósito")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")	
	public Deposito getDeposito() {
		return deposito;
	}
	@DescriptionProperty
	@Required
    @DisplayName("Descrição")
	public String getDescricao() {
		return descricao;
	}
	
	//Set's
	public void setCdrotagerenciadora(Integer cdrotagerenciadora) {
		this.cdrotagerenciadora = cdrotagerenciadora;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
