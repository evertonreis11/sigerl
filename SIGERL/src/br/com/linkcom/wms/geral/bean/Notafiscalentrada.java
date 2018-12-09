package br.com.linkcom.wms.geral.bean;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
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
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_notafiscalentrada", sequenceName = "sq_notafiscalentrada")
public class Notafiscalentrada {

	protected Integer cdnotafiscalentrada;
	protected Long codigoerp;
	protected Notafiscaltipo notafiscaltipo;
	protected Fornecedor fornecedor;
	protected java.sql.Date dtemissao;
	protected java.sql.Date dtcancelamento;
	protected java.sql.Date dtlancamento = new Date(System.currentTimeMillis());
	protected java.sql.Timestamp dtchegada;
	protected String transportador;
	protected String veiculo;
	protected String numero;
	protected Deposito deposito;
	protected Boolean devolvida = false;
	protected Timestamp dtsincronizacao;
	protected Set<Notafiscalentradaproduto> listaNotafiscalentradaproduto = new ListSet<Notafiscalentradaproduto>(Notafiscalentradaproduto.class);
	protected Set<Recebimentonotafiscal> listaRecebimentonotafiscalentrada = new ListSet<Recebimentonotafiscal>(Recebimentonotafiscal.class);
	protected Agenda agenda;
	protected Pedidocompra pedidocompra;
	protected Acompanhamentoveiculo acompanhamentoveiculo;
	
	/* Propriedades Transientes */
	protected Boolean situacao;	
	protected String serie;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_notafiscalentrada")
	public Integer getCdnotafiscalentrada() {
		return cdnotafiscalentrada;
	}
	public void setCdnotafiscalentrada(Integer id) {
		this.cdnotafiscalentrada = id;
	}

	
	@Required
	@MaxLength(10)
	public Long getCodigoerp() {
		return codigoerp;
	}
	
	@Required
	@DisplayName("Tipo")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdnotafiscaltipo")
	public Notafiscaltipo getNotafiscaltipo() {
		return notafiscaltipo;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpessoa")
	@Required
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdagenda")
	public Agenda getAgenda() {
		return agenda;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpedidocompra")
	public Pedidocompra getPedidocompra() {
		return pedidocompra;
	}
	
	@DisplayName("Data de emissão")
	@Required
	public java.sql.Date getDtemissao() {
		return dtemissao;
	}
	
	@DisplayName("Data de cancelamento")
	public java.sql.Date getDtcancelamento() {
		return dtcancelamento;
	}
	
	@DisplayName("Data de chegada")
	public java.sql.Timestamp getDtchegada() {
		return dtchegada;
	}
	
	@Required
	@DisplayName("Data de lançamento")
	public java.sql.Date getDtlancamento() {
		return dtlancamento;
	}
	
	@MaxLength(100)
	@Required
	public String getTransportador() {
		return transportador;
	}

	@MaxLength(7)
	@DisplayName("Veículo")
	@Required
	public String getVeiculo() {
		return veiculo;
	}
	
	@MaxLength(15)
	@Required
	@DisplayName("Número")
	public String getNumero() {
		return numero;
	}
	
	@Required
	public Boolean getDevolvida() {
		return devolvida;
	}
	
	@Column(updatable=false, insertable=false)
	public Timestamp getDtsincronizacao() {
		return dtsincronizacao;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@OneToMany(mappedBy="notafiscalentrada")
	public Set<Notafiscalentradaproduto> getListaNotafiscalentradaproduto() {
		return listaNotafiscalentradaproduto;
	}
	
	@OneToMany(mappedBy="notafiscalentrada")
	public Set<Recebimentonotafiscal> getListaRecebimentonotafiscalentrada() {
		return listaRecebimentonotafiscalentrada;
	}
	
	@Transient
	public Boolean getSituacao() {
		return situacao;
	}
	
	@Transient
	@DisplayName("Status")
	public String getStatusnota(){
		if (this.dtcancelamento != null)
			return "Cancelado";
		else if (this.getListaRecebimentonotafiscalentrada() != null && 
				this.getListaRecebimentonotafiscalentrada().size() > 0){
			return "Recebido";
		}else
			return "Emitido";
	}
	
	@MaxLength(10)
	@DisplayName("Série")
	public String getSerie() {
		return serie;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "veiculo", referencedColumnName = "placaveiculo",insertable = false, updatable = false)	
	public Acompanhamentoveiculo getAcompanhamentoveiculo() {
		return acompanhamentoveiculo;
	}
	
	public void setAcompanhamentoveiculo(Acompanhamentoveiculo acompanhamentoveiculo) {
		this.acompanhamentoveiculo = acompanhamentoveiculo;
	}
	
	public void setCodigoerp(Long codigoerp) {
		this.codigoerp = codigoerp;
	}
	
	public void setNotafiscaltipo(Notafiscaltipo notafiscaltipo) {
		this.notafiscaltipo = notafiscaltipo;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}
	
	public void setPedidocompra(Pedidocompra pedidocompra) {
		this.pedidocompra = pedidocompra;
	}
	
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public void setDtemissao(java.sql.Date dtemissao) {
		this.dtemissao = dtemissao;
	}
	
	public void setDtcancelamento(java.sql.Date dtcancelamento) {
		this.dtcancelamento = dtcancelamento;
	}
	
	public void setDtchegada(java.sql.Timestamp dtchegada) {
		this.dtchegada = dtchegada;
	}
	
	public void setDtlancamento(java.sql.Date dtlancamento) {
		this.dtlancamento = dtlancamento;
	}
	
	public void setTransportador(String transportador) {
		this.transportador = transportador;
	}
	
	public void setVeiculo(String veiculo) {
		this.veiculo = veiculo;
	}
	
	public void setListaNotafiscalentradaproduto(Set<Notafiscalentradaproduto> listaNotafiscalentradaproduto) {
		this.listaNotafiscalentradaproduto = listaNotafiscalentradaproduto;
	}
	
	public void setListaRecebimentonotafiscalentrada(Set<Recebimentonotafiscal> listaRecebimentonotafiscalentrada) {
		this.listaRecebimentonotafiscalentrada = listaRecebimentonotafiscalentrada;
	}
	
	public void setDevolvida(Boolean devolvida) {
		this.devolvida = devolvida;
	}
	
	public void setDtsincronizacao(Timestamp dtsincronizacao) {
		this.dtsincronizacao = dtsincronizacao;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public void setSituacao(Boolean situacao) {
		this.situacao = situacao;
	}
	
	public void setSerie(String serie) {
		this.serie = serie;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Notafiscalentrada){
			return ((Notafiscalentrada)obj).getCdnotafiscalentrada().equals(this.cdnotafiscalentrada);
		}
		return super.equals(obj);
	}
}
