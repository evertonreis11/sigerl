package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.ValidationOverride;
import br.com.linkcom.wms.geral.bean.Area;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecostatus;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Tipoendereco;
import br.com.linkcom.wms.geral.bean.Tipoestrutura;
import br.com.linkcom.wms.geral.bean.Tipopalete;

public class EnderecoFiltro extends FiltroListagem {
	protected Integer cdendereco;
	protected Area area;
	protected Integer ruaI;
	protected Integer ruaF;
	protected Integer predioI;
	protected Integer predioF;
	protected Integer nivelI;
	protected Integer nivelF;
	protected Integer aptoI;
	protected Integer aptoF;
	protected Boolean lado;	
	protected String estado;
	protected Enderecofuncao enderecofuncao;
	protected Tipoestrutura tipoestrutura;
	protected Tipoendereco tipoendereco;
	protected Boolean larguraexcedente;
	protected Enderecostatus enderecostatus;
	protected Tipopalete tipopalete;
	protected Produto produto;
	protected Linhaseparacao linhaseparacao;
	
	public Integer getCdendereco() {
		return cdendereco;
	}
	
	@DisplayName("Área de armazenagem")
	@ValidationOverride(field="area.codigo",required={})
	public Area getArea() {
		return area;
	}
	
	@DisplayName("inicial")
	@MaxLength(3)
	public Integer getRuaI() {
		return ruaI;
	}
	
	@DisplayName("final")
	@MaxLength(3)
	public Integer getRuaF() {
		return ruaF;
	}
	
	@DisplayName("inicial")
	@MaxLength(3)
	public Integer getPredioI() {
		return predioI;
	}
	
	@DisplayName("final")
	@MaxLength(3)
	public Integer getPredioF() {
		return predioF;
	}
	
	@DisplayName("inicial")
	@MaxLength(2)
	public Integer getNivelI() {
		return nivelI;
	}
	
	@DisplayName("final")
	@MaxLength(2)
	public Integer getNivelF() {
		return nivelF;
	}
	
	@DisplayName("inicial")
	@MaxLength(3)
	public Integer getAptoI() {
		return aptoI;
	}
	
	@DisplayName("final")
	@MaxLength(3)
	public Integer getAptoF() {
		return aptoF;
	}
	
	@DisplayName("Lado")	
	public Boolean getLado() {
		return lado;
	}
	
	@DisplayName("Característica")
	public Enderecofuncao getEnderecofuncao() {
		return enderecofuncao;
	}
	
	@DisplayName("Estrutura de armazenagem")
	public Tipoestrutura getTipoestrutura() {
		return tipoestrutura;
	}
	
	@DisplayName("Tipo de endereço")
	public Tipoendereco getTipoendereco() {
		return tipoendereco;
	}
	
	@DisplayName("L. excedente")
	public Boolean getLarguraexcedente() {
		return larguraexcedente;
	}
	
	@DisplayName("Status")
	public Enderecostatus getEnderecostatus() {
		return enderecostatus;
	}
	
	@DisplayName("Tipo de palete")
	public Tipopalete getTipopalete() {
		return tipopalete;
	}
	
	@DisplayName("Produto")
	public Produto getProduto() {
		return produto;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setCdendereco(Integer cdendereco) {
		this.cdendereco = cdendereco;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	public void setRuaI(Integer ruaI) {
		this.ruaI = ruaI;
	}
	
	public void setRuaF(Integer ruaF) {
		this.ruaF = ruaF;
	}
	
	public void setPredioI(Integer predioI) {
		this.predioI = predioI;
	}
	
	public void setPredioF(Integer predioF) {
		this.predioF = predioF;
	}
	
	public void setNivelI(Integer nivelI) {
		this.nivelI = nivelI;
	}
	
	public void setNivelF(Integer nivelF) {
		this.nivelF = nivelF;
	}
	
	public void setAptoI(Integer aptoI) {
		this.aptoI = aptoI;
	}
	
	public void setAptoF(Integer aptoF) {
		this.aptoF = aptoF;
	}
	
	public void setLado(Boolean lado) {
		this.lado = lado;
	}
	
	public void setEnderecofuncao(Enderecofuncao enderecofuncao) {
		this.enderecofuncao = enderecofuncao;
	}
	
	public void setTipoestrutura(Tipoestrutura tipoestrutura) {
		this.tipoestrutura = tipoestrutura;
	}
	
	public void setTipoendereco(Tipoendereco tipoendereco) {
		this.tipoendereco = tipoendereco;
	}
	
	public void setLarguraexcedente(Boolean larguraexcedente) {
		this.larguraexcedente = larguraexcedente;
	}
	
	public void setEnderecostatus(Enderecostatus enderecostatus) {
		this.enderecostatus = enderecostatus;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public void setTipopalete(Tipopalete tipopalete) {
		this.tipopalete = tipopalete;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	@DisplayName("Linha de separação")
	public Linhaseparacao getLinhaseparacao() {
		return linhaseparacao;
	}

	public void setLinhaseparacao(Linhaseparacao linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}

}
