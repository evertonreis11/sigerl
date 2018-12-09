package br.com.linkcom.wms.modulo.logistica.controller.report.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Produto;

public class RelatoriomovimentacaoFiltro extends FiltroListagem {

	private Produto produto;
	private Date datade;
	private Date dataate;
	private Cliente cliente;
	
	public Produto getProduto() {
		return produto;
	}
	@Required
	@DisplayName("De")
	public Date getDatade() {
		return datade;
	}
	@Required
	@DisplayName("Até")
	public Date getDataate() {
		return dataate;
	}
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public void setDataate(Date dataate) {
		this.dataate = dataate;
	}
	public void setDatade(Date datade) {
		this.datade = datade;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
}
