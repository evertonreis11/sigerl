package br.com.linkcom.wms.geral.bean;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Immutable;

import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@Immutable
@Table(name = "V_CORTEDIARIO")
@DisplayName("Corte diário")
public class Cortediario {

	private Integer chave;
	private Integer cddeposito;
	private String deposito;
	private String tipooperacao;
	private Date fechamento;
	private String placa;
	private Long qtdeesperada;
	private Long qtdeconfirmada;
	private Long qtdecortada;
	private Double cortepercentual;

	@Id
	public Integer getChave() {
		return chave;
	}
	
	public Integer getCddeposito() {
		return cddeposito;
	}

	@DisplayName("Depósito")
	public String getDeposito() {
		return deposito;
	}

	@DisplayName("Tipo de operação")
	public String getTipooperacao() {
		return tipooperacao;
	}

	public Date getFechamento() {
		return fechamento;
	}

	@DisplayName("Veículo")
	public String getPlaca() {
		return placa;
	}

	@DisplayName("Qtde. esperada")
	public Long getQtdeesperada() {
		return qtdeesperada;
	}

	@DisplayName("Qtde. confirmada")
	public Long getQtdeconfirmada() {
		return qtdeconfirmada;
	}

	@DisplayName("Qtde. cortada")
	public Long getQtdecortada() {
		return qtdecortada;
	}
	
	@Column()
	@Formula("qtdecortada/qtdeesperada")
	@DisplayName("Corte percentual")
	public Double getCortepercentual() {
		return cortepercentual;
	}

	public void setChave(Integer chave) {
		this.chave = chave;
	}
	
	public void setCddeposito(Integer cddeposito) {
		this.cddeposito = cddeposito;
	}

	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}

	public void setTipooperacao(String tipooperacao) {
		this.tipooperacao = tipooperacao;
	}

	public void setFechamento(Date fechamento) {
		this.fechamento = fechamento;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public void setQtdeesperada(Long qtdeesperada) {
		this.qtdeesperada = qtdeesperada;
	}

	public void setQtdeconfirmada(Long qtdeconfirmada) {
		this.qtdeconfirmada = qtdeconfirmada;
	}

	public void setQtdecortada(Long qtdecortada) {
		this.qtdecortada = qtdecortada;
	}

	public void setCortepercentual(Double cortepercentual) {
		this.cortepercentual = cortepercentual;
	}
}
