package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.IndexColumn;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.MaxValue;
import br.com.linkcom.neo.validation.annotation.MinValue;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_produto", sequenceName = "sq_produto")
public class Produto {

	protected Integer cdproduto;
	protected Long codigoerp;
	protected String descricao;
	protected String referencia;
	protected Produtoclasse produtoclasse;
	protected Long altura;
	protected Long largura;
	protected Long profundidade;
	protected Double peso;
	protected Double pesounitario;
	protected String codigo;
	protected Integer qtdevolumes;
	protected String complementocodigobarras;
	protected Produto produtoprincipal;
	protected Double cubagem;
	protected Double cubagemunitaria;
	protected Boolean exigevalidade;
	protected Boolean exigelote;
	protected List<Pedidocompraproduto> listaPedidoCompraProduto = new ArrayList<Pedidocompraproduto>();
	protected Set<Notafiscalentradaproduto> listaNotafiscalentradaproduto = new ListSet<Notafiscalentradaproduto>(Notafiscalentradaproduto.class);
	protected List<Produtoclassificacao> listaProdutoClassificacao = new ArrayList<Produtoclassificacao>();
	protected List<Produtotipopalete> listaProdutoTipoPalete = new ArrayList<Produtotipopalete>();
	protected List<Ordemservicoproduto> listaOrdemServicoProduto = new ArrayList<Ordemservicoproduto>();
	protected List<Dadologistico> listaDadoLogistico = new ArrayList<Dadologistico>();
	protected List<Produtotipoestrutura> listaProdutoTipoEstrutura = new ArrayList<Produtotipoestrutura>();
	protected List<Produtocodigobarras> listaProdutoCodigoDeBarras = new ArrayList<Produtocodigobarras>();
	protected List<Produtoembalagem> listaProdutoEmbalagem = new ArrayList<Produtoembalagem>();
	protected List<Pedidovendaproduto> listaPedidoVendaProduto = new ArrayList<Pedidovendaproduto>();
	protected Set<Enderecoproduto> listaEnderecoproduto = new ListSet<Enderecoproduto>(Enderecoproduto.class);
	protected Set<Transferenciaitem> listaTransferenciaitem = new ListSet<Transferenciaitem>(Transferenciaitem.class);
	
	//Transitentes
	protected Integer totalDirvegencias;
	protected StringBuilder stringNorma = new StringBuilder();
	protected Set<Produto> listaVolumes = new ListSet<Produto>(Produto.class);
	protected String produtoCorte;
	protected Boolean modificado = false;
	protected Integer qtde;
	protected Ordemservicoproduto ordemservicoproduto;
	
	//Transientes Relatorio movimentação
	protected Money valormedio;
	protected Integer qtdeanterior;
	protected Integer qtdeentrada;
	protected Integer qtdesaida;
	protected Integer qtdefinal;
	protected Boolean larguraexcedente;
	protected Long picoPallete;
	protected Long qtdePallete;
	
	//Construtores
	public Produto() {

	}
	
	public Produto(Integer cdproduto) {
		this.cdproduto = cdproduto;
	}
	
	public Produto(Integer cdproduto, String produtonome) {
		this.cdproduto = cdproduto;
		this.descricao = produtonome;
	}
	
	public Produto(Integer cdproduto, String codigoproduto, String descricaoproduto) {
		this.cdproduto = cdproduto;
		this.codigo = codigoproduto;
		this.descricao = descricaoproduto;
	}
	
	//Metodos get e set
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_produto")
	public Integer getCdproduto() {
		return cdproduto;
	}
	
	@DisplayName("Código do produto no ERP")
	@MaxLength(10)
	public Long getCodigoerp() {
		return codigoerp;
	}
	
