package br.com.linkcom.wms.geral.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name ="PONTODECONTROLE")
@SequenceGenerator(name="sq_pontodecontrole", sequenceName="sq_pontodecontrole")
public class PontoControle {
	
	private Integer cdPontoControle;
	
    private String descricao;
    
	private String sigla;
	
	private String codigoErp;
	
	private Boolean utilizadoEmManutencao;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pontodecontrole")
	@Column(name="CDPONTODECONTROLE")
	public Integer getCdPontoControle() {
		return cdPontoControle;
	}

	public void setCdPontoControle(Integer cdPontoControle) {
		this.cdPontoControle = cdPontoControle;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getCodigoErp() {
		return codigoErp;
	}

	public void setCodigoErp(String codigoErp) {
		this.codigoErp = codigoErp;
	}

	public Boolean getUtilizadoEmManutencao() {
		return utilizadoEmManutencao;
	}

	public void setUtilizadoEmManutencao(Boolean utilizadoEmManutencao) {
		this.utilizadoEmManutencao = utilizadoEmManutencao;
	}
}
