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

@Entity
@SequenceGenerator(name = "sq_tabelafretehistorico", sequenceName = "sq_tabelafretehistorico")
@DefaultOrderBy("dtalteracao")
public class Tabelafretehistorico {

	private Integer cdtabelafretehistorico;
	private Tabelafrete tabelafrete;
	private Usuario usuario;
	private Timestamp dtalteracao;
	private String motivo;
	
	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_tabelafretehistorico")
	public Integer getCdtabelafretehistorico() {
		return cdtabelafretehistorico;
	}
    @DisplayName("Tabela de Frete")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtabelafrete")	
	public Tabelafrete getTabelafrete() {
		return tabelafrete;
	}
	@DisplayName("Usuário")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdusuario")    
	public Usuario getUsuario() {
		return usuario;
	}
    @DisplayName("Dt. de Alteração")
	public Timestamp getDtalteracao() {
		return dtalteracao;
	}
    @DisplayName("Motivo")
	public String getMotivo() {
		return motivo;
	}
	
	
	//Set's
    public void setCdtabelafretehistorico(Integer cdtabelafretehistorico) {
    	this.cdtabelafretehistorico = cdtabelafretehistorico;
    }
	public void setTabelafrete(Tabelafrete tabelafrete) {
		this.tabelafrete = tabelafrete;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public void setDtalteracao(Timestamp dtalteracao) {
		this.dtalteracao = dtalteracao;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
}
