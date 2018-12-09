package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.Required;

/**
 * 
 * @author Arantes
 *
 */

@Entity
@SequenceGenerator(name="sq_descargaprecoveiculo", sequenceName="sq_descargaprecoveiculo")
public class Descargaprecoveiculo {
	
	protected Integer cddescargaprecoveiculo;
	protected Descargapreco descargapreco;
	protected Tipoveiculo tipoveiculo;
	protected Money valor;
	
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_descargaprecoveiculo")
	@Id
	public Integer getCddescargaprecoveiculo() {
		return cddescargaprecoveiculo;
	}
	
	@JoinColumn(name="cddescargapreco")
	@ManyToOne(fetch=FetchType.LAZY)
	public Descargapreco getDescargapreco() {
		return descargapreco;
	}
	
	@JoinColumn(name="cdtipoveiculo")
	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName("Tipo de veículo")
	@Required
	@DescriptionProperty
	public Tipoveiculo getTipoveiculo() {
		return tipoveiculo;
	}
	
	@Required
	@DisplayName("Valor")
	public Money getValor() {
		return valor;
	}
	
	public void setCddescargaprecoveiculo(Integer cddescargaprecoveiculo) {
		this.cddescargaprecoveiculo = cddescargaprecoveiculo;
	}
	
	public void setDescargapreco(Descargapreco descargapreco) {
		this.descargapreco = descargapreco;
	}
	
	public void setTipoveiculo(Tipoveiculo tipoveiculo) {
		this.tipoveiculo = tipoveiculo;
	}
	
	public void setValor(Money valor) {
		this.valor = valor;
	}
}