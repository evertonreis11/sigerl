package br.com.linkcom.wms.geral.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.util.DateUtil;

@Entity
@Table(name="tb_int_fila_tms_re_rotacorte")
@SequenceGenerator(name = "sq_fila_int_tms_re", sequenceName = "sq_fila_int_tms_re")
public class Rotacorte {
	
	private Integer nro_seq_fila;
	private Integer cdrota;
	private Long cddeposito;
	private Integer cikey;
	private Date dt_inclusao;
	private Date dt_alteracao;
	private String mensagem;
	private String flag_status;
	
	public Rotacorte(){
		
	}
	
	public Rotacorte(Integer cdrota, Long cddeposito){
		this.cdrota = cdrota;
		this.cddeposito = cddeposito;
		this.dt_inclusao = DateUtil.limpaMinSegHora(new Date(System.currentTimeMillis()));
		this.flag_status = "U";
	}
	
	//Get's	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_fila_int_tms_re")
	public Integer getNro_seq_fila() {
		return nro_seq_fila;
	}
	public Integer getCdrota() {
		return cdrota;
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
	public void setCdrota(Integer cdrota) {
		this.cdrota = cdrota;
	}
	public void setCddeposito(Long cddeposito) {
		this.cddeposito = cddeposito;
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
	
}