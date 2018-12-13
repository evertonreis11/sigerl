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
@SequenceGenerator(name = "sq_estoqueprodutoloja", sequenceName = "sq_estoqueprodutoloja")
public class EstoqueProdutoLoja {
	
	private Integer cdEstoqueProdutoLoja;
	
	private Produto produto;
	
	private Integer qtde;
	
	private TipoEstoque tipoEstoque;
	
	private Deposito deposito;
	
	private Timestamp dtInclusao;
	
	private Timestamp dtAlteracao;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_estoqueprodutoloja")
	public Integer getCdEstoqueProdutoLoja() {
		return cdEstoqueProdutoLoja;
	}

	public void setCdEstoqueProdutoLoja(Integer cdEstoqueProdutoLoja) {
		this.cdEstoqueProdutoLoja = cdEstoqueProdutoLoja;
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
	@JoinColumn(name="cdtipoestoque")
	public TipoEstoque getTipoEstoque() {
		return tipoEstoque;
	}

	public void setTipoEstoque(TipoEstoque tipoEstoque) {
		this.tipoEstoque = tipoEstoque;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public Timestamp getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(Timestamp dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	public Timestamp getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDtAlteracao(Timestamp dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}
	
}
