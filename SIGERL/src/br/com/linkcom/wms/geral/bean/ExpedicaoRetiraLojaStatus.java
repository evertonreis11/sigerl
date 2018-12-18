package br.com.linkcom.wms.geral.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EXPEDICAORETLOJASTATUS")
public class ExpedicaoRetiraLojaStatus {
	private Integer cdExpedicaoRetiraLojaStatus;
	private String nome;
	
	public static ExpedicaoRetiraLojaStatus EM_PROCESSO_ENTREGA = new ExpedicaoRetiraLojaStatus(1, "Em Processo de Entrega"); 
	public static ExpedicaoRetiraLojaStatus ENTREGUE = new ExpedicaoRetiraLojaStatus(2, "Entregue"); 
	
	// default Constructor
	public ExpedicaoRetiraLojaStatus() {	}
	
	public ExpedicaoRetiraLojaStatus(Integer cdExpedicaoRetiraLojaStatus, String nome) {
		this.cdExpedicaoRetiraLojaStatus = cdExpedicaoRetiraLojaStatus;
		this.nome = nome;
	}

	@Id
	@Column(name="CDEXPEDICAORETLOJASTATUS")
	public Integer getCdExpedicaoRetiraLojaStatus() {
		return cdExpedicaoRetiraLojaStatus;
	}
	
	public void setCdExpedicaoRetiraLojaStatus(Integer cdExpedicaoRetiraLojaStatus) {
		this.cdExpedicaoRetiraLojaStatus = cdExpedicaoRetiraLojaStatus;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof ExpedicaoRetiraLojaStatus){
			ExpedicaoRetiraLojaStatus ExpedicaoRetiraLojaStatus = (ExpedicaoRetiraLojaStatus) obj;
			try {
				if(ExpedicaoRetiraLojaStatus.getCdExpedicaoRetiraLojaStatus().equals(this.getCdExpedicaoRetiraLojaStatus()))
					return true;				
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
}
