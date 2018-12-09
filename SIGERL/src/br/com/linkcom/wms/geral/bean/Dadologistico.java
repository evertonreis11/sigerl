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

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.MinValue;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_dadologistico", sequenceName="sq_dadologistico")
public class Dadologistico {
	
	protected Integer cddadologistico;
	protected Produto produto;
	protected Deposito deposito;
	protected Boolean normavolume;
	protected Linhaseparacao linhaseparacao;
	protected Boolean larguraexcedente;
	protected Tipoendereco tipoendereco;
	protected Area area;
	protected Endereco endereco;
	protected Long capacidadepicking;
	protected Long pontoreposicao;
	protected Tiporeposicao tiporeposicao;
	protected Descargapreco descargapreco;
	protected Boolean geracodigo;
	
	//Transiente
	protected Double pontoReposicaoPercentual;
	protected String enderecoDePicking;
	protected String tipoEnderecoPicking;
	
	// Construtores
	public Dadologistico() {
		
	}
	
	public Dadologistico(Integer cd) {
		this.cddadologistico = cd;		
	}
	
	public Dadologistico(Integer cddadologistico, Boolean larguraexcedenteDadologistico, Deposito deposito) {
		this.cddadologistico = cddadologistico;
		this.larguraexcedente = larguraexcedenteDadologistico;
		this.deposito = deposito;
	}
	
	// Métodos get e set	
	@Id
	@DisplayName("Id")
	@GeneratedValue(generator = "sq_dadologistico",strategy = GenerationType.AUTO)
	public Integer getCddadologistico() {
		return cddadologistico;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdproduto")
	public Produto getProduto() {
		return produto;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")
	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@DisplayName("Utilizar norma do volume")
	@Required
	public Boolean getNormavolume() {
		return normavolume;
	}
	
	@Required
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdlinhaseparacao")
	@DisplayName("Linha de separação")
	public Linhaseparacao getLinhaseparacao() {
		return linhaseparacao;
	}
	
	@DisplayName("Largura excedente")
	@Required
	public Boolean getLarguraexcedente() {
		return larguraexcedente;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipoendereco")
	@DisplayName("Tipo de endereço de pulmão")
	public Tipoendereco getTipoendereco() {
		return tipoendereco;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdarea")
	@DisplayName("Área de armazenagem")
	public Area getArea() {
		return area;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdendereco")
	@DisplayName("Endereço")
	public Endereco getEndereco() {
		return endereco;
	}
	@DisplayName("Capacidade de picking")
	@MinValue(1)
	@MaxLength(10)
	public Long getCapacidadepicking() {
		return capacidadepicking;
	}
	
	@DisplayName("Ponto de reposição(Quantidade)")
	@MinValue(0)
	@MaxLength(10)
	public Long getPontoreposicao() {
		return pontoreposicao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtiporeposicao")
	@DisplayName("Tipo de reposição")
	public Tiporeposicao getTiporeposicao() {
		return tiporeposicao;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddescargapreco")
	@DisplayName("Tabela de descarga")
	public Descargapreco getDescargapreco() {
		return descargapreco;
	}
	
	@DisplayName("Gerar código de barras no recebimento")
	public Boolean getGeracodigo() {
		return geracodigo;
	}
	
	@Transient
	@DisplayName("Ponto de reposição de picking")
	public Double getPontoReposicaoPercentual() {
		return pontoReposicaoPercentual;
	}
	
	@DisplayName("Endereço de piking")
	@Transient
	public String getEnderecoDePicking() {
		return enderecoDePicking;
	}
	
	@Transient
	@DisplayName("Tipo de endereço de picking")
	public String getTipoEnderecoPicking() {
		return tipoEnderecoPicking;
	}

	public void setCddadologistico(Integer cddadologistico) {
		this.cddadologistico = cddadologistico;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setNormavolume(Boolean normavolume) {
		this.normavolume = normavolume;
	}
	
	public void setLinhaseparacao(Linhaseparacao linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}
	
	public void setLarguraexcedente(Boolean larguraexcedente) {
		this.larguraexcedente = larguraexcedente;
	}
	
	public void setTipoendereco(Tipoendereco tipoendereco) {
		this.tipoendereco = tipoendereco;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public void setCapacidadepicking(Long capacidadepicking) {
		this.capacidadepicking = capacidadepicking;
	}
	
	public void setPontoreposicao(Long pontoreposicao) {
		this.pontoreposicao = pontoreposicao;
	}
	
	public void setTiporeposicao(Tiporeposicao tiporeposicao) {
		this.tiporeposicao = tiporeposicao;
	}
	
	public void setDescargapreco(Descargapreco descargapreco) {
		this.descargapreco = descargapreco;
	}
	
	public void setGeracodigo(Boolean geracodigo) {
		this.geracodigo = geracodigo;
	}
	
	public void setEnderecoDePicking(String enderecoDePicking) {
		this.enderecoDePicking = enderecoDePicking;
	}
	
	public void setTipoEnderecoPicking(String tipoEnderecoPicking) {
		this.tipoEnderecoPicking = tipoEnderecoPicking;
	}
	
	public void setPontoReposicaoPercentual(Double pontoReposicaoPercentual) {
		this.pontoReposicaoPercentual = pontoReposicaoPercentual;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Dadologistico)
			if(obj != null)
				return ((Dadologistico)obj).getCddadologistico().equals(this.cddadologistico);
		return super.equals(obj);
	}
}
