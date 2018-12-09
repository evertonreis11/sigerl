package br.com.linkcom.wms.util.recebimento;

import java.awt.Image;

public class EtiquetasVO {
	
	private String codProduto;
	private String descricao;
	private String volume;
	private String codigobarras;
	private String endereco;
	private String descricaoEndereco;
	private String caracteristica;
	private String caracteristicaCodigo;
	private Image imagemCodigoBarra;
	private String pathImagemCodigoBarra;
	
	
	public String getCaracteristicaCodigo() {
		return caracteristicaCodigo;
	}
	
	public String getCodProduto() {
		return codProduto;
	}
	public String getDescricao() {
		return descricao;
	}
	public String getVolume() {
		return volume;
	}
	public String getCodigobarras() {
		return codigobarras;
	}
	public String getEndereco() {
		return endereco;
	}
	public String getDescricaoEndereco() {
		return descricaoEndereco;
	}
	public String getCaracteristica() {
		return caracteristica;
	}
	
	
	public String getPathImagemCodigoBarra() {
		return pathImagemCodigoBarra;
	}

	public void setPathImagemCodigoBarra(String pathImagemCodigoBarra) {
		this.pathImagemCodigoBarra = pathImagemCodigoBarra;
	}

	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public void setCodigobarras(String codigobarras) {
		this.codigobarras = codigobarras;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public void setDescricaoEndereco(String descricaoEndereco) {
		this.descricaoEndereco = descricaoEndereco;
	}
	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
	}
	public void setCaracteristicaCodigo(String caracteristicaCodigo) {
		this.caracteristicaCodigo = caracteristicaCodigo;
	}

	public Image getImagemCodigoBarra() {
		return imagemCodigoBarra;
	}

	public void setImagemCodigoBarra(Image imagemCodigoBarra) {
		this.imagemCodigoBarra = imagemCodigoBarra;
	}
}
