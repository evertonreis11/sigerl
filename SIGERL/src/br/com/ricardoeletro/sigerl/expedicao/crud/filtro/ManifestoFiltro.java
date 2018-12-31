package br.com.ricardoeletro.sigerl.expedicao.crud.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Manifestostatus;
import br.com.linkcom.wms.geral.bean.Motorista;
import br.com.linkcom.wms.geral.bean.Statusconfirmacaoentrega;
import br.com.linkcom.wms.geral.bean.Tipoentrega;
import br.com.linkcom.wms.geral.bean.Tiponotafiscal;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Veiculo;

public class ManifestoFiltro extends FiltroListagem{

	private Deposito deposito;
	private Long numeronota;
	private Date dtemissaoinicio;
	private Date dtemissaofim;
	private Usuario usuario; 
	private Transportador transportador;
	private Motorista motorista;
	private Veiculo veiculo;
	private Boolean defaultValues = Boolean.TRUE;
	private String motivo;
	private String whereIn;
	private Integer cdcarregamento;
	private String chavenfe;
	private String nroNotaSaida;
	private Date dtemissaoNotaSaida;
	private String codigo;
	private Tipoentrega tipoentrega;
    private String lacrelateral;
    private String lacretraseiro;
    private Integer cdmanifesto;
    private Cliente filial;
    private String serieNota;
    private Long kminicial;
    private Long kmfinal;
    private Manifestostatus manifestostatus;
    private String cdcargaerp;
    private Tiponotafiscal tiponotafiscal;  
    private String codigosImportacaoCarga;
    
    //Cupom Fiscal
    private Cliente filialCupom;
	private Date dtEmissaoCupomInicio;
	private Date dtEmissaoCupomFim;
	private String numeroCupom;
	private String numeroNotaCupom;
	private String idNotasSelecionadas;
	private String cuponsSelecionados;
	private Deposito depositoSelecionado;
	
	private Money vrtotalmanifesto;
	
