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

import org.hibernate.annotations.Formula;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name = "sq_pedidocompra", sequenceName = "sq_pedidocompra")
public class Pedidocompra{

	protected Integer cdpedidocompra;
	protected Long codigoerp;
	protected Fornecedor fornecedor;
	protected List<Agendapedido> listaagendapedido = new ListSet<Agendapedido>(Agendapedido.class);
	protected List<Pedidocompraproduto> listapedidocompraproduto = new ListSet<Pedidocompraproduto>(Pedidocompraproduto.class);
	protected Date dtemissao;
	protected Date dtcancelamento;
	protected Deposito deposito;
	protected String numero;
	protected Pedidocomprastatus pedidocomprastatus = Pedidocomprastatus.DISPONIVEL;
	
	//Propriedade somente leitura obtidas via fórmula SQL
	protected Integer qtdetotal;
	protected Integer qtdeagenda;
	protected Integer qtdedisponivel;
	protected Integer qtdeliberada;

	protected Boolean parcialpossivel;
	
	public Pedidocompra(){}

	public Pedidocompra(Integer cdpedidocompra){
		this.cdpedidocompra = cdpedidocompra;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pedidocompra")
	public Integer getCdpedidocompra() {
		return cdpedidocompra;
	}
	
	@DescriptionProperty
	@DisplayName("Número do pedido")
	@MaxLength(10)
	public Long getCodigoerp() {
		return codigoerp;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName("Fornecedor")
	@JoinColumn(name="cdpessoa")
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	
	@DisplayName("Data de emissão")
	public Date getDtemissao() {
		return dtemissao;
	}
	
	@DisplayName("Data de cancelamento")
	public Date getDtcancelamento() {
		return dtcancelamento;
	}
	
	@OneToMany(mappedBy="pedidocompra")
	public List<Agendapedido> getListaagendapedido() {
		return listaagendapedido;
	}
	
	@OneToMany(mappedBy="pedidocompra")
	public List<Pedidocompraproduto> getListapedidocompraproduto() {
		return listapedidocompraproduto;
	}
	
	/*@DisplayName("Quantidade total")
	@Formula("(select sum(pcp.qtdeliberada) from pedidocompraproduto pcp where pcp.cdpedidocompra = cdpedidocompra)")
	public Integer getQtdetotal() {
		return qtdetotal;
	}*/
	@DisplayName("Quantidade total")
	public Integer getQtdetotal() {
		return qtdetotal;
	}
	
	/*@DisplayName("Quantidade agendada")
	@Formula("BUSCAR_QTDEAGENDA(cdpedidocompra)")
	public Integer getQtdeagenda() {
		return qtdeagenda;
	}*/
	@DisplayName("Quantidade agendada")
	public Integer getQtdeagenda() {
		return qtdeagenda;
	}
	
	/*@Transient
	@DisplayName("Quantidade disponível")
	public Integer getQtdedisponivel(){
		if (qtdeagenda != null)
			return qtdetotal - qtdeagenda;
		else
			return qtdetotal;
	}*/
	@DisplayName("Quantidade disponível")
	public Integer getQtdedisponivel() {
		return qtdedisponivel;
	}
	
	@DisplayName("Quantidade liberada")
	public Integer getQtdeliberada() {
		return qtdeliberada;
	}
	
	
	@DisplayName("Depósito")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	@DisplayName("Número do pedido")
	@MaxLength(10)
	public String getNumero() {
		return numero;
	}
	
	@DisplayName("Status")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpedidocomprastatus")
	public Pedidocomprastatus getPedidocomprastatus() {
		return pedidocomprastatus;
	}
	
	@Transient
	public Boolean getParcialpossivel() {
		return parcialpossivel;
	}
	
	public void setParcialpossivel(Boolean parcialpossivel) {
		this.parcialpossivel = parcialpossivel;
	}
	
	public void setPedidocomprastatus(Pedidocomprastatus pedidocomprastatus) {
		this.pedidocomprastatus = pedidocomprastatus;
	}
	
	public void setCdpedidocompra(Integer cdpedidocompra) {
		this.cdpedidocompra = cdpedidocompra;
	}
	
	public void setCodigoerp(Long codigoerp) {
		this.codigoerp = codigoerp;
	}
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public void setDtemissao(Date dtemissao) {
		this.dtemissao = dtemissao;
	}
	
	public void setDtcancelamento(Date dtcancelamento) {
		this.dtcancelamento = dtcancelamento;
	}
	
	public void setListaagendapedido(List<Agendapedido> listaagendapedido) {
		this.listaagendapedido = listaagendapedido;
	}
	
	public void setListapedidocompraproduto(List<Pedidocompraproduto> listapedidocompraproduto) {
		this.listapedidocompraproduto = listapedidocompraproduto;
	}
	
	public void setQtdeagenda(Integer qtdedisponivel) {
		this.qtdeagenda = qtdedisponivel;
	}
	
	public void setQtdetotal(Integer qtdeagenda) {
		this.qtdetotal = qtdeagenda;
	}
	
	public void setQtdedisponivel(Integer qtdedisponivel) {
		this.qtdedisponivel = qtdedisponivel;
	}
	
	public void setQtdeliberada(Integer qtdeliberada) {
		this.qtdeliberada = qtdeliberada;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdpedidocompra == null) ? 0 : cdpedidocompra.hashCode());
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
		final Pedidocompra other = (Pedidocompra) obj;
		if (cdpedidocompra == null) {
			if (other.cdpedidocompra != null)
				return false;
		} else if (!cdpedidocompra.equals(other.cdpedidocompra))
			return false;
		return true;
	}
	
	
	
}