	@Required
	@DisplayName("Descrição")
	@MaxLength(100)
	public String getDescricao() {
		return descricao;
	}
		
	
	@DisplayName("Referência")
	@MaxLength(20)
	public String getReferencia() {
		return referencia;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdprodutoclasse")
	@DisplayName("Classe do produto")
	public Produtoclasse getProdutoclasse() {
		return produtoclasse;
	}
	@MaxLength(5)
	@DisplayName("Altura(Cm)")
	@MinValue(1)
	public Long getAltura() {
		return altura;
	}
	@MaxLength(5)
	@DisplayName("Largura(Cm)")
	@MinValue(1)
	public Long getLargura() {
		return largura;
	}
	@MaxLength(5)
	@DisplayName("Profundidade(Cm)")
	@MinValue(1)
	public Long getProfundidade() {
		return profundidade;
	}
	
	@DisplayName("Peso(Kg)")
	@MaxValue(value = 9999999)
	public Double getPeso() {
		return peso;
	}
	
	@DisplayName("Peso unitário(Kg)")
	@MaxValue(value = 9999999)
	@Formula("PESOUNITARIO_PRODUTO(cdproduto)")
	public Double getPesounitario() {
		return pesounitario;
	}
	
	@OneToMany(mappedBy="produto")
	@IndexColumn(name = "cdpedidocompraproduto")
	@CollectionOfElements
	public List<Pedidocompraproduto> getListaPedidoCompraProduto() {
		return listaPedidoCompraProduto;
	}
	
	@OneToMany(mappedBy="produto")
	@IndexColumn(name="cdordemsevicoproduto")
	@CollectionOfElements
	public List<Ordemservicoproduto> getListaOrdemServicoProduto() {
		return listaOrdemServicoProduto;
	}
	
	@OneToMany(mappedBy="produto")
	@IndexColumn(name="cdprodutotipopalete")
	@DisplayName("Tipos de palete do produto")
	@CollectionOfElements
	public List<Produtotipopalete> getListaProdutoTipoPalete() {
		return listaProdutoTipoPalete;
	}
	
	@Transient
	public Integer getTotalDirvegencias() {
		return totalDirvegencias;
	}
	
	@OneToMany(mappedBy="produto")
	@IndexColumn(name="cdnotafiscalentradaproduto")
	@CollectionOfElements
	public Set<Notafiscalentradaproduto> getListaNotafiscalentradaproduto() {
		return listaNotafiscalentradaproduto;
	}
	
	@DisplayName("Classificação do produto")
	@OneToMany(mappedBy = "produto")
	@IndexColumn(name="cdprodutoclassificacao")
	@CollectionOfElements
	public List<Produtoclassificacao> getListaProdutoClassificacao() {
		return listaProdutoClassificacao;
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="produto")
	@DisplayName("Dados logísticos")
	@IndexColumn(name="cddadologistico")
	@Required
	public List<Dadologistico> getListaDadoLogistico() {
		return listaDadoLogistico;
	}
	
	@OneToMany(mappedBy="produto")
	public Set<Enderecoproduto> getListaEnderecoproduto() {
		return listaEnderecoproduto;
	}
	
	@OneToMany(mappedBy="produto")
	public Set<Transferenciaitem> getListaTransferenciaitem() {
		return listaTransferenciaitem;
	}
	
	@DisplayName("Cubagem(m³)")
	@Column(insertable=false,updatable=false)
	public Double getCubagem() {
		return cubagem;
	}
	
	@DisplayName("Cubagem unitária(m³)")
	@Formula("CUBAGEMUNITARIA_PRODUTO(cdproduto)")
	public Double getCubagemunitaria() {
		return cubagemunitaria;
	}
	
	@DisplayName("Volumes")
	public Integer getQtdevolumes() {
		return qtdevolumes;
	}
	
	@Transient
	public StringBuilder getStringNorma() {
		return stringNorma;
	}
	
	@OneToMany(mappedBy = "produto")
	@DisplayName("Tipo de estrutura do produto")
	public List<Produtotipoestrutura> getListaProdutoTipoEstrutura() {
		return listaProdutoTipoEstrutura;
	}
	
	@OneToMany(mappedBy = "produto")
	@DisplayName("Código de barras")
	public List<Produtocodigobarras> getListaProdutoCodigoDeBarras() {
		return listaProdutoCodigoDeBarras;
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="produto")
	@DisplayName("Embalagens do produto")
	@IndexColumn(name="cdprodutoembalagem")
	public List<Produtoembalagem> getListaProdutoEmbalagem() {
		return listaProdutoEmbalagem;
	}
	
	@Required
	@DisplayName("Código")
	@MaxLength(20)
	public String getCodigo() {
		return codigo;
	}
	
	@DisplayName("Produto principal")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdprodutoprincipal")
	public Produto getProdutoprincipal() {
		return produtoprincipal;
	}
	
	@DisplayName("Complem. cód. barras")
	@MaxLength(4)
	public String getComplementocodigobarras() {
		return complementocodigobarras;
	}
	
	@DisplayName("Volumes do produto")
	@Transient
	public Set<Produto> getListaVolumes() {
		return listaVolumes;
	}
	
	@OneToMany(mappedBy="produto")
	@IndexColumn(name = "cdpedidovendaproduto")
	@CollectionOfElements
	public List<Pedidovendaproduto> getListaPedidoVendaProduto() {
		return listaPedidoVendaProduto;
	}

	@DisplayName("Exige lote")
	public Boolean getExigelote() {
		return exigelote;
	}
	
	@DisplayName("Exige data de validade")
	public Boolean getExigevalidade() {
		return exigevalidade;
	}
	
	@Transient
	//@DescriptionProperty(usingFields={"codigo","descricao"})
	public String getProdutoCorte() {
		return getCodigo() + "  -  " + getDescricao();
	}
	
	@Transient
	public Boolean getModificado() {
		return modificado;
	}
	
	@Transient
	public Integer getQtde() {
		return qtde;
	}
	
	public void setCdproduto(Integer cdproduto) {
		this.cdproduto = cdproduto;
	}
	
	public void setCodigoerp(Long codigoerp) {
		this.codigoerp = codigoerp;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	public void setPesounitario(Double pesounitario) {
		this.pesounitario = pesounitario;
	}
	
	public void setListaPedidoCompraProduto(List<Pedidocompraproduto> listaPedidoCompraProduto) {
		this.listaPedidoCompraProduto = listaPedidoCompraProduto;
	}	
	
	public void setListaOrdemServicoProduto(List<Ordemservicoproduto> listaOrdemServicoProduto) {
		this.listaOrdemServicoProduto = listaOrdemServicoProduto;
	}
	
	public void setListaProdutoTipoPalete(List<Produtotipopalete> listaProdutoTipoPalete) {
		this.listaProdutoTipoPalete = listaProdutoTipoPalete;
	}
	
	public void setQtdevolumes(Integer qtdevolumes) {
		this.qtdevolumes = qtdevolumes;
	}
	
	public void setListaVolumes(Set<Produto> listaVolumes) {
		this.listaVolumes = listaVolumes;
	}
	
	public void setTotalDirvegencias(Integer totalDirvegencias) {
		this.totalDirvegencias = totalDirvegencias;
	}
	
	public void setListaNotafiscalentradaproduto(Set<Notafiscalentradaproduto> listaNotafiscalentradaproduto) {
		this.listaNotafiscalentradaproduto = listaNotafiscalentradaproduto;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public void setProdutoclasse(Produtoclasse produtoclasse) {
		this.produtoclasse = produtoclasse;
	}

	public void setAltura(Long altura) {
		this.altura = altura;
	}

	public void setLargura(Long largura) {
		this.largura = largura;
	}

	public void setProfundidade(Long profundidade) {
		this.profundidade = profundidade;
	}
		
	public void setListaProdutoClassificacao(List<Produtoclassificacao> listaProdutoClassificacao) {
		this.listaProdutoClassificacao = listaProdutoClassificacao;
	}
	
	public void setCubagem(Double cubagem) {
		this.cubagem = cubagem;
	}
	
	public void setCubagemunitaria(Double cubagemunitaria) {
		this.cubagemunitaria = cubagemunitaria;
	}
	
	public void setStringNorma(StringBuilder stringNorma) {
		this.stringNorma = stringNorma;
	}
	
	public void setListaDadoLogistico(List<Dadologistico> listaDadoLogistico) {
		this.listaDadoLogistico = listaDadoLogistico;
	}
	
	public void setListaProdutoTipoEstrutura(List<Produtotipoestrutura> listaProdutoTipoEstrutura) {
		this.listaProdutoTipoEstrutura = listaProdutoTipoEstrutura;
	}
	
	public void setListaProdutoCodigoDeBarras(List<Produtocodigobarras> listaProdutoCodigoDeBarras) {
		this.listaProdutoCodigoDeBarras = listaProdutoCodigoDeBarras;
	}
	
	public void setListaProdutoEmbalagem(List<Produtoembalagem> listaProdutoEmbalagem) {
		this.listaProdutoEmbalagem = listaProdutoEmbalagem;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public void setProdutoprincipal(Produto produtoprincipal) {
		this.produtoprincipal = produtoprincipal;
	}
	
	public void setComplementocodigobarras(String complementocodigobarras) {
		this.complementocodigobarras = complementocodigobarras;
	}
	
	public void setListaPedidoVendaProduto(List<Pedidovendaproduto> listaPedidoVendaProduto) {
		this.listaPedidoVendaProduto = listaPedidoVendaProduto;
	}
	
	public void setProdutoCorte(String produtoCorte) {
		this.produtoCorte = produtoCorte;
	}
	
	public void setListaEnderecoproduto(Set<Enderecoproduto> listaEnderecoproduto) {
		this.listaEnderecoproduto = listaEnderecoproduto;
	}
	
	public void setListaTransferenciaitem(Set<Transferenciaitem> listaTransferenciaitem) {
		this.listaTransferenciaitem = listaTransferenciaitem;
	}
	
	public void setModificado(Boolean modificado) {
		this.modificado = modificado;
	}
	
	public void setExigelote(Boolean exigelote) {
		this.exigelote = exigelote;
	}
	
	public void setExigevalidade(Boolean exigevalidade) {
		this.exigevalidade = exigevalidade;
	}
	
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	
	@Transient
	public Ordemservicoproduto getOrdemservicoproduto() {
		return ordemservicoproduto;
	}
	
	public void setOrdemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		this.ordemservicoproduto = ordemservicoproduto;
	}
	
	@Transient
	@DescriptionProperty(usingFields={"produtoprincipal", "descricao", "codigo"})
	public String getDescriptionProperty(){
		if (codigo != null && produtoprincipal != null && produtoprincipal.getDescricao() != null)
			return this.codigo + " - " + this.produtoprincipal.getDescricao() + " - " + this.descricao;
		else if(this.codigo != null)
			return this.codigo + " - " + this.descricao;
		else
			return this.descricao;
	}
	
	//Metodos
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if (obj instanceof Ordemservicoproduto) {
			Ordemservicoproduto os = (Ordemservicoproduto) obj;
			if (os.getProduto() == null || os.getProduto().getCdproduto() == null || this.getCdproduto() == null)
				return false;
			
			return os.getProduto().getCdproduto().equals(this.getCdproduto());
		}
		
		if (getClass() != obj.getClass())
			return false;
		final Produto other = (Produto) obj;
		if (cdproduto == null) {
			if (other.cdproduto != null)
				return false;
		} else if (!cdproduto.equals(other.cdproduto))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdproduto == null) ? 0 : cdproduto.hashCode());
		return result;
	}

