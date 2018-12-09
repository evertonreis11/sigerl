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
@SequenceGenerator(name = "sq_usuariopapel", sequenceName = "sq_usuariopapel")
public class Usuariopapel {

	protected Integer cdusuariopapel;
	protected Usuario usuario;
	protected Papel papel;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_usuariopapel")
	public Integer getCdusuariopapel() {
		return cdusuariopapel;
	}
	public void setCdusuariopapel(Integer id) {
		this.cdusuariopapel = id;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpessoa")
	@Required
	public Usuario getUsuario() {
		return usuario;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpapel")
	@Required
	public Papel getPapel() {
		return papel;
	}

	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void setPapel(Papel papel) {
		this.papel = papel;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdusuariopapel == null) ? 0 : cdusuariopapel.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if (obj instanceof Papel){
			if (this.papel == obj)
				return true;
			if (this.papel == null || this.papel.cdpapel == null)
				return false;
			
			final Papel other = (Papel) obj;
			return papel.cdpapel.equals(other.cdpapel);
		}
		
		if (getClass() != obj.getClass())
			return false;
		final Usuariopapel other = (Usuariopapel) obj;

		if (cdusuariopapel == null && other.cdusuariopapel == null)
			return this == other;
		else if (cdusuariopapel == null) {
			if (other.cdusuariopapel != null)
				return false;
		} else if (!cdusuariopapel.equals(other.cdusuariopapel))
			return false;
		return true;
	}

	
}
