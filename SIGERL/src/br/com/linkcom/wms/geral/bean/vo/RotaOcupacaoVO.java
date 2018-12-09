package br.com.linkcom.wms.geral.bean.vo;

import java.util.List;

public class RotaOcupacaoVO {

	private List<Double> listaPercentual;
	private String data;
	private List<List<Double>> listaValores;
	
	public String getData() {
		return data;
	}
	public List<Double> getListaPercentual() {
		return listaPercentual;
	}
	public List<List<Double>> getListaValores() {
		return listaValores;
	}
	public void setData(String data) {
		this.data = data;
	}
	public void setListaPercentual(List<Double> listaPercentual) {
		this.listaPercentual = listaPercentual;
	}
	public void setListaValores(List<List<Double>> listaValores) {
		this.listaValores = listaValores;
	}
	
}
