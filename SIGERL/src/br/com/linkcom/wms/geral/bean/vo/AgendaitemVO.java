package br.com.linkcom.wms.geral.bean.vo;

public class AgendaitemVO {
	
	private Integer cdagenda;
	private Integer cdagendapedido;
	private Integer cdpedidocompra;
	private Integer cdpedidocompraproduto; 
	private Long qtde;
	private Integer cdproduto;
	
	public Integer getCdagenda() {
		return cdagenda;
	}
	public Integer getCdagendapedido() {
		return cdagendapedido;
	}
	public Integer getCdpedidocompra() {
		return cdpedidocompra;
	}
	public Integer getCdpedidocompraproduto() {
		return cdpedidocompraproduto;
	}
	public Long getQtde() {
		return qtde;
	}
	public Integer getCdproduto() {
		return cdproduto;
	}
	public void setCdproduto(Integer cdproduto) {
		this.cdproduto = cdproduto;
	}
	public void setCdagenda(Integer cdagenda) {
		this.cdagenda = cdagenda;
	}
	public void setCdagendapedido(Integer cdagendapedido) {
		this.cdagendapedido = cdagendapedido;
	}
	public void setCdpedidocompra(Integer cdpedidocompra) {
		this.cdpedidocompra = cdpedidocompra;
	}
	public void setCdpedidocompraproduto(Integer cdpedidocompraproduto) {
		this.cdpedidocompraproduto = cdpedidocompraproduto;
	}
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
}