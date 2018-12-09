package br.com.linkcom.wms.geral.bean;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@SequenceGenerator(name = "sq_manifestoentrega", sequenceName = "sq_manifestoentrega")
public class Manifestoentrega {

	private Integer cdmanifestoentrega;
	private Manifestonotafiscal manifestonotafiscal;
	private Notafiscalsaida notafiscalsaida;
	private Statusconfirmacaoentrega statusconfirmacaoentrega;
	private Cliente filialentrega;
	private Entregatipo entregatipo;
	private Integer ordem;
	private String observacao;
	private Date dataentrega;
	private Date dt_inclusao;
	private Date dt_alteracao;
	
	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_manifestoentrega")
	public Integer getCdmanifestoentrega() {
		return cdmanifestoentrega;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdmanifestonotafiscal")
	public Manifestonotafiscal getManifestonotafiscal() {
		return manifestonotafiscal;
	}
    @DisplayName("NF de Saída")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdnotafiscalsaida")
	public Notafiscalsaida getNotafiscalsaida() {
		return notafiscalsaida;
	}
    @DisplayName("Status da Entrega")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdstatusconfirmacaoentrega")
	public Statusconfirmacaoentrega getStatusconfirmacaoentrega() {
		return statusconfirmacaoentrega;
	}
    @DisplayName("Filial de Entrega")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdfilialentrega")
	public Cliente getFilialentrega() {
		return filialentrega;
	}
    @DisplayName("Tipo de Entrega")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdentregatipo")
	public Entregatipo getEntregatipo() {
		return entregatipo;
	}
	public Integer getOrdem() {
		return ordem;
	}
	@DisplayName("Observação")
	public String getObservacao() {
		return observacao;
	}
	@DisplayName("Dt. da entrega")
	public Date getDataentrega() {
		return dataentrega;
	}
	@DisplayName("Dt. de Inclusão")
	public Date getDt_inclusao() {
		return dt_inclusao;
	}
	@DisplayName("Dt. de Alteração")
	public Date getDt_alteracao() {
		return dt_alteracao;
	}
	
	//Set's
	public void setCdmanifestoentrega(Integer cdmanifestoentrega) {
		this.cdmanifestoentrega = cdmanifestoentrega;
	}
	public void setManifestonotafiscal(Manifestonotafiscal manifestonotafiscal) {
		this.manifestonotafiscal = manifestonotafiscal;
	}
	public void setNotafiscalsaida(Notafiscalsaida notafiscalsaida) {
		this.notafiscalsaida = notafiscalsaida;
	}
	public void setStatusconfirmacaoentrega(
			Statusconfirmacaoentrega statusconfirmacaoentrega) {
		this.statusconfirmacaoentrega = statusconfirmacaoentrega;
	}
	public void setFilialentrega(Cliente filialentrega) {
		this.filialentrega = filialentrega;
	}
	public void setEntregatipo(Entregatipo entregatipo) {
		this.entregatipo = entregatipo;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public void setDataentrega(Date dataentrega) {
		this.dataentrega = dataentrega;
	}
	public void setDt_inclusao(Date dtInclusao) {
		dt_inclusao = dtInclusao;
	}
	public void setDt_alteracao(Date dtAlteracao) {
		dt_alteracao = dtAlteracao;
	}
	
}
