package br.com.linkcom.wms.geral.bean;

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
@SequenceGenerator(name = "sq_manifestoentregahistorico", sequenceName = "sq_manifestoentregahistorico")
public class Manifestoentregahistorico {

	private Integer cdmanifestoentregahistorico;
	private Manifestonotafiscal manifestonotafiscal;
	private Motorista motorista;
	private String historico;
	
	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_manifestoentregahistorico")
	public Integer getCdmanifestoentregahistorico() {
		return cdmanifestoentregahistorico;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdmanifestonotafiscal")
	public Manifestonotafiscal getManifestonotafiscal() {
		return manifestonotafiscal;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpessoa")
	public Motorista getMotorista() {
		return motorista;
	}
	@DisplayName("Histórico")
	public String getHistorico() {
		return historico;
	}
	
	
	//Set's
	public void setCdmanifestoentregahistorico(Integer cdmanifestoentregahistorico) {
		this.cdmanifestoentregahistorico = cdmanifestoentregahistorico;
	}
	public void setManifestonotafiscal(Manifestonotafiscal manifestonotafiscal) {
		this.manifestonotafiscal = manifestonotafiscal;
	}
	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}
	public void setHistorico(String historico) {
		this.historico = historico;
	}
	
}
