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
@SequenceGenerator(name = "sq_etiquetaexpedicaogrupo", sequenceName = "sq_etiquetaexpedicaogrupo")
public class Etiquetaexpedicaogrupo {

	protected Integer cdetiquetaexpedicaogrupo;
	protected Ordemservicoproduto ordemservicoproduto;
	protected Carregamentoitem carregamentoitem;
	protected Etiquetaexpedicao etiquetaexpedicao;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_etiquetaexpedicaogrupo")
	public Integer getCdetiquetaexpedicaogrupo() {
		return cdetiquetaexpedicaogrupo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdordemservicoproduto")
	public Ordemservicoproduto getOrdemservicoproduto() {
		return ordemservicoproduto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdcarregamentoitem")
	public Carregamentoitem getCarregamentoitem() {
		return carregamentoitem;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdetiquetaexpedicao")
	public Etiquetaexpedicao getEtiquetaexpedicao() {
		return etiquetaexpedicao;
	}

	public void setCdetiquetaexpedicaogrupo(Integer cdetiquetaexpedicaogrupo) {
		this.cdetiquetaexpedicaogrupo = cdetiquetaexpedicaogrupo;
	}

	public void setOrdemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		this.ordemservicoproduto = ordemservicoproduto;
	}

	public void setCarregamentoitem(Carregamentoitem carregamentoitem) {
		this.carregamentoitem = carregamentoitem;
	}
	
	public void setEtiquetaexpedicao(Etiquetaexpedicao etiquetaexpedicao) {
		this.etiquetaexpedicao = etiquetaexpedicao;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Etiquetaexpedicaogrupo) {
			Integer cdetiqueta = ((Etiquetaexpedicaogrupo) obj)
					.getCdetiquetaexpedicaogrupo();
			if (this.cdetiquetaexpedicaogrupo == null || cdetiqueta == null)
				return cdetiqueta == this.cdetiquetaexpedicaogrupo;
			else
				return this.cdetiquetaexpedicaogrupo.equals(cdetiqueta);
		}
		return super.equals(obj);
	}
}
