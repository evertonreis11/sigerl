package br.com.linkcom.wms.geral.bean;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
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
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name = "sq_acompanhamentoveiculo", sequenceName = "sq_acompanhamentoveiculo")
@DisplayName("Dados do Motorista")
public class Acompanhamentoveiculo {
	
	private Integer cdacompanhamentoveiculo;
	private String numerorav;
	private String nomemotorista;
	private String cnhmotorista;
	private String primeirotelefone;
	private String segundotelefone;
	private String terceirotelefone;
	private String placaveiculo;
	private Timestamp dataentrada;
	private Timestamp datasaida;
	private Date dtgeracaorecebimento;
	private Date dtfimrecebimento;
	private String nomeusuario;
	private Acompanhamentoveiculostatus acompanhamentoveiculostatus;
	private String lacre;
	private List<Agendaacompanhamentoveiculo>  listaAgendaacompanhamentoveiculo = new ListSet<Agendaacompanhamentoveiculo>(Agendaacompanhamentoveiculo.class);
	private Recebimento recebimento;
	private Tipoveiculo tipoveiculo;
	private String numeronota;
	private Boolean temDevolucao = Boolean.FALSE;
	private Deposito deposito;
	private List<Acompanhamentoveiculohistorico> listAcompanhamentoveiculohistorico = new ArrayList<Acompanhamentoveiculohistorico>();
	private Date dtinclusao;
	private Date dtalteracao;

