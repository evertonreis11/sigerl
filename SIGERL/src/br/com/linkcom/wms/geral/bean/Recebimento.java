package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
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

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
@Entity
@SequenceGenerator(name = "sq_recebimento", sequenceName = "sq_recebimento")
@NamedNativeQueries(value={
	@NamedNativeQuery(name="atualizar_valormedio", query="{ call  ATUALIZAR_VALORMEDIO(?) }", resultSetMapping="scalar")
})
public class Recebimento {

	protected Integer cdrecebimento;
	protected Recebimentostatus recebimentostatus;
	protected Box box;
	protected Boolean saidaliberada; 
	protected Timestamp dtrecebimento;
	protected Timestamp dtfinalizacao;
	protected Descargapreco descargapreco;
	protected Tipoveiculo tipoveiculo;
	protected Money valordescarga;
	protected Money valorreceber;
	protected String observacao;
	protected Deposito deposito;
	protected Usuario usuario;
	protected Usuario usuariofinalizacao;
	protected List<Recebimentonotafiscal> listaRecebimentoNF = new ArrayList<Recebimentonotafiscal>();
	protected List<Ordemservico> listaOrdemservico = new ArrayList<Ordemservico>();
	protected Tipoenderecamento tipoenderecamento;
	
	//Transientes
	protected String notasFiscais = "";
	protected String numeroRav = "";
	protected Integer numeroNotas = 0;
	
	// Construtor default
	public Recebimento() {
		
	}
	
	// Construtor
	public Recebimento(Integer recebimento) {
		this.cdrecebimento = recebimento;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_recebimento")
	@DescriptionProperty
	public Integer getCdrecebimento() {
		return cdrecebimento;
	}
	public void setCdrecebimento(Integer id) {
		this.cdrecebimento = id;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdrecebimentostatus")
	@Required
	public Recebimentostatus getRecebimentostatus() {
		return recebimentostatus;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdbox")
	@Required
	public Box getBox() {
		return box;
	}
	
	@Required
	@DisplayName("Data")
	public Timestamp getDtrecebimento() {
		return dtrecebimento;
	}
	
	@Required
	@DisplayName("Finalizado em")
	public Timestamp getDtfinalizacao() {
		return dtfinalizacao;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddescargapreco")
	public Descargapreco getDescargapreco() {
		return descargapreco;
	}
	
	@DisplayName("Tipo de veículo")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipoveiculo")
	public Tipoveiculo getTipoveiculo() {
		return tipoveiculo;
	}
	
	@DisplayName("Usuário responsável")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdusuario")
	public Usuario getUsuario() {
		return usuario;
	}
	
	@DisplayName("Finalizado por")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdusuariofinalizacao")
	public Usuario getUsuariofinalizacao() {
		return usuariofinalizacao;
	}
	
	public Money getValordescarga() {
		return valordescarga;
	}
	
	public Money getValorreceber() {
		return valorreceber;
	}
	
	@MaxLength(1000)
	public String getObservacao() {
		return observacao;
	}
	
	@OneToMany(mappedBy="recebimento")
	public List<Recebimentonotafiscal> getListaRecebimentoNF() {
		return listaRecebimentoNF;
	}
	
	@DisplayName("Depósito")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	public void setRecebimentostatus(Recebimentostatus recebimentostatus) {
		this.recebimentostatus = recebimentostatus;
	}
	
	@Transient
	public String getNotasFiscais() {
		return notasFiscais;
	}
	
	@DisplayName("Tipo de endereçamento")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cdtipoenderecamento")
	public Tipoenderecamento getTipoenderecamento() {
		return tipoenderecamento;
	}
	
	@OneToMany(mappedBy="recebimento")
	public List<Ordemservico> getListaOrdemservico() {
		return listaOrdemservico;
	}

	public Boolean getSaidaliberada() {
		return saidaliberada;
	}

	@Transient
	public String getNumeroRav() {
		return numeroRav;
	}
	
	@Transient
	public Integer getNumeroNotas() {
		return numeroNotas;
	}

	public void setNumeroNotas(Integer numeroNotas) {
		this.numeroNotas = numeroNotas;
	}

	public void setNumeroRav(String numeroRav) {
		this.numeroRav = numeroRav;
	}

	public void setSaidaliberada(Boolean saidaliberada) {
		this.saidaliberada = saidaliberada;
	}

	public void setBox(Box box) {
		this.box = box;
	}
	
	public void setDtrecebimento(Timestamp data) {
		this.dtrecebimento = data;
	}
	
	public void setDtfinalizacao(Timestamp dtfinalizacao) {
		this.dtfinalizacao = dtfinalizacao;
	}
	
	public void setDescargapreco(Descargapreco descargapreco) {
		this.descargapreco = descargapreco;
	}
	
	public void setTipoveiculo(Tipoveiculo tipoveiculo) {
		this.tipoveiculo = tipoveiculo;
	}
	
	public void setValordescarga(Money valordescarga) {
		this.valordescarga = valordescarga;
	}
	
	public void setValorreceber(Money valorreceber) {
		this.valorreceber = valorreceber;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public void setListaRecebimentoNF(List<Recebimentonotafiscal> listaRecebimentoNF) {
		this.listaRecebimentoNF = listaRecebimentoNF;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void setUsuariofinalizacao(Usuario usuariofinalizacao) {
		this.usuariofinalizacao = usuariofinalizacao;
	}
	
	public void setNotasFiscais(String notasFiscais) {
		this.notasFiscais = notasFiscais;
	}
	
	public void setTipoenderecamento(Tipoenderecamento tipoenderecamento) {
		this.tipoenderecamento = tipoenderecamento;
	}
	
	public void setListaOrdemservico(List<Ordemservico> listaOrdemservico) {
		this.listaOrdemservico = listaOrdemservico;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Recebimento){
			return ((Recebimento)obj).getCdrecebimento().equals(this.cdrecebimento);
		}
		return super.equals(obj);
	}
}
