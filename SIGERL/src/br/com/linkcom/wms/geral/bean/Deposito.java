package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
import java.util.List;
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
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.types.Cnpj;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@DisplayName("Depósito")
@DefaultOrderBy("nome")
@SequenceGenerator(name = "sq_deposito", sequenceName = "sq_deposito")
public class Deposito {
	
	protected Integer cddeposito;
	protected String nome;
	protected String endereco;
	protected Integer tamanho;
	protected Cnpj cnpj;
	protected Boolean ativo;
	protected List<Usuariodeposito> listaUsuarioDeposito = new ArrayList<Usuariodeposito>();
	protected Set<Recebimento> listaRecebimento = new ListSet<Recebimento>(Recebimento.class);
	protected Set<Tipoendereco> listaTipoendereco = new ListSet<Tipoendereco>(Tipoendereco.class);
	protected Set<Area> listaArea = new ListSet<Area>(Area.class);
	protected Boolean tmsativo;
	protected Empresa empresa;
	protected Long codigoerp;
	protected Tipodeposito tipodeposito;
	//protected List<Box> listaBox= new ListSet<Box>(Box.class);
	
	// construtores
	public Deposito() {

	}
	
	public Deposito(Integer cd) {
		this.cddeposito = cd;	
	}
	
	public Deposito(int cd, String nome) {
		this.cddeposito = cd;
		this.nome = nome;
		
	}

	// Metodos get e set
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_deposito")
	public Integer getCddeposito() {
		return cddeposito;
	}
	
	@DescriptionProperty
	@MaxLength(50)
	@Required
	public String getNome() {
		return nome;
	}
	
	@MaxLength(100)
	@DisplayName("Endereço")
	public String getEndereco() {
		return endereco;
	}
	
	public Integer getTamanho() {
		return tamanho;
	}	
	
	public Cnpj getCnpj() {
		return cnpj;
	}
	
	@OneToMany(mappedBy="deposito")
	public List<Usuariodeposito> getListaUsuarioDeposito() {
		return listaUsuarioDeposito;
	}
	
	public Boolean getAtivo() {
		return ativo;
	}
	
	public Boolean getTmsativo() {
		return tmsativo;
	}
	
	public Long getCodigoerp() {
		return codigoerp;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdempresa")
	public Empresa getEmpresa() {
		return empresa;
	}


	public void setCodigoerp(Long codigoerp) {
		this.codigoerp = codigoerp;
	}

	public void setTmsativo(Boolean tmsativo) {
		this.tmsativo = tmsativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@OneToMany(mappedBy="deposito")
	public Set<Recebimento> getListaRecebimento() {
		return listaRecebimento;
	}

	@OneToMany(mappedBy="deposito")
	public Set<Tipoendereco> getListaTipoendereco() {
		return listaTipoendereco;
	}

	@OneToMany(mappedBy="deposito")	
	public Set<Area> getListaArea() {
		return listaArea;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipodeposito")
	public Tipodeposito getTipodeposito() {
		return tipodeposito;
	}

	public void setCddeposito(Integer cddeposito) {
		this.cddeposito = cddeposito;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}
	
	public void setCnpj(Cnpj cnpj) {
		this.cnpj = cnpj;
	}
	
	public void setListaUsuarioDeposito(List<Usuariodeposito> listaUsuarioDeposito) {
		this.listaUsuarioDeposito = listaUsuarioDeposito;
	}

	public void setListaRecebimento(Set<Recebimento> listaRecebimento) {
		this.listaRecebimento = listaRecebimento;
	}

	public void setListaTipoendereco(Set<Tipoendereco> listaTipoendereco) {
		this.listaTipoendereco = listaTipoendereco;
	}
	
	public void setListaArea(Set<Area> listaArea) {
		this.listaArea = listaArea;
	}
	
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public void setTipodeposito(Tipodeposito tipodeposito) {
		this.tipodeposito = tipodeposito;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof Deposito) {
			Deposito dep = (Deposito) obj;
			return dep.getCddeposito().equals(getCddeposito());
		}
		
		return super.equals(obj);
	}
	
	@Transient
	public String getNomeForBase(){
		if(nome != null && nome.length() > 17)
			return nome.substring(0,17)+("...");
		else return nome;
	}
	
	
/*	@OneToMany(mappedBy="deposito")
	public List<Box> getListaBox() {
		return listaBox;
	}

	
	public void setListaBox(List<Box> listaBox) {
		this.listaBox = listaBox;
	}*/
}
