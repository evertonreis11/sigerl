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

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;

@Entity
@SequenceGenerator(name="sq_reabastecimento",sequenceName="sq_reabastecimento")
@NamedNativeQueries(value={
	@NamedNativeQuery(name="iniciar_reabastecimento", query="{ call INICIAR_REABASTECIMENTO(?) }", resultSetMapping="scalar")
})
public class Reabastecimento {
	
	private Integer cdreabastecimento;
	private Deposito deposito;
	private Timestamp dtreabastecimento;
	private Reabastecimentostatus reabastecimentostatus;
	private List<Reabastecimentolote> listaReabastecimentolote = new ListSet<Reabastecimentolote>(Reabastecimentolote.class);

	@Id
	@GeneratedValue(generator="sq_reabastecimento",strategy = GenerationType.AUTO)
	@DisplayName("Nº Reabastecimento")
	public Integer getCdreabastecimento() {
		return cdreabastecimento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")
	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}

	@DisplayName("Data")
	public Timestamp getDtreabastecimento() {
		return dtreabastecimento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdreabastecimentostatus")
	@DisplayName("Situação")
	public Reabastecimentostatus getReabastecimentostatus() {
		return reabastecimentostatus;
	}

	public void setCdreabastecimento(Integer cdreabastecimento) {
		this.cdreabastecimento = cdreabastecimento;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setDtreabastecimento(Timestamp dtreabastecimento) {
		this.dtreabastecimento = dtreabastecimento;
	}

	public void setReabastecimentostatus(
			Reabastecimentostatus reabastecimentoStatus) {
		this.reabastecimentostatus = reabastecimentoStatus;
	}

	@OneToMany(mappedBy="reabastecimento", fetch=FetchType.LAZY)
	public List<Reabastecimentolote> getListaReabastecimentolote() {
		return listaReabastecimentolote;
	}

	public void setListaReabastecimentolote(
			List<Reabastecimentolote> listaReabastecimentolote) {
		this.listaReabastecimentolote = listaReabastecimentolote;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cdreabastecimento == null) ? 0 : cdreabastecimento
						.hashCode());
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
		final Reabastecimento other = (Reabastecimento) obj;

		if (cdreabastecimento == null && other.cdreabastecimento == null)
			return this == other;
		else if (cdreabastecimento == null) {
			if (other.cdreabastecimento != null)
				return false;
		} else if (!cdreabastecimento.equals(other.cdreabastecimento))
			return false;
		return true;
	}
	
}
