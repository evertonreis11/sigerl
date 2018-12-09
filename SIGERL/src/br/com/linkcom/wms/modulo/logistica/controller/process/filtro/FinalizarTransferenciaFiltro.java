package br.com.linkcom.wms.modulo.logistica.controller.process.filtro;

import java.sql.Date;

import br.com.linkcom.neo.types.Hora;
import br.com.linkcom.wms.geral.bean.Transferencia;
import br.com.linkcom.wms.geral.bean.Usuario;

public class FinalizarTransferenciaFiltro {

	protected Transferencia transferencia;
	protected Usuario usuario;
	protected Date dtinicio;
	protected Hora hrinicio;
	protected Hora hrfim;

	public Transferencia getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(Transferencia transferencia) {
		this.transferencia = transferencia;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getDtinicio() {
		return dtinicio;
	}

	public void setDtinicio(Date dtinicio) {
		this.dtinicio = dtinicio;
	}

	public Hora getHrinicio() {
		return hrinicio;
	}

	public void setHrinicio(Hora hrinicio) {
		this.hrinicio = hrinicio;
	}

	public Hora getHrfim() {
		return hrfim;
	}

	public void setHrfim(Hora hrfim) {
		this.hrfim = hrfim;
	}

}
