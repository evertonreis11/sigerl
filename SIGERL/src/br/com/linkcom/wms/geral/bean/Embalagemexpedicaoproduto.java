package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@SequenceGenerator(name = "sq_embalagemexpedicaoproduto", sequenceName = "sq_embalagemexpedicaoproduto")
public class Embalagemexpedicaoproduto {

	private Integer cdembalagemexpedicaoproduto;
	private Embalagemexpedicao embalagemexpedicao;
	private Etiquetaexpedicao etiquetaexpedicao;

	public Embalagemexpedicaoproduto() {}

	public Embalagemexpedicaoproduto(Etiquetaexpedicao etiquetaexpedicao) {
		this.etiquetaexpedicao = etiquetaexpedicao;
	}

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_embalagemexpedicaoproduto")
	public Integer getCdembalagemexpedicaoproduto() {
		return cdembalagemexpedicaoproduto;
	}
	
	@DisplayName("Embalagem")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdembalagemexpedicao")
	public Embalagemexpedicao getEmbalagemexpedicao() {
		return embalagemexpedicao;
	}
	
	@DisplayName("Etiqueta")
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cdetiquetaexpedicao", referencedColumnName="cdetiquetaexpedicao")
	public Etiquetaexpedicao getEtiquetaexpedicao() {
		return etiquetaexpedicao;
	}

	public void setCdembalagemexpedicaoproduto(
			Integer cdembalagemexpedicaoproduto) {
		this.cdembalagemexpedicaoproduto = cdembalagemexpedicaoproduto;
	}
	
	public void setEmbalagemexpedicao(Embalagemexpedicao embalagemexpedicao) {
		this.embalagemexpedicao = embalagemexpedicao;
	}
	
	public void setEtiquetaexpedicao(Etiquetaexpedicao etiquetaexpedicao) {
		this.etiquetaexpedicao = etiquetaexpedicao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cdembalagemexpedicaoproduto == null) ? 0
						: cdembalagemexpedicaoproduto.hashCode());
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
		final Embalagemexpedicaoproduto other = (Embalagemexpedicaoproduto) obj;
		if (cdembalagemexpedicaoproduto == null) {
			if (other.cdembalagemexpedicaoproduto != null)
				return false;
		} else if (!cdembalagemexpedicaoproduto
				.equals(other.cdembalagemexpedicaoproduto))
			return false;
		return true;
	}

}
