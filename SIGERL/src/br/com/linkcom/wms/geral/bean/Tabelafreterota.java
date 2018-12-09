package br.com.linkcom.wms.geral.bean;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_tabelafreterota", sequenceName = "sq_tabelafreterota")
public class Tabelafreterota {
	
	private Integer cdtabelafreterota;
	private Rota rota;
	private Tabelafrete tabelafrete;
	private Money valorentrega;
	private List<Tabelafretepraca> listaTabelafretepraca;
	
	
	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_tabelafreterota")
	public Integer getCdtabelafreterota() {
		return cdtabelafreterota;
	}
	@Required
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdrota")
	public Rota getRota() {
		return rota;
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
    @OneToMany(mappedBy="tabelafreterota")
	public List<Tabelafretepraca> getListaTabelafretepraca() {
		return listaTabelafretepraca;
	}
	
	
	//Set's
	public void setCdtabelafreterota(Integer cdtabelafreterota) {
		this.cdtabelafreterota = cdtabelafreterota;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
	}
	public void setTabelafrete(Tabelafrete tabelafrete) {
		this.tabelafrete = tabelafrete;
	}
	public void setValorentrega(Money valorentrega) {
		this.valorentrega = valorentrega;
	}
	public void setListaTabelafretepraca(List<Tabelafretepraca> listaTabelafretepraca) {
		this.listaTabelafretepraca = listaTabelafretepraca;
	}
	
}
