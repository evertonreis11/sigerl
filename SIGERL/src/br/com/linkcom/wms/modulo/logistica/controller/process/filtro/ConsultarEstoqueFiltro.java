package br.com.linkcom.wms.modulo.logistica.controller.process.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.MaxValue;
import br.com.linkcom.wms.geral.bean.Area;
import br.com.linkcom.wms.geral.bean.Enderecolado;
import br.com.linkcom.wms.geral.bean.Produto;

public class ConsultarEstoqueFiltro extends FiltroListagem {

	protected Area area;
	protected Integer ruainicial;
	protected Integer predioinicial;
	protected Integer nivelinicial;
	protected Integer aptoinicial;
	protected Integer ruafinal;
	protected Integer prediofinal;
	protected Integer nivelfinal;
	protected Integer aptofinal;
	protected Produto produto;
	protected Enderecolado enderecolado;
	protected Integer etiquetaUma;

	@DisplayName("Área")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@DisplayName("Rua inicial")
	@MaxValue(999)
	@MaxLength(3)
	public Integer getRuainicial() {
		return ruainicial;
	}

	public void setRuainicial(Integer ruainicial) {
		this.ruainicial = ruainicial;
	}

	@MaxValue(999)
	@MaxLength(3)
	@DisplayName("Prédio inicial")
	public Integer getPredioinicial() {
		return predioinicial;
	}

	public void setPredioinicial(Integer predioinicial) {
		this.predioinicial = predioinicial;
	}

	@MaxValue(99)
	@MaxLength(2)
	@DisplayName("Nível inicial")
	public Integer getNivelinicial() {
		return nivelinicial;
	}

	public void setNivelinicial(Integer nivelinicial) {
		this.nivelinicial = nivelinicial;
	}

	@MaxValue(999)
	@MaxLength(3)
	@DisplayName("Apartamento inicial")
	public Integer getAptoinicial() {
		return aptoinicial;
	}

	public void setAptoinicial(Integer aptoinicial) {
		this.aptoinicial = aptoinicial;
	}

	@MaxValue(999)
	@MaxLength(3)
	@DisplayName("Rua final")
	public Integer getRuafinal() {
		return ruafinal;
	}

	public void setRuafinal(Integer ruafinal) {
		this.ruafinal = ruafinal;
	}

	@MaxValue(999)
	@MaxLength(3)
	@DisplayName("Prédio final")
	public Integer getPrediofinal() {
		return prediofinal;
	}

	public void setPrediofinal(Integer prediofinal) {
		this.prediofinal = prediofinal;
	}

	@MaxValue(99)
	@MaxLength(2)
	@DisplayName("Nível final")
	public Integer getNivelfinal() {
		return nivelfinal;
	}

	public void setNivelfinal(Integer nivelfinal) {
		this.nivelfinal = nivelfinal;
	}

	@MaxValue(999)
	@MaxLength(3)
	@DisplayName("Apartamento final")
	public Integer getAptofinal() {
		return aptofinal;
	}

	public void setAptofinal(Integer aptofinal) {
		this.aptofinal = aptofinal;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Enderecolado getEnderecolado() {
		return enderecolado;
	}

	public void setEnderecolado(Enderecolado enderecolado) {
		this.enderecolado = enderecolado;
	}

	@MaxLength(9)
	public Integer getEtiquetaUma() {
		return etiquetaUma;
	}

	public void setEtiquetaUma(Integer etiquetaUma) {
		this.etiquetaUma = etiquetaUma;
	}

}
