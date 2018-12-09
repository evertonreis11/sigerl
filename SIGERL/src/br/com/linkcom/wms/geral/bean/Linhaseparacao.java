package br.com.linkcom.wms.geral.bean;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_linhaseparacao", sequenceName = "sq_linhaseparacao")
@DisplayName("Linha de separação")
public class Linhaseparacao {
	
	protected Integer cdlinhaseparacao;
	protected String nome;
	protected boolean usacheckout;
	protected Deposito deposito;
	protected Set<Usuariolinhaseparacao> listaUsuarioLinhaSeparacao = new ListSet<Usuariolinhaseparacao>(Usuariolinhaseparacao.class);
	
	public Linhaseparacao(Integer cd, String nome) {
		this.cdlinhaseparacao = cd;
		this.nome = nome;
	}
	public Linhaseparacao(Integer cd) {
		this.cdlinhaseparacao = cd;
	}
	public Linhaseparacao() {
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_linhaseparacao")
	public Integer getCdlinhaseparacao() {
		return cdlinhaseparacao;
	}
	
	@Required
	@DescriptionProperty
	@MaxLength(20)
	public String getNome() {
		return nome;
	}
	
	@Required
	@DisplayName("Usa checkout")
	public boolean getUsacheckout() {
		return usacheckout;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")
	@Required
	public Deposito getDeposito() {
		return deposito;
	}
	@OneToMany(mappedBy="linhaseparacao")
	public Set<Usuariolinhaseparacao> getListaUsuarioLinhaSeparacao() {
		return listaUsuarioLinhaSeparacao;
	}
	
	public void setCdlinhaseparacao(Integer cdlinhaseparacao) {
		this.cdlinhaseparacao = cdlinhaseparacao;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setUsacheckout(boolean usacheckout) {
		this.usacheckout = usacheckout;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setListaUsuarioLinhaSeparacao(Set<Usuariolinhaseparacao> listaUsuarioLinhaSeparacao) {
		this.listaUsuarioLinhaSeparacao = listaUsuarioLinhaSeparacao;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Linhaseparacao) {
			Linhaseparacao linhaSeparacao = (Linhaseparacao) obj;
			
			return linhaSeparacao.getCdlinhaseparacao().equals(this.getCdlinhaseparacao());
		}
		
		return super.equals(obj);
	}
	
}
