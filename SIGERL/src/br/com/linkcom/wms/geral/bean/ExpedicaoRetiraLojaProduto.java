package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

// TODO: Auto-generated Javadoc
/**
 * The Class RecebimentoRetiraLojaProduto.
 */
@Entity
@Table(name="EXPRETIRALOJAPRODUTO")
@SequenceGenerator(name = "sq_expretiralojaproduto", sequenceName = "sq_expretiralojaproduto")
public class ExpedicaoRetiraLojaProduto {
	
	private Integer cdExpedicaoRetiraLojaProduto;

	private Produto produto;
	
	private Integer qtde;
	
	private Notafiscalsaidaproduto notaFiscalSaidaProduto;
	
	private ExpedicaoRetiraLoja expedicaoRetiraLoja;
	
	private ConferenciaExpedicaoRetiraLojaStatus conferenciaExpedicaoRetiraLojaStatus;
	
	
	// Transients
	private String codigoBarrasConferencia;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_expretiralojaproduto")
	public Integer getCdExpedicaoRetiraLojaProduto() {
		return cdExpedicaoRetiraLojaProduto;
	}

	public void setCdExpedicaoRetiraLojaProduto(Integer cdExpedicaoRetiraLojaProduto) {
		this.cdExpedicaoRetiraLojaProduto = cdExpedicaoRetiraLojaProduto;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Integer getQtde() {
		return qtde;
	}

	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdnotafiscalsaidaproduto")
	public Notafiscalsaidaproduto getNotaFiscalSaidaProduto() {
		return notaFiscalSaidaProduto;
	}

	public void setNotaFiscalSaidaProduto(Notafiscalsaidaproduto notaFiscalSaidaProduto) {
		this.notaFiscalSaidaProduto = notaFiscalSaidaProduto;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdExpedicaoRetiraLoja")
	public ExpedicaoRetiraLoja getExpedicaoRetiraLoja() {
		return expedicaoRetiraLoja;
	}

	public void setExpedicaoRetiraLoja(ExpedicaoRetiraLoja expedicaoRetiraLoja) {
		this.expedicaoRetiraLoja = expedicaoRetiraLoja;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdConfExpedicaoRetLojaStatus")
	public ConferenciaExpedicaoRetiraLojaStatus getConferenciaExpedicaoRetiraLojaStatus() {
		return conferenciaExpedicaoRetiraLojaStatus;
	}

	public void setConferenciaExpedicaoRetiraLojaStatus(
			ConferenciaExpedicaoRetiraLojaStatus conferenciaExpedicaoRetiraLojaStatus) {
		this.conferenciaExpedicaoRetiraLojaStatus = conferenciaExpedicaoRetiraLojaStatus;
	}
	
	@Transient
	public String getCodigoBarrasConferencia() {
		return codigoBarrasConferencia;
	}

	public void setCodigoBarrasConferencia(String codigoBarrasConferencia) {
		this.codigoBarrasConferencia = codigoBarrasConferencia;
	}
	
}
