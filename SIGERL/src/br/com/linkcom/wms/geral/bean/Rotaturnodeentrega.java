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
@SequenceGenerator(name = "sq_rotaturnodeentrega", sequenceName = "sq_rotaturnodeentrega")
public class Rotaturnodeentrega {

	private Integer cdrotaturnodeentrega; 
	private Turnodeentrega turnodeentrega; 
	private Rota rota; 
	private Integer maximoentregas; 
	private Integer maximovalor; 
	private Integer maximacubagem; 
	private Integer maximopeso;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_rotaturnodeentrega")
	public Integer getCdrotaturnodeentrega() {
		return cdrotaturnodeentrega;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdturnodeentrega")
	public Turnodeentrega getTurnodeentrega() {
		return turnodeentrega;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdrota")
	public Rota getRota() {
		return rota;
	}
	public Integer getMaximoentregas() {
		return maximoentregas;
	}
	public Integer getMaximovalor() {
		return maximovalor;
	}		
	public Integer getMaximacubagem() {
		return maximacubagem;
	}
	public Integer getMaximopeso() {
		return maximopeso;
	}

	public void setCdrotaturnodeentrega(Integer cdrotaturnodeentrega) {
		this.cdrotaturnodeentrega = cdrotaturnodeentrega;
	}
	public void setTurnodeentrega(Turnodeentrega turnodeentrega) {
		this.turnodeentrega = turnodeentrega;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
	}
	public void setMaximoentregas(Integer maximoentregas) {
		this.maximoentregas = maximoentregas;
	}
	public void setMaximovalor(Integer maximovalor) {
		this.maximovalor = maximovalor;
	}
	public void setMaximacubagem(Integer maximacubagem) {
		this.maximacubagem = maximacubagem;
	}
	public void setMaximopeso(Integer maximopeso) {
		this.maximopeso = maximopeso;
	}
}