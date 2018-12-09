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
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_pedidovenda", sequenceName = "sq_pedidovenda")
public class Pedidovenda {

	protected Integer cdpedidovenda;
	protected Long codigoerp;
	protected Cliente cliente;
	protected java.sql.Date dtemissao;
	protected java.sql.Date dtcancelamento;
	protected java.sql.Date dtlancamento = new Date(System.currentTimeMillis());
	protected List<Pedidovendaproduto> listaPedidoVendaProduto = new ListSet<Pedidovendaproduto>(Pedidovendaproduto.class);
	protected String numero;
	protected Cliente filialemissao;
	protected Boolean troca = false;
	protected Boolean convenio = false;
	protected Empresa bandeira;
	
	//Transients
	protected String numeroPedidoMV;
	protected String numeroLojaMV;
	protected String empresaMV;

	
	/* Propriedades Transientes */
	protected Pessoaendereco enderecoentrega;

	public Pedidovenda() {
	}
	
	public Pedidovenda(int cd) {
		this.cdpedidovenda = cd;
	}
	public Pedidovenda(Integer cdpedidovenda) {
		this.cdpedidovenda = cdpedidovenda;
	}

	public Pedidovenda(Integer cdpedidovenda, String numero) {
		this.cdpedidovenda = cdpedidovenda;
		this.numero = numero;
		this.codigoerp = Long.valueOf(numero);
	}

	public Pedidovenda(Integer cdpedidovenda, String numero, Long codigoerp) {
		this.cdpedidovenda = cdpedidovenda;
		this.numero = numero;
		this.codigoerp = codigoerp;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pedidovenda")
	public Integer getCdpedidovenda() {
		return cdpedidovenda;
	}
	public void setCdpedidovenda(Integer id) {
		this.cdpedidovenda = id;
	}
	
	@Required
	@MaxLength(20)
	public Long getCodigoerp() {
		return codigoerp;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdcliente")
	public Cliente getCliente() {
		return cliente;
	}
		
	@Required
	@DisplayName("Data de emissão")
	public java.sql.Date getDtemissao() {
		return dtemissao;
	}
	@DisplayName("Data do Cancelamento")
	public java.sql.Date getDtcancelamento() {
		return dtcancelamento;
	}
	
	@OneToMany(mappedBy="pedidovenda")
	public List<Pedidovendaproduto> getListaPedidoVendaProduto() {
		return listaPedidoVendaProduto;
	}
	
	@Required
	@MaxLength(15)
	@DisplayName("Número")
	public String getNumero() {
		return numero;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdfilialemissao")
	@Required
	public Cliente getFilialemissao() {
		return filialemissao;
	}
	
	public Boolean getTroca() {
		return troca;
	}

	@Required
	@DisplayName("Data de lançamento")
	public java.sql.Date getDtlancamento() {
		return dtlancamento;
	}
	
	@Required
	@DisplayName("Endereço de entrega")
	@Transient
	public Pessoaendereco getEnderecoentrega() {
		return enderecoentrega;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdbandeira")
	public Empresa getBandeira() {
		return bandeira;
	}

	//Set's
	public void setCodigoerp(Long codigoerp) {
		this.codigoerp = codigoerp;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public void setDtemissao(java.sql.Date dtemissao) {
		this.dtemissao = dtemissao;
	}
	
	public void setDtcancelamento(java.sql.Date dtcancelamento) {
		this.dtcancelamento = dtcancelamento;
	}
	
	public void setListaPedidoVendaProduto(List<Pedidovendaproduto> listaPedidoVendaProduto) {
		this.listaPedidoVendaProduto = listaPedidoVendaProduto;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public void setFilialemissao(Cliente filialemissao) {
		this.filialemissao = filialemissao;
	}
	
	public void setTroca(Boolean troca) {
		this.troca = troca;
	}
	public void setDtlancamento(java.sql.Date dtlancamento) {
		this.dtlancamento = dtlancamento;
	}
	
	public void setEnderecoentrega(Pessoaendereco enderecoentrega) {
		this.enderecoentrega = enderecoentrega;
	}
	
	public void setBandeira(Empresa bandeira) {
		this.bandeira = bandeira;
	}

	//Transient's
	@Transient
	public String getNumeroPedidoMV() {
		return numeroPedidoMV;
	}
	@Transient
	public String getNumeroLojaMV() {
		return numeroLojaMV;
	}
	@Transient
	public String getEmpresaMV() {
		return empresaMV;
	}
	public void setNumeroPedidoMV(String numeroPedidoMV) {
		this.numeroPedidoMV = numeroPedidoMV;
	}
	public void setNumeroLojaMV(String numeroLojaMV) {
		this.numeroLojaMV = numeroLojaMV;
	}
	public void setEmpresaMV(String empresaMV) {
		this.empresaMV = empresaMV;
	}
	
}
