package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
public class Ordemstatus {
	
	//Constantes
	public static final Ordemstatus EM_ABERTO = new Ordemstatus(1);
	public static final Ordemstatus EM_EXECUCAO= new Ordemstatus(2);
	public static final Ordemstatus FINALIZADO_DIVERGENCIA = new Ordemstatus(3);
	public static final Ordemstatus FINALIZADO_SUCESSO = new Ordemstatus(4);
	public static final Ordemstatus CANCELADO = new Ordemstatus(5);
	public static final Ordemstatus AGUARDANDO_CONFIRMACAO = new Ordemstatus(6);
	public static final Ordemstatus FINALIZADO_MANUALMENTE = new Ordemstatus(7);
	
	protected Integer cdordemstatus;
	protected String nome;
	
	// ------------------------------ Construtor -------------------------------
	public Ordemstatus() {

	}
	
	public Ordemstatus(Integer cdordemstatus) {
		this.cdordemstatus = cdordemstatus;
		
	}
	
	// ------------------------- Metodos get e set -------------------------
	@Id
	@DisplayName("Id")
	public Integer getCdordemstatus() {
		return cdordemstatus;
	}
	
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	
	public void setCdordemstatus(Integer cdordemstatus) {
		this.cdordemstatus = cdordemstatus;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdordemstatus == null) ? 0 : cdordemstatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Ordemstatus other = (Ordemstatus) obj;
		if (cdordemstatus == null) {
			if (other.cdordemstatus != null)
				return false;
		} else if (!cdordemstatus.equals(other.cdordemstatus))
			return false;
		return true;
	}
	
	
}
