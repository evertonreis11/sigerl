package br.com.linkcom.wms.geral.bean;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@Immutable
@Table(name = "V_RESUMORECEBIMENTO")
@DisplayName("Resumo dos recebimentos")
public class Resumorecebimento {

	private Integer chave;
	private Integer cddeposito;
	private String deposito;
	private Date fechamento;
	private String linhaseparacao;
	private Long qtde;
	private Double peso;
	private Double valor;
	private Double cubagem;

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

	@DisplayName("Qtde.")
	public Long getQtde() {
		return qtde;
	}

	public Double getPeso() {
		return peso;
	}

	public Double getValor() {
		return valor;
	}

	public Double getCubagem() {
		return cubagem;
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

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public void setCubagem(Double cubagem) {
		this.cubagem = cubagem;
	}

}
