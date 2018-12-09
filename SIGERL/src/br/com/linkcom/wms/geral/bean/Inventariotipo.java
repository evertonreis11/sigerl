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
@SequenceGenerator(name="sq_inventariotipo",sequenceName="sq_inventariotipo")
public class Inventariotipo {
	
	public static final Inventariotipo TOTAL = new Inventariotipo(1);
	public static final Inventariotipo PARCIAl = new Inventariotipo(2);
	
	protected Integer cdinventariotipo;
	protected String nome;
	
	public Inventariotipo() {
	}
	
	public Inventariotipo(Integer cd) {
		this.cdinventariotipo = cd;
	}
	
	@GeneratedValue(generator = "sq_inventariotipo", strategy = GenerationType.AUTO)
	@Id
	@DisplayName("Id")
	public Integer getCdinventariotipo() {
		return cdinventariotipo;
	}
	
	@MaxLength(10)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	public void setCdinventariotipo(Integer cdinventariotipo) {
		this.cdinventariotipo = cdinventariotipo;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Inventariotipo) {
			Inventariotipo inventariotipo = (Inventariotipo) obj;
			return inventariotipo.getCdinventariotipo().equals(this.cdinventariotipo);
		}
		return super.equals(obj);
	}
}
