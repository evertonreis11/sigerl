package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@SequenceGenerator(name = "SQ_CONFERENCIAORDEMRECEBIMENTO", sequenceName = "SQ_CONFERENCIAORDEMRECEBIMENTO")
public class Conferenciaordemrecebimento {
	private Long cdconferenciaordemrecebimento;
	private Ordemprodutohistorico ordemprodutohistorico;
	private Long qtde;
	private Recebimento recebimento;
	private Boolean iscoletaavaria;
	private Produtoembalagem produtoembalagem;
	private Boolean conferenciafinalizada;
	
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="SQ_CONFERENCIAORDEMRECEBIMENTO")
	public Long getCdconferenciaordemrecebimento() {
		return cdconferenciaordemrecebimento;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdordemprodutohistorico")
	public Ordemprodutohistorico getOrdemprodutohistorico() {
		return ordemprodutohistorico;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdrecebimento")
	public Recebimento getRecebimento() {
		return recebimento;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdprodutoembalagem")
	public Produtoembalagem getProdutoembalagem() {
		return produtoembalagem;
	}
	
	public Long getQtde() {
		return qtde;
	}
	
	public Boolean getIscoletaavaria() {
		return iscoletaavaria;
	}
	
	public Boolean getConferenciafinalizada() {
		return conferenciafinalizada;
	}
	
	public void setCdconferenciaordemrecebimento(Long cdconferenciaordemrecebimento) {
		this.cdconferenciaordemrecebimento = cdconferenciaordemrecebimento;
	}
	
	public void setOrdemprodutohistorico(Ordemprodutohistorico ordemprodutohistorico) {
		this.ordemprodutohistorico = ordemprodutohistorico;
	}
	
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}

	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}

	public void setIscoletaavaria(Boolean iscoletaavaria) {
		this.iscoletaavaria = iscoletaavaria;
	}

	public void setProdutoembalagem(Produtoembalagem produtoembalagem) {
		this.produtoembalagem = produtoembalagem;
	}

	public void setConferenciafinalizada(Boolean conferenciafinalizada) {
		this.conferenciafinalizada = conferenciafinalizada;
	}
	
}
