package br.com.linkcom.wms.geral.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;

@Entity
public class Motivoretornoentrega {

	public static Motivoretornoentrega CLIENTE_AUSENTE = new Motivoretornoentrega(1, "Cliente Ausente", false);
	public static Motivoretornoentrega CLIENTE_CANCELOU = new Motivoretornoentrega(2, "Cliente vai cancelar a compra", false);
	public static Motivoretornoentrega MERCADORIA_PORTA = new Motivoretornoentrega(3, "Mercadoria não passa na porta", false);
	public static Motivoretornoentrega OUTROS = new Motivoretornoentrega(4, "Outros", true);
	
	private Integer cdmotivoretornoentrega;
	private String motivo;
	private Boolean exigeobservacao;
	private Statusconfirmacaoentrega statusconfirmacaoentrega;
	
	public Motivoretornoentrega(){
		
	}
	
	public Motivoretornoentrega(Integer cdmotivoretornoentrega, String motivo, Boolean exigeobservacao){
		this.cdmotivoretornoentrega = cdmotivoretornoentrega;
		this.motivo = motivo;
		this.exigeobservacao = exigeobservacao;
	}
	
	//Get's
	@Id
	public Integer getCdmotivoretornoentrega() {
		return cdmotivoretornoentrega;
	}
	@DescriptionProperty
	@Column(name = "descmotivo")
	public String getMotivo() {
		return motivo;
	}
	public Boolean getExigeobservacao() {
		return exigeobservacao;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdstatusconfirmacaoentrega")
	public Statusconfirmacaoentrega getStatusconfirmacaoentrega() {
		return statusconfirmacaoentrega;
	}

	//Set's
	public void setCdmotivoretornoentrega(Integer cdmotivoretornoentrega) {
		this.cdmotivoretornoentrega = cdmotivoretornoentrega;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public void setExigeobservacao(Boolean exigeobservacao) {
		this.exigeobservacao = exigeobservacao;
	}
	public void setStatusconfirmacaoentrega(Statusconfirmacaoentrega statusconfirmacaoentrega) {
		this.statusconfirmacaoentrega = statusconfirmacaoentrega;
	}
	
}
