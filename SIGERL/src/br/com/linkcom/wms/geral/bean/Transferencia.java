package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;
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

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_transferencia", sequenceName = "sq_transferencia")
public class Transferencia {
	// Variaveis de instancia
	protected Integer cdtransferencia;
	protected Deposito deposito;
	protected Timestamp dttransferencia;
	protected Transferenciastatus transferenciastatus;
	protected List<Transferenciaitem> listaTransferenciaitem = new ListSet<Transferenciaitem>(Transferenciaitem.class);
	
	//Transientes
	protected Boolean hasOrdemServico;
	
	// Construtores
	public Transferencia() {

	}
	
	public Transferencia(Integer cd) {
		this.cdtransferencia = cd;
	}
	
	// Metodos get e set
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_transferencia")
	@DescriptionProperty
	public Integer getCdtransferencia() {
		return cdtransferencia;
	}
	
	@DisplayName("Data da transferência")
	public Timestamp getDttransferencia() {
		return dttransferencia;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdtransferenciastatus")
	@DisplayName("Status da transferência")
	public Transferenciastatus getTransferenciastatus() {
		return transferenciastatus;
	}
	
	@OneToMany(mappedBy="transferencia")
	public List<Transferenciaitem> getListaTransferenciaitem() {
		return listaTransferenciaitem;
	}

	@Required
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@Transient
	public Boolean getHasOrdemServico() {
		return hasOrdemServico;
	}
	
	public void setCdtransferencia(Integer cdtransferencia) {
		this.cdtransferencia = cdtransferencia;
	}
	
	public void setDttransferencia(Timestamp dttransferencia) {
		this.dttransferencia = dttransferencia;
	}
	
	public void setTransferenciastatus(Transferenciastatus transferenciastatus) {
		this.transferenciastatus = transferenciastatus;
	}
	
	public void setListaTransferenciaitem(List<Transferenciaitem> listaTransferenciaitem) {
		this.listaTransferenciaitem = listaTransferenciaitem;
	}
	
	public void setHasOrdemServico(Boolean hasOrdemServico) {
		this.hasOrdemServico = hasOrdemServico;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
}
