package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;
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

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.Required;

import com.ibm.icu.text.SimpleDateFormat;

@Entity
@SequenceGenerator(name = "sq_expedicao", sequenceName = "sq_expedicao")
@NamedNativeQueries(value={
	@NamedNativeQuery(name="baixar_estoque_expedicao", query="{ call BAIXAR_ESTOQUE_EXPEDICAO(?) }", resultSetMapping="scalar"),
	@NamedNativeQuery(name="enderecar_mapas_expedicao", query="{ call ENDERECAR_MAPAS_EXPEDICAO(?) }", resultSetMapping="scalar")
})
@DisplayName("Expedição")
public class Expedicao {
	
	protected Integer cdexpedicao;
	protected Box box;
	protected Expedicaostatus expedicaostatus;
	protected Timestamp dtexpedicao;
	protected Timestamp dtfimexpedicao;
	protected List<Ordemservico> listaOrdensservico = new ListSet<Ordemservico>(Ordemservico.class);
	protected List<Carregamento> listaCarregamento = new ListSet<Carregamento>(Carregamento.class);
	
	//TRANSIENTS
	protected String dtexpedicaoString;
	protected List<Carregamentohistorico> listaCarregamentohistorico =  new ListSet<Carregamentohistorico>(Carregamentohistorico.class);
	
	public Expedicao(){
	}

	public Expedicao(Integer cdexpedicao){
		this.cdexpedicao = cdexpedicao;
	}

	public Expedicao(Box box, Timestamp dtexpedicao, Expedicaostatus expedicaostatus){
		this.box = box;
		this.dtexpedicao = dtexpedicao;
		this.expedicaostatus = expedicaostatus;
	}
	
	@Id	
	@DisplayName("Expedição")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_expedicao")
	public Integer getCdexpedicao() {
		return cdexpedicao;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdbox")
	@Required
	public Box getBox() {
		return box;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdexpedicaostatus")
	@Required
	@DisplayName("Status da expedição")
	public Expedicaostatus getExpedicaostatus() {
		return expedicaostatus;
	}

	@DisplayName("Data expedição")
	@Required
	public Timestamp getDtexpedicao() {
		return dtexpedicao;
	}

	@DisplayName("Data fim expedição")
	public Timestamp getDtfimexpedicao() {
		return dtfimexpedicao;
	}

	public void setCdexpedicao(Integer cdexpedicao) {
		this.cdexpedicao = cdexpedicao;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public void setDtexpedicao(Timestamp dtexpedicao) {
		this.dtexpedicao = dtexpedicao;
	}

	public void setDtfimexpedicao(Timestamp dtfimexpedicao) {
		this.dtfimexpedicao = dtfimexpedicao;
	}
	
	public void setExpedicaostatus(Expedicaostatus expedicaostatus) {
		this.expedicaostatus = expedicaostatus;
	}
	
	@OneToMany(mappedBy="expedicao")
	public List<Ordemservico> getListaOrdensservico() {
		return listaOrdensservico;
	}
	
	public void setListaOrdensservico(List<Ordemservico> listaOrdensservico) {
		this.listaOrdensservico = listaOrdensservico;
	}
	
	@OneToMany(mappedBy="expedicao")
	public List<Carregamento> getListaCarregamento() {
		return listaCarregamento;
	}
	
	public void setListaCarregamento(List<Carregamento> listaCarregamento) {
		this.listaCarregamento = listaCarregamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdexpedicao == null) ? 0 : cdexpedicao.hashCode());
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
		final Expedicao other = (Expedicao) obj;
		if (cdexpedicao == null) {
			if (other.cdexpedicao != null)
				return false;
		} else if (!cdexpedicao.equals(other.cdexpedicao))
			return false;
		return true;
	}
	
	@Transient
	public String getDtexpedicaoString() {
		if(this.dtexpedicao != null)
			return new SimpleDateFormat("dd/MM/yyyy").format(this.dtexpedicao);
		return dtexpedicaoString;
	}
	public void setDtexpedicaoString(String dtexpedicaoString) {
		this.dtexpedicaoString = dtexpedicaoString;
	}

	@Transient
	public List<Carregamentohistorico> getListaCarregamentohistorico() {
		return listaCarregamentohistorico;
	}

	public void setListaCarregamentohistorico(List<Carregamentohistorico> listaCarregamentohistorico) {
		this.listaCarregamentohistorico = listaCarregamentohistorico;
	}
	
	
	
}
