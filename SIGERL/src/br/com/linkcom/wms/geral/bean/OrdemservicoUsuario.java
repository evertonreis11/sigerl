package br.com.linkcom.wms.geral.bean;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Hora;

@Entity
@SequenceGenerator(name = "sq_ordemservicousuario", sequenceName = "sq_ordemservicousuario")
public class OrdemservicoUsuario {

	// ------------------------------ Variáveis de instância----------------------------------
	protected Integer cdordemservicousuario;
	protected Ordemservico ordemservico;
	protected Usuario usuario;
	protected Timestamp dtinicio;
	protected Timestamp dtfim;
	protected Integer paletes;
	
	// ------------------------------ Propriedades transientes ----------------------------------
	protected Date datainicio;
	protected Date datafim;
	protected Hora horainicio;
	protected Hora horafim;
	
	
	// ---------------------------------- Construtores ---------------------------------------
	public OrdemservicoUsuario() {

	}
	
	public OrdemservicoUsuario(Integer cd) {
		this.cdordemservicousuario = cd;
	}

	public OrdemservicoUsuario(Ordemservico ordemservico, Usuario usuario, Timestamp dtinicio, Timestamp dtfim) {
		this.ordemservico = ordemservico;
		this.usuario = usuario;
		this.dtinicio = dtinicio;
		this.dtfim = dtfim;
	}
	
	public OrdemservicoUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	// ---------------------------------- Métodos get e set -----------------------------------
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_ordemservicousuario")
	public Integer getCdordemservicousuario() {
		return cdordemservicousuario;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdordemservico")
	public Ordemservico getOrdemservico() {
		return ordemservico;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpessoa")
	public Usuario getUsuario() {
		return usuario;
	}

	@DisplayName("Data e hora inicial")
	public Timestamp getDtinicio() {
		return dtinicio;
	}

	@DisplayName("Data e hora final")
	public Timestamp getDtfim() {
		return dtfim;
	}

	@Transient
	@DisplayName("Data inicial")
	public Date getDatainicio() {
		return datainicio;
	}

	@Transient
	@DisplayName("Data final")
	public Date getDatafim() {
		return datafim;
	}

	@Transient
	@DisplayName("Hora inicial")
	public Hora getHorainicio() {
		return horainicio;
	}

	@Transient
	@DisplayName("Hora final")
	public Hora getHorafim() {
		return horafim;
	}

	public void setCdordemservicousuario(Integer cdordemservicousuario) {
		this.cdordemservicousuario = cdordemservicousuario;
	}
	
	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public void setDtinicio(Timestamp dtinicio) {
		this.dtinicio = dtinicio;
	}

	public void setDtfim(Timestamp dtfim) {
		this.dtfim = dtfim;
	}

	public void setDatainicio(Date datainicio) {
		this.datainicio = datainicio;
	}
	
	public void setDatafim(Date datafim) {
		this.datafim = datafim;
	}
	
	public void setHorainicio(Hora horainicio) {
		this.horainicio = horainicio;
	}
	
	public void setHorafim(Hora horafim) {
		this.horafim = horafim;
	}

	public Integer getPaletes() {
		return paletes;
	}

	public void setPaletes(Integer paletes) {
		this.paletes = paletes;
	}

}
