package br.com.linkcom.wms.geral.bean;

import java.sql.Date;
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
import br.com.linkcom.neo.types.Cep;
import br.com.linkcom.neo.types.Money;

@Entity
@SequenceGenerator(name = "sq_notafiscalsaida", sequenceName = "sq_notafiscalsaida")
public class Notafiscalsaida {
	
    private Integer cdnotafiscalsaida;
    private Deposito deposito;
    private Cliente cliente;
    private Integer codigoerp;
    private String numero;
    private String chavenfe; 
    private Boolean ativo;
    private Date dtemissao;
    private Date dt_inclusao;
    private Date dt_alteracao;
    private List<Notafiscalsaidaproduto> listNotafiscalsaidaproduto;
    private Money vlrtotalnf;
    private Integer qtdeitens;
    private String serie;
    private Pedidovenda pedidovenda;
    private Boolean vinculado = Boolean.FALSE;
    private Notafiscaltipo notafiscaltipo;
    private Praca praca;
    private Deposito depositoDestino;
    private String numeropedido;
    private String lojapedido;
    private String cdcargaerp;
    private Carregamento carregamento;
    private Cliente filialfaturamento;
    private Pessoaendereco pessoaendereco;
    private Cep cep;
    private Integer numeroLojaRetirada; 
    private Tipovenda tipovenda;
    
    //Transient's
	private Rota rota = null;
    
    
	//Get's    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_notafiscalsaida")
	public Integer getCdnotafiscalsaida() {
    	return cdnotafiscalsaida;
    }
    public String getCdcargaerp() {
		return cdcargaerp;
	}
	
	@DisplayName("Depósito")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeposito")
    public Deposito getDeposito() {
		return deposito;
	}
    @DisplayName("Cliente")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdcliente")
	public Cliente getCliente() {
		return cliente;
	}
    @DisplayName("Pedido Venda")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpedidovenda")
	public Pedidovenda getPedidovenda() {
		return pedidovenda;
	}
	public Integer getCodigoerp() {
		return codigoerp;
	}
	@DisplayName("Número")
	public String getNumero() {
		return numero;
	}
	public String getChavenfe() {
		return chavenfe;
	}
	public Date getDtemissao() {
		return dtemissao;
	}
	public Money getVlrtotalnf() {
		return vlrtotalnf;
	}
	public Integer getQtdeitens() {
		return qtdeitens;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	@DisplayName("Dt. de Inclusão")
	public Date getDt_inclusao() {
		return dt_inclusao;
	}
	@DisplayName("Dt. de Alteração")
	public Date getDt_alteracao() {
		return dt_alteracao;
	}
	@OneToMany(mappedBy="notafiscalsaida")
	public List<Notafiscalsaidaproduto> getListNotafiscalsaidaproduto() {
		return listNotafiscalsaidaproduto;
	}
	@DisplayName("Série")
	public String getSerie() {
		return serie;
	}
	public Boolean getVinculado() {
		return vinculado;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtiponf")
	public Notafiscaltipo getNotafiscaltipo() {
		return notafiscaltipo;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpraca")
	public Praca getPraca() {
		return praca;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddepositodestino")
	public Deposito getDepositoDestino() {
		return depositoDestino;
	}
	public String getNumeropedido() {
		return numeropedido;
	}
	public String getLojapedido() {
		return lojapedido;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdcarregamento")
	public Carregamento getCarregamento() {
		return carregamento;
	}
    @DisplayName("Filial de Faturametno")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdfilialfaturamento")
	public Cliente getFilialfaturamento() {
		return filialfaturamento;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpessoaendereco") 
    public Pessoaendereco getPessoaendereco() {
    	return pessoaendereco;
    }
	public Cep getCep() {
		return cep;
	}
	@Column(name="NRO_LOJA_RETIRADA")
	public Integer getNumeroLojaRetirada() {
		return numeroLojaRetirada;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipovenda")
	public Tipovenda getTipovenda() {
		return tipovenda;
	}
	
	
	//Set's
	public void setCdcargaerp(String cdcargaerp) {
		this.cdcargaerp = cdcargaerp;
	}
	public void setCdnotafiscalsaida(Integer cdnotafiscalsaida) {
		this.cdnotafiscalsaida = cdnotafiscalsaida;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public void setDt_inclusao(Date dtInclusao) {
		dt_inclusao = dtInclusao;
	}
	public void setDt_alteracao(Date dtAlteracao) {
		dt_alteracao = dtAlteracao;
	}
	public void setListNotafiscalsaidaproduto(List<Notafiscalsaidaproduto> listNotafiscalsaidaproduto) {
		this.listNotafiscalsaidaproduto = listNotafiscalsaidaproduto;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public void setCodigoerp(Integer codigoerp) {
		this.codigoerp = codigoerp;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public void setChavenfe(String chavenfe) {
		this.chavenfe = chavenfe;
	}
	public void setDtemissao(Date dtemissao) {
		this.dtemissao = dtemissao;
	}
	public void setVlrtotalnf(Money vlrtotalnf) {
		this.vlrtotalnf = vlrtotalnf;
	}
	public void setQtdeitens(Integer qtdeitens) {
		this.qtdeitens = qtdeitens;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public void setPedidovenda(Pedidovenda pedidovenda) {
		this.pedidovenda = pedidovenda;
	}
	public void setVinculado(Boolean vinculado) {
		this.vinculado = vinculado;
	}
	public void setNotafiscaltipo(Notafiscaltipo notafiscaltipo) {
		this.notafiscaltipo = notafiscaltipo;
	}
	public void setPraca(Praca praca) {
		this.praca = praca;
	}
	public void setDepositoDestino(Deposito depositoDestino) {
		this.depositoDestino = depositoDestino;
	}
	public void setNumeropedido(String numeropedido) {
		this.numeropedido = numeropedido;
	}
	public void setLojapedido(String lojapedido) {
		this.lojapedido = lojapedido;
	}
	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}
	public void setFilialfaturamento(Cliente filialfaturamento) {
		this.filialfaturamento = filialfaturamento;
	}
	public void setPessoaendereco(Pessoaendereco pessoaendereco) {
		this.pessoaendereco = pessoaendereco;
	}
	public void setCep(Cep cep) {
		this.cep = cep;
	}
	public void setNumeroLojaRetirada(Integer numeroLojaRetirada) {
		this.numeroLojaRetirada = numeroLojaRetirada;
	}
	public void setTipovenda(Tipovenda tipovenda) {
		this.tipovenda = tipovenda;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Notafiscalsaida){
			Notafiscalsaida nfs = (Notafiscalsaida) obj;
			if(nfs.getCdnotafiscalsaida().equals(this.getCdnotafiscalsaida())){
				return true;
			}
		}
		return false;
	}
	
	@Transient
	public Rota getRota(){
		if(praca!=null && praca.getListaRotapraca()!=null && !praca.getListaRotapraca().isEmpty()){
			List<Rotapraca> rotapraca = new ArrayList<Rotapraca>();
			rotapraca.addAll(praca.getListaRotapraca());
			if(rotapraca.get(0)!=null && rotapraca.get(0).getRota()!=null){
				rota = rotapraca.get(0).getRota();
			}
		}
		return rota;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
	}
}
