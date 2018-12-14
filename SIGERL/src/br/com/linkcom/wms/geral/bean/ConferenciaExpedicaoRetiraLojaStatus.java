package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CONFEXPEDICAORETLOJASTATUS")
public class ConferenciaExpedicaoRetiraLojaStatus {
	private Integer cdConfExpedicaoRetLojaStatus;
	private String nome;
	
	public static ConferenciaExpedicaoRetiraLojaStatus AGUARDANDO_CONFERENCIA = new ConferenciaExpedicaoRetiraLojaStatus(1, "Aguardando Conferência"); 
	public static ConferenciaExpedicaoRetiraLojaStatus CONFERIDO = new ConferenciaExpedicaoRetiraLojaStatus(2, "Conferido"); 
	
	
	// default Constructor
	public ConferenciaExpedicaoRetiraLojaStatus() {	}
	
	public ConferenciaExpedicaoRetiraLojaStatus(Integer cdConfExpedicaoRetLojaStatus, String nome) {
		this.cdConfExpedicaoRetLojaStatus = cdConfExpedicaoRetLojaStatus;
		this.nome = nome;
	}

	@Id
	public Integer getCdConfExpedicaoRetLojaStatus() {
		return cdConfExpedicaoRetLojaStatus;
	}
	
	public void setCdConfExpedicaoRetLojaStatus(Integer cdConfExpedicaoRetLojaStatus) {
		this.cdConfExpedicaoRetLojaStatus = cdConfExpedicaoRetLojaStatus;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof ConferenciaExpedicaoRetiraLojaStatus){
			ConferenciaExpedicaoRetiraLojaStatus ExpedicaoRetiraLojaStatus = (ConferenciaExpedicaoRetiraLojaStatus) obj;
			try {
				if(ExpedicaoRetiraLojaStatus.getCdConfExpedicaoRetLojaStatus().equals(this.getCdConfExpedicaoRetLojaStatus()))
					return true;				
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
}
