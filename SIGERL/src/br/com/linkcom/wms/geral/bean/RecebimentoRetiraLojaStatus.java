package br.com.linkcom.wms.geral.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="RECEBRETIRALOJASTATUS")
public class RecebimentoRetiraLojaStatus {
	private Integer cdRecebimentoRetiraLojaStatus;
	private String nome;
	
	public static RecebimentoRetiraLojaStatus EM_RECEBIMENTO = new RecebimentoRetiraLojaStatus(1, "Em Recebimento"); 
	public static RecebimentoRetiraLojaStatus CONCLUIDO = new RecebimentoRetiraLojaStatus(2, "Recebimento Concluído"); 
	public static RecebimentoRetiraLojaStatus CANCELADO = new RecebimentoRetiraLojaStatus(3, "Cancelado"); 
	
	// default Constructor
	public RecebimentoRetiraLojaStatus() {	}
	
	public RecebimentoRetiraLojaStatus(Integer cdRecebimentoRetiraLojaStatus, String nome) {
		this.cdRecebimentoRetiraLojaStatus = cdRecebimentoRetiraLojaStatus;
		this.nome = nome;
	}

	@Id
	@Column(name="CDRECEBRETIRALOJASTATUS")
	public Integer getCdRecebimentoRetiraLojaStatus() {
		return cdRecebimentoRetiraLojaStatus;
	}
	
	public void setCdRecebimentoRetiraLojaStatus(Integer cdRecebimentoRetiraLojaStatus) {
		this.cdRecebimentoRetiraLojaStatus = cdRecebimentoRetiraLojaStatus;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof RecebimentoRetiraLojaStatus){
			RecebimentoRetiraLojaStatus recebimentoRetiraLojaStatus = (RecebimentoRetiraLojaStatus) obj;
			try {
				if(recebimentoRetiraLojaStatus.getCdRecebimentoRetiraLojaStatus().equals(this.getCdRecebimentoRetiraLojaStatus()))
					return true;				
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
}
