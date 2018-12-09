package br.com.linkcom.wms.modulo.logistica.controller.report.filtro;

public enum TipoAnalise {

	GIRO("Giro"), 
	VENDA_QTDE("Venda (Qtde)"), 
	VENDA_VALOR("Venda (Valor)"), 
	ESTOQUE_QTDE("Estoque (Qtde)"), 
	ESTOQUE_VALOR("Estoque (Valor)");
	
	private String descricao;
	
	private TipoAnalise(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	@Override
	public String toString() {
		return descricao;
	}
}
