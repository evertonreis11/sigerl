package br.com.linkcom.wms.geral.bean;

import java.util.List;
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
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

/**
 * 
 * @author Guilherme Arantes de Oliveira
 *
 */
@Entity
@SequenceGenerator(name = "sq_descargapreco", sequenceName = "sq_descargapreco")
@DisplayName("Cobrança de descarga")
public class Descargapreco {
	
	protected Integer cddescargapreco;
	protected String nome;
	protected Descargatipocalculo descargatipocalculo;
	protected Money valor;
	protected List<Descargaprecoveiculo> listaDescargaPrecoVeiculo = new ListSet<Descargaprecoveiculo>(Descargaprecoveiculo.class);
	protected Set<Recebimento> listaRecebimento = new ListSet<Recebimento>(Recebimento.class);
	protected Set<Dadologistico> listaDadologistico = new ListSet<Dadologistico>(Dadologistico.class);
	protected Deposito deposito;
	
	public Descargapreco() {
	}
	
	@Id	
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_descargapreco")
	public Integer getCddescargapreco() {
		return cddescargapreco;
	}
	
	@Required
	@DescriptionProperty
	@MaxLength(30)
	@DisplayName("Nome")
	public String getNome() {
		return nome;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddescargatipocalculo")
	@DisplayName("Tipo de cálculo")
	public Descargatipocalculo getDescargatipocalculo() {
		return descargatipocalculo;
	}
	
	@Required
	@DisplayName("Valor")
	public Money getValor() {
		return valor;
	}
	
	@DisplayName("Cobrança por veículo")
	@OneToMany(mappedBy="descargapreco")
	public List<Descargaprecoveiculo> getListaDescargaPrecoVeiculo() {
		return listaDescargaPrecoVeiculo;
	}
	
	@OneToMany(mappedBy="descargapreco")
	public Set<Recebimento> getListaRecebimento() {
		return listaRecebimento;
	}
	
	@OneToMany(mappedBy="descargapreco")
	public Set<Dadologistico> getListaDadologistico() {
		return listaDadologistico;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}

	public void setCddescargapreco(Integer cddescargapreco) {
		this.cddescargapreco = cddescargapreco;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setDescargatipocalculo(Descargatipocalculo descargatipocalculo) {
		this.descargatipocalculo = descargatipocalculo;
	}
	
	public void setValor(Money valor) {
		this.valor = valor;
	}

	public void setListaDescargaPrecoVeiculo(List<Descargaprecoveiculo> listaDescargaPrecoVeiculo) {
		this.listaDescargaPrecoVeiculo = listaDescargaPrecoVeiculo;
	}
	
	public void setListaRecebimento(Set<Recebimento> listaRecebimento) {
		this.listaRecebimento = listaRecebimento;
	}

	public void setListaDadologistico(Set<Dadologistico> listaDadologistico) {
		this.listaDadologistico = listaDadologistico;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	
	
	
}