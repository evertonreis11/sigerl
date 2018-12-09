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
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name="sq_inventario", sequenceName="sq_inventario")
@DisplayName("Inventário")
public class Inventario {
	
	protected Integer cdinventario;
	protected Inventariotipo inventariotipo =  Inventariotipo.PARCIAl;
	protected Date dtinventario = new Date(System.currentTimeMillis());
	protected Inventariostatus inventariostatus;
	protected Deposito deposito;
	protected Double acuracia;
	protected List<Inventariolote> listaInventariolote = new ListSet<Inventariolote>(Inventariolote.class);
	
	//Transiente
	protected String ordens;
	
	
	// construtor
	public Inventario() {

	}
	
	public Inventario(Integer cd) {
		this.cdinventario = cd;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(generator="sq_inventario",strategy=GenerationType.AUTO)
	@Required
	public Integer getCdinventario() {
		return cdinventario;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdinventariotipo")
	@DisplayName("Tipo de inventário")
	@Required
	public Inventariotipo getInventariotipo() {
		return inventariotipo;
	}
	
	@Required
	@DisplayName("Data do inventário")
	public Date getDtinventario() {
		return dtinventario;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdinventariostatus")
	@DisplayName("Situação")
	@Required
	public Inventariostatus getInventariostatus() {
		return inventariostatus;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@DisplayName("Acurácia")
	public Double getAcuracia() {
		return acuracia;
	}
	
	@OneToMany(mappedBy="inventario")
	public List<Inventariolote> getListaInventariolote() {
		return listaInventariolote;
	}
	
	@Transient
	public String getOrdens() {
		return ordens;
	}
	
	public void setCdinventario(Integer cdinventario) {
		this.cdinventario = cdinventario;
	}
	
	public void setInventariotipo(Inventariotipo inventariotipo) {
		this.inventariotipo = inventariotipo;
	}
	
	public void setDtinventario(Date dtinventario) {
		this.dtinventario = dtinventario;
	}
	
	public void setInventariostatus(Inventariostatus inventariostatus) {
		this.inventariostatus = inventariostatus;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setAcuracia(Double acuracia) {
		this.acuracia = acuracia;
	}

	public void setListaInventariolote(List<Inventariolote> listaInventariolote) {
		this.listaInventariolote = listaInventariolote;
	}
	
	public void setOrdens(String ordens) {
		this.ordens = ordens;
	}
}
