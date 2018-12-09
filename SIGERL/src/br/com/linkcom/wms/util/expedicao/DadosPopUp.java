package br.com.linkcom.wms.util.expedicao;

import java.util.List;

import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Pedidovendaproduto;


public class DadosPopUp {
	private String chave;
	private String clienteID;
	private Boolean roteirizacao;
	private Integer cdcarregamento;
	private String titulo;
	private String enderecoentrega;
	
	private Double cubagemTotal = 0.0;
	private Double pesoTotal = 0.0;
	private Long qtdeTotal = 0L;
	private Money valorTotal;
	
	private List<Pedidovendaproduto> listaPedidoVendaProduto;

	public String getChave() {
		return chave;
	}

	public String getClienteID() {
		return clienteID;
	}

	public Boolean getRoteirizacao() {
		return roteirizacao;
	}

	public Integer getCdcarregamento() {
		return cdcarregamento;
	}

	public String getTitulo() {
		return titulo;
	}
	
	public String getEnderecoentrega() {
		return enderecoentrega;
	}

	public Double getCubagemTotal() {
		return cubagemTotal;
	}

	public Double getPesoTotal() {
		return pesoTotal;
	}

	public Long getQtdeTotal() {
		return qtdeTotal;
	}

	public Money getValorTotal() {
		return valorTotal;
	}

	public List<Pedidovendaproduto> getListaPedidoVendaProduto() {
		return listaPedidoVendaProduto;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public void setClienteID(String clienteID) {
		this.clienteID = clienteID;
	}

	public void setRoteirizacao(Boolean roteirizacao) {
		this.roteirizacao = roteirizacao;
	}

	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public void setEnderecoentrega(String enderecoentrega) {
		this.enderecoentrega = enderecoentrega;
	}

	public void setCubagemTotal(Double cubagemTotal) {
		this.cubagemTotal = cubagemTotal;
	}

	public void setPesoTotal(Double pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public void setQtdeTotal(Long qtdeTotal) {
		this.qtdeTotal = qtdeTotal;
	}
	
	public void setValorTotal(Money valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void setListaPedidoVendaProduto(List<Pedidovendaproduto> listaPedidoVendaProduto) {
		this.listaPedidoVendaProduto = listaPedidoVendaProduto;
	}
	
	
}
