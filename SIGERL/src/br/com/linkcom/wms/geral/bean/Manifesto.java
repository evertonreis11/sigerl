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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Hora;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.vo.Auditoria;
import br.com.linkcom.wms.geral.bean.vo.ManifestoTransbordoVO;

@Entity
@SequenceGenerator(name = "sq_manifesto", sequenceName = "sq_manifesto")
public class Manifesto {
	
    private Integer cdmanifesto;
    private Deposito deposito;
    private Manifestostatus manifestostatus;
    private Usuario	usuarioemissor; //cdusuarioemissor
    private Transportador transportador;
    private Motorista motorista;
    private Veiculo veiculo;
    private Usuario conferenteExpedicao;
    private Usuario conferenteInspetoria;
    private String observacao;
    private String lacrelateral;
    private String lacretraseiro;
    private Timestamp dtemissao;
    private List<Manifestonotafiscal> listaManifestonotafiscal;
    private Tipoentrega tipoentrega;
	private Timestamp dtsaidaveiculo;
	private Timestamp dtfinalizacao;
	private Integer qtdtotalnf;
	private Integer qtdtotalitensnf;
	private Money vrtotalmanifesto;
	private Money valorapagartransporte;
	private Boolean ind_confirmado = Boolean.FALSE; 
	private Boolean ind_confirmado_avulso = Boolean.FALSE;
	private List<Manifestohistorico> listaManifestohistorico;
	private List<Manifestocodigobarras> listaManifestocodigobarra;
	private Usuario usuarioliberador;
	private Usuario usuariofinalizador;
	private Double cubagem;
	private Long kminicial;
    private Long kmfinal;
    private Timestamp dtretornoveiculo;
    private Manifestofinanceiro manifestofinanceiro;
    private Box box;
    private Timestamp dtprestacaocontas;
    private Integer cdae;
    private Rotagerenciadora rotagerenciadora;
	private Date datainicio;
	private Hora horainicio;
	private Manifesto manifestopai;
	private Filial filialreferencia;
	private Date dtconfirmacaotransito;
	private Boolean cteValidado  = Boolean.FALSE;
	
	//Transientes
	private String motivo;
	private String selectCdnotafiscalsaida;
	private String selectCdmanifesto;
	private Auditoria auditoriaVO;
	private Boolean isFechamentoFinalizado;
	private Boolean isSolicitarAprovacao = Boolean.FALSE;
	private List<Manifesto> listaManifesto;
	private String codigobarras;
	private String showMsgSave;
	private Integer cdSolictacaoAcrescimo;
	private List<Notafiscalsaida> listaNotaFiscalDevolucao;
	private Boolean temNotaDevolucao = Boolean.FALSE;
	private List<Manifestonotafiscal> listaNotaFiscalSemFrete;
	private String codigoPacklist;
	private Deposito depositoDestino;
	private String selectCdImportacaoCarga;
	private Boolean existeFreteClienteNota = Boolean.FALSE;
	private String senhaAutorizacao;
	private Boolean exigeMotivo = Boolean.FALSE;	
	private Boolean temTransbordo = Boolean.FALSE;
	private List<ManifestoTransbordoVO> listaNotasTransbordo;
	
    public Manifesto(){
    }
    
    public Manifesto(Integer cdmanifesto) {
    	this.cdmanifesto = cdmanifesto;
    }
    
