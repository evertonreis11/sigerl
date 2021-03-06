package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import java.util.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Deposito;

public class CortediarioFiltro extends FiltroListagem{

	private Deposito deposito;
	private Date datainicio;
	private Date datafim;

	@Required
	@DisplayName("Dep�sito")
	public Deposito getDeposito() {
		return deposito;
	}

	@Required
	@DisplayName("Data inicial")
	public Date getDatainicio() {
		return datainicio;
	}
	
	@DisplayName("Data final")
	public Date getDatafim() {
		return datafim;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setDatainicio(Date datafechamento) {
		this.datainicio = datafechamento;
	}

	public void setDatafim(Date datafinal) {
		this.datafim = datafinal;
	}
	
}
