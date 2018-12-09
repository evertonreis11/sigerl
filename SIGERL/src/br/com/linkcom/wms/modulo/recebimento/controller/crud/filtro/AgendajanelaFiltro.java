package br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro;

import java.util.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Produtoclasse;

public class AgendajanelaFiltro extends FiltroListagem {

	private Deposito deposito;
	private Produtoclasse produtoclasse;
	private Date dataAgenda;

	@Required
	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}

	@DisplayName("Classe de produto")
	public Produtoclasse getProdutoclasse() {
		return produtoclasse;
	}
	
	@DisplayName("Data de agendamento")
	public Date getDataAgenda() {
		return dataAgenda;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setProdutoclasse(Produtoclasse produtoclasse) {
		this.produtoclasse = produtoclasse;
	}
	
	public void setDataAgenda(Date dataAgenda) {
		this.dataAgenda = dataAgenda;
	}
	
}
