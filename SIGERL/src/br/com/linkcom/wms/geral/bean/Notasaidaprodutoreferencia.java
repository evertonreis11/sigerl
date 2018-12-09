package br.com.linkcom.wms.geral.bean;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "sq_notareferencia", sequenceName = "sq_notareferencia")
public class Notasaidaprodutoreferencia {
	
	private Integer cdnotasaidaprodutoreferencia;
	private Notafiscalsaidaproduto notafiscalsaidaproduto;
	private Usuario usuario;
	private Long notareferencia;
	private Date dataemissao;
	private Date datainclusao;
	
	public Notasaidaprodutoreferencia(){
	}
	public Notasaidaprodutoreferencia(Integer cdnotasaidaprodutoreferencia) {
		this.cdnotasaidaprodutoreferencia = cdnotasaidaprodutoreferencia;
	}
	
	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_notareferencia")
	public Integer getCdnotasaidaprodutoreferencia() {
		return cdnotasaidaprodutoreferencia;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdnotafiscalsaidaproduto")   
	public Notafiscalsaidaproduto getNotafiscalsaidaproduto() {
		return notafiscalsaidaproduto;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdusuario")   	
	public Usuario getUsuario() {
		return usuario;
	}
	public Long getNotareferencia() {
		return notareferencia;
	}
	public Date getDataemissao() {
		return dataemissao;
	}
	public Date getDatainclusao() {
		return datainclusao;
	}
	public void setCdnotasaidaprodutoreferencia(Integer cdnotasaidaprodutoreferencia) {
		this.cdnotasaidaprodutoreferencia = cdnotasaidaprodutoreferencia;
	}
	
	
	//Set's
	public void setNotafiscalsaidaproduto(Notafiscalsaidaproduto notafiscalsaidaproduto) {
		this.notafiscalsaidaproduto = notafiscalsaidaproduto;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public void setNotareferencia(Long notareferencia) {
		this.notareferencia = notareferencia;
	}
	public void setDataemissao(Date dataemissao) {
		this.dataemissao = dataemissao;
	}
	public void setDatainclusao(Date datainclusao) {
		this.datainclusao = datainclusao;
	}
	
}
