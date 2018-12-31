package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;

@Entity
public class Tipoentrega {

	public static Tipoentrega TRANSFERENCIA = new Tipoentrega(1,"Transferência");
	public static Tipoentrega ENTREGA_CLIENTE = new Tipoentrega(2,"Entrega Cliente");
	public static Tipoentrega AGRUPAMENTO = new Tipoentrega(3,"Agrupamento");
	public static Tipoentrega CONSOLIDACAO = new Tipoentrega(4,"Consolidação");
	
	private Integer cdtipoentrega;
	private String nome;
	
	public Tipoentrega(){}
	
	public Tipoentrega(Integer cdtipoentrega, String nome){
		this.cdtipoentrega = cdtipoentrega;
		this.nome = nome;
	}
	
	@Id	
	public Integer getCdtipoentrega() {
		return cdtipoentrega;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	

	public void setCdtipoentrega(Integer cdtipoentrega) {
		this.cdtipoentrega = cdtipoentrega;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Tipoentrega){
			Tipoentrega te = (Tipoentrega) obj;
			try {
				if(te.getCdtipoentrega().equals(this.getCdtipoentrega()))
					return true;				
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
}