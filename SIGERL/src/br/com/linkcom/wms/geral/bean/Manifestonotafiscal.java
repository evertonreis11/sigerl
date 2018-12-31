package br.com.linkcom.wms.geral.bean;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Money;

@Entity
@SequenceGenerator(name = "sq_manifestonotafiscal", sequenceName = "sq_manifestonotafiscal")
public class Manifestonotafiscal {
	
    private Integer cdmanifestonotafiscal;
    private Manifesto manifesto;
    private Notafiscalsaida notafiscalsaida; 
    private Usuario usuario;
    private Date dt_inclusao;
    private Date dt_alteracao;
    private String observacao;
    private Boolean ind_avulso;
    private Praca praca;
    private Money valorentrega;
    private List<Manifestoentrega> listaManifestoentrega;
    private Statusconfirmacaoentrega statusconfirmacaoentrega;
    private Timestamp dataentrega;
    private Motivoretornoentrega motivoretornoentrega;
    private Boolean temDepositoTransbordo;
    private Deposito depositotransbordo;
    private Money valorprevisao;
    private Integer token;
    
    //Transient's
    private Boolean existeFreteClienteNota = Boolean.TRUE;
    private Integer senhaAutorizacao;
    private Integer cdnotafiscalsaidareferencia = null;
    private Boolean temFretePago = Boolean.FALSE;
    private List<Statusconfirmacaoentrega> statusForCombo;
    
    
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_manifestonotafiscal")
	public Integer getCdmanifestonotafiscal() {
		return cdmanifestonotafiscal;
	}
    @DisplayName("Manifesto")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdmanifesto")
	public Manifesto getManifesto() {
		return manifesto;
	}
    @DisplayName("Nota Fiscal Saída")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdnotafiscalsaida")    
	public Notafiscalsaida getNotafiscalsaida() {
		return notafiscalsaida;
	}
    @DisplayName("Usuário")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdusuario")    
	public Usuario getUsuario() {
		return usuario;
	}
    @DisplayName("Praça")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cdpraca")	
    public Praca getPraca() {
    	return praca;
    }
    @DisplayName("Dt. de Inclusão")
	public Date getDt_inclusao() {
		return dt_inclusao;
	}
    @DisplayName("Dt. de Alteração")
	public Date getDt_alteracao() {
		return dt_alteracao;
	}
    @DisplayName("Observação")
	public String getObservacao() {
		return observacao;
	}
    @DisplayName("Avulso")
	public Boolean getInd_avulso() {
		return ind_avulso;
	}
    @DisplayName("Valor da Entrega")
	public Money getValorentrega() {
		return valorentrega;
	}
    @OneToMany(mappedBy="manifestonotafiscal")
	public List<Manifestoentrega> getListaManifestoentrega() {
		return listaManifestoentrega;
	}
    @DisplayName("Status da Entrega")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdstatusconfirmacaoentrega")    
	public Statusconfirmacaoentrega getStatusconfirmacaoentrega() {
		return statusconfirmacaoentrega;
	}
	public Timestamp getDataentrega() {
		return dataentrega;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdmotivoretorno")
	public Motivoretornoentrega getMotivoretornoentrega() {
		return motivoretornoentrega;
	}
	public Boolean getTemDepositoTransbordo() {
		return temDepositoTransbordo;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddepositotransbordo")
	public Deposito getDepositotransbordo() {
		return depositotransbordo;
	}
	public Money getValorprevisao() {
		return valorprevisao;
	}
	public Integer getToken() {
		return token;
	}
	
	//Set's
	public void setCdmanifestonotafiscal(Integer cdmanifestonotafiscal) {
		this.cdmanifestonotafiscal = cdmanifestonotafiscal;
	}
	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}
	public void setNotafiscalsaida(Notafiscalsaida notafiscalsaida) {
		this.notafiscalsaida = notafiscalsaida;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public void setDt_inclusao(Date dtInclusao) {
		dt_inclusao = dtInclusao;
	}
	public void setDt_alteracao(Date dtAlteracao) {
		dt_alteracao = dtAlteracao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public void setInd_avulso(Boolean indAvulso) {
		ind_avulso = indAvulso;
	}
	public void setPraca(Praca praca) {
		this.praca = praca;
	}
	public void setValorentrega(Money valorentrega) {
		this.valorentrega = valorentrega;
	}
	public void setListaManifestoentrega(List<Manifestoentrega> listaManifestoentrega) {
		this.listaManifestoentrega = listaManifestoentrega;
	}
	public void setStatusconfirmacaoentrega(Statusconfirmacaoentrega statusconfirmacaoentrega) {
		this.statusconfirmacaoentrega = statusconfirmacaoentrega;
	}
	public void setDataentrega(Timestamp dataentrega) {
		this.dataentrega = dataentrega;
	}
	public void setMotivoretornoentrega(Motivoretornoentrega motivoretornoentrega) {
		this.motivoretornoentrega = motivoretornoentrega;
	}
	public void setTemDepositoTransbordo(Boolean temDepositoTransbordo) {
		this.temDepositoTransbordo = temDepositoTransbordo;
	}
	public void setDepositotransbordo(Deposito depositotransbordo) {
		this.depositotransbordo = depositotransbordo;
	}
	public void setValorprevisao(Money valorprevisao) {
		this.valorprevisao = valorprevisao;
	}
	public void setToken(Integer token) {
		this.token = token;
	}
	
	@Transient
	public Boolean getExisteFreteClienteNota() {
		return existeFreteClienteNota;
	}
	@Transient
	public Integer getSenhaAutorizacao() {
		return senhaAutorizacao;
	}
	public void setSenhaAutorizacao(Integer senhaAutorizacao) {
		this.senhaAutorizacao = senhaAutorizacao;
	}
	public void setExisteFreteClienteNota(Boolean existeFreteClienteNota) {
		this.existeFreteClienteNota = existeFreteClienteNota;
	}
	
	
	@Transient
	public Integer getCdnotafiscalsaidareferencia() {
		return cdnotafiscalsaidareferencia;
	}
	public void setCdnotafiscalsaidareferencia(Integer cdnotafiscalsaidareferencia) {
		this.cdnotafiscalsaidareferencia = cdnotafiscalsaidareferencia;
	}
	@Transient
	public Boolean getTemFretePago() {
		return temFretePago;
	}
	public void setTemFretePago(Boolean temFretePago) {
		this.temFretePago = temFretePago;
	}
	@Transient
	public List<Statusconfirmacaoentrega> getStatusForCombo() {
		return statusForCombo;
	}
	public void setStatusForCombo(List<Statusconfirmacaoentrega> statusForCombo) {
		this.statusForCombo = statusForCombo;
	}
	
}
