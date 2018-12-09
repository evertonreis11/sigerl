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

@Entity
@SequenceGenerator(name = "sq_agendaacompanhamentoveiculo", sequenceName = "sq_agendaacompanhamentoveiculo")
public class Agendaacompanhamentoveiculo {

	
	protected Integer cdagendaacompanhamentoveiculo;
	protected Integer cdagenda;
	//protected Integer  cdnotafiscalentrada;
	protected Acompanhamentoveiculo acompanhamentoveiculo;
	//protected Agenda agenda;
	///protected Notafiscalentrada notafiscalentrada;

	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_agendaacompanhamentoveiculo")
	public Integer getCdagendaacompanhamentoveiculo() {
		return cdagendaacompanhamentoveiculo;
	}
	
	
	

	@Required
	@DisplayName("Agenda")
	public Integer getCdagenda() {
		return cdagenda;
	} 
	
	//public Integer getCdnotafiscalentrada() {
	//	return cdnotafiscalentrada;
//	} 
	
	
	/*@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdagenda")
	@Required
	public Agenda getAgenda() {
		return agenda;
	}
	*/
	
	/*@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdnotafiscalentrada")
	@Required
	public Notafiscalentrada getNotafiscalentrada() {
		return notafiscalentrada;
	}
	*/
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdacompanhamentoveiculo")
	@Required
	public Acompanhamentoveiculo getAcompanhamentoveiculo() {
		return acompanhamentoveiculo;
	}
	
	public void setCdagendaacompanhamentoveiculo(
			Integer cdagendaacompanhamentoveiculo) {
		this.cdagendaacompanhamentoveiculo = cdagendaacompanhamentoveiculo;
	}
	
//	public void setCdnotafiscalentrada(Integer cdnotafiscalentrada) {
//		this.cdnotafiscalentrada = cdnotafiscalentrada;
	//}
	
	public void setCdagenda(Integer cdagenda) {
		this.cdagenda = cdagenda;
	}
	
	public void setAcompanhamentoveiculo(Acompanhamentoveiculo acompanhamentoveiculo) {
		this.acompanhamentoveiculo = acompanhamentoveiculo;
	}
	
	/*public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}*/
	
	/*public void setNotafiscalentrada(Notafiscalentrada notafiscalentrada) {
		this.notafiscalentrada = notafiscalentrada;
	}*/
	
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Agendaacompanhamentoveiculo){
			return ((Agendaacompanhamentoveiculo)obj).getCdagendaacompanhamentoveiculo().equals(this.cdagendaacompanhamentoveiculo);
		}
		return super.equals(obj);
	}
	
}
