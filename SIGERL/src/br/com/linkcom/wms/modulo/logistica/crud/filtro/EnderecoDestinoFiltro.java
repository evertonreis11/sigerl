package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Area;
import br.com.linkcom.wms.geral.bean.Deposito;

public class EnderecoDestinoFiltro extends EnderecoFiltro {
	
	protected Deposito deposito;
	
	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	// Metodos sobrescritos
	@DisplayName("Área de armazenagem")
	public Area getArea() {
		return area;
	}
	
	@DisplayName("inicial")
	@MaxLength(3)
	@Required
	public Integer getRuaI() {
		return ruaI;
	}
	
	@DisplayName("final")
	@MaxLength(3)
	@Required
	public Integer getRuaF() {
		return ruaF;
	}
	
	@DisplayName("inicial")
	@MaxLength(3)
	@Required
	public Integer getPredioI() {
		return predioI;
	}
	
	@DisplayName("final")
	@MaxLength(3)
	@Required
	public Integer getPredioF() {
		return predioF;
	}
	
	@DisplayName("inicial")
	@MaxLength(2)
	@Required
	public Integer getNivelI() {
		return nivelI;
	}
	
	@DisplayName("final")
	@MaxLength(2)
	@Required
	public Integer getNivelF() {
		return nivelF;
	}
	
	@DisplayName("inicial")
	@MaxLength(3)
	@Required
	public Integer getAptoI() {
		return aptoI;
	}
	
	@DisplayName("final")
	@MaxLength(3)
	@Required
	public Integer getAptoF() {
		return aptoF;
	}
}
