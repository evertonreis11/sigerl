package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;


@Entity
@SequenceGenerator(name = "sq_arquivostatus", sequenceName = "sq_arquivostatus")
public class Arquivostatus {

	protected Integer cdarquivostatus;
	protected String nome;
	
	public static final Integer ABERTO = 1;
	public static final Integer FECHADO = 2;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_arquivostatus")
	public Integer getCdarquivostatus() {
		return cdarquivostatus;
	}
	public void setCdarquivostatus(Integer id) {
		this.cdarquivostatus = id;
	}

	
	@Required
	@MaxLength(10)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}

	
	public void setNome(String nome) {
		this.nome = nome;
	}

}
