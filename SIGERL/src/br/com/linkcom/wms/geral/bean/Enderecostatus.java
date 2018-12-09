package br.com.linkcom.wms.geral.bean;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name = "sq_enderecostatus", sequenceName = "sq_enderecostatus")
public class Enderecostatus {
	
	//Constantes
	public static final Enderecostatus DISPONIVEL = new Enderecostatus(1);
	public static final Enderecostatus BLOQUEADO = new Enderecostatus(2);
	public static final Enderecostatus OCUPADO = new Enderecostatus(3);
	public static final Enderecostatus EMPRESTIMO = new Enderecostatus(4);
	public static final Enderecostatus RESERVADO = new Enderecostatus(5);
	
	protected Integer cdenderecostatus;
	protected String nome;
	protected Boolean bloqueado;
	protected Set<Endereco> listaendereco = new ListSet<Endereco>(Endereco.class);
	
	/* ------------------------------ construtores -----------------------------*/
	public Enderecostatus() {
		
	}
	
	public Enderecostatus(Integer cdenderecostatus) {
		this.cdenderecostatus = cdenderecostatus;
	}
	
	/* ------------------------------ metodos get´s e set´s -----------------------------*/
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_enderecostatus")
	public Integer getCdenderecostatus() {
		return cdenderecostatus;
	}
	
	@DescriptionProperty
	@DisplayName("nome")
	@MaxLength(20)
	public String getNome() {
		return nome;
	}
	
	@DisplayName("Bloqueado")
	public Boolean getBloqueado() {
		return bloqueado;
	}
	
	@OneToMany(mappedBy="enderecostatus")
	public Set<Endereco> getListaendereco() {
		return listaendereco;
	}
	
	public void setCdenderecostatus(Integer cdenderecostatus) {
		this.cdenderecostatus = cdenderecostatus;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	
	public void setListaendereco(Set<Endereco> listaendereco) {
		this.listaendereco = listaendereco;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Enderecostatus) {
			return ((Enderecostatus) obj).getCdenderecostatus().equals(this.getCdenderecostatus());
		} 
		return super.equals(obj);
	}
	
}
