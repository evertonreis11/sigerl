package br.com.linkcom.wms.geral.bean.view;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Money;

@Entity
@Immutable
public class Vpedidovendareport{
	
	private String chave;
	private Integer cdpedidovenda;
	private String numero;
	private Date dtemissao;
	private Date dtchegadaerp;
	private String cliente;
	private Integer qtde;
	private String codigoproduto;
	private Date dtprevisaoentrega;
	private String descricaoproduto;
	private Integer cdcarregamento;
	private Integer cdbox;
	private String box;
	private Integer cdboxstatus;
	private String boxstatus;
	private String deposito;
	private String tipooperacao;
	private String cubagem;
	private String carregamentostatus;
	private Integer cdpedidovendaproduto;
	private Integer cdcliente;
	private Integer cdproduto;
	private Integer cddeposito;
	private Integer cdtipooperacao;
	private Integer cdcarregamentoitem;
	private Integer cdcarregamentostatus;
	private Date dtfaturamento;
	private String turnodeentrega;
	private Integer cdturnodeentrega;
	private String rota;
	private Integer cdrota;
	private Money valorproduto;
	private Integer cdtiporotapraca;
	private Integer empresa;
	private Integer pedido_mvlojas;
	private Integer loja_mvlojas;
	private Date dtmontagemcarga;
	private Date dtfinalizacaocarga;
	private Date dtsaidaportaria;
	private Date dtconfirmacaoCDA;
	private Date dtconfirmacaoLJ;
	private Date dtfaturamentocliente;
	
	
	//Get's
	@Id
	@DisplayName("Chave")
	public String getChave() {
		return chave;
	}
	@DisplayName("ID Pedido Venda")
	public Integer getCdpedidovenda() {
		return cdpedidovenda;
	}
	@DisplayName("Número do Pedido")
	public String getNumero() {
		return numero;
	}
	@DisplayName("Dt. de Emissão do Pedido")
	public Date getDtemissao() {
		return dtemissao;
	}
	@DisplayName("Dt. de Chegada do Pedido no WMS")
	public Date getDtchegadaerp() {
		return dtchegadaerp;
	}
	@DisplayName("Nome do Cliente")
	public String getCliente() {
		return cliente;
	}
	@DisplayName("Quantidade de peças Vendidas")
	public Integer getQtde() {
		return qtde;
	}
	@DisplayName("Código do Produto (WMS)")
	public String getCodigoproduto() {
		return codigoproduto;
	}
	@DisplayName("Dt. de Previsão de Entrega do Pedido")
	public Date getDtprevisaoentrega() {
		return dtprevisaoentrega;
	}
	@DisplayName("Descrição do Produto")
	public String getDescricaoproduto() {
		return descricaoproduto;
	}
	@DisplayName("ID do Carregamento")
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	@DisplayName("ID do Box")
	public Integer getCdbox() {
		return cdbox;
	}
	@DisplayName("Nome do Box")
	public String getBox() {
		return box;
	}
	@DisplayName("ID do Status do Box")
	public Integer getCdboxstatus() {
		return cdboxstatus;
	}
	@DisplayName("Nome do Status do Box")
	public String getBoxstatus() {
		return boxstatus;
	}
	@DisplayName("Nome do Depósito")
	public String getDeposito() {
		return deposito;
	}
	@DisplayName("Nome do Tipo de Operação")
	public String getTipooperacao() {
		return tipooperacao;
	}
	@DisplayName("Valor da Cubagem")
	public String getCubagem() {
		return cubagem;
	}
	@DisplayName("Nome do Status do Carregamento")
	public String getCarregamentostatus() {
		return carregamentostatus;
	}
	@DisplayName("ID do Item vinculado ao Pedido")
	public Integer getCdpedidovendaproduto() {
		return cdpedidovendaproduto;
	}
	@DisplayName("Nome do Cliente")
	public Integer getCdcliente() {
		return cdcliente;
	}
	@DisplayName("ID do Produto")
	public Integer getCdproduto() {
		return cdproduto;
	}
	@DisplayName("ID do Depósito")
	public Integer getCddeposito() {
		return cddeposito;
	}
	@DisplayName("ID do Tipo de Operação")
	public Integer getCdtipooperacao() {
		return cdtipooperacao;
	}
	@DisplayName("ID do Item do Carregamento")
	public Integer getCdcarregamentoitem() {
		return cdcarregamentoitem;
	}
	@DisplayName("ID do Status do Carregamento")
	public Integer getCdcarregamentostatus() {
		return cdcarregamentostatus;
	}
	@DisplayName("Dt. do Faturamento")
	public Date getDtfaturamento() {
		return dtfaturamento;
	}
	@DisplayName("Nome do Turno de Entrega")
	public String getTurnodeentrega() {
		return turnodeentrega;
	}
	@DisplayName("ID do Turno de Entrega")
	public Integer getCdturnodeentrega() {
		return cdturnodeentrega;
	}
	@DisplayName("Nome da Rota")
	public String getRota() {
		return rota;
	}
	@DisplayName("ID da Rota")
	public Integer getCdrota() {
		return cdrota;
	}
	@DisplayName("Valor do Produto")
	public Money getValorproduto() {
		return valorproduto;
	}
	@DisplayName("Tipo de Entrega")
	public Integer getCdtiporotapraca() {
		return cdtiporotapraca;
	}
	public Integer getEmpresa() {
		return empresa;
	}
	public Integer getPedido_mvlojas() {
		return pedido_mvlojas;
	}
	public Integer getLoja_mvlojas() {
		return loja_mvlojas;
	}
	@DisplayName("Dt montagem da Carga")
	public Date getDtmontagemcarga() {
		return dtmontagemcarga;
	}
	@DisplayName("Finalizada em")
	public Date getDtfinalizacaocarga() {
		return dtfinalizacaocarga;
	}
	@DisplayName("Saida Portaria")
	public Date getDtsaidaportaria() {
		return dtsaidaportaria;
	}
	@DisplayName("Dt Confirmação CDA")
	public Date getDtconfirmacaoCDA() {
		return dtconfirmacaoCDA;
	}
	@DisplayName("Dt confirmação LJ")
	public Date getDtconfirmacaoLJ() {
		return dtconfirmacaoLJ;
	}
	@DisplayName("Dt Faturamento Cliente")
	public Date getDtfaturamentocliente() {
		return dtfaturamentocliente;
	}

	
	//Set's
	public void setCdpedidovenda(Integer cdpedidovenda) {
		this.cdpedidovenda = cdpedidovenda;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public void setDtemissao(Date dtemissao) {
		this.dtemissao = dtemissao;
	}
	public void setDtchegadaerp(Date dtchegadaerp) {
		this.dtchegadaerp = dtchegadaerp;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	public void setCodigoproduto(String codigoproduto) {
		this.codigoproduto = codigoproduto;
	}
	public void setDtprevisaoentrega(Date dtprevisaoentrega) {
		this.dtprevisaoentrega = dtprevisaoentrega;
	}
	public void setDescricaoproduto(String descricaoproduto) {
		this.descricaoproduto = descricaoproduto;
	}
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}
	public void setCdbox(Integer cdbox) {
		this.cdbox = cdbox;
	}
	public void setBox(String box) {
		this.box = box;
	}
	public void setCdboxstatus(Integer cdboxstatus) {
		this.cdboxstatus = cdboxstatus;
	}
	public void setBoxstatus(String boxstatus) {
		this.boxstatus = boxstatus;
	}
	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}
	public void setTipooperacao(String tipooperacao) {
		this.tipooperacao = tipooperacao;
	}
	public void setCubagem(String cubagem) {
		this.cubagem = cubagem;
	}
	public void setCarregamentostatus(String carregamentostatus) {
		this.carregamentostatus = carregamentostatus;
	}
	public void setCdpedidovendaproduto(Integer cdpedidovendaproduto) {
		this.cdpedidovendaproduto = cdpedidovendaproduto;
	}
	public void setCdcliente(Integer cdcliente) {
		this.cdcliente = cdcliente;
	}
	public void setCdproduto(Integer cdproduto) {
		this.cdproduto = cdproduto;
	}
	public void setCddeposito(Integer cddeposito) {
		this.cddeposito = cddeposito;
	}
	public void setCdtipooperacao(Integer cdtipooperacao) {
		this.cdtipooperacao = cdtipooperacao;
	}
	public void setCdcarregamentoitem(Integer cdcarregamentoitem) {
		this.cdcarregamentoitem = cdcarregamentoitem;
	}
	public void setCdcarregamentostatus(Integer cdcarregamentostatus) {
		this.cdcarregamentostatus = cdcarregamentostatus;
	}
	public void setDtfaturamento(Date dtfaturamento) {
		this.dtfaturamento = dtfaturamento;
	}
	public void setTurnodeentrega(String turnodeentrega) {
		this.turnodeentrega = turnodeentrega;
	}
	public void setCdturnodeentrega(Integer cdturnodeentrega) {
		this.cdturnodeentrega = cdturnodeentrega;
	}
	public void setRota(String rota) {
		this.rota = rota;
	}
	public void setCdrota(Integer cdrota) {
		this.cdrota = cdrota;
	}
	public void setValorproduto(Money valorproduto) {
		this.valorproduto = valorproduto;
	}
	public void setCdtiporotapraca(Integer cdtiporotapraca) {
		this.cdtiporotapraca = cdtiporotapraca;
	}
	public void setEmpresa(Integer empresa) {
		this.empresa = empresa;
	}
	public void setPedido_mvlojas(Integer pedidoMvlojas) {
		pedido_mvlojas = pedidoMvlojas;
	}
	public void setLoja_mvlojas(Integer lojaMvlojas) {
		loja_mvlojas = lojaMvlojas;
	}
	public void setDtmontagemcarga(Date dtmontagemcarga) {
		this.dtmontagemcarga = dtmontagemcarga;
	}
	public void setDtfinalizacaocarga(Date dtfinalizacaocarga) {
		this.dtfinalizacaocarga = dtfinalizacaocarga;
	}
	public void setDtsaidaportaria(Date dtsaidaportaria) {
		this.dtsaidaportaria = dtsaidaportaria;
	}
	public void setDtconfirmacaoCDA(Date dtconfirmacaoCDA) {
		this.dtconfirmacaoCDA = dtconfirmacaoCDA;
	}
	public void setDtconfirmacaoLJ(Date dtconfirmacaoLJ) {
		this.dtconfirmacaoLJ = dtconfirmacaoLJ;
	}
	public void setDtfaturamentocliente(Date dtfaturamentocliente) {
		this.dtfaturamentocliente = dtfaturamentocliente;
	}
	
	/**
	 * Valida se o tipo de operação é um 'Entrega Cliente'
	 * @return
	 */
	@Transient
	public boolean isEntregaCliente() {
		if(cdtipooperacao!=null){
			if(cdtipooperacao==1){
				return true;
			}else if(cdtipooperacao==3){
				return true;
			}else if(cdtipooperacao==4){
				return true;
			}else if(cdtipooperacao==8){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
}
