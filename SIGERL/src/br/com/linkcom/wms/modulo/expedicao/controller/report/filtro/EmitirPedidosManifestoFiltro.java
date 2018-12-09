package br.com.linkcom.wms.modulo.expedicao.controller.report.filtro;

import java.sql.Date;
import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.vo.PedidosmanifestoVO;

public class EmitirPedidosManifestoFiltro extends FiltroListagem{

	private String codigo;
	private Integer cdmanifesto;
	private Date dataInicio;
	private Date dataFim;
	private String numeroPedido;
	private Integer numeroLoja;
	private Deposito depositoOrigem;
	private Deposito depositoDestino;
	private List<PedidosmanifestoVO> listaPedidosmanifestoVO;
	private String dataInicioString;
	private String dataFimString;
	
	
	//Get's
	public String getCodigo() {
		return codigo;
	}
	public Integer getCdmanifesto() {
		return cdmanifesto;
	}
	@Required
	public Date getDataInicio() {
		return dataInicio;
	}
	@Required
	public Date getDataFim() {
		return dataFim;
	}
	public String getNumeroPedido() {
		return numeroPedido;
	}
	public Integer getNumeroLoja() {
		return numeroLoja;
	}
	public Deposito getDepositoDestino() {
		return depositoDestino;
	}
	public Deposito getDepositoOrigem() {
		return depositoOrigem;
	}
	public List<PedidosmanifestoVO> getListaPedidosmanifestoVO() {
		return listaPedidosmanifestoVO;
	}
	public String getDataInicioString() {
		return dataInicioString;
	}
	public String getDataFimString() {
		return dataFimString;
	}
	
	
	//Set's
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setCdmanifesto(Integer cdmanifesto) {
		this.cdmanifesto = cdmanifesto;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	public void setNumeroLoja(Integer numeroLoja) {
		this.numeroLoja = numeroLoja;
	}
	public void setDepositoOrigem(Deposito depositoOrigem) {
		this.depositoOrigem = depositoOrigem;
	}
	public void setDepositoDestino(Deposito depositoDestino) {
		this.depositoDestino = depositoDestino;
	}
	public void setListaPedidosmanifestoVO(List<PedidosmanifestoVO> listaPedidosmanifestoVO) {
		this.listaPedidosmanifestoVO = listaPedidosmanifestoVO;
	}
	public void setDataInicioString(String dataInicioString) {
		this.dataInicioString = dataInicioString;
	}
	public void setDataFimString(String dataFimString) {
		this.dataFimString = dataFimString;
	}

}
