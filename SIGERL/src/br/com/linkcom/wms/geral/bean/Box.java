package br.com.linkcom.wms.geral.bean;

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

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_box", sequenceName = "sq_box")
public class Box {
	
	protected Integer cdbox;
	protected String nome;
	protected Deposito deposito;
	protected Boxstatus boxstatus;
	protected Double cubagem;
	
	protected List<Expedicao> listaExpedicoes = new ListSet<Expedicao>(Expedicao.class);
	protected List<Carregamento> listaCarregamentos = new ListSet<Carregamento>(Carregamento.class);
	
	//TRANSIENT's
	protected Boolean expedicaoemandamento;
	
	public Box(Integer cd, String nome) {
		this.cdbox = cd;
		this.nome = nome;
	}
	public Box(Integer cd) {
		this.cdbox = cd;
	}
	public Box() {
	}
	
	@Id	
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_box")
	public Integer getCdbox() {
		return cdbox;
	}
	@MaxLength(5)
	@Required
	@DisplayName("Nome (Número do box)")
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName("Depósito")
	@JoinColumn(name="cddeposito")
	@Required
	public Deposito getDeposito() {
		return deposito;
	}
	@Required
	@DisplayName("Status do box")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdboxstatus")
	public Boxstatus getBoxstatus() {
		return boxstatus;
	}
	@OneToMany(mappedBy="box")
	public List<Expedicao> getListaExpedicoes() {
		return listaExpedicoes;
	}
	@OneToMany(mappedBy="box")
	public List<Carregamento> getListaCarregamentos() {
		return listaCarregamentos;
	}
	public Double getCubagem() {
		return cubagem;
	}
	
	public void setCubagem(Double cubagem) {
		this.cubagem = cubagem;
	}
	public void setListaCarregamentos(List<Carregamento> listaCarregamentos) {
		this.listaCarregamentos = listaCarregamentos;
	}
	public void setListaExpedicoes(List<Expedicao> listaExpedicoes) {
		this.listaExpedicoes = listaExpedicoes;
	}
	public void setCdbox(Integer cdbox) {
		this.cdbox = cdbox;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setBoxstatus(Boxstatus boxstatus) {
		this.boxstatus = boxstatus;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdbox == null) ? 0 : cdbox.hashCode());
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
		final Box other = (Box) obj;
		if (cdbox == null) {
			if (other.cdbox != null)
				return false;
		} else if (!cdbox.equals(other.cdbox))
			return false;
		return true;
	}
	
	@Transient
	public Boolean getExpedicaoemandamento() {
		return expedicaoemandamento;
	}
	public void setExpedicaoemandamento(Boolean expedicaoemandamento) {
		this.expedicaoemandamento = expedicaoemandamento;
	}
	
}
