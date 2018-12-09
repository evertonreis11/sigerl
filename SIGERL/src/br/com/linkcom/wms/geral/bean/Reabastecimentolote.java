package br.com.linkcom.wms.geral.bean;

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
import br.com.linkcom.neo.validation.annotation.MaxValue;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.filter.IntervaloEndereco;

@Entity
@SequenceGenerator(name = "sq_reabastecimentolote", sequenceName = "sq_reabastecimentolote")
public class Reabastecimentolote implements IntervaloEndereco{

	private Integer cdreabastecimentolote;
	private Reabastecimento reabastecimento;
	private Area area;
	private Integer ruainicial;
	private Integer predioinicial;
	private Integer nivelinicial;
	private Integer aptoinicial;
	private Integer ruafinal;
	private Integer prediofinal;
	private Integer nivelfinal;
	private Integer aptofinal;
	private Produto produto;
	private Enderecolado enderecolado;
	private Tiporeposicao tiporeposicao;
	private Integer pontoreposicao;
	private List<Ordemservico> listaOrdemservico;
	
	//Propriedades transientes
	private String codigo;
	
	@Required
	@MaxLength(3)
	@DisplayName("Apto. Final")
	public Integer getAptofinal() {
		return aptofinal;
	}

	@Required
	@MaxLength(3)
	@DisplayName("Apto. Inicial")
	public Integer getAptoinicial() {
		return aptoinicial;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdarea")
	@DisplayName("Área")
	@Required
	public Area getArea() {
		return area;
	}

	@Id
	@GeneratedValue(generator = "sq_reabastecimentolote", strategy = GenerationType.AUTO)
	public Integer getCdreabastecimentolote() {
		return cdreabastecimentolote;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdenderecolado")
	@DisplayName("Lado")
	public Enderecolado getEnderecolado() {
		return enderecolado;
	}

	@OneToMany(mappedBy = "reabastecimentolote", fetch=FetchType.LAZY)
	public List<Ordemservico> getListaOrdemservico() {
		return listaOrdemservico;
	}

	@Required
	@MaxLength(2)
	@DisplayName("Nível Final")
	public Integer getNivelfinal() {
		return nivelfinal;
	}

	@Required
	@MaxLength(2)
	@DisplayName("Nível Inicial")
	public Integer getNivelinicial() {
		return nivelinicial;
	}

	@Required
	@MaxValue(100)
	@MaxLength(3)
	@DisplayName("Ponto de reposição")
	public Integer getPontoreposicao() {
		return pontoreposicao;
	}

	@Required
	@MaxLength(3)
	@DisplayName("Prédio Final")
	public Integer getPrediofinal() {
		return prediofinal;
	}

	@Required
	@MaxLength(3)
	@DisplayName("Prédio Inicial")
	public Integer getPredioinicial() {
		return predioinicial;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdproduto")
	public Produto getProduto() {
		return produto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdreabastecimento")
	@DisplayName("Reabastecimento")
	public Reabastecimento getReabastecimento() {
		return reabastecimento;
	}

	@Required
	@MaxLength(3)
	@DisplayName("Rua Final")
	public Integer getRuafinal() {
		return ruafinal;
	}

	@Required
	@MaxLength(3)
	@DisplayName("Rua Inicial")
	public Integer getRuainicial() {
		return ruainicial;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtiporeposicao")
	@DisplayName("Tipo de reposição")
	public Tiporeposicao getTiporeposicao() {
		return tiporeposicao;
	}

	public void setAptofinal(Integer aptofinal) {
		this.aptofinal = aptofinal;
	}

	public void setAptoinicial(Integer aptoinicial) {
		this.aptoinicial = aptoinicial;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public void setCdreabastecimentolote(Integer cdreabastecimentolote) {
		this.cdreabastecimentolote = cdreabastecimentolote;
	}

	public void setEnderecolado(Enderecolado enderecolado) {
		this.enderecolado = enderecolado;
	}

	public void setListaOrdemservico(List<Ordemservico> listaOrdemservico) {
		this.listaOrdemservico = listaOrdemservico;
	}

	public void setNivelfinal(Integer nivelfinal) {
		this.nivelfinal = nivelfinal;
	}

	public void setNivelinicial(Integer nivelinicial) {
		this.nivelinicial = nivelinicial;
	}

	public void setPontoreposicao(Integer pontoreposicao) {
		this.pontoreposicao = pontoreposicao;
	}

	public void setPrediofinal(Integer prediofinal) {
		this.prediofinal = prediofinal;
	}

	public void setPredioinicial(Integer predioinicial) {
		this.predioinicial = predioinicial;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setReabastecimento(Reabastecimento reabastecimento) {
		this.reabastecimento = reabastecimento;
	}

	public void setRuafinal(Integer ruafinal) {
		this.ruafinal = ruafinal;
	}

	public void setRuainicial(Integer ruainicial) {
		this.ruainicial = ruainicial;
	}

	public void setTiporeposicao(Tiporeposicao tiporeposicao) {
		this.tiporeposicao = tiporeposicao;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cdreabastecimentolote == null) ? 0 : cdreabastecimentolote
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		final Reabastecimentolote other = (Reabastecimentolote) obj;
		
		if (cdreabastecimentolote == null && other.cdreabastecimentolote == null)
			return this == other;
		else if (cdreabastecimentolote == null) {
			if (other.cdreabastecimentolote != null)
				return false;
		} else if (!cdreabastecimentolote.equals(other.cdreabastecimentolote))
			return false;
		
		return true;
	}

	@Override
	@Transient
	public Enderecofuncao getEnderecofuncao() {
		//O lote deve considerar apenas os endereços de picking do intervalo selecionado.
		return Enderecofuncao.PICKING;
	}
	
}
