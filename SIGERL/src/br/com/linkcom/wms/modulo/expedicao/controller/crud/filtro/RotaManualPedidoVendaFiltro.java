package br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Municipio;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.bean.Uf;

public class RotaManualPedidoVendaFiltro extends FiltroListagem{
	
	private Praca praca;
	private String pedidoVendaNumero;
	private Deposito deposito;
	private Tipooperacao tipooperacao;
	private boolean listagemVazia = true;
	private Date datainicial;
	private Date datafim;
	private Boolean troca;
	private Boolean convenio;
	private Municipio municipio;
	private Uf uf;
	private String bairro;
	private Cliente filialEmissao;

	
	//Get's
	@Required
	public Date getDatainicial() {
		return datainicial;
	}
	@Required
	public Date getDatafim() {
		return datafim;
	}
	public void setDatainicial(Date datainicial) {
		this.datainicial = datainicial;
	}
	public void setDatafim(Date datafim) {
		this.datafim = datafim;
	}
	public Praca getPraca() {
		return praca;
	}
	@DisplayName("Número do pedido")
	public String getPedidoVendaNumero() {
		return pedidoVendaNumero;
	}
	public Deposito getDeposito() {
		return deposito;
	}
	@DisplayName("Tipo do pedido")
	public Tipooperacao getTipooperacao() {
		return tipooperacao;
	}
	public boolean isListagemVazia() {
		return listagemVazia;
	}
	public Boolean getTroca() {
		return troca;
	}
	public Boolean getConvenio() {
		return convenio;
	}
	public Uf getUf() {
		return uf;
	}
	public Municipio getMunicipio() {
		return municipio;
	}
	public String getBairro() {
		return bairro;
	}
	public Cliente getFilialEmissao() {
		return filialEmissao;
	}
	
	
	//Set's
	public void setTroca(Boolean troca) {
		this.troca = troca;
	}
	public void setPraca(Praca praca) {
		this.praca = praca;
	}
	public void setPedidoVendaNumero(String pedidoVendaNumero) {
		this.pedidoVendaNumero = pedidoVendaNumero;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setTipooperacao(Tipooperacao tipooperacao) {
		this.tipooperacao = tipooperacao;
	}
	public void setListagemVazia(boolean listagemVazia) {
		this.listagemVazia = listagemVazia;
	}
	public void setConvenio(Boolean convenio) {
		this.convenio = convenio;
	}
	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public void setFilialEmissao(Cliente filialEmissao) {
		this.filialEmissao = filialEmissao;
	}
	public void setUf(Uf uf) {
		this.uf = uf;
	}

}
