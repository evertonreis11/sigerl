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
@SequenceGenerator(name = "sq_recebimentonotafiscal", sequenceName = "sq_recebimentonotafiscal")
public class Recebimentonotafiscal {

	protected Integer cdrecebimentonotafiscal;
	protected Recebimento recebimento;
	protected Notafiscalentrada notafiscalentrada;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_recebimentonotafiscal")
	public Integer getCdrecebimentonotafiscal() {
		return cdrecebimentonotafiscal;
	}
	public void setCdrecebimentonotafiscal(Integer id) {
		this.cdrecebimentonotafiscal = id;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdrecebimento")
	@Required
	public Recebimento getRecebimento() {
		return recebimento;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdnotafiscalentrada")
	@Required
	public Notafiscalentrada getNotafiscalentrada() {
		return notafiscalentrada;
	}

	
	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}
	
	public void setNotafiscalentrada(Notafiscalentrada notafiscalentrada) {
		this.notafiscalentrada = notafiscalentrada;
	}
}
