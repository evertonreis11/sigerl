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

@Entity
@SequenceGenerator(name = "sq_pessoa", sequenceName = "sq_pessoa")
public class Agendapedido {

	protected Integer cdagendapedido;
	protected Agenda agenda;
	protected Pedidocompra pedidocompra;
	protected Boolean parcial;

	private boolean permitirIncluirProduto = false;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_pessoa")
	public Integer getCdagendapedido() {
		return cdagendapedido;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdagenda")
	public Agenda getAgenda() {
		return agenda;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpedidocompra")
	@DisplayName("Pedido de compra")
	public Pedidocompra getPedidocompra() {
		return pedidocompra;
	}

	public Boolean getParcial() {
		return parcial;
	}
	
	@Transient
	public boolean getPermitirIncluirProduto(){
		return permitirIncluirProduto;
	}

	public void setCdagendapedido(Integer cdagendapedido) {
		this.cdagendapedido = cdagendapedido;
	}

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}

	public void setPedidocompra(Pedidocompra pedidocompra) {
		this.pedidocompra = pedidocompra;
	}

	public void setParcial(Boolean parcial) {
		this.parcial = parcial;
	}

	public void setPermitirIncluirProduto(boolean permitirIncluirProduto) {
		this.permitirIncluirProduto = permitirIncluirProduto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdagendapedido == null) ? 0 : cdagendapedido.hashCode());
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
		final Agendapedido other = (Agendapedido) obj;
		if (cdagendapedido == null) {
			if (other.cdagendapedido != null)
				return false;
		} else if (!cdagendapedido.equals(other.cdagendapedido))
			return false;
		return true;
	}
	
	
}
