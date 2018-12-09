package br.com.linkcom.wms.modulo.logistica.controller.process.filtro;

import java.sql.Date;
import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.Hora;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Inventario;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Usuario;

public class LancarDadosLotesFiltro extends FiltroListagem{

	protected Usuario responsavel;
	protected Date dtinicio;
	protected Date dtfim;
	protected Hora hrinicio;
	protected Hora hrfim;
	protected Inventario inventario;
	protected Ordemservico ordemservico;
	protected List<Ordemprodutohistorico> listaOrdemProdutoHistorico = new ListSet<Ordemprodutohistorico>(Ordemprodutohistorico.class);
	
	@Required
	@DisplayName("Responsável")
	public Usuario getResponsavel() {
		return responsavel;
	}
	@DisplayName("Data início")	
	public Date getDtinicio() {
		return dtinicio;
	}
	
	@DisplayName("Data fim")
	public Date getDtfim() {
		return dtfim;
	}
	
	@DisplayName("Hora início")
	public Hora getHrinicio() {
		return hrinicio;
	}
	
	@DisplayName("Hora fim")
	public Hora getHrfim() {
		return hrfim;
	}
	public Inventario getInventario() {
		return inventario;
	}
	public Ordemservico getOrdemservico() {
		return ordemservico;
	}
	public List<Ordemprodutohistorico> getListaOrdemProdutoHistorico() {
		return listaOrdemProdutoHistorico;
	}
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
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
	public void setInventario(Inventario inventario) {
		this.inventario = inventario;
	}
	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}
	public void setListaOrdemProdutoHistorico(List<Ordemprodutohistorico> listaOrdemProdutoHistorico) {
		this.listaOrdemProdutoHistorico = listaOrdemProdutoHistorico;
	}
	
}
