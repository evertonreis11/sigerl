package br.com.linkcom.wms.modulo.recebimento.controller.process.filtro;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoendereco;

public class VisualizarEnderecamentoFiltro {

	private Ordemservico ordemservico;
	private OrdemservicoUsuario ordemservicoUsuario;
	private List<Ordemservicoprodutoendereco> listaOSPE;

	public Ordemservico getOrdemservico() {
		return ordemservico;
	}

	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}

	public OrdemservicoUsuario getOrdemservicoUsuario() {
		return ordemservicoUsuario;
	}

	public void setOrdemservicoUsuario(OrdemservicoUsuario ordemservicoUsuario) {
		this.ordemservicoUsuario = ordemservicoUsuario;
	}

	public List<Ordemservicoprodutoendereco> getListaOSPE() {
		return listaOSPE;
	}

	public void setListaOSPE(List<Ordemservicoprodutoendereco> listaOSPE) {
		this.listaOSPE = listaOSPE;
	}

}
