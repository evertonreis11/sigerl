package br.com.linkcom.wms.geral.bean.view;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.wms.geral.bean.Deposito;


@Entity
@Table(name="VITEMPEDIDO_V2")
@Immutable
public class Vitempedido {

	private String chave;
	private Integer cdcarregamentoitem;
	private Integer cdpedidovendaproduto;
	private Integer cdpedidovenda;
	private Long qtdeconfirmada;
	private String numero;
	private Long codigoerp;
	private Long numeronota;
	private String serienota;
	private Date dtmarcacao;
	private Date dtexclusaoerp;
	private Long qtde;
	private String codigo;
	private String descricao;
	private Integer cdcarregamento;
	private String status;
	private Integer cdtipooperacao;
	private String operacao;
	private Deposito deposito;
	private Date dtfaturamento;
	private String transportador;
	private String placa;
	private String seriesegundanota;
	private Long numerosegundanota;
	private Boolean usaminicd;
	private String transportadorPlaca;
	private Integer cdexpedicao;
	private String numerosegundanotaReport;

	@Id
	public String getChave() {
		return chave;
	}
	
	@Column(insertable=false,updatable=false)
	public Integer getCdcarregamentoitem() {
		return cdcarregamentoitem;
	}
	@Column(insertable=false,updatable=false)
	public Integer getCdpedidovendaproduto() {
		return cdpedidovendaproduto;
	}
	public Integer getCdpedidovenda() {
		return cdpedidovenda;
	}
	@DisplayName("Qtde Confirmada")
	public Long getQtdeconfirmada() {
		return qtdeconfirmada;
	}
	@DisplayName("Nº do pedido")
	public String getNumero() {
		return numero;
	}
	@DisplayName("Nº da nota")
	public Long getNumeronota() {
		return numeronota;
	}
	@DisplayName("Série")
	public String getSerienota() {
		return serienota;
	}
	public Date getDtmarcacao() {
		return dtmarcacao;
	}
	public Date getDtexclusaoerp() {
		return dtexclusaoerp;
	}
	@DisplayName("Qtde Esperada")
	public Long getQtde() {
		return qtde;
	}
	@DisplayName("Código")
	public String getCodigo() {
		return codigo;
	}
	@DisplayName("Descrição")
	public String getDescricao() {
		return descricao;
	}
	@DisplayName("Carregamento")
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	@DisplayName("Status do carregamento")
	public String getStatus() {
		return status;
	}
	@DisplayName("Tipo")
	public String getOperacao() {
		return operacao;
	}
	public Long getCodigoerp() {
		return codigoerp;
	}
	public Integer getCdtipooperacao() {
		return cdtipooperacao;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	@DisplayName("Faturado em")
	public Date getDtfaturamento() {
		return dtfaturamento;
	}
	public String getPlaca() {
		return placa;
	}
	public String getSeriesegundanota() {
		return seriesegundanota;
	}
	public Long getNumerosegundanota() {
		return numerosegundanota;
	}
	public Boolean getUsaminicd() {
		return usaminicd;
	}
	@Transient
	public String getTransportadorPlaca() {
		transportadorPlaca = transportador+" - "+placa;
		return transportadorPlaca;
	}
	public Integer getCdexpedicao() {
		return cdexpedicao;
	}
	@Transient
	public String getNumerosegundanotaReport() {
		return numerosegundanotaReport;
	}
	
	public void setChave(String identificador) {
		this.chave = identificador;
	}
	public void setQtdeconfirmada(Long qtdeconfirmada) {
		this.qtdeconfirmada = qtdeconfirmada;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public void setNumeronota(Long numeronota) {
		this.numeronota = numeronota;
	}
	public void setSerienota(String serienota) {
		this.serienota = serienota;
	}
	public void setDtmarcacao(Date dtmarcacao) {
		this.dtmarcacao = dtmarcacao;
	}
	public void setDtexclusaoerp(Date dtexclusaoerp) {
		this.dtexclusaoerp = dtexclusaoerp;
	}
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	public void setCdpedidovendaproduto(Integer cdpedidovendaproduto) {
		this.cdpedidovendaproduto = cdpedidovendaproduto;
	}
	public void setCdpedidovenda(Integer cdpedidovenda) {
		this.cdpedidovenda = cdpedidovenda;
	}
	public void setCodigoerp(Long codigoerp) {
		this.codigoerp = codigoerp;
	}
	public void setCdtipooperacao(Integer cdtipooperacao) {
		this.cdtipooperacao = cdtipooperacao;
	}
	public void setCdcarregamentoitem(Integer cdcarregamentoitem) {
		this.cdcarregamentoitem = cdcarregamentoitem;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setDtfaturamento(Date dtfaturamento) {
		this.dtfaturamento = dtfaturamento;
	}
	public String getTransportador() {
		return transportador;
	}
	public void setTransportador(String transportador) {
		this.transportador = transportador;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public void setSeriesegundanota(String seriesegundanota) {
		this.seriesegundanota = seriesegundanota;
	}
	public void setNumerosegundanota(Long numerosegundanota) {
		this.numerosegundanota = numerosegundanota;
	}
	public void setUsaminicd(Boolean usaminicd) {
		this.usaminicd = usaminicd;
	}
	public void setTransportadorPlaca(String transportadorPlaca) {
		this.transportadorPlaca = transportadorPlaca;
	}
	public void setCdexpedicao(Integer cdexpedicao) {
		this.cdexpedicao = cdexpedicao;
	}
	public void setNumerosegundanotaReport(String numerosegundanotaReport) {
		this.numerosegundanotaReport = numerosegundanotaReport;
	}
	
}
