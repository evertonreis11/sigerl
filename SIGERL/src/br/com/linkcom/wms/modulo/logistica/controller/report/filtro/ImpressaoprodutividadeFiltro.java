package br.com.linkcom.wms.modulo.logistica.controller.report.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.modulo.logistica.controller.report.ext.OperacaoProdutividade;

public class ImpressaoprodutividadeFiltro extends FiltroListagem {
	private Date datainicial;
	private Date datafinal;	
	private Usuario usuario;
	private OperacaoProdutividade ordemtipo;
	
	@DisplayName("OPERADOR")
	public Usuario getUsuario() {
		return usuario;
	}
	
	@DisplayName("OPERAÇÃO")
	public OperacaoProdutividade getOrdemtipo() {
		return ordemtipo;
	}
	
	public Date getDatainicial() {
		return datainicial;
	}

	public Date getDatafinal() {
		return datafinal;
	}

	public void setDatainicial(Date datainicial) {
		this.datainicial = datainicial;
	}

	public void setDatafinal(Date datafinal) {
		this.datafinal = datafinal;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void setOrdemtipo(OperacaoProdutividade ordemtipo) {
		this.ordemtipo = ordemtipo;
	}
}
