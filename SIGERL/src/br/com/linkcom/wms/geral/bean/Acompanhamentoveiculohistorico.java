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
import javax.persistence.Table;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name="sq_ravhistorico", sequenceName = "sq_ravhistorico")
@Table(name="ravhistorico")
public class Acompanhamentoveiculohistorico {
	
	//Mensagens pré-definidas de log para todo o fluxo.
	public static String LIBERACAO_VEICULO = "Veículo Liberado";
	public static String OCORREU_DEVOLUCAO = "O veículo possuí itens que serão devolvidos.";
	public static String CANCELAMENTO_FATURAMENTO = "O recebimento vinculado ao RAV foi cancelado.";

	private Integer cdravhistorico;
	private Timestamp dtaltera;
	private Usuario usuarioAltera;
	private String descricao;
	private Recebimento recebimento;
	private Acompanhamentoveiculo acompanhamentoveiculo;
	private Acompanhamentoveiculostatus acompanhamentoveiculostatus;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_ravhistorico")
	public Integer getCdravhistorico() {
		return cdravhistorico;
	}
	@Required
	@DisplayName("Data de Alteração")
	public Timestamp getDtaltera() {
		return dtaltera;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdusuarioaltera")
	@Required
	public Usuario getUsuarioAltera() {
		return usuarioAltera;
	}
	public String getDescricao() {
		return descricao;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdrecebimento")
	public Recebimento getRecebimento() {
		return recebimento;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdacompanhamentoveiculo")
	public Acompanhamentoveiculo getAcompanhamentoveiculo() {
		return acompanhamentoveiculo;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdstatusrav")	
	public Acompanhamentoveiculostatus getAcompanhamentoveiculostatus() {
		return acompanhamentoveiculostatus;
	}

	public void setCdravhistorico(Integer cdravhistorico) {
		this.cdravhistorico = cdravhistorico;
	}
	public void setDtaltera(Timestamp dtaltera) {
		this.dtaltera = dtaltera;
	}
	public void setUsuarioAltera(Usuario usuarioAltera) {
		this.usuarioAltera = usuarioAltera;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}
	public void setAcompanhamentoveiculo(Acompanhamentoveiculo acompanhamentoveiculo) {
		this.acompanhamentoveiculo = acompanhamentoveiculo;
	}
	public void setAcompanhamentoveiculostatus(Acompanhamentoveiculostatus acompanhamentoveiculostatus) {
		this.acompanhamentoveiculostatus = acompanhamentoveiculostatus;
	}
}
