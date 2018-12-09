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
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_produtotipopalete", sequenceName = "sq_produtotipopalete")
public class Produtotipopalete {

	protected Integer cdprodutotipopalete;
	protected Produto produto;
	protected Deposito deposito;
	protected Tipopalete tipopalete;
	protected Long lastro;
	protected Long camada;
	protected Boolean padrao;
	protected Long normaPaletizacao;
	protected Double pesoDoPalete;
	
	//transiente para controle
	protected Boolean alterado;

	// construores
	public Produtotipopalete() {

	}
	
	public Produtotipopalete(Integer cd) {
		this.cdprodutotipopalete = cd;
	}
	
	public Produtotipopalete(Integer cd, Long lastro, Long camada) {
		this.cdprodutotipopalete = cd;
		this.lastro = lastro;
		this.camada = camada;
	}
	
	// métodos get e set	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_produtotipopalete")
	public Integer getCdprodutotipopalete() {
		return cdprodutotipopalete;
	}
	public void setCdprodutotipopalete(Integer id) {
		this.cdprodutotipopalete = id;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	@Required
	public Produto getProduto() {
		return produto;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipopalete")
	@DisplayName("Tipo de palete")
	@Required
	public Tipopalete getTipopalete() {
		return tipopalete;
	}
	
	@Required
	@MaxLength(10)
	public Long getLastro() {
		return lastro;
	}
	
	@Required
	@MaxLength(10)
	public Long getCamada() {
		return camada;
	}
	
	public Boolean getPadrao() {
		return padrao;
	}
	

	@DisplayName("Norma de paletização")
	@Transient
	public Long getNormaPaletizacao() {
		return normaPaletizacao;
	}
	
	@DisplayName("Peso do palete(Kg)")
	@Transient
	public Double getPesoDoPalete() {
		return pesoDoPalete;
	}

	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setTipopalete(Tipopalete tipopalete) {
		this.tipopalete = tipopalete;
	}
	
	public void setLastro(Long lastro) {
		this.lastro = lastro;
	}
	
	public void setCamada(Long camada) {
		this.camada = camada;
	}
	
	public void setPadrao(Boolean padrao) {
		this.padrao = padrao;
	}

	public void setNormaPaletizacao(Long normaPaletizacao) {
		this.normaPaletizacao = normaPaletizacao;
	}
	public void setPesoDoPalete(Double pesoDoPalete) {
		this.pesoDoPalete = pesoDoPalete;
	}
	
	//transiente
	@Transient
	public Boolean getAlterado() {
		return alterado;
	}
	
	public void setAlterado(Boolean alterado) {
		this.alterado = alterado;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Produtotipopalete)
			if(obj != null)
				return (((Produtotipopalete)obj).getCdprodutotipopalete()).equals(this.cdprodutotipopalete);
		return super.equals(obj);
	}

}
