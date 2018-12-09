package br.com.linkcom.wms.modulo.sistema.controller.process.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Agendastatus;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Fornecedor;
import br.com.linkcom.wms.geral.bean.Pedidocompra;
import br.com.linkcom.wms.geral.bean.Produtoclasse;
import br.com.linkcom.wms.geral.bean.Tipocarga;

public class AgendamentoFiltro extends FiltroListagem implements Cloneable {
	
	protected Deposito deposito;
	protected Integer cdagenda;
	protected Fornecedor fornecedor;
	protected Long numeroPedido;
	protected Boolean parcial;
	protected Date datainicial;
	protected Date datafinal;
	protected Tipocarga tipocarga;
	protected Pedidocompra pedidocompra;
	protected String codigoProduto;
	protected Integer cdagendamento;
	protected Agendastatus agendastatus;
	protected Deposito depositoTransferencia;
	protected Produtoclasse produtoclasse;
	protected boolean apenasNaoAgendados = false;
	protected String whereInAgendaPopup;
	protected Date dtAgendaGera;
	
	// para recuperar a data inicial
	{
		this.datainicial = new Date(System.currentTimeMillis());
	}
	
	@Required
	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@DisplayName("Número")
	public Integer getCdagenda() {
		return cdagenda;
	}
	
	@DisplayName("Fornecedor")
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	
	@DisplayName("Número do pedido")
	public Long getNumeroPedido() {
		return numeroPedido;
	}
	
	public Boolean getParcial() {
		return parcial;
	}
	
	@DisplayName("De")
	public Date getDatainicial() {
		return datainicial;
	}
	
	@DisplayName("Até")
	public Date getDatafinal() {
		return datafinal;
	}
	
	@DisplayName("Tipo de carga")
	public Tipocarga getTipocarga() {
		return tipocarga;
	}
	
	@DisplayName("Pedido")
	public Pedidocompra getPedidocompra() {
		return pedidocompra;
	}
	
	@DisplayName("Código do produto")
	public String getCodigoProduto() {
		return codigoProduto;
	}
	
	@DisplayName("Número do agendamento")
	public Integer getCdagendamento() {
		return cdagendamento;
	}
	
	@DisplayName("Status")
	public Agendastatus getAgendastatus() {
		return agendastatus;
	}
	
	@DisplayName("Depósito de transferência")
	public Deposito getDepositoTransferencia() {
		return depositoTransferencia;
	}
	
	@DisplayName("Classe de produto")
	public Produtoclasse getProdutoclasse() {
		return produtoclasse;
	}
	
	public boolean getApenasNaoAgendados() {
		return apenasNaoAgendados;
	}
	
	public String getWhereInAgendaPopup() {
		return whereInAgendaPopup;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setCdagenda(Integer cdagenda) {
		this.cdagenda = cdagenda;
	}
	
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public void setNumeroPedido(Long numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	
	public void setParcial(Boolean parcial) {
		this.parcial = parcial;
	}
	
	public void setDatainicial(Date datainicial) {
		this.datainicial = datainicial;
	}
	
	public void setDatafinal(Date datafinal) {
		this.datafinal = datafinal;
	}
	
	public void setTipocarga(Tipocarga tipocarga) {
		this.tipocarga = tipocarga;
	}
	
	public void setPedidocompra(Pedidocompra pedidocompra) {
		this.pedidocompra = pedidocompra;
	}
	
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	public void setCdagendamento(Integer cdagendamento) {
		this.cdagendamento = cdagendamento;
	}
	
	public void setAgendastatus(Agendastatus agendastatus) {
		this.agendastatus = agendastatus;
	}
	
	@Override
	public AgendamentoFiltro clone() throws CloneNotSupportedException {
		return (AgendamentoFiltro)super.clone();
	}
	
	public void setDepositoTransferencia(Deposito depositoTransferencia) {
		this.depositoTransferencia = depositoTransferencia;
	}
	
	public void setProdutoclasse(Produtoclasse produtoclasse) {
		this.produtoclasse = produtoclasse;
	}
	
	public void setApenasNaoAgendados(boolean apenasNaoAgendados) {
		this.apenasNaoAgendados = apenasNaoAgendados;
	}
	public void setWhereInAgendaPopup(String whereInAgendaPopup) {
		this.whereInAgendaPopup = whereInAgendaPopup;
	}

	@DisplayName("Data de Geração")
	public Date getDtAgendaGera() {
		return dtAgendaGera;
	}

	public void setDtAgendaGera(Date dtAgendaGera) {
		this.dtAgendaGera = dtAgendaGera;
	}
}
