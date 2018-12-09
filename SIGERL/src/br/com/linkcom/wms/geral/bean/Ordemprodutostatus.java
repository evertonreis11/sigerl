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

@Entity
@SequenceGenerator(name="sq_ordemstatus",sequenceName="sq_ordemstatus")
public class Ordemprodutostatus {
	
	protected Integer cdordemprodutostatus;
	protected String nome;
	protected Set<Ordemservicoproduto> listaOrdemservicoproduto = new ListSet<Ordemservicoproduto>(Ordemservicoproduto.class);
	
	// Constantes
	public static final Ordemprodutostatus NAO_CONCLUIDO = new Ordemprodutostatus(1);
	public static final Ordemprodutostatus CONCLUIDO_OK = new Ordemprodutostatus(2);
	public static final Ordemprodutostatus CONCLUIDO_DIVERGENTE = new Ordemprodutostatus(3);
	public static final Ordemprodutostatus EM_EXECUCAO = new Ordemprodutostatus(4);
	
	// Construtores
	public Ordemprodutostatus() {

	}
	
	public Ordemprodutostatus(Integer cdordemprodutostatus) {
		this.cdordemprodutostatus = cdordemprodutostatus;

	}
	
	// Metodos get e set
	@Id
	@DisplayName("id")
	@GeneratedValue(generator="sq_ordemstatus",strategy=GenerationType.AUTO)
	public Integer getCdordemprodutostatus() {
		return cdordemprodutostatus;
	}
	
	@DescriptionProperty
	@DisplayName("Status do produto")
	public String getNome() {
		return nome;
	}
	
	@OneToMany(mappedBy="ordemprodutostatus")
	public Set<Ordemservicoproduto> getListaOrdemservicoproduto() {
		return listaOrdemservicoproduto;
	}

	public void setCdordemprodutostatus(Integer cdordemprodutostatus) {
		this.cdordemprodutostatus = cdordemprodutostatus;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setListaOrdemservicoproduto(Set<Ordemservicoproduto> listaOrdemservicoproduto) {
		this.listaOrdemservicoproduto = listaOrdemservicoproduto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Ordemprodutostatus) {
			Ordemprodutostatus ordemprodutostatus = (Ordemprodutostatus) obj;
			
			return ordemprodutostatus.getCdordemprodutostatus().equals(this.getCdordemprodutostatus());
		}
		
		return super.equals(obj);
	}
}
