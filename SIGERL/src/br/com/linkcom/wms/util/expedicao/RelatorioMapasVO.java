package br.com.linkcom.wms.util.expedicao;

import java.util.ArrayList;
import java.util.List;

public class RelatorioMapasVO {
		
	private Integer mapaSeparacao;
	private String dataHora;
	private String paleteLinhaSeparacao;
	private String box;
	private Integer carregamento;
	private List<MapaDados> listaDados = new ArrayList<MapaDados>();
	private Integer totalItens;
	private Double volumeTotal = 0.0;
	private Double pesoTotal = 0.0;
	private String tipoPedido;
	private String filialEntrega;
	
	
	public Integer getMapaSeparacao() {
		return mapaSeparacao;
	}
	public String getDataHora() {
		return dataHora;
	}
	public String getPaleteLinhaSeparacao() {
		return paleteLinhaSeparacao;
	}
	public String getBox() {
		return box;
	}
	public Integer getCarregamento() {
		return carregamento;
	}
	public List<MapaDados> getListaDados() {
		return listaDados;
	}
	public Double getPesoTotal() {
		return pesoTotal;
	}
	public Integer getTotalItens() {
		return totalItens;
	}
	public Double getVolumeTotal() {
		return volumeTotal;
	}
	public String getTipoPedido() {
		return tipoPedido;
	}
	public String getFilialEntrega() {
		return filialEntrega;
	}
	public void setMapaSeparacao(Integer mapaSeparacao) {
		this.mapaSeparacao = mapaSeparacao;
	}
	public void setDataHora(String dataHora) {
		this.dataHora = dataHora;
	}
	public void setPaleteLinhaSeparacao(String paleteLinhaSeparacao) {
		this.paleteLinhaSeparacao = paleteLinhaSeparacao;
	}
	public void setBox(String box) {
		this.box = box;
	}
	public void setCarregamento(Integer carregamento) {
		this.carregamento = carregamento;
	}
	public void setListaDados(List<MapaDados> listaDados) {
		this.listaDados = listaDados;
	}
	public void setTotalItens(Integer totalItens) {
		this.totalItens = totalItens;
	}
	public void setPesoTotal(Double pesoTotal) {
		this.pesoTotal = pesoTotal;
	}
	public void setVolumeTotal(Double volumeTotal) {
		this.volumeTotal = volumeTotal;
	}
	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido;
	}
	public void setFilialEntrega(String filialEntrega) {
		this.filialEntrega = filialEntrega;
	}

}
