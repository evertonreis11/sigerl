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
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_tabelafretepraca", sequenceName = "sq_tabelafretepraca")
public class Tabelafretepraca {

	private Integer cdtabelafretepraca;
	private Praca praca;
	private Rota rota;
	private Tabelafreterota tabelafreterota;
	private Tabelafrete tabelafrete;
	private Money valorentrega;
	
	//Transient
	private Boolean isUpdate = Boolean.FALSE; 
	
	
	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_tabelafretepraca")
	public Integer getCdtabelafretepraca() {
		return cdtabelafretepraca;
	}
	@Required
    @DisplayName("Praça")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpraca")
	public Praca getPraca() {
		return praca;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdrota")
	public Rota getRota() {
		return rota;
	}
	@Required
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtabelafreterota")
	public Tabelafreterota getTabelafreterota() {
		return tabelafreterota;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtabelafrete")
	public Tabelafrete getTabelafrete() {
		return tabelafrete;
	}
	@Required
	public Money getValorentrega() {
		return valorentrega;
	}
	
	
	//Set's
	public void setCdtabelafretepraca(Integer cdtabelafretepraca) {
		this.cdtabelafretepraca = cdtabelafretepraca;
	}
	public void setPraca(Praca praca) {
		this.praca = praca;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
	}
	public void setTabelafreterota(Tabelafreterota tabelafreterota) {
		this.tabelafreterota = tabelafreterota;
	}
	public void setTabelafrete(Tabelafrete tabelafrete) {
		this.tabelafrete = tabelafrete;
	}
	public void setValorentrega(Money valorentrega) {
		this.valorentrega = valorentrega;
	}
	
	
	//Transient's
	@Transient
	public Boolean getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
	
}
