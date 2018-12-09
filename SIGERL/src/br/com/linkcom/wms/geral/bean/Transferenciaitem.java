package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@SequenceGenerator(name = "sq_transferenciaitem", sequenceName = "sq_transferenciaitem")
public class Transferenciaitem {
	// variaveis de instancia
	protected Integer cdtransferenciaitem;
	protected Transferencia transferencia;
	protected Produto produto;
	protected Endereco enderecoorigem;
	protected Endereco enderecodestino;
	protected Long qtde;
	
	//propriedades transientes
	private Ordemservico ordemservico;
	private Produtoembalagem produtoembalagem;
	
	// construtores
	public Transferenciaitem() {

	}
	
	public Transferenciaitem(Integer cd) {
		this.cdtransferenciaitem = cd;
	}
	
	// metodos get e set
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_transferenciaitem")
	@DescriptionProperty
	public Integer getCdtransferenciaitem() {
		return cdtransferenciaitem;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtransferencia")
	public Transferencia getTransferencia() {
		return transferencia;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	public Produto getProduto() {
		return produto;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdenderecoorigem")
	public Endereco getEnderecoorigem() {
		return enderecoorigem;
	}


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdenderecodestino")
	public Endereco getEnderecodestino() {
		return enderecodestino;
	}

	@DisplayName("Quantidade")
	public Long getQtde() {
		return qtde;
	}
	
	@Transient
	public Ordemservico getOrdemservico() {
		return ordemservico;
	}

	public void setCdtransferenciaitem(Integer cdtransferenciaitem) {
		this.cdtransferenciaitem = cdtransferenciaitem;
	}

	public void setTransferencia(Transferencia transferencia) {
		this.transferencia = transferencia;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setEnderecoorigem(Endereco enderecoorigem) {
		this.enderecoorigem = enderecoorigem;
	}

	public void setEnderecodestino(Endereco enderecodestino) {
		this.enderecodestino = enderecodestino;
	}

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	
	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Transferencia) {
			Transferencia t = (Transferencia) obj;
			return t.getCdtransferencia().equals(this.cdtransferenciaitem);
		}
		return super.equals(obj);
	}

	@Transient
	@DisplayName("Embalagem")
	public Produtoembalagem getProdutoembalagem() {
		return produtoembalagem;
	}

	public void setProdutoembalagem(Produtoembalagem produtoembalagem) {
		this.produtoembalagem = produtoembalagem;
	}
}
