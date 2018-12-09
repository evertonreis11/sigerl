package br.com.linkcom.wms.modulo.logistica.controller.report.filtro;

import java.math.BigDecimal;
import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.MaxValue;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;

public class ClassificacaoABCFiltro extends FiltroListagem {

	private Date datainicial;
	private Date datafinal;
	private TipoAnalise tipoAnalise = TipoAnalise.GIRO;
	private Linhaseparacao linhaseparacao;
	private BigDecimal nivelA = new BigDecimal(70);
	private BigDecimal nivelB = new BigDecimal(20);
	private BigDecimal nivelC = new BigDecimal(10);

	@Required
	@DisplayName("Até")
	public Date getDatafinal() {
		return datafinal;
	}

	@Required
	@DisplayName("De")
	public Date getDatainicial() {
		return datainicial;
	}

	@DisplayName("Linha de separação")
	public Linhaseparacao getLinhaseparacao() {
		return linhaseparacao;
	}

	@Required
	@MaxValue(100)
	@MaxLength(3)
	@DisplayName("Nível A")
	public BigDecimal getNivelA() {
		return nivelA;
	}

	@Required
	@MaxValue(100)
	@MaxLength(3)
	@DisplayName("Nível B")
	public BigDecimal getNivelB() {
		return nivelB;
	}

	@Required
	@MaxValue(100)
	@MaxLength(3)
	@DisplayName("Nível C")
	public BigDecimal getNivelC() {
		return nivelC;
	}

	@DisplayName("Análise")
	public TipoAnalise getTipoAnalise() {
		return tipoAnalise;
	}

	public void setDatafinal(Date dataate) {
		this.datafinal = dataate;
	}

	public void setDatainicial(Date datade) {
		this.datainicial = datade;
	}

	public void setLinhaseparacao(Linhaseparacao linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}

	public void setNivelA(BigDecimal nivalA) {
		this.nivelA = nivalA;
	}

	public void setNivelB(BigDecimal nivalB) {
		this.nivelB = nivalB;
	}

	public void setNivelC(BigDecimal nivalC) {
		this.nivelC = nivalC;
	}

	public void setTipoAnalise(TipoAnalise tipoAnalise) {
		this.tipoAnalise = tipoAnalise;
	}
	
}
