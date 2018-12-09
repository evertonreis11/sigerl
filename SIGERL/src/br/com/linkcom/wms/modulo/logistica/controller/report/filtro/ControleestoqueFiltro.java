package br.com.linkcom.wms.modulo.logistica.controller.report.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.GenericBean;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.MaxValue;
import br.com.linkcom.wms.geral.bean.Controleestoquestatus;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;

public class ControleestoqueFiltro extends FiltroListagem {

	protected Integer numeroControle;
	protected Date dataInicial;
	protected Date dataFinal;
	protected Controleestoquestatus controleestoquestatus;
	protected Linhaseparacao linhaseparacao;
	protected GenericBean ordenacao;
	protected Boolean somenteItensDivergentes;

	@DisplayName("Número")
	@MaxValue(Integer.MAX_VALUE)
	@MaxLength(9)
	public Integer getNumeroControle() {
		return numeroControle;
	}

	@DisplayName("Data inicial")
	public Date getDataInicial() {
		return dataInicial;
	}

	@DisplayName("Data final")
	public Date getDataFinal() {
		return dataFinal;
	}

	@DisplayName("Status")
	public Controleestoquestatus getControleestoquestatus() {
		return controleestoquestatus;
	}

	public void setNumeroControle(Integer numeroControle) {
		this.numeroControle = numeroControle;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public void setControleestoquestatus(
			Controleestoquestatus controleestoquestatus) {
		this.controleestoquestatus = controleestoquestatus;
	}

	@DisplayName("Linha de separação")
	public Linhaseparacao getLinhaseparacao() {
		return linhaseparacao;
	}

	public void setLinhaseparacao(Linhaseparacao linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}

	@DisplayName("Ordernar por")
	public GenericBean getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(GenericBean ordenacao) {
		this.ordenacao = ordenacao;
	}
	
	@DisplayName("Exibir somente os itens que possuem divergência")
	public Boolean getSomenteItensDivergentes() {
		return somenteItensDivergentes;
	}
	
	public void setSomenteItensDivergentes(Boolean somenteItensDivergentes) {
		this.somenteItensDivergentes = somenteItensDivergentes;
	}

}
