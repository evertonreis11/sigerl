package br.com.linkcom.wms.modulo.recebimento.controller.process.filtro;

import java.sql.Date;
import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Hora;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoendereco;
import br.com.linkcom.wms.geral.bean.Usuario;

public class EditarEnderecamentoFiltro {

	private Ordemservico ordemservico;
	private Hora horaInicial;
	private Hora horaFinal;
	private Usuario operador;
	private Date data;
	private List<Ordemservicoprodutoendereco> listaOSPE;

	public List<Ordemservicoprodutoendereco> getListaOSPE() {
		return listaOSPE;
	}

	@DisplayName("Ordem de serviço")
	public Ordemservico getOrdemservico() {
		return ordemservico;
	}

	public void setListaOSPE(List<Ordemservicoprodutoendereco> listaOSPE) {
		this.listaOSPE = listaOSPE;
	}

	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}

	@DisplayName("Hora Inicial")
	public Hora getHoraInicial() {
		return horaInicial;
	}

	public void setHoraInicial(Hora horaInicial) {
		this.horaInicial = horaInicial;
	}

	@DisplayName("Hora Final")
	public Hora getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(Hora horaFinal) {
		this.horaFinal = horaFinal;
	}

	@Required
	public Usuario getOperador() {
		return operador;
	}

	public void setOperador(Usuario operador) {
		this.operador = operador;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

}
