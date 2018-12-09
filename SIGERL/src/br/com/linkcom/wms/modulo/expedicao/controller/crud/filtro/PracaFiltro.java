package br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro;

import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.Cep;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Tiporotapraca;

public class PracaFiltro extends FiltroListagem {
	
	protected Cep cepinicio;
	protected Cep cepfim;
	protected String nome;
	protected Integer cdrota;
	protected Tiporotapraca tiporotapraca;
	protected List<Praca> listaPraca;
	protected String pracasSelecionadas;
	protected Integer cdpraca;
	protected Integer cdtiporotapraca;
	
	
	//Get's
	@MaxLength(9)
	public Cep getCepinicio() {
		return cepinicio;
	}
	@MaxLength(9)
	public Cep getCepfim() {
		return cepfim;
	}
	@DisplayName("Nome")
	@MaxLength(50)
	public String getNome() {
		return nome;
	}
	public Integer getCdrota() {
		return cdrota;
	}
	public Tiporotapraca getTiporotapraca() {
		return tiporotapraca;
	}
	public String getPracasSelecionadas() {
		return pracasSelecionadas;
	}
	public Integer getCdpraca() {
		return cdpraca;
	}
	public Integer getCdtiporotapraca() {
		return cdtiporotapraca;
	}
	
	
	//Set's
	public List<Praca> getListaPraca() {
		return listaPraca;
	}
	public void setListaPraca(List<Praca> listaPraca) {
		this.listaPraca = listaPraca;
	}
	public void setTiporotapraca(Tiporotapraca tiporotapraca) {
		this.tiporotapraca = tiporotapraca;
	}
	public void setCdrota(Integer cdrota) {
		this.cdrota = cdrota;
	}
	public void setCepinicio(Cep cepinicio) {
		this.cepinicio = cepinicio;
	}
	public void setCepfim(Cep cepfim) {
		this.cepfim = cepfim;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setPracasSelecionadas(String pracasSelecionadas) {
		this.pracasSelecionadas = pracasSelecionadas;
	}
	public void setCdpraca(Integer cdpraca) {
		this.cdpraca = cdpraca;
	}
	public void setCdtiporotapraca(Integer cdtiporotapraca) {
		this.cdtiporotapraca = cdtiporotapraca;
	}

}
