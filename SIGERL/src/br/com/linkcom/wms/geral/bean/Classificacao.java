package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name = "sq_classificacao", sequenceName = "sq_classificacao")
public class Classificacao {

	protected Integer cdclassificacao;
	protected String nome;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator="sq_classificacao")
	public Integer getCdclassificacao() {
		return cdclassificacao;
	}
	@MaxLength(20)
	public String getNome() {
		return nome;
	}
	public void setCdclassificacao(Integer cdclassificacao) {
		this.cdclassificacao = cdclassificacao;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
