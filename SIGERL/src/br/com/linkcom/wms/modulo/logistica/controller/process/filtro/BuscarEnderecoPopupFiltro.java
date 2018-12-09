package br.com.linkcom.wms.modulo.logistica.controller.process.filtro;

import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Produto;

public class BuscarEnderecoPopupFiltro extends FiltroListagem{
	
	protected Integer area;	
	protected Integer ruaI;
	protected Integer ruaF;
	protected Integer predioI;
	protected Integer PredioF;
	protected Integer nivelI;
	protected Integer nivelF;
	protected Integer aptoI;
	protected Integer aptoF;
	protected Produto produto;
	protected Deposito deposito;
	protected List<Endereco> listaEndereco;
	protected Integer index;
	
	
	public Integer getIndex() {
		return index;
	}


	@Required
	@MaxLength(2)	
	@DisplayName("Área")
	public Integer getArea() {
		return area;
	}
	
	
	@Required
	@MaxLength(3)
	@DisplayName("Rua inicial")
	public Integer getRuaI() {
		return ruaI;
	}
	
	@Required
	@MaxLength(3)
	@DisplayName("Rua final")
	public Integer getRuaF() {
		return ruaF;
	}
	
	@Required
	@MaxLength(3)
	@DisplayName("Prédio inicial")
	public Integer getPredioI() {
		return predioI;
	}
	
	@Required
	@MaxLength(3)
	@DisplayName("Prédio final")
	public Integer getPredioF() {
		return PredioF;
	}
	
	@Required
	@MaxLength(2)
	@DisplayName("Nível inicial")
	public Integer getNivelI() {
		return nivelI;
	}
	
	@Required
	@MaxLength(2)
	@DisplayName("Nível final")
	public Integer getNivelF() {
		return nivelF;
	}
	
	@Required
	@MaxLength(3)
	@DisplayName("Apto. inicial")
	public Integer getAptoI() {
		return aptoI;
	}
	
	@Required
	@MaxLength(3)
	@DisplayName("Apto. final")
	public Integer getAptoF() {
		return aptoF;
	}
	
	@Required
	@MaxLength(3)
	public Produto getProduto() {
		return produto;
	}
	
	@Required
	@MaxLength(3)
	public Deposito getDeposito() {
		return deposito;
	}
	
	@Required
	@MaxLength(3)
	public List<Endereco> getListaEndereco() {
		return listaEndereco;
	}
	
	@Required
	@MaxLength(3)
	public void setArea(Integer area) {
		this.area = area;
	}
			
	@Required
	@MaxLength(3)
	public void setRuaI(Integer ruaI) {
		this.ruaI = ruaI;
	}
	
	@Required
	@MaxLength(3)
	public void setRuaF(Integer ruaF) {
		this.ruaF = ruaF;
	}
	
	@Required
	@MaxLength(3)
	public void setPredioI(Integer predioI) {
		this.predioI = predioI;
	}
	
	@Required
	@MaxLength(3)
	public void setPredioF(Integer predioF) {
		PredioF = predioF;
	}
	
	@Required
	@MaxLength(3)
	public void setNivelI(Integer nivelI) {
		this.nivelI = nivelI;
	}
	
	@Required
	@MaxLength(3)
	public void setNivelF(Integer nivelF) {
		this.nivelF = nivelF;
	}
	
	@Required
	@MaxLength(3)
	public void setAptoI(Integer aptoI) {
		this.aptoI = aptoI;
	}
	
	@Required
	@MaxLength(3)
	public void setAptoF(Integer aptoF) {
		this.aptoF = aptoF;
	}
	
	@Required	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	@Required	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setListaEndereco(List<Endereco> listaEndereco) {
		this.listaEndereco = listaEndereco;
	}
	
	
	public void setIndex(Integer index) {
		this.index = index;
	}

	

}
