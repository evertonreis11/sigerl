package br.com.linkcom.wms.util.logistica;

import java.util.Date;

import br.com.linkcom.neo.util.NeoFormater;

public class InventarioVO {
	// ---------------------------------- Variáveis de instância ----------------------------------
	private Integer cdinventario;
	private String inventariotipo;
	private String data;
	private String codigodescricaoproduto;
	private String descricaoembalagem;
	private Long qtdeanterior;
	private Long qtdeinventario;
	private Long divergencia;
	
	// ---------------------------------- Construtores ---------------------------------- 
	public InventarioVO() {
		
	}
	
	public InventarioVO(Integer cdinventario, Date data, String inventariotipo, String codigo, String descricaoproduto, String descricaoembalagem, Long qtdeesperada, Long qtde, Long divergencia) {
		this.cdinventario = cdinventario;
		this.data = NeoFormater.getInstance().format(data);
		this.inventariotipo = inventariotipo;
		this.codigodescricaoproduto = codigo + " - " + descricaoproduto;
		this.descricaoembalagem = descricaoembalagem;
		this.qtdeinventario = qtde;
		this.qtdeanterior = qtdeesperada;
		this.divergencia = divergencia;
	}

	// ---------------------------------- Métodos get e set ----------------------------------
	public Integer getCdinventario() {
		return cdinventario;
	}
	
	public String getInventariotipo() {
		return inventariotipo;
	}
	
	public String getData() {
		return data;
	}
	
	public String getCodigodescricaoproduto() {
		return codigodescricaoproduto;
	}
	
	public String getDescricaoembalagem() {
		return descricaoembalagem;
	}
	
	public Long getQtdeanterior() {
		return qtdeanterior;
	}
	
	public Long getQtdeinventario() {
		return qtdeinventario;
	}
	
	public Long getDivergencia() {
		return divergencia;
	}
	
	public void setCdinventario(Integer cdinventario) {
		this.cdinventario = cdinventario;
	}
	
	public void setInventariotipo(String inventariotipo) {
		this.inventariotipo = inventariotipo;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public void setCodigodescricaoproduto(String codigodescricaoproduto) {
		this.codigodescricaoproduto = codigodescricaoproduto;
	}
	
	public void setDescricaoembalagem(String descricaoembalagem) {
		this.descricaoembalagem = descricaoembalagem;
	}
	
	public void setQtdeanterior(Long qtdeanterior) {
		this.qtdeanterior = qtdeanterior;
	}
	
	public void setQtdeinventario(Long qtdeinventario) {
		this.qtdeinventario = qtdeinventario;
	}
	
	public void setDivergencia(Long divergencia) {
		this.divergencia = divergencia;
	}
}
