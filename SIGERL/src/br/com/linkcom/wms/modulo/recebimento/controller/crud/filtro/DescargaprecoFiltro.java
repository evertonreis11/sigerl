package br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Descargatipocalculo;

/**
 * 
 * @author Guilherme Arantes de Oliveira
 *
 */

public class DescargaprecoFiltro extends FiltroListagem {
	protected String nome;
	protected Descargatipocalculo descargatipocalculo;
	
	@MaxLength(30)
	@DisplayName("Nome")
	public String getNome() {
		return nome;
	}
	
	@DisplayName("Tipo de cálculo")
	public Descargatipocalculo getDescargatipocalculo() {
		return descargatipocalculo;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setDescargatipocalculo(Descargatipocalculo descargatipocalculo) {
		this.descargatipocalculo = descargatipocalculo;
	}
}