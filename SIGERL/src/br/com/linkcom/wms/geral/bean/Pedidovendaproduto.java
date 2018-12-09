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

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.service.VitempedidonaoliberadoService;

@Entity
@SequenceGenerator(name = "sq_pedidovendaproduto", sequenceName = "sq_pedidovendaproduto")
public class Pedidovendaproduto {

	protected Integer cdpedidovendaproduto;
	protected Pedidovenda pedidovenda;
	protected Deposito deposito;
	protected Produto produto;
	protected Pessoaendereco pessoaendereco;
	protected Long qtde;
	protected Money valor;
	protected Tipooperacao tipooperacao;
	protected Date dtprevisaoentrega;
	protected Boolean carregado = false;
	protected Date dtmarcacao;
	protected Timestamp dtexclusaoerp;
	protected Set<Carregamentoitem> listaCarregamentoitem = new ListSet<Carregamentoitem>(Carregamentoitem.class);
	protected Cliente filialEntrega; 
	protected Long idnota; 
	protected String serienota; 
	protected Long numeronota;
	protected Praca praca; 
	protected Deposito depositotransbordo;
	protected Cliente filialentregatransbordo;
	protected Long codigoerp;
	protected Cliente filialnota;
	protected String observacao;
	protected Boolean prioridade;
	protected Cliente filialsegundanota;
	protected Long idsegundanota;
	protected String seriesegundanota;
	protected Boolean itemfaturado;
	protected Date dtsegundofaturamento;
	protected Boolean corte;
	protected Pedidovendaprodutostatus pedidovendaprodutostatus;
	protected Tipopedido tipopedido;
	protected Empresa empresa;
	protected Long seqitem;
	protected Turnodeentrega turnodeentrega;
	
	
	// -------------------------- Construtores --------------------------
	public Pedidovendaproduto() {

	}

	/**
	 * Construtor criado para atender a reliberação de PVP em cargas finalizadas.
	 * 
	 * @author Filipe Santos
	 * @see VitempedidonaoliberadoService#liberarPedidos(String, String)
	 * @param pvp
	 */
	public Pedidovendaproduto(Pedidovendaproduto pvp){
		this.cdpedidovendaproduto = null;
		this.pedidovenda = pvp.getPedidovenda();
		this.deposito = pvp.getDeposito();
		this.produto = pvp.getProduto();
		this.pessoaendereco = pvp.getPessoaendereco();
		this.qtde = pvp.getQtde();
		this.valor = pvp.getValor();
		this.tipooperacao = pvp.getTipooperacao();
		this.dtprevisaoentrega = pvp.getDtprevisaoentrega();
		this.carregado = false;
		this.dtmarcacao = null;
		this.dtexclusaoerp = null;
		this.listaCarregamentoitem = new ListSet<Carregamentoitem>(Carregamentoitem.class);
		this.filialEntrega = pvp.getFilialEntrega(); 
		this.idnota = null; 
		this.serienota = null; 
		this.numeronota = null;
		this.praca = pvp.getPraca(); 
		this.depositotransbordo = pvp.getDepositotransbordo();
		this.filialentregatransbordo = pvp.getFilialentregatransbordo();
		this.codigoerp = pvp.getCodigoerp();
		this.filialnota = pvp.getFilialnota();
		this.observacao = pvp.getObservacao();
		this.prioridade = pvp.getPrioridade();
		this.tipopedido = pvp.getTipopedido();
		this.empresa = pvp.getEmpresa();
		this.seqitem = pvp.getSeqitem();
		this.turnodeentrega = pvp.getTurnodeentrega();
	}
	
	public Pedidovendaproduto(Integer cdpedidovendaproduto) {
		this.cdpedidovendaproduto = cdpedidovendaproduto;
	}

