package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Telefone;

@Entity
@SequenceGenerator(name = "sq_equipamento", sequenceName = "sq_equipamento")
public class Equipamento {
	
	private Integer cdequipamento;
	private String nome;
	private String mac;
	private Boolean rastreado;
	private Empresa empresa;
	private Deposito deposito;
	private Telefone fonenumero;
	private Motorista motorista;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_equipamento")
	public Integer getCdequipamento() {
		return cdequipamento;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	public String getMac() {
		return mac;
	}
	public Boolean getRastreado() {
		return rastreado;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@DisplayName("Empresa")
	@JoinColumn(name="cdempresa")	
	public Empresa getEmpresa() {
		return empresa;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@DisplayName("Depósito")
	@JoinColumn(name="cddeposito")	
	public Deposito getDeposito() {
		return deposito;
	}
	public Telefone getFonenumero() {
		return fonenumero;
	}
	@OneToOne(mappedBy="equipamento")
	public Motorista getMotorista() {
		return motorista;
	}
	
	
	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}
	public void setFonenumero(Telefone fonenumero) {
		this.fonenumero = fonenumero;
	}
	public void setCdequipamento(Integer cdequipamento) {
		this.cdequipamento = cdequipamento;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public void setRastreado(Boolean rastreado) {
		this.rastreado = rastreado;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
}