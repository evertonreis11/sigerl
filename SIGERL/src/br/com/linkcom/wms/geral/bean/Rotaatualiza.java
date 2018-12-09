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
import javax.persistence.Table;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@Table(name="tb_int_fila_tms_re_rota_upd")
@SequenceGenerator(name = "sq_fila_int_tms_re", sequenceName = "sq_fila_int_tms_re")
public class Rotaatualiza {
	
	private Rota rota;
	private Integer nro_seq_fila;	
	private Long cddeposito;
	private Integer cikey;
	private Date dt_inclusao;
	private Date dt_alteracao;
	private String mensagem;
	private String flag_status;
	
	public Rotaatualiza(){
	}
	
	public Rotaatualiza(Rota rota, Long cddeposito){
		this.rota = rota;
		this.cddeposito = cddeposito;
		this.flag_status = "U";
	}
	
	//Get's	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_fila_int_tms_re")
	public Integer getNro_seq_fila() {
		return nro_seq_fila;
	}
	
	@DisplayName("Rota")
	@JoinColumn(name="cdrota")
	@ManyToOne(fetch=FetchType.LAZY)
	public Rota getRota() {
		return rota;
	}
	public Long getCddeposito() {
		return cddeposito;
	}
	public Integer getCikey() {
		return cikey;
	}
	public Date getDt_inclusao() {
		return dt_inclusao;
	}
	public Date getDt_alteracao() {
		return dt_alteracao;
	}
	public String getMensagem() {
		return mensagem;
	}
	@MaxLength(1)
	public String getFlag_status() {
		return flag_status;
	}
	
	//Set's
	public void setNro_seq_fila(Integer nroSeqFila) {
		nro_seq_fila = nroSeqFila;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
	}
	public void setCikey(Integer cikey) {
		this.cikey = cikey;
	}
	public void setDt_inclusao(Date dtInclusao) {
		dt_inclusao = dtInclusao;
	}
	public void setDt_alteracao(Date dtAlteracao) {
		dt_alteracao = dtAlteracao;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public void setFlag_status(String flagStatus) {
		flag_status = flagStatus;
	}
	public void setCddeposito(Long cddeposito) {
		this.cddeposito = cddeposito;
	}
	
}