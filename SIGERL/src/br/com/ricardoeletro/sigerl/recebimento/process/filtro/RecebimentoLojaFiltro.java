package br.com.ricardoeletro.sigerl.recebimento.process.filtro;

import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLoja;
import br.com.ricardoeletro.sigerl.geral.filtro.GenericFilter;

public class RecebimentoLojaFiltro extends GenericFilter{
	
	private RecebimentoRetiraLoja recebimentoRetiraLoja;
	
	private Boolean avaria;
	
	public RecebimentoRetiraLoja getRecebimentoRetiraLoja() {
		return recebimentoRetiraLoja;
	}

	public void setRecebimentoRetiraLoja(RecebimentoRetiraLoja recebimentoRetiraLoja) {
		this.recebimentoRetiraLoja = recebimentoRetiraLoja;
	}

	public Boolean getAvaria() {
		return avaria;
	}

	public void setAvaria(Boolean avaria) {
		this.avaria = avaria;
	}
	
	

}
