package br.com.linkcom.wms.modulo.recebimento.controller.process.filtro;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.Hora;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculo;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Fornecedor;
import br.com.linkcom.wms.geral.bean.Notafiscaltipo;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;
import br.com.linkcom.wms.geral.bean.Tipoveiculo;

public class RecebimentoFiltro extends FiltroListagem{
	
	//dados filtro
	protected Date emissaode;
	protected Date emissaoate;
	protected Fornecedor fornecedor;
	protected String veiculo;
	protected String notafiscal;
	protected Notafiscaltipo notafiscaltipo;
	protected Produto produto;
	protected String numerorav;
	
	//dados do cadastro
	protected String observacao;
	protected Box box;
	protected Tipoveiculo tipoveiculo;
	protected Recebimento recebimento;
	protected Ordemservico ordemservico;
	protected Date dataChegada;
	protected Hora horaChegada;
	
	//dados complementares para a listagem de recebimento
	protected Integer numeroRecebimento;
	protected Recebimentostatus recebimentostatus;
	protected Date montadade = new Date(System.currentTimeMillis());
	protected Date montadaate = new Date(System.currentTimeMillis());
	
	protected List<Acompanhamentoveiculo> listaAcompanhamentoVeiculo = new ArrayList<Acompanhamentoveiculo>();
	
		
	@DisplayName("Data de emissão")
	public Date getEmissaode() {
		return emissaode;
	}
	
	public Date getMontadaate() {
		return montadaate;
	}
	
	public Date getMontadade() {
		return montadade;
	}
	
	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}
	
	
	public Date getEmissaoate() {
		return emissaoate;
	}
	
	@DisplayName("Fornecedor")
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	
	@DisplayName("Placa veículo")
	@MaxLength(7)
	public String getVeiculo() {
		return veiculo;
	}
	
	@DisplayName("Nota fiscal")
	@MaxLength(15)
	public String getNotafiscal() {
		return notafiscal;
	}
	
	@DisplayName("Tipo")
	public Notafiscaltipo getNotafiscaltipo() {
		return notafiscaltipo;
	}
	
	public Produto getProduto() {
		return produto;
	}
	
	public Ordemservico getOrdemservico() {
		return ordemservico;
	}
	
	@DisplayName("Observação")
	public String getObservacao() {
		return observacao;
	}
	
	@DisplayName("Box")
	public Box getBox() {
		return box;
	}
	
	@DisplayName("Tipo de veículo")
	public Tipoveiculo getTipoveiculo() {
		return tipoveiculo;
	}
	
	@DisplayName("Número do recebimento")
	public Integer getNumeroRecebimento() {
		return numeroRecebimento;
	}
	
	@DisplayName("Status")
	public Recebimentostatus getRecebimentostatus() {
		return recebimentostatus;
	}
	
	@DisplayName("Data de chegada")
	public Date getDataChegada() {
		return dataChegada;
	}
	
	@DisplayName("Hora de chegada")
	public Hora getHoraChegada() {
		return horaChegada;
	}
	
	public Recebimento getRecebimento() {
		return recebimento;
	}
	
	public List<Acompanhamentoveiculo> getListaAcompanhamentoVeiculo() {
		return listaAcompanhamentoVeiculo;
	}

	public void setListaAcompanhamentoVeiculo(List<Acompanhamentoveiculo> listaAcompanhamentoVeiculo) {
		this.listaAcompanhamentoVeiculo = listaAcompanhamentoVeiculo;
	}
	
	public void setEmissaode(Date emissaode) {
		this.emissaode = emissaode;
	}
	
	public void setEmissaoate(Date emissaoate) {
		this.emissaoate = emissaoate;
	}
	
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public void setVeiculo(String veiculo) {
		this.veiculo = veiculo;
	}
	
	public void setNotafiscal(String notafiscal) {
		this.notafiscal = notafiscal;
	}
	
	public void setNotafiscaltipo(Notafiscaltipo notafiscaltipo) {
		this.notafiscaltipo = notafiscaltipo;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public void setBox(Box box) {
		this.box = box;
	}
	
	public String getNumerorav() {
		return numerorav;
	}

	public void setNumerorav(String numerorav) {
		this.numerorav = numerorav;
	}

	public void setTipoveiculo(Tipoveiculo tipoveiculo) {
		this.tipoveiculo = tipoveiculo;
	}
	
	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}
	
	public void setNumeroRecebimento(Integer numeroRecebimento) {
		this.numeroRecebimento = numeroRecebimento;
	}
	
	public void setRecebimentostatus(Recebimentostatus recebimentostatus) {
		this.recebimentostatus = recebimentostatus;
	}
	
	public void setMontadaate(Date montadaate) {
		this.montadaate = montadaate;
	}
	public void setMontadade(Date montadade) {
		this.montadade = montadade;
	}
	
	public void setDataChegada(Date dataChegada) {
		this.dataChegada = dataChegada;
	}
	
	public void setHoraChegada(Hora horaChegada) {
		this.horaChegada = horaChegada;
	}
	
}
