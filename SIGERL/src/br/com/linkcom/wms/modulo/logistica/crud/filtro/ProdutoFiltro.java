package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Produtoclasse;

public class ProdutoFiltro extends FiltroListagem {
	
	public static enum IsVolume {PRINCIPAL, VOLUME};
	
	protected String descricao;
	protected String referencia;
	protected String codigo;
	protected Produtoclasse produtoclasse;
	protected Linhaseparacao linhaseparacao;
	protected IsVolume isVolume;
	
	//Get's
	@DisplayName("Descrição")
	@MaxLength(100)
	public String getDescricao() {
		return descricao;
	}
	@DisplayName("Referência")
	@MaxLength(20)
	public String getReferencia() {
		return referencia;
	}
	@DisplayName("Classe")
	public Produtoclasse getProdutoclasse() {
		return produtoclasse;
	}
	@DisplayName("Linha de separação")
	public Linhaseparacao getLinhaseparacao() {
		return linhaseparacao;
	}
	@MaxLength(20)
	@DisplayName("Código")
	public String getCodigo() {
		return codigo;
	}
	public IsVolume getIsVolume() {
		return isVolume;
	}

	//Set's
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public void setProdutoclasse(Produtoclasse produtoclasse) {
		this.produtoclasse = produtoclasse;
	}
	public void setLinhaseparacao(Linhaseparacao linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setIsVolume(IsVolume isVolume) {
		this.isVolume = isVolume;
	}
	
}	
