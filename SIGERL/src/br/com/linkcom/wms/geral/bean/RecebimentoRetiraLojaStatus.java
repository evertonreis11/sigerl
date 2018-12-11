package br.com.linkcom.wms.geral.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RecebimentoRetiraLojaStatus {
	private Integer cdRecebimentoRetiraLojaStatus;
	private String nome;
	
	public static RecebimentoRetiraLojaStatus EM_CONFERENCIA = new RecebimentoRetiraLojaStatus(1, "Em Conferência"); 
	public static RecebimentoRetiraLojaStatus CONCLUIDO = new RecebimentoRetiraLojaStatus(2, "Concluído"); 
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

	
	
}
