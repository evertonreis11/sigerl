package br.com.linkcom.wms.geral.bean;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;


@Entity
@SequenceGenerator(name = "sq_pedidocompraprodutolibera", sequenceName = "sq_pedidocompraprodutolibera")
public class Pedidocompraprodutolibera implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Integer cdpedidocompraprodutolibera;
	protected Pedidocompraproduto pedidocompraproduto;
	protected Integer qtdeliberada;
	protected Date dtprevisaorecebimento;
	protected Date dtprevisaofinanceiro;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pedidocompraprodutolibera")
	public Integer getCdpedidocompraprodutolibera() {
		return cdpedidocompraprodutolibera;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpedidocompraproduto")
	public Pedidocompraproduto getPedidocompraproduto() {
		return pedidocompraproduto;
	}

	
	
	
	public Integer getQtdeliberada() {
		return qtdeliberada;
	}

	public Date getDtprevisaorecebimento() {
		return dtprevisaorecebimento;
	}
	public Date getDtprevisaofinanceiro() {
		return dtprevisaofinanceiro;
	}

	public void setCdpedidocompraprodutolibera(Integer cdpedidocompraprodutolibera) {
		this.cdpedidocompraprodutolibera = cdpedidocompraprodutolibera;
	}

	public void setPedidocompraproduto(Pedidocompraproduto pedidocompraproduto) {
		this.pedidocompraproduto = pedidocompraproduto;
	}

	public void setQtdeliberada(Integer qtdeliberada) {
		this.qtdeliberada = qtdeliberada;
	}

	public void setDtprevisaorecebimento(Date dtprevisaorecebimento) {
		this.dtprevisaorecebimento = dtprevisaorecebimento;
	}
	
	public void setDtprevisaofinanceiro(Date dtprevisaofinanceiro) {
		this.dtprevisaofinanceiro = dtprevisaofinanceiro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cdpedidocompraprodutolibera == null) ? 0
						: cdpedidocompraprodutolibera.hashCode());
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
		final Pedidocompraprodutolibera other = (Pedidocompraprodutolibera) obj;
		if (cdpedidocompraprodutolibera == null) {
			if (other.cdpedidocompraprodutolibera != null)
				return false;
		} else if (!cdpedidocompraprodutolibera
				.equals(other.cdpedidocompraprodutolibera))
			return false;
		return true;
	}

	
}
