package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.types.Cpf;

@Entity
@SequenceGenerator(name = "sq_motorista", sequenceName = "sq_motorista")
@DefaultOrderBy("motorista.nome")
public class Motorista {

	private Integer cdmotorista;
	private String nome;
	private Cpf cpf;
	private String apelido;
	private Transportador transportador;
	private Veiculo veiculo;
	private Equipamento equipamento;
	private Deposito deposito;
	
	public Motorista(){
	}
	
	public Motorista(Integer cdmotorista){
		this.cdmotorista = cdmotorista;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_motorista")
	public Integer getCdmotorista() {
		return cdmotorista;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	public Cpf getCpf() {
		return cpf;
	}
	public String getApelido() {
		return apelido;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@DisplayName("Transportador")
	@JoinColumn(name="cdtransportador")
	public Transportador getTransportador() {
		return transportador;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@DisplayName("Veículo")
	@JoinColumn(name="cdveiculo")
	public Veiculo getVeiculo() {
		return veiculo;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@DisplayName("Equipamento")
	@JoinColumn(name="cdequipamento")
	public Equipamento getEquipamento() {
		return equipamento;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@DisplayName("Depósito")
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setCdmotorista(Integer cdmotorista) {
		this.cdmotorista = cdmotorista;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setCpf(Cpf cpf) {
		this.cpf = cpf;
	}
	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}
	
}