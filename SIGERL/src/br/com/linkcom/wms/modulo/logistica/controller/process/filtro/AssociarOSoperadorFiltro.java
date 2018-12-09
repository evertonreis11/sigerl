package br.com.linkcom.wms.modulo.logistica.controller.process.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Usuario;

public class AssociarOSoperadorFiltro extends FiltroListagem {
	protected Integer cdordemservico;
	protected String logincoletor;
	
	protected Ordemservico ordemservico;
	protected Usuario usuario;
	
	@DisplayName("Ordem de serviço")
	@MaxLength(9)
	public Integer getCdordemservico() {
		return cdordemservico;
	}

	@DisplayName("Login do operador")
	@MaxLength(15)
	public String getLogincoletor() {
		return logincoletor;
	}
	
	public void setCdordemservico(Integer cdordemservico) {
		this.cdordemservico = cdordemservico;
	}

	public void setLogincoletor(String logincoletor) {
		this.logincoletor = logincoletor;
	}

	public Ordemservico getOrdemservico() {
		return ordemservico;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
