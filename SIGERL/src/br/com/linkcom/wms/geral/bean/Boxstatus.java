package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
public class Boxstatus {
	
	public static final Boxstatus DISPONIVEL = new Boxstatus(1);
	public static final Boxstatus BLOQUEADO = new Boxstatus(2); 
	public static final Boxstatus OCUPADO = new Boxstatus(3); 
	
	protected Integer cdboxstatus;
	protected String nome;
	protected Boolean bloqueado;
	
	public Boxstatus() {
	}
	
	public Boxstatus(Integer cdboxstatus) {
		this.cdboxstatus = cdboxstatus;
	}

	@Id
	@DisplayName("Id")
	public Integer getCdboxstatus() {
		return cdboxstatus;
	}
	
	@DescriptionProperty
	@MaxLength(20)
	public String getNome() {
		return nome;
	}
	public Boolean getBloqueado() {
		return bloqueado;
	}
	public void setCdboxstatus(Integer cdboxstatus) {
		this.cdboxstatus = cdboxstatus;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	
	

}
