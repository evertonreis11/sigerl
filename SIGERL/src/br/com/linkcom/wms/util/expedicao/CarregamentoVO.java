package br.com.linkcom.wms.util.expedicao;

import java.sql.Date;

import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Tipooperacao;

public class CarregamentoVO {

	protected Integer pai;
	protected Integer index;
	protected Integer nivel;
	protected Rota rota = new Rota();
	protected Praca praca = new Praca();
	protected Tipooperacao tipooperacao = new Tipooperacao();
	protected Cliente cliente = new Cliente();
	protected Produto produto = new Produto();
	protected String descricao;
	protected Integer totalItens = 0;
	protected Double peso = 0.0;
	protected Double cubagem = 0.0;
	protected Money valor = new Money();
	protected String collapsed = "";
	protected Date date;
	protected String styleClass = "";
	protected Integer cdpedidovenda;
	protected Integer cdpedidovendaproduto;
	protected Long cdformacaocarga;
	protected String cep;
	protected String numeropedido;
	protected Cliente filialEntrega;
	protected String nomedepositotransbordo;
	protected String filialemissao;
	protected Boolean prioridade;
	protected String datavenda;
	
	public Cliente getFilialEntrega() {
		return filialEntrega;
	}
	public void setFilialEntrega(Cliente filialEntrega) {
		this.filialEntrega = filialEntrega;
	}
	public String getNumeropedido() {
		return numeropedido;
	}
	public void setNumeropedido(String numeropedido) {
		this.numeropedido = numeropedido;
	}
	public Integer getPai() {
		return pai;
	}
	public Integer getIndex() {
		return index;
	}
	public Integer getNivel() {
		return nivel;
	}
	public Rota getRota() {
		return rota;
	}
	public Praca getPraca() {
		return praca;
	}
	public Tipooperacao getTipooperacao() {
		return tipooperacao;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public Produto getProduto() {
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
	public String getStyleClass() {
		return styleClass;
	}
	public String getCollapsed() {
		return collapsed;
	}
	
	public Date getDate() {
		return date;
	}
	public Integer getCdpedidovendaproduto() {
		return cdpedidovendaproduto;
	}
	public Integer getCdpedidovenda() {
		return cdpedidovenda;
	}
	public Long getCdformacaocarga() {
		return cdformacaocarga;
	}
	public String getCep() {
		return cep;
	}
	public String getFilialemissao() {
		return filialemissao;
	}
	public Boolean getPrioridade() {
		return prioridade;
	}
	
	public String getDatavenda() {
		return datavenda;
	}
	public void setDatavenda(String datavenda) {
		this.datavenda = datavenda;
	}
	public void setPai(Integer pai) {
		this.pai = pai;
	}
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
	}
	public void setPraca(Praca praca) {
		this.praca = praca;
	}
	public void setTipooperacao(Tipooperacao tipooperacao) {
		this.tipooperacao = tipooperacao;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	public void setProduto(Produto produto) {
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
	public void setCdpedidovenda(Integer cdpedidovenda) {
		this.cdpedidovenda = cdpedidovenda;
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
	public String getNomedepositotransbordo() {
		return nomedepositotransbordo;
	}
	public void setNomedepositotransbordo(String nomedepositotransbordo) {
		this.nomedepositotransbordo = nomedepositotransbordo;
	}
	public void setFilialemissao(String filialemissao) {
		this.filialemissao = filialemissao;
	}
	public void setPrioridade(Boolean prioridade) {
		this.prioridade = prioridade;
	}
}
