package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Enderecoproduto;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Transferencia;
import br.com.linkcom.wms.geral.bean.Transferenciastatus;

public class TransferenciaFiltro extends FiltroListagem {
	protected Date dtinicial;
	protected Date dtfinal;
	protected Transferenciastatus transferenciastatus;
	protected Produto produto;
	
	protected Long codigoareaOrigem;
	protected Integer ruaOrigem;
	protected Integer predioOrigem;
	protected Integer nivelOrigem;
	protected Integer aptoOrigem;
	
	protected Long codigoareaDestino;
	protected Integer ruaDestino;
	protected Integer predioDestino;
	protected Integer nivelDestino;
	protected Integer aptoDestino;
	
	protected Long qtdeDestino;
	protected Set<Enderecoproduto> listaEnderecoproduto = new ListSet<Enderecoproduto>(Enderecoproduto.class);
	protected List<Transferencia> listaTransferencia; 
	protected Integer cdtransferencia;
	
	@MaxLength(9)
	@DisplayName("Transferência")
	public Integer getCdtransferencia() {
		return cdtransferencia;
	}

	public List<Transferencia> getListaTransferencia() {
		return listaTransferencia;
	}

	@DisplayName("DE")
	public Date getDtinicial() {
		return dtinicial;
	}
	
	@DisplayName("ATÉ")
	public Date getDtfinal() {
		return dtfinal;
	}
	
	@DisplayName("STATUS")
	public Transferenciastatus getTransferenciastatus() {
		return transferenciastatus;
	}
	
	@DisplayName("PRODUTO")
	public Produto getProduto() {
		return produto;
	}
	
	@DisplayName("ÁREA")
	@MaxLength(2)	
	public Long getCodigoareaOrigem() {
		return codigoareaOrigem;
	}
	
	@DisplayName("RUA")
	@MaxLength(3)
	public Integer getRuaOrigem() {
		return ruaOrigem;
	}
	
	@DisplayName("PRÉDIO")
	@MaxLength(3)
	public Integer getPredioOrigem() {
		return predioOrigem;
	}
	
	@DisplayName("NÍVEL")
	@MaxLength(2)
	public Integer getNivelOrigem() {
		return nivelOrigem;
	}
	
	@DisplayName("APTO.")
	@MaxLength(3)
	public Integer getAptoOrigem() {
		return aptoOrigem;
	}
	
	@DisplayName("ÁREA")
	@MaxLength(2)
	public Long getCodigoareaDestino() {
		return codigoareaDestino;
	}
	
	@DisplayName("RUA")
	@MaxLength(3)
	public Integer getRuaDestino() {
		return ruaDestino;
	}
	
	@DisplayName("PRÉDIO")
	@MaxLength(3)
	public Integer getPredioDestino() {
		return predioDestino;
	}
	
	@DisplayName("NÍVEL")
	@MaxLength(2)
	public Integer getNivelDestino() {
		return nivelDestino;
	}
	
	@DisplayName("APTO.")
	@MaxLength(3)
	public Integer getAptoDestino() {
		return aptoDestino;
	}
	
	public Long getQtdeDestino() {
		return qtdeDestino;
	}

	public Set<Enderecoproduto> getListaEnderecoproduto() {
		return listaEnderecoproduto;
	}
	
	public void setDtinicial(Date dtinicial) {
		this.dtinicial = dtinicial;
	}
	
	public void setDtfinal(Date dtfinal) {
		this.dtfinal = dtfinal;
	}
	
	public void setTransferenciastatus(Transferenciastatus transferenciastatus) {
		this.transferenciastatus = transferenciastatus;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public void setCodigoareaOrigem(Long codigoareaOrigem) {
		this.codigoareaOrigem = codigoareaOrigem;
	}
	
	public void setRuaOrigem(Integer ruaOrigem) {
		this.ruaOrigem = ruaOrigem;
	}
	
	public void setPredioOrigem(Integer predioOrigem) {
		this.predioOrigem = predioOrigem;
	}
	
	public void setNivelOrigem(Integer nivelOrigem) {
		this.nivelOrigem = nivelOrigem;
	}
	
	public void setAptoOrigem(Integer aptoOrigem) {
		this.aptoOrigem = aptoOrigem;
	}
	
	public void setCodigoareaDestino(Long codigoareaDestino) {
		this.codigoareaDestino = codigoareaDestino;
	}
		
	public void setRuaDestino(Integer ruaDestino) {
		this.ruaDestino = ruaDestino;
	}
		
	public void setPredioDestino(Integer predioDestino) {
		this.predioDestino = predioDestino;
	}
		
	public void setNivelDestino(Integer nivelDestino) {
		this.nivelDestino = nivelDestino;
	}
		
	public void setAptoDestino(Integer aptoDestino) {
		this.aptoDestino = aptoDestino;
	}
	
	public void setQtdeDestino(Long qtdeDestino) {
		this.qtdeDestino = qtdeDestino;
	}

	public void setListaEnderecoproduto(Set<Enderecoproduto> listaEnderecoproduto) {
		this.listaEnderecoproduto = listaEnderecoproduto;
	}
	
	public void setListaTransferencia(List<Transferencia> listaTransferencia) {
		this.listaTransferencia = listaTransferencia;
	}
	
	public void setCdtransferencia(Integer cdtransferencia) {
		this.cdtransferencia = cdtransferencia;
	}
}
