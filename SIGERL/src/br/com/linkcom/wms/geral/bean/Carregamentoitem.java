package br.com.linkcom.wms.geral.bean;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;

@Entity
@SequenceGenerator(name = "sq_carregamentoitem", sequenceName = "sq_carregamentoitem")
public class Carregamentoitem {

	protected Integer cdcarregamentoitem;
	protected Carregamento carregamento;
	protected Long ordem;
	protected Long qtdeconfirmada;
	protected Pedidovendaproduto pedidovendaproduto;
	protected Set<Etiquetaexpedicao> listaEtiquetaexpedicao = new ListSet<Etiquetaexpedicao>(Etiquetaexpedicao.class);
	protected Set<Etiquetaexpedicaogrupo> listaEtiquetaexpedicaogrupo = new ListSet<Etiquetaexpedicaogrupo>(Etiquetaexpedicaogrupo.class);

	public Carregamentoitem(Integer cd) {
		this.cdcarregamentoitem = cd;
	}
	
	public Carregamentoitem() {
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_carregamentoitem")
	public Integer getCdcarregamentoitem() {
		return cdcarregamentoitem;
	}
	public void setCdcarregamentoitem(Integer id) {
		this.cdcarregamentoitem = id;
	}

	@DescriptionProperty
	@DisplayName("Quantidade confirmada")
	public Long getQtdeconfirmada() {
		return qtdeconfirmada;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdcarregamento")
	public Carregamento getCarregamento() {
		return carregamento;
	}
	
	public Long getOrdem() {
		return ordem;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cdpedidovendaproduto")
	public Pedidovendaproduto getPedidovendaproduto() {
		return pedidovendaproduto;
	}
	
	@OneToMany(mappedBy="carregamentoitem")
	public Set<Etiquetaexpedicao> getListaEtiquetaexpedicao() {
		return listaEtiquetaexpedicao;
	}
	
	@OneToMany(mappedBy="carregamentoitem")
	public Set<Etiquetaexpedicaogrupo> getListaEtiquetaexpedicaogrupo() {
		return listaEtiquetaexpedicaogrupo;
	}
	
	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}
	
	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}
	
	public void setPedidovendaproduto(Pedidovendaproduto pedidovendaproduto) {
		this.pedidovendaproduto = pedidovendaproduto;
	}
	
	public void setListaEtiquetaexpedicao(Set<Etiquetaexpedicao> listaEtiquetaexpedicao) {
		this.listaEtiquetaexpedicao = listaEtiquetaexpedicao;
	}
	
	public void setListaEtiquetaexpedicaogrupo(Set<Etiquetaexpedicaogrupo> listaEtiquetaexpedicaogrupo) {
		this.listaEtiquetaexpedicaogrupo = listaEtiquetaexpedicaogrupo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cdcarregamentoitem == null) ? 0 : cdcarregamentoitem
						.hashCode());
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
		final Carregamentoitem other = (Carregamentoitem) obj;
		if (cdcarregamentoitem == null) {
			if (other.cdcarregamentoitem != null)
				return false;
		} else if (!cdcarregamentoitem.equals(other.cdcarregamentoitem))
			return false;
		return true;
	}

	public void setQtdeconfirmada(Long qtdeconfirmada) {
		this.qtdeconfirmada = qtdeconfirmada;
	}
}
