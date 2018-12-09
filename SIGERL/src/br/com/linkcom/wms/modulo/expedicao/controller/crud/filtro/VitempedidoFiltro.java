package br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;


public class VitempedidoFiltro extends FiltroListagem {
	
	protected Integer cdCarregamento;
	protected String numeroPedido;
	protected Boolean situacaoItem;
	protected Integer cdExpedicao;
	
	@DisplayName("Nº Carregamento")
	@MaxLength(9)
	public Integer getCdCarregamento() {
		return cdCarregamento;
	}
	@DisplayName("Nº Pedido")
	@MaxLength(18)
	public String getNumeroPedido() {
		return numeroPedido;
	}
	@DisplayName("Situação do item")
	public Boolean getSituacaoItem() {
		return situacaoItem;
	}
	@DisplayName("Nº Expedição")
	public Integer getCdExpedicao() {
		return cdExpedicao;
	}
	
	public void setCdCarregamento(Integer cdCarregamento) {
		this.cdCarregamento = cdCarregamento;
	}
	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	public void setSituacaoItem(Boolean situacaoItem) {
		this.situacaoItem = situacaoItem;
	}
	public void setCdExpedicao(Integer cdExpedicao) {
		this.cdExpedicao = cdExpedicao;
	}
	
	
}
