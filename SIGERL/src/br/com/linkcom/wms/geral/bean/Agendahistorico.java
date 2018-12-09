package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@DefaultOrderBy("dtalteracao")
@SequenceGenerator(name = "sq_agendahistorico", sequenceName = "sq_agendahistorico")
@DisplayName("Histórico do agendamento")
public class Agendahistorico {

	private Integer cdagendahistorico;
	private Timestamp dtalteracao;
	private String descricao;
	private Usuario usuarioaltera;
	private Agenda agenda;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_agendahistorico")
	public Integer getCdagendahistorico() {
		return cdagendahistorico;
	}

	@DisplayName("Data da alteração")
	public Timestamp getDtalteracao() {
		return dtalteracao;
	}

	@MaxLength(255)
	@DisplayName("Descrição")
	public String getDescricao() {
		return descricao;
	}

	@DisplayName("Responsável")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdusuarioaltera")
	public Usuario getUsuarioaltera() {
		return usuarioaltera;
	}

	@DisplayName("Agendamento")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdagenda")
	public Agenda getAgenda() {
		return agenda;
	}

	public void setCdagendahistorico(Integer cdagendahistorico) {
		this.cdagendahistorico = cdagendahistorico;
	}

	public void setDtalteracao(Timestamp dtalteracao) {
		this.dtalteracao = dtalteracao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setUsuarioaltera(Usuario usuarioaltera) {
		this.usuarioaltera = usuarioaltera;
	}

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}

}
