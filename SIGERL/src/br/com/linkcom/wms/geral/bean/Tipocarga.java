package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@SequenceGenerator(name = "sq_tipocarga", sequenceName = "sq_tipocarga")
public class Tipocarga {

	protected Integer cdtipocarga;
	protected String nome;
	
	public Tipocarga(Integer cdtipocarga, String nome) {
		super();
		this.cdtipocarga = cdtipocarga;
		this.nome = nome;
	}
	
	public Tipocarga() {
	}

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_tipocarga")
	public Integer getCdtipocarga() {
		return cdtipocarga;
	}
	
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	public void setCdtipocarga(Integer cdtipocarga) {
		this.cdtipocarga = cdtipocarga;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
