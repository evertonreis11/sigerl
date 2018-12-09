package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.wms.geral.bean.auxiliar.IOrdemexpedicao;

@Entity
public class Vordemexpedicao implements IOrdemexpedicao{
	
	private Integer cdcarregamento;
	private Integer cdcliente;
	private Integer cdtipooperacao;
	private Integer cdordemservico;
	private Integer cddeposito;
	
	public Integer getCddeposito() {
		return cddeposito;
	}	
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	public Integer getCdcliente() {
		return cdcliente;
	}
	public Integer getCdtipooperacao() {
		return cdtipooperacao;
	}
	@Id
	public Integer getCdordemservico() {
		return cdordemservico;
	}
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}
	public void setCdcliente(Integer cdcliente) {
		this.cdcliente = cdcliente;
	}
	public void setCdtipooperacao(Integer cdtipooperacao) {
		this.cdtipooperacao = cdtipooperacao;
	}
	public void setCdordemservico(Integer cdordemservico) {
		this.cdordemservico = cdordemservico;
	}
	public void setCddeposito(Integer cddeposito) {
		this.cddeposito = cddeposito;
	}
	
}
