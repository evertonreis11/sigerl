package br.com.linkcom.wms.modulo.logistica.controller.report.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.GenericBean;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Area;
import br.com.linkcom.wms.geral.bean.Inventariolote;
import br.com.linkcom.wms.geral.bean.Produto;

public class ContagemInventarioFiltro {

	private Integer cdinventario;
	private GenericBean ordenacao;
	private Area area;
	private Integer ruaInicial;
	private Integer ruaFinal;
	private Integer predioInicial;
	private Integer predioFinal;
	private Integer nivelInicial;
	private Integer nivelFinal;
	private Integer aptoInicial;
	private Integer aptoFinal;
	private Produto produto;
	private Boolean existeDivergenciaContagem;
	private Boolean divergenciaEsperadoFinal;
	private Inventariolote inventariolote;
	

	@DisplayName("Ordena��o")
	public GenericBean getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(GenericBean ordenacao) {
		this.ordenacao = ordenacao;
	}

	@DisplayName("�rea")
	public Area getArea() {
		return area;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	@DisplayName("Rua inicial")
	public Integer getRuaInicial() {
		return ruaInicial;
	}

	public void setRuaInicial(Integer ruaInicial) {
		this.ruaInicial = ruaInicial;
	}

	@DisplayName("Rua final")
	public Integer getRuaFinal() {
		return ruaFinal;
	}

	public void setRuaFinal(Integer ruaFinal) {
		this.ruaFinal = ruaFinal;
	}

	@DisplayName("Pr�dio inicial")
	public Integer getPredioInicial() {
		return predioInicial;
	}

	public void setPredioInicial(Integer predioInicial) {
		this.predioInicial = predioInicial;
	}

	@DisplayName("Pr�dio final")
	public Integer getPredioFinal() {
		return predioFinal;
	}

	public void setPredioFinal(Integer predioFinal) {
		this.predioFinal = predioFinal;
	}

	@DisplayName("N�vel inicial")
	public Integer getNivelInicial() {
		return nivelInicial;
	}

	public void setNivelInicial(Integer nivelInicial) {
		this.nivelInicial = nivelInicial;
	}

	@DisplayName("N�vel final")
	public Integer getNivelFinal() {
		return nivelFinal;
	}

	public void setNivelFinal(Integer nivelFinal) {
		this.nivelFinal = nivelFinal;
	}

	@DisplayName("Apartamento inicial")
	public Integer getAptoInicial() {
		return aptoInicial;
	}

	public void setAptoInicial(Integer aptoInicial) {
		this.aptoInicial = aptoInicial;
	}

	@DisplayName("Apartamento final")
	public Integer getAptoFinal() {
		return aptoFinal;
	}

	public void setAptoFinal(Integer aptoFinal) {
		this.aptoFinal = aptoFinal;
	}

	@DisplayName("Produto")
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	@DisplayName("Exist�ncia de diverg�ncia na contagem")
	public Boolean getExisteDivergenciaContagem() {
		return existeDivergenciaContagem;
	}

	public void setExisteDivergenciaContagem(Boolean somenteItensDivergentes) {
		this.existeDivergenciaContagem = somenteItensDivergentes;
	}

	@DisplayName("Diverg�ncia entre o Estoque Atual e a Posi��o Final")
	public Boolean getDivergenciaEsperadoFinal() {
		return divergenciaEsperadoFinal;
	}

	public void setDivergenciaEsperadoFinal(Boolean somenteDiferenteEsperado) {
		this.divergenciaEsperadoFinal = somenteDiferenteEsperado;
	}

	@Required
	@DisplayName("N�mero do invent�rio")
	public Integer getCdinventario() {
		return cdinventario;
	}

	public void setCdinventario(Integer cdinventario) {
		this.cdinventario = cdinventario;
	}
	
	public Inventariolote getInventariolote() {
		return inventariolote;
	}
	
	public void setInventariolote(Inventariolote inventariolote) {
		this.inventariolote = inventariolote;
	}
	
}
