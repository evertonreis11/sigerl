package br.com.linkcom.wms.modulo.expedicao.controller.process.filtro;

import java.sql.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Empresa;
import br.com.linkcom.wms.geral.bean.Motorista;
import br.com.linkcom.wms.geral.bean.Tipoentrega;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.geral.bean.Veiculo;

public class BorderoFiltro extends FiltroListagem{
	
	private Integer cdbordero;
	private Deposito deposito;
	private Transportador transportador;
	private Motorista motorista;
	private Veiculo veiculo;
	private Date dtemissaoInicio;
	private Date dtemissaoFim;
	private Integer numeroManifesto;
	private Money totalSelecionado = new Money(0);
	private String whereIn;
	private Integer manifestosSelecionados = 0;
	private Tipoentrega tipoentrega;
	private Empresa empresa;
	private Integer numeroBordero;
	private List<Integer> cdManifestosSelecionados;
	
	// Atributos utilizados na paginação
	private String dtemissaoInicioStr;
	private String dtemissaoFimStr;
	
	//Atributo utilizado na gravação dos manisfestos selecionados para geração de Borderô
	private String cdmanifestoSelecionadosStr;
	
	//Get's
	public Integer getCdbordero() {
		return cdbordero;
	}
	public Deposito getDeposito() {
		return deposito;
	}
	public Transportador getTransportador() {
		return transportador;
	}
	public Motorista getMotorista() {
		return motorista;
	}
	@DisplayName("Placa do Veículo")
	public Veiculo getVeiculo() {
		return veiculo;
	}	
	public Date getDtemissaoFim() {
		return dtemissaoFim;
	}
	public Date getDtemissaoInicio() {
		return dtemissaoInicio;
	}
	@DisplayName("Núm. Manifesto")
	public Integer getNumeroManifesto() {
		return numeroManifesto;
	}
	public Money getTotalSelecionado() {
		if (totalSelecionado == null){
			totalSelecionado = new Money(0);
		}
		return totalSelecionado;
	}
	public String getWhereIn() {
		return whereIn;
	}
	public Integer getManifestosSelecionados() {
		if (manifestosSelecionados == null){
			manifestosSelecionados = NumberUtils.INTEGER_ZERO;
		}
		return manifestosSelecionados;
	}
	public Tipoentrega getTipoentrega() {
		return tipoentrega;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public Integer getNumeroBordero() {
		return numeroBordero;
	}
	public String getDtemissaoInicioStr() {
		return dtemissaoInicioStr;
	}
	public String getDtemissaoFimStr() {
		return dtemissaoFimStr;
	}
	public String getCdmanifestoSelecionadosStr() {
		return cdmanifestoSelecionadosStr;
	}
	
	//Set's
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setCdbordero(Integer cdbordero) {
		this.cdbordero = cdbordero;
	}
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	public void setDtemissaoInicio(Date dtemissaoInicio) {
		this.dtemissaoInicio = dtemissaoInicio;
	}
	public void setDtemissaoFim(Date dtemissaoFim) {
		this.dtemissaoFim = dtemissaoFim;
	}
	public void setNumeroManifesto(Integer numeroManifesto) {
		this.numeroManifesto = numeroManifesto;
	}
	public void setTotalSelecionado(Money totalSelecionado) {
		this.totalSelecionado = totalSelecionado;
	}
	public void setWhereIn(String whereIn) {
		this.whereIn = whereIn;
	}
	public void setManifestosSelecionados(Integer manifestosSelecionados) {
		this.manifestosSelecionados = manifestosSelecionados;
	}
	public void setTipoentrega(Tipoentrega tipoentrega) {
		this.tipoentrega = tipoentrega;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public void setNumeroBordero(Integer numeroBordero) {
		this.numeroBordero = numeroBordero;
	}
	public void setDtemissaoInicioStr(String dtemissaoInicioStr) {
		this.dtemissaoInicioStr = dtemissaoInicioStr;
	}
	public void setDtemissaoFimStr(String dtemissaoFimStr) {
		this.dtemissaoFimStr = dtemissaoFimStr;
	}
	public void setCdmanifestoSelecionadosStr(String cdmanifestoSelecionadosStr) {
		this.cdmanifestoSelecionadosStr = cdmanifestoSelecionadosStr;
	}
	public List<Integer> getCdManifestosSelecionados() {
		return cdManifestosSelecionados;
	}
	public void setCdManifestosSelecionados(List<Integer> cdManifestosSelecionados) {
		this.cdManifestosSelecionados = cdManifestosSelecionados;
	}
	
}
