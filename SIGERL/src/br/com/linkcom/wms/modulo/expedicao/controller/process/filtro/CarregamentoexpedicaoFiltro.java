package br.com.linkcom.wms.modulo.expedicao.controller.process.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.geral.bean.Veiculo;

public class CarregamentoexpedicaoFiltro extends FiltroListagem {

	protected Integer cdcarregamento;
	protected Carregamentostatus carregamentostatus;
	protected Date dtmontageminicial;
	protected Date dtmontagemfinal;
	protected Date dtfinalizadainicial;
	protected Date dtfinalizadafinal;
	protected Box box;
	protected Transportador transportador;
	protected Veiculo veiculo;
	
	@DisplayName("Transportador")
	public Transportador getTransportador() {
		return transportador;
	}
	
	@DisplayName("Veículo")
	public Veiculo getVeiculo() {
		return veiculo;
	}
	
	@DisplayName("Status do carregamento")
	public Carregamentostatus getCarregamentostatus() {
		return carregamentostatus;
	}
	
	public Box getBox() {
		return box;
	}
	
	@DisplayName("Número do carregamento")
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	
	public Date getDtmontageminicial() {
		return dtmontageminicial;
	}

	public Date getDtmontagemfinal() {
		return dtmontagemfinal;
	}

	public Date getDtfinalizadainicial() {
		return dtfinalizadainicial;
	}

	public Date getDtfinalizadafinal() {
		return dtfinalizadafinal;
	}
	
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
	
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	
	public void setBox(Box box) {
		this.box = box;
	}
	
	public void setCarregamentostatus(Carregamentostatus carregamentostatus) {
		this.carregamentostatus = carregamentostatus;
	}
	
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}
	
	public void setDtmontageminicial(Date dtmontageminicial) {
		this.dtmontageminicial = dtmontageminicial;
	}

	public void setDtmontagemfinal(Date dtmontagemfinal) {
		this.dtmontagemfinal = dtmontagemfinal;
	}

	public void setDtfinalizadainicial(Date dtfinalizadainicial) {
		this.dtfinalizadainicial = dtfinalizadainicial;
	}

	public void setDtfinalizadafinal(Date dtfinalizadafinal) {
		this.dtfinalizadafinal = dtfinalizadafinal;
	}
	
}
