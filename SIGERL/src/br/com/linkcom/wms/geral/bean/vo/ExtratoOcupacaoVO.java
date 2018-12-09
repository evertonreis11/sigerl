package br.com.linkcom.wms.geral.bean.vo;

import java.sql.Timestamp;

public class ExtratoOcupacaoVO {

	private Integer cdordemservico;
	private String endereco;
	private Integer cdendereco;
	private String descricaoProduto;
	private String codigoProduto;
	private Integer cdproduto;
	private Timestamp dtentrada;
	private Long qtde;
	private Integer acumula;
	private String tipoOperacao;
	private String operador;
	private Integer numeroDoc;
	private Integer cdcarregamento;
	private Integer cdtransferencia;
	private Integer cdinventario;
	private Integer cdrecebimento;
	private Integer cdocupacaoenderecohistorico;
	private Long entrada;
	private Long saida;
	private Long saldoParcial;

	public ExtratoOcupacaoVO() {
	}

	public String getDescricaoDoc(){
		if (cdordemservico == null)
			return "";
		
		String descricaoDoc = cdordemservico.toString();
		
		if (cdcarregamento != null)
			descricaoDoc += " / " + cdcarregamento;
		if (cdrecebimento != null)
			descricaoDoc += " / " + cdrecebimento;
		if (cdinventario != null)
			descricaoDoc += " / " + cdinventario;
		if (cdtransferencia != null)
			descricaoDoc += " / " + cdtransferencia;
		
		return descricaoDoc;
	}
	
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Integer getCdendereco() {
		return cdendereco;
	}

	public void setCdendereco(Integer cdendereco) {
		this.cdendereco = cdendereco;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String produto) {
		this.descricaoProduto = produto;
	}

	public Integer getCdproduto() {
		return cdproduto;
	}

	public void setCdproduto(Integer cdproduto) {
		this.cdproduto = cdproduto;
	}

	public Timestamp getDtentrada() {
		return dtentrada;
	}

	public void setDtentrada(Timestamp dtentrada) {
		this.dtentrada = dtentrada;
	}

	public Long getQtde() {
		return qtde;
	}

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}

	public Integer getAcumula() {
		return acumula;
	}

	public void setAcumula(Integer acumula) {
		this.acumula = acumula;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdendereco == null) ? 0 : cdendereco.hashCode());
		result = prime * result + ((cdproduto == null) ? 0 : cdproduto.hashCode());
		return result;
	}

	/**
	 * Verifica se este item de ocupação é igual a um outro, baseando no produto
	 * e no endereço.
	 * 
	 */
	public boolean isProdutoEnderecoEquals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ExtratoOcupacaoVO other = (ExtratoOcupacaoVO) obj;
		if (cdendereco == null) {
			if (other.cdendereco != null)
				return false;
		} else if (!cdendereco.equals(other.cdendereco))
			return false;
		if (cdproduto == null) {
			if (other.cdproduto != null)
				return false;
		} else if (!cdproduto.equals(other.cdproduto))
			return false;
		return true;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public Integer getCdordemservico() {
		return cdordemservico;
	}

	public void setCdordemservico(Integer cdodemservico) {
		this.cdordemservico = cdodemservico;
	}

	public Integer getNumeroDoc() {
		return numeroDoc;
	}

	public void setNumeroDoc(Integer numeroDoc) {
		this.numeroDoc = numeroDoc;
	}

	public Integer getCdcarregamento() {
		return cdcarregamento;
	}

	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}

	public Integer getCdtransferencia() {
		return cdtransferencia;
	}

	public void setCdtransferencia(Integer cdtransferencia) {
		this.cdtransferencia = cdtransferencia;
	}

	public Integer getCdinventario() {
		return cdinventario;
	}

	public void setCdinventario(Integer cdinventario) {
		this.cdinventario = cdinventario;
	}

	public Integer getCdrecebimento() {
		return cdrecebimento;
	}

	public void setCdrecebimento(Integer cdrecebimento) {
		this.cdrecebimento = cdrecebimento;
	}

	public Integer getCdocupacaoenderecohistorico() {
		return cdocupacaoenderecohistorico;
	}

	public void setCdocupacaoenderecohistorico(Integer cdocupacaoenderecohistorico) {
		this.cdocupacaoenderecohistorico = cdocupacaoenderecohistorico;
	}

	public Long getEntrada() {
		return entrada;
	}

	public void setEntrada(Long entrada) {
		this.entrada = entrada;
	}

	public Long getSaida() {
		return saida;
	}

	public void setSaida(Long saida) {
		this.saida = saida;
	}

	public Long getSaldoParcial() {
		return saldoParcial;
	}

	public void setSaldoParcial(Long saldoParcial) {
		this.saldoParcial = saldoParcial;
	}
	
}
