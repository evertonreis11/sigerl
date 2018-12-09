package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name = "sq_municipio", sequenceName = "sq_municipio")
public class Municipio {
	
	protected Integer cdmunicipio;
	protected String nome;
	protected Uf uf;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_municipio")
	public Integer getCdmunicipio() {
		return cdmunicipio;
	}
	
	@MaxLength(50)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cduf")
	public Uf getUf() {
		return uf;
	}
	
	public void setCdmunicipio(Integer cdmunicipio) {
		this.cdmunicipio = cdmunicipio;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setUf(Uf uf) {
		this.uf = uf;
	}
	
	
}
