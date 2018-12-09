package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;

@Entity
public class Entregatipo {

	public static Entregatipo ENTREGA_CLIENTE = new Entregatipo(1,"ENTREGA PARA CLIENTE");
	public static Entregatipo ENTREGA_CDA_BASE = new Entregatipo(1,"ENTREGA PARA CDA BASE");
	public static Entregatipo ENTREGA_LOJA_BASE = new Entregatipo(1,"ENTREGA PARA LOJA BASE");
	public static Entregatipo ENTREGA_TRANSFERENCIA = new Entregatipo(1,"ENTREGA DE TRANSFERENCIA");
	
	private Integer cdentregatipo; 
	private String nome;
	
	public Entregatipo(){
		
	}
	
	public Entregatipo(Integer cdentregatipo, String nome){
		this.cdentregatipo = cdentregatipo;
		this.nome = nome;
	}
	
	//Get's
	@Id
	public Integer getCdentregatipo() {
		return cdentregatipo;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	//Set's
	public void setCdentregatipo(Integer cdentregatipo) {
		this.cdentregatipo = cdentregatipo;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Entregatipo){
			Entregatipo et = (Entregatipo) obj;
			if(et.getCdentregatipo().equals(this.getCdentregatipo())){
				return true;
			}
		}
		return false;
	}
	
}
