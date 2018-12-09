package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "sq_enderecolinhaseparacao", sequenceName = "sq_enderecolinhaseparacao")
public class EnderecoLinhaseparacao {

	private Integer cdenderecolinhaseparacao;
	private Endereco endereco;
	private Linhaseparacao linhaseparacao;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_enderecolinhaseparacao")
	public Integer getCdenderecolinhaseparacao() {
		return cdenderecolinhaseparacao;
	}

	public void setCdenderecolinhaseparacao(Integer cdenderecolinhaseparacao) {
		this.cdenderecolinhaseparacao = cdenderecolinhaseparacao;
	}

	@ManyToOne()
	@JoinColumn(name="cdendereco")
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@ManyToOne()
	@JoinColumn(name="cdlinhaseparacao")
	public Linhaseparacao getLinhaseparacao() {
		return linhaseparacao;
	}

	public void setLinhaseparacao(Linhaseparacao linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}

}
