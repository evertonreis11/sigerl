package br.com.linkcom.wms.modulo.recebimento.controller.report.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.wms.geral.bean.Recebimento;

/**
 * 
 * @author Pedro Gonçalves
 */
public class DadosFaltantesFiltro {
	protected Recebimento recebimento;
	
	@DisplayName("Recebimento")
	public Recebimento getRecebimento() {
		return recebimento;
	}
	
	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}
}
