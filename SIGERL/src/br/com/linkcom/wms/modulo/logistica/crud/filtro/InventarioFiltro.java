package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Inventariostatus;
import br.com.linkcom.wms.geral.bean.Inventariotipo;

public class InventarioFiltro extends FiltroListagem{
	
	protected Inventariotipo inventariotipo;
	protected Integer cdinventario;
	protected Date datainicial;
	protected Date datafinal;
	protected Inventariostatus situacao;
	
	@DisplayName("Tipo de inventário")
	public Inventariotipo getInventariotipo() {
		return inventariotipo;
	}
	
	@DisplayName("Número do inventário")
	public Integer getCdinventario() {
		return cdinventario;
	}
	
	public Date getDatainicial() {
		return datainicial;
	}
	public Date getDatafinal() {
		return datafinal;
	}
	
	@DisplayName("Situação")
	public Inventariostatus getSituacao() {
		return situacao;
	}
	public void setInventariotipo(Inventariotipo inventariotipo) {
		this.inventariotipo = inventariotipo;
	}
	
	public void setCdinventario(Integer cdinventario) {
		this.cdinventario = cdinventario;
	}
	
	public void setDatainicial(Date datainicial) {
		this.datainicial = datainicial;
	}
	public void setDatafinal(Date datafinal) {
		this.datafinal = datafinal;
	}
	public void setSituacao(Inventariostatus situacao) {
		this.situacao = situacao;
	}
	
	
	
}
