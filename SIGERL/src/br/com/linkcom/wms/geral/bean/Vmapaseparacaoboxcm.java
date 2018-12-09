package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.linkcom.wms.geral.bean.auxiliar.IMapaseparacao;

@Entity
@Table(name="VMAPASEPARACAOBOX_CM")
public class Vmapaseparacaoboxcm implements IMapaseparacao{

	private String chave;
	private Box box;
	private Carregamento carregamento;
	private Carregamentoitem carregamentoitem;
	private Tipooperacao tipooperacao;
	private Cliente cliente;
	private Produto produto;
	private Produto produtoprincipal;
	private Linhaseparacao linhaseparacao;
	private Double peso;
	private Long cubagem;
	private Long qtde;
	private Produtoembalagem produtoembalagem;

	@Id
	public String getChave() {
		return chave;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDBOX")
	public Box getBox() {
		return box;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDCARREGAMENTO")
	public Carregamento getCarregamento() {
		return carregamento;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDCARREGAMENTOITEM")
	public Carregamentoitem getCarregamentoitem() {
		return carregamentoitem;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDTIPOOPERACAO")
	public Tipooperacao getTipooperacao() {
		return tipooperacao;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDCLIENTE")
	public Cliente getCliente() {
		return cliente;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDPRODUTO")
	public Produto getProduto() {
		return produto;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDPRODUTOPRINCIPAL")
	public Produto getProdutoprincipal() {
		return produtoprincipal;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDLINHASEPARACAO")
	public Linhaseparacao getLinhaseparacao() {
		return linhaseparacao;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDPRODUTOEMBALAGEM")
	public Produtoembalagem getProdutoembalagem() {
		return produtoembalagem;
	}
	
	public Double getPeso() {
		return peso;
	}

	public Long getCubagem() {
		return cubagem;
	}

	public Long getQtde() {
		return qtde;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}

	public void setCarregamentoitem(Carregamentoitem carregamentoitem) {
		this.carregamentoitem = carregamentoitem;
	}

	public void setTipooperacao(Tipooperacao tipooperacao) {
		this.tipooperacao = tipooperacao;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setProdutoprincipal(Produto produtoprincipal) {
		this.produtoprincipal = produtoprincipal;
	}

	public void setLinhaseparacao(Linhaseparacao linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public void setCubagem(Long cubagem) {
		this.cubagem = cubagem;
	}

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}

	public void setProdutoembalagem(Produtoembalagem produtoembalagem) {
		this.produtoembalagem = produtoembalagem;
	}
}
