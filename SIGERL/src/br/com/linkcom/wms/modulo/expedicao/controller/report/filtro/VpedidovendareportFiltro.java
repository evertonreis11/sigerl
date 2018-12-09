package br.com.linkcom.wms.modulo.expedicao.controller.report.filtro;

import java.sql.Date;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.bean.Tiporotapraca;

public class VpedidovendareportFiltro extends FiltroListagem{

	private Deposito deposito;
	private Tipooperacao tipooperacao;
	private Date dataEmissaoInicial;
	private Date dataEmissaoFinal;
	private Carregamentostatus carregamentostatus;
	private Rota rota;
	private Cliente cliente;
	private Date dataPrevisaoInicial;
	private Date dataPrevisaoFinal;
	private Tiporotapraca tiporotapraca;
	
	
	//Get's
	@Required
	public Deposito getDeposito() {
		return deposito;
	}
	public Tipooperacao getTipooperacao() {
		return tipooperacao;
	}
	@Required
	public Date getDataEmissaoInicial() {
		return dataEmissaoInicial;
	}
	@Required	
	public Date getDataEmissaoFinal() {
		return dataEmissaoFinal;
	}
	public Rota getRota() {
		return rota;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public Carregamentostatus getCarregamentostatus() {
		return carregamentostatus;
	}
	public Date getDataPrevisaoInicial() {
		return dataPrevisaoInicial;
	}
	public Date getDataPrevisaoFinal() {
		return dataPrevisaoFinal;
	}
	public Tiporotapraca getTiporotapraca() {
		return tiporotapraca;
	}
	
	//Set's
	public void setCarregamentostatus(Carregamentostatus carregamentostatus) {
		this.carregamentostatus = carregamentostatus;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setTipooperacao(Tipooperacao tipooperacao) {
		this.tipooperacao = tipooperacao;
	}
	public void setDataEmissaoInicial(Date dataEmissaoInicial) {
		this.dataEmissaoInicial = dataEmissaoInicial;
	}
	public void setDataEmissaoFinal(Date dataEmissaoFinal) {
		this.dataEmissaoFinal = dataEmissaoFinal;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public void setDataPrevisaoInicial(Date dataPrevisaoInicial) {
		this.dataPrevisaoInicial = dataPrevisaoInicial;
	}
	public void setDataPrevisaoFinal(Date dataPrevisaoFinal) {
		this.dataPrevisaoFinal = dataPrevisaoFinal;
	}
	public void setTiporotapraca(Tiporotapraca tiporotapraca) {
		this.tiporotapraca = tiporotapraca;
	}
	
}
