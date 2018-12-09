package br.com.linkcom.wms.geral.bean;

import java.sql.Date;
import java.util.List;

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

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_tabelafrete", sequenceName = "sq_tabelafrete")
public class Tabelafrete {
	
	private Integer cdtabelafrete;
	private Deposito deposito;
	private Date validadeinicial;
	private Date validadefinal;
	private Tipoveiculo tipoveiculo;
	private Transportador transportador;
	private Tipotabelafrete tipotabelafrete;
	private Tipoentrega tipoentrega;
	private List<Tabelafreterota> listaTabelafreterota;
	private List<Tabelafretehistorico> listaTabelafretehistorico;
	private Money valorfechado;
	
	//Transient's
	private Rota rota = new Rota(0);
	
	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_tabelafrete")
	public Integer getCdtabelafrete() {
		return cdtabelafrete;
	}
	@Required
    @DisplayName("Depósito")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	public Date getValidadeinicial() {
		return validadeinicial;
	}
	public Date getValidadefinal() {
		return validadefinal;
	}
    @DisplayName("Tipo Veículo")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipoveiculo")
	public Tipoveiculo getTipoveiculo() {
		return tipoveiculo;
	}
    @DisplayName("Transportador")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtransportador")
	public Transportador getTransportador() {
		return transportador;
	}
    @Required
    @DisplayName("Tipo de Tabela")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipotabelafrete")
	public Tipotabelafrete getTipotabelafrete() {
		return tipotabelafrete;
	}
    @Required
    @DisplayName("Tipo de Entrega")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipoentrega")
	public Tipoentrega getTipoentrega() {
		return tipoentrega;
	}
    @OneToMany(mappedBy="tabelafrete")
    public List<Tabelafreterota> getListaTabelafreterota() {
    	return listaTabelafreterota;
    }
    @OneToMany(mappedBy="tabelafrete")    
	public List<Tabelafretehistorico> getListaTabelafretehistorico() {
		return listaTabelafretehistorico;
	}
    public Money getValorfechado() {
		return valorfechado;
	}
    
    
	//Set's
	public void setCdtabelafrete(Integer cdtabelafrete) {
		this.cdtabelafrete = cdtabelafrete;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setValidadeinicial(Date validadeinicial) {
		this.validadeinicial = validadeinicial;
	}
	public void setValidadefinal(Date validadefinal) {
		this.validadefinal = validadefinal;
	}
	public void setTipoveiculo(Tipoveiculo tipoveiculo) {
		this.tipoveiculo = tipoveiculo;
	}
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
	public void setTipotabelafrete(Tipotabelafrete tipotabelafrete) {
		this.tipotabelafrete = tipotabelafrete;
	}
	public void setTipoentrega(Tipoentrega tipoentrega) {
		this.tipoentrega = tipoentrega;
	}
	public void setListaTabelafreterota(List<Tabelafreterota> listaTabelafreterota) {
		this.listaTabelafreterota = listaTabelafreterota;
	}
	public void setListaTabelafretehistorico(List<Tabelafretehistorico> listaTabelafretehistorico) {
		this.listaTabelafretehistorico = listaTabelafretehistorico;
	}
	public void setValorfechado(Money valorfechado) {
		this.valorfechado = valorfechado;
	}
	
	
	//Transient's
	@Transient
	public Rota getRota() {
		return rota;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
	}
	
}
