package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;

@Entity
public class Tipodeposito {
	
	public static Tipodeposito CD = new Tipodeposito(1,"CD");
	public static Tipodeposito CDA = new Tipodeposito(2,"CDA");
	public static Tipodeposito CD_BASE = new Tipodeposito(3,"CD BASE");
	public static Tipodeposito DAT = new Tipodeposito(4,"DAT");
	public static Tipodeposito CENTRAL_MONTAGEM = new Tipodeposito(5,"CENTRAL MONTAGEM");
	public static Tipodeposito LOJA = new Tipodeposito(6,"LOJA");
	
	private Integer cdtipodeposito;
	private String nome;
	
	public Tipodeposito(){
		
	}
	
	public Tipodeposito(Integer cdtipodeposito, String nome){
		this.cdtipodeposito = cdtipodeposito;
		this.nome = nome;
	}
	
	//Get's
	@Id
	public Integer getCdtipodeposito() {
		return cdtipodeposito;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	//Set's
	public void setCdtipodeposito(Integer cdtipodeposito) {
		this.cdtipodeposito = cdtipodeposito;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Tipodeposito){
			Tipodeposito td = (Tipodeposito) obj;
			if(td.getCdtipodeposito().equals(this.getCdtipodeposito())){
				return true;
			}
		}
		return false;
	}
	
}
