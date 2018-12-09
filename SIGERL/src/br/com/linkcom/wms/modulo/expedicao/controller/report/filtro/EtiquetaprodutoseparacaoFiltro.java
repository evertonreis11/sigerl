package br.com.linkcom.wms.modulo.expedicao.controller.report.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Expedicao;

public class EtiquetaprodutoseparacaoFiltro extends FiltroListagem{
	
	
	private Carregamento carregamento;
	private Expedicao expedicao;
	private Boolean impressaoOnda;
	
	public Carregamento getCarregamento() {
		return carregamento;
	}
	
	public Expedicao getExpedicao() {
		return expedicao;
	}
	
	public Boolean getImpressaoOnda() {
		return impressaoOnda;
	}
	
	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}
	
	public void setExpedicao(Expedicao expedicao) {
		this.expedicao = expedicao;
	}
	
	public void setImpressaoOnda(Boolean impressaoOnda) {
		this.impressaoOnda = impressaoOnda;
	}
	
}
