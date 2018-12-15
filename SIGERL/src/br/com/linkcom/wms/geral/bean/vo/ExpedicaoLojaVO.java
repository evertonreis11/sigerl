package br.com.linkcom.wms.geral.bean.vo;

public class ExpedicaoLojaVO {
	
	private Integer cdNotaFiscalSaida;
    
	private String numeroNota;
	
    private String serieNota;
    
    private String chaveNotaFiscal;
    
    private Integer cdPessoa;
    
    private String nomePessoa;
    
    private String documentoPessoa;
    
    private Integer cdNotaFiscalSaidaProduto;
    
    private Integer cdProduto;
    
    private String codigoProduto;
    
    private String descricaoProduto;
    
    private Integer qtde;

	public Integer getCdNotaFiscalSaida() {
		return cdNotaFiscalSaida;
	}

	public void setCdNotaFiscalSaida(Integer cdNotaFiscalSaida) {
		this.cdNotaFiscalSaida = cdNotaFiscalSaida;
	}

	public String getChaveNotaFiscal() {
		return chaveNotaFiscal;
	}

	public void setChaveNotaFiscal(String chaveNotaFiscal) {
		this.chaveNotaFiscal = chaveNotaFiscal;
	}

	public Integer getCdPessoa() {
		return cdPessoa;
	}

	public void setCdPessoa(Integer cdPessoa) {
		this.cdPessoa = cdPessoa;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public String getDocumentoPessoa() {
		return documentoPessoa;
	}

	public void setDocumentoPessoa(String documentoPessoa) {
		this.documentoPessoa = documentoPessoa;
	}

	public Integer getCdNotaFiscalSaidaProduto() {
		return cdNotaFiscalSaidaProduto;
	}

	public void setCdNotaFiscalSaidaProduto(Integer cdNotaFiscalSaidaProduto) {
		this.cdNotaFiscalSaidaProduto = cdNotaFiscalSaidaProduto;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public Integer getQtde() {
		return qtde;
	}

	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}

	public Integer getCdProduto() {
		return cdProduto;
	}

	public void setCdProduto(Integer cdProduto) {
		this.cdProduto = cdProduto;
	}

	public String getSerieNota() {
		return serieNota;
	}

	public void setSerieNota(String serieNota) {
		this.serieNota = serieNota;
	}

	public String getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
	}
}
