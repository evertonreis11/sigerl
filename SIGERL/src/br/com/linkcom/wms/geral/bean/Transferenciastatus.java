package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
public class Transferenciastatus {
	
	// Constantes
	public static final Transferenciastatus EM_ABERTO = new Transferenciastatus(1);
	public static final Transferenciastatus EM_EXECUCAO = new Transferenciastatus(2);
	public static final Transferenciastatus FINALIZADO = new Transferenciastatus(3);
	public static final Transferenciastatus CANCELADO = new Transferenciastatus(4);
	
	// Variáveis de instância
	protected Integer cdtransferenciastatus;
	protected String nome;
	
	// Construtores
	public Transferenciastatus() {

	}
	
	public Transferenciastatus(Integer cd) {
		this.cdtransferenciastatus = cd;		
	}
	
	// Metodos get e set	
	@Id
	@DisplayName("Id")
	public Integer getCdtransferenciastatus() {
		return cdtransferenciastatus;
	}
	
	@DisplayName("Nome")
	@DescriptionProperty
	public String getNome() {
		return nome;
	}

	public void setCdtransferenciastatus(Integer cdtransferenciastatus) {
		this.cdtransferenciastatus = cdtransferenciastatus;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	// Metodo equals
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Transferenciastatus) {
			Transferenciastatus transferenciastatus = (Transferenciastatus) obj;
			
			return transferenciastatus.getCdtransferenciastatus().equals(this.cdtransferenciastatus);			
		}
		
		return super.equals(obj);
	}
}