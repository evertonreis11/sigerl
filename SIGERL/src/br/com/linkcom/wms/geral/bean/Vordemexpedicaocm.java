package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import br.com.linkcom.wms.geral.bean.auxiliar.IOrdemexpedicao;


@Entity
@Immutable
@Table(name="VORDEMEXPEDICAO_CM")
public class Vordemexpedicaocm implements IOrdemexpedicao{

	private Integer cdcarregamento;
	private Integer cdcliente;
	private Integer cdtipooperacao;
	private Integer cdordemservico;
	private Integer cddeposito;
	private Produtoembalagem produtoembalagem;
	private Long qtde;
	private String chave;
	private Integer cdcarregamentoitem;
	private Integer cdproduto;
	private Integer cdprodutoprincipal;

	public Integer getCdordemservico() {
		return cdordemservico;
	}
	public Integer getCddeposito() {
		return cddeposito;
	}
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	public Integer getCdcliente() {
		return cdcliente;
	}
	public Integer getCdtipooperacao() {
		return cdtipooperacao;
	}	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDPRODUTOEMBALAGEM")
	public Produtoembalagem getProdutoembalagem() {
		return produtoembalagem;
	}	
	public Long getQtde() {
		return qtde;
	}
	@Id
	public String getChave() {
		return chave;
	}
	public Integer getCdcarregamentoitem() {
		return cdcarregamentoitem;
	}
	public Integer getCdproduto() {
		return cdproduto;
	}
	public Integer getCdprodutoprincipal() {
		return cdprodutoprincipal;
	}
	public void setCdprodutoprincipal(Integer cdprodutoprincipal) {
		this.cdprodutoprincipal = cdprodutoprincipal;
	}
	public void setCdcarregamentoitem(Integer cdcarregamentoitem) {
		this.cdcarregamentoitem = cdcarregamentoitem;
	}
	public void setCdproduto(Integer cdproduto) {
		this.cdproduto = cdproduto;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}
	public void setCdcliente(Integer cdcliente) {
		this.cdcliente = cdcliente;
	}
	public void setCdtipooperacao(Integer cdtipooperacao) {
		this.cdtipooperacao = cdtipooperacao;
	}
	public void setCdordemservico(Integer cdordemservico) {
		this.cdordemservico = cdordemservico;
	}
	public void setCddeposito(Integer cddeposito) {
		this.cddeposito = cddeposito;
	}
	public void setProdutoembalagem(Produtoembalagem produtoembalagem) {
		this.produtoembalagem = produtoembalagem;
	}
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
}
