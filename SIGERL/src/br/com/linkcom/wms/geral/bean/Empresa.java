package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name = "sq_empresa", sequenceName = "sq_empresa")
public class Empresa {
	
	private Integer cdempresa;
	private String nome; 
	private Long codigoerp;
	private Long codigowms;
	private Boolean ativo;
	private List<Deposito> listaDeposito = new ArrayList<Deposito>();
	private String depositosVinculados;
	private String urllocation;
	
	public Empresa(){}
	
	public Empresa(Integer cdempresa, String nome){
		this.cdempresa = cdempresa;
		this.nome = nome;
	}
	
	public Empresa(Integer cdempresa){
		this.cdempresa = cdempresa;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_empresa")
	public Integer getCdempresa() {
		return cdempresa;
	}
	@DescriptionProperty
	@MaxLength(200)
	public String getNome() {
		return nome;
	}
	public Long getCodigoerp() {
		return codigoerp;
	}
	public Long getCodigowms() {
		return codigowms;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	
	@Transient
	@OneToMany(mappedBy="empresa")
	public List<Deposito> getListaDeposito() {
		return listaDeposito;
	}
	@Transient
	@DisplayName("Depósitos Vinculados")
	public String getDepositosVinculados() {
		return depositosVinculados;
	}
	public String getUrllocation() {
		return urllocation;
	}

	
	public void setUrllocation(String urllocation) {
		this.urllocation = urllocation;
	}
	public void setCdempresa(Integer cdempresa) {
		this.cdempresa = cdempresa;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setCodigoerp(Long codigoerp) {
		this.codigoerp = codigoerp;
	}
	public void setCodigowms(Long codigowms) {
		this.codigowms = codigowms;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public void setListaDeposito(List<Deposito> listaDeposito) {
		this.listaDeposito = listaDeposito;
	}
	public void setDepositosVinculados(String depositosVinculados) {
		this.depositosVinculados = depositosVinculados;
	}

}
