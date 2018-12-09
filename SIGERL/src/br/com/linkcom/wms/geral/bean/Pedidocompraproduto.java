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
import br.com.linkcom.neo.validation.annotation.Required;


@Entity
@SequenceGenerator(name = "sq_pedidocompraproduto", sequenceName = "sq_pedidocompraproduto")
public class Pedidocompraproduto {

	protected Integer cdpedidocompraproduto;
	protected Pedidocompra pedidocompra;
	protected Produto produto;
	protected Integer qtde;
	protected Money valor;
	//Transiente
	protected Integer qtdetotal;
	protected Integer qtdedisponivel;
	protected Integer qtdealiberar;
	protected Date dataAgendamento;
	protected Boolean included;
	protected Integer qtdeAgendada;
	protected List<Pedidocompraprodutolibera> listaPedidocompraprodutolibera = new ListSet<Pedidocompraprodutolibera>(Pedidocompraprodutolibera.class);
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pedidocompraproduto")
	public Integer getCdpedidocompraproduto() {
		return cdpedidocompraproduto;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpedidocompra")
	public Pedidocompra getPedidocompra() {
		return pedidocompra;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	public Produto getProduto() {
		return produto;
	}
	
	@DisplayName("Quantidade agendada")
	@Required
	public Integer getQtde() {
		return qtde;
	}	
	
	@Transient
	@DisplayName("Quantidade total")
	public Integer getQtdetotal() {
		return qtdetotal;
	}
	
	@Transient
	@DisplayName("Quantidade disponivel")
	public Integer getQtdedisponivel() {
		return qtdedisponivel;
	}
	
	@DisplayName("Data do agendamento")
	@Required
	@Transient
	public Date getDataAgendamento() {
		return dataAgendamento;
	}
	
	@Transient
	public Boolean getIncluded() {
		return included;
	}
	
	@Transient
	public Integer getQtdeAgendada() {
		return qtdeAgendada;
	}
	
	@Transient
	@DisplayName("Quantidade a ser liberada")
	public Integer getQtdealiberar() {
		return qtdealiberar;
	}
	
	
	@DisplayName("Valor unitário")
	public Money getValor() {
		return valor;
	}

	public void setValor(Money valor) {
		this.valor = valor;
	}


	public void setQtdealiberar(Integer qtdealiberar) {
		this.qtdealiberar = qtdealiberar;
	}


	public void setCdpedidocompraproduto(Integer cdpedidocompraproduto) {
		this.cdpedidocompraproduto = cdpedidocompraproduto;
	}
	public void setPedidocompra(Pedidocompra pedidocompra) {
		this.pedidocompra = pedidocompra;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}	
	
	public void setQtdetotal(Integer qtdtotal) {
		this.qtdetotal = qtdtotal;
	}
	
	public void setQtdedisponivel(Integer qtdedisponivel) {
		this.qtdedisponivel = qtdedisponivel;
	}
	public void setDataAgendamento(Date dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}
	public void setIncluded(Boolean included) {
		this.included = included;
	}
	
	public void setQtdeAgendada(Integer qtdeAgendada) {
		this.qtdeAgendada = qtdeAgendada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cdpedidocompraproduto == null) ? 0 : cdpedidocompraproduto
						.hashCode());
		return result;
	}
	
	@OneToMany(mappedBy="pedidocompraproduto")
	public List<Pedidocompraprodutolibera> getListaPedidocompraprodutolibera() {
		return listaPedidocompraprodutolibera;
	}
	
	public void setListaPedidocompraprodutolibera(
			List<Pedidocompraprodutolibera> listaPedidocompraprodutolibera) {
		this.listaPedidocompraprodutolibera = listaPedidocompraprodutolibera;
	}
	
	@Transient
	public Integer getQtdeliberada(){
		Integer total = 0;
		
		if(this.getListaPedidocompraprodutolibera() != null && this.getListaPedidocompraprodutolibera().size() > 0){
			for (Pedidocompraprodutolibera item : this.getListaPedidocompraprodutolibera()) {
				if(item.getQtdeliberada() != null)
					total += item.getQtdeliberada();
			}
		}
		return total;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Pedidocompraproduto other = (Pedidocompraproduto) obj;
		if (cdpedidocompraproduto == null) {
			if (other.cdpedidocompraproduto != null)
				return false;
		} else if (!cdpedidocompraproduto.equals(other.cdpedidocompraproduto))
			return false;
		return true;
	}
	
}
