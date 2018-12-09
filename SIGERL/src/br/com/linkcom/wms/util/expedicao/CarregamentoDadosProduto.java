package br.com.linkcom.wms.util.expedicao;

import java.util.List;

import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.geral.bean.Veiculo;

public class CarregamentoDadosProduto {
	
	private Integer cdcarregamento;
	private Veiculo veiculo;
	private Transportador transportador;
	private Box box;
	private Cliente filialretirada;
	private Tipooperacao tipooperacaoretirada;
	private Boolean localretirada;
	private List<CarregamentoApoio> listaCarregamentoApoio = new ListSet<CarregamentoApoio>(CarregamentoApoio.class);
	
	public CarregamentoDadosProduto(){
	}

	public CarregamentoDadosProduto(Integer cdcarregamento, Veiculo veiculo, Transportador transportador, Box box, Cliente cliente, 
			 						Tipooperacao tipooperacao, Boolean localretirada){
		this.cdcarregamento = cdcarregamento;
		this.veiculo = veiculo;
		this.transportador = transportador;
		this.box = box;
		this.filialretirada = cliente;
		this.tipooperacaoretirada = tipooperacao;
		this.localretirada = localretirada;
	}
	
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public Transportador getTransportador() {
		return transportador;
	}
	public Box getBox() {
		return box;
	}
	public List<CarregamentoApoio> getListaCarregamentoApoio() {
		return listaCarregamentoApoio;
	}
	public Cliente getFilialretirada() {
		return filialretirada;
	}
	public Tipooperacao getTipooperacaoretirada() {
		return tipooperacaoretirada;
	}
	public Boolean getLocalretirada() {
		return localretirada;
	}
	
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
	public void setBox(Box box) {
		this.box = box;
	}
	public void setListaCarregamentoApoio(
			List<CarregamentoApoio> listaCarregamentoApoio) {
		this.listaCarregamentoApoio = listaCarregamentoApoio;
	}
	public void setFilialretirada(Cliente filialretirada) {
		this.filialretirada = filialretirada;
	}
	public void setTipooperacaoretirada(Tipooperacao tipooperacaoretirada) {
		this.tipooperacaoretirada = tipooperacaoretirada;
	}
	public void setLocalretirada(Boolean localretirada) {
		this.localretirada = localretirada;
	}
	
}
