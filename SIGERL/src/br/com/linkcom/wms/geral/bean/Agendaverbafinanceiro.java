package br.com.linkcom.wms.geral.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_agendaverbafinanceiro", sequenceName = "sq_agendaverbafinanceiro")
public class Agendaverbafinanceiro {

	private Integer cdagendaverbafinanceiro;
	private Agendaverba agendaverba;
	private Date dtagendaverba;
	private Money verba;
	
	//Transientes
	private Money agendado;
	private Money recebido;
	private Money disponivel;
	

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_agendaverbafinanceiro")
	public Integer getCdagendaverbafinanceiro() {
		return cdagendaverbafinanceiro;
	}

	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdagendaverba")
	public Agendaverba getAgendaverba() {
		return agendaverba;
	}

	public Date getDtagendaverba() {
		return dtagendaverba;
	}

	public Money getVerba() {
		return verba;
	}

	public void setCdagendaverbafinanceiro(Integer cdagendaverbafinanceiro) {
		this.cdagendaverbafinanceiro = cdagendaverbafinanceiro;
	}

	public void setAgendaverba(Agendaverba agendaverba) {
		this.agendaverba = agendaverba;
	}

	public void setDtagendaverba(Date dtagendaverba) {
		this.dtagendaverba = dtagendaverba;
	}

	public void setVerba(Money verba) {
		this.verba = verba;
	}

	//Transientes

	@Transient
	public Money getDisponivel() {
		return disponivel;
	}

	@Transient
	public Money getAgendado() {
		return agendado;
	}

	@Transient
	public Money getRecebido() {
		return recebido;
	}

	public void setDisponivel(Money liberado) {
		this.disponivel = liberado;
	}

	public void setAgendado(Money agendado) {
		this.agendado = agendado;
	}

	public void setRecebido(Money recebido) {
		this.recebido = recebido;
	}

	
}
