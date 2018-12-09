package br.com.linkcom.wms.util.recebimento;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Recebimento;

public class DivergenciaRecebimentoVO {

	protected Recebimento recebimento;
	protected String placa;
	protected Timestamp data;
	protected String transportador;
	protected Integer totalDivergente;
	protected Integer totalProdutos;
	protected String conferente;
	protected Long qtdeRecebida;
	protected Long qtdeAvaria;
	protected List<Ordemservicoproduto> listaOrdemServicoProduto = new ArrayList<Ordemservicoproduto>();
	protected TreeMap<String, String> fornecedorNotafiscal = new TreeMap<String, String>();
	
	public Recebimento getRecebimento() {
		return recebimento;
	}

	public String getPlaca() {
		return placa;
	}
	
	public Timestamp getData() {
		return data;
	}
	
	public String getTransportador() {
		return transportador;
	}
	
	public Integer getTotalDivergente() {
		return totalDivergente;
	}
	
	public Integer getTotalProdutos() {
		return totalProdutos;
	}
	
	/*public String getHrinicio() {
		return hrinicio;
	}
	
	public String getHrfim() {
		return hrfim;
	}*/
	
	public List<Ordemservicoproduto> getListaOrdemServicoProduto() {
		return listaOrdemServicoProduto;
	}
		
	public TreeMap<String, String> getFornecedorNotafiscal() {
		return fornecedorNotafiscal;
	}	

	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}
	
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	
	public void setData(Timestamp data) {
		this.data = data;
	}

	public void setTransportador(String transportador) {
		this.transportador = transportador;
	}
	
	public void setTotalDivergente(Integer totalDivergente) {
		this.totalDivergente = totalDivergente;
	}
	
	public void setTotalProdutos(Integer totalProdutos) {
		this.totalProdutos = totalProdutos;
	}
	
	/*public void setHrinicio(String hrinicio) {
		this.hrinicio = hrinicio;
	}
	
	public void setHrfim(String hrfim) {
		this.hrfim = hrfim;
	}*/
	
	public void setListaOrdemServicoProduto(List<Ordemservicoproduto> listaOrdemServicoProduto) {
		this.listaOrdemServicoProduto = listaOrdemServicoProduto;
	}
	
	public void setFornecedorNotafiscal(TreeMap<String, String> fornecedorNotafiscal) {
		this.fornecedorNotafiscal = fornecedorNotafiscal;
	}

	public String getConferente() {
		return conferente;
	}

	public void setConferente(String conferente) {
		this.conferente = conferente;
	}

	public Long getQtdeRecebida() {
		return qtdeRecebida;
	}

	public void setQtdeRecebida(Long qtdeRecebida) {
		this.qtdeRecebida = qtdeRecebida;
	}

	public Long getQtdeAvaria() {
		return qtdeAvaria;
	}

	public void setQtdeAvaria(Long qtdeAvaria) {
		this.qtdeAvaria = qtdeAvaria;
	}
	
}
