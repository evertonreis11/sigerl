package br.com.linkcom.wms.modulo.expedicao.controller.process.filtro;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Departamentocodigogerenciadora;

public class AssociardeptoFiltro extends FiltroListagem{

	private List<Departamentocodigogerenciadora> listaDepartamentocodigogerenciadora;

	public List<Departamentocodigogerenciadora> getListaDepartamentocodigogerenciadora() {
		return listaDepartamentocodigogerenciadora;
	}

	public void setListaDepartamentocodigogerenciadora(List<Departamentocodigogerenciadora> listaDepartamentocodigogerenciadora) {
		this.listaDepartamentocodigogerenciadora = listaDepartamentocodigogerenciadora;
	}

}
