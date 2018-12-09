package br.com.linkcom.wms.util.expedicao;

public class EtiquetasProdutoSeparacaoVO implements Cloneable {

	protected Integer cdetiquetaexpedicao;
	protected Integer cdpessoaendereco;
	protected String pedido;
	protected String nomefilial;
	protected Integer carga;
	protected String box;
	protected String enderecoPiking;
	protected String codigoDescricao;
	protected String volume;
	protected String endereco;
	protected String volumeEntrega;
	protected String data;
	protected String codigo;
	protected String cliente;
	protected StringBuilder totalEntrega;
	protected String linhaSeparacao;
	protected Integer ordemservico;
	protected Long qtdeembalagem;

	
	public Integer getCdetiquetaexpedicao() {
		return cdetiquetaexpedicao;
	}
	public Integer getCdpessoaendereco() {
		return cdpessoaendereco;
	}
	public String getPedido() {
		return pedido;
	}
	public Integer getCarga() {
		return carga;
	}
	public String getBox() {
		return box;
	}
	public String getEnderecoPiking() {
		return enderecoPiking;
	}
	public String getCodigoDescricao() {
		return codigoDescricao;
	}
	public String getVolume() {
		return volume;
	}
	public String getEndereco() {
		return endereco;
	}
	public String getVolumeEntrega() {
		return volumeEntrega;
	}
	public String getData() {
		return data;
	}
	public String getCodigo() {
		return codigo;
	}
	public String getCliente() {
		return cliente;
	}
	public String getLinhaSeparacao() {
		return linhaSeparacao;
	}
	public Integer getOrdemservico() {
		return ordemservico;
	}
	public String getNomefilial() {
		return nomefilial;
	}
	public StringBuilder getTotalEntrega() {
		return totalEntrega;
	}
	public Long getQtdeembalagem() {
		return qtdeembalagem;
	}

	
	public void setQtdeembalagem(Long qtdeembalagem) {
		this.qtdeembalagem = qtdeembalagem;
	}
	public void setCdetiquetaexpedicao(Integer cdetiquetaexpedicao) {
		this.cdetiquetaexpedicao = cdetiquetaexpedicao;
	}
	public void setCdpessoaendereco(Integer cdpessoaendereco) {
		this.cdpessoaendereco = cdpessoaendereco;
	}
	public void setPedido(String pedido) {
		this.pedido = pedido;
	}
	public void setCarga(Integer carga) {
		this.carga = carga;
	}
	public void setBox(String box) {
		this.box = box;
	}
	public void setEnderecoPiking(String enderecoPiking) {
		this.enderecoPiking = enderecoPiking;
	}
	public void setCodigoDescricao(String codigoDescricao) {
		this.codigoDescricao = codigoDescricao;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public void setVolumeEntrega(String volumeEntrega) {
		this.volumeEntrega = volumeEntrega;
	}
	public void setData(String data) {
		this.data = data;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public void setTotalEntrega(StringBuilder totalEntrega) {
		this.totalEntrega = totalEntrega;
	}
	public void setLinhaSeparacao(String linhaSeparacao) {
		this.linhaSeparacao = linhaSeparacao;
	}
	public void setOrdemservico(Integer ordemservico) {
		this.ordemservico = ordemservico;
	}
	public void setNomefilial(String nomefilial) {
		this.nomefilial = nomefilial;
	}
	@Override
	public EtiquetasProdutoSeparacaoVO clone() throws CloneNotSupportedException {
		return (EtiquetasProdutoSeparacaoVO)super.clone();
	}
	
	
	/*
	 * Esta classe foi criada para que a etiqueta guardasse um valor inteiro
	 * por referência
	 */
	public class newString{
		private String val;
		
		public String getVal() {
			return val;
		}
		
		public void setVal(String val) {
			this.val = val;
		}
		
		@Override
		public String toString() {
			return val.toString();
		}
	}
}
