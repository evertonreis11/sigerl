package br.com.linkcom.wms.geral.bean;

import java.util.List;
import java.util.Set;

import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Transient;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_ordemservico", sequenceName = "sq_ordemservico")
@SqlResultSetMapping(name="resposta", columns={@ColumnResult(name="PROXIMA_ORDEM")})
@NamedNativeQueries(value={
	@NamedNativeQuery(name="atualizar_rastreabilidadedoc", query="{ call ATUALIZAR_RASTREABILIDADEDOC(?) }", resultSetMapping="scalar"),
	@NamedNativeQuery(name="gerar_conferencia_box", query="{ call GERAR_CONFERENCIA_BOX(?) }", resultSetMapping="scalar")
})
public class Ordemservico {

	protected Integer cdordemservico;
	protected Ordemtipo ordemtipo;
	protected Recebimento recebimento;
	protected Deposito deposito;
	protected Carregamento carregamento;
	protected Ordemstatus ordemstatus;
	protected Integer ordem;
	protected Ordemservico ordemservicoprincipal;
	protected Cliente clienteExpedicao;
	protected Transferencia transferencia;
	protected Inventariolote inventariolote;
	protected Reabastecimentolote reabastecimentolote;
	protected Tipooperacao tipooperacao;
	protected Expedicao expedicao;
	protected List<OrdemservicoUsuario> listaOrdemServicoUsuario = new ListSet<OrdemservicoUsuario>(OrdemservicoUsuario.class);
	protected Set<Ordemprodutoligacao> listaOrdemProdutoLigacao = new ListSet<Ordemprodutoligacao>(Ordemprodutoligacao.class);
	protected List<Ordemprodutohistorico> listaOrdemProdutoHistorico = new ListSet<Ordemprodutohistorico>(Ordemprodutohistorico.class);
	
	//Transientes
	
	protected Boolean hasReconferencia;
	protected Boolean hasItensFaturadosOutraFilia = Boolean.FALSE;
	protected Linhaseparacao linhaseparacao;
	protected String ordemservicoexpedicao;
	protected Boolean selecionada;
	protected String itensOrdem;
	protected boolean podeCortar = true;
	private String filiaisClientes;
	private String tiposPedidos;
	
	// -------------------------- Construtores ----------------------------
	public Ordemservico() {

	}
	
	public Ordemservico(Integer cdordemservico) {
		this.cdordemservico = cdordemservico;
	}

	public Ordemservico(Ordemtipo ordemtipo, Ordemstatus ordemstatus, Deposito deposito) {
		this.ordemtipo = ordemtipo;
		this.ordemstatus = ordemstatus;
		this.deposito = deposito;
	}
	
