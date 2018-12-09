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
@SequenceGenerator(name = "sq_acaopapel", sequenceName = "sq_acaopapel")
@DisplayName("Permissão de ações")
public class Acaopapel{

	protected Integer cdacaopapel;
	protected Papel papel;
	protected Acao acao;
	protected Boolean permitido;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_acaopapel")
	public Integer getCdacaopapel() {
		return cdacaopapel;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpapel")
	@DisplayName("Papel")
	@Required
	public Papel getPapel() {
		return papel;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdacao")
	@DisplayName("Ação")
	@Required
	public Acao getAcao() {
		return acao;
	}
	
	@DisplayName("Permitido")
	public Boolean getPermitido() {
		return permitido;
	}

	public void setCdacaopapel(Integer cdacaopapel) {
		this.cdacaopapel = cdacaopapel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public void setPermitido(Boolean permitido) {
		this.permitido = permitido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdacaopapel == null) ? 0 : cdacaopapel.hashCode());
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
		final Acaopapel other = (Acaopapel) obj;
		if (cdacaopapel == null) {
			if (other.cdacaopapel != null)
				return false;
		} else if (!cdacaopapel.equals(other.cdacaopapel))
			return false;
		return true;
	}
}
