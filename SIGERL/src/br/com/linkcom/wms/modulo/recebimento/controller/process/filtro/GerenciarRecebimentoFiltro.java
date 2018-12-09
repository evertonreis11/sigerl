package br.com.linkcom.wms.modulo.recebimento.controller.process.filtro;

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Recebimento;

public class GerenciarRecebimentoFiltro extends FiltroListagem{
	
	private Recebimento recebimento;
	private List<Ordemservico> listaOrdemServico = new ArrayList<Ordemservico>();
	
	public Recebimento getRecebimento() {
		return recebimento;
	}
	
	public List<Ordemservico> getListaOrdemServico() {
		return listaOrdemServico;
	}
	
	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}
	
	public void setListaOrdemServico(List<Ordemservico> listaOrdemServico) {
		this.listaOrdemServico = listaOrdemServico;
	}
}
