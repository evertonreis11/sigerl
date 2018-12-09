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
@SequenceGenerator(name="sq_inventariostatus",sequenceName="sq_inventariostatus")
public class Inventariostatus {

	public static final Inventariostatus NAO_INICIADO = new Inventariostatus(1,"Não iniciado"); 
	public static final Inventariostatus EM_EXECUCAO = new Inventariostatus(2); 
	public static final Inventariostatus FINALIZADO_SUCESSO = new Inventariostatus(3);
	public static final Inventariostatus FINALIZADO_DIVERGENTE = new Inventariostatus(4); 
	public static final Inventariostatus CANCELADO = new Inventariostatus(5);
	public static final Inventariostatus AJUSTANDO_ESTOQUE = new Inventariostatus(6);
	
	protected Integer cdinventariostatus;
	protected String nome;
	
	public Inventariostatus() {
	}
	
	public Inventariostatus(Integer cd) {
		this.cdinventariostatus = cd;
	}
	
	public Inventariostatus(Integer cd, String nome) {
		this.cdinventariostatus = cd;
		this.nome = nome;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(generator="sq_inventariostatus",strategy = GenerationType.AUTO)
	public Integer getCdinventariostatus() {
		return cdinventariostatus;
	}
	
	@MaxLength(20)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
		
	public void setCdinventariostatus(Integer cdinventariostatus) {
		this.cdinventariostatus = cdinventariostatus;
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
				+ ((cdinventariostatus == null) ? 0 : cdinventariostatus
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
		final Inventariostatus other = (Inventariostatus) obj;
		if (cdinventariostatus == null) {
			if (other.cdinventariostatus != null)
				return false;
		} else if (!cdinventariostatus.equals(other.cdinventariostatus))
			return false;
		return true;
	}
	
	
}
