package br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Tiporotapraca;

/**
 * @author Guilherme Arantes de Oliveira
 *
 */
public class RotaFiltro extends FiltroListagem {
	
	protected String nome;
	protected Praca praca;
	protected Rota rota;
	protected Integer linhaInicial;
	protected Integer linhaFinal;
	protected Tiporotapraca tiporotapraca;
	
	
	@DisplayName("Nome")
	@MaxLength(50)	
	public String getNome() {
		return nome;
	}
	
	@DisplayName("Praça")
	public Praca getPraca() {
		return praca;
	}
	
	public Rota getRota() {
		return rota;
	}

	public Integer getLinhaInicial() {
		return linhaInicial;
	}

	public Integer getLinhaFinal() {
		return linhaFinal;
	}

	public Tiporotapraca getTiporotapraca() {
		return tiporotapraca;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public void setLinhaInicial(Integer linhaInicial) {
		this.linhaInicial = linhaInicial;
	}

	public void setLinhaFinal(Integer linhaFinal) {
		this.linhaFinal = linhaFinal;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setPraca(Praca praca) {
		this.praca = praca;
	}

	public void setTiporotapraca(Tiporotapraca tiporotapraca) {
		this.tiporotapraca = tiporotapraca;
	}
	
}
