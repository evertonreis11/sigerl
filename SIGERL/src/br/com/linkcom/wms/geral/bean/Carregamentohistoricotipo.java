package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Carregamentohistoricotipo {
	
	public static Carregamentohistoricotipo STATUS = new Carregamentohistoricotipo(1,"Status");
	public static Carregamentohistoricotipo PRE_VALIDACAO = new Carregamentohistoricotipo(2,"Pré-Validação de Carga");
	
	private Integer cdcarregamentohistoricotipo;
	private String nome;
	
	public Carregamentohistoricotipo(){
	}

	public Carregamentohistoricotipo(Integer cdcarregamentohistoricotipo, String nome){
		this.cdcarregamentohistoricotipo = cdcarregamentohistoricotipo;
		this.nome = nome;
	}
	
	@Id
	public Integer getCdcarregamentohistoricotipo() {
		return cdcarregamentohistoricotipo;
	}
	public String getNome() {
		return nome;
	}
	
	public void setCdcarregamentohistoricotipo(Integer cdcarregamentohistoricotipo) {
		this.cdcarregamentohistoricotipo = cdcarregamentohistoricotipo;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
