package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;


@Entity
@SequenceGenerator(name = "sq_permissaoordem", sequenceName = "sq_permissaoordem")
@DisplayName("Permissão de Ordens")
public class Permissaoordem{

	protected Integer cdpermissaoordem;
	protected Papel papel;
	protected Ordemtipo ordemtipo;
	
	//Transiente
	protected Boolean permitido;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_permissaoordem")
	public Integer getCdpermissaoordem() {
		return cdpermissaoordem;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpapel")
	@DisplayName("Papel")
	@Required
	public Papel getPapel() {
		return papel;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdordemtipo")
	@DisplayName("Ordem Tipo")
	@Required
	public Ordemtipo getOrdemtipo() {
		return ordemtipo;
	}
	@Transient
	@DisplayName("Permitido")
	public Boolean getPermitido() {
		return permitido;
	}
	

	
	public void setCdpermissaoordem(Integer cdpermissaoordem) {
		this.cdpermissaoordem = cdpermissaoordem;
	}
	
	public void setPapel(Papel papel) {
		this.papel = papel;
	}
	
	public void setOrdemtipo(Ordemtipo ordemtipo) {
		this.ordemtipo = ordemtipo;
	}
	
	public void setPermitido(Boolean permitido) {
		this.permitido = permitido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdpermissaoordem == null) ? 0 : cdpermissaoordem.hashCode());
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
		final Permissaoordem other = (Permissaoordem) obj;
		if (cdpermissaoordem == null) {
			if (other.cdpermissaoordem != null)
				return false;
		} else if (!cdpermissaoordem.equals(other.cdpermissaoordem))
			return false;
		return true;
	}
	
	
}
