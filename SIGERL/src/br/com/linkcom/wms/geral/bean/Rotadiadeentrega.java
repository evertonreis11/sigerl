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
@SequenceGenerator(name = "sq_rotadiadeentrega", sequenceName = "sq_rotadiadeentrega")
public class Rotadiadeentrega {

	private Integer cdrotadiadeentrega; 
	private Rota rota;
	private Diadeentrega diadeentrega;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_rotadiadeentrega")
	public Integer getCdrotadiadeentrega() {
		return cdrotadiadeentrega;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdrota")
	public Rota getRota() {
		return rota;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddiadeentrega")
	public Diadeentrega getDiadeentrega() {
		return diadeentrega;
	}
	
	public void setCdrotadiadeentrega(Integer cdrotadiadeentrega) {
		this.cdrotadiadeentrega = cdrotadiadeentrega;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
	}
	public void setDiadeentrega(Diadeentrega diadeentrega) {
		this.diadeentrega = diadeentrega;
	}
}