	// ----------------------- Métodos get e set ---------------------------
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_ordemservico")
	@DescriptionProperty
	public Integer getCdordemservico() {
		return cdordemservico;
	}
	public void setCdordemservico(Integer id) {
		this.cdordemservico = id;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdordemtipo")
	@DisplayName("Tipo de O.S.")
	public Ordemtipo getOrdemtipo() {
		return ordemtipo;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdrecebimento")
	public Recebimento getRecebimento() {
		return recebimento;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@OneToMany(mappedBy="ordemservico")
	public List<OrdemservicoUsuario> getListaOrdemServicoUsuario() {
		return listaOrdemServicoUsuario;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdordemstatus")
	@Required
	@DisplayName("Status da ordem de serviço")
	public Ordemstatus getOrdemstatus() {
		return ordemstatus;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdcarregamento")
	public Carregamento getCarregamento() {
		return carregamento;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdexpedicao")
	public Expedicao getExpedicao() {
		return expedicao;
	}
	
	@OneToMany(mappedBy="ordemservico")
	public Set<Ordemprodutoligacao> getListaOrdemProdutoLigacao() {
		return listaOrdemProdutoLigacao;
	}
	
	@OneToMany(mappedBy="ordemservico")
	public List<Ordemprodutohistorico> getListaOrdemProdutoHistorico() {
		return listaOrdemProdutoHistorico;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdordemservicoprincipal")
	@DisplayName("Ordem de serviço principal")
	public Ordemservico getOrdemservicoprincipal() {
		return ordemservicoprincipal;
	}
	
	public Integer getOrdem() {
		return ordem;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdclienteexpedicao")
	@DisplayName("Cliente da expedição")
	public Cliente getClienteExpedicao() {
		return clienteExpedicao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtransferencia")
	@DisplayName("Transferência")
	public Transferencia getTransferencia() {
		return transferencia;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdinventariolote")
	@DisplayName("Intervalo Lote")
	public Inventariolote getInventariolote() {
		return inventariolote;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdreabastecimentolote")
	public Reabastecimentolote getReabastecimentolote() {
		return reabastecimentolote;
	}
	
	@DisplayName("Tipo de operação")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipooperacao")
	public Tipooperacao getTipooperacao() {
		return tipooperacao;
	}
	
	public void setExpedicao(Expedicao expedicao) {
		this.expedicao = expedicao;
	}
	
	@Transient
	public Boolean getHasReconferencia() {
		return hasReconferencia;
	}
	
	public void setOrdemtipo(Ordemtipo ordemtipo) {
		this.ordemtipo = ordemtipo;
	}
	
	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}
	
	public void setOrdemstatus(Ordemstatus ordemstatus) {
		this.ordemstatus = ordemstatus;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setListaOrdemServicoUsuario(List<OrdemservicoUsuario> listaOrdemServicoUsuario) {
		this.listaOrdemServicoUsuario = listaOrdemServicoUsuario;
	}
	
	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}
	
	public void setListaOrdemProdutoLigacao(Set<Ordemprodutoligacao> listaOrdemProdutoLigacao) {
		this.listaOrdemProdutoLigacao = listaOrdemProdutoLigacao;
	}
	
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	
	public void setClienteExpedicao(Cliente clienteExpedicao) {
		this.clienteExpedicao = clienteExpedicao;
	}
	
	public void setOrdemservicoprincipal(Ordemservico ordemservicoprincipal) {
		this.ordemservicoprincipal = ordemservicoprincipal;
	}
	
	public void setListaOrdemProdutoHistorico(List<Ordemprodutohistorico> listaOrdemProdutoHistorico) {
		this.listaOrdemProdutoHistorico = listaOrdemProdutoHistorico;
	}
	
	public void setTransferencia(Transferencia transferencia) {
		this.transferencia = transferencia;
	}
	
	public void setInventariolote(Inventariolote inventariolote) {
		this.inventariolote = inventariolote;
	}
	
	public void setReabastecimentolote(Reabastecimentolote reabastecimentolote) {
		this.reabastecimentolote = reabastecimentolote;
	}
	
	public void setTipooperacao(Tipooperacao tipooperacao) {
		this.tipooperacao = tipooperacao;
	}
	public void setHasReconferencia(Boolean hasReconferencia) {
		this.hasReconferencia = hasReconferencia;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Ordemservico other = (Ordemservico) obj;

		if (cdordemservico == null && other.cdordemservico == null)
			return this == other;
		else if (cdordemservico == null) {
			if (other.cdordemservico != null)
				return false;
		} else if (!cdordemservico.equals(other.cdordemservico))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdordemservico == null) ? 0 : cdordemservico.hashCode());
		return result;
	}
	
	@Transient
	public Boolean getHasItensFaturadosOutraFilia() {
		return hasItensFaturadosOutraFilia;
	}
	
	public void setHasItensFaturadosOutraFilia(
			Boolean hasItensFaturadosOutraFilia) {
		this.hasItensFaturadosOutraFilia = hasItensFaturadosOutraFilia;
	}
	
	@Transient
	public Linhaseparacao getLinhaseparacao() {
		return linhaseparacao;
	}
	
	public void setLinhaseparacao(Linhaseparacao linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}
	
	@Transient
	public String getOrdemservicoexpedicao() {
		if(this.clienteExpedicao == null || this.clienteExpedicao.cdpessoa == null){
			return this.cdordemservico + (this.tipooperacao != null && this.tipooperacao.getCdtipooperacao() != null ? " / "+this.tipooperacao.getNome() : "");
		}else{
			return this.cdordemservico + (this.tipooperacao != null && this.tipooperacao.getCdtipooperacao() != null && this.tipooperacao.getNome() != null ? " / "+this.tipooperacao.getNome().charAt(0) : "")+ " / "+this.clienteExpedicao.getNome();
		}
	}
	
	public void setOrdemservicoexpedicao(String ordemservicoexpedicao) {
		this.ordemservicoexpedicao = ordemservicoexpedicao;
	}
	
	@Transient
	public Boolean getSelecionada() {
		return selecionada;
	}
	
	public void setSelecionada(Boolean selecionada) {
		this.selecionada = selecionada;
	}
	
	@Transient
	public String getItensOrdem() {
		return itensOrdem;
	}
	
	public void setItensOrdem(String itensOrdem) {
		this.itensOrdem = itensOrdem;
	}

	@Transient
	public boolean getPodeCortar() {
		return podeCortar;
	}
	
	public void setPodeCortar(boolean podeCortar) {
		this.podeCortar = podeCortar;
	}

	@Transient
	public String getFiliaisClientes() {
		return filiaisClientes;
	}
	
	public void setFilialCliente(String filiaisClientes) {
		this.filiaisClientes = filiaisClientes;
	}

	@Transient
	public String getTiposPedidos() {
		return tiposPedidos;
	}
	
	public void setTiposPedidos(String tiposPedidos) {
		this.tiposPedidos = tiposPedidos;
	}
	
}
