package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;

@Entity
public class Tipovenda {
	
	public static Tipovenda LOJA_FISICA = new Tipovenda(1,"Loja Física");
	public static Tipovenda SITE = new Tipovenda(2,"Site");
	
	private Integer cdtipovenda;
	private String nome;
	
	public Tipovenda(){
	}
	
	public Tipovenda(Integer cdtipovenda, String nome){
		this.cdtipovenda = cdtipovenda;
		this.nome = nome;
	}
	
	//Get's
	@Id
	public Integer getCdtipovenda() {
		return cdtipovenda;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	//Set's
	public void setCdtipovenda(Integer cdtipovenda) {
		this.cdtipovenda = cdtipovenda;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Tipovenda){
			Tipovenda td = (Tipovenda) obj;
			if(td.getCdtipovenda().equals(this.getCdtipovenda())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return cdtipovenda == 2 ? "Sim" : "Não";
	}
}
