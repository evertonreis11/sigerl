package br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Pedidovendatipo;

public class PedidovendaFiltro extends FiltroListagem {

	protected String numero;
	protected Cliente cliente;
	protected Pedidovendatipo pedidovendatipo;
	
	@MaxLength(15)
	@DisplayName("Número")
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	@DisplayName("Tipo")
	public Pedidovendatipo getPedidovendatipo() {
		return pedidovendatipo;
	}
	
	public void setPedidovendatipo(Pedidovendatipo pedidovendatipo) {
		this.pedidovendatipo = pedidovendatipo;
	}
	
}
