package br.com.linkcom.wms.geral.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Money;

@Entity
@SequenceGenerator(name = "sq_pedidocomprafinanceiro", sequenceName = "sq_pedidocomprafinanceiro")
public class Pedidocomprafinanceiro {

	private Integer cdpedidocomprafinanceiro;
	private Pedidocompra pedidocompra;
	private Date dtprevisao;
	private Money valor;
	private Long codigoerp;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pedidocomprafinanceiro")
	public Integer getCdpedidocomprafinanceiro() {
		return cdpedidocomprafinanceiro;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpedidocompra")
	public Pedidocompra getPedidocompra() {
		return pedidocompra;
	}

	@DisplayName("Previsão")
	public Date getDtprevisao() {
		return dtprevisao;
	}

	public Money getValor() {
		return valor;
	}
	
	public Long getCodigoerp() {
		return codigoerp;
	}

	public void setCdpedidocomprafinanceiro(Integer cdpedidocomprafinanceiro) {
		this.cdpedidocomprafinanceiro = cdpedidocomprafinanceiro;
	}

	public void setPedidocompra(Pedidocompra pedidocompra) {
		this.pedidocompra = pedidocompra;
	}

	public void setDtprevisao(Date dtprevisao) {
		this.dtprevisao = dtprevisao;
	}

	public void setValor(Money valor) {
		this.valor = valor;
	}
	
	public void setCodigoerp(Long codigoerp) {
		this.codigoerp = codigoerp;
	}

}
