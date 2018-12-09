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

import br.com.linkcom.neo.authorization.Role;
import br.com.linkcom.neo.authorization.impl.AbstractPermission;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;


@Entity
@SequenceGenerator(name = "sq_permissao", sequenceName = "sq_permissao")
public class Permissao extends AbstractPermission {

	protected Integer cdpermissao;
	protected Papel papel;
	protected Tela tela;
	protected String stringpermissao;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_permissao")
	public Integer getCdpermissao() {
		return cdpermissao;
	}
	public void setCdpermissao(Integer id) {
		this.cdpermissao = id;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpapel")
	@Required
	public Papel getPapel() {
		return papel;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtela")
	@Required
	public Tela getTela() {
		return tela;
	}
	
	@Required
	@MaxLength(200)
	public String getStringpermissao() {
		return stringpermissao;
	}

	
	public void setPapel(Papel papel) {
		this.papel = papel;
	}
	
	public void setTela(Tela tela) {
		this.tela = tela;
	}
	
	public void setStringpermissao(String stringpermissao) {
		this.stringpermissao = stringpermissao;
	}

	/* API */
	@Override
	public void setPermissionString(String string) {
		this.stringpermissao = string;
	}
	@Override
	@Transient
	public String getPermissionString() {
		return stringpermissao;
	}
	@Transient
	public Role getRole() {
		return papel;
	}
	/* API */
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdpermissao == null) ? 0 : cdpermissao.hashCode());
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
		final Permissao other = (Permissao) obj;

		if (cdpermissao == null && other.cdpermissao == null)
			return this == other;
		else if (cdpermissao == null) {
			if (other.cdpermissao != null)
				return false;
		} else if (!cdpermissao.equals(other.cdpermissao))
			return false;
		return true;
	}
	
	
}
