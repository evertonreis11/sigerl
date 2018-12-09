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
@SequenceGenerator(name = "sq_tipocarregamento", sequenceName = "sq_tipocarregamento")
public class Tipocarregamento {
	
	private Integer cdtipocarregamento;	
	private String nome;
	private Boolean prioridade = Boolean.FALSE;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_tipocarregamento")	
	public Integer getCdtipocarregamento() {
		return cdtipocarregamento;
	}
	@Required
	@MaxLength(100)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	@Required
	public Boolean getPrioridade() {
		return prioridade;
	}

	public void setCdtipocarregamento(Integer cdtipocarregamento) {
		this.cdtipocarregamento = cdtipocarregamento;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setPrioridade(Boolean prioridade) {
		this.prioridade = prioridade;
	}
}
