package br.com.linkcom.wms.geral.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@SequenceGenerator(name = "sq_diadeentrega", sequenceName = "sq_diadeentrega")
public class Diadeentrega {

	private Integer cddiadeentrega; 
	private Date dia;
	private Boolean segunda;
	private Boolean terca;
	private Boolean quarta;
	private Boolean quinta;
	private Boolean sexta;
	private Boolean sabado;
	private Boolean domingo;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_diadeentrega")
	public Integer getCddiadeentrega() {
		return cddiadeentrega;
	}
	public Date getDia() {
		return dia;
	}
	public Boolean getSegunda() {
		return segunda;
	}
	public Boolean getTerca() {
		return terca;
	}
	public Boolean getQuarta() {
		return quarta;
	}
	public Boolean getQuinta() {
		return quinta;
	}
	public Boolean getSexta() {
		return sexta;
	}
	public Boolean getSabado() {
		return sabado;
	}
	public Boolean getDomingo() {
		return domingo;
	}
	
	public void setSegunda(Boolean segunda) {
		this.segunda = segunda;
	}
	public void setTerca(Boolean terca) {
		this.terca = terca;
	}
	public void setQuarta(Boolean quarta) {
		this.quarta = quarta;
	}
	public void setQuinta(Boolean quinta) {
		this.quinta = quinta;
	}
	public void setSexta(Boolean sexta) {
		this.sexta = sexta;
	}
	public void setSabado(Boolean sabado) {
		this.sabado = sabado;
	}
	public void setDomingo(Boolean domingo) {
		this.domingo = domingo;
	}
	public void setCddiadeentrega(Integer cddiadeentrega) {
		this.cddiadeentrega = cddiadeentrega;
	}
	public void setDia(Date dia) {
		this.dia = dia;
	}
}