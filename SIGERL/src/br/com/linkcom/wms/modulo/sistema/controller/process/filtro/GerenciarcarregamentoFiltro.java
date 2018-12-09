package br.com.linkcom.wms.modulo.sistema.controller.process.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;

public class GerenciarcarregamentoFiltro extends FiltroListagem {
	protected Integer cdcarregamento;
	protected Integer cdbox;
	protected Integer cdcarregamentostatus;
	
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}
	
	public Integer getCdbox() {
		return cdbox;
	}
	
	public void setCdbox(Integer cdbox) {
		this.cdbox = cdbox;
	}
	
	public Integer getCdcarregamentostatus() {
		return cdcarregamentostatus;
	}
	
	public void setCdcarregamentostatus(Integer cdcarregamentostatus) {
		this.cdcarregamentostatus = cdcarregamentostatus;
	}
}
