package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tipomanifestohistorico {
	
	public static Tipomanifestohistorico STATUS = new Tipomanifestohistorico(1,"Status");
	public static Tipomanifestohistorico ACRESCIMO = new Tipomanifestohistorico(2,"Acréscimo");
	public static Tipomanifestohistorico DESCONTO = new Tipomanifestohistorico(3,"Desconto");
	
    private Integer cdtipomanifestohistorico;
    private String nome;
    
    public Tipomanifestohistorico(){
    	
    }
    
    public Tipomanifestohistorico(Integer cdtipomanifestohistorico, String nome){
    	this.cdtipomanifestohistorico = cdtipomanifestohistorico;
    	this.nome = nome;
    }
    
    //Get's
    @Id
	public Integer getCdtipomanifestohistorico() {
		return cdtipomanifestohistorico;
	}
	public String getNome() {
		return nome;
	}
	
	//Set's
	public void setCdtipomanifestohistorico(Integer cdtipomanifestohistorico) {
		this.cdtipomanifestohistorico = cdtipomanifestohistorico;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
