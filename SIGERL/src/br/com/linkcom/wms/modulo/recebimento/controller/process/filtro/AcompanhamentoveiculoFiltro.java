package br.com.linkcom.wms.modulo.recebimento.controller.process.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculostatus;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Fornecedor;

public class AcompanhamentoveiculoFiltro  extends FiltroListagem {

	private Integer cdacompanhamentoveiculo;
	private Fornecedor fornecedor;
	private Date dtinicio;
	private Date dtfim;
	private String numerorav;
	private Date dataentrada;
	private Integer cdagenda;
	private Deposito deposito;
	private String placa;
	private String agendaId;
	private Acompanhamentoveiculostatus acompanhamentoveiculostatus;
	
	
	//Get's
	@DisplayName("Número do RAV")
	public String getNumerorav() {
		return numerorav;
	}
	@DisplayName("Data de Entrada")
	public Date getDataentrada() {
		return dataentrada;
	}
	public void setNumerorav(String numerorav) {
		this.numerorav = numerorav;
	}
	public void setDataentrada(Date dataentrada) {
		this.dataentrada = dataentrada;
	}
	public Date getDtinicio() {
		return dtinicio;
	}
	public Date getDtfim() {
		return dtfim;
	}
	@DisplayName("Código da Solicitação ")
	public Integer getCdacompanhamentoveiculo() {
		return cdacompanhamentoveiculo;
	}
	public String getAgendaId() {
		return agendaId;
	}
	public Acompanhamentoveiculostatus getAcompanhamentoveiculostatus() {
		return acompanhamentoveiculostatus;
	}
	public Deposito getDeposito() {
		return deposito;
	}
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	public String getPlaca() {
		return placa;
	}	
	public Integer getCdagenda() {
		return cdagenda;
	}
	
	
	//Set's
	public void setAgendaId(String agendaId) {
		this.agendaId = agendaId;
	}
	public void setCdacompanhamentoveiculo(Integer cdacompanhamentoveiculo) {
		this.cdacompanhamentoveiculo = cdacompanhamentoveiculo;
	}
	public void setDtinicio(Date dtinicio) {
		this.dtinicio = dtinicio;
	}
	public void setDtfim(Date dtfim) {
		this.dtfim = dtfim;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public void setCdagenda(Integer cdagenda) {
		this.cdagenda = cdagenda;
	}
	public void setAcompanhamentoveiculostatus(Acompanhamentoveiculostatus acompanhamentoveiculostatus) {
		this.acompanhamentoveiculostatus = acompanhamentoveiculostatus;
	}
	
}