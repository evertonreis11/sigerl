package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
public class Turnodeentrega {
	
	public static Turnodeentrega MANHA = new Turnodeentrega(1,"MANHÃ");
	public static Turnodeentrega TARDE = new Turnodeentrega(2,"TARDE");
	public static Turnodeentrega NOITE = new Turnodeentrega(3,"NOITE");
	
	private Integer cdturnodeentrega;
	private String turno;
	private Boolean ativo;
	
	public Turnodeentrega(){}
	
	public Turnodeentrega(Integer cd){
		this.cdturnodeentrega = cd;
	}
	
	public Turnodeentrega(Integer cd, String turno){
		this.cdturnodeentrega = cd;
		this.turno = turno;
	}
	
	@Id
	@DisplayName("Id")
	public Integer getCdturnodeentrega() {
		return cdturnodeentrega;
	}
	@DescriptionProperty
	public String getTurno() {
		return turno;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	public void setCdturnodeentrega(Integer cdturnodeentrega) {
		this.cdturnodeentrega = cdturnodeentrega;
	}
	
	public void setTurno(String turno) {
		this.turno = turno;
	}
}
