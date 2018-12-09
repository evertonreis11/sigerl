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
@SequenceGenerator(name = "sq_carregamentohistorico", sequenceName = "sq_carregamentohistorico")
@DisplayName("Hist�rico do Carregamento")
@DefaultOrderBy("dtaltera")
public class Carregamentohistorico {

	public static String CRIA_CARGA = "Carga Criada.";
	public static String EDITA_CARGA = "Carga Editada.";
	public static String FINALIZA_EXPEDICAO = "Expedi��o Finalizada.";
	public static String CANCELA_EXPEDICAO = "Expedi��o Cancelada.";
	public static String CRIA_EXPEDICAO = "Expedi��o Criada.";
	public static String CONFERENCIA_FINALIZADA  = "Confer�ncia Finalizada. OS: ";
	public static String CONFERENCIA_FINALIZADA_CHECKOUT  = "Confer�ncia de Checkout Finalizada. OS: ";
	
	private Integer cdcarregamentohistorico;
	private Timestamp dtaltera;
	private Usuario usuarioaltera;
	private String descricao;
	private Carregamentostatus carregamentostatus;
	private Carregamento carregamento;
	private Carregamentohistoricotipo carregamentohistoricotipo;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_carregamentohistorico")
	public Integer getCdcarregamentohistorico() {
		return cdcarregamentohistorico;
	}
	@DisplayName("Data de Altera��o")
	public Timestamp getDtaltera() {
		return dtaltera;
	}
	@DisplayName("Respons�vel")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdusuarioaltera")
	public Usuario getUsuarioaltera() {
		return usuarioaltera;
	}
	@MaxLength(255)
	@DisplayName("Descri��o")
	public String getDescricao() {
		return descricao;
	}
	@DisplayName("Status")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdcarregamentostatus")
	public Carregamentostatus getCarregamentostatus() {
		return carregamentostatus;
	}
	@DisplayName("Carregamento")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdcarregamento")
	public Carregamento getCarregamento() {
		return carregamento;
	}
	@DisplayName("Tipo")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdcarregamentohistoricotipo")
	public Carregamentohistoricotipo getCarregamentohistoricotipo() {
		return carregamentohistoricotipo;
	}
	
	
	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}
	public void setCarregamentostatus(Carregamentostatus carregamentostatus) {
		this.carregamentostatus = carregamentostatus;
	}
	public void setCdcarregamentohistorico(Integer cdcarregamentohistorico) {
		this.cdcarregamentohistorico = cdcarregamentohistorico;
	}
	public void setDtaltera(Timestamp dtaltera) {
		this.dtaltera = dtaltera;
	}
	public void setUsuarioaltera(Usuario usuarioaltera) {
		this.usuarioaltera = usuarioaltera;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setCarregamentohistoricotipo(Carregamentohistoricotipo carregamentohistoricotipo) {
		this.carregamentohistoricotipo = carregamentohistoricotipo;
	}
	
}