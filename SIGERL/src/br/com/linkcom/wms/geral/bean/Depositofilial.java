package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
public class Depositofilial {

	private Integer cddepositofilial;
	private Deposito deposito;
	private Cliente filial;
	
	//Get's
	@Id
	public Integer getCddepositofilial() {
		return cddepositofilial;
	}
	@DisplayName("Endereço do cliente")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	@DisplayName("Endereço do cliente")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdfilial")
	public Cliente getFilial() {
		return filial;
	}
	
	//Set's
	public void setCddepositofilial(Integer cddepositofilial) {
		this.cddepositofilial = cddepositofilial;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setFilial(Cliente filial) {
		this.filial = filial;
	}
	
}
