package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@SequenceGenerator(name = "sq_ordemservicoprodutoerrado", sequenceName = "sq_ordemservicoprodutoerrado")
public class Ordemservicoprodutoerrado {

	private Integer cdordemservicoprodutoerrado;
	private Ordemservico ordemservico;
	private Produto produto;
	private Etiquetaexpedicao etiquetaexpedicao;
	private String codigoProduto;
	private String codigoEtiqueta;
	private Usuario usuariobloqueio;
	private Timestamp dtbloqueio;
	private Usuario usuariolibera;
	private Timestamp dtlibera;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_ordemservicoprodutoerrado")
	public Integer getCdordemservicoprodutoerrado() {
		return cdordemservicoprodutoerrado;
	}

	@DisplayName("Ordem de serviço")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdordemservico")
	public Ordemservico getOrdemservico() {
		return ordemservico;
	}

	@DisplayName("Produto")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	public Produto getProduto() {
		return produto;
	}

	@DisplayName("Etiqueta")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdetiquetaexpedicao")
	public Etiquetaexpedicao getEtiquetaexpedicao() {
		return etiquetaexpedicao;
	}
	
	@DisplayName("Etiqueta lida")
	public String getCodigoEtiqueta() {
		return codigoEtiqueta;
	}
	
	@DisplayName("Produto lido")
	public String getCodigoProduto() {
		return codigoProduto;
	}

	@DisplayName("Conferente")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdusuariobloqueio")
	public Usuario getUsuariobloqueio() {
		return usuariobloqueio;
	}

	@DisplayName("Bloqueado em")
	public Timestamp getDtbloqueio() {
		return dtbloqueio;
	}

	@DisplayName("Responsável")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdusuariolibera")
	public Usuario getUsuariolibera() {
		return usuariolibera;
	}

	@DisplayName("Liberado em")
	public Timestamp getDtlibera() {
		return dtlibera;
	}

	public void setCdordemservicoprodutoerrado(
			Integer cdordemservicoprodutoerrado) {
		this.cdordemservicoprodutoerrado = cdordemservicoprodutoerrado;
	}

	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setEtiquetaexpedicao(Etiquetaexpedicao etiquetaexpedicao) {
		this.etiquetaexpedicao = etiquetaexpedicao;
	}
	
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	public void setCodigoEtiqueta(String codigoEtiqueta) {
		this.codigoEtiqueta = codigoEtiqueta;
	}
	
	public void setUsuariobloqueio(Usuario usuariobloqueio) {
		this.usuariobloqueio = usuariobloqueio;
	}

	public void setDtbloqueio(Timestamp dtbloqueio) {
		this.dtbloqueio = dtbloqueio;
	}

	public void setUsuariolibera(Usuario usuariolibera) {
		this.usuariolibera = usuariolibera;
	}

	public void setDtlibera(Timestamp dtlibera) {
		this.dtlibera = dtlibera;
	}
	
}
