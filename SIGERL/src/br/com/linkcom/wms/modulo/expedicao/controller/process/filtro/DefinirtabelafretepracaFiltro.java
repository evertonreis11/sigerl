package br.com.linkcom.wms.modulo.expedicao.controller.process.filtro;

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.Cep;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Tabelafretepraca;
import br.com.linkcom.wms.geral.bean.Tabelafreterota;
import br.com.linkcom.wms.geral.bean.Tipoentrega;
import br.com.linkcom.wms.geral.bean.Tipotabelafrete;

public class DefinirtabelafretepracaFiltro extends FiltroListagem{
	
	private Tabelafreterota tabelafreterota;
	private Integer cdtabelafrete;
	private Deposito deposito;
	private Tipotabelafrete tipotabelafrete;
	private Tipoentrega tipoentrega;
	private Cep cepinicio;
	private Cep cepfim;
	private String nomepraca;
	private List<Praca> listaPraca = new ArrayList<Praca>();
	private Rota rota;
	private List<Tabelafretepraca> listaTabelafretepraca = new ArrayList<Tabelafretepraca>();
	
	//Get's
	public Tabelafreterota getTabelafreterota() {
		return tabelafreterota;
	}
	public Integer getCdtabelafrete() {
		return cdtabelafrete;
	}
	public Deposito getDeposito() {
		return deposito;
	}
	public Tipotabelafrete getTipotabelafrete() {
		return tipotabelafrete;
	}
	public Tipoentrega getTipoentrega() {
		return tipoentrega;
	}
	public Cep getCepinicio() {
		return cepinicio;
	}
	public Cep getCepfim() {
		return cepfim;
	}
	public String getNomepraca() {
		return nomepraca;
	}
	public List<Praca> getListaPraca() {
		return listaPraca;
	}
	public Rota getRota() {
		return rota;
	}
	public List<Tabelafretepraca> getListaTabelafretepraca() {
		return listaTabelafretepraca;
	}
	
	
	//Set's
	public void setTabelafreterota(Tabelafreterota tabelafreterota) {
		this.tabelafreterota = tabelafreterota;
	}
	public void setCdtabelafrete(Integer cdtabelafrete) {
		this.cdtabelafrete = cdtabelafrete;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setTipotabelafrete(Tipotabelafrete tipotabelafrete) {
		this.tipotabelafrete = tipotabelafrete;
	}
	public void setTipoentrega(Tipoentrega tipoentrega) {
		this.tipoentrega = tipoentrega;
	}
	public void setCepinicio(Cep cepinicio) {
		this.cepinicio = cepinicio;
	}
	public void setCepfim(Cep cepfim) {
		this.cepfim = cepfim;
	}
	public void setNomepraca(String nomepraca) {
		this.nomepraca = nomepraca;
	}
	public void setListaPraca(List<Praca> listaPraca) {
		this.listaPraca = listaPraca;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
	}
	public void setListaTabelafretepraca(List<Tabelafretepraca> listaTabelafretepraca) {
		this.listaTabelafretepraca = listaTabelafretepraca;
	}
	
}
