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
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name="sq_produtotipoestrutura",sequenceName="sq_produtotipoestrutura")
public class Produtotipoestrutura {
		
	protected Integer cdprodutotipoestrutura;
	protected Produto produto;
	protected Deposito deposito;
	protected Integer ordem;
	protected Tipoestrutura tipoestrutura;
	protected Long restricaonivel;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(generator = "sq_produtotipoestrutura", strategy = GenerationType.AUTO)
	public Integer getCdprodutotipoestrutura() {
		return cdprodutotipoestrutura;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdproduto")
	public Produto getProduto() {
		return produto;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")
	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	public Integer getOrdem() {
		return ordem;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipoestrutura")
	@DisplayName("Estrutura")
	@Required
	public Tipoestrutura getTipoestrutura() {
		return tipoestrutura;
	}
	
	@DisplayName("Restrição de nível")
	@MaxLength(2)
	@Required
	public Long getRestricaonivel() {
		return restricaonivel;
	}
	
	public void setCdprodutotipoestrutura(Integer cdprodutotipoestrutura) {
		this.cdprodutotipoestrutura = cdprodutotipoestrutura;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	public void setTipoestrutura(Tipoestrutura tipoestrutura) {
		this.tipoestrutura = tipoestrutura;
	}
	public void setRestricaonivel(Long restricaonivel) {
		this.restricaonivel = restricaonivel;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Produtotipoestrutura)
			if(obj != null)
				if(((Produtotipoestrutura)obj).getCdprodutotipoestrutura().equals(this.cdprodutotipoestrutura))
					return this.cdprodutotipoestrutura.equals(((Produtotipoestrutura)obj).getCdprodutotipoestrutura());
		return super.equals(obj);
	}
	
}
