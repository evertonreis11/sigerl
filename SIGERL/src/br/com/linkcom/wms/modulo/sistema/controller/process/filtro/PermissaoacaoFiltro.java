package br.com.linkcom.wms.modulo.sistema.controller.process.filtro;

import java.util.Set;

import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Acaopapel;
import br.com.linkcom.wms.geral.bean.Papel;

public class PermissaoacaoFiltro {
	protected Papel papel;
	protected Set<Acaopapel> listaAcaoPapel = new ListSet<Acaopapel>(Acaopapel.class);

	public Papel getPapel() {
		return papel;
	}

	public Set<Acaopapel> getListaAcaoPapel() {
		return listaAcaoPapel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}
	
	public void setListaAcaoPapel(Set<Acaopapel> listaAcaoPapel) {
		this.listaAcaoPapel = listaAcaoPapel;
	}
	
}
