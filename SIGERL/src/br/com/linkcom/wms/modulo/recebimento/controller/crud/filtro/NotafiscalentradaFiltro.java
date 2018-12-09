package br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro;

import java.util.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.GenericBean;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Fornecedor;
import br.com.linkcom.wms.geral.bean.Notafiscaltipo;

public class NotafiscalentradaFiltro extends FiltroListagem {

	public static final GenericBean EMITIDO = new GenericBean(1, "Emitido");
	public static final GenericBean RECEBIDO = new GenericBean(2, "Recebido");
	public static final GenericBean CANCELADO = new GenericBean(3, "Cancelado");
	
	protected String numero;
	protected Notafiscaltipo notafiscaltipo;
	protected Fornecedor fornecedor;
	protected GenericBean statusnotafiscal;
	protected Date dtinicio;
	protected Date dtfim;
	
	@MaxLength(15)
	@DisplayName("Número")
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	@DisplayName("Tipo")
	public Notafiscaltipo getNotafiscaltipo() {
		return notafiscaltipo;
	}
	
	@Required
	@DisplayName("Data Inicio")
	public Date getDtinicio() {
		return dtinicio;
	}

	@Required
	@DisplayName("Data Fim")
	public Date getDtfim() {
		return dtfim;
	}

	public void setDtinicio(Date dtinicio) {
		this.dtinicio = dtinicio;
	}

	public void setDtfim(Date dtfim) {
		this.dtfim = dtfim;
	}

	public void setNotafiscaltipo(Notafiscaltipo notafiscaltipo) {
		this.notafiscaltipo = notafiscaltipo;
	}
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	@DisplayName("Status")
	public GenericBean getStatusnotafiscal() {
		return statusnotafiscal;
	}
	
	public void setStatusnotafiscal(GenericBean statusnotafiscal) {
		this.statusnotafiscal = statusnotafiscal;
	}

	
}
