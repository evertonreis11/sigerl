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
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_area", sequenceName = "sq_area")
@DisplayName("Área de armazenagem")
public class Area {
	
	// variáveis de instância	
	protected Integer cdarea;
	protected String nome;
	protected Deposito deposito;
	protected String observacao;
	protected Long codigo;
	protected Boolean virtual;
	protected Boolean avaria;
	protected Boolean box;
	protected Set<Endereco> listaEndereco = new ListSet<Endereco>(Endereco.class);
	protected Set<Enderecosentido> listaEnderecosentido = new ListSet<Enderecosentido>(Enderecosentido.class);
	protected Set<Dadologistico> listaDadologistico = new ListSet<Dadologistico>(Dadologistico.class);
	
	// Transient
	@Deprecated
	protected Long codigoAE;
	
	//Construtores
	public Area() {
		
	}
	
	public Area(Integer cd) {
		this.cdarea = cd;
	}

	public Area(Long codigo) {
		this.codigo = codigo;
	}
	
	public Area(Integer cdarea, Long codigoarea) {
		this.cdarea = cdarea;
		this.codigo = codigoarea;
	}
	
 	// Metodos get e set
	@Id	
	@DisplayName("Código da área")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_area")
	public Integer getCdarea() {
		return cdarea;
	}
	
	@DisplayName("Nome")
	@MaxLength(50)
	@Required
	public String getNome() {
		return nome;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName("Depósito")
	@JoinColumn(name="cddeposito")
	@Required
	public Deposito getDeposito() {
		return deposito;
	}
	
	@DisplayName("Observação")
	@MaxLength(1000)
	public String getObservacao() {
		return observacao;
	}
	
	@DescriptionProperty
	@DisplayName("Código da área")
	@MaxLength(2)
	@Required
	public Long getCodigo() {
		return codigo;
	}
	
	@OneToMany(mappedBy="area")
	public Set<Endereco> getListaEndereco() {
		return listaEndereco;
	}
	
	@DisplayName("Virtual")
	public Boolean getVirtual() {
		return virtual;
	}
	
	@DisplayName("Avaria")
	public Boolean getAvaria() {
		return avaria;
	}
	
	public Boolean getBox() {
		return box;
	}

	@OneToMany(mappedBy="area")
	public Set<Enderecosentido> getListaEnderecosentido() {
		return listaEnderecosentido;
	}
	
	@OneToMany(mappedBy="area")
	public Set<Dadologistico> getListaDadologistico() {
		return listaDadologistico;
	}
	
	public void setCdarea(Integer cdarea) {
		this.cdarea = cdarea;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public void setListaEndereco(Set<Endereco> listaEndereco) {
		this.listaEndereco = listaEndereco;
	}
	
	public void setVirtual(Boolean virtual) {
		this.virtual = virtual;
	}
	
	public void setListaEnderecosentido(Set<Enderecosentido> listaEnderecosentido) {
		this.listaEnderecosentido = listaEnderecosentido;
	}
	
	public void setAvaria(Boolean avaria) {
		this.avaria = avaria;
	}
	
	public void setBox(Boolean box) {
		this.box = box;
	}
	
	public void setListaDadologistico(Set<Dadologistico> listaDadologistico) {
		this.listaDadologistico = listaDadologistico;
	}
	
	// Métodos get e set Transient
	@Transient
	@DisplayName("Área")
	@MaxLength(2)
	@Deprecated
	public Long getCodigoAE() {
		return codigoAE;
	}
	
	@Deprecated
	public void setCodigoAE(Long codigoAE) {
		this.codigoAE = codigoAE;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Area) {
			Area area = (Area) obj;
			return area.getCdarea().equals(this.cdarea);
		}
		return super.equals(obj);
	}
	
}
