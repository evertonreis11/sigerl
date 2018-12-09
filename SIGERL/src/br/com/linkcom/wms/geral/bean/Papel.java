package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.authorization.Role;
import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;


@Entity
@SequenceGenerator(name = "sq_papel", sequenceName = "sq_papel")
@DisplayName("Perfil")
@DefaultOrderBy("nome")
public class Papel implements Role {

	protected Integer cdpapel;
	protected String nome; 
	protected Boolean administrador = Boolean.FALSE;
	protected String descricao;
	protected Boolean ativo;
	protected Boolean somenteadm = Boolean.FALSE;
	protected List<Usuariopapel> listaUsuariopapel = new ArrayList<Usuariopapel>();
	
	// constantes
	public static final Papel CONFERENTE_DE_RECEBIMENTO = new Papel(5);
	public static final Papel SEPARADOR = new Papel(8);
	public static final Papel CONFERENTE_EXPEDICAO = new Papel(7);
	public static final Papel REPOSITOR = new Papel(13);
	public static final Papel ESTOQUISTA = new Papel(14);
	public static final Papel OPERADOR_TRANSPALETEIRA = new Papel(10);
	public static final Papel OPERADOR_EMPILHADEIRA = new Papel(9);
	public static final Papel COORDENADOR_EXPEDICAO = new Papel(6);
	public static final Papel COORDENADOR_2CONFERENCIA = new Papel(15);
	public static final Papel CONFERENTE_2CONFERENCIA = new Papel(16);

	
	public Papel(){
		
	}
	
	public Papel(Integer cd){
		this.cdpapel = cd;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_papel")
	public Integer getCdpapel() {
		return cdpapel;
	}
	
	public void setCdpapel(Integer id) {
		this.cdpapel = id;
	}

	
	@Required
	@MaxLength(50)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	public Boolean getAdministrador() {
		return administrador;
	}
	
	@MaxLength(100)
	@DisplayName("Descrição")
	public String getDescricao() {
		return descricao;
	}
	
	public Boolean getAtivo() {
		return ativo;
	}
	
	public Boolean getSomenteadm() {
		return somenteadm;
	}

	@OneToMany(mappedBy="papel")
	public List<Usuariopapel> getListaUsuariopapel() {
		return listaUsuariopapel;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	public void setSomenteadm(Boolean somenteadm) {
		this.somenteadm = somenteadm;
	}

	public void setListaUsuariopapel(List<Usuariopapel> listaUsuariopapel) {
		this.listaUsuariopapel = listaUsuariopapel;
	}
	
	/* API */
	@Transient
	public String getDescription() {
		return this.descricao;
	}
	@Transient
	public String getName() {
		return this.nome;
	}
	
	@Transient
	public Boolean isAdmin() {
		return administrador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdpapel == null) ? 0 : cdpapel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if (obj instanceof Usuariopapel){
			final Usuariopapel other = (Usuariopapel) obj;

			if (this == other.getPapel())
				return true;
			if (other.getPapel() == null || other.getPapel().getCdpapel() == null)
				return false;
			
			return other.getPapel().getCdpapel().equals(this.cdpapel);
		}
		
		if (getClass() != obj.getClass())
			return false;
		final Papel other = (Papel) obj;
		if (cdpapel == null) {
			if (other.cdpapel != null)
				return false;
		} else if (!cdpapel.equals(other.cdpapel))
			return false;
		return true;
	}
	
	

}