	private Statusconfirmacaoentrega statusconfirmacaoentrega;
	private Integer cdtipovenda;
    
    
	//Get's
	@Required
	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}
	@DisplayName("Número Nota")
	public Long getNumeronota() {
		return numeronota;
	}
	public Date getDtemissaoinicio() {
		return dtemissaoinicio;
	}
	public String getCdcargaerp() {
		return cdcargaerp;
	}
	public Date getDtemissaofim() {
		return dtemissaofim;
	}
	@DisplayName("Usuário Emissor")
	public Usuario getUsuario() {
		return usuario;
	}
	public Transportador getTransportador() {
		return transportador;
	}
	public Motorista getMotorista() {
		return motorista;
	}
	@DisplayName("Placa do Veículo")
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public Boolean getDefaultValues() {
		return defaultValues;
	}
	public String getMotivo() {
		return motivo;
	}
	public String getWhereIn() {
		return whereIn;
	}
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	public String getChavenfe() {
		return chavenfe;
	}
	public String getNroNotaSaida() {
		return nroNotaSaida;
	}
	public Date getDtemissaoNotaSaida() {
		return dtemissaoNotaSaida;
	}
	public String getCodigo() {
		return codigo;
	}
	public Tipoentrega getTipoentrega() {
		return tipoentrega;
	}
	public String getLacrelateral() {
		return lacrelateral;
	}
	public String getLacretraseiro() {
		return lacretraseiro;
	}
	public Integer getCdmanifesto() {
		return cdmanifesto;
	}
	public Cliente getFilial() {
		return filial;
	}
	public String getSerieNota() {
		return serieNota;
	}
	public Long getKminicial() {
		return kminicial;
	}
	public Long getKmfinal() {
		return kmfinal;
	}
	public Manifestostatus getManifestostatus() {
		return manifestostatus;
	}
	public Date getDtEmissaoCupomInicio() {
		return dtEmissaoCupomInicio;
	}
	public Date getDtEmissaoCupomFim() {
		return dtEmissaoCupomFim;
	}
	public String getNumeroCupom() {
		return numeroCupom;
	}
	public String getIdNotasSelecionadas() {
		return idNotasSelecionadas;
	}
	public Cliente getFilialCupom() {
		return filialCupom;
	}
	public String getNumeroNotaCupom() {
		return numeroNotaCupom;
	}
	public String getCuponsSelecionados() {
		return cuponsSelecionados;
	}
	public Tiponotafiscal getTiponotafiscal() {
		return tiponotafiscal;
	}
	public Deposito getDepositoSelecionado() {
		return depositoSelecionado;
	}
	public Statusconfirmacaoentrega getStatusconfirmacaoentrega() {
		return statusconfirmacaoentrega;
	}
	public Integer getCdtipovenda() {
		return cdtipovenda;
	}
	public String getCodigosImportacaoCarga() {
		return codigosImportacaoCarga;
	}
	
	
	//Set's
	public void setNumeroCupom(String numeroCupom) {
		this.numeroCupom = numeroCupom;
	}
	public void setCdcargaerp(String cdcargaerp) {
		this.cdcargaerp = cdcargaerp;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setNumeronota(Long numeronota) {
		this.numeronota = numeronota;
	}
	public void setDtemissaoinicio(Date dtemissaoinicio) {
		this.dtemissaoinicio = dtemissaoinicio;
	}
	public void setDtemissaofim(Date dtemissaofim) {
		this.dtemissaofim = dtemissaofim;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public void setWhereIn(String whereIn) {
		this.whereIn = whereIn;
	}
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}
	public void setChavenfe(String chavenfe) {
		this.chavenfe = chavenfe;
	}
	public void setNroNotaSaida(String nroNotaSaida) {
		this.nroNotaSaida = nroNotaSaida;
	}
	public void setDtemissaoNotaSaida(Date dtemissaoNotaSaida) {
		this.dtemissaoNotaSaida = dtemissaoNotaSaida;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
 	public void setTipoentrega(Tipoentrega tipoentrega) {
		this.tipoentrega = tipoentrega;
	}
	public void setLacrelateral(String lacrelateral) {
		this.lacrelateral = lacrelateral;
	}
	public void setLacretraseiro(String lacretraseiro) {
		this.lacretraseiro = lacretraseiro;
	}
	public void setCdmanifesto(Integer cdmanifesto) {
		this.cdmanifesto = cdmanifesto;
	}
	public void setDefaultValues(Boolean defaultValues) {
		this.defaultValues = defaultValues;
	}
	public void setFilial(Cliente filial) {
		this.filial = filial;
	}
	public void setSerieNota(String serieNota) {
		this.serieNota = serieNota;
	}
	public void setKminicial(Long kminicial) {
		this.kminicial = kminicial;
	}
	public void setKmfinal(Long kmfinal) {
		this.kmfinal = kmfinal;
	}
	public void setManifestostatus(Manifestostatus manifestostatus) {
		this.manifestostatus = manifestostatus;
	}
	public void setIdNotasSelecionadas(String idNotasSelecionadas) {
		this.idNotasSelecionadas = idNotasSelecionadas;
	}
	public void setFilialCupom(Cliente filialCupom) {
		this.filialCupom = filialCupom;
	}
	public void setDtEmissaoCupomInicio(Date dtEmissaoCupomInicio) {
		this.dtEmissaoCupomInicio = dtEmissaoCupomInicio;
	}
	public void setDtEmissaoCupomFim(Date dtEmissaoCupomFim) {
		this.dtEmissaoCupomFim = dtEmissaoCupomFim;
	}
	public void setNumeroNotaCupom(String numeroNotaCupom) {
		this.numeroNotaCupom = numeroNotaCupom;
	}
	public void setCuponsSelecionados(String cuponsSelecionados) {
		this.cuponsSelecionados = cuponsSelecionados;
	}
	public void setTiponotafiscal(Tiponotafiscal tiponotafiscal) {
		this.tiponotafiscal = tiponotafiscal;
	}
	public void setDepositoSelecionado(Deposito depositoSelecionado) {
		this.depositoSelecionado = depositoSelecionado;
	}
	public Money getVrtotalmanifesto() {
		return vrtotalmanifesto;
	}
	public void setVrtotalmanifesto(Money vrtotalmanifesto) {
		this.vrtotalmanifesto = vrtotalmanifesto;
	}
	public void setStatusconfirmacaoentrega(Statusconfirmacaoentrega statusconfirmacaoentrega) {
		this.statusconfirmacaoentrega = statusconfirmacaoentrega;
	}
	public void setCdtipovenda(Integer cdtipovenda) {
		this.cdtipovenda = cdtipovenda;
	}
	public void setCodigosImportacaoCarga(String codigosImportacaoCarga) {
		this.codigosImportacaoCarga = codigosImportacaoCarga;
	}

	
}