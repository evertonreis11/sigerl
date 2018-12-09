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
@SequenceGenerator(name = "sq_enderecolado", sequenceName = "sq_enderecolado")
public class Enderecolado {

	public final static Enderecolado PAR = new Enderecolado(1);
	public final static Enderecolado IMPAR = new Enderecolado(2);
	
	protected Integer cdenderecolado;
	protected String nome;
	
	public Enderecolado(){
		
	}
	
	public Enderecolado(Integer cdenderecolado){
		this.cdenderecolado = cdenderecolado;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(generator = "sq_enderecolado", strategy = GenerationType.AUTO)
	public Integer getCdenderecolado() {
		return cdenderecolado;
	}
	
	@MaxLength(10)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	public void setCdenderecolado(Integer cdenderecolado) {
		this.cdenderecolado = cdenderecolado;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdenderecolado == null) ? 0 : cdenderecolado.hashCode());
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
		final Enderecolado other = (Enderecolado) obj;
		if (cdenderecolado == null) {
			if (other.cdenderecolado != null)
				return false;
		} else if (!cdenderecolado.equals(other.cdenderecolado))
			return false;
		return true;
	}
}
