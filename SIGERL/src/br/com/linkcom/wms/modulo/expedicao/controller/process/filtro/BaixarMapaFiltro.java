package br.com.linkcom.wms.modulo.expedicao.controller.process.filtro;

import java.util.Set;

import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;

public class BaixarMapaFiltro {
	
	protected Carregamento carregamento;
	protected Set<OrdemservicoUsuario> listaOrdemServicoUsuario = new ListSet<OrdemservicoUsuario>(OrdemservicoUsuario.class);
	
	public Set<OrdemservicoUsuario> getListaOrdemServicoUsuario() {
		return listaOrdemServicoUsuario;
	}
	
	public Carregamento getCarregamento() {
		return carregamento;
	}
	
	public void setListaOrdemServicoUsuario(Set<OrdemservicoUsuario> listaOrdemServicoUsuario) {
		this.listaOrdemServicoUsuario = listaOrdemServicoUsuario;
	}
	
	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}
}
