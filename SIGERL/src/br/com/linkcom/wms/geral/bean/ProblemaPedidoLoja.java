package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "sq_problemapedidoloja", sequenceName = "sq_problemapedidoloja")
public class ProblemaPedidoLoja {

	private Integer cdProblemaPedidoLoja;
	
	private String descricao;
	
	private PontoControle pontoControle;
	
	private TipoEstoque tipoEstoque;
	
	

	public ProblemaPedidoLoja(Integer cdProblemaPedidoLoja) {
		this.cdProblemaPedidoLoja = cdProblemaPedidoLoja;
	}
	
	public ProblemaPedidoLoja() {}

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_problemapedidoloja")
	public Integer getCdProblemaPedidoLoja() {
		return cdProblemaPedidoLoja;
	}
	
	public void setCdProblemaPedidoLoja(Integer cdProblemaPedidoLoja) {
		this.cdProblemaPedidoLoja = cdProblemaPedidoLoja;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpontodecontrole")
	public PontoControle getPontoControle() {
		return pontoControle;
	}

	public void setPontoControle(PontoControle pontoControle) {
		this.pontoControle = pontoControle;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipoestoque")
	public TipoEstoque getTipoEstoque() {
		return tipoEstoque;
	}

	public void setTipoEstoque(TipoEstoque tipoEstoque) {
		this.tipoEstoque = tipoEstoque;
	}

}
