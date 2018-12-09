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
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name = "sq_configuracao", sequenceName = "sq_configuracao")
public class Configuracao {

	protected Integer cdconfiguracao;
	protected Deposito deposito;
	protected String nome;
	protected String valor;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_configuracao")
	public Integer getCdconfiguracao() {
		return cdconfiguracao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}

	@MaxLength(30)
	@DisplayName("Nome")
	public String getNome() {
		return nome;
	}

	@MaxLength(200)
	@DisplayName("Valor")
	public String getValor() {
		return valor;
	}

	public void setCdconfiguracao(Integer cdconfiguracao) {
		this.cdconfiguracao = cdconfiguracao;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
