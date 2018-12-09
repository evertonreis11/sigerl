package br.com.linkcom.wms.modulo.expedicao.controller.process.filtro;

import java.sql.Date;

import br.com.linkcom.neo.types.Hora;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Usuario;

public class FinalizarOsReabastecimentoFiltro {
	
	protected Ordemservico ordemservico;
	protected Usuario usuario;
	protected Date dtinicio;
	protected Date dtfim;
	protected Hora hrinicio;
	protected Hora hrfim;
	
	
	public Ordemservico getOrdemservico() {
		return ordemservico;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public Date getDtinicio() {
		return dtinicio;
	}
	public Date getDtfim() {
		return dtfim;
	}
	public Hora getHrinicio() {
		return hrinicio;
	}
	public Hora getHrfim() {
		return hrfim;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public void setDtinicio(Date dtinicio) {
		this.dtinicio = dtinicio;
	}
	public void setDtfim(Date dtfim) {
		this.dtfim = dtfim;
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
	
}
