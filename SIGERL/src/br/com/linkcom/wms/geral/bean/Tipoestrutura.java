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
@SequenceGenerator(name = "sq_tipoestrutura", sequenceName = "sq_tipoestrutura")
public class Tipoestrutura {
	
	public static final Tipoestrutura PORTA_PALETE = new Tipoestrutura(1);
	public static final Tipoestrutura ESTANTE = new Tipoestrutura(2);
	public static final Tipoestrutura BLOCADO = new Tipoestrutura(3);
	
	protected Integer cdtipoestrutura;
	protected String nome;
	protected Boolean utilizapicking;
	protected Set<Endereco> listaEndereco = new ListSet<Endereco>(Endereco.class);
	
	public Tipoestrutura(int cdtipoestrutura) {
		this.cdtipoestrutura = cdtipoestrutura;
	}
	
	public Tipoestrutura(){
		
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_tipoestrutura")
	public Integer getCdtipoestrutura() {
		return cdtipoestrutura;
	}
	
	@MaxLength(20)
	@DisplayName("Nome")
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	@DisplayName("Estrutura padrão")
	public Boolean getUtilizapicking() {
		return utilizapicking;
	}
	
	@OneToMany(mappedBy="tipoestrutura")
	public Set<Endereco> getListaEndereco() {
		return listaEndereco;
	}
	
	public void setCdtipoestrutura(Integer cdtipoestrutura) {
		this.cdtipoestrutura = cdtipoestrutura;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setUtilizapicking(Boolean utilizapicking) {
		this.utilizapicking = utilizapicking;
	}
	
	public void setListaEndereco(Set<Endereco> listaEndereco) {
		this.listaEndereco = listaEndereco;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tipoestrutura) {
			Tipoestrutura tipoestrutura = (Tipoestrutura) obj;
			return tipoestrutura.getCdtipoestrutura().equals(this.getCdtipoestrutura());
		}
		return super.equals(obj);
	}
}
