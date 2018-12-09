package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;

@Entity
public class Statusconfirmacaoentrega {
	
	public static Statusconfirmacaoentrega ENTREGA_EM_ANDAMENTO = new Statusconfirmacaoentrega(1, "ENTREGA EM ANDAMENTO", false, false);
	public static Statusconfirmacaoentrega ENTREGA_CONFIRMADA = new Statusconfirmacaoentrega(2, "ENTREGA CONFIRMADA", true, false);
	public static Statusconfirmacaoentrega RETORNO_DE_ENTREGA = new Statusconfirmacaoentrega(3, "RETORNO DE ENTREGA", true, true);
	public static Statusconfirmacaoentrega EXCLUSAO = new Statusconfirmacaoentrega(4, "EXCLUSÃO", true , true);
	public static Statusconfirmacaoentrega ENTREGA_SEM_PAGAMENTO = new Statusconfirmacaoentrega(5, "ENTREGA SEM PAGAMENTO", true , true);
	
	
	private Integer cdstatusconfirmacaoentrega;
	private String nome;
	private Boolean finanlizador;
	private Boolean exigemotivo;
	
	public Statusconfirmacaoentrega(){
	}
	
	public Statusconfirmacaoentrega(Integer cdstatusconfirmacaoentrega, String nome, Boolean finanlizador, Boolean exigemotivo){
		this.cdstatusconfirmacaoentrega = cdstatusconfirmacaoentrega;
		this.nome = nome;
		this.finanlizador = finanlizador;
		this.exigemotivo = exigemotivo;
	}
	
	//Get's
	@Id
	public Integer getCdstatusconfirmacaoentrega() {
		return cdstatusconfirmacaoentrega;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	public Boolean getFinanlizador() {
		return finanlizador;
	}
	public Boolean getExigemotivo() {
		return exigemotivo;
	}

	
	//Set's
	public void setCdstatusconfirmacaoentrega(Integer cdstatusconfirmacaoentrega) {
		this.cdstatusconfirmacaoentrega = cdstatusconfirmacaoentrega;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setFinanlizador(Boolean finanlizador) {
		this.finanlizador = finanlizador;
	}
	public void setExigemotivo(Boolean exigemotivo) {
		this.exigemotivo = exigemotivo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Statusconfirmacaoentrega){
			Statusconfirmacaoentrega sce = (Statusconfirmacaoentrega) obj;
			if(sce.getCdstatusconfirmacaoentrega().equals(this.getCdstatusconfirmacaoentrega())){
				return true;
			}
		}
		return false;
	}
	
}
