package br.com.linkcom.wms.modulo.expedicao.controller.process.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;

public class CancelarCarregamentoFiltro extends FiltroListagem{

	public Integer cdcarregamento;
	public String senha;
	
	@DisplayName("Confirme o número do carregamento a ser cancelado:")
	@MaxLength(9)
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	
	@DisplayName("Informe sua senha:")
	@MaxLength(15)
	public String getSenha() {
		return senha;
	}
	
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
}
