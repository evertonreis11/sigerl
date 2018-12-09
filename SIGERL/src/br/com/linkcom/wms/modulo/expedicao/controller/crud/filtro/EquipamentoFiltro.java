package br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Deposito;

public class EquipamentoFiltro extends FiltroListagem{
	
	private String nome;
	private Deposito deposito;
	private String mac;
	private Boolean rastreado;
	
	public String getNome() {
		return nome;
	}
	public Deposito getDeposito() {
		return deposito;
	}
	public String getMac() {
		return mac;
	}
	public Boolean getRastreado() {
		return rastreado;
	}
	
	public void setMac(String mac) {
		this.mac = mac;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setRastreado(Boolean rastreado) {
		this.rastreado = rastreado;
	}
}
