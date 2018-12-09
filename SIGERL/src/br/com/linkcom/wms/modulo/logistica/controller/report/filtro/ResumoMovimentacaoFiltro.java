package br.com.linkcom.wms.modulo.logistica.controller.report.filtro;

import java.util.Date;
import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Tipooperacao;

public class ResumoMovimentacaoFiltro {

	private List<Deposito> depositos;
	private List<Tipooperacao> listatipooperacao;
	private Date datainicial;
	private Date datafinal;

	@DisplayName("Depósitos")
	public List<Deposito> getDepositos() {
		return depositos;
	}
	
	@DisplayName("Tipos de operação")
	public List<Tipooperacao> getListatipooperacao() {
		return listatipooperacao;
	}

	@Required
	@DisplayName("Data inicial")
	public Date getDatainicial() {
		return datainicial;
	}

	@DisplayName("Data final")
	public Date getDatafinal() {
		return datafinal;
	}

	public void setDepositos(List<Deposito> depositos) {
		this.depositos = depositos;
	}
	
	public void setListatipooperacao(List<Tipooperacao> listatipooperacao) {
		this.listatipooperacao = listatipooperacao;
	}

	public void setDatainicial(Date datainicial) {
		this.datainicial = datainicial;
	}

	public void setDatafinal(Date datafinal) {
		this.datafinal = datafinal;
	}

}
