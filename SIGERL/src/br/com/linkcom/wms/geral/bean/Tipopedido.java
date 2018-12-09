package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tipopedido{
	
	private Integer cdtipopedido;
	private String nome;
	private Boolean usaminicd;
	
	@Id
	public Integer getCdtipopedido() {
		return cdtipopedido;
	}
	public String getNome() {
		return nome;
	}
	public Boolean getUsaminicd() {
		return usaminicd;
	}
	
	public void setCdtipopedido(Integer cdtipopedido) {
		this.cdtipopedido = cdtipopedido;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setUsaminicd(Boolean usaminicd) {
		this.usaminicd = usaminicd;
	}
}
