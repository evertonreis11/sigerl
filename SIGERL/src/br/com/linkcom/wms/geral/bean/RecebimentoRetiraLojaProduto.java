package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

// TODO: Auto-generated Javadoc
/**
 * The Class RecebimentoRetiraLojaProduto.
 */
@Entity
@SequenceGenerator(name = "sq_recebretiralojaproduto", sequenceName = "sq_recebretiralojaproduto")
public class RecebimentoRetiraLojaProduto {
	
	/** The cd recebimento retira loja produto. */
	private Integer cdRecebimentoRetiraLojaProduto;

	/** The produto. */
	private Produto produto;
	
	/** The qtde. */
	private Integer qtde;
	
	/** The notafiscalsaida. */
	private Notafiscalsaida notafiscalsaida;
	
	/** The recebimento retira loja. */
	private RecebimentoRetiraLoja recebimentoRetiraLoja;
	
	/** The tipo estoque. */
	private TipoEstoque tipoEstoque;
	
	
	//transients
	private String numeroPedido;
	
	/**
	 * Gets the cd recebimento retira loja produto.
	 *
	 * @return the cd recebimento retira loja produto
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_recebretiralojaproduto")
	public Integer getCdRecebimentoRetiraLojaProduto() {
		return cdRecebimentoRetiraLojaProduto;
	}

	/**
	 * Sets the cd recebimento retira loja produto.
	 *
	 * @param cdRecebimentoRetiraLojaProduto the new cd recebimento retira loja produto
	 */
	public void setCdRecebimentoRetiraLojaProduto(Integer cdRecebimentoRetiraLojaProduto) {
		this.cdRecebimentoRetiraLojaProduto = cdRecebimentoRetiraLojaProduto;
	}
	
	/**
	 * Gets the produto.
	 *
	 * @return the produto
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdproduto")
	public Produto getProduto() {
		return produto;
	}

	/**
	 * Sets the produto.
	 *
	 * @param produto the new produto
	 */
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	/**
	 * Gets the qtde.
	 *
	 * @return the qtde
	 */
	public Integer getQtde() {
		return qtde;
	}

	/**
	 * Sets the qtde.
	 *
	 * @param qtde the new qtde
	 */
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	
	/**
	 * Gets the notafiscalsaida.
	 *
	 * @return the notafiscalsaida
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdnotafiscalsaida")
	public Notafiscalsaida getNotafiscalsaida() {
		return notafiscalsaida;
	}

	/**
	 * Sets the notafiscalsaida.
	 *
	 * @param notafiscalsaida the new notafiscalsaida
	 */
	public void setNotafiscalsaida(Notafiscalsaida notafiscalsaida) {
		this.notafiscalsaida = notafiscalsaida;
	}
	
	/**
	 * Gets the recebimento retira loja.
	 *
	 * @return the recebimento retira loja
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdrecebimentoretiraloja")
	public RecebimentoRetiraLoja getRecebimentoRetiraLoja() {
		return recebimentoRetiraLoja;
	}

	/**
	 * Sets the recebimento retira loja.
	 *
	 * @param recebimentoRetiraLoja the new recebimento retira loja
	 */
	public void setRecebimentoRetiraLoja(RecebimentoRetiraLoja recebimentoRetiraLoja) {
		this.recebimentoRetiraLoja = recebimentoRetiraLoja;
	}
	
	/**
	 * Gets the tipo estoque.
	 *
	 * @return the tipo estoque
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipoestoque")
	public TipoEstoque getTipoEstoque() {
		return tipoEstoque;
	}

	/**
	 * Sets the tipo estoque.
	 *
	 * @param tipoEstoque the new tipo estoque
	 */
	public void setTipoEstoque(TipoEstoque tipoEstoque) {
		this.tipoEstoque = tipoEstoque;
	}
	
	@Transient
	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	
}