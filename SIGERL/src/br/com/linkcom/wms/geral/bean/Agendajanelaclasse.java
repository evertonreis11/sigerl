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
@SequenceGenerator(name = "sq_agendajanela", sequenceName = "sq_agendajanela")
public class Agendajanelaclasse {

	private Integer cdagendajanelaclasse;
	private Agendajanela agendajanela;
	private Produtoclasse produtoclasse;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_agendajanela")
	public Integer getCdagendajanelaclasse() {
		return cdagendajanelaclasse;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdagendajanela")
	public Agendajanela getAgendajanela() {
		return agendajanela;
	}

	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdprodutoclasse")
	public Produtoclasse getProdutoclasse() {
		return produtoclasse;
	}

	public void setCdagendajanelaclasse(Integer cdagendajanelaclasse) {
		this.cdagendajanelaclasse = cdagendajanelaclasse;
	}

	public void setAgendajanela(Agendajanela agendajanela) {
		this.agendajanela = agendajanela;
	}

	public void setProdutoclasse(Produtoclasse produtoclasse) {
		this.produtoclasse = produtoclasse;
	}

}
