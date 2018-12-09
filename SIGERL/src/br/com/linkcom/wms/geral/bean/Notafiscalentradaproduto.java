package br.com.linkcom.wms.geral.bean;

import java.sql.Date;
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
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_notafiscalentradaproduto", sequenceName = "sq_notafiscalentradaproduto")
public class Notafiscalentradaproduto {

	protected Integer cdnotafiscalentradaproduto;
	protected Notafiscalentrada notafiscalentrada;
	protected Produto produto;
	protected Long qtde;
	protected Money valor;
	protected Date dtvalidade;
	protected String lote;
	
	protected List<Notafiscalenderecohistorico> listanotafiscalenderecohistorico = new ListSet<Notafiscalenderecohistorico>(Notafiscalenderecohistorico.class);
	
	// -------------------------- Transientes ---------------------------
	protected Produtoembalagem produtoembalagem;
	protected Boolean embalagemfracionada;

	@DisplayName("Data de validade")
	public Date getDtvalidade() {
		return dtvalidade;
	}
	@MaxLength(30)
	public String getLote() {
		return lote;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_notafiscalentradaproduto")
	public Integer getCdnotafiscalentradaproduto() {
		return cdnotafiscalentradaproduto;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdnotafiscalentrada")
	@Required
	public Notafiscalentrada getNotafiscalentrada() {
		return notafiscalentrada;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	@Required
	public Produto getProduto() {
		return produto;
	}
	
	@Required
	@DisplayName("Qtde.")
	public Long getQtde() {
		return qtde;
	}
	
	@OneToMany(mappedBy="notafiscalentradaproduto")
	public List<Notafiscalenderecohistorico> getListanotafiscalenderecohistorico() {
		return listanotafiscalenderecohistorico;
	}
	
	@Transient
	@DisplayName("Embalagem")
	public Produtoembalagem getProdutoembalagem() {
		return produtoembalagem;
	}
	
	@Transient
	public Boolean getEmbalagemfracionada() {
		return embalagemfracionada;
	}

	public Money getValor() {
		return valor;
	}

	public void setCdnotafiscalentradaproduto(Integer id) {
		this.cdnotafiscalentradaproduto = id;
	}
	
	public void setNotafiscalentrada(Notafiscalentrada notafiscalentrada) {
		this.notafiscalentrada = notafiscalentrada;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	
	public void setValor(Money valor) {
		this.valor = valor;
	}
	
	public void setProdutoembalagem(Produtoembalagem produtoembalagem) {
		this.produtoembalagem = produtoembalagem;
	}
	
	public void setDtvalidade(Date dtvalidade) {
		this.dtvalidade = dtvalidade;
	}
	
	public void setLote(String lote) {
		this.lote = lote;
	}
	
	public void setEmbalagemfracionada(Boolean embalagemfracionada) {
		this.embalagemfracionada = embalagemfracionada;
	}
	
	public void setListanotafiscalenderecohistorico(
			List<Notafiscalenderecohistorico> listanotafiscalenderecohistorico) {
		this.listanotafiscalenderecohistorico = listanotafiscalenderecohistorico;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Notafiscalentradaproduto){
			return ((Notafiscalentradaproduto)obj).getCdnotafiscalentradaproduto().equals(this.cdnotafiscalentradaproduto);
		}
		return super.equals(obj);
	}
}
