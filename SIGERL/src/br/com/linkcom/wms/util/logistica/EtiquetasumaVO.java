package br.com.linkcom.wms.util.logistica;

/**
 * 
 * @author Arantes
 *
 */
public class EtiquetasumaVO {
	
	private String data;
	private String enderecoDestino;
	private String enderecoDadoLogistico;
	private String produtoCodigoDescricao;
	private String produtoEmbalagemDescricao;
	private Long lastroTipoPaleteCamada;
	private Long camadaTipoPaleteCamada;
	private Long calculoQtde;
	private String enderecoProdutoId;
	private Integer recebimento;
	
	public String getData() {
		return data;
	}
	public String getEnderecoDestino() {
		return enderecoDestino;
	}
	
	public String getEnderecoDadoLogistico() {
		return enderecoDadoLogistico;
	}
	
	public String getProdutoCodigoDescricao() {
		return produtoCodigoDescricao;
	}
	
	public String getProdutoEmbalagemDescricao() {
		return produtoEmbalagemDescricao;
	}
	
	public Long getLastroTipoPaleteCamada() {
		return lastroTipoPaleteCamada;
	}
	
	public Long getCamadaTipoPaleteCamada() {
		return camadaTipoPaleteCamada;
	}
	
	public Long getCalculoQtde() {
		return calculoQtde;
	}
	
	public String getEnderecoProdutoId() {
		return enderecoProdutoId;
	}

	
	public Integer getRecebimento() {
		return recebimento;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public void setEnderecoDestino(String enderecoDestino) {
		this.enderecoDestino = enderecoDestino;
	}
	
	public void setEnderecoDadoLogistico(String enderecoDadoLogistico) {
		this.enderecoDadoLogistico = enderecoDadoLogistico;
	}
	
	public void setProdutoCodigoDescricao(String produtoCodigoDescricao) {
		this.produtoCodigoDescricao = produtoCodigoDescricao;
	}
	
	public void setProdutoEmbalagemDescricao(String produtoEmbalagemDescricao) {
		this.produtoEmbalagemDescricao = produtoEmbalagemDescricao;
	}
	
	public void setLastroTipoPaleteCamada(Long lastroTipoPaleteCamada) {
		this.lastroTipoPaleteCamada = lastroTipoPaleteCamada;
	}
	
	public void setCamadaTipoPaleteCamada(Long camadaTipoPaleteCamada) {
		this.camadaTipoPaleteCamada = camadaTipoPaleteCamada;
	}
	
	public void setCalculoQtde(Long calculoQtde) {
		this.calculoQtde = calculoQtde;
	}

	public void setEnderecoProdutoId(String enderecoProdutoId) {
		this.enderecoProdutoId = enderecoProdutoId;
	}

	public void setRecebimento(Integer recebimento) {
		this.recebimento = recebimento;
	}

}