	@Transient
	public Integer getQtdeanterior() {
		return qtdeanterior;
	}

	@Transient
	public Integer getQtdeentrada() {
		return qtdeentrada;
	}

	@Transient
	public Integer getQtdesaida() {
		return qtdesaida;
	}

	@Transient
	public Integer getQtdefinal() {
		return qtdefinal;
	}

	@Transient
	public Money getValormedio() {
		return valormedio;
	}

	public void setQtdeanterior(Integer qtdeanterior) {
		this.qtdeanterior = qtdeanterior;
	}
	
	public void setQtdeentrada(Integer qtdeentrada) {
		this.qtdeentrada = qtdeentrada;
	}

	public void setQtdesaida(Integer qtdesaida) {
		this.qtdesaida = qtdesaida;
	}

	public void setQtdefinal(Integer qtdefinal) {
		this.qtdefinal = qtdefinal;
	}

	public void setValormedio(Money valormedio) {
		this.valormedio = valormedio;
	}
	
	@Transient
	public Boolean getLarguraexcedente() {
		return larguraexcedente;
	}
	public void setLarguraexcedente(Boolean larguraexcedente) {
		this.larguraexcedente = larguraexcedente;
	}
	
	@Transient
	public Long getPicoPallete() {
		return picoPallete;
	}
	
	public void setPicoPallete(Long picoPallete) {
		this.picoPallete = picoPallete;
	}
	
	@Transient
	public Long getQtdePallete() {
		return qtdePallete;
	}
	
	public void setQtdePallete(Long qtdePallete) {
		this.qtdePallete = qtdePallete;
	}
	
}
