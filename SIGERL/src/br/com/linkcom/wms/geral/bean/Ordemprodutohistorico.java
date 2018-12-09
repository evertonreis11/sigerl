package br.com.linkcom.wms.geral.bean;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@SequenceGenerator(name="sq_ordemprodutohistorico",sequenceName="sq_ordemprodutohistorico")
public class Ordemprodutohistorico {
	
	// variáveis de instância
	protected Integer cdordemprodutohistorico;
	protected Ordemservico ordemservico;
	protected Ordemservicoproduto ordemservicoproduto;
	protected Long qtde;
	protected Long qtdeavaria;
	protected Long qtdefracionada;
	protected Long qtdefalta;
	protected Produtoembalagem produtoembalagem;
	
	//Transientes
	protected Long qtdeColetada;
	protected Integer codigoBarrasIndex;//Indica a posição do codigo de barras lido na lista em que a ordem foi encontrada. Essa variavel é usada pelo coletor
	protected Notafiscalentradaproduto notafiscalentradaproduto;
	protected List<Produtoembalagem> listaEmbalagens;
	protected String codigoBarrasConferencia;
	
	// Construtores
	public Ordemprodutohistorico() {

	}
	
	public Ordemprodutohistorico(Integer cd) {
		this.cdordemprodutohistorico = cd;
	}
	
	// Métodos get e set
	@Id
	@DisplayName("Id")
	@GeneratedValue(generator="sq_ordemprodutohistorico",strategy=GenerationType.AUTO)
	public Integer getCdordemprodutohistorico() {
		return cdordemprodutohistorico;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdordemservico")
	@DisplayName("Ordem de serviço")
	public Ordemservico getOrdemservico() {
		return ordemservico;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdordemservicoproduto")
	@DisplayName("Ordem de serviço do produto")
	public Ordemservicoproduto getOrdemservicoproduto() {
		return ordemservicoproduto;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdprodutoembalagem")
	@DisplayName("Embalagem")
	public Produtoembalagem getProdutoembalagem() {
		return produtoembalagem;
	}
	
	@DisplayName("Quantidade")
	public Long getQtde() {
		return qtde;
	}
	@DisplayName("Quantidade avariada")
	public Long getQtdeavaria() {
		return qtdeavaria;
	}
	
	@DisplayName("Quantidade fracionada")
	public Long getQtdefracionada() {
		return qtdefracionada;
	}

	@DisplayName("Quantidade faltante")
	public Long getQtdefalta() {
		return qtdefalta;
	}

	@Transient
	@Deprecated
	public Long getQtdeColetada() {
		return qtdeColetada;
	}

	@Transient
	public Integer getCodigoBarrasIndex() {
		return codigoBarrasIndex;
	}
	
	@Transient
	public Notafiscalentradaproduto getNotafiscalentradaproduto() {
		return notafiscalentradaproduto;
	}
	
	@Transient
	public List<Produtoembalagem> getListaEmbalagens() {
		return listaEmbalagens;
	}
	
	@Transient
	public String getCodigoBarrasConferencia() {
		return codigoBarrasConferencia;
	}

	public void setCodigoBarrasConferencia(String codigoBarrasConferencia) {
		this.codigoBarrasConferencia = codigoBarrasConferencia;
	}
	
	public void setListaEmbalagens(List<Produtoembalagem> listaEmbalagens) {
		this.listaEmbalagens = listaEmbalagens;
	}

	public void setNotafiscalentradaproduto(Notafiscalentradaproduto notafiscalentradaproduto) {
		this.notafiscalentradaproduto = notafiscalentradaproduto;
	}
	
	public void setCdordemprodutohistorico(Integer cdordemprodutohistorico) {
		this.cdordemprodutohistorico = cdordemprodutohistorico;
	}
	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}
	public void setOrdemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		this.ordemservicoproduto = ordemservicoproduto;
	}
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	public void setQtdeavaria(Long qtdeavaria) {
		this.qtdeavaria = qtdeavaria;
	}
	
	public void setQtdefracionada(Long qtdefracionada) {
		this.qtdefracionada = qtdefracionada;
	}

	public void setQtdefalta(Long qtdefalta) {
		this.qtdefalta = qtdefalta;
	}

	@Deprecated
	public void setQtdeColetada(Long qtdeColetada) {
		this.qtdeColetada = qtdeColetada;
	}
	public void setCodigoBarrasIndex(Integer codigoBarrasIndex) {
		this.codigoBarrasIndex = codigoBarrasIndex;
	}
	
	public void setProdutoembalagem(Produtoembalagem produtoembalagem) {
		this.produtoembalagem = produtoembalagem;
	}

	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Ordemprodutohistorico) {
			Ordemprodutohistorico oph = (Ordemprodutohistorico) obj;
			
			if(oph.getCdordemprodutohistorico() == null && this.cdordemprodutohistorico == null)  
				return obj.hashCode() == this.hashCode();								
			return oph.getCdordemprodutohistorico().equals(this.getCdordemprodutohistorico());
		}
		
		return super.equals(obj);
	}


	
}
