package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name="sq_pedidopontocontrole", sequenceName="sq_pedidopontocontrole")
public class PedidoPontoControle {
	
	private Integer cdPedidoPontoControle;
	
	private PontoControle pontoControle;
	
	private Timestamp dtInclusao;
	
	private Long pedidoErp;
	
	private String chaveNfe;
	
	private String observacao;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pedidopontocontrole")
	public Integer getCdPedidoPontoControle() {
		return cdPedidoPontoControle;
	}

	public void setCdPedidoPontoControle(Integer cdPedidoPontoControle) {
		this.cdPedidoPontoControle = cdPedidoPontoControle;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cdpontodecontrole")
	public PontoControle getPontoControle() {
		return pontoControle;
	}

	public void setPontoControle(PontoControle pontoControle) {
		this.pontoControle = pontoControle;
	}

	public Timestamp getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(Timestamp dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	public Long getPedidoErp() {
		return pedidoErp;
	}

	public void setPedidoErp(Long pedidoErp) {
		this.pedidoErp = pedidoErp;
	}

	public String getChaveNfe() {
		return chaveNfe;
	}

	public void setChaveNfe(String chaveNfe) {
		this.chaveNfe = chaveNfe;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
}
