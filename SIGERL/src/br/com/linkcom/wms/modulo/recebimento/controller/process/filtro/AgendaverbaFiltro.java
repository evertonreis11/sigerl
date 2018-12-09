package br.com.linkcom.wms.modulo.recebimento.controller.process.filtro;

import java.util.Date;
import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.MaxValue;
import br.com.linkcom.wms.geral.bean.Agendaverba;
import br.com.linkcom.wms.geral.bean.Deposito;

public class AgendaverbaFiltro {

	private Deposito deposito;
	private Date exercicio;
	private List<Agendaverba> listaAgendaverba;
	private Integer semestre;
	private Integer ano;

	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}

	@DisplayName("Exercício")
	public Date getExercicio() {
		return exercicio;
	}

	public List<Agendaverba> getListaAgendaverba() {
		return listaAgendaverba;
	}
	
	public Integer getSemestre() {
		return semestre;
	}
	
	@MaxLength(4)
	@MaxValue(9999)
	public Integer getAno() {
		return ano;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setExercicio(Date exercicio) {
		this.exercicio = exercicio;
	}

	public void setListaAgendaverba(List<Agendaverba> listaAgendaverba) {
		this.listaAgendaverba = listaAgendaverba;
	}

	public void setSemestre(Integer semestre) {
		this.semestre = semestre;
	}
	
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
}
