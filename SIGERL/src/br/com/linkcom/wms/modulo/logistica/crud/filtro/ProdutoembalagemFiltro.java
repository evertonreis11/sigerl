package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Produto;

public class ProdutoembalagemFiltro extends FiltroListagem {

	protected Produto produto;

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

}
