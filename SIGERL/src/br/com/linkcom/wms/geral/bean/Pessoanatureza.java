package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name = "sq_pessoanatureza", sequenceName = "sq_pessoanatureza")
public class Pessoanatureza {
	
	public static final Pessoanatureza FISICA = new Pessoanatureza(1);
	public static final Pessoanatureza JURIDICA = new Pessoanatureza(2);
	
	protected Integer cdpessoanatureza;
	protected String nome;
	
	
	public Pessoanatureza() {
	}
	
	public Pessoanatureza(Integer cdpessoanatureza) {
		this.cdpessoanatureza = cdpessoanatureza;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pessoanatureza")
	public Integer getCdpessoanatureza() {
		return cdpessoanatureza;
	}
	@MaxLength(10)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	public void setCdpessoanatureza(Integer cdpessoanatureza) {
		this.cdpessoanatureza = cdpessoanatureza;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pessoanatureza){
			Pessoanatureza pessoanatureza = (Pessoanatureza)obj;
			return this.cdpessoanatureza.equals(pessoanatureza.getCdpessoanatureza());
		}
		return super.equals(obj);
	}
	
	
}
