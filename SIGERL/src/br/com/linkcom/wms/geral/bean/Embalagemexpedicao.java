package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
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

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_embalagemexpedicao", sequenceName = "sq_embalagemexpedicao")
public class Embalagemexpedicao {

	private Integer cdembalagemexpedicao;
	private Ordemservico ordemservico;
	private String lacre;
	private List<Embalagemexpedicaoproduto> listaEmbalagemexpedicaoproduto = new ArrayList<Embalagemexpedicaoproduto>();
	private Boolean conferida = false;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_embalagemexpedicao")
	public Integer getCdembalagemexpedicao() {
		return cdembalagemexpedicao;
	}
	
	@DisplayName("Ordem de serviço")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdordemservico")
	public Ordemservico getOrdemservico() {
		return ordemservico;
	}
	
	@Required
	@MaxLength(20)
	public String getLacre() {
		return lacre;
	}
	
	@OneToMany(mappedBy="embalagemexpedicao")
	@JoinColumn(name="cdembalagemexpedicao")
	public List<Embalagemexpedicaoproduto> getListaEmbalagemexpedicaoproduto() {
		return listaEmbalagemexpedicaoproduto;
	}
	
	public Boolean getConferida() {
		return conferida;
	}

	public void setCdembalagemexpedicao(
			Integer cdembalagemexpedicao) {
		this.cdembalagemexpedicao = cdembalagemexpedicao;
	}
	
	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}
	
	public void setLacre(String lacre) {
		this.lacre = lacre;
	}
	
	public void setListaEmbalagemexpedicaoproduto(
			List<Embalagemexpedicaoproduto> listaEmbalagemexpedicaoproduto) {
		this.listaEmbalagemexpedicaoproduto = listaEmbalagemexpedicaoproduto;
	}
	
	public void setConferida(Boolean conferida) {
		this.conferida = conferida;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cdembalagemexpedicao == null) ? 0
						: cdembalagemexpedicao.hashCode());
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
		final Embalagemexpedicao other = (Embalagemexpedicao) obj;
		if (cdembalagemexpedicao == null) {
			if (other.cdembalagemexpedicao != null)
				return false;
		} else if (!cdembalagemexpedicao
				.equals(other.cdembalagemexpedicao))
			return false;
		return true;
	}

}
