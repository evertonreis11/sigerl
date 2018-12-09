package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Money;

@Entity
@SequenceGenerator(name = "sq_notafiscalsaidaproduto", sequenceName = "sq_notafiscalsaidaproduto")
public class Notafiscalsaidaproduto {
	
    private Integer cdnotafiscalsaidaproduto;
    private Notafiscalsaida notafiscalsaida;
    private Produto produto;
    private Pedidovendaproduto pedidovendaproduto;
    private Long qtde;
    private Money vlrunitario;
    private Long seqitemerp;
    private Pedidovenda pedidovenda;
    
    
    public Notafiscalsaidaproduto() {
    	
    }
    public Notafiscalsaidaproduto(Integer cdnotafiscalsaidaproduto) {
    	this.cdnotafiscalsaidaproduto = cdnotafiscalsaidaproduto;
	}
    
    //Get's
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_notafiscalsaidaproduto")
	public Integer getCdnotafiscalsaidaproduto() {
		return cdnotafiscalsaidaproduto;
	}
    @DisplayName("Nota fiscal")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdnotafiscalsaida")
	public Notafiscalsaida getNotafiscalsaida() {
		return notafiscalsaida;
	}
    @DisplayName("Produto")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdproduto")    
	public Produto getProduto() {
		return produto;
	}
    @DisplayName("Núm. Manifesto")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpedidovendaproduto")    
	public Pedidovendaproduto getPedidovendaproduto() {
		return pedidovendaproduto;
	}
	public Long getQtde() {
		return qtde;
	}
    @DisplayName("Valor unitário")
	public Money getVlrunitario() {
		return vlrunitario;
	}
    @DisplayName("Código Item (ERP)")
	public Long getSeqitemerp() {
		return seqitemerp;
	}
    @DisplayName("Pedido de Venda")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpedidovenda")    
	public Pedidovenda getPedidovenda() {
		return pedidovenda;
	}
	
	
	//Set's
	public void setCdnotafiscalsaidaproduto(Integer cdnotafiscalsaidaproduto) {
		this.cdnotafiscalsaidaproduto = cdnotafiscalsaidaproduto;
	}
	public void setNotafiscalsaida(Notafiscalsaida notafiscalsaida) {
		this.notafiscalsaida = notafiscalsaida;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public void setPedidovendaproduto(Pedidovendaproduto pedidovendaproduto) {
		this.pedidovendaproduto = pedidovendaproduto;
	}
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	public void setVlrunitario(Money vlrunitario) {
		this.vlrunitario = vlrunitario;
	}
	public void setSeqitemerp(Long seqitemerp) {
		this.seqitemerp = seqitemerp;
	}
	public void setPedidovenda(Pedidovenda pedidovenda) {
		this.pedidovenda = pedidovenda;
	}
	
}
