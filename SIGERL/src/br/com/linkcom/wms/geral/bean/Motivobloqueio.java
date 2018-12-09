package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name = "sq_motivobloqueio",sequenceName="sq_motivobloqueio")
public class Motivobloqueio {
	
	public static final Motivobloqueio INVENTARIO = new Motivobloqueio(1);
	public static final Motivobloqueio ROTINA = new Motivobloqueio(2);
	
	protected Integer cdmotivobloqueio;
	protected String nome;
	
	
	public Motivobloqueio(Integer cd) {
		this.cdmotivobloqueio = cd;
	}
	
	public Motivobloqueio() {
	}
	
	@Id
	@GeneratedValue(generator = "sq_motivobloqueio",strategy = GenerationType.AUTO)
	public Integer getCdmotivobloqueio() {
		return cdmotivobloqueio;
	}
	
	@MaxLength(20)
	public String getNome() {
		return nome;
	}
	
	public void setCdmotivobloqueio(Integer cdmotivobloqueio) {
		this.cdmotivobloqueio = cdmotivobloqueio;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Motivobloqueio) {
			Motivobloqueio motivobloqueio = (Motivobloqueio) obj;
			return motivobloqueio.getCdmotivobloqueio().equals(getCdmotivobloqueio());
		}
		return super.equals(obj);
	}
}
