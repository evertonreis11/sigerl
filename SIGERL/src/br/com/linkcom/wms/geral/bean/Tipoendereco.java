package br.com.linkcom.wms.geral.bean;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_tipoendereco", sequenceName = "sq_tipoendereco")
public class Tipoendereco {

	protected Integer cdtipoendereco;
	protected String nome;
	protected Deposito deposito;
	protected Integer ordem;
	protected Set<Endereco> listaEndereco = new ListSet<Endereco>(Endereco.class);
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_tipoendereco")
	public Integer getCdtipoendereco() {
		return cdtipoendereco;
	}
	
	@DescriptionProperty
	@DisplayName("Descrição")
	@MaxLength(50)
	@Required
	public String getNome() {
		return nome;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@DisplayName("Seqüência de endereçamento")
	public Integer getOrdem() {
		return ordem;
	}
	
	@OneToMany(mappedBy="tipoendereco")
	public Set<Endereco> getListaEndereco() {
		return listaEndereco;
	}
	
	public void setCdtipoendereco(Integer cdtipoendereco) {
		this.cdtipoendereco = cdtipoendereco;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	
	public void setListaEndereco(Set<Endereco> listaEndereco) {
		this.listaEndereco = listaEndereco;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Tipoendereco) {
			Tipoendereco tipoendereco = (Tipoendereco) obj;
			
			return tipoendereco.getCdtipoendereco().equals(this.getCdtipoendereco());
		}
		
		return super.equals(obj);
	}	
}