	//transientes
	protected Integer cdrecebimento;
	protected String box;
	protected String dataCadastro;
	protected String Senha;
	protected String itemDevolvido;
	protected String tipoVeiculoNome;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_acompanhamentoveiculo")
	public Integer getCdacompanhamentoveiculo() {
		return cdacompanhamentoveiculo;
	}
	@DisplayName("Número do RAV")
	public String getNumerorav() {
		return numerorav;
	}
	@DisplayName("Nome do motorista")
	public String getNomemotorista() {
		return nomemotorista;
	}
	@DisplayName("CNH")
	public String getCnhmotorista() {
		return cnhmotorista;
	}
	@DisplayName("Placa")
	@MaxLength(7)
	public String getPlacaveiculo() {
		return placaveiculo;
	}
	@DisplayName("Data de Entrada")
	public Timestamp getDataentrada() {
		return dataentrada;
	}
	@DisplayName("Data da saída")
	public Timestamp getDatasaida() {
		return datasaida;
	}
	@DisplayName("1º Opção")
	public String getPrimeirotelefone() {
		return primeirotelefone;
	}
	@DisplayName("2º Opção")
	public String getSegundotelefone() {
		return segundotelefone;
	}
	@DisplayName("3º Opção")
	public String getTerceirotelefone() {
		return terceirotelefone;
	}
	@OneToMany(mappedBy="acompanhamentoveiculo")
	public List<Agendaacompanhamentoveiculo> getListaAgendaacompanhamentoveiculo() {
		return listaAgendaacompanhamentoveiculo;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdrecebimento")
	public Recebimento getRecebimento() {
		return recebimento;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipoveiculo")
	@DisplayName("Tipo de Veículo")
	public Tipoveiculo getTipoveiculo() {
		return tipoveiculo;
	}
	@DisplayName("Nº de Notas")
	public String getNumeronota() {
		return numeronota;
	}
	@DisplayName("Ocorreu Devolução?")
	public Boolean getTemDevolucao() {
		return temDevolucao;
	}
	@Transient
	public String getItemDevolvido() {
		return itemDevolvido;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	@DisplayName("Déposito")
	public Deposito getDeposito() {
		return deposito;
	}
	public Date getDtgeracaorecebimento() {
		return dtgeracaorecebimento;
	}
	public Date getDtfimrecebimento() {
		return dtfimrecebimento;
	}
	public String getNomeusuario() {
		return nomeusuario;
	}
	@DisplayName("Status")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdstatusrav")
	public Acompanhamentoveiculostatus getAcompanhamentoveiculostatus(){
		return acompanhamentoveiculostatus;
	}
	public String getLacre() {
		return lacre;
	}
	@Transient
	public Integer getCdrecebimento() {
		return cdrecebimento;
	}
	@Transient
	public String getBox() {
		return box;
	}
	@Transient
	public String getDataCadastro() {
		return dataCadastro;
	}
	@Transient
	public String getSenha() {
		return Senha;
	}
	@Transient
	public String getTipoVeiculoNome() {
		return tipoVeiculoNome;
	}
	@OneToMany(mappedBy="acompanhamentoveiculo")
	public List<Acompanhamentoveiculohistorico> getListAcompanhamentoveiculohistorico() {
		return listAcompanhamentoveiculohistorico;
	}
	@Column(name="dt_inclusao")
	public Date getDtinclusao() {
		return dtinclusao;
	}
	@Column(name="dt_alteracao")
	public Date getDtalteracao() {
		return dtalteracao;
	}
	
	
	//Set's
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}
	public void setCdacompanhamentoveiculo(Integer cdacompanhamentoveiculo) {
		this.cdacompanhamentoveiculo = cdacompanhamentoveiculo;
	}
	public void setNumerorav(String numerorav) {
		this.numerorav = numerorav;
	}
	public void setNomemotorista(String nomemotorista) {
		this.nomemotorista = nomemotorista;
	}
	public void setCnhmotorista(String cnhmotorista) {
		this.cnhmotorista = cnhmotorista;
	}
	public void setPlacaveiculo(String placaveiculo) {
		this.placaveiculo = placaveiculo;
	}
	public void setDataentrada(Timestamp dataentrada) {
		this.dataentrada = dataentrada;
	}
	public void setDatasaida(Timestamp datasaida) {
		this.datasaida = datasaida;
	}
	public void setDtgeracaorecebimento(Date dtgeracaorecebimento) {
		this.dtgeracaorecebimento = dtgeracaorecebimento;
	}
	public void setDtfimrecebimento(Date dtfimrecebimento) {
		this.dtfimrecebimento = dtfimrecebimento;
	}
	public void setNomeusuario(String nomeusuario) {
		this.nomeusuario = nomeusuario;
	}
	public void setAcompanhamentoveiculostatus(Acompanhamentoveiculostatus acompanhamentoveiculostatus) {
		this.acompanhamentoveiculostatus = acompanhamentoveiculostatus;
	}
	public void setLacre(String lacre) {
		this.lacre = lacre;
	}
	public void setPrimeirotelefone(String primeirotelefone) {
		this.primeirotelefone = primeirotelefone;
	}
	public void setSegundotelefone(String segundotelefone) {
		this.segundotelefone = segundotelefone;
	}
	public void setTerceirotelefone(String terceirotelefone) {
		this.terceirotelefone = terceirotelefone;
	}
	public void setListaAgendaacompanhamentoveiculo(List<Agendaacompanhamentoveiculo> listaAgendaacompanhamentoveiculo) {
		this.listaAgendaacompanhamentoveiculo = listaAgendaacompanhamentoveiculo;
	}
	public void setCdrecebimento(Integer cdrecebimento) {
		this.cdrecebimento = cdrecebimento;
	}
	public void setBox(String box) {
		this.box = box;
	}
	public void setDataCadastro(String dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	public void setSenha(String senha) {
		Senha = senha;
	}
	public void setTipoveiculo(Tipoveiculo tipoveiculo) {
		this.tipoveiculo = tipoveiculo;
	}
	public void setNumeronota(String numeronota) {
		this.numeronota = numeronota;
	}
	public void setTemDevolucao(Boolean temDevolucao) {
		this.temDevolucao = temDevolucao;
	}
	public void setItemDevolvido(String itemDevolvido) {
		this.itemDevolvido = itemDevolvido;
	}
	public void setTipoVeiculoNome(String tipoVeiculoNome) {
		this.tipoVeiculoNome = tipoVeiculoNome;
	}
	public void setListAcompanhamentoveiculohistorico(List<Acompanhamentoveiculohistorico> listAcompanhamentoveiculohistorico) {
		this.listAcompanhamentoveiculohistorico = listAcompanhamentoveiculohistorico;
	}
	public void setDtinclusao(Date dtinclusao) {
		this.dtinclusao = dtinclusao;
	}
	public void setDtalteracao(Date dtalteracao) {
		this.dtalteracao = dtalteracao;
	}
	
}