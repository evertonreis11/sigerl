package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;

@Entity
@Table(name="deptogerenciadora")
public class Departamentogerenciadora {

	private Integer cdprod;
	private String dsproduto;
	private String dsprodsigla;
	private String flperigoso;
	
	//Get's
	@Id
	public Integer getCdprod() {
		return cdprod;
	}
	@DescriptionProperty
	public String getDsproduto() {
		return dsproduto;
	}
	public String getDsprodsigla() {
		return dsprodsigla;
	}
	public String getFlperigoso() {
		return flperigoso;
	}
	
	//Set's
	public void setCdprod(Integer cdprod) {
		this.cdprod = cdprod;
	}
	public void setDsproduto(String dsproduto) {
		this.dsproduto = dsproduto;
	}
	public void setDsprodsigla(String dsprodsigla) {
		this.dsprodsigla = dsprodsigla;
	}
	public void setFlperigoso(String flperigoso) {
		this.flperigoso = flperigoso;
	}
	
}
