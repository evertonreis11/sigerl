package br.com.linkcom.wms.geral.bean.vo;

import javax.persistence.Id;


public class ExtratoFinanceiroVO {

	private String cdmanifesto;
	private String cliente;	
	private String dtemissao;	
	private String dtentrega;
	private String dtfechamento;	
	private String dtprestacao;	
	private String entregasexcluida;
	private String entregasprevistas;
	private String entregasrealizadas;
	private String entregasretorno;	
	private String lojapedido;	
	private String numeronota;	
	private String numeropedido;	
	private String statusentrega;	
	private String rotapraca;	
	private String transportador;	
	private String valoradicional;	
	private String valorapagar;	
	private String valorentregas;	
	private String valortotal;
	private String kminicial;
	private String kmfinal;
	private String entregaRotaConfirmada;
	private String entregaRotaRetorno;
	private String entregaRotaExcluidas;
	private String entregaRotaPrevista;
	private String motorista;
	private String veiculo;
	
	
	//Get's
	@Id
	public String getCdmanifesto() {
		return cdmanifesto;
	}
	public String getTransportador() {
		return transportador;
	}
	public String getDtemissao() {
		return dtemissao;
	}
	public String getDtprestacao() {
		return dtprestacao;
	}
	public String getDtfechamento() {
		return dtfechamento;
	}
	public String getEntregasexcluida() {
		return entregasexcluida;
	}
	public String getValorentregas() {
		return valorentregas;
	}
	public String getValoradicional() {
		return valoradicional;
	}
	public String getValortotal() {
		return valortotal;
	}
	public String getNumeronota() {
		return numeronota;
	}
	public String getNumeropedido() {
		return numeropedido;
	}
	public String getLojapedido() {
		return lojapedido;
	}
	public String getCliente() {
		return cliente;
	}
	public String getStatusentrega() {
		return statusentrega;
	}
	public String getRotapraca() {
		return rotapraca;
	}
	public String getValorapagar() {
		return valorapagar;
	}
	public String getDtentrega() {
		return dtentrega;
	}
	public String getEntregasprevistas() {
		return entregasprevistas;
	}
	public String getEntregasrealizadas() {
		return entregasrealizadas;
	}
	public String getEntregasretorno() {
		return entregasretorno;
	}
	public String getKminicial() {
		return kminicial;
	}
	public String getKmfinal() {
		return kmfinal;
	}
	public String getEntregaRotaConfirmada() {
		return entregaRotaConfirmada;
	}
	public String getEntregaRotaRetorno() {
		return entregaRotaRetorno;
	}
	public String getEntregaRotaExcluidas() {
		return entregaRotaExcluidas;
	}
	public String getEntregaRotaPrevista() {
		return entregaRotaPrevista;
	}
	public String getMotorista() {
		return motorista;
	}
	public String getVeiculo() {
		return veiculo;
	}
	
	
	//Set'a
	public void setCdmanifesto(String cdmanifesto) {
		this.cdmanifesto = cdmanifesto;
	}
	public void setTransportador(String transportador) {
		this.transportador = transportador;
	}
	public void setDtemissao(String dtemissao) {
		this.dtemissao = dtemissao;
	}
	public void setDtprestacao(String dtprestacao) {
		this.dtprestacao = dtprestacao;
	}
	public void setDtfechamento(String dtfechamento) {
		this.dtfechamento = dtfechamento;
	}
	public void setValorentregas(String valorentregas) {
		this.valorentregas = valorentregas;
	}
	public void setValoradicional(String valoradicional) {
		this.valoradicional = valoradicional;
	}
	public void setValortotal(String valortotal) {
		this.valortotal = valortotal;
	}
	public void setNumeronota(String numeronota) {
		this.numeronota = numeronota;
	}
	public void setNumeropedido(String numeropedido) {
		this.numeropedido = numeropedido;
	}
	public void setLojapedido(String lojapedido) {
		this.lojapedido = lojapedido;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public void setStatusentrega(String statusentrega) {
		this.statusentrega = statusentrega;
	}
	public void setRotapraca(String rotapraca) {
		this.rotapraca = rotapraca;
	}
	public void setValorapagar(String valorapagar) {
		this.valorapagar = valorapagar;
	}
	public void setDtentrega(String dtentrega) {
		this.dtentrega = dtentrega;
	}
	public void setEntregasexcluida(String entregasexcluida) {
		this.entregasexcluida = entregasexcluida;
	}
	public void setEntregasprevistas(String entregasprevistas) {
		this.entregasprevistas = entregasprevistas;
	}
	public void setEntregasretorno(String entregasretorno) {
		this.entregasretorno = entregasretorno;
	}
	public void setEntregasrealizadas(String entregasrealizadas) {
		this.entregasrealizadas = entregasrealizadas;
	}
	public void setKminicial(String kminicial) {
		this.kminicial = kminicial;
	}
	public void setKmfinal(String kmfinal) {
		this.kmfinal = kmfinal;
	}
	public void setEntregaRotaConfirmada(String entregaRotaConfirmada) {
		this.entregaRotaConfirmada = entregaRotaConfirmada;
	}
	public void setEntregaRotaRetorno(String entregaRotaRetorno) {
		this.entregaRotaRetorno = entregaRotaRetorno;
	}
	public void setEntregaRotaExcluidas(String entregaRotaExcluidas) {
		this.entregaRotaExcluidas = entregaRotaExcluidas;
	}
	public void setEntregaRotaPrevista(String entregaRotaPrevista) {
		this.entregaRotaPrevista = entregaRotaPrevista;
	}
	public void setMotorista(String motorista) {
		this.motorista = motorista;
	}
	public void setVeiculo(String veiculo) {
		this.veiculo = veiculo;
	}

}
