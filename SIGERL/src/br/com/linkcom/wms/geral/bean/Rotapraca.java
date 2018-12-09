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
import br.com.linkcom.neo.validation.annotation.Required;


/**
 * @author Guilherme Arantes de Oliveira
 *
 */
@Entity
@SequenceGenerator(name = "sq_rotapraca", sequenceName = "sq_rotapraca")
public class Rotapraca {
	
	protected Integer cdrotapraca;
	protected Rota rota;
	protected Praca praca;
	protected Integer ordem;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_rotapraca")
	public Integer getCdrotapraca() {
		return cdrotapraca;
	}
	
	@DisplayName("Rota")
	@JoinColumn(name="cdrota")
	@ManyToOne(fetch=FetchType.LAZY)
	public Rota getRota() {
		return rota;
	}
	
	@DisplayName("Praça")
	@JoinColumn(name="cdpraca")
	@ManyToOne(fetch=FetchType.LAZY)
	@Required
	public Praca getPraca() {
		return praca;
	}
	
	@DisplayName("Sequência de entrega")
	@Required
	public Integer getOrdem() {
		return ordem;
	}
	
	public void setCdrotapraca(Integer cdrotapraca) {
		this.cdrotapraca = cdrotapraca;
	}
	
	public void setRota(Rota rota) {
		this.rota = rota;
	}
	
	public void setPraca(Praca praca) {
		this.praca = praca;
	}
	
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}
