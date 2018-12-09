package br.com.linkcom.wms.modulo.recebimento.controller.process.filtro;

import java.sql.Date;

import br.com.linkcom.neo.types.Hora;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Usuario;

public class FinalizarOsRecebimentoFiltro {
	
	protected Ordemservico ordemservico;
	protected Usuario usuario;
	protected Date dtinicio;
	protected Hora hrinicio;
	protected Hora hrfim;
	
	protected String cdsordensservicos;
	
	public Ordemservico getOrdemservico() {
		return ordemservico;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public Date getDtinicio() {
		return dtinicio;
	}
	public Hora getHrinicio() {
		return hrinicio;
	}
	public Hora getHrfim() {
		return hrfim;
	}
	public String getCdsordensservicos() {
		return cdsordensservicos;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public void setDtinicio(Date dtinicio) {
		this.dtinicio = dtinicio;
	}
	public void setHrinicio(Hora hrinicio) {
		this.hrinicio = hrinicio;
	}
	public void setHrfim(Hora hrfim) {
		this.hrfim = hrfim;
	}
	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}
	public void setCdsordensservicos(String cdsordensservicos) {
		this.cdsordensservicos = cdsordensservicos;
	}
	
}
