package br.com.linkcom.wms.geral.bean;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.MinValue;


@Entity
@SequenceGenerator(name = "sq_ordemservicoprodutoendereco", sequenceName = "sq_ordemservicoprodutoendereco")
public class Ordemservicoprodutoendereco {
	
	protected Integer cdordemservicoprodutoendereco;
	protected Ordemservicoproduto ordemservicoproduto;
	protected Endereco enderecoorigem;
	protected Endereco enderecodestino;
	protected Long qtde;
	protected List<Umareserva> listaUmareserva = new ListSet<Umareserva>(Umareserva.class);
	
	//transientes
	
	protected String etiquetaUMA;
	protected Produtoembalagem produtoEmbalagem;
	
	// -------------------------------------- Construtores ---------------------------------------
	public Ordemservicoprodutoendereco() {

	}
	
	public Ordemservicoprodutoendereco(Integer cd) {
		this.cdordemservicoprodutoendereco = cd;

	}

	public Ordemservicoprodutoendereco(Ordemservicoproduto ordemservicoproduto, Endereco enderecoorigem, Endereco enderecodestino, Long qtde) {
		this.ordemservicoproduto = ordemservicoproduto;
		this.enderecoorigem = enderecoorigem;
		this.enderecodestino = enderecodestino;
		this.qtde = qtde;
	}
	
	// -------------------------------------- Métodos get e set ---------------------------------------
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_ordemservicoprodutoendereco")
	public Integer getCdordemservicoprodutoendereco() {
		return cdordemservicoprodutoendereco;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdordemservicoproduto")
	public Ordemservicoproduto getOrdemservicoproduto() {
		return ordemservicoproduto;
	}
	
	@DisplayName("Origem")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdenderecoorigem")
	public Endereco getEnderecoorigem() {
		return enderecoorigem;
	}
	
	@DisplayName("Destino")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdenderecodestino")
	public Endereco getEnderecodestino() {
		return enderecodestino;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="ordemservicoprodutoendereco")
	public List<Umareserva> getListaUmareserva() {
		return listaUmareserva;
	}
	
	@Transient
	public String getEtiquetaUMA() {
		return etiquetaUMA;
	}
	
	@MaxLength(6)
    @MinValue(0)
	public Long getQtde() {
		return qtde;
	}
		
	public void setCdordemservicoprodutoendereco(Integer cdordemservicoprodutoendereco) {
		this.cdordemservicoprodutoendereco = cdordemservicoprodutoendereco;
	}
		
	public void setOrdemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		this.ordemservicoproduto = ordemservicoproduto;
	}
		
	public void setEnderecoorigem(Endereco enderecoorigem) {
		this.enderecoorigem = enderecoorigem;
	}
	
	public void setEnderecodestino(Endereco enderecodestino) {
		this.enderecodestino = enderecodestino;
	}
	
	public void setListaUmareserva(List<Umareserva> listaUmareserva) {
		this.listaUmareserva = listaUmareserva;
	}
	
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	
	public void setEtiquetaUMA(String etiquetaUMA) {
		this.etiquetaUMA = etiquetaUMA;
	}

	@Transient
	public Produtoembalagem getProdutoEmbalagem() {
		return produtoEmbalagem;
	}

	public void setProdutoEmbalagem(Produtoembalagem produtoEmbalagem) {
		this.produtoEmbalagem = produtoEmbalagem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdordemservicoprodutoendereco == null) ? 0 : cdordemservicoprodutoendereco.hashCode());
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
		final Ordemservicoprodutoendereco other = (Ordemservicoprodutoendereco) obj;

		if (cdordemservicoprodutoendereco == null && other.cdordemservicoprodutoendereco == null)
			return this == other;
		else if (cdordemservicoprodutoendereco == null) {
			if (other.cdordemservicoprodutoendereco != null)
				return false;
		} else if (!cdordemservicoprodutoendereco.equals(other.cdordemservicoprodutoendereco))
			return false;
		return true;
	}
	
}
