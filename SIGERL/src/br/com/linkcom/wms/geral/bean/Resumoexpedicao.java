package br.com.linkcom.wms.geral.bean;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@Immutable
@Table(name = "V_RESUMOEXPEDICAO")
@DisplayName("Resumo das expedições")
public class Resumoexpedicao {

	private Integer chave;
	private Integer cddeposito;
	private String deposito;
	private Date fechamento;
	private String linhaseparacao;
	private String tipooperacao;
	private Long qtdeconfirmada;
	private Long qtdeesperada;
	private Double pesoesperado;
	private Double valoresperado;
	private Long volumesesperados;
	private Double cubagemesperada;
	private Double pesoconfirmado;
	private Double valorconfirmado;
	private Long volumesconfirmados;
	private Double cubagemconfirmada;

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

	public Date getFechamento() {
		return fechamento;
	}

	@DisplayName("Linha de separação")
	public String getLinhaseparacao() {
		return linhaseparacao;
	}

	@DisplayName("Tipo de operação")
	public String getTipooperacao() {
		return tipooperacao;
	}

	@DisplayName("Qtde. confirmada")
	public Long getQtdeconfirmada() {
		return qtdeconfirmada;
	}

	@DisplayName("Qtde. esperada")
	public Long getQtdeesperada() {
		return qtdeesperada;
	}

	@DisplayName("Peso")
	public Double getPesoesperado() {
		return pesoesperado;
	}

	@DisplayName("Valor")
	public Double getValoresperado() {
		return valoresperado;
	}

	@DisplayName("Volumes")
	public Long getVolumesesperados() {
		return volumesesperados;
	}

	@DisplayName("Cubagem")
	public Double getCubagemesperada() {
		return cubagemesperada;
	}
	
	@DisplayName("Peso")
	public Double getPesoconfirmado() {
		return pesoconfirmado;
	}
	
	@DisplayName("Valor")
	public Double getValorconfirmado() {
		return valorconfirmado;
	}
	
	@DisplayName("Cubagem")
	public Double getCubagemconfirmada() {
		return cubagemconfirmada;
	}
	
	public void setCubagemconfirmada(Double cubagemconfirmada) {
		this.cubagemconfirmada = cubagemconfirmada;
	}
	
	@DisplayName("Volumes")
	public Long getVolumesconfirmados() {
		return volumesconfirmados;
	}

	public void setChave(Integer chave) {
		this.chave = chave;
	}
	
	public void setCddeposito(Integer cdeposito) {
		this.cddeposito = cdeposito;
	}

	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}

	public void setFechamento(Date fechamento) {
		this.fechamento = fechamento;
	}

	public void setLinhaseparacao(String linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}

	public void setTipooperacao(String tipooperacao) {
		this.tipooperacao = tipooperacao;
	}

	public void setQtdeconfirmada(Long qtdeconfirmada) {
		this.qtdeconfirmada = qtdeconfirmada;
	}

	public void setQtdeesperada(Long qtdeesperada) {
		this.qtdeesperada = qtdeesperada;
	}

	public void setPesoesperado(Double peso) {
		this.pesoesperado = peso;
	}

	public void setValoresperado(Double valor) {
		this.valoresperado = valor;
	}

	public void setVolumesesperados(Long volumes) {
		this.volumesesperados = volumes;
	}

	public void setCubagemesperada(Double cubagem) {
		this.cubagemesperada = cubagem;
	}
	
	public void setPesoconfirmado(Double pesoconfirmado) {
		this.pesoconfirmado = pesoconfirmado;
	}
	
	public void setValorconfirmado(Double valorconfirmado) {
		this.valorconfirmado = valorconfirmado;
	}
	
	public void setVolumesconfirmados(Long volumesconfirmados) {
		this.volumesconfirmados = volumesconfirmados;
	}
	
}
