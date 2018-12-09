package br.com.linkcom.wms.geral.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.util.WmsUtil;

@Entity
@SequenceGenerator(name = "sq_rotaturnoextra", sequenceName = "sq_rotaturnoextra")
public class Rotaturnoextra {
	
	private Integer cdrotaturnoextra;
	private Turnodeentrega turnodeentrega;
	private Rota rota;
	private Integer maximoentregas;
	private Integer maximovalor;
	private Integer maximacubagem;
	private Integer maximopeso;
	private Date dtvalidadeinicio;
	private Date dtvalidadefim;
	
	public Rotaturnoextra(){
		this.dtvalidadeinicio = WmsUtil.currentDate();
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_rotaturnoextra")
	public Integer getCdrotaturnoextra() {
		return cdrotaturnoextra;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdturnodeentrega")
	public Turnodeentrega getTurnodeentrega() {
		return turnodeentrega;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdrota")
	public Rota getRota() {
		return rota;
	}
	public Integer getMaximoentregas() {
		return maximoentregas;
	}
	public Integer getMaximovalor() {
		return maximovalor;
	}
	public Integer getMaximacubagem() {
		return maximacubagem;
	}
	public Integer getMaximopeso() {
		return maximopeso;
	}
	@Required
	public Date getDtvalidadeinicio() {
		return dtvalidadeinicio;
	}
	@Required
	public Date getDtvalidadefim() {
		return dtvalidadefim;
	}
	
	public void setCdrotaturnoextra(Integer cdrotaturnoextra) {
		this.cdrotaturnoextra = cdrotaturnoextra;
	}
	public void setTurnodeentrega(Turnodeentrega turnodeentrega) {
		this.turnodeentrega = turnodeentrega;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
	}
	public void setMaximoentregas(Integer maximoentregas) {
		this.maximoentregas = maximoentregas;
	}
	public void setMaximovalor(Integer maximovalor) {
		this.maximovalor = maximovalor;
	}
	public void setMaximacubagem(Integer maximacubagem) {
		this.maximacubagem = maximacubagem;
	}
	public void setMaximopeso(Integer maximopeso) {
		this.maximopeso = maximopeso;
	}
	public void setDtvalidadeinicio(Date dtvalidadeinicio) {
		this.dtvalidadeinicio = dtvalidadeinicio;
	}
	public void setDtvalidadefim(Date dtvalidadefim) {
		this.dtvalidadefim = dtvalidadefim;
	}
}
