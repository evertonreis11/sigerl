package br.com.linkcom.wms.modulo.logistica.controller.report.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.GenericBean;
import br.com.linkcom.wms.geral.bean.Deposito;

public class RelatorioestoqueFiltro {

	public static final GenericBean TIPO_ANALITICO = new GenericBean(1, "Anal�tico");
	public static final GenericBean TIPO_SINTETICO = new GenericBean(2, "Sint�tico");

	private Deposito deposito;
	private GenericBean tiporelatorio=TIPO_ANALITICO;
	private Ordenacao ordenacao = Ordenacao.DESCRICAO;
	private Date datafinal;

	@DisplayName("Dep�sito")
	public Deposito getDeposito() {
		return deposito;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	@DisplayName("Tipo de relat�rio")
	public GenericBean getTiporelatorio() {
		return tiporelatorio;
	}

	public void setTiporelatorio(GenericBean tiporelatorio) {
		this.tiporelatorio = tiporelatorio;
	}

	@DisplayName("Ordenar por")
	public Ordenacao getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(Ordenacao ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Date getDatafinal() {
		return datafinal;
	}

	public void setDatafinal(Date data) {
		this.datafinal = data;
	}

}
