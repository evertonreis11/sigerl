package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_transportadordeposito", sequenceName = "sq_transportadordeposito")
public class Transportadordeposito {
	
	private Integer cdtransportadordeposito;
	private Deposito deposito;
	private Transportador transportador;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_transportadordeposito")
	public Integer getCdtransportadordeposito() {
		return cdtransportadordeposito;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@DisplayName("Depósito")
	@JoinColumn(name="cddeposito")
	@Required
	public Deposito getDeposito() {
		return deposito;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@DisplayName("Transportador")
	@JoinColumn(name="cdtransportador")
	@Required
	public Transportador getTransportador() {
		return transportador;
	}
	
	public void setCdtransportadordeposito(Integer cdtransportadordeposito) {
		this.cdtransportadordeposito = cdtransportadordeposito;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
	
}