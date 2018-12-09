package br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;

public class RecebimentolistagemFiltro extends FiltroListagem {
	protected Integer cdrecebimento;
	protected Date dataInicio;
	protected Date dataFim;
	protected Box box;
	protected Recebimentostatus recebimentostatus;
	
	@DisplayName("Número do recebimento")	
	public Integer getCdrecebimento() {
		return cdrecebimento;
	}
	
	public Date getDataInicio() {
		return dataInicio;
	}
	
	public Date getDataFim() {
		return dataFim;
	}
	
	@DisplayName("Box")
	public Box getBox() {
		return box;
	}
	
	@DisplayName("Status")
	public Recebimentostatus getRecebimentostatus() {
		return recebimentostatus;
	}
	
	public void setCdrecebimento(Integer cdrecebimento) {
		this.cdrecebimento = cdrecebimento;
	}
	
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public void setBox(Box box) {
		this.box = box;
	}
	
	public void setRecebimentostatus(Recebimentostatus recebimentostatus) {
		this.recebimentostatus = recebimentostatus;
	}
}
