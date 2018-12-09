package br.com.linkcom.wms.util.logistica;

public class OrdemVO {

	private Integer os;
	private Integer carregamento;
	private String linhaSeparacao;
	private EnderecoAux enderecoOrigem;
	private String produto;
	private String embalagem;
	private Long qtde;
	private EnderecoAux enderecoDestino;
	private Integer recebimento;
	private Integer Box;
	private Integer transferencia;
	private String tipoArmazenagem;
	private Long qtdeEmbalagem;
	private Integer cdordemtipo;
	private Integer cdproduto;

	public Integer getOs() {
		return os;
	}

	public Integer getCarregamento() {
		return carregamento;
	}

	public String getLinhaSeparacao() {
		return linhaSeparacao;
	}
	
	public EnderecoAux getEnderecoDestino() {
		return enderecoDestino;
	}
	
	public EnderecoAux getEnderecoOrigem() {
		return enderecoOrigem;
	}

	public String getProduto() {
		return produto;
	}

	public String getEmbalagem() {
		return embalagem;
	}

	public Long getQtde() {
		return qtde;
	}

	public Integer getRecebimento() {
		return recebimento;
	}

	public Integer getBox() {
		return Box;
	}

	public Integer getTransferencia() {
		return transferencia;
	}

	public String getTipoArmazenagem() {
		return tipoArmazenagem;
	}
	
	public void setOs(Integer os) {
		this.os = os;
	}

	public void setCarregamento(Integer carregamento) {
		this.carregamento = carregamento;
	}

	public void setLinhaSeparacao(String linhaSeparacao) {
		this.linhaSeparacao = linhaSeparacao;
	}
	
	public void setEnderecoDestino(EnderecoAux enderecoDestino) {
		this.enderecoDestino = enderecoDestino;
	}
	
	public void setEnderecoOrigem(EnderecoAux enderecoOrigem) {
		this.enderecoOrigem = enderecoOrigem;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public void setEmbalagem(String embalagem) {
		this.embalagem = embalagem;
	}

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}

	public void setRecebimento(Integer recebimento) {
		this.recebimento = recebimento;
	}

	public void setBox(Integer box) {
		Box = box;
	}

	public void setTransferencia(Integer transferencia) {
		this.transferencia = transferencia;
	}

	public void setTipoArmazenagem(String tipoArmazenagem) {
		this.tipoArmazenagem = tipoArmazenagem;
	}

	public String getEnderecoDestinoStr() {
		if (enderecoDestino != null)
			return enderecoDestino.toString();
		else
			return null;
	}

	public void setEnderecoDestinoStr(String enderecoDestinoStr) {
		if (enderecoDestinoStr != null && enderecoDestinoStr.matches("\\d{2}\\.\\d{3}\\.\\d{3}\\.\\d{2}\\.\\d{3}")) {
			String endereco = enderecoDestinoStr.substring(3);
			String area = enderecoDestinoStr.substring(0, 2);
			this.enderecoDestino = new EnderecoAux(area, endereco);
		}
	}

	public String getEnderecoOrigemStr() {
		if (enderecoDestino != null)
			return enderecoOrigem.toString();
		else
			return null;
	}

	public void setEnderecoOrigemStr(String enderecoOrigemStr) {
		if (enderecoOrigemStr != null && enderecoOrigemStr.matches("\\d{2}\\.\\d{3}\\.\\d{3}\\.\\d{2}\\.\\d{3}")) {
			String endereco = enderecoOrigemStr.substring(3);
			String area = enderecoOrigemStr.substring(0, 2);
			this.enderecoOrigem = new EnderecoAux(area, endereco);
		}
	}

	public Long getQtdeEmbalagem() {
		return qtdeEmbalagem;
	}

	public void setQtdeEmbalagem(Long qtdeEmbalagem) {
		this.qtdeEmbalagem = qtdeEmbalagem;
	}

	public Integer getCdordemtipo() {
		return cdordemtipo;
	}

	public void setCdordemtipo(Integer cdordemtipo) {
		this.cdordemtipo = cdordemtipo;
	}
	
	public Integer getCdproduto() {
		return cdproduto;
	}
	
	public void setCdproduto(Integer cdproduto) {
		this.cdproduto = cdproduto;
	}
	
}
