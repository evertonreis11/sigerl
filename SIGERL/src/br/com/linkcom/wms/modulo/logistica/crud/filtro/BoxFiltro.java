package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Boxstatus;
import br.com.linkcom.wms.geral.bean.Deposito;

public class BoxFiltro extends FiltroListagem {

	private String nome;
	private Deposito deposito;
	private Boxstatus boxstatus;
	private Integer cdcarregamento;

	@DisplayName("Nome (Número do box)")
	@MaxLength(5)
	public String getNome() {
		return nome;
	}

	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}

	@DisplayName("Status do box")
	public Boxstatus getBoxstatus() {
		return boxstatus;
	}
	
	@DisplayName("Carregamento")
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setBoxstatus(Boxstatus boxstatus) {
		this.boxstatus = boxstatus;
	}
	
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}

}
