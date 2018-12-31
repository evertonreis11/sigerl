package br.com.linkcom.wms.geral.bean.vo;



import java.util.Date;

import org.apache.commons.lang.StringUtils;

import br.com.linkcom.wms.geral.bean.Deposito;

public class ManifestoTransbordoVO {
	private Integer cdManifestoNotaFiscal; 
	private String numero;
	private String serie; 
	private Date dtEmissao;
	private String numeroPedido;
	private String lojaPedido;
	private String rota;
	private Boolean temDepositoTransbordoManifesto;
	private Boolean temDepositoTransbordoRota;
	private Integer cddepositoTransbordoManifesto; 
	private Integer cddepositoTransbordoRota; 
	private Deposito depositoTransbordo;
	
	
	
	
	public ManifestoTransbordoVO(Integer cdManifestoNotaFiscal, String numero, String serie, Date dtEmissao,
			String numeroPedido, String lojaPedido, String rota, String rotaConsolidacao, Boolean temDepositoTransbordoManifesto,
			Boolean temDepositoTransbordoRota, Boolean temDepositoTransbordoRotaConsolidacao, Integer cddepositoTransbordoManifesto, 
			Integer cddepositoTransbordoRota, Integer cddepositoTransbordoRotaConsolidacao) {
		this.cdManifestoNotaFiscal = cdManifestoNotaFiscal;
		this.numero = numero;
		this.serie = serie;
		this.dtEmissao = dtEmissao;
		this.numeroPedido = numeroPedido;
		this.lojaPedido = lojaPedido;
		this.temDepositoTransbordoManifesto = temDepositoTransbordoManifesto;
		this.cddepositoTransbordoManifesto = cddepositoTransbordoManifesto;
		this.cddepositoTransbordoRota = cddepositoTransbordoRota;
		
		if (StringUtils.isNotBlank(rotaConsolidacao)){
			this.rota = rotaConsolidacao;
		}else{
			this.rota = rota;
		}
		
		if (temDepositoTransbordoRotaConsolidacao != null){
			this.temDepositoTransbordoRota = temDepositoTransbordoRotaConsolidacao;
		}else{
			this.temDepositoTransbordoRota = temDepositoTransbordoRota;
		}
		
		
		if (cddepositoTransbordoManifesto != null){
			this.depositoTransbordo = new Deposito(cddepositoTransbordoManifesto);
		}else if (cddepositoTransbordoRotaConsolidacao != null){
			this.depositoTransbordo = new Deposito(cddepositoTransbordoRotaConsolidacao);
		}else if (cddepositoTransbordoRota != null){
			this.depositoTransbordo = new Deposito(cddepositoTransbordoRota);
		}else{
			this.depositoTransbordo = null;
		}
	}
	
	
	
	public ManifestoTransbordoVO() {}



	public Integer getCdManifestoNotaFiscal() {
		return cdManifestoNotaFiscal;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public String getSerie() {
		return serie;
	}
	
	public Date getDtEmissao() {
		return dtEmissao;
	}
	
	public String getNumeroPedido() {
		return numeroPedido;
	}
	
	public String getLojaPedido() {
		return lojaPedido;
	}
	
	public String getRota() {
		return rota;
	}
	
	public Boolean getTemDepositoTransbordoManifesto() {
		return temDepositoTransbordoManifesto;
	}
	
	public Boolean getTemDepositoTransbordoRota() {
		return temDepositoTransbordoRota;
	}
	
	public Integer getCddepositoTransbordoManifesto() {
		return cddepositoTransbordoManifesto;
	}
	
	public Integer getCddepositoTransbordoRota() {
		return cddepositoTransbordoRota;
	}
	
	public Deposito getDepositoTransbordo() {
		return depositoTransbordo;
	}
	
	public void setCdManifestoNotaFiscal(Integer cdManifestoNotaFiscal) {
		this.cdManifestoNotaFiscal = cdManifestoNotaFiscal;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public void setSerie(String serie) {
		this.serie = serie;
	}
	
	public void setDtEmissao(Date dtEmissao) {
		this.dtEmissao = dtEmissao;
	}
	
	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	
	public void setLojaPedido(String lojaPedido) {
		this.lojaPedido = lojaPedido;
	}
	
	public void setRota(String rota) {
		this.rota = rota;
	}
	
	public void setTemDepositoTransbordoManifesto(Boolean temDepositoTransbordoManifesto) {
		this.temDepositoTransbordoManifesto = temDepositoTransbordoManifesto;
	}
	
	public void setTemDepositoTransbordoRota(Boolean temDepositoTransbordoRota) {
		this.temDepositoTransbordoRota = temDepositoTransbordoRota;
	}
	
	public void setCddepositoTransbordoManifesto(Integer cddepositoTransbordoManifesto) {
		this.cddepositoTransbordoManifesto = cddepositoTransbordoManifesto;
	}
	
	public void setCddepositoTransbordoRota(Integer cddepositoTransbordoRota) {
		this.cddepositoTransbordoRota = cddepositoTransbordoRota;
	}
	
	public void setDepositoTransbordo(Deposito depositoTransbordo) {
		this.depositoTransbordo = depositoTransbordo;
	}

}
