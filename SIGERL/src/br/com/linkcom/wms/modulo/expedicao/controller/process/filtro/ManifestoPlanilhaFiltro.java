package br.com.linkcom.wms.modulo.expedicao.controller.process.filtro;

import java.sql.Date;

import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Motorista;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Tipoentrega;
import br.com.linkcom.wms.geral.bean.Transportador;

public class ManifestoPlanilhaFiltro {

	public enum TipoRelatorio {Analítico,Sintético}; 
	
	private Integer cdmanifesto;
	private Transportador transportador;
	private Motorista motorista;
	private Integer numeropedido;
	private Rota rota;
	private Integer lojapedido;
	private TipoRelatorio tipoRelatorio;
	private Date dtemissaoInicio;
	private Date dtemissaoFim;
	private Deposito deposito;
	private String whereIn;
	private Tipoentrega tipoentrega;
	
	
	//Get's
	public Integer getCdmanifesto() {
		return cdmanifesto;
	}
	public Transportador getTransportador() {
		return transportador;
	}
	public Motorista getMotorista() {
		return motorista;
	}
	public Integer getNumeropedido() {
		return numeropedido;
	}
	public Rota getRota() {
		return rota;
	}
	public Integer getLojapedido() {
		return lojapedido;
	}
	public TipoRelatorio getTipoRelatorio() {
		return tipoRelatorio;
	}
	public Date getDtemissaoInicio() {
		return dtemissaoInicio;
	}
	public Date getDtemissaoFim() {
		return dtemissaoFim;
	}
	public Deposito getDeposito() {
		return deposito;
	}
	public String getWhereIn() {
		return whereIn;
	}
	public Tipoentrega getTipoentrega() {
		return tipoentrega;
	}
	
	
	//Set's
	public void setCdmanifesto(Integer cdmanifesto) {
		this.cdmanifesto = cdmanifesto;
	}
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}
	public void setNumeropedido(Integer numeropedido) {
		this.numeropedido = numeropedido;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
	}
	public void setLojapedido(Integer lojapedido) {
		this.lojapedido = lojapedido;
	}
	public void setTipoRelatorio(TipoRelatorio tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}
	public void setDtemissaoInicio(Date dtemissaoInicio) {
		this.dtemissaoInicio = dtemissaoInicio;
	}
	public void setDtemissaoFim(Date dtemissaoFim) {
		this.dtemissaoFim = dtemissaoFim;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setWhereIn(String whereIn) {
		this.whereIn = whereIn;
	}
	public void setTipoentrega(Tipoentrega tipoentrega) {
		this.tipoentrega = tipoentrega;
	}
	
}