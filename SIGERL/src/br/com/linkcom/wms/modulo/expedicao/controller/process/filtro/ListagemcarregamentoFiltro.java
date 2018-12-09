package br.com.linkcom.wms.modulo.expedicao.controller.process.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.geral.bean.Tipoveiculo;
import br.com.linkcom.wms.geral.bean.Veiculo;

public class ListagemcarregamentoFiltro {
	protected Carregamentostatus carregamentostatus;
	protected Veiculo veiculo;
	protected Tipoveiculo tipoveiculo;
	protected Box box;

	@DisplayName("Status do carregamento")
	public Carregamentostatus getCarregamentostatus() {
		return carregamentostatus;
	}
	
	@DisplayName("Veículo")
	public Veiculo getVeiculo() {
		return veiculo;
	}
	
	@DisplayName("Tipo de veículo")
	public Tipoveiculo getTipoveiculo() {
		return tipoveiculo;
	}
	public Box getBox() {
		return box;
	}
	public void setCarregamentostatus(Carregamentostatus carregamentostatus) {
		this.carregamentostatus = carregamentostatus;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	public void setTipoveiculo(Tipoveiculo tipoveiculo) {
		this.tipoveiculo = tipoveiculo;
	}
	public void setBox(Box box) {
		this.box = box;
	}
}
