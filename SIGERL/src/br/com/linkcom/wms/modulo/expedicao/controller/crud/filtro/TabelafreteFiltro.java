package br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.Cep;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Tipoentrega;
import br.com.linkcom.wms.geral.bean.Tipotabelafrete;

public class TabelafreteFiltro extends FiltroListagem{

	private Integer cdtabelafrete;
	private Deposito deposito;
	private Tipotabelafrete tipotabelafrete;
	private Tipoentrega tipoentrega;
	private Cep cepinicio;
	private Cep cepfim;
	private String nomepraca;
	private Integer cdtabelafreterota;
	
	
	//Get's
	@DisplayName("Núm. do Frete")
	public Integer getCdtabelafrete() {
		return cdtabelafrete;
	}
	@DisplayName("Depósito")
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
	public Integer getCdtabelafreterota() {
		return cdtabelafreterota;
	}
	
	
	//Set's
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
	public void setCdtabelafreterota(Integer cdtabelafreterota) {
		this.cdtabelafreterota = cdtabelafreterota;
	}

}
