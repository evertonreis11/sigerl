package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.linkcom.wms.geral.bean.auxiliar.IMapaseparacao;

@Entity
@Table(name="VMAPASEPARACAO_CM")
public class Vmapaseparacaocm implements IMapaseparacao{
	
	protected Tipooperacao tipooperacao;
	protected Carregamento carregamento;	
	protected Carregamentoitem carregamentoitem;
	protected Cliente cliente;
	protected Produto produto;
	protected Produto produtoprincipal;
	protected Integer ordem;
	protected Linhaseparacao linhaseparacao;
	protected Produtoembalagem produtoembalagem;
	protected Double peso;
	protected Long cubagem;
	protected Long qtde;	
	protected String chave;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDCARREGAMENTO")
	public Carregamento getCarregamento() {
		return carregamento;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDTIPOOPERACAO")
	public Tipooperacao getTipooperacao() {
		return tipooperacao;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdcarregamentoitem")
	public Carregamentoitem getCarregamentoitem() {
		return carregamentoitem;
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
	public Integer getOrdem() {
		return ordem;
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
	@Id
	public String getChave() {
		return chave;
	}
	
	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}
	public void setTipooperacao(Tipooperacao tipooperacao) {
		this.tipooperacao = tipooperacao;
	}
	public void setCarregamentoitem(Carregamentoitem carregamentoitem) {
		this.carregamentoitem = carregamentoitem;
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
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	public void setLinhaseparacao(Linhaseparacao linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}
	public void setProdutoembalagem(Produtoembalagem produtoembalagem) {
		this.produtoembalagem = produtoembalagem;
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
	public void setChave(String chave) {
		this.chave = chave;
	}
}
