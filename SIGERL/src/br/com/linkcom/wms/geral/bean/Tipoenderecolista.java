package br.com.linkcom.wms.geral.bean;

import java.util.Set;

import br.com.linkcom.neo.types.ListSet;

public class Tipoenderecolista {
	protected Set<Tipoendereco> listaTipoEndereco = new ListSet<Tipoendereco>(Tipoendereco.class);
	protected Boolean excluir;

	public Set<Tipoendereco> getListaTipoEndereco() {
		return listaTipoEndereco;
	}
	
	public Boolean getExcluir() {
		return excluir;
	}

	public void setListaTipoEndereco(Set<Tipoendereco> listaTipoEndereco) {
		this.listaTipoEndereco = listaTipoEndereco;
	}

	public void setExcluir(Boolean excluir) {
		this.excluir = excluir;
	}
}