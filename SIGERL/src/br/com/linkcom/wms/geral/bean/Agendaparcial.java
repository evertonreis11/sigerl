package br.com.linkcom.wms.geral.bean;

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
@SequenceGenerator(name = "sq_agendaparcial", sequenceName = "sq_agendaparcial")
public class Agendaparcial {

	protected Integer cdagendaparcial;
	protected Agenda agenda;
	protected Pedidocompraproduto pedidocompraproduto;
	protected Integer qtde;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_agendaparcial")
	public Integer getCdagendaparcial() {
		return cdagendaparcial;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdagenda")
	public Agenda getAgenda() {
		return agenda;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpedidocompraproduto")
	public Pedidocompraproduto getPedidocompraproduto() {
		return pedidocompraproduto;
	}
	
	public Integer getQtde() {
		return qtde;
	}
	
	public void setCdagendaparcial(Integer cdagendaparcial) {
		this.cdagendaparcial = cdagendaparcial;
	}
	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}
	public void setPedidocompraproduto(Pedidocompraproduto pedidocompraproduto) {
		this.pedidocompraproduto = pedidocompraproduto;
	}
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdagendaparcial == null) ? 0 : cdagendaparcial.hashCode());
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
		final Agendaparcial other = (Agendaparcial) obj;
		if (cdagendaparcial == null) {
			if (other.cdagendaparcial != null)
				return false;
		} else if (!cdagendaparcial.equals(other.cdagendaparcial))
			return false;
		return true;
	}
	
	 
	
	
}
