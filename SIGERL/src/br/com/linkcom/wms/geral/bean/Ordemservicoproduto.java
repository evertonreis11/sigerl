package br.com.linkcom.wms.geral.bean;

import java.util.List;
import java.util.Set;

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

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_ordemservicoproduto", sequenceName = "sq_ordemservicoproduto")
public class Ordemservicoproduto {

	protected Integer cdordemservicoproduto;
	protected Produto produto;
	protected Long qtdeesperada;
	protected Ordemprodutostatus ordemprodutostatus;
	protected Tipopalete tipopalete;
	protected Set<Etiquetaexpedicao> listaEtiquetaexpedicao = new ListSet<Etiquetaexpedicao>(Etiquetaexpedicao.class);
	protected Set<Etiquetaexpedicaogrupo> listaEtiquetaexpedicaogrupo = new ListSet<Etiquetaexpedicaogrupo>(Etiquetaexpedicaogrupo.class);
	protected Set<Ordemprodutohistorico> listaOrdemprodutohistorico = new ListSet<Ordemprodutohistorico>(Ordemprodutohistorico.class);
	protected Set<Ordemprodutoligacao> listaOrdemprodutoLigacao = new ListSet<Ordemprodutoligacao>(Ordemprodutoligacao.class);
	protected List<Ordemservicoprodutoendereco> listaOrdemservicoprodutoendereco = new ListSet<Ordemservicoprodutoendereco>(Ordemservicoprodutoendereco.class);
	protected Produtoembalagem produtoembalagem;
	
	//Transiente
	protected Boolean ignore = false;
	protected Boolean createdByColetor = false;
	protected Produtocodigobarras codigoBarrasLido;
	protected Long qtdeFaltante;
	protected Long qtdeFracionada;
	protected Integer qntdeTotal;
	
	// Construtores
	public Ordemservicoproduto() {
	}
	
	public Ordemservicoproduto(Integer cd) {
		this.cdordemservicoproduto = cd;
	}
	
	public Ordemservicoproduto(Produto produto, Long qtdeesperada, Ordemprodutostatus ordemprodutostatus) {
		this.produto = produto;
		this.qtdeesperada = qtdeesperada;
		this.ordemprodutostatus = ordemprodutostatus;
	}
	
