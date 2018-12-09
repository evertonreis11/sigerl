package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
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

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_controleestoque", sequenceName = "sq_controleestoque")
public class Controleestoque {

	protected Integer cdcontroleestoque;
	protected Timestamp dtcontroleestoque;
	protected Controleestoquestatus controleestoquestatus;
	protected Deposito deposito;
	protected List<Controleestoqueproduto> listaControleestoqueproduto = new ArrayList<Controleestoqueproduto>();
	
	public Controleestoque(){
		
	}
	
	public Controleestoque(Integer cdControleestoque) {
		this.cdcontroleestoque = cdControleestoque;
	}
	
	//Metodos get e set
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_controleestoque")
	public Integer getCdcontroleestoque() {
		return cdcontroleestoque;
	}
	
	@Required
	@DisplayName("Data")
	public Timestamp getDtcontroleestoque() {
		return dtcontroleestoque;
	}
	
	@Required
	@DisplayName("Status")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdcontroleestoquestatus")
	public Controleestoquestatus getControleestoquestatus() {
		return controleestoquestatus;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")
	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@OneToMany(mappedBy="controleestoque")
	public List<Controleestoqueproduto> getListaControleestoqueproduto() {
		return listaControleestoqueproduto;
	}
	
	public void setCdcontroleestoque(Integer cdcontroleestoque) {
		this.cdcontroleestoque = cdcontroleestoque;
	}
	
	public void setDtcontroleestoque(Timestamp dtcontroleestoque) {
		this.dtcontroleestoque = dtcontroleestoque;
	}
	
	public void setControleestoquestatus(
			Controleestoquestatus controleestoquestatus) {
		this.controleestoquestatus = controleestoquestatus;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setListaControleestoqueproduto(
			List<Controleestoqueproduto> listaControleestoqueproduto) {
		this.listaControleestoqueproduto = listaControleestoqueproduto;
	}
	
}
