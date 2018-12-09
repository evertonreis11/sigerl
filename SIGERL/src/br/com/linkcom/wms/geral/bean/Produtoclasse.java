package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.view.V_produtoclasse;

@Entity
@SequenceGenerator(name = "sq_produtoclasse", sequenceName = "sq_produtoclasse")
@DisplayName("Classe")
public class Produtoclasse {

	protected Integer cdprodutoclasse;
	protected String numero;
	protected String nome;
	protected Boolean controlaverba;
	
	protected V_produtoclasse v_produtoclasse;

	// Transientes
	private Agendaverba agendaverbaControle;
	private List<Agendaverba> listaAgendaverba = new ArrayList<Agendaverba>();

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_produtoclasse")
	public Integer getCdprodutoclasse() {
		return cdprodutoclasse;
	}

	@MaxLength(20)
	@DisplayName("Número")
	public String getNumero() {
		return numero;
	}

	@MaxLength(100)
	@DisplayName("Nome")
	@DescriptionProperty
	public String getNome() {
		return nome;
	}

	public Boolean getControlaverba() {
		return controlaverba;
	}

	@Transient
	public List<Agendaverba> getListaAgendaverba() {
		return listaAgendaverba;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="cdprodutoclasse", updatable=false, insertable=false)
	public V_produtoclasse getV_produtoclasse() {
		return v_produtoclasse;
	}

	public void setV_produtoclasse(V_produtoclasse v_produtoclasse) {
		this.v_produtoclasse = v_produtoclasse;
	}

	public void setCdprodutoclasse(Integer cdprodutoclasse) {
		this.cdprodutoclasse = cdprodutoclasse;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setListaAgendaverba(List<Agendaverba> listaAgendaverba) {
		this.listaAgendaverba = listaAgendaverba;
	}
	
	public void setControlaverba(Boolean controlaverba) {
		this.controlaverba = controlaverba;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdprodutoclasse == null) ? 0 : cdprodutoclasse.hashCode());
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
		final Produtoclasse other = (Produtoclasse) obj;
		if (cdprodutoclasse == null) {
			if (other.cdprodutoclasse != null)
				return false;
		} else if (!cdprodutoclasse.equals(other.cdprodutoclasse))
			return false;
		return true;
	}
	
	@Transient
	public Agendaverba getAgendaverbaControle() {
		return agendaverbaControle;
	}
	
	public void setAgendaverbaControle(Agendaverba agendaverbaControle) {
		this.agendaverbaControle = agendaverbaControle;
	}
}
