package br.com.linkcom.wms.geral.bean.vo;

import java.util.List;

import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Box;

public class ExpedicaoVO {

	private List<Box> listaBoxes = new ListSet<Box>(Box.class);
	private String cdcarregamentos;
	private Boolean showLogPreValidacao = Boolean.FALSE;
	
	public List<Box> getListaBoxes() {
		return listaBoxes;
	}
	
	public String getCdcarregamentos() {
		return cdcarregamentos;
	}

	public Boolean getShowLogPreValidacao() {
		return showLogPreValidacao;
	}

	public void setShowLogPreValidacao(Boolean showLogPreValidacao) {
		this.showLogPreValidacao = showLogPreValidacao;
	}

	public void setCdcarregamentos(String cdcarregamentos) {
		this.cdcarregamentos = cdcarregamentos;
	}

	public void setListaBoxes(List<Box> listaBoxes) {
		this.listaBoxes = listaBoxes;
	}
	
}
