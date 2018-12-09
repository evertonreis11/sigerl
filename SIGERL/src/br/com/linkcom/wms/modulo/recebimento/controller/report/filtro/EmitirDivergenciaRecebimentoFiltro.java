package br.com.linkcom.wms.modulo.recebimento.controller.report.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Fornecedor;


public class EmitirDivergenciaRecebimentoFiltro {
	private Integer recebimento;
	private Date emissaode;
	private Date emissaoate;
	private Fornecedor fornecedor;
	private String notafiscal;

	@DisplayName("NÚMERO DO RECEBIMENTO")
	@MaxLength(9)
	public Integer getRecebimento() {
		return recebimento;
	}

	public Date getEmissaode() {
		return emissaode;
	}

	public Date getEmissaoate() {
		return emissaoate;
	}

	@DisplayName("Fornecedor")
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	@MaxLength(15)
	public String getNotafiscal() {
		return notafiscal;
	}

	public void setRecebimento(Integer recebimento) {
		this.recebimento = recebimento;
	}

	public void setEmissaode(Date emissaode) {
		this.emissaode = emissaode;
	}

	public void setEmissaoate(Date emissaoate) {
		this.emissaoate = emissaoate;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public void setNotafiscal(String notafiscal) {
		this.notafiscal = notafiscal;
	}
}
