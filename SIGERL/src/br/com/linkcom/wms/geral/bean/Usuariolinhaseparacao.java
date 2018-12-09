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

@Entity
@SequenceGenerator(name="sq_usuariolinhaseparacao", sequenceName = "sq_usuariolinhaseparacao")
public class Usuariolinhaseparacao {

	private Integer cdusuariolinhaseparacao;
	private Usuario usuario;
	private Linhaseparacao  linhaseparacao;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(generator = "sq_usuariolinhaseparacao", strategy = GenerationType.AUTO)
	public Integer getCdusuariolinhaseparacao() {
		return cdusuariolinhaseparacao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@DisplayName("Usuário")
	@JoinColumn(name="cdpessoa")
	public Usuario getUsuario() {
		return usuario;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@DisplayName("Linha de separação")
	@JoinColumn(name="cdlinhaseparacao")
	public Linhaseparacao getLinhaseparacao() {
		return linhaseparacao;
	}
	public void setCdusuariolinhaseparacao(Integer cdusuariolinhaseparacao) {
		this.cdusuariolinhaseparacao = cdusuariolinhaseparacao;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public void setLinhaseparacao(Linhaseparacao linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}
	
	
}
