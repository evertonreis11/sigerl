package br.com.linkcom.wms.geral.bean;


import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_agenda", sequenceName = "sq_agenda")
@DisplayName("Agendamento")
public class Agenda {

	protected Integer cdagenda;
	protected Date dtagenda;
	protected Date dtprevisao;
	protected Tipocarga tipocarga;
	protected Deposito deposito;
	protected Agendastatus agendastatus;
	protected Set<Agendapedido> listaagendapedido = new ListSet<Agendapedido>(Agendapedido.class);
	protected Set<Agendaparcial> listaagendaparcial = new ListSet<Agendaparcial>(Agendaparcial.class);
	protected Set<Agendahistorico> listaHistorico  = new ListSet<Agendahistorico>(Agendahistorico.class);
	protected Deposito depositoTransferencia;
	protected Boolean validado;
	protected Set<Notafiscalentrada> listanotafiscalentrada = new ListSet<Notafiscalentrada>(Notafiscalentrada.class);
	protected Date dtAgendaGera;
	protected String observacao;
	
	//transiente
	protected String Pedidos;
	protected String recebimentos;
	protected Integer totalAgendado = 0;
	protected Fornecedor fornecedor;
	private boolean incluiuPedidos = false;
	
	public Agenda() {
	}
	
	public Agenda(Integer cdagenda) {
		this.cdagenda = cdagenda;
	}
	
	@Id
	@DisplayName("Número")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_agenda")
	public Integer getCdagenda() {
		return cdagenda;
	}
	@Required
	@DisplayName("Data de agendamento")
	public Date getDtagenda() {
		return dtagenda;
	}
	@DisplayName("Observação")
	public String getObservacao() {
		return observacao;
	}
	@Required
	@DisplayName("Previsão de vencimento financeiro")
	public Date getDtprevisao() {
		return dtprevisao;
	}
	@DisplayName("Tipo de carga")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipocarga")
	@Required
	public Tipocarga getTipocarga() {
		return tipocarga;
	}
	@DisplayName("Depósito")
	@JoinColumn(name="cddeposito")
	@ManyToOne(fetch=FetchType.LAZY)
	public Deposito getDeposito() {
		return deposito;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdagendastatus")
	@Required
	@DisplayName("Status")
	public Agendastatus getAgendastatus() {
		return agendastatus;
	}
	@OneToMany(mappedBy="agenda")
	public Set<Agendapedido> getListaagendapedido() {
		return listaagendapedido;
	}
	@OneToMany(mappedBy="agenda")
	public Set<Agendaparcial> getListaagendaparcial() {
		return listaagendaparcial;
	}
	@OneToMany(mappedBy="agenda")
	public Set<Agendahistorico> getListaHistorico() {
		return listaHistorico;
	}
	@OneToMany(mappedBy="agenda")
	public Set<Notafiscalentrada> getListanotafiscalentrada() {
		return listanotafiscalentrada;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDDEPOSITOTRANSFERENCIA")
	@DisplayName("Depósito de transferência")
	public Deposito getDepositoTransferencia() {
		return depositoTransferencia;
	}
	@DisplayName("Verba conferida")
	public Boolean getValidado() {
		return validado;
	}
	@Transient
	public String getPedidos() {
		return Pedidos;
	}
	@Transient
	public String getRecebimentos() {
		return recebimentos;
	}
	@DisplayName("Total agendado")
	@Transient
	public Integer getTotalAgendado() {
		return totalAgendado;
	}
	@Transient
	@DisplayName("Fornecedor")
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	@Transient
	public boolean getIncluiuPedidos(){
		return incluiuPedidos;
	}


	//Set's
	public void setCdagenda(Integer cdagenda) {
		this.cdagenda = cdagenda;
	}
	public void setDtagenda(Date data) {
		this.dtagenda = data;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public void setDtprevisao(Date dtprevisao) {
		this.dtprevisao = dtprevisao;
	}
	public void setTipocarga(Tipocarga tipocarga) {
		this.tipocarga = tipocarga;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setAgendastatus(Agendastatus agendastatus) {
		this.agendastatus = agendastatus;
	}
	public void setListaagendapedido(Set<Agendapedido> listaagendapedido) {
		this.listaagendapedido = listaagendapedido;
	}
	public void setListaagendaparcial(Set<Agendaparcial> listaagendaparcial) {
		this.listaagendaparcial = listaagendaparcial;
	}
	public void setListaHistorico(Set<Agendahistorico> listaHistorico) {
		this.listaHistorico = listaHistorico;
	}
	public void setListanotafiscalentrada(Set<Notafiscalentrada> listanotafiscalentrada) {
		this.listanotafiscalentrada = listanotafiscalentrada;
	}
	public void setDepositoTransferencia(Deposito depositoTransferencia) {
		this.depositoTransferencia = depositoTransferencia;
	}
	public void setPedidos(String pedidos) {
		Pedidos = pedidos;
	}
	public void setRecebimentos(String recebimentos) {
		this.recebimentos = recebimentos;
	}
	public void setTotalAgendado(Integer totalAgendado) {
		this.totalAgendado = totalAgendado;
	}
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	public void setIncluiuPedidos(boolean incluiupedido){
		this.incluiuPedidos = incluiupedido;
	}
	public void setValidado(Boolean validado) {
		this.validado = validado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdagenda == null) ? 0 : cdagenda.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Agenda other = (Agenda) obj;
		if (cdagenda == null) {
			if (other.cdagenda != null)
				return false;
		} else if (!cdagenda.equals(other.cdagenda))
			return false;
		return true;
	}

	@DisplayName("Data de geração do agendamento")
	public Date getDtAgendaGera() {
		return dtAgendaGera;
	}

	public void setDtAgendaGera(Date dtAgendaGera) {
		this.dtAgendaGera = dtAgendaGera;
	}
}
