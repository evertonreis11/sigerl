package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.MaxValue;
import br.com.linkcom.wms.geral.bean.Reabastecimentostatus;

public class ReabastecimentoFiltro extends FiltroListagem {
	
	protected Integer cdreabastecimento;
	protected Date datainicial;
	protected Date datafinal;
	protected Reabastecimentostatus situacao;
	
	@MaxValue(Integer.MAX_VALUE)
	@MaxLength(9)
	@DisplayName("Número do reabastecimento")
	public Integer getCdreabastecimento() {
		return cdreabastecimento;
	}
	
	public Date getDatainicial() {
		return datainicial;
	}
	public Date getDatafinal() {
		return datafinal;
	}
	
	@DisplayName("Situação")
	public Reabastecimentostatus getSituacao() {
		return situacao;
	}

	public void setCdreabastecimento(Integer cdinventario) {
		this.cdreabastecimento = cdinventario;
	}
	
	public void setDatainicial(Date datainicial) {
		this.datainicial = datainicial;
	}
	public void setDatafinal(Date datafinal) {
		this.datafinal = datafinal;
	}
	public void setSituacao(Reabastecimentostatus situacao) {
		this.situacao = situacao;
	}
	
}
