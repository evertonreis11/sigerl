package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "sq_manifestobordero", sequenceName = "sq_manifestobordero")
public class Manifestobordero {
	
	private Integer cdmanifestobordero;
	private Manifesto manifesto;
	private Bordero bordero;
	private Empresa empresa;
	
	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_manifestobordero")
	public Integer getCdmanifestobordero() {
		return cdmanifestobordero;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdmanifesto")
	public Manifesto getManifesto() {
		return manifesto;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdbordero")
	public Bordero getBordero() {
		return bordero;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdempresa")	
	public Empresa getEmpresa() {
		return empresa;
	}
	
	//Set's
	
	public void setCdmanifestobordero(Integer cdmanifestobordero) {
		this.cdmanifestobordero = cdmanifestobordero;
	}
	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}
	public void setBordero(Bordero bordero) {
		this.bordero = bordero;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
}
