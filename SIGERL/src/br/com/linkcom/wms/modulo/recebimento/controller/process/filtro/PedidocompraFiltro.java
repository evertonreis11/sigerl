package br.com.linkcom.wms.modulo.recebimento.controller.process.filtro;

import java.util.Date;

import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Pedidocomprastatus;

public class PedidocompraFiltro {

	private Pedidocomprastatus pedidocomprastatus;
	private Date dtagendaverba;
	private Deposito deposito;
	private String classeproduto;

	public Pedidocomprastatus getPedidocomprastatus() {
		return pedidocomprastatus;
	}

	public Date getDtagendaverba() {
		return dtagendaverba;
	}

	public Deposito getDeposito() {
		return deposito;
	}
	
	public String getClasseproduto() {
		return classeproduto;
	}

	public void setPedidocomprastatus(Pedidocomprastatus pedidocomprastatus) {
		this.pedidocomprastatus = pedidocomprastatus;
	}

	public void setDtagendaverba(Date dtagendaverba) {
		this.dtagendaverba = dtagendaverba;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setClasseproduto(String classeproduto) {
		this.classeproduto = classeproduto;
	}

}
