package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
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
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.filter.IntervaloEndereco;

@Entity
@SequenceGenerator(name = "sq_inventariolote", sequenceName = "sq_inventariolote")
public class Inventariolote  implements IntervaloEndereco{
	
	// constantes
	public static final Boolean FRACIONADA_FALSE = Boolean.FALSE;
	public static final Boolean FRACIONADA_TRUE = Boolean.TRUE;
	
	// variaveis
	protected Integer cdinventariolote;
	protected Inventario inventario;
	protected Area area;
	protected Integer ruainicial;
	protected Integer predioinicial;
	protected Integer nivelinicial;
	protected Integer aptoinicial;
	protected Integer ruafinal;
	protected Integer prediofinal;
	protected Integer nivelfinal;
	protected Integer aptofinal;
	protected Produto produto;
	protected Enderecofuncao enderecofuncao;
	protected Enderecolado enderecolado;
	protected Boolean fracionada = Boolean.FALSE;
	protected List<Ordemservico> listaOrdemservico = new ArrayList<Ordemservico>();
	
	protected String codigo;
	
	// construtor
	public Inventariolote() {

	}
	
	public Inventariolote(Integer cd) {
		this.cdinventariolote = cd;
	}

	public Inventariolote(Area area, Integer ruainicial, Integer predioinicial, Integer nivelinicial, Integer aptoinicial, 
						  Integer ruafinal, Integer prediofinal, Integer nivelfinal, Integer aptofinal, Enderecolado enderecolado, 
						  Boolean fracionada, Enderecofuncao enderecofuncao, Produto produto) {
		this.area = area;
		this.ruainicial = ruainicial;
		this.predioinicial = predioinicial;
		this.nivelinicial = nivelinicial;
		this.aptoinicial = aptoinicial;
		this.ruafinal = ruafinal;
		this.prediofinal = prediofinal;
		this.nivelfinal = nivelfinal;
		this.aptofinal = aptofinal;
		this.enderecolado = enderecolado;
		this.fracionada = fracionada;
		this.enderecofuncao = enderecofuncao;
		this.produto = produto;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(generator = "sq_inventariolote", strategy = GenerationType.AUTO)
	public Integer getCdinventariolote() {
		return cdinventariolote;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdinventario")
	@DisplayName("Invetário")
	public Inventario getInventario() {
		return inventario;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdarea")
	@DisplayName("Área")
	@Required
	public Area getArea() {
		return area;
	}
	@Required
	@MaxLength(3)
	@DisplayName("Rua Inicial")
	public Integer getRuainicial() {
		return ruainicial;
	}
	@Required
	@MaxLength(3)
	@DisplayName("Prédio Inicial")
	public Integer getPredioinicial() {
		return predioinicial;
	}
	@Required
	@MaxLength(2)
	@DisplayName("Nível Inicial")
	public Integer getNivelinicial() {
		return nivelinicial;
	}
	@Required
	@MaxLength(3)
	@DisplayName("Apto. Inicial")
	public Integer getAptoinicial() {
		return aptoinicial;
	}
	@Required
	@MaxLength(3)
	@DisplayName("Rua Final")
	public Integer getRuafinal() {
		return ruafinal;
	}
	@Required
	@MaxLength(3)
	@DisplayName("Prédio Final")
	public Integer getPrediofinal() {
		return prediofinal;
	}
	@Required
	@MaxLength(2)
	@DisplayName("Nível Final")
	public Integer getNivelfinal() {
		return nivelfinal;
	}
	@Required
	@MaxLength(3)
	@DisplayName("Apto. Final")
	public Integer getAptofinal() {
		return aptofinal;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	public Produto getProduto() {
		return produto;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdenderecofuncao")
	public Enderecofuncao getEnderecofuncao() {
		return enderecofuncao;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdenderecolado")
	@DisplayName("Lado")
	public Enderecolado getEnderecolado() {
		return enderecolado;
	}
	public Boolean getFracionada() {
		return fracionada;
	}
	
	@OneToMany(mappedBy="inventariolote")
	public List<Ordemservico> getListaOrdemservico() {
		return listaOrdemservico;
	}
	
	public void setCdinventariolote(Integer cdinventariolote) {
		this.cdinventariolote = cdinventariolote;
	}
	public void setInventario(Inventario inventario) {
		this.inventario = inventario;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public void setRuainicial(Integer ruainicial) {
		this.ruainicial = ruainicial;
	}
	public void setPredioinicial(Integer predioinicial) {
		this.predioinicial = predioinicial;
	}
	public void setNivelinicial(Integer nivelinicial) {
		this.nivelinicial = nivelinicial;
	}
	public void setAptoinicial(Integer aptoinicial) {
		this.aptoinicial = aptoinicial;
	}
	public void setRuafinal(Integer ruafinal) {
		this.ruafinal = ruafinal;
	}
	public void setPrediofinal(Integer prediofinal) {
		this.prediofinal = prediofinal;
	}
	public void setNivelfinal(Integer nivelfinal) {
		this.nivelfinal = nivelfinal;
	}
	public void setAptofinal(Integer aptofinal) {
		this.aptofinal = aptofinal;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public void setEnderecofuncao(Enderecofuncao enderecofuncao) {
		this.enderecofuncao = enderecofuncao;
	}
	public void setEnderecolado(Enderecolado enderecolado) {
		this.enderecolado = enderecolado;
	}
	public void setFracionada(Boolean fracionada) {
		this.fracionada = fracionada;
	}
	
	public void setListaOrdemservico(List<Ordemservico> listaOrdemservico) {
		this.listaOrdemservico = listaOrdemservico;
	}
	
	@Transient
	@MaxLength(20)
	@DisplayName("Código")
	public String getCodigo() {
		if(this.produto != null)
			return this.produto.codigo;
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
}
