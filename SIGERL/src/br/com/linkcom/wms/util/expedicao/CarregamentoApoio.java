package br.com.linkcom.wms.util.expedicao;

import java.sql.Date;
import java.util.List;

import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Pedidovendaproduto;

public class CarregamentoApoio {

	protected Integer pai;
	protected Integer index;
	protected Integer nivel;
	protected String rota;
	protected String praca;
	protected String tipooperacao;
	protected String cliente;
	protected String pedidovenda;
	protected Integer cdpedidovenda;
	protected String produto;
	protected String descricao;
	protected Integer totalItens = 0;
	protected Double peso = 0.0;
	protected Double cubagem = 0.0;
	protected Money valor = new Money();
	protected String collapsed = "";
	protected Date date;
	protected String styleClass = "";
	protected Integer cdpedidovendaproduto;
	protected Long cdformacaocarga;
	protected String cep;
	protected String numeropedido;
	protected Cliente filialEntrega;
	
	protected Integer cdpessoa;
	protected Integer cdpraca;
	protected Integer cdtipooperacao;
	protected String nomedepositotransbordo;
	
	protected List<Pedidovendaproduto> listaPedidoVendaProduto = new ListSet<Pedidovendaproduto>(Pedidovendaproduto.class);

	public Integer getPai() {
		return pai;
	}
	public Integer getIndex() {
		return index;
	}
	public Integer getNivel() {
		return nivel;
	}
	public String getRota() {
		return rota;
	}
	public String getPraca() {
		return praca;
	}
	public String getTipooperacao() {
		return tipooperacao;
	}
	public String getCliente() {
		return cliente;
	}
	public String getPedidovenda() {
		return pedidovenda;
	}
	public String getProduto() {
		return produto;
	}
	public String getDescricao() {
		return descricao;
	}
	public Integer getTotalItens() {
		return totalItens;
	}
	public Double getPeso() {
		return peso;
	}
	public Double getCubagem() {
		return cubagem;
	}
	public Money getValor() {
		return valor;
	}
	public List<Pedidovendaproduto> getListaPedidoVendaProduto() {
		return listaPedidoVendaProduto;
	}
	
	public Integer getCdpessoa() {
		return cdpessoa;
	}
	public Integer getCdpraca() {
		return cdpraca;
	}
	public Integer getCdtipooperacao() {
		return cdtipooperacao;
	}
	public String getCollapsed() {
		return collapsed;
	}
	public Date getDate() {
		return date;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public Integer getCdpedidovendaproduto() {
		return cdpedidovendaproduto;
	}
	public Long getCdformacaocarga() {
		return cdformacaocarga;
	}
	public String getCep() {
		return cep;
	}
	public String getNumeropedido() {
		return numeropedido;
	}
	public Cliente getFilialEntrega() {
		return filialEntrega;
	}
	public Integer getCdpedidovenda() {
		return cdpedidovenda;
	}
	public void setCdpedidovenda(Integer cdpedidovenda) {
		this.cdpedidovenda = cdpedidovenda;
	}
	public void setPai(Integer pai) {
		this.pai = pai;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}
	public void setRota(String rota) {
		this.rota = rota;
	}
	public void setPraca(String praca) {
		this.praca = praca;
	}
	public void setTipooperacao(String tipooperacao) {
		this.tipooperacao = tipooperacao;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public void setPedidovenda(String pedidovenda) {
		this.pedidovenda = pedidovenda;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setTotalItens(Integer totalItens) {
		this.totalItens = totalItens;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	public void setCubagem(Double cubagem) {
		this.cubagem = cubagem;
	}
	public void setValor(Money valor) {
		this.valor = valor;
	}
	public void setCollapsed(String collapsed) {
		this.collapsed = collapsed;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	public void setCdpedidovendaproduto(Integer cdpedidovendaproduto) {
		this.cdpedidovendaproduto = cdpedidovendaproduto;
	}
	public void setCdformacaocarga(Long cdformacaocarga) {
		this.cdformacaocarga = cdformacaocarga;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public void setNumeropedido(String numeropedido) {
		this.numeropedido = numeropedido;
	}
	public void setFilialEntrega(Cliente filialEntrega) {
		this.filialEntrega = filialEntrega;
	}
	public void setCdpessoa(Integer cdpessoa) {
		this.cdpessoa = cdpessoa;
	}
	public void setCdpraca(Integer cdpraca) {
		this.cdpraca = cdpraca;
	}
	public void setCdtipooperacao(Integer cdtipooperacao) {
		this.cdtipooperacao = cdtipooperacao;
	}
	public void setListaPedidoVendaProduto(
			List<Pedidovendaproduto> listaPedidoVendaProduto) {
		this.listaPedidoVendaProduto = listaPedidoVendaProduto;
	}
	public String getNomedepositotransbordo() {
		return nomedepositotransbordo;
	}
	public void setNomedepositotransbordo(String nomedepositotransbordo) {
		this.nomedepositotransbordo = nomedepositotransbordo;
	}
	
}
