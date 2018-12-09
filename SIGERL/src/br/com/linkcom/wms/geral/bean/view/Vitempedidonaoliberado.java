package br.com.linkcom.wms.geral.bean.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.wms.geral.bean.Deposito;


@Entity
@Immutable
public class Vitempedidonaoliberado {

//	private String chave;
	private Integer cdpedidovendaproduto;
	private String numero;
	private String operacao;
	private String codigo;
	private String descricao;
	private Long qtde;
	private Long qtdeconfirmada;
	private Integer cdcarregamento;
	private Deposito deposito;
	private String movitoLiberacao;
	
	/*
	public String getChave() {
		return chave;
	}
	*/
	@Id
	@Column(insertable=false,updatable=false)
	public Integer getCdpedidovendaproduto() {
		return cdpedidovendaproduto;
	}
	@DisplayName("Qtde Confirmada")
	public Long getQtdeconfirmada() {
		return qtdeconfirmada;
	}
	@DisplayName("Nº do pedido")
	public String getNumero() {
		return numero;
	}
	@DisplayName("Qtde Esperada")
	public Long getQtde() {
		return qtde;
	}
	@DisplayName("Código")
	public String getCodigo() {
		return codigo;
	}
	@DisplayName("Descrição")
	public String getDescricao() {
		return descricao;
	}
	@DisplayName("Carregamento")
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	@DisplayName("Tipo")
	public String getOperacao() {
		return operacao;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	@Transient
	public String getMovitoLiberacao() {
		return movitoLiberacao;
	}
	/*
	public void setChave(String identificador) {
		this.chave = identificador;
	}
	*/
	public void setQtdeconfirmada(Long qtdeconfirmada) {
		this.qtdeconfirmada = qtdeconfirmada;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	public void setCdpedidovendaproduto(Integer cdpedidovendaproduto) {
		this.cdpedidovendaproduto = cdpedidovendaproduto;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setMovitoLiberacao(String movitoLiberacao) {
		this.movitoLiberacao = movitoLiberacao;
	}

}
