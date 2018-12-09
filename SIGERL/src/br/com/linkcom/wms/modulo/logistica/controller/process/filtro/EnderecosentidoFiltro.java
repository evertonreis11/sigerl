package br.com.linkcom.wms.modulo.logistica.controller.process.filtro;

import java.util.Set;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Enderecosentido;

public class EnderecosentidoFiltro extends FiltroListagem {
	Set<Enderecosentido> listaEnderecosentido = new ListSet<Enderecosentido>(Enderecosentido.class);

	public Set<Enderecosentido> getListaEnderecosentido() {
		return listaEnderecosentido;
	}

	public void setListaEnderecosentido(Set<Enderecosentido> listaEnderecosentido) {
		this.listaEnderecosentido = listaEnderecosentido;
	}
}
