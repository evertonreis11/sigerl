package br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Tipoveiculo;
import br.com.linkcom.wms.geral.bean.Transportador;

public class VeiculoFiltro extends FiltroListagem {
	
	protected String placa;
	protected String modelo;
	protected Tipoveiculo tipoveiculo;
	protected Boolean disponivel = Boolean.TRUE;
	protected Transportador transportador;
	
	@MaxLength(7)
	public String getPlaca() {
		return placa;
	}
	
	@MaxLength(20)
	public String getModelo() {
		return modelo;
	}
	
	@DisplayName("Tipo de veículo")
	public Tipoveiculo getTipoveiculo() {
		return tipoveiculo;
	}
	
	@DisplayName("Situação")
	public Boolean getDisponivel() {
		return disponivel;
	}
	
	@DisplayName("Transportadora")
	public Transportador getTransportador() {
		return transportador;
	}
	
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	public void setTipoveiculo(Tipoveiculo tipoveiculo) {
		this.tipoveiculo = tipoveiculo;
	}
	
	public void setDisponivel(Boolean disponivel) {
		this.disponivel = disponivel;
	}
	
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
}
