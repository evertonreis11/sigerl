package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name = "sq_uf", sequenceName = "sq_uf")
public class Uf {
	
	protected Integer cduf;
	protected String nome;
	protected String sigla;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_uf")
	public Integer getCduf() {
		return cduf;
	}
	
	@MaxLength(50)
	public String getNome() {
		return nome;
	}
	@MaxLength(2)
	@DescriptionProperty
	public String getSigla() {
		return sigla;
	}
	
	public void setCduf(Integer cduf) {
		this.cduf = cduf;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	
}
