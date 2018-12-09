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
@SequenceGenerator(name="sq_ordemprodutoligacao",sequenceName="sq_ordemprodutoligacao")
public class Ordemprodutoligacao {
	
	protected Integer cdordemprodutoligacao;
	protected Ordemservico ordemservico;
	protected Ordemservicoproduto ordemservicoproduto;
	
	// construtores
	public Ordemprodutoligacao() {

	}
	
	public Ordemprodutoligacao(Integer cd) {
		this.cdordemprodutoligacao = cd;
	}
	
	public Ordemprodutoligacao(Ordemservicoproduto ordemservicoproduto) {
		this.ordemservicoproduto = ordemservicoproduto;
	}
	
	// métodos get e set
	@Id
	@DisplayName("id")
	@GeneratedValue(generator="sq_ordemprodutoligacao",strategy=GenerationType.AUTO)
	public Integer getCdordemprodutoligacao() {
		return cdordemprodutoligacao;
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
	
	public void setCdordemprodutoligacao(Integer cdordemprodutoligacao) {
		this.cdordemprodutoligacao = cdordemprodutoligacao;
	}
	
	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}
	
	public void setOrdemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		this.ordemservicoproduto = ordemservicoproduto;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cdordemprodutoligacao == null) ? 0 : cdordemprodutoligacao
						.hashCode());
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
		final Ordemprodutoligacao other = (Ordemprodutoligacao) obj;
		
		if (cdordemprodutoligacao == null && other.cdordemprodutoligacao == null)
			return this == other;
		else if (cdordemprodutoligacao == null) {
			if (other.cdordemprodutoligacao != null)
				return false;
		} else if (!cdordemprodutoligacao.equals(other.cdordemprodutoligacao))
			return false;
		return true;
	}
}