	// Métodos get e set
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_ordemservicoproduto")
	public Integer getCdordemservicoproduto() {
		return cdordemservicoproduto;
	}
	public void setCdordemservicoproduto(Integer id) {
		this.cdordemservicoproduto = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	public Produto getProduto() {
		return produto;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdprodutoembalagem")
	public Produtoembalagem getProdutoembalagem() {
		return produtoembalagem;
	}
	
	@Required
	@DisplayName("Qtde esperada")
	public Long getQtdeesperada() {
		return qtdeesperada;
	}
	
	@Transient
	public Boolean getIgnore() {
		return ignore;
	}
	
	@Transient
	public Boolean getCreatedByColetor() {
		return createdByColetor;
	}
	
	@Transient
	public Produtocodigobarras getCodigoBarrasLido() {
		return codigoBarrasLido;
	}
	
	@Transient
	public Long getQtdeFaltante() {
		return qtdeFaltante;
	}
	
	@Transient
	public Long getQtdeFracionada() {
		return qtdeFracionada;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdordemprodutostatus")
	public Ordemprodutostatus getOrdemprodutostatus() {
		return ordemprodutostatus;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipopalete")
	public Tipopalete getTipopalete() {
		return tipopalete;
	}
	
	@OneToMany(mappedBy="ordemservicoproduto")
	@IndexColumn(name="cdetiquetaexpedicao")
	@CollectionOfElements
	public Set<Etiquetaexpedicao> getListaEtiquetaexpedicao() {
		return listaEtiquetaexpedicao;
	}

	@OneToMany(mappedBy="ordemservicoproduto")
	@IndexColumn(name="cdetiquetaexpedicaogrupo")
	@CollectionOfElements
	public Set<Etiquetaexpedicaogrupo> getListaEtiquetaexpedicaogrupo() {
		return listaEtiquetaexpedicaogrupo;
	}
	
	@OneToMany(mappedBy="ordemservicoproduto")
	public List<Ordemservicoprodutoendereco> getListaOrdemservicoprodutoendereco() {
		return listaOrdemservicoprodutoendereco;
	}
	
	@OneToMany(mappedBy="ordemservicoproduto")
	public Set<Ordemprodutohistorico> getListaOrdemprodutohistorico() {
		return listaOrdemprodutohistorico;
	}
	
	@OneToMany(mappedBy="ordemservicoproduto")
	public Set<Ordemprodutoligacao> getListaOrdemprodutoLigacao() {
		return listaOrdemprodutoLigacao;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public void setQtdeesperada(Long qtdeesperada) {
		this.qtdeesperada = qtdeesperada;
	}
	
	public void setListaEtiquetaexpedicao(Set<Etiquetaexpedicao> listaEtiquetaexpedicao) {
		this.listaEtiquetaexpedicao = listaEtiquetaexpedicao;
	}
	
	public void setListaEtiquetaexpedicaogrupo(Set<Etiquetaexpedicaogrupo> listaEtiquetaexpedicaogrupo) {
		this.listaEtiquetaexpedicaogrupo = listaEtiquetaexpedicaogrupo;
	}
	
	public void setListaOrdemprodutohistorico(Set<Ordemprodutohistorico> listaOrdemprodutohistorico) {
		this.listaOrdemprodutohistorico = listaOrdemprodutohistorico;
	}
	
	public void setOrdemprodutostatus(Ordemprodutostatus ordemprodutostatus) {
		this.ordemprodutostatus = ordemprodutostatus;
	}
	
	public void setListaOrdemprodutoLigacao(Set<Ordemprodutoligacao> listaOrdemprodutoLigacao) {
		this.listaOrdemprodutoLigacao = listaOrdemprodutoLigacao;
	}

	public void setIgnore(Boolean ignore) {
		this.ignore = ignore;
	}
	
	public void setCreatedByColetor(Boolean createdByColetor) {
		this.createdByColetor = createdByColetor;
	}

	public void setTipopalete(Tipopalete tipopalete) {
		this.tipopalete = tipopalete;
	}
	
	public void setListaOrdemservicoprodutoendereco(List<Ordemservicoprodutoendereco> listaOrdemservicoprodutoendereco) {
		this.listaOrdemservicoprodutoendereco = listaOrdemservicoprodutoendereco;
	}
	
	public void setProdutoembalagem(Produtoembalagem produtoembalagem) {
		this.produtoembalagem = produtoembalagem;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cdordemservicoproduto == null) ? 0 : cdordemservicoproduto
						.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if (obj instanceof Produto) {
			Produto produto = (Produto) obj;
			return produto.getCdproduto().equals(this.getProduto().getCdproduto());
		}
		
		if (getClass() != obj.getClass())
			return false;
		final Ordemservicoproduto other = (Ordemservicoproduto) obj;
		if (cdordemservicoproduto == null) {
			if (other.cdordemservicoproduto != null)
				return false;
		} else if (!cdordemservicoproduto.equals(other.cdordemservicoproduto))
			return false;
		return true;
	}

	public void setCodigoBarrasLido(Produtocodigobarras pcb) {
		this.codigoBarrasLido = pcb;
	}
	
	public void setQtdeFaltante(Long qtdeFaltante) {
		this.qtdeFaltante = qtdeFaltante;
	}
	
	public void setQtdeFracionada(Long qtdeFracionada) {
		this.qtdeFracionada = qtdeFracionada;
	}

	@Transient
	public Integer getQntdeTotal() {
		return qntdeTotal;
	}

	public void setQntdeTotal(Integer qntdeTotal) {
		this.qntdeTotal = qntdeTotal;
	}
	
}

