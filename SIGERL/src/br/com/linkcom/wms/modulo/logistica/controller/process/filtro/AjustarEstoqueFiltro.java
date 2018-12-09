package br.com.linkcom.wms.modulo.logistica.controller.process.filtro;

import java.util.Set;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecoproduto;

public class AjustarEstoqueFiltro extends ConsultarEstoqueFiltro {
	
	protected String codigoproduto;
	protected Endereco endereco;
	protected Enderecoproduto enderecoproduto;
	private Set<AjustarEstoqueFiltro> listaAjustarEstoque = new ListSet<AjustarEstoqueFiltro>(
			AjustarEstoqueFiltro.class);

	public Set<AjustarEstoqueFiltro> getListaAjustarEstoque() {
		return listaAjustarEstoque;
	}

	public void setListaAjustarEstoque(Set<AjustarEstoqueFiltro> listaAjustarEstoque) {
		this.listaAjustarEstoque = listaAjustarEstoque;
	}

	@DisplayName("Endereço")
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Enderecoproduto getEnderecoproduto() {
		return enderecoproduto;
	}

	public void setEnderecoproduto(Enderecoproduto enderecoproduto) {
		this.enderecoproduto = enderecoproduto;
	}

	@MaxLength(20)
	@DisplayName("Código")
	public String getCodigoproduto() {
		return codigoproduto;
	}

	public void setCodigoproduto(String codigoproduto) {
		this.codigoproduto = codigoproduto;
	}

}
