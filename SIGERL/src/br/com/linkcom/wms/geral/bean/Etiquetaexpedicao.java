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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_etiquetaexpedicao", sequenceName = "sq_etiquetaexpedicao")
public class Etiquetaexpedicao {
	
	protected Integer cdetiquetaexpedicao;
	protected Ordemservicoproduto ordemservicoproduto;
	protected Carregamentoitem carregamentoitem;
	protected Long qtdecoletor;
	protected Long qtdecoletororiginal;
	protected Embalagemexpedicaoproduto embalagemexpedicaoproduto;
	protected Set<Etiquetaexpedicaogrupo> listaEtiquetaexpedicaogrupo = new ListSet<Etiquetaexpedicaogrupo>(Etiquetaexpedicaogrupo.class);
	
	//Transientes
	protected Boolean readed = false;
	protected Long qtdeauxiliar;
	
	/* ------------------------------ construtores -----------------------------*/
	public Etiquetaexpedicao() {
	}
	
	public Etiquetaexpedicao(Integer cdetiquetaexpedicao) {
		this.cdetiquetaexpedicao = cdetiquetaexpedicao;
	}

	/* ------------------------------ metodos get´s e set´s -----------------------------*/
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_etiquetaexpedicao")
	public Integer getCdetiquetaexpedicao() {
		return cdetiquetaexpedicao;
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
	
	@Required
	@DisplayName("Quantidade conferida")
	public Long getQtdecoletor() {
		return qtdecoletor;
	}
	
	public Long getQtdecoletororiginal() {
		return qtdecoletororiginal;
	}
	
	@OneToOne(mappedBy = "etiquetaexpedicao", fetch=FetchType.LAZY)
	public Embalagemexpedicaoproduto getEmbalagemexpedicaoproduto() {
		return embalagemexpedicaoproduto;
	}
	
	@OneToMany(mappedBy="etiquetaexpedicao")
	public Set<Etiquetaexpedicaogrupo> getListaEtiquetaexpedicaogrupo() {
		return listaEtiquetaexpedicaogrupo;
	}
	
	public void setCdetiquetaexpedicao(Integer cdetiquetaexpedicao) {
		this.cdetiquetaexpedicao = cdetiquetaexpedicao;
	}
	
	public void setOrdemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		this.ordemservicoproduto = ordemservicoproduto;
	}
	
	public void setCarregamentoitem(Carregamentoitem carregamentoitem) {
		this.carregamentoitem = carregamentoitem;
	}
	
	public void setQtdecoletor(Long qtdecoletor) {
		this.qtdecoletor = qtdecoletor;
	}
	
	public void setQtdecoletororiginal(Long qtdecoletororiginal) {
		this.qtdecoletororiginal = qtdecoletororiginal;
	}
	
	public void setEmbalagemexpedicaoproduto(Embalagemexpedicaoproduto embalagemexpedicaoproduto) {
		this.embalagemexpedicaoproduto = embalagemexpedicaoproduto;
	}
	
	public void setListaEtiquetaexpedicaogrupo(
			Set<Etiquetaexpedicaogrupo> listaEtiquetaexpedicaogrupo) {
		this.listaEtiquetaexpedicaogrupo = listaEtiquetaexpedicaogrupo;
	}
	
	/* transientes */
	@Transient
	public Boolean getReaded() {
		return readed;
	}
	public void setReaded(Boolean readed) {
		this.readed = readed;
	}
	
	@Transient
	public Long getQtdeauxiliar() {
		return qtdeauxiliar;
	}
	
	public void setQtdeauxiliar(Long qtdeauxiliar) {
		this.qtdeauxiliar = qtdeauxiliar;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Etiquetaexpedicao){
			Integer cdetiqueta = ((Etiquetaexpedicao)obj).getCdetiquetaexpedicao();
			if(this.cdetiquetaexpedicao == null || cdetiqueta == null)
				return cdetiqueta == this.cdetiquetaexpedicao;
			else
				return this.cdetiquetaexpedicao.equals(cdetiqueta);
		}
		return super.equals(obj);
	}
}
