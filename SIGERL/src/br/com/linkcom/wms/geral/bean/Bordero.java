package br.com.linkcom.wms.geral.bean;

import java.sql.Date;
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

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.util.WmsUtil;

@Entity
@SequenceGenerator(name = "sq_bordero", sequenceName = "sq_bordero")
public class Bordero{

    private Integer cdbordero;
    private Usuario usuariocriacao;
    private Transportador transportador;
    private Money valorcalculado;
    private Money valoracrescimo;
    private Money valortotal;
    private Date periodoinicial;
    private Date periodofinal;
    private Tipoentrega tipoentrega;
    private Integer qtdentregas;
    private Date dtbordero;
    private List<Manifestobordero> listaManifestobordero;
    private Deposito deposito;
    private Empresa empresa;    
    
    public Bordero(){
    }
    
    public Bordero(Usuario usuariocriacao, Transportador transportador, Money valortotal, Date periodoinicial, Date periodofinal, Tipoentrega tipoentrega, Integer qtdentregas, Deposito depostio){
    	this.usuariocriacao = WmsUtil.getUsuarioLogado();
    	this.transportador = transportador;
    	this.usuariocriacao = usuariocriacao;
    	this.valortotal = valortotal;
    	this.periodoinicial = periodoinicial;
    	this.periodofinal = periodofinal;
    	this.tipoentrega = tipoentrega;
    	this.qtdentregas = qtdentregas;
    	this.dtbordero = WmsUtil.currentDate();
    	this.deposito = depostio;
    	this.empresa = WmsUtil.getEmpresa();
    }
    
    //Get's
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_bordero")
	public Integer getCdbordero() {
		return cdbordero;
	}
    @DisplayName("Usuário Responável")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdusuariocriacao")
	public Usuario getUsuariocriacao() {
		return usuariocriacao;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtransportador")
	public Transportador getTransportador() {
		return transportador;
	}
	@DisplayName("Valor")
	public Money getValorcalculado() {
		return valorcalculado;
	}
	@DisplayName("Acréscimo")
	public Money getValoracrescimo() {
		return valoracrescimo;
	}
	@DisplayName("Total a Pagar")
	public Money getValortotal() {
		return valortotal;
	}
	@OneToMany(mappedBy="bordero")	
	public List<Manifestobordero> getListaManifestobordero() {
		return listaManifestobordero;
	}
	public Date getPeriodoinicial() {
		return periodoinicial;
	}
	public Date getPeriodofinal() {
		return periodofinal;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipoentrega")	
	public Tipoentrega getTipoentrega() {
		return tipoentrega;
	}
	public Integer getQtdentregas() {
		return qtdentregas;
	}
	public Date getDtbordero() {
		return dtbordero;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")	
	public Deposito getDeposito() {
		return deposito;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdempresa")	
	public Empresa getEmpresa() {
		return empresa;
	}


	//Set's
	public void setCdbordero(Integer cdbordero) {
		this.cdbordero = cdbordero;
	}
	public void setUsuariocriacao(Usuario usuariocriacao) {
		this.usuariocriacao = usuariocriacao;
	}
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
	public void setValorcalculado(Money valorcalculado) {
		this.valorcalculado = valorcalculado;
	}
	public void setValoracrescimo(Money valoracrescimo) {
		this.valoracrescimo = valoracrescimo;
	}
	public void setValortotal(Money valortotal) {
		this.valortotal = valortotal;
	}
	public void setListaManifestobordero(List<Manifestobordero> listaManifestobordero) {
		this.listaManifestobordero = listaManifestobordero;
	}
	public void setPeriodoinicial(Date periodoinicial) {
		this.periodoinicial = periodoinicial;
	}
	public void setPeriodofinal(Date periodofinal) {
		this.periodofinal = periodofinal;
	}
	public void setTipoentrega(Tipoentrega tipoentrega) {
		this.tipoentrega = tipoentrega;
	}
	public void setQtdentregas(Integer qtdentregas) {
		this.qtdentregas = qtdentregas;
	}
	public void setDtbordero(Date dtbordero) {
		this.dtbordero = dtbordero;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
}