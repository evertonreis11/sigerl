package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_controleetiqueta", sequenceName = "sq_controleetiqueta")
public class Controleetiqueta {

	protected Integer cdcontroleetiqueta;
	protected Integer cdcarregamento;
	protected Integer cdexpedicao;
	protected Integer cdpessoa;
	protected Timestamp dtcontroleetiqueta;
	protected Timestamp dtimpressao;
	
	public Controleetiqueta(){
		
	}
	
	public Controleetiqueta(Integer cdcontroleetiqueta) {
		this.cdcontroleetiqueta = cdcontroleetiqueta;
	}
	
	//Metodos get e set
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_controleetiqueta")
	public Integer getCdcontroleetiqueta() {
		return cdcontroleetiqueta;
	}
	
	@DisplayName("Carregamento")
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	
	@DisplayName("Expedição")
	public Integer getCdexpedicao() {
		return cdexpedicao;
	}
	
	@DisplayName("Usuário")
	public Integer getCdpessoa() {
		return cdpessoa;
	}
	
	@Required
	@DisplayName("Data")
	public Timestamp getDtcontroleetiqueta() {
		return dtcontroleetiqueta;
	}
	
	@DisplayName("Data Impressão")
	public Timestamp getDtimpressao() {
		return dtimpressao;
	}

	public void setCdcontroleetiqueta(Integer cdcontroleetiqueta) {
		this.cdcontroleetiqueta = cdcontroleetiqueta;
	}

	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}

	public void setCdexpedicao(Integer cdexpedicao) {
		this.cdexpedicao = cdexpedicao;
	}

	public void setCdpessoa(Integer cdpessoa) {
		this.cdpessoa = cdpessoa;
	}

	public void setDtcontroleetiqueta(Timestamp dtcontroleetiqueta) {
		this.dtcontroleetiqueta = dtcontroleetiqueta;
	}

	public void setDtimpressao(Timestamp dtimpressao) {
		this.dtimpressao = dtimpressao;
	}
	
	
	
}
