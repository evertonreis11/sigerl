package br.com.linkcom.wms.geral.bean;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Money;

@Entity
@SequenceGenerator(name = "carregamentoView", sequenceName = "carregamentoView")
public class Vformacaocarga {

	protected Long cdformacaocarga;
	protected Integer cdrota;
	protected String rotanome;
	protected Integer ordem;
	protected Praca praca;
	protected String pracanome;
	protected Tipooperacao tipooperacao;
	protected String tipooperacaonome;
	protected String cep;
	protected Cliente pessoa;
	protected String pessoanome;
	protected Integer qtde;
	protected Double peso;
	protected Double cubagem;
	protected Money valor;
	protected Deposito deposito;
	protected Pedidovenda pedidovenda;
	protected String numero;
	protected Integer cdpessoa;
	protected Integer cdpedidovenda;
	protected Integer cdfilialentrega;
	protected String filialentreganome;
	protected Boolean troca;
	protected Integer cddepositotransbordo;
	protected Integer cdfilialtransbordo;
	protected String depositotransbordonome;
	protected String filialtransbordonome;
	protected String filialemissao;
	protected Boolean prioridade;
	protected Date datavenda;
//	protected Integer cdlinhaseparacao;
	protected Integer cdturnodeentrega;

	
	public Integer getCdfilialentrega() {
		return cdfilialentrega;
	}

	@Id
	@GeneratedValue(generator = "carregamentoView", strategy = GenerationType.AUTO)
	public Long getCdformacaocarga() {
		return cdformacaocarga;
	}

	@Column(insertable = false, updatable = false)
	public Integer getCdpedidovenda() {
		return cdpedidovenda;
	}

	@Column(insertable = false, updatable = false)
	public Integer getCdpessoa() {
		return cdpessoa;
	}

	public Integer getCdrota() {
		return cdrota;
	}

	public String getCep() {
		return cep;
	}

	public Double getCubagem() {
		return cubagem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}

	public String getFilialentreganome() {
		return filialentreganome;
	}

	public String getNumero() {
		return numero;
	}

	public Integer getOrdem() {
		return ordem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpedidovenda")
	public Pedidovenda getPedidovenda() {
		return pedidovenda;
	}

	public Double getPeso() {
		return peso;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpessoa")
	public Cliente getPessoa() {
		return pessoa;
	}

	public String getPessoanome() {
		return pessoanome;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpraca")
	public Praca getPraca() {
		return praca;
	}

	public String getPracanome() {
		return pracanome;
	}

	public Integer getQtde() {
		return qtde;
	}

	public String getRotanome() {
		return rotanome;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cdtipooperacao")
	public Tipooperacao getTipooperacao() {
		return tipooperacao;
	}

	public String getTipooperacaonome() {
		return tipooperacaonome;
	}

	public Boolean getTroca() {
		return troca;
	}
	
	public Integer getCddepositotransbordo() {
		return cddepositotransbordo;
	}
	
	public Integer getCdfilialtransbordo() {
		return cdfilialtransbordo;
	}
	
	@DisplayName("Depósito de transbordo")
	public String getDepositotransbordonome() {
		return depositotransbordonome;
	}
	
	@DisplayName("Filial de transbordo")
	public String getFilialtransbordonome() {
		return filialtransbordonome;
	}

	public Money getValor() {
		return valor;
	}
	
	public Date getDatavenda() {
		return datavenda;
	}
	
	public Integer getCdturnodeentrega() {
		return cdturnodeentrega;
	}

//	public Integer getCdlinhaseparacao() {
//		return cdlinhaseparacao;
//	}
//
//	public void setCdlinhaseparacao(Integer cdlinhaseparacao) {
//		this.cdlinhaseparacao = cdlinhaseparacao;
//	}

	public void setCdturnodeentrega(Integer cdturnodeentrega) {
		this.cdturnodeentrega = cdturnodeentrega;
	}
	
	public void setDatavenda(Date datavenda) {
		this.datavenda = datavenda;
	}

	public void setCdfilialentrega(Integer cdfilialentrega) {
		this.cdfilialentrega = cdfilialentrega;
	}

	public void setCdformacaocarga(Long cdformacaocarga) {
		this.cdformacaocarga = cdformacaocarga;
	}

	public void setCdpedidovenda(Integer cdpedidovenda) {
		this.cdpedidovenda = cdpedidovenda;
	}

	public void setCdpessoa(Integer cdpessoa) {
		this.cdpessoa = cdpessoa;
	}

	public void setCdrota(Integer cdrota) {
		this.cdrota = cdrota;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public void setCubagem(Double cubagem) {
		this.cubagem = cubagem;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setFilialentreganome(String filialentreganome) {
		this.filialentreganome = filialentreganome;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setPedidovenda(Pedidovenda pedidovenda) {
		this.pedidovenda = pedidovenda;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public void setPessoa(Cliente pessoa) {
		this.pessoa = pessoa;
	}

	public void setPessoanome(String pessoanome) {
		this.pessoanome = pessoanome;
	}

	public void setPraca(Praca praca) {
		this.praca = praca;
	}

	public void setPracanome(String pracanome) {
		this.pracanome = pracanome;
	}

	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}

	public void setRotanome(String rotanome) {
		this.rotanome = rotanome;
	}

	public void setTipooperacao(Tipooperacao tipooperacao) {
		this.tipooperacao = tipooperacao;
	}

	public void setTipooperacaonome(String tipooperacaonome) {
		this.tipooperacaonome = tipooperacaonome;
	}

	public void setTroca(Boolean troca) {
		this.troca = troca;
	}
	
	public void setCddepositotransbordo(Integer cddepositotransbordo) {
		this.cddepositotransbordo = cddepositotransbordo;
	}
	
	public void setCdfilialtransbordo(Integer cdfilialtransbordo) {
		this.cdfilialtransbordo = cdfilialtransbordo;
	}
	
	public void setDepositotransbordonome(String depositotransbordonome) {
		this.depositotransbordonome = depositotransbordonome;
	}
	
	public void setFilialtransbordonome(String filialtransbordonome) {
		this.filialtransbordonome = filialtransbordonome;
	}

	public void setValor(Money valor) {
		this.valor = valor;
	}

	public String getFilialemissao() {
		return filialemissao;
	}
	
	public void setFilialemissao(String filialemissao) {
		this.filialemissao = filialemissao;
	}
	
	public Boolean getPrioridade() {
		return prioridade;
	}
	
	public void setPrioridade(Boolean prioridade) {
		this.prioridade = prioridade;
	}
	
}
