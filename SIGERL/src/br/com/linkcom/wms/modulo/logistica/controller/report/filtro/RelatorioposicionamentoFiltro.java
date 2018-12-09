package br.com.linkcom.wms.modulo.logistica.controller.report.filtro;

import java.util.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Produto;

public class RelatorioposicionamentoFiltro extends FiltroListagem {

	private Produto produto;
	private String documentoentrada;
	private String lote;
	private Date dtvalidadede;
	private Date dtvalidadeate;
	
	public Produto getProduto() {
		return produto;
	}
	@DisplayName("Documento de entrada")
	@MaxLength(15)
	public String getDocumentoentrada() {
		return documentoentrada;
	}
	@MaxLength(30)
	public String getLote() {
		return lote;
	}
	@DisplayName("De")
	public Date getDtvalidadede() {
		return dtvalidadede;
	}
	@DisplayName("Até")
	public Date getDtvalidadeate() {
		return dtvalidadeate;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public void setDocumentoentrada(String documentoentrada) {
		this.documentoentrada = documentoentrada;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public void setDtvalidadede(Date dtvalidadede) {
		this.dtvalidadede = dtvalidadede;
	}
	public void setDtvalidadeate(Date dtvalidadeate) {
		this.dtvalidadeate = dtvalidadeate;
	}
	
}
