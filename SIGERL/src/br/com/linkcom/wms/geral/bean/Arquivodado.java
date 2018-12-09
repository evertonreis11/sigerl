package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;

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
import br.com.linkcom.neo.validation.annotation.MaxLength;


@Entity
@SequenceGenerator(name = "sq_arquivodado", sequenceName = "sq_arquivodado")
public class Arquivodado {

	protected Integer cdarquivodado;
	protected Arquivo arquivo;
	protected Usuario cdusuarioupload;
	protected Timestamp dtupload;
	protected Boolean processado;
	protected Usuario cdusuarioprocessa;
	protected Timestamp dtprocessa;
	protected String observacao;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_arquivodado")
	public Integer getCdarquivodado() {
		return cdarquivodado;
	}
	public void setCdarquivodado(Integer id) {
		this.cdarquivodado = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdarquivo")
	public Arquivo getArquivo() {
		return arquivo;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdusuarioupload")
	public Usuario getCdusuarioupload() {
		return cdusuarioupload;
	}
	
	public Timestamp getDtupload() {
		return dtupload;
	}
	
	@Transient
	public Boolean getProcessado() {
		return processado;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdusuarioprocessa")
	public Usuario getCdusuarioprocessa() {
		return cdusuarioprocessa;
	}
	
	public Timestamp getDtprocessa() {
		return dtprocessa;
	}
	@DisplayName("Observação")
	@MaxLength(100)
	public String getObservacao() {
		return observacao;
	}
	
	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}
	
	public void setCdusuarioupload(Usuario cdusuarioupload) {
		this.cdusuarioupload = cdusuarioupload;
	}
	
	public void setDtupload(Timestamp dtupload) {
		this.dtupload = dtupload;
	}
	
	public void setProcessado(Boolean processado) {
		this.processado = processado;
	}
	
	public void setCdusuarioprocessa(Usuario cdusuarioprocessa) {
		this.cdusuarioprocessa = cdusuarioprocessa;
	}
	
	public void setDtprocessa(Timestamp dtprocessa) {
		this.dtprocessa = dtprocessa;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}
