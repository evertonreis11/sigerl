package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import br.com.linkcom.wms.geral.bean.auxiliar.IOrdemexpedicaobox;

@Entity
@Immutable
@Table(name="VORDEMEXPEDICAO_BOX")
public class Vordemexpedicaobox implements IOrdemexpedicaobox {

	
	private String chave;
	private Box box;
	private Cliente cliente;
	private Carregamento carregamento;
	private Tipooperacao tipooperacao;
	private Deposito deposito;
	private Produto produtoprincipal;
	private Produto volume;
	private Carregamentoitem carregamentoitem;
	private Linhaseparacao linhaseparacao;
	private Long qtde;
	
	@Id
	public String getChave() {
		return chave;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDBOX")
	public Box getBox() {
		return box;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDCLIENTE")
	public Cliente getCliente() {
		return cliente;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDCARREGAMENTO")
	public Carregamento getCarregamento() {
		return carregamento;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDTIPOOPERACAO")
	public Tipooperacao getTipooperacao() {
		return tipooperacao;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDDEPOSITO")
	public Deposito getDeposito() {
		return deposito;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDPRODUTO")
	public Produto getProdutoprincipal() {
		return produtoprincipal;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDVOLUME")
	public Produto getVolume() {
		return volume;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDCARREGAMENTOITEM")
	public Carregamentoitem getCarregamentoitem() {
		return carregamentoitem;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CDLINHASEPARACAO")
	public Linhaseparacao getLinhaseparacao() {
		return linhaseparacao;
	}

	public Long getQtde() {
		return qtde;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}

	public void setTipooperacao(Tipooperacao tipooperacao) {
		this.tipooperacao = tipooperacao;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setProdutoprincipal(Produto produtoprincipal) {
		this.produtoprincipal = produtoprincipal;
	}

	public void setVolume(Produto volume) {
		this.volume = volume;
	}

	public void setCarregamentoitem(Carregamentoitem carregamentoitem) {
		this.carregamentoitem = carregamentoitem;
	}
	
	public void setLinhaseparacao(Linhaseparacao linhaseparacao) {
		this.linhaseparacao = linhaseparacao;
	}

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}

}