    public Manifesto (Integer cdManifesto, Integer cdDeposito, String nomeDeposito, Integer cdTipoEntrega,
	   String descTipoEntrega, java.util.Date dtEmissao,Integer cdManifestoStatus, String status){
    	this.cdmanifesto = cdManifesto;
    	this.dtemissao = new Timestamp(dtEmissao.getTime());
    	
    	Deposito deposito = new Deposito();
    	deposito.setCddeposito(cdDeposito);
    	deposito.setNome(nomeDeposito);
    	this.deposito = deposito;
    	
    	Tipoentrega tipoEntrega = new Tipoentrega();
    	tipoEntrega.setCdtipoentrega(cdTipoEntrega);
    	tipoEntrega.setNome(descTipoEntrega);
    	this.tipoentrega = tipoEntrega;
    	
    	Manifestostatus manifestoStatus = new Manifestostatus();
    	manifestoStatus.setCdmanifestostatus(cdManifestoStatus);
    	manifestoStatus.setNome(status);
    	this.manifestostatus = manifestoStatus;
    	
    }
    
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_manifesto")
	public Integer getCdmanifesto() {
		return cdmanifesto;
	}
    @DisplayName("Depósito")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
    @DisplayName("Status")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdmanifestostatus")
	public Manifestostatus getManifestostatus() {
		return manifestostatus;
	}
    @DisplayName("Usuário Emissor")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdusuarioemissor")
	public Usuario getUsuarioemissor() {
		return usuarioemissor;
	}
    @Required
    @DisplayName("Transportador")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtransportador")
	public Transportador getTransportador() {
		return transportador;
	}
    @DisplayName("Motorista")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdmotorista")
	public Motorista getMotorista() {
		return motorista;
	}
    @DisplayName("Veículo")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdveiculo")
	public Veiculo getVeiculo() {
		return veiculo;
	}
    @DisplayName("Conferente da Expedição")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdusuarioexpedicao")
	public Usuario getConferenteExpedicao() {
		return conferenteExpedicao;
	}
    @DisplayName("Conferente da Inspetoria")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdusuarioinspetoria")
	public Usuario getConferenteInspetoria() {
		return conferenteInspetoria;
	}	
    @DisplayName("Observação")
	public String getObservacao() {
		return observacao;
	}
	@DisplayName("Lacre Lateral")
	public String getLacrelateral() {
		return lacrelateral;
	}
	@DisplayName("Lacre Traseiro")
	public String getLacretraseiro() {
		return lacretraseiro;
	}
	@DisplayName("Dt. de Emissão")
	public Timestamp getDtemissao() {
		return dtemissao;
	}
	@OneToMany(mappedBy="manifesto")
	public List<Manifestonotafiscal> getListaManifestonotafiscal() {
		return listaManifestonotafiscal;
	}
	@Required
    @DisplayName("Tipo de entrega")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipoentrega")
    public Tipoentrega getTipoentrega() {
		return tipoentrega;
	}
    @DisplayName("Dt. Saída Veículo")
    public Timestamp getDtsaidaveiculo() {
    	return dtsaidaveiculo;
    }
	@DisplayName("Dt. de Finalização")
    public Timestamp getDtfinalizacao() {
    	return dtfinalizacao;
    }
	@DisplayName("Data de Saída do Veículo")
	public Integer getQtdtotalnf() {
		return qtdtotalnf;
	}
	@DisplayName("Data de Saída do Veículo")
	public Integer getQtdtotalitensnf() {
		return qtdtotalitensnf;
	}
    @DisplayName("Valor total Manifesto")
	public Money getVrtotalmanifesto() {
		return vrtotalmanifesto;
	}
    @DisplayName("Data de Saída do Veículo")
	public Boolean getInd_confirmado() {
		return ind_confirmado;
	}
    @DisplayName("Data de Saída do Veículo")
    public Boolean getInd_confirmado_avulso() {
    	return ind_confirmado_avulso;
    }
    @DisplayName("Data de Saída do Veículo")
	public Money getValorapagartransporte() {
		return valorapagartransporte;
	}
    @OneToMany(mappedBy="manifesto")
    public List<Manifestohistorico> getListaManifestohistorico() {
		return listaManifestohistorico;
	}
    @OneToMany(mappedBy="manifesto")
    public List<Manifestocodigobarras> getListaManifestocodigobarra() {
		return listaManifestocodigobarra;
	}
    @DisplayName("Usuário de Liberação")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdusuarioliberador")
	public Usuario getUsuarioliberador() {
		return usuarioliberador;
	}
    @DisplayName("Usuário de Finalização")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdusuariofinalizador")
	public Usuario getUsuariofinalizador() {
		return usuariofinalizador;
	}
	public Double getCubagem() {
		return cubagem;
	}
	public Long getKminicial() {
		return kminicial;
	}
	public Long getKmfinal() {
		return kmfinal;
	}
	@DisplayName("Dt. de Retorno")
	public Timestamp getDtretornoveiculo() {
		return dtretornoveiculo;
	} 
	@OneToOne(mappedBy = "manifesto", fetch=FetchType.LAZY)
	public Manifestofinanceiro getManifestofinanceiro() {
		return manifestofinanceiro;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdbox")
	public Box getBox() {
		return box;
	}
	public Timestamp getDtprestacaocontas() {
		return dtprestacaocontas;
	}
	@DisplayName("Código da AE")
	public Integer getCdae() {
		return cdae;
	}
	@DisplayName("Rota Gerenciadora")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdrotagerenciadora")
	public Rotagerenciadora getRotagerenciadora() {
		return rotagerenciadora;
	}
	public Date getDatainicio() {
		return datainicio;
	}
	public Hora getHorainicio() {
		return horainicio;
	}
	@DisplayName("Manifesto Pai")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdmanifestopai")
	public Manifesto getManifestopai() {
		return manifestopai;
	}
	@DisplayName("Filial Referência")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdfilialreferencia")
	public Filial getFilialreferencia() {
		return filialreferencia;
	}
	public Date getDtconfirmacaotransito() {
		return dtconfirmacaotransito;
	}

	
	//Set's
	public void setDtemissao(Timestamp dtemissao) {
		this.dtemissao = dtemissao;
	}
	public void setTipoentrega(Tipoentrega tipoentrega) {
		this.tipoentrega = tipoentrega;
	}
	public void setDtsaidaveiculo(Timestamp dtsaidaveiculo) {
		this.dtsaidaveiculo = dtsaidaveiculo;
	}
	public void setDtfinalizacao(Timestamp dtfinalizacao) {
		this.dtfinalizacao = dtfinalizacao;
	}
	public void setQtdtotalnf(Integer qtdtotalnf) {
		this.qtdtotalnf = qtdtotalnf;
	}
	public void setQtdtotalitensnf(Integer qtdtotalitensnf) {
		this.qtdtotalitensnf = qtdtotalitensnf;
	}
	public void setVrtotalmanifesto(Money vrtotalmanifesto) {
		this.vrtotalmanifesto = vrtotalmanifesto;
	}
	public void setValorapagartransporte(Money valorapagartransporte) {
		this.valorapagartransporte = valorapagartransporte;
	}
	public void setInd_confirmado(Boolean indConfirmado) {
		ind_confirmado = indConfirmado;
	}
	public void setInd_confirmado_avulso(Boolean indConfirmadoAvulso) {
		ind_confirmado_avulso = indConfirmadoAvulso;
	}
	public void setCdmanifesto(Integer cdmanifesto) {
		this.cdmanifesto = cdmanifesto;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setManifestostatus(Manifestostatus manifestostatus) {
		this.manifestostatus = manifestostatus;
	}
	public void setUsuarioemissor(Usuario usuarioemissor) {
		this.usuarioemissor = usuarioemissor;
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
	public void setConferenteExpedicao(Usuario conferenteExpedicao) {
		this.conferenteExpedicao = conferenteExpedicao;
	}
	public void setConferenteInspetoria(Usuario conferenteInspetoria) {
		this.conferenteInspetoria = conferenteInspetoria;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public void setLacrelateral(String lacrelateral) {
		this.lacrelateral = lacrelateral;
	}
	public void setLacretraseiro(String lacretraseiro) {
		this.lacretraseiro = lacretraseiro;
	}
	public void setListaManifestonotafiscal(List<Manifestonotafiscal> listaManifestonotafiscal) {
		this.listaManifestonotafiscal = listaManifestonotafiscal;
	}
	public void setListaManifestohistorico(List<Manifestohistorico> listaManifestohistorico) {
		this.listaManifestohistorico = listaManifestohistorico;
	}
	public void setListaManifestocodigobarra(List<Manifestocodigobarras> listaManifestocodigobarra) {
		this.listaManifestocodigobarra = listaManifestocodigobarra;
	}
	public void setUsuarioliberador(Usuario usuarioliberador) {
		this.usuarioliberador = usuarioliberador;
	}
	public void setUsuariofinalizador(Usuario usuariofinalizador) {
		this.usuariofinalizador = usuariofinalizador;
	}
	public void setCubagem(Double cubagem) {
		this.cubagem = cubagem;
	}
	public void setKminicial(Long kminicial) {
		this.kminicial = kminicial;
	}
	public void setKmfinal(Long kmfinal) {
		this.kmfinal = kmfinal;
	}
	public void setDtretornoveiculo(Timestamp dtretornoveiculo) {
		this.dtretornoveiculo = dtretornoveiculo;
	}
	public void setManifestofinanceiro(Manifestofinanceiro manifestofinanceiro) {
		this.manifestofinanceiro = manifestofinanceiro;
	}
	public void setBox(Box box) {
		this.box = box;
	}
	public void setDtprestacaocontas(Timestamp dtprestacaocontas) {
		this.dtprestacaocontas = dtprestacaocontas;
	}
	public void setCdae(Integer cdae) {
		this.cdae = cdae;
	}
	public void setRotagerenciadora(Rotagerenciadora rotagerenciadora) {
		this.rotagerenciadora = rotagerenciadora;
	}
	public void setDatainicio(Date datainicio) {
		this.datainicio = datainicio;
	}
	public void setHorainicio(Hora horainicio) {
		this.horainicio = horainicio;
	}
	public void setManifestopai(Manifesto manifestopai) {
		this.manifestopai = manifestopai;
	}
	public void setFilialreferencia(Filial filialreferencia) {
		this.filialreferencia = filialreferencia;
	}
	public void setDtconfirmacaotransito(Date dtconfirmacaotransito) {
		this.dtconfirmacaotransito = dtconfirmacaotransito;
	}
	@DisplayName("Cte Validado")
	public Boolean getCteValidado() {
		return cteValidado;
	}

	public void setCteValidado(Boolean cteValidado) {
		this.cteValidado = cteValidado;
	}

	//Transient's
	@Transient
	public String getMotivo() {
		return motivo;
	}
	@Transient
	public String getSelectCdnotafiscalsaida() {
		return selectCdnotafiscalsaida;
	}
	@Transient
	public Auditoria getAuditoriaVO() {
		return auditoriaVO;
	}
	@Transient
	public Boolean getIsFechamentoFinalizado() {
		return isFechamentoFinalizado;
	}
	@Transient
	public Boolean getIsSolicitarAprovacao() {
		return isSolicitarAprovacao;
	}
	@Transient
	public List<Manifesto> getListaManifesto() {
		return listaManifesto;
	}
	@Transient
	public String getCodigobarras() {
		return codigobarras;
	}
	@Transient
	public String getSelectCdmanifesto() {
		return selectCdmanifesto;
	}
	@Transient
	public String getShowMsgSave() {
		return showMsgSave;
	}
	@Transient
	public Integer getCdSolictacaoAcrescimo() {
		return cdSolictacaoAcrescimo;
	}
	@Transient
	public List<Manifestonotafiscal> getListaNotaFiscalSemFrete() {
		return listaNotaFiscalSemFrete;
	}
	public void setCdSolictacaoAcrescimo(Integer cdSolictacaoAcrescimo) {
		this.cdSolictacaoAcrescimo = cdSolictacaoAcrescimo;
	}
	@Transient
	public List<Notafiscalsaida> getListaNotaFiscalDevolucao() {
		return listaNotaFiscalDevolucao;
	}

	public void setListaNotaFiscalDevolucao(List<Notafiscalsaida> listaNotaFiscalDevolucao) {
		this.listaNotaFiscalDevolucao = listaNotaFiscalDevolucao;
	}

	@Transient
	public Boolean getTemNotaDevolucao() {
		return temNotaDevolucao;
	}

	public void setTemNotaDevolucao(Boolean temNotaDevolucao) {
		this.temNotaDevolucao = temNotaDevolucao;
	}

	public void setShowMsgSave(String showMsgSave) {
		this.showMsgSave = showMsgSave;
	}
	public void setSelectCdmanifesto(String selectCdmanifesto) {
		this.selectCdmanifesto = selectCdmanifesto;
	}
	public void setCodigobarras(String codigobarras) {
		this.codigobarras = codigobarras;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public void setSelectCdnotafiscalsaida(String selectCdnotafiscalsaida) {
		this.selectCdnotafiscalsaida = selectCdnotafiscalsaida;
	}
	public void setAuditoriaVO(Auditoria auditoriaVO) {
		this.auditoriaVO = auditoriaVO;
	}
	public void setIsFechamentoFinalizado(Boolean isFechamentoFinalizado) {
		this.isFechamentoFinalizado = isFechamentoFinalizado;
	}
	public void setIsSolicitarAprovacao(Boolean isSolicitarAprovacao) {
		this.isSolicitarAprovacao = isSolicitarAprovacao;
	}
	public void setListaManifesto(List<Manifesto> listaManifesto) {
		this.listaManifesto = listaManifesto;
	}
	public void setListaNotaFiscalSemFrete(List<Manifestonotafiscal> listaNotaFiscalSemFrete) {
		this.listaNotaFiscalSemFrete = listaNotaFiscalSemFrete;
	}
	
	@Transient
	public String getCodigoPacklist() {
		return codigoPacklist;
	}

	public void setCodigoPacklist(String codigoPacklist) {
		this.codigoPacklist = codigoPacklist;
	}
	@Transient
	public Deposito getDepositoDestino() {
		return depositoDestino;
	}

	public void setDepositoDestino(Deposito depositoDestino) {
		this.depositoDestino = depositoDestino;
	}
	
	@Transient
	public String getSelectCdImportacaoCarga() {
		return selectCdImportacaoCarga;
	}

	public void setSelectCdImportacaoCarga(String selectCdImportacaoCarga) {
		this.selectCdImportacaoCarga = selectCdImportacaoCarga;
	}

	@Transient
	public Boolean getExisteFreteClienteNota() {
		return existeFreteClienteNota;
	}

	public void setExisteFreteClienteNota(Boolean existeFreteClienteNota) {
		this.existeFreteClienteNota = existeFreteClienteNota;
	}
	@Transient
	public String getSenhaAutorizacao() {
		return senhaAutorizacao;
	}

	public void setSenhaAutorizacao(String senhaAutorizacao) {
		this.senhaAutorizacao = senhaAutorizacao;
	}
	@Transient
	public Boolean getExigeMotivo() {
		return exigeMotivo;
	}

	public void setExigeMotivo(Boolean exigeMotivo) {
		this.exigeMotivo = exigeMotivo;
	}
	
	@Transient
	public Boolean getTemTransbordo() {
		return temTransbordo;
	}

	public void setTemTransbordo(Boolean temTransbordo) {
		this.temTransbordo = temTransbordo;
	}
	
	@Transient
	public List<ManifestoTransbordoVO> getListaNotasTransbordo() {
		return listaNotasTransbordo;
	}

	public void setListaNotasTransbordo(List<ManifestoTransbordoVO> listaNotasTransbordo) {
		this.listaNotasTransbordo = listaNotasTransbordo;
	}
	
}