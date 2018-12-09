package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_tipoveiculo", sequenceName = "sq_tipoveiculo")
@DisplayName("Tipo de veículo")
public class Tipoveiculo {
	
	protected Integer cdtipoveiculo;
	protected String nome;
	protected Long quantPlacasVeiculo;
	protected List<Descargaprecoveiculo> listaDescargaPrecoVeiculo = new ArrayList<Descargaprecoveiculo>();
	protected Set<Recebimento> listaRecebimento = new ListSet<Recebimento>(Recebimento.class);
	
	//transiente
	protected Integer pesobruto;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_tipoveiculo")
	public Integer getCdtipoveiculo() {
		return cdtipoveiculo;
	}
	
	@DescriptionProperty
	@MaxLength(20)
	@Required
	@DisplayName("Nome")
	public String getNome() {
		return nome;
	}
		
	@OneToMany(mappedBy="tipoveiculo")
	public List<Descargaprecoveiculo> getListaDescargaPrecoVeiculo() {
		return listaDescargaPrecoVeiculo;
	}
	
	@OneToMany(mappedBy="tipoveiculo")
	public Set<Recebimento> getListaRecebimento() {
		return listaRecebimento;
	}
	
	@MaxLength(1)
	@Required
	@DisplayName("Quantidade de Placas do Veículo")
	public Long getQuantPlacasVeiculo() {
		return quantPlacasVeiculo;
	}

	public void setQuantPlacasVeiculo(Long quantPlacasVeiculo) {
		this.quantPlacasVeiculo = quantPlacasVeiculo;
	}
	
	public void setCdtipoveiculo(Integer cdtipoveiculo) {
		this.cdtipoveiculo = cdtipoveiculo;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}	

	public void setListaDescargaPrecoVeiculo(List<Descargaprecoveiculo> listaDescargaPrecoVeiculo) {
		this.listaDescargaPrecoVeiculo = listaDescargaPrecoVeiculo;
	}
	
	public void setListaRecebimento(Set<Recebimento> listaRecebimento) {
		this.listaRecebimento = listaRecebimento;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdtipoveiculo == null) ? 0 : cdtipoveiculo.hashCode());
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
		final Tipoveiculo other = (Tipoveiculo) obj;

		if (cdtipoveiculo == null && other.cdtipoveiculo == null)
			return this == other;
		else if (cdtipoveiculo == null) {
			if (other.cdtipoveiculo != null)
				return false;
		} else if (!cdtipoveiculo.equals(other.cdtipoveiculo))
			return false;
		return true;
	}
	
}