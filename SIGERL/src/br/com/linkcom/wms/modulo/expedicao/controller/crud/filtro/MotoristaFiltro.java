package br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.Cpf;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Equipamento;
import br.com.linkcom.wms.geral.bean.Transportador;

public class MotoristaFiltro extends FiltroListagem{

	private String nome;
	private String apelido;
	private Cpf cpf;
	private Transportador transportador;
	private Deposito deposito;
	private Equipamento equipamento;
	
	
	public String getNome() {
		return nome;
	}
	public String getApelido() {
		return apelido;
	}
	public Cpf getCpf() {
		return cpf;
	}
	public Transportador getTransportador() {
		return transportador;
	}
	public Deposito getDeposito() {
		return deposito;
	}
	public Equipamento getEquipamento() {
		return equipamento;
	}
	
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
	public void setCpf(Cpf cpf) {
		this.cpf = cpf;
	}
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}
}
