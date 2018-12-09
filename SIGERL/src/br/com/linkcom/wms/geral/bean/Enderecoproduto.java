package br.com.linkcom.wms.geral.bean;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name = "sq_enderecoproduto", sequenceName = "sq_enderecoproduto")
public class Enderecoproduto {
	
	// Constantes
	public static final Integer IS_UMA = new Integer(1);
	public static final Long EXCLUIR_ENDERECO_PRODUTO = new Long(0);
	
	// Variaveis de instancia
	protected Integer cdenderecoproduto;
	protected Endereco endereco;
	protected Produto produto;
	protected Long qtde;
	protected Long qtdereservadaentrada;
	protected Long qtdereservadasaida;	
	protected Date dtentrada;
	protected Boolean uma;
	
	// Variaveis transientes
	protected Long qtdedestino;
	protected Area areadestino;
	protected Integer cdenderecodestino;
	protected Integer ruadestino;
	protected Integer prediodestino;
	protected Integer niveldestino;
	protected Integer aptodestino;
	protected Long qtdeatual;
	protected Boolean isPicking;
	protected Boolean atualizar;
	protected Produtoembalagem produtoembalagem;
	
	protected String cdsenderecosprodutos;
	
	// Construtores
	public Enderecoproduto() {

	}
	
	public Enderecoproduto(Integer cd, Long qtde) {
		this.cdenderecoproduto = cd;
		this.qtde = qtde;
	}

	public Enderecoproduto(Integer cd) {
		this.cdenderecoproduto = cd;
	}
	
	// Metodos get e set
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_enderecoproduto")
	@DescriptionProperty
	public Integer getCdenderecoproduto() {
		return cdenderecoproduto;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdendereco")
	@DisplayName("Endereço")
	public Endereco getEndereco() {
		return endereco;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	@DisplayName("Produto")
	public Produto getProduto() {
		return produto;
	}
	
	@DisplayName("Quantidade")
	public Long getQtde() {
		return qtde;
	}
	
	@DisplayName("Quantidade reservada entrada")
	public Long getQtdereservadaentrada() {
		return qtdereservadaentrada;
	}
	
	@DisplayName("Quantidade reservada saída")
	public Long getQtdereservadasaida() {
		return qtdereservadasaida;
	}
	
	@DisplayName("Entrada")
	public Date getDtentrada() {
		return dtentrada;
	}
	
	public Boolean getUma() {
		return uma;
	}
	
	public void setCdenderecoproduto(Integer cdenderecoproduto) {
		this.cdenderecoproduto = cdenderecoproduto;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	
	public void setQtdereservadaentrada(Long qtdereservadaentrada) {
		this.qtdereservadaentrada = qtdereservadaentrada;
	}
	
	public void setQtdereservadasaida(Long qtdereservadasaida) {
		this.qtdereservadasaida = qtdereservadasaida;
	}
	
	public void setDtentrada(Date dtentrada) {
		this.dtentrada = dtentrada;
	}
	
	public void setUma(Boolean uma) {
		this.uma = uma;
	}
	
	// Metodos get e set das variaveis transientes
	@Transient
	public Long getQtdedestino() {
		return qtdedestino;
	}

	@Transient
	public Area getAreadestino() {
		return areadestino;
	}

	@Transient	
	public Integer getCdenderecodestino() {
		return cdenderecodestino;
	}
	
	@Transient
	@MaxLength(3)
	public Integer getRuadestino() {
		return ruadestino;
	}

	@Transient
	@MaxLength(3)
	public Integer getPrediodestino() {
		return prediodestino;
	}

	@Transient
	@MaxLength(2)
	public Integer getNiveldestino() {
		return niveldestino;
	}

	@Transient
	@MaxLength(3)
	public Integer getAptodestino() {
		return aptodestino;
	}
	
	@Transient
	@MaxLength(10)
	public Long getQtdeatual() {
		return qtdeatual;
	}
	
	@Transient
	public Boolean getIsPicking() {
		return isPicking;
	}
	
	@Transient
	public Boolean getAtualizar() {
		return atualizar;
	}
	
	@Transient
	public String getCdsenderecosprodutos() {
		return cdsenderecosprodutos;
	}
	
	public void setCdsenderecosprodutos(String cdsenderecosprodutos) {
		this.cdsenderecosprodutos = cdsenderecosprodutos;
	}
	
	public void setQtdedestino(Long qtdedestino) {
		this.qtdedestino = qtdedestino;
	}
	
	public void setAreadestino(Area areadestino) {
		this.areadestino = areadestino;
	}

	public void setCdenderecodestino(Integer cdenderecodestino) {
		this.cdenderecodestino = cdenderecodestino;
	}
	
	public void setRuadestino(Integer ruadestino) {
		this.ruadestino = ruadestino;
	}

	public void setPrediodestino(Integer prediodestino) {
		this.prediodestino = prediodestino;
	}

	public void setNiveldestino(Integer niveldestino) {
		this.niveldestino = niveldestino;
	}

	public void setAptodestino(Integer aptodestino) {
		this.aptodestino = aptodestino;
	}
	
	public void setQtdeatual(Long qtdeatual) {
		this.qtdeatual = qtdeatual;
	}
	
	public void setIsPicking(Boolean isPicking) {
		this.isPicking = isPicking;
	}
	
	public void setAtualizar(Boolean atualizar) {
		this.atualizar = atualizar;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Enderecoproduto) {
			Enderecoproduto enderecoproduto = (Enderecoproduto) obj;
			if(enderecoproduto!=null && enderecoproduto.getCdenderecoproduto()!=null 
					&& this.getCdenderecoproduto()!=null){
				return enderecoproduto.getCdenderecoproduto().equals(this.getCdenderecoproduto());
			}
		}
		
		return super.equals(obj);
	}

	@Transient
	public Produtoembalagem getProdutoembalagem() {
		return produtoembalagem;
	}

	public void setProdutoembalagem(Produtoembalagem produtoembalagem) {
		this.produtoembalagem = produtoembalagem;
	}
}
