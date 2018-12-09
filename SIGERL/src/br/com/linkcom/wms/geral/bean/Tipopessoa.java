package br.com.linkcom.wms.geral.bean;

import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;

public class Tipopessoa {
	
	public static final Tipopessoa CLIENTE = new Tipopessoa("CL");
	
	protected String chavenome;
	protected String descricao;
	
	public Tipopessoa(String chavenome) {
		this.chavenome = chavenome;
	}
	public Tipopessoa() {
	}
	
	
	@Id
	public String getChavenome() {
		return chavenome;
	}
	@DescriptionProperty
	public String getDescricao() {
		return descricao;
	}
	
	public void setChavenome(String chavenome) {
		this.chavenome = chavenome;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Tipopessoa){
			Tipopessoa tp = (Tipopessoa)obj;
			return tp.getChavenome().equals(this.chavenome);
		}
		return super.equals(obj);
	}
}
