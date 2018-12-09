package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;

@Entity
public class Tipotabelafrete {

	public static Tipotabelafrete ROTAPRACA = new Tipotabelafrete(1,"Rota / Praça");
	public static Tipotabelafrete TIPOVEICULO = new Tipotabelafrete(2,"Tipo de Veículo");
	public static Tipotabelafrete TRANSPORTADOR = new Tipotabelafrete(3,"Transportador");
	public static Tipotabelafrete VALORFECHADO = new Tipotabelafrete(4,"Valor Fechado");
	
	private Integer cdtipotabelafrete;
	private String nome;
	
	public Tipotabelafrete(){}
	
	public Tipotabelafrete(Integer cdtipotabelafrete, String nome){
		this.cdtipotabelafrete = cdtipotabelafrete;
		this.nome = nome;
	}
	
	//Get's
	@Id
	public Integer getCdtipotabelafrete() {
		return cdtipotabelafrete;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	
	//Set's
	public void setCdtipotabelafrete(Integer cdtipotabelafrete) {
		this.cdtipotabelafrete = cdtipotabelafrete;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
