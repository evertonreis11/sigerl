package br.com.linkcom.wms.geral.bean;

import java.util.Date;
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
@SequenceGenerator(name = "sq_agendaverba", sequenceName = "sq_agendaverba")
public class Agendaverba {

	private Integer cdagendaverba;
	private Deposito deposito;
	private Produtoclasse produtoclasse;
	private Date dtagendaverba;
	private Money verba;
	private List<Agendaverbafinanceiro> listaAgendaverbafinanceiro;
	
	//Campos transientes
	private Money valorLiberado;
	private Money valorAgendado;
	private Money valorRecebido;
	private Money valorDisponivel;
	private Money valorLiberadoTotal;
	private List<Agendaverbafinanceiro> listaAgendaverbafinanceiroControle;

	public Agendaverba(){}
	
	public Agendaverba(Produtoclasse produtoclasse, Date data){
		this.produtoclasse = produtoclasse;
		this.dtagendaverba = data;
		this.verba = new Money(0);
	}
	
	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_agendaverba")
	public Integer getCdagendaverba() {
		return cdagendaverba;
	}

	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName("Depósito")
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}

	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName("Classe de produto")
	@JoinColumn(name="cdprodutoclasse")
	public Produtoclasse getProdutoclasse() {
		return produtoclasse;
	}

	public Date getDtagendaverba() {
		return dtagendaverba;
	}

	public Money getVerba() {
		return verba;
	}
	
	@OneToMany(mappedBy="agendaverba")
	public List<Agendaverbafinanceiro> getListaAgendaverbafinanceiro() {
		return listaAgendaverbafinanceiro;
	}

	@Transient
	public Money getValorLiberado() {
		return valorLiberado;
	}

	@Transient
	public Money getValorAgendado() {
		return valorAgendado;
	}

	@Transient
	public Money getValorRecebido() {
		return valorRecebido;
	}

	@Transient
	public Money getValorDisponivel() {
		return valorDisponivel;
	}
	
	@Transient
	public Money getValorLiberadoTotal() {
		return valorLiberadoTotal;
	}

	public void setValorLiberadoTotal(Money valorLiberadoTotal) {
		this.valorLiberadoTotal = valorLiberadoTotal;
	}

	public void setCdagendaverba(Integer cdagendaverba) {
		this.cdagendaverba = cdagendaverba;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setProdutoclasse(Produtoclasse produtoclasse) {
		this.produtoclasse = produtoclasse;
	}

	public void setDtagendaverba(Date dtagendaverba) {
		this.dtagendaverba = dtagendaverba;
	}

	public void setVerba(Money verba) {
		this.verba = verba;
	}

	public void setListaAgendaverbafinanceiro(List<Agendaverbafinanceiro> listaAgendaverbafinanceiro) {
		this.listaAgendaverbafinanceiro = listaAgendaverbafinanceiro;
	}
	
	public void setValorLiberado(Money valorLiberado) {
		this.valorLiberado = valorLiberado;
	}

	public void setValorAgendado(Money valorAgendado) {
		this.valorAgendado = valorAgendado;
	}

	public void setValorRecebido(Money valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public void setValorDisponivel(Money valorDisponivel) {
		this.valorDisponivel = valorDisponivel;
	}
	
	@Transient
	public List<Agendaverbafinanceiro> getListaAgendaverbafinanceiroControle() {
		return listaAgendaverbafinanceiroControle;
	}
	public void setListaAgendaverbafinanceiroControle(List<Agendaverbafinanceiro> listaAgendaverbafinanceiroControle) {
		this.listaAgendaverbafinanceiroControle = listaAgendaverbafinanceiroControle;
	}
}
