package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
public class Tiporota {
	public static final Tiporota SEMANAL = new Tiporota(1);
	public static final Tiporota TEMPORAL = new Tiporota(2);
	
	// variaveis de instancia
	protected Integer cdtiporota;
	protected String descricao;
	
	// construtores
	public Tiporota(int cd, String descricao) {
		this.cdtiporota = cd;
		this.descricao = descricao;
	}

	public Tiporota(int cd) {
		this.cdtiporota = cd;
	}
	
	public Tiporota() {
	}

	@Id
	@DisplayName("Id")
	public Integer getCdtiporota() {
		return cdtiporota;
	}
	
	@MaxLength(30)
	@DescriptionProperty
	public String getDescricao() {
		return descricao;
	}


	public void setCdtiporota(Integer cdtiporota) {
		this.cdtiporota = cdtiporota;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Tiporota){
			Tiporota tr = (Tiporota) obj;
			try {
				if(tr.getCdtiporota().equals(this.getCdtiporota()))
					return true;				
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
}