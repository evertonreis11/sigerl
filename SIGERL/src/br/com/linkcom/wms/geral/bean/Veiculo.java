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
import br.com.linkcom.neo.validation.annotation.MaxValue;
import br.com.linkcom.neo.validation.annotation.MinValue;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_veiculo", sequenceName = "sq_veiculo")
@DisplayName("Veículo")
public class Veiculo {

	protected Integer cdveiculo;
	protected String placa;
	protected Tipoveiculo tipoveiculo;
	protected Boolean disponivel = Boolean.TRUE;
	protected String modelo;
	protected Tipopalete tipopalete;
	protected Double capacidadepeso;
	protected Double altura;
	protected Double largura;
	protected Double profundidade;
	protected Integer capacidadepalete;
	protected Transportador transportador;
	protected Deposito deposito;
	protected Set<Carregamento> listaCarregamento = new ListSet<Carregamento>(Carregamento.class);
	private String placaPrimeiraCarreta;
	private String placaSegundaCarreta;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_veiculo")
	public Integer getCdveiculo() {
		return cdveiculo;
	}
	public void setCdveiculo(Integer id) {
		this.cdveiculo = id;
	}

	
	@Required
	@MaxLength(7)
	@DescriptionProperty
	public String getPlaca() {
		return placa;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipoveiculo")
	@DisplayName("Tipo de veículo")
	@Required
	public Tipoveiculo getTipoveiculo() {
		return tipoveiculo;
	}
	
	@Required
	@DisplayName("Disponível")
	public Boolean getDisponivel() {
		return disponivel;
	}
	
	@MaxLength(20)
	public String getModelo() {
		return modelo;
	}
	
	@Required	
	public Double getAltura() {
		return altura;
	}
	
	@DisplayName("Capacidade de palete")
	@MaxLength(5)
	public Integer getCapacidadepalete() {
		return capacidadepalete;
	}
	
	@Required
	public Double getLargura() {
		return largura;
	}
	
	@Required
	public Double getProfundidade() {
		return profundidade;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipopalete")
	@DisplayName("Tipo de palete")
	@Required
	public Tipopalete getTipopalete() {
		return tipopalete;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpessoa")
	@DisplayName("Transportador")
	public Transportador getTransportador() {
		return transportador;
	}
	
	@MinValue(0)
	@MaxValue(9999999.999)
	@DisplayName("Capacidade de peso")
	public Double getCapacidadepeso() {
		return capacidadepeso;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName("Depósito")
	@JoinColumn(name="cddeposito")
	@Required
	public Deposito getDeposito() {
		return deposito;
	}

	@OneToMany(mappedBy="veiculo")
	public Set<Carregamento> getListaCarregamento() {
		return listaCarregamento;
	}
	
	@DisplayName("Placa da Primeira Carreta")
	public String getPlacaPrimeiraCarreta() {
		return placaPrimeiraCarreta;
	}
	
	public void setPlacaPrimeiraCarreta(String placaPrimeiraCarreta) {
		this.placaPrimeiraCarreta = placaPrimeiraCarreta;
	}
	
	public void setPlaca(String placa) {
		this.placa = placa == null ? placa : placa.toUpperCase();
	}
	
	public void setTipoveiculo(Tipoveiculo tipoveiculo) {
		this.tipoveiculo = tipoveiculo;
	}
	
	public void setDisponivel(Boolean disponivel) {
		this.disponivel = disponivel;
	}
	
	public void setAltura(Double altura) {
		this.altura = altura;
	}
	
	public void setCapacidadepalete(Integer capacidadepalete) {
		this.capacidadepalete = capacidadepalete;
	}
	
	public void setLargura(Double largura) {
		this.largura = largura;
	}
	
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	public void setProfundidade(Double profundidade) {
		this.profundidade = profundidade;
	}
	
	public void setTipopalete(Tipopalete tipopalete) {
		this.tipopalete = tipopalete;
	}
	
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
	
	public void setCapacidadepeso(Double capacidadepeso) {
		this.capacidadepeso = capacidadepeso;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setListaCarregamento(Set<Carregamento> listaCarregamento) {
		this.listaCarregamento = listaCarregamento;
	}
	
	@DisplayName("Placa da Segunda Carreta")
	public String getPlacaSegundaCarreta() {
		return placaSegundaCarreta;
	}
	public void setPlacaSegundaCarreta(String placaSegundaCarreta) {
		this.placaSegundaCarreta = placaSegundaCarreta;
	}
}
