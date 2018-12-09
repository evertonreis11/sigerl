package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@SequenceGenerator(name="sq_reabastecimentostatus",sequenceName="sq_reabastecimentostatus")
public class Reabastecimentostatus {
	
	private Integer cdreabastecimentostatus;
	private String nome;
	
	public final static Reabastecimentostatus NAO_INICIADO = new Reabastecimentostatus(1, "Não iniciado");
	public final static Reabastecimentostatus EM_EXECUCAO = new Reabastecimentostatus(2, "Em execução");
	public final static Reabastecimentostatus FINALIZADO_SUCESSO = new Reabastecimentostatus(3, "Finalizado com sucesso");
	public final static Reabastecimentostatus FINALIZADO_DIVERGENCIA = new Reabastecimentostatus(4, "Finalizado com divergência");
	public final static Reabastecimentostatus CANCELADO = new Reabastecimentostatus(5, "Cancelado");


	public Reabastecimentostatus() {
	}
	
	public Reabastecimentostatus(Integer cdstatus) {
		this.cdreabastecimentostatus = cdstatus;
	}
	
	public Reabastecimentostatus(Integer cdstatus, String nome) {
		this.cdreabastecimentostatus = cdstatus;
		this.nome = nome;
	}

	@Id
	@GeneratedValue(generator="sq_reabastecimentostatus",strategy = GenerationType.AUTO)
	public Integer getCdreabastecimentostatus() {
		return cdreabastecimentostatus;
	}

	@DescriptionProperty
	@DisplayName("Situação")
	public String getNome() {
		return nome;
	}

	public void setCdreabastecimentostatus(Integer cdreabastecimentostatus) {
		this.cdreabastecimentostatus = cdreabastecimentostatus;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cdreabastecimentostatus == null) ? 0
						: cdreabastecimentostatus.hashCode());
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
		final Reabastecimentostatus other = (Reabastecimentostatus) obj;
		if (cdreabastecimentostatus == null) {
			if (other.cdreabastecimentostatus != null)
				return false;
		} else if (!cdreabastecimentostatus
				.equals(other.cdreabastecimentostatus))
			return false;
		return true;
	}
	
}
