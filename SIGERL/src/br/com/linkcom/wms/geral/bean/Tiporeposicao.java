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
@SequenceGenerator(name = "sq_tiporeposicao", sequenceName = "sq_tiporeposicao")
public class Tiporeposicao {
	
	protected Integer cdtiporeposicao;
	protected String nome;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_tiporeposicao")
	public Integer getCdtiporeposicao() {
		return cdtiporeposicao;
	}
	
	@DescriptionProperty
	@MaxLength(30)
	public String getNome() {
		return nome;
	}
	
	public void setCdtiporeposicao(Integer cdtiporeposicao) {
		this.cdtiporeposicao = cdtiporeposicao;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
}
