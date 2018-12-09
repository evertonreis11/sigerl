package br.com.linkcom.wms.modulo.expedicao.controller.report.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Expedicao;

public class EmitirmapaseparacaoPapelFiltro extends FiltroListagem{
	
	private  Carregamento carregamento;
	private  Expedicao expedicao;
	private String cdsOS;
	
	public Carregamento getCarregamento() {
		return carregamento;
	}
	
	public Expedicao getExpedicao() {
		return expedicao;
	}
	
	public String getCdsOS() {
		return cdsOS;
	}
	
	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}
	
	public void setExpedicao(Expedicao expedicao) {
		this.expedicao = expedicao;
	}
	
	public void setCdsOS(String cdsOS) {
		this.cdsOS = cdsOS;
	}
}
