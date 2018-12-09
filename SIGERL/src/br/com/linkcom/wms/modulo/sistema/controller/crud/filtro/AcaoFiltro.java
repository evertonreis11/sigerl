package br.com.linkcom.wms.modulo.sistema.controller.crud.filtro;

import org.apache.commons.lang.StringUtils;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;

public class AcaoFiltro extends FiltroListagem{

	protected String key;
	protected String descricao;
	
	@DisplayName("Chave")
	public String getKey() {
		return key;
	}
	@DisplayName("Descrição")
	public String getDescricao() {
		return descricao;
	}
	public void setKey(String key) {
		this.key = StringUtils.trimToNull(key);
	}
	public void setDescricao(String descricao) {
		this.descricao = StringUtils.trimToNull(descricao);
	}

	
	@Override
	public int getPageSize() {
		return 100;
	}
}
