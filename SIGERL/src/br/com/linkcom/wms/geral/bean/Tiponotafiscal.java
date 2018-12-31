package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;

@Entity
public class Tiponotafiscal {

	public static Tiponotafiscal NFE = new Tiponotafiscal(1,"NF-E");
	public static Tiponotafiscal NFCE = new Tiponotafiscal(2,"NF-CE");
	public static Tiponotafiscal CUPOM_FISCAL = new Tiponotafiscal(3,"CF");
	
	private Integer cdtiponotafiscal;
	private String nome;
	
	public Tiponotafiscal(){
	}
	
	public Tiponotafiscal(Integer cdtiponotafiscal, String nome){
		this.cdtiponotafiscal = cdtiponotafiscal;
		this.nome = nome;
	}
	
	//Get's
	@Id
	public Integer getCdtiponotafiscal() {
		return cdtiponotafiscal;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	//Set's
	public void setCdtiponotafiscal(Integer cdtiponotafiscal) {
		this.cdtiponotafiscal = cdtiponotafiscal;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
