package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Cep;

@Entity
@SequenceGenerator(name = "sq_filial", sequenceName = "sq_filial")
public class Filial {
	
	private Integer cdfilial;
	private String nome;
	private Long codigoerp;
	private Long numero;
	private Empresa empresa;
	private Cep cep;
	
	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_filial")
	public Integer getCdfilial() {
		return cdfilial;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	public Long getCodigoerp() {
		return codigoerp;
	}
	@DisplayName("Número")
	public Long getNumero() {
		return numero;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdempresa")	
	public Empresa getEmpresa() {
		return empresa;
	}
	public Cep getCep() {
		return cep;
	}
	
	//Set's
	public void setCdfilial(Integer cdfilial) {
		this.cdfilial = cdfilial;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setCodigoerp(Long codigoerp) {
		this.codigoerp = codigoerp;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public void setCep(Cep cep) {
		this.cep = cep;
	}
	
}
