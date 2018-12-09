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
@SequenceGenerator(name = "sq_enderecofuncao", sequenceName = "sq_enderecofuncao")
public class Enderecofuncao {
	
	// constantes
	public static final Enderecofuncao PICKING = new Enderecofuncao(1);
	public static final Enderecofuncao PULMAO = new Enderecofuncao(2);
	public static final Enderecofuncao BLOCADO = new Enderecofuncao(3);
	
	protected Integer cdenderecofuncao;
	protected String nome;
	
	/* ------------------------------ construtores -----------------------------*/
	public Enderecofuncao() {
		
	}
	
	public Enderecofuncao(Integer cdenderecofuncao) {
		this.cdenderecofuncao = cdenderecofuncao;
	}
	
	/* ------------------------------ metodos get´s e set´s -----------------------------*/
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_enderecofuncao")	
	public Integer getCdenderecofuncao() {
		return cdenderecofuncao;
	}
	
	@DescriptionProperty
	@DisplayName("Característica")
	@MaxLength(20)
	public String getNome() {
		return nome;
	}
	
	public void setCdenderecofuncao(Integer cdenderecofuncao) {
		this.cdenderecofuncao = cdenderecofuncao;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Enderecofuncao) {
			Enderecofuncao enderecofuncao = (Enderecofuncao) obj;
			return enderecofuncao.getCdenderecofuncao().equals(this.cdenderecofuncao);
		}
		return super.equals(obj);
	}
}