	public Pedidovendaproduto(Integer cdpedidovendaproduto, Pedidovenda pedidovenda, Produto produto, Pessoaendereco pessoaendereco, Tipooperacao tipooperacao) {
		this.cdpedidovendaproduto = cdpedidovendaproduto;
		this.pedidovenda = pedidovenda;
		this.produto = produto;
		this.pessoaendereco = pessoaendereco;
		this.tipooperacao = tipooperacao;
	}
	
	// -------------------------- Métodos get e set --------------------------
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pedidovendaproduto")
	public Integer getCdpedidovendaproduto() {
		return cdpedidovendaproduto;
	}
	
	public void setCdpedidovendaproduto(Integer id) {
		this.cdpedidovendaproduto = id;
	}
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpedidovenda")
	@Required
	public Pedidovenda getPedidovenda() {
		return pedidovenda;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	@Required
	public Produto getProduto() {
		return produto;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdenderecoentrega")
	@Required
	public Pessoaendereco getPessoaendereco() {
		return pessoaendereco;
	}
	
	@Required
	@DisplayName("Qtde.")
	public Long getQtde() {
		return qtde;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipooperacao")
	@Required
	public Tipooperacao getTipooperacao() {
		return tipooperacao;
	}
	
	@DisplayName("Previsão de entrega")
	public Date getDtprevisaoentrega() {
		return dtprevisaoentrega;
	}
	
	@OneToMany(mappedBy="pedidovendaproduto")
	public Set<Carregamentoitem> getListaCarregamentoitem() {
		return listaCarregamentoitem;
	}
	
	@DisplayName("Data de marcação")
	@Column(updatable = false, insertable = false)
	public Date getDtmarcacao() {
		return dtmarcacao;
	}
	
	@DisplayName("Data de exclusão")
	@Column(updatable = false, insertable = false)
	public Timestamp getDtexclusaoerp() {
		return dtexclusaoerp;
	}
	
	@DisplayName("Nº nota")
	@Column(updatable = false, insertable = false)
	public Long getNumeronota() {
		return numeronota;
	}
	
	@DisplayName("ID da nota fiscal")
	@Column(updatable = false, insertable = false)
	public Long getIdnota() {
		return idnota;
	}
	
	@DisplayName("Série da Nota Fiscal")
	@Column(updatable = false, insertable = false)
	public String getSerienota() {
		return serienota;
	}

	public Boolean getCarregado() {
		return carregado;
	}
	
	public Money getValor() {
		return valor;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdfilialentrega")
	public Cliente getFilialEntrega() {
		return filialEntrega;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpraca")
	public Praca getPraca() {
		return praca;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddepositotransbordo")
	public Deposito getDepositotransbordo() {
		return depositotransbordo;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdfilialentregatransbordo")
	public Cliente getFilialentregatransbordo() {
		return filialentregatransbordo;
	}
	
	@DisplayName("Código ERP")
	@Column(updatable = false, insertable = true)
	public Long getCodigoerp() {
		return this.codigoerp;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdfilialnota")
	public Cliente getFilialnota() {
		return filialnota;
	}
	
	@Column(updatable = false, insertable = true)
	public Boolean getPrioridade() {
		return prioridade;
	}

	public Long getIdsegundanota() {
		return idsegundanota;
	}

	public String getSeriesegundanota() {
		return seriesegundanota;
	}

	public Boolean getItemfaturado() {
		return itemfaturado;
	}	
	
	public Date getDtsegundofaturamento() {
		return dtsegundofaturamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdfilialsegundanota")
	public Cliente getFilialsegundanota() {
		return filialsegundanota;
	}
	
	public Boolean getCorte() {
		return corte;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpedidovendaprodutostatus")
	public Pedidovendaprodutostatus getPedidovendaprodutostatus() {
		return pedidovendaprodutostatus;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipopedido")	
	public Tipopedido getTipopedido() {
		return tipopedido;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdempresa")	
	public Empresa getEmpresa() {
		return empresa;
	}
	
	public Long getSeqitem() {
		return seqitem;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdturnodeentrega")
	public Turnodeentrega getTurnodeentrega() {
		return turnodeentrega;
	}

	
	//Set's
	public void setCorte(Boolean corte) {
		this.corte = corte;
	}

	public void setDtsegundofaturamento(Date dtsegundofaturamento) {
		this.dtsegundofaturamento = dtsegundofaturamento;
	}

	public void setIdsegundanota(Long idsegundanota) {
		this.idsegundanota = idsegundanota;
	}

	public void setSeriesegundanota(String seriesegundanota) {
		this.seriesegundanota = seriesegundanota;
	}

	public void setItemfaturado(Boolean itemfaturado) {
		this.itemfaturado = itemfaturado;
	}

	public void setFilialsegundanota(Cliente filialsegundanota) {
		this.filialsegundanota = filialsegundanota;
	}

	public void setPedidovenda(Pedidovenda pedidovenda) {
		this.pedidovenda = pedidovenda;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public void setPessoaendereco(Pessoaendereco pessoaendereco) {
		this.pessoaendereco = pessoaendereco;
	}
	
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	
	public void setValor(Money valor) {
		this.valor = valor;
	}
	
	public void setTipooperacao(Tipooperacao tipooperacao) {
		this.tipooperacao = tipooperacao;
	}
	
	public void setDtprevisaoentrega(Date dtprevisaoentrega) {
		this.dtprevisaoentrega = dtprevisaoentrega;
	}
	
	public void setCarregado(Boolean carregado) {
		this.carregado = carregado;
	}
	
	public void setListaCarregamentoitem(Set<Carregamentoitem> listaCarregamentoitem) {
		this.listaCarregamentoitem = listaCarregamentoitem;
	}
	
	public void setDtmarcacao(Date dtmarcacao) {
		this.dtmarcacao = dtmarcacao;
	}
	
	public void setDtexclusaoerp(Timestamp dtexclusaoerp) {
		this.dtexclusaoerp = dtexclusaoerp;
	}
	
	public void setFilialEntrega(Cliente filialEntrega) {
		this.filialEntrega = filialEntrega;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pedidovendaproduto) {
			Pedidovendaproduto pedidovendaproduto = (Pedidovendaproduto) obj;
			
			return pedidovendaproduto.getCdpedidovendaproduto().equals(this.getCdpedidovendaproduto());
		}
		
		return super.equals(obj);
	}

	public void setNumeronota(Long numeronota) {
		this.numeronota = numeronota;
	}
	
	public void setIdnota(Long idnotafiscal) {
		this.idnota = idnotafiscal;
	}
	
	public void setSerienota(String serienota) {
		this.serienota = serienota;
	}
	public void setPraca(Praca praca) {
		this.praca = praca;
	}
	
	public void setDepositotransbordo(Deposito depositotransbordo) {
		this.depositotransbordo = depositotransbordo;
	}
	
	public void setFilialentregatransbordo(Cliente filialentregatransbordo) {
		this.filialentregatransbordo = filialentregatransbordo;
	}
	
	public void setCodigoerp(Long codigoerp) {
		this.codigoerp = codigoerp;
	}
	
	public void setFilialnota(Cliente filialnota) {
		this.filialnota = filialnota;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacoes) {
		this.observacao = observacoes;
	}
	
	public void setPrioridade(Boolean prioridade) {
		this.prioridade = prioridade;
	}

	public void setPedidovendaprodutostatus(Pedidovendaprodutostatus pedidovendaprodutostatus) {
		this.pedidovendaprodutostatus = pedidovendaprodutostatus;
	}

	public void setTipopedido(Tipopedido tipopedido) {
		this.tipopedido = tipopedido;
	}

	public void setEmpresa(Empresa emmpresa) {
		this.empresa = emmpresa;
	}

	public void setSeqitem(Long seqitem) {
		this.seqitem = seqitem;
	}

	public void setTurnodeentrega(Turnodeentrega turnodeentrega) {
		this.turnodeentrega = turnodeentrega;
	}
	
}
