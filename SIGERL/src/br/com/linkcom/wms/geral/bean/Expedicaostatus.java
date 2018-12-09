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
@SequenceGenerator(name = "sq_expedicaostatus", sequenceName = "sq_expedicaostatus")
public class Expedicaostatus {
	
	public static final Expedicaostatus EM_ANDAMENTO = new Expedicaostatus(1);
	public static final Expedicaostatus FINALIZADO = new Expedicaostatus(2); 
	public static final Expedicaostatus CANCELADO = new Expedicaostatus(3); 
	
	protected Integer cdexpedicaostatus;
	protected String nome;
	
	public Expedicaostatus(){
	}

	public Expedicaostatus(Integer cdexpedicaostatus){
		this.cdexpedicaostatus = cdexpedicaostatus;
	}
	
	@Id	
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_expedicaostatus")
	public Integer getCdexpedicaostatus() {
		return cdexpedicaostatus;
	}
	@MaxLength(20)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	public void setCdexpedicaostatus(Integer cdexpedicaostatus) {
		this.cdexpedicaostatus = cdexpedicaostatus;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cdexpedicaostatus == null) ? 0 : cdexpedicaostatus
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Expedicaostatus other = (Expedicaostatus) obj;
		if (cdexpedicaostatus == null) {
			if (other.cdexpedicaostatus != null)
				return false;
		} else if (!cdexpedicaostatus.equals(other.cdexpedicaostatus))
			return false;
		return true;
	}
	
	
}
