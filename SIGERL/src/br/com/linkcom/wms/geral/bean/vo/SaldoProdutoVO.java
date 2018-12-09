package br.com.linkcom.wms.geral.bean.vo;

import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Produto;

public class SaldoProdutoVO {

	//TODO: Apagar estes dois atributos foram mantidos durante fase de sincronização de código
	protected Produto produto;
	protected Long qtdeEstoque;


	private Integer cdproduto;
	private Long codigoerp;
	private String codigoproduto;
	private String descricaoproduto;
	private String nomedeposito;
	private Long qtde;
	private Money valorunitario;
	private Integer cddeposito;
	private Money valortotal;
	private Double peso;
	private Double pesototal;
	private Long volumes;
	private Long volumesarmazenados;
	private Long avaria;
	private Long volumesdivergentes;

	public Integer getCdproduto() {
		return cdproduto;
	}

	public Long getCodigoerp() {
		return codigoerp;
	}

	public void setCdproduto(Integer cdproduto) {
		this.cdproduto = cdproduto;
	}

	public void setCodigoerp(Long codigoerp) {
		this.codigoerp = codigoerp;
	}

	public String getCodigoproduto() {
		return codigoproduto;
	}

	public String getDescricaoproduto() {
		return descricaoproduto;
	}

	public String getNomedeposito() {
		return nomedeposito;
	}

	public Long getQtde() {
		return qtde;
	}

	public Money getValorunitario() {
		return valorunitario;
	}

	public Integer getCddeposito() {
		return cddeposito;
	}

	public Money getValortotal() {
		return valortotal;
	}

	public Double getPeso() {
		return peso;
	}

	public Double getPesototal() {
		return pesototal;
	}

	public Long getVolumes() {
		return volumes;
	}

	public Long getVolumesarmazenados() {
		return volumesarmazenados;
	}

	public Long getAvaria() {
		return avaria;
	}

	public Long getVolumesdivergentes() {
		return volumesdivergentes;
	}

	public void setVolumes(Long volumes) {
		this.volumes = volumes;
	}

	public void setVolumesarmazenados(Long volumesarmazenados) {
		this.volumesarmazenados = volumesarmazenados;
	}

	public void setAvaria(Long avaria) {
		this.avaria = avaria;
	}

	public void setVolumesdivergentes(Long volumesdivergentes) {
		this.volumesdivergentes = volumesdivergentes;
	}

	public void setCodigoproduto(String codigoproduto) {
		this.codigoproduto = codigoproduto;
	}

	public void setDescricaoproduto(String descricaoproduto) {
		this.descricaoproduto = descricaoproduto;
	}

	public void setNomedeposito(String nomedeposito) {
		this.nomedeposito = nomedeposito;
	}

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}

	public void setValorunitario(Money valorunitario) {
		this.valorunitario = valorunitario;
	}

	public void setCddeposito(Integer cddeposito) {
		this.cddeposito = cddeposito;
	}

	public void setValortotal(Money valortotal) {
		this.valortotal = valortotal;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public void setPesototal(Double pesototal) {
		this.pesototal = pesototal;
	}

	public Produto getProduto() {
		return produto;
	}

	public Long getQtdeEstoque() {
		return qtdeEstoque;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setQtdeEstoque(Long qtdeEstoque) {
		this.qtdeEstoque = qtdeEstoque;
	}

}
