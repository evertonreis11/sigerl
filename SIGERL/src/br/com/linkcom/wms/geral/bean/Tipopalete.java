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
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_tipopalete", sequenceName = "sq_tipopalete")
@DisplayName("Tipo de palete")
public class Tipopalete {

	protected Integer cdtipopalete;
	protected String nome;
	protected Set<Endereco> listaEndereco = new ListSet<Endereco>(Endereco.class);
	protected Set<Produtotipopalete> listaProdutotipopalete = new ListSet<Produtotipopalete>(Produtotipopalete.class);
	
	// ------------------------------- CONSTRUCTOR --------------------------------
	public Tipopalete() {
		
	}
	
	public Tipopalete(Integer cd) {
		this.cdtipopalete = cd;
		
	}
		
	// ------------------------------- METODOS GET E SET --------------------------------
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_tipopalete")
	public Integer getCdtipopalete() {
		return cdtipopalete;
	
	}
	
	@DescriptionProperty
	@MaxLength(50)
	@Required
	public String getNome() {
		return nome;
	
	}
	
	@OneToMany(mappedBy="tipopalete")
	public Set<Endereco> getListaEndereco() {
		return listaEndereco;
	
	}
	
	@OneToMany(mappedBy="tipopalete")
	public Set<Produtotipopalete> getListaProdutotipopalete() {
		return listaProdutotipopalete;
		
	}
	
	public void setCdtipopalete(Integer cdtipopalete) {
		this.cdtipopalete = cdtipopalete;
	
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	
	}
	
	public void setListaEndereco(Set<Endereco> listaEndereco) {
		this.listaEndereco = listaEndereco;
	
	}	
	
	public void setListaProdutotipopalete(Set<Produtotipopalete> listaProdutotipopalete) {
		this.listaProdutotipopalete = listaProdutotipopalete;
		
	}
}
