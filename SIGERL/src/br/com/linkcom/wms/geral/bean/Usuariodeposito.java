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
@SequenceGenerator(name = "sq_usuariodeposito", sequenceName = "sq_usuariodeposito")
public class Usuariodeposito {
	
	protected Integer cdusuariodeposito;
	protected Usuario usuario;
	protected Deposito deposito;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_usuariodeposito")
	public Integer getCdusuariodeposito() {
		return cdusuariodeposito;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpessoa")
	public Usuario getUsuario() {
		return usuario;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	public void setCdusuariodeposito(Integer cdusuariodeposito) {
		this.cdusuariodeposito = cdusuariodeposito;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	

}
