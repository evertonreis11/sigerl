package br.com.linkcom.wms.modulo.recebimento.controller.report.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Recebimento;

public class EtiquetaprodutoFiltro extends FiltroListagem{
	
	private Recebimento recebimento;
	private Produto produto;
	private int numerocopias = 1;
	
	@DisplayName("Número de cópias")
	public int getNumerocopias() {
		return numerocopias;
	}

	public void setNumerocopias(int numerocopias) {
		this.numerocopias = numerocopias;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Recebimento getRecebimento() {
		return recebimento;
	}
	
	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}
}
