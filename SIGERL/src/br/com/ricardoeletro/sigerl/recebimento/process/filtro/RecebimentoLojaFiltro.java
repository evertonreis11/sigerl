package br.com.ricardoeletro.sigerl.recebimento.process.filtro;

import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLoja;
import br.com.ricardoeletro.sigerl.geral.filtro.GenericFilter;

public class RecebimentoLojaFiltro extends GenericFilter{
	
	private RecebimentoRetiraLoja recebimentoRetiraLoja;
	
	private Boolean avaria;
	
	private Integer cdTipoEstoque;
	
	private Integer cdRecebimentoRetiraLojaProduto;
	
	
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

	public Integer getCdTipoEstoque() {
		return cdTipoEstoque;
	}

	public void setCdTipoEstoque(Integer cdTipoEstoque) {
		this.cdTipoEstoque = cdTipoEstoque;
	}

	public Integer getCdRecebimentoRetiraLojaProduto() {
		return cdRecebimentoRetiraLojaProduto;
	}

	public void setCdRecebimentoRetiraLojaProduto(Integer cdRecebimentoRetiraLojaProduto) {
		this.cdRecebimentoRetiraLojaProduto = cdRecebimentoRetiraLojaProduto;
	}
	
	

}
