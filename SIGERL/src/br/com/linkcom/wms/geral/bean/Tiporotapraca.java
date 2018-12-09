package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;

@Entity
public class Tiporotapraca {
	
	public static Tiporotapraca TRANSFERENCIA = new Tiporotapraca(1,"Transferência");
	public static Tiporotapraca CLIENTE = new Tiporotapraca(2,"Cliente");
	
	private Integer cdtiporotapraca;
	private String nome;
	
	public Tiporotapraca(){
	}
	
	public Tiporotapraca(Integer cdtiporotapraca, String nome){
		this.cdtiporotapraca = cdtiporotapraca;
		this.nome = nome;
	}
	
	//Get's
	@Id
	public Integer getCdtiporotapraca() {
		return cdtiporotapraca;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	//Set's
	public void setCdtiporotapraca(Integer cdtiporotapraca) {
		this.cdtiporotapraca = cdtiporotapraca;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Tiporotapraca){
			Tiporotapraca td = (Tiporotapraca) obj;
			if(td.getCdtiporotapraca().equals(this.getCdtiporotapraca())){
				return true;
			}
		}
		return false;
	}
}
