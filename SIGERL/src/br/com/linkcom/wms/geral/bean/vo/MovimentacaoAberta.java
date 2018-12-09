package br.com.linkcom.wms.geral.bean.vo;

import java.util.Date;


public class MovimentacaoAberta {

	private Long qtde;
	private String tipoOperacao;
	private Integer ordemServico;
	private Integer carregamento;
	private Integer recebimento;
	private Integer transferencia;
	private Integer reabastecimento;
	private Date dataOrdemServico;
	private Integer expedicao;

	public Long getQtde() {
		return qtde;
	}

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}

	public String getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public Integer getOrdemServico() {
		return ordemServico;
	}

	public void setOrdemServico(Integer ordemServico) {
		this.ordemServico = ordemServico;
	}

	public Integer getCarregamento() {
		return carregamento;
	}

	public void setCarregamento(Integer carregamento) {
		this.carregamento = carregamento;
	}

	public Integer getRecebimento() {
		return recebimento;
	}

	public void setRecebimento(Integer recebimento) {
		this.recebimento = recebimento;
	}

	public Integer getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(Integer transferencia) {
		this.transferencia = transferencia;
	}
	
	public Integer getReabastecimento() {
		return reabastecimento;
	}
	
	public void setReabastecimento(Integer reabastecimento) {
		this.reabastecimento = reabastecimento;
	}
	
	public Date getDataOrdemServico() {
		return dataOrdemServico;
	}
	
	public void setDataOrdemServico(Date dataOrdemServico) {
		this.dataOrdemServico = dataOrdemServico;
	}
	
	public Integer getExpedicao() {
		return expedicao;
	}
	
	public void setExpedicao(Integer expedicao) {
		this.expedicao = expedicao;
	}

}
